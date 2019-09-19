# Console Search Service

## Introduction

The Console Search Service standalone application provides functionality for filtering Json structured data for user entered attribute 
value. Current implementation supports for provide Users, Organization & Ticket structured data. It can be extended easily to accommodate additional
provider implementations as well. 

This service currently support following operations

* Filter and print Users,Organization & Tickets data with interrelations for a given search field and attribute values
* Print list of field values considered for searching for each data provider

In order to build the project, you will have to install the following:

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Maven](https://maven.apache.org/download.cgi)
* Set up Maven settings by following [instructions](https://projectwave.atlassian.net/wiki/spaces/PW/pages/21004292)
    
## Build

Following describe step to build the artifacts from the scratch.

1. Go to the project folder 
   /ConsoleSearchService
    
2. Building the project artifacts

### Maven

```
mvn clean install
``` 
3. Once step 2 completed successfully following artifact will be available in the target folder. 
        ConsoleSearchService/target

        1. swivelconsoleapp-1.0.0.jar     
        2. json files 'organizations.json','tickets.json','users.json' 
        3. libs/gson-2.8.4.jar

## Run

1. Go to the project targe folder 
   /ConsoleSearchService/target

2. Run the program using java command line

```
java -jar swivelconsoleapp-1.0.0.jar
``` 
  
## Testing

To run the unit tests, execute:

```
mvn verify
```
