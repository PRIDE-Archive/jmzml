# xxindex
Summary a library to index XML files for random access.

## About XXIndex
XXIndex library is originally a part of the PSI-Dev project. It aims at addressing the needs for quick data retrieval in large XML files.
 
The key features of the library include:
 * *Easy to Use*: XXIndex uses XPath-like expressions to identify XML entities. 
 * *Small Memory Footprint*: Unlike some XML indexing libraries, XXIndex does not require to load the whole file into memory (large or small). Only the indices are stored in order to achieve minimum memory requirement.
 * *Quick Indexing*: Although the speed of indexing depends on the complexity of the input XML file (the higher the XML tag density the slower the indexing), based on our experience, a 500MB XML file usually takes only around 50 seconds.

The XXIndex library is currently used by many projects, below are a couple of them:
 * [PRIDEXMLJAXB PRIDE XML JAXB Library]
 * [jmzML jmzML Library]
 * [http://www.psidev.info/index.php?q=node/304 Most of the PSI semantic validators]

This library is written in Java, brought to you by the [http://www.ebi.ac.uk/pride/ PRIDE] team. 

##Getting XXIndex

The zip file in the [http://code.google.com/p/pride-toolsuite/downloads/list downloads section] contains the *XXIndex* jar file and all other required libraries.

##Maven Dependency

The *XXIndex* library can be used in Maven projects, just include the following snippets in the appropriate sections of your Maven pom file.

```maven
 <dependency>
   <groupId>psidev.psi.tools</groupId>
   <artifactId>xxindex</artifactId>
   <version>0.10</version>
 </dependency> 
```

```maven
 <repository>
   <id>ebi-repo</id>
   <name>The EBI internal repository</name>
   <url>http://www.ebi.ac.uk/~maven/m2repo</url>
   <releases>
     <enabled>true</enabled>
   </releases>
   <snapshots>
     <enabled>false</enabled>
   </snapshots>
 </repository>
```

*Note*: you may want to update the version number to the latest version available in the repository.


##Using XXIndex

The first step of using the XXIndex library is to index the XML file. For instance, if you want to read a file named: `example.xml`, you should first initialize an instance of `StandardXpathAccess`, the code below will show you how to do that:

```java
// create index for xml file
StandardXpathAccess access = new StandardXpathAccess("example.xml");
```

Once the index has been created, you can access the elements using their Xpath. For example, the code below shows you how to get the number elements in the file:

```java
// Get the index of the file
XpathIndex index = access.getIndex();
// Get the number of a element
int count = index.getElementCount("xpath/to/element/");
```
 
##Getting Help

If you have questions or need additional help, please contact the PRIDE Helpdesk at the EBI: *pride-support at ebi.ac.uk (replace at with @)*.

Please send us your feedback, including error reports, improvement suggestions, new feature requests and any other things you might want to suggest to the PRIDE team.
