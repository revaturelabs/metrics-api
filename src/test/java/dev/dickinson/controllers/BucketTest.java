package dev.dickinson.controllers;

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

import dev.dickinson.services.AmazonClient;

//import dev.ateam.entities.Build;
//import dev.ateam.services.BuildService;
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ContextConfiguration(classes = com.example.demo.MetricsBucketApplication.class)
class BucketTest {

  @Autowired
  private AmazonClient amazonClient;

  private MultipartFile test = new MockMultipartFile("files", "filename.txt", "text/plain",
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
    testUrl = amazonClient.uploadFile(test);
    System.out.println(testUrl);
  }



  @Test
  @Order(2)
  void deleteFileTest() {
    System.out.println(amazonClient.deleteFileFromS3Bucket(testUrl));
  }
  
  @Test
  @Order(1)
  //this test is inconclusive as returns void
  void createProject() {
    amazonClient.createProject("test_project_to_delete");
  }

//  @Test
//  @Order(2)
//  void addSprintTest() {
//    
//  }
  
//@Test
//@Order(2)
//void listAllSprintFilesTest() {
//  System.out.println(amazonClient.listAllSprintFiles());
//}
  
  @Test
  @Order(4)
  void deleteProjectTest() {
    amazonClient.deleteProject("test_project_to_delete");
  }
  
  
//  @Test
//  @Order(3)
//  //need this to determine if the project created actually completed.
//  void verifyProjectCreated() {
//    String strToCheck = amazonClient." something here about check all projects "();
//    
//    //possibly change this to an expects
//    if(strToCheck.contains("test_project_to_delete")){
//     return; 
//    }
//    fail();
//  }

  @Test
  void listSprintFilesByProjectTest() {
    System.out.println("listSprintFilesByProjectTest() unimplemented");
  }
  
  @Test
  void listSprintFilesByFileNameTest() {
    System.out.println("listSprintFilesByFileNameTest() unimplemented");
  }
}

/*
 * public String listAllSprintFiles() { return objects.toString(); public String
 * listSprintFilesByProject(String projectName) { return objects.toString();
 * public String listSprintFilesByFileName(String fileName) { return
 * objects.toString();
 */