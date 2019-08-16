package dev.dickinson.controllers;
import static org.junit.Assert.fail;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
  @Test
  void tokenCreationTest() {
    try {
      String verifyData = amazonClient.getCredentials();
      if (verifyData.contains("{\"arr\":[")){
        //passes
      }
    }
    catch(Exception e) {
      e.printStackTrace();
      fail();
    }
  }
}
//    
//    @Nested
//    @DisplayName("tests that depend on others")
//    class   
//  }
//  
//  
//  
//  @Test
//  void listAllSpringFilesTest(){
//    
//  }
//  @Test
//  void listSprintFilesByProject(){
//    
//  }
//  @Test
//  void listSprintFilesByFileName(){
//    
//  }
  /*
   * public String listAllSprintFiles() {
   * return objects.toString();
   *  public String listSprintFilesByProject(String projectName) {
   *  return objects.toString();
   *  public String listSprintFilesByFileName(String fileName) {
   *  return objects.toString();
   */
