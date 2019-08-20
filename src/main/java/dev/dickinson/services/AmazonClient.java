package dev.dickinson.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
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
		this.s3client = new AmazonS3Client(credentials);
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

	public String deleteFileFromS3Bucket(String fileUrl) {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
		return "Successfully deleted";
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

	public String listAllSprints() {
		System.out.println("listAllSprints called in AmazonClient");

		ObjectListing ol = s3client.listObjects(bucketName);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();
		Set<S3ObjectSummary> sprints = new HashSet<S3ObjectSummary>();
		
		for (S3ObjectSummary s : objects) {
			String temp = s.getKey().replaceAll("[^/]", "");
			if(temp.length() == 2) {
				sprints.add(s);
			}
		}

		return sprints.toString();
	}
	
	public String listAllSprintsByProject(String projectName) {
		System.out.println("listAllSprintsByProject called in AmazonClient");
		String reportName = "report";

		ObjectListing ol = s3client.listObjects(bucketName);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();
		Set<S3ObjectSummary> sprints = new HashSet<S3ObjectSummary>();
		
		for (S3ObjectSummary s : objects) {
			String temp = s.getKey().replaceAll("[^/]", "");
			if(s.getKey().startsWith(projectName) && temp.length() == 2) {
				sprints.add(s);
			}
		}

		return sprints.toString();
	}
	
	public String listAllSprintFiles() {
		System.out.println("listAllSprintFiles called in AmazonClient");

		ObjectListing ol = s3client.listObjects(bucketName);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();

		for (S3ObjectSummary s : objects) {
			System.out.println("One object summary: " + s);
		}

		return objects.toString();
	}

	public String listSprintFilesByProject(String projectName) {
		System.out.println("listSprintFilesByProject called in AmazonClient");

		ObjectListing ol = s3client.listObjects(bucketName, projectName + "/");
		List<S3ObjectSummary> objects = ol.getObjectSummaries();

		for (S3ObjectSummary s : objects) {
			System.out.println("One object summary: " + s);
		}

		return objects.toString();
	}

	
	
	public String listSprintFilesByFileName(String fileName) {
		//!!! Needs Implementation.
		
		
		System.out.println("listSprintFilesByFileName called in AmazonClient");

		ObjectListing ol = s3client.listObjects(bucketName);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();

		List<S3ObjectSummary> filteredObjects = objects.stream()
				.filter(o -> o.getKey().contains("derserserterstferl.txt")).collect(Collectors.toList());

		for (S3ObjectSummary s : filteredObjects) {
			System.out.println("One object summary: " + s);
		}

		return filteredObjects.toString();
	}

	public void downloadFile(String folderPath, String fileName) {
		String bucketName = "metricbuckettest";

		try {
			S3Object o = s3client.getObject(new GetObjectRequest(bucketName, folderPath + "/" + fileName));
			S3ObjectInputStream s3is = o.getObjectContent();
			// Save the file to the Desktop
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			byte[] read_buf = new byte[1024];
			int read_len = 0;
			while ((read_len = s3is.read(read_buf)) > 0) {
				fos.write(read_buf, 0, read_len);
			}

			s3is.close();
			fos.close();
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	public void createProject(String projectName) {
		try {

			InputStream input = new ByteArrayInputStream(new byte[0]);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(0);

			s3client.putObject(new PutObjectRequest(bucketName, projectName+"/", input, metadata));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createSprint(String projectName, String sprintName) {
		String reportName = "report";

		try {

			InputStream input = new ByteArrayInputStream(new byte[0]);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(0);

			s3client.putObject(new PutObjectRequest(bucketName, projectName+"/"+sprintName+"/", input, metadata));
			
			s3client.putObject(new PutObjectRequest(bucketName, projectName+"/"+sprintName+"/"+reportName+"/", input, metadata));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String uploadReportFile(String projectName, String sprintName, MultipartFile multipartFile) {
		final String reportName = "report";

		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			fileUrl = projectName+"/"+sprintName+"/"+reportName+"/" + fileName;
			uploadFileTos3bucket(fileUrl, file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(fileUrl);
		return fileUrl;
	}

	public void uploadMultipleFiles(String projectName, String sprintName, MultipartFile[] file) {
		System.out.println("uploadMultipleFiles called from AmazonClient");
		
		for(MultipartFile f: file) {
			uploadReportFile(projectName,sprintName,f);
		}
	}
	
}
