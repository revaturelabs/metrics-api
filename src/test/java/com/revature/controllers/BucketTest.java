package com.revature.controllers;

import static org.assertj.core.api.Assertions.fail;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import com.revature.services.AmazonClient;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ContextConfiguration(classes = com.example.demo.MetricsBucketApplication.class)
class BucketTest {

  @Autowired
  private AmazonClient amazonClient;

  private MultipartFile testFile = new MockMultipartFile("files", "filename.txt", "text/plain",
      "hello".getBytes(StandardCharsets.UTF_8));
  private String testUrl = "";

  @Test
  void tokenCreationTest() {
    try {
      String verifyData = amazonClient.getCredentials();
      if (verifyData.contains("{\"arr\":[")) {
        // passes
      }
    } catch (Exception e) {
      e.printStackTrace();
      fail("failed in token creation");
    }
  }

  @Test
  @Order(1)
  void uploadFileTest() {
    testUrl = amazonClient.uploadFile(testFile);
    System.out.println(testUrl);
  }

  @Test
  @Order(2)
  void deleteFileTest() {
    //System.out.println(amazonClient.deleteFileFromS3Bucket(testUrl));
	  System.out.println(amazonClient.deleteFileFromS3Bucket("files", "text/plain", "filename.txt"));
  }

  @Test
  @Order(1)
  // check for exception test
  void createProject() {
    amazonClient.createProject("test_project_to_delete");
  }

  @Test
  @Order(2)
//check for exception test
  void deleteProjectTest() {
    amazonClient.deleteProject("test_project_to_delete");
  }

  @Test
  @Order(3)
  void createSprintAndTest() {
    amazonClient.createSprint("test_project_to_delete", "test_sprint_to_delete");
  }

  @Test
  @Order(4)
  void addFilesToSprintTest() {
    MultipartFile[] testFileArray = new MockMultipartFile[1];
    testFileArray[0] = testFile;
    amazonClient.uploadMultipleFiles("test_project_to_delete", "test_sprint_to_delete", testFileArray);
  }
  
  @Test
  @Order(5)
  void listAllSprintsTest() {
    String testStr = amazonClient.listAllSprints();
    if(testStr.contains("test_sprint_to_delete")) {
      
    }else {
      fail("failed to find the file in our sprint");
    }
  }

  @Test
  @Order(6)//verify that the files placed in the sprint
  void listAllSprintFilesTest() {
    String testString = amazonClient.listAllSprints();
    if(testString.contains("filename.txt")) {
      
    }else {
      fail("failed to find the file in our sprint");
    }
  }


}