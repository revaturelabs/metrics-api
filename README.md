steps:
============
Create Policy:
-------------
* Login to your AWS account
* Search for IAM
* Click policies on the left
* Click create policy
* Click the JSON tab
* Overwrite the content with the following:
{"Version": "2012-10-17", "Statement": [{"Effect": "Allow","Action": "iam:CreateUser","Resource": "\*"}]}
* Click Review policy
* Enter a name, this guide assumes you add CreateUser
* Click Create policy

Create Role:
-------------
* Login to your AWS account
* Search for IAM
* Click Roles on the left
* Under '' "Choose the service that will use this role" '' select ec2
* Click Next: Permissions
* Find and add AmazonS3FullAccess, and CreateUser
* Click Next: Tags
* Click Next: Review
* Add a Role Name, this guide assumes the name is s3AccessRole

create IAM user:
-------------
* Login to your AWS account
* Search for IAM
* Click Users on the left
* Click Add User
* Enter a chosen username
* Make sure you select Programmatic Access
* Click Next:Permissions
* Click Attach existing policies directly
* Search for the policy you have just created (you may need to hit the refresh button above the policies list)
* Click the checkbox selecting that policy
* Click Next: Tags
* Click Next: Review
* Click Create User
* Click Download .csv (you will need this later)

create an s3 bucket
-------------
* Login to your AWS account
* Search for S3
* Click Create a Bucket.
* Enter a Bucket Name
* Click Next
* Click Next
* Select Block All Public Access
* Click Next
* Click Create Bucket
* Click your new bucket name
* Click Permissions
* Click Cors Configuration and paste the below
```XML
<?xml version="1.0" encoding="UTF-8"?>
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
<CORSRule>
    <AllowedOrigin>*</AllowedOrigin>
    <AllowedMethod>GET</AllowedMethod>
    <AllowedMethod>PUT</AllowedMethod>
    <AllowedMethod>POST</AllowedMethod>
    <AllowedMethod>HEAD</AllowedMethod>
    <AllowedMethod>DELETE</AllowedMethod>
    <MaxAgeSeconds>3000</MaxAgeSeconds>
    <AllowedHeader>*</AllowedHeader>
</CORSRule>
</CORSConfiguration>
  ```
* Click Save
* Click Bucket Policy
* Click Policy Generator in the lower left
* for Select Type of Policy select: S3 Bucket policy
* For Principal enter your IAM user ARN (located in the .csv file generated when you created the IAM user)
 * Search for IAM from the AWS console
 * Click on the chosen user
 * User ARN is displayed on that page
* AWS Service: Amazon S3
* Actions: all actions
* Amazon Resource Name: The buckets ARN
 * Search for S3 from the AWS console
 * Select the chosen S3
 * Wait for the menu to slide on the screen from the right
 * click Copy Bucket ARN
* Click Add Statement
* Click Generate Policy
* Copy the displayed JSON
* Go back to the Bucket Policy (please see previous steps)
* Paste the JSON into the text field and save

Create an EC2:
-------------
* Login to your AWS account
* Search for EC2
* Click Launch instance
* Click Select on "Amazon Linux 2 AMI (HVM), SSD Volume Type"
* Unless paid for you want to ensure that you click on the free tier option, then click Next: Configure instance variables
* Change the IAM Access Role to s3AccessRole that you created
* Click Next: Add storage, then Next: Add Tags, then Next: Configure Security Group
* Click Add rule and fill out the fields using the following information

| Type            | Protocol | Port Range | Source    | Description |
|-----------------|----------|------------|-----------|-------------|
| Custom TCP Rule | TCP      | 8080       | 0.0.0.0/0 |             |
| Custom TCP Rule | TCP      | 8080       | ::/0      |             |
| Custom TCP Rule | TCP      | 9999       | ::/0      |             |
| Custom TCP Rule | TCP      | 9999       | 0.0.0.0/0 |             |
| SSH             | TCP      | 22         | 0.0.0.0/0 |             |
| SSH             | TCP      | 22         | ::/0      |             |

* please note that this is extremely insecure, you will want to update this to better rules as needed
* Click Review and launch
* Click Launch
* Select a PPK and click launch
* If you need further information please consult https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/get-set-up-for-amazon-ec2.html

EC2 setup
-------------
* Connect to EC2 via SSH (Putty Recommended) https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/putty.html
* Copy and paste the following once logged in:
```bash
#Install Maven
sudo yum install -y maven
#Install git
sudo yum install -y git
#Get RevOps from s3 bucket
wget https://adammisc.s3.us-east-2.amazonaws.com/RevOps.tar.gz
#Unzip RevOps.tar.gz
tar xzvf RevOps.tar.gz
#removes tar file
rm RevOps.tar.gz
#Start Up Tomcat
/home/ec2-user/RevOps/apache-tomcat-9.0.20/bin/startup.sh
#get intial admin password takes time to generate
#cat /home/ec2-user/.jenkins/secrets/initialAdminPassword
REGION=`curl http://169.254.169.254/latest/dynamic/instance-identity/document|grep region|awk -F\" '{print $4}'`
echo $REGION
aws configure
```
* open the .csv file from when you created your IAM user, you will need information from there
* Press enter
* AWS Access Key ID: User AKID from .csv file
* Press enter
* AWS Secret Access Key" User SAK from .csv file
* Press enter
* Default Region: EC2 Region, this is located in the URL for your EC2, usually something like us-west-2
* No Default output format (just hit enter)
* cd into RevOps/apache-tomcat-9.0.20/bin
* run: ./startup.sh

-------------
Setting up Jenkins on your EC2
-------------
* Navigate to your AWS EC2 page
* Click on your EC2 instance
* On the Description, copy the Public DNS and paste it into a new URL
* Add :8080/jenkins to the end of the URL and travel to the destination
* You will be prompted to setup Jnekins by first supplying the initialAdminPassword
* It will give you the path on the EC2 you need to reach in order to reach the file containing the password password
* open the file via any means
* Copy that string into the input given by Jenkins on your browser
* assign an appropriate username, password, and email

-------------
Using Jenkins to download metrics-api to your EC2
-------------
* Create a new item by clicking on New Item in the upper left
* Select a new Freestyle Project, give it a name, and click okay at the bottom.
* On the next page, under General, check the GitHub project checkbox
* Get the Metrics-api Github URL and paste it here
* Under Source Code Management, select the Git radio button
* Paste the Repository URL here, keep everything else default (currently just using master branch)
* Click save
* Click Build Now

-------------
create .yml file
-------------
```bash
#Navigate to your Jenkins workspace, then your build folder
cd /home/ec2-user/.jenkins/workspace
cd yourbuildfolder
#Navigate to the application's resource folder
cd src/main/resources
```
* create a new file, application.yml, and copy this into it:
```bash
amazonProperties:
        endpointUrl: (Your bucket endpoint)
        accessKey: (User .csv AccessKey)
        secretKey: (User .csv SecretKey)
        bucketName: (bucket name)
 ```
*IMPORTANT NOTE: This .yml file is extremley picky about whitespace, ONE leading tab on each line. Spaces will cause it to parse incorretly.

-------------
Launching Metrics-API on your EC2
-------------
* SSH into your EC2
```bash
#Navigate to your Jenkins workspace
cd /home/ec2-user/.jenkins/workspace
#Navigate into your build folder
cd yourbuildfolder
#build your .jar file
mvn package
#Navigate to the (newly) created target folder
cd target
#Run the .jar
java -jar yoursnapshot.jar
```
* Current implementation creates the tomcat server on port 9999, this assignment is in the application.properties file in the application resource folder.


