package dev.dickinson.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.dickinson.services.AmazonClient;

@RestController
//@RequestMapping("/storage/")
public class BucketController {

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
    
    
    
}