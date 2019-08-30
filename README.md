**Make sure that you're on the us-east-1 region (US East N. Virginia).** Otherwise some things (like Postman and the backend) don't work.
reference this link for a potential solution: https://github.com/aws/aws-sdk-java/issues/1451 (cross-region behaviour)

Make sure to view this file as a Markdown file, some things are incorrect when copied/pasted as plain text.

Steps:
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
* Find and add: 
    AmazonS3FullAccess
    CreateUser (The policy you just created)
* Click "Next: Tags"
* Click "Next: Review"
* Add a Role Name, this guide assumes the name is s3AccessRole

Create IAM user:
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
* Search for the AmazonS3FullAccess
* Click the checkbox selecting that policy
* Click Next: Tags
* Click Next: Review
* Click Create User
* **Click Download .csv** (you **will** need this later)
* Click Close

Create an s3 bucket
-------------
* Login to your AWS account
* Search for S3
* Click Create a Bucket.
* Enter a Bucket Name
* **Make sure the region is US East N. Virginia**
* Click Next
* Click Next
* Select Block All Public Access (If it hasn't been selected already)
* Click Next
* Click Create Bucket
* Click your new bucket name
* Click Permissions
* Click Cors Configuration (After clicking Permissions) and paste the XML below
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
* Click Save.
* Click Bucket Policy.
* Click Policy Generator in the lower left.

The First Policy: (This is a policy for the **User**)
* for Select Type of Policy select: S3 Bucket policy.
* For Principal enter your IAM user ARN (click on the User in the IAM).
 * Search for IAM from the AWS console.
 * Click on the chosen user (that you created).
 * User ARN is displayed on that page.
* AWS Service: Amazon S3.
* Actions: all actions.
* Amazon Resource Name: The buckets ARN (Navigate to the main S3 page, click on the bucket row (not the name!)).
 * click Copy Bucket ARN.
* Click Add Statement.

The Second Policy: (making the same thing, but for the **role** this time)
* for Select Type of Policy select: S3 Bucket policy.
* For Principal enter your IAM Role ARN  (click on the Role in IAM and the ARN should be listed).
* AWS Service: Amazon S3.
* Actions: all actions.
* Amazon Resource Name: The buckets ARN.
* Click Add Statement.
* Click Generate Policy, and **copy** this JSON.
* Go back to the Bucket Policy page.
* Paste the JSON into the text field and save.

Create an EC2: (Add things about creating and puttygening a new key pair)
-------------
* Login to your AWS account
* Search for EC2
* **Make sure you're on the N.Virginia US East EC2 server**
* Click Launch instance
* Click Select on "Amazon Linux 2 AMI (HVM), SSD Volume Type"
* Unless paid for you want to ensure that you click on the free tier option, then click "Next: Configure Instance Details"
* Change the IAM Access Role to s3AccessRole that you created
* Click "Next: Add storage", then "Next: Add Tags", then "Next: Configure Security Group"
* Click Add rule and fill out the fields using the following information:

| Type            | Protocol | Port Range | Source          | Description |
|-----------------|----------|------------|-----------------|-------------|
| Custom TCP Rule | TCP      | 8080       | 0.0.0.0/0, ::/0 |             |
| Custom TCP Rule | TCP      | 9999       | 0.0.0.0/0, ::/0 |             |
| SSH             | TCP      | 22         | 0.0.0.0/0, ::/0 |             |

* please note that this is extremely insecure, you will want to update this to better rules as needed
* Click Review and launch
* Click Launch
* If you already have a key:pair, you can use that, otherwise, create a new one.
* Select a PPK and click launch
* If you need further information please consult https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/get-set-up-for-amazon-ec2.html

EC2 setup
-------------
* Connect to EC2 via SSH (Putty Recommended) https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/putty.html
* Make sure to use the public DNS under the description of your EC2
* Copy and paste the following once logged in: (You will need to press enter once it hits "aws configure")
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
* On the Description, copy the Public DNS and paste it into a new URL and add ":8080/jenkins" to the end of the URL
* Navigate to the URL
* You will be prompted to setup Jnekins by first supplying the initialAdminPassword
* It will give you the path on the EC2 you need to reach in order to reach the file:
* vim/nano to the entire path, or cd to the folder just before "initialAdminPassword"
* open the file via any means
* Copy that string into the input given by Jenkins on your browser
* Install suggested Plugins.
* assign an appropriate username, password, and email
* Save and continue

-------------
Using Jenkins to download metrics-api to your EC2
-------------
* Create a new item by clicking on New Item in the upper left
* Select a new Freestyle Project, give it a name, and click okay at the bottom.
* On the next page, under General, check the GitHub project checkbox
* Get the Metrics-api Github URL (clone URL) and paste it here
* Under Source Code Management, select the Git radio button
* Paste the Repository URL here, keep everything else default (currently just using master branch)
* Click save
* Click Build Now

-------------
Create .yml file
-------------
```bash
#Navigate to your Jenkins workspace, then your build folder
cd /home/ec2-user/.jenkins/workspace
cd TheJenkinsBuildNameYouChose
#Navigate to the application's resource folder
cd src/main/resources
```
* create a new file, application.yml, and copy this into it:
```bash
amazonProperties:
        endpointUrl: (Your bucket endpoint) (can find this under your S3/Properties/static hosting section)
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
cd TheJenkinsBuildNameYouChose
#build your .jar file
mvn package
#**If the tests fail**, you can build it without running the tests, using: 
mvn -Dmaven.test.skip=true package
#Navigate to the (newly) created target folder
cd target
#Run the .jar
java -jar yoursnapshot.jar
```

* Current implementation creates the tomcat server on port 9999, this assignment is in the application.properties file in the application resource folder.

----------
Launching the Front-End
----------

*Navigate to: https://github.com/revaturelabs/metrics-bucket and follow the README

----------
Potential/Expected Errors
----------
Association of user after EC2 creation may not update. A server (EC2) restart may be necessary.

Restarting EC2 will change endpoint URL (IP change), all related URLS must now be updated.

Attempting to run locally will not generate Auth Tokens, this is because certain environment variables are required that the EC2 has inherintley.

MVN package build fails, as a result of tests failing. If this happens, make sure to run with suppressed tests (-Dmaven.test.skip=true)
MAKE SURE THAT YOU BUILD IT ON THE US_EAST_1 REGION. You can make it cross-region, but we didn't get there.

If you have to restart anything, make sure all regions/URLS are correct between all files. (This includes Jenkins webhooks if you add build automation.

If Jenkins for some reason is unavailible, make sure that the associated Tomcat server is running (See EC2 setup, a line inside the bash commands './startup.sh' in apache-tomcat bin)

-----------
Things that need to be done
-----------
Add cross-region functionality: Currently the front-end, backend, all must be on "us-east-1" in order to function properly. Try to get the front end up and running on "us-east-2" by changing the environement variable on a metrics-bucket and going from there.

Change the EC2 security group to only revature IP's: Currently it's open to the world, change that both in the README, and whatever EC2 you end up using.

Setup and add documentation for automatic Jenkins builds: Use webhooks with GitHub and write some scripts on the EC2 server that maintain an active server (restarts it if it exits), restarts the server after a successful Jenkin's build, and kills the server (if it exists) right before Jenkin's restarts the server (so that there are no "port already in use" errors).
