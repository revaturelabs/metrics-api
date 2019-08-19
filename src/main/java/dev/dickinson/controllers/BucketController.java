package dev.dickinson.controllers;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.dickinson.services.AmazonClient;

@RestController
//@RequestMapping("/storage/")
public class BucketController {

	@Autowired
    private AmazonClient amazonClient;

    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }
    
    @RequestMapping(value="/helloWerld", method=RequestMethod.GET)
    public String helloWerld() {
    	System.out.println("HELLO WERLD");
    	return "HELLO WERLD";
    }

    @RequestMapping(value="/uploadFile", method=RequestMethod.POST)
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
    	System.out.println("UPLOAD FILE CALLED");
        return this.amazonClient.uploadFile(file);
    }

    @RequestMapping(value="/deleteFile", method=RequestMethod.DELETE)
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
    	System.out.println("DELETE FILE CALLED");
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }
    
    @CrossOrigin
    @RequestMapping(value="/tokens", method=RequestMethod.GET)
    public String getCreds() {
        System.out.println("GET TOKEN CALLED");
        //TODO: if the region changes this needs to be updated.
        return this.amazonClient.getCredentials();

    }
    
    @RequestMapping(value="/listAllSprintFiles", method=RequestMethod.GET)
    public String listBucketFiles() {
    	System.out.println("listBucketFiles called in controller.");
    	return this.amazonClient.listAllSprintFiles();
    }
    
    @RequestMapping(value="/listSprintFilesByProject",method=RequestMethod.GET)
    public String listSprintFilesByProject(@RequestPart(value="projectName") String projectName) {
    	System.out.println("listSprintFilesByProject called in controller");
    	return this.amazonClient.listSprintFilesByProject(projectName);
    }
    
    @RequestMapping(value="/listSprintFilesByFileName",method=RequestMethod.GET)
    public String listSprintFilesByFileName(@RequestPart(value="fileName") String fileName) {
    	System.out.println("listSprintFilesByFileName called in controller");
    	return this.amazonClient.listSprintFilesByFileName(fileName);
    }
    
    @RequestMapping(value="/downloadFile",method=RequestMethod.GET)
    public void downloadFile(@RequestPart(value="folderPath") String folderPath, @RequestPart(value="fileName") String fileName) {
    	System.out.println("downloadFile called in controller");
    	this.amazonClient.downloadFile(folderPath, fileName);
    }
    
    @RequestMapping(value="createProject",method=RequestMethod.POST)
    public void createProject(@RequestPart(value="projectName") String projectName) {
    	System.out.println("createProject called in controller");
    	this.amazonClient.createProject(projectName);
    }
    
    @RequestMapping(value="createSprint",method=RequestMethod.POST)
    public void createSprint(@RequestPart(value="projectName") String projectName, @RequestPart(value="sprintName") String sprintName) {
    	System.out.println("createSprint called in controller");
    	this.amazonClient.createSprint(projectName, sprintName);
    }
    
    @RequestMapping(value="uploadReportFile",method=RequestMethod.POST)
    public void uploadReportFile(@RequestPart(value="projectName") String projectName, @RequestPart(value="sprintName") String sprintName,
    		@RequestPart(value = "file") MultipartFile file) {
    	System.out.println("uploadReportFile called in controller");
    	this.amazonClient.uploadReportFile(projectName,sprintName,file);
    }
    
    @RequestMapping(value="uploadMultipleFiles",method=RequestMethod.POST)
    public void uploadMultipleFiles(@RequestPart(value="projectName") String projectName, @RequestPart(value="sprintName") String sprintName,
    		@RequestPart(value = "file") MultipartFile[] file) {
    	System.out.println("uploadMultipleFiles called in controller");
    	this.amazonClient.uploadMultipleFiles(projectName, sprintName, file);
    }
    
}