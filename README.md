## About jmzML

jmzML provides a portable and lightweight JAXB-based implementation of the full mzML 1.1 standard format (note that the jmzML version number has no relation to the mzML version number), with considerable tweaks to make the processing of files memory efficient. In particular, mzML files are effectively indexed on the fly and used as swap files, with only requested snippets of data loaded from a file when accessing it. Additionally, internal references in the mzML XML are resolved automatically by jmzML, giving you direct access in the object model to entities that are only referenced by ID in the actual XML file.

Apart from reading indexed and non-indexed mzML files, jmzML also allows writing of non-indexed mzML files.


## mzML Viewer Application

We recommnded [PRIDE Toolsuite](https://github.com/PRIDE-Toolsuite/pride-inspector) 


## Getting jmzML

The zip file in the downloads section contains the jmzML jar file and all other required libraries.

## Maven Dependency

If you wish to include jmzML in your own Java projects, and you use Maven 2, the following snippets could be useful for you:

```maven
        <repository>
            <id>ebi-repo</id>
            <name>The EBI Maven 2 repository</name>
            <url>http://www.ebi.ac.uk/~maven/m2repo</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>

        <dependency>
            <groupId>uk.ac.ebi.jmzml</groupId>
            <artifactId>jmzml</artifactId>
            <version>x.y.z</version>
        </dependency>
```

Getting Help
If you have questions or need additional help, please contact the PRIDE Helpdesk at the EBI: pride-support at ebi.ac.uk (replace at with @).

Please send us your feedback, including error reports, improvement suggestions, new feature requests and any other things you might want to suggest to the PRIDE team.

