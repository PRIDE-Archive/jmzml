## About jmzML

jmzML provides a portable and lightweight JAXB-based implementation of the full mzML 1.1 standard format (note that the jmzML version number has no relation to the mzML version number), with considerable tweaks to make the processing of files memory efficient. In particular, mzML files are effectively indexed on the fly and used as swap files, with only requested snippets of data loaded from a file when accessing it. Additionally, internal references in the mzML XML are resolved automatically by jmzML, giving you direct access in the object model to entities that are only referenced by ID in the actual XML file.

Apart from reading indexed and non-indexed mzML files, jmzML also allows writing of non-indexed mzML files.


## mzML Viewer Application

We recommended [PRIDE Toolsuite](https://github.com/PRIDE-Toolsuite/pride-inspector)


## Getting jmzML

The zip file in the downloads section contains the jmzML jar file and all other required libraries.

## Maven Dependency

If you wish to include jmzML in your own Java projects, and you use Maven 2, the following snippets could be useful for you:


PRIDE Utilities library can be used in Maven projects, you can include the following snippets in your Maven pom file.

 ```maven

 <!-- EBI repo -->
 <repository>
     <id>nexus-ebi-release-repo</id>
     <url>http://www.ebi.ac.uk/Tools/maven/repos/content/groups/ebi-repo/</url>
 </repository>

 <!-- EBI SNAPSHOT repo -->
 <snapshotRepository>
    <id>nexus-ebi-snapshots-repo</id>
    <url>http://www.ebi.ac.uk/Tools/maven/repos/content/groups/ebi-snapshots/</url>
 </snapshotRepository>
```

```maven
        <dependency>
            <groupId>uk.ac.ebi.jmzml</groupId>
            <artifactId>jmzml</artifactId>
            <version>x.y.z</version>
        </dependency>
```

## Getting Help

If you have questions or need additional help, please contact the PRIDE Helpdesk at the EBI: pride-support at ebi.ac.uk (replace at with @).

Please send us your feedback, including error reports, improvement suggestions, new feature requests and any other things you might want to suggest to the PRIDE team.

### Cite:

Perez-Riverol Y, Uszkoreit J, Sanchez A, Ternent T, Del Toro N, Hermjakob H, Vizcaíno JA, Wang R. (2015). ms-data-core-api: An open-source, metadata-oriented library for computational proteomics. Bioinformatics. 2015 Apr 24. [PDF File](http://www.ncbi.nlm.nih.gov/pubmed/25910694) [Pubmed Record](http://www.ncbi.nlm.nih.gov/pubmed/25910694)


### Known Problems

When compiling some of the tests are failing due the calculation of the index and checksum that depend on the architecture.

