package com.revature.controllers;

import java.io.File;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.revature.services.AmazonClient;

@RestController
//@RequestMapping("/storage/")
public class BucketController {

	@Autowired
    private AmazonClient amazonClient;

    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }
    
    @RequestMapping(value="/uploadFile", method=RequestMethod.POST)
    public String uploadFile(@RequestPart(value="file") MultipartFile file) {
        return this.amazonClient.uploadFile(file);
    }

    @RequestMapping(value="/deleteFile/{projectName}/{sprintName}/{fileName}", method=RequestMethod.DELETE)
    public String deleteFile(@PathVariable String fileName, @PathVariable String projectName, @PathVariable String sprintName) {
        return this.amazonClient.deleteFileFromS3Bucket(projectName,sprintName,fileName);
    }
    
    @CrossOrigin
    @RequestMapping(value="/tokens", method=RequestMethod.GET)
    public String getCreds() {
        return this.amazonClient.getCredentials();

    }
    
    @RequestMapping(value="/listAllSprints", method=RequestMethod.GET)
    public String listBuckets() {
    	return this.amazonClient.listAllSprints();
    }
    
    @RequestMapping(value="/listAllFilesBySprint/{projectName}/{sprintName}", method=RequestMethod.GET)
    public String listAllFilesBySprint(@PathVariable String projectName, @PathVariable String sprintName) {
    	return this.amazonClient.listAllFilesBySprint(projectName, sprintName);
    }
    
    @RequestMapping(value="/listAllSprintsByProject/{projectName}",method=RequestMethod.GET)
    public String listAllSprintsByProject(@PathVariable String projectName) {
    	return this.amazonClient.listAllSprintsByProject(projectName);
    }
    
    @RequestMapping(value="/listSprintFilesByProject/{projectName}",method=RequestMethod.GET)
    public String listSprintFilesByProject(@PathVariable String projectName) {
    	return this.amazonClient.listSprintFilesByProject(projectName);
    }
    
    
    @RequestMapping(value="createProject/{projectName}",method=RequestMethod.POST)
    public void createProject(@PathVariable String projectName) {
    	this.amazonClient.createProject(projectName);
    }
    
    @RequestMapping(value="createSprint/{projectName}/{sprintName}",method=RequestMethod.POST)
    public void createSprint(@PathVariable String projectName, @PathVariable String sprintName) {
    	this.amazonClient.createSprint(projectName, sprintName);
    }
    
    @RequestMapping(value="uploadReportFile/{projectName}/{sprintName}",method=RequestMethod.POST)
    public void uploadReportFile(@PathVariable String projectName, @PathVariable String sprintName,
    		@RequestPart(value="file") MultipartFile file) {
    	this.amazonClient.uploadReportFile(projectName,sprintName,file);
    }
    
    @RequestMapping(value="uploadMultipleFiles/{projectName}/{sprintName}",method=RequestMethod.POST)
    public void uploadMultipleFiles(@PathVariable String projectName, @PathVariable String sprintName,
    		@RequestPart(value="file") MultipartFile[] file) {
    	this.amazonClient.uploadMultipleFiles(projectName, sprintName, file);
    }
    
    @RequestMapping(value="/deleteProject/{projectName}", method=RequestMethod.DELETE)
    public void deleteProject(@PathVariable String projectName) {
        this.amazonClient.deleteProject(projectName);
    }
    
    @RequestMapping(value="/deleteSprint/{projectName}/{sprintName}", method=RequestMethod.DELETE)
    public void deleteSprint(@PathVariable String sprintName, @PathVariable String projectName) {
        this.amazonClient.deleteSprint(sprintName, projectName);
    }
    
    @RequestMapping(value="/countSprints/{projectName}", method=RequestMethod.GET)
    public int countSprints(@PathVariable String projectName) {
    	return this.amazonClient.countSprints(projectName);
    }
    
    @RequestMapping(value="/countSprintFiles/{projectName}/{sprintName}", method=RequestMethod.GET)
    public int countSprintFiles(@PathVariable String projectName, @PathVariable String sprintName) {
    	return this.amazonClient.countSprintFiles(projectName, sprintName);
    }
    
    @RequestMapping(value="/downloadFile/{projectName}/{sprintName}/{fileName}", method=RequestMethod.GET)
    public URL downloadFile(@PathVariable String projectName,@PathVariable String sprintName,@PathVariable String fileName) {
    	return this.amazonClient.downloadFile(projectName, sprintName, fileName);
    }
    
}