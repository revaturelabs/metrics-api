package com.revature.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;
import com.google.gson.Gson;

@Service
public class AmazonClient {

	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	private final String reportName = "report";

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials, new ClientConfiguration().withSignerOverride("AWSS3V4SignerType"));
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public String uploadFile(MultipartFile multipartFile) {

		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
			uploadFileTos3bucket(fileName, file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	public String getCredentials() {
		AWSSecurityTokenServiceClient sts_client = (AWSSecurityTokenServiceClient) AWSSecurityTokenServiceClientBuilder
				.standard().build();
		GetSessionTokenRequest session_token_request = new GetSessionTokenRequest();
		session_token_request.setDurationSeconds(7200); // optional.
		GetSessionTokenResult session_token_result = sts_client.getSessionToken(session_token_request);
		Credentials session_creds = session_token_result.getCredentials();
		Gson gson = new Gson();
		String returnData = "{\"arr\":[" + gson.toJson(session_creds) + ",";
		returnData += gson.toJson(session_token_request) + "]}";
		return returnData;
	}

	//returns a String of all just the sprint folder names.
	public String listAllSprints() {

		ObjectListing ol = s3client.listObjects(bucketName);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();
		Set<S3ObjectSummary> sprints = new HashSet<S3ObjectSummary>();

		for (S3ObjectSummary s : objects) {
			String temp = s.getKey().replaceAll("[^/]", "");
			if (temp.length() == 2) {
				sprints.add(s);
			}
		}

		return sprints.toString();
	}

	//returns a String of all the sprint folder names inside of the provided project
	public String listAllSprintsByProject(String projectName) {

		ObjectListing ol = s3client.listObjects(bucketName);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();
		Set<S3ObjectSummary> sprints = new HashSet<S3ObjectSummary>();

		for (S3ObjectSummary s : objects) {
			String temp = s.getKey().replaceAll("[^/]", "");
			if (s.getKey().startsWith(projectName) && temp.length() == 2) {
				sprints.add(s);
			}
		}

		return sprints.toString();
	}

	//returns a String of all the file names contained within a sprint
	public String listAllFilesBySprint(String projectName, String sprintName) {

		ObjectListing ol = s3client.listObjects(bucketName);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();
		Set<S3ObjectSummary> sprints = new HashSet<S3ObjectSummary>();

		for (S3ObjectSummary s : objects) {
			String temp = s.getKey().replaceAll("[^/]", "");
			if (s.getKey().equals(projectName + "/" + sprintName + "/report/")) {
				// Do nothing, this is the report file
			} else if (s.getKey().startsWith(projectName + "/" + sprintName + "/report/") && temp.length() == 3) {
				sprints.add(s);
			}
		}

		return sprints.toString();
	}

	//returns a String of all the files in a project.
	public String listSprintFilesByProject(String projectName) {

		ObjectListing ol = s3client.listObjects(bucketName, projectName + "/");
		List<S3ObjectSummary> objects = ol.getObjectSummaries();

		for (S3ObjectSummary s : objects) {
			System.out.println("One object summary: " + s);
		}

		return objects.toString();
	}

	//creates a project folder with nothing in it.
	public void createProject(String projectName) {
		try {

			InputStream input = new ByteArrayInputStream(new byte[0]);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(0);

			s3client.putObject(new PutObjectRequest(bucketName, projectName + "/", input, metadata));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Creates a sprint folder inside an existing project and then creates a report folder inside of that sprint folder
	public void createSprint(String projectName, String sprintName) {
		String reportName = "report";

		try {

			InputStream input = new ByteArrayInputStream(new byte[0]);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(0);

			s3client.putObject(new PutObjectRequest(bucketName, projectName + "/" + sprintName + "/", input, metadata));

			s3client.putObject(new PutObjectRequest(bucketName, projectName + "/" + sprintName + "/" + reportName + "/",
					input, metadata));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//uploads a report file to an existing sprint's report folder.
	public String uploadReportFile(String projectName, String sprintName, MultipartFile multipartFile) {
		final String reportName = "report";

		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = projectName + "/" + sprintName + "/" + reportName + "/" + fileName;
			uploadFileTos3bucket(fileUrl, file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(fileUrl);
		return fileUrl;
	}

	//Uploads multiple files to a sprint report folder.
	public void uploadMultipleFiles(String projectName, String sprintName, MultipartFile[] file) {

		for (MultipartFile f : file) {
			uploadReportFile(projectName, sprintName, f);
		}
	}

	//Deletes a project and everything in it.
	public void deleteProject(String projectName) {
		ObjectListing ol = s3client.listObjects(bucketName);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();

		for (S3ObjectSummary s : objects) {
			if (s.getKey().startsWith(projectName + "/")) {
				s3client.deleteObject(new DeleteObjectRequest(bucketName, s.getKey()));
			}
		}

	}

	//Deletes a sprint folder and everything in it.
	public void deleteSprint(String projectName, String sprintName) {
		ObjectListing ol = s3client.listObjects(bucketName);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();

		for (S3ObjectSummary s : objects) {
			if (s.getKey().startsWith(projectName + "/" + sprintName + "/")) {
				s3client.deleteObject(new DeleteObjectRequest(bucketName, s.getKey()));
			}
		}
	}

	//returns the number of sprint folders in a project.
	public int countSprints(String projectName) {
		int count = 0;

		ObjectListing ol = s3client.listObjects(bucketName);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();

		for (S3ObjectSummary s : objects) {
			String temp = s.getKey().replaceAll("[^/]", "");
			if (s.getKey().startsWith(projectName) && temp.length() == 2) {
				count++;
			}
		}

		return count;
	}

	//returns the number of files in a sprint folder.
	public int countSprintFiles(String projectName, String sprintName) {
		int count = 0;

		ObjectListing ol = s3client.listObjects(bucketName);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();

		for (S3ObjectSummary s : objects) {
			String temp = s.getKey().replaceAll("[^/]", "");

			if (s.getKey().equals(projectName + "/" + sprintName + "/report/")) {
				// Do nothing, this is the report file
			} else if (s.getKey().startsWith(projectName + "/" + sprintName + "/report/") && temp.length() == 3) {
				count++;
			}
		}

		return count;
	}

	//deletes a specified file within the provided project and sprint folders.
	public String deleteFileFromS3Bucket(String projectName, String sprintName, String fileName) {
		s3client.deleteObject(
				new DeleteObjectRequest(bucketName, projectName + "/" + sprintName + "/report/" + fileName));
		return "Successfully deleted";
	}

	//Returns a byte array representing the file to download.
	//Could be improved by returning a link to download the file directly from the S3 client-side instead.
	public byte[] downloadFile(String projectName, String sprintName, String fileName) {
        File newFile = new File(fileName);
        try {
            S3Object o = s3client.getObject(
                    new GetObjectRequest(bucketName, projectName + "/" + sprintName + "/report/" + fileName));
            S3ObjectInputStream s3is = o.getObjectContent();
            System.out.println(o);
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
            return read_buf;
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
	
	
}
