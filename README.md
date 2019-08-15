# metrics-api
Secure back-end for metrics-bucket

# Setup
>git clone 
>mvn package
>aws configure
>java -jar <output jar from mvn>

By default the server will be running on port 9999, you can change this in src/main/resources/application.properties or at runtime with java -jar <path to jar> --server.port=<a port>
