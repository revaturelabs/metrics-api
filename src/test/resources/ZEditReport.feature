Feature: On Edit Report page        
    Scenario: On Edit Report to add a file
    	Given The user is on the Sprint Reports home page
    	When The user clicks on Edit Report
    	When The user selects a project
    	When The user selects an iteration
        When The user adds a file
        Then The user clicks on update file
    
      
    Scenario: On Edit Report to remove files
    	Given The user is on the Sprint Reports home page
    	When The user clicks on Edit Report
    	When The user selects a project
    	When The user selects an iteration
        When The user removes a file
        When The user removes a file
        Then The user clicks on update file
        
    Scenario: On Edit Report to delete an iteration
    	Given The user is on the Sprint Reports home page
    	When The user clicks on Edit Report
    	When The user selects a project
    	When The user selects an iteration
    	When The user removes a file
        Then The user clicks on delete Iteration