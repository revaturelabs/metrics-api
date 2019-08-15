package dev.dickinson.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;
import com.google.gson.Gson;

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
        AWSSecurityTokenServiceClient sts_client = (AWSSecurityTokenServiceClient) AWSSecurityTokenServiceClientBuilder.standard().withRegion("us-east-2").build();
        GetSessionTokenRequest session_token_request = new GetSessionTokenRequest();
        session_token_request.setDurationSeconds(7200); // optional.
        GetSessionTokenResult session_token_result =
           sts_client.getSessionToken(session_token_request);
        Credentials session_creds = session_token_result.getCredentials();
        Gson gson = new Gson();
        String returnData = "{"+gson.toJson(session_creds); 
        returnData+= gson.toJson(session_token_request)+"}";
        return returnData;
    }
    
    
    
}