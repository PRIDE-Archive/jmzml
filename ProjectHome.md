# jmzML #

  * [About jmzML](#About_jmzML.md)
  * [mzML Viewer Application](#mzML_Viewer_Application.md)
  * [Getting jmzML](#Getting_jmzML.md)
  * [Using jmzML](HowToUseJMzML.md)
  * [Getting Help](#Getting_Help.md)
  * [About mzML](#About_mzML.md)

**jmzML Publications:**
  * [Côté et al: Proteomics 2010 Apr;10(7):1332-5](http://www.ncbi.nlm.nih.gov/pubmed/20127693).
  * If you use jmzML as part of a paper, please include the reference above.


---


## About jmzML ##
**jmzML** provides a portable and lightweight JAXB-based implementation of the full mzML 1.1 standard format (note that the **jmzML** version number has no relation to the mzML version number), with considerable tweaks to make the processing of files memory efficient. In particular, mzML files are effectively indexed on the fly and used as swap files, with only requested snippets of data loaded from a file when accessing it. Additionally, internal references in the mzML XML are resolved automatically by **jmzML**, giving you direct access in the object model to entities that are only referenced by ID in the actual XML file.

Apart from reading indexed and non-indexed mzML files, **jmzML** also allows writing of non-indexed mzML files.

**jmzML** is written in 100% pure Java, and is made available under the permissive Apache2 open source license.

A one-page guide on using the **jmzML** API can be found here: [HowToUseJMzML](HowToUseJMzML.md)

_[top of page](#jmzML.md)_


---


## mzML Viewer Application ##
**jmzML** comes with a separat viewer for mzML files, allowing you to interactively visualize the spectra and chromatograms in an mzML file. Simply double-click the **jmzmlGUI** jar file after downloading and unzipping the zip file distribution (mzml-viewer-`<`version`>`.zip) shown on the right to start the application.

If you need an example mzML file, you can download one [here](http://jmzml.googlecode.com/svn/trunk/src/test/resources/tiny.pwiz.mzML) (right-click and choose 'download as', or 'save as').

The **jmzML** viewer can visualize multiple mzML files at the same time, simply use the 'File' --> 'Open' menu in the application to add additional mzML files to view. Each mzML file will be shown in an individual tab.

Screenshots of the application in action are shown below. Click an image to view a larger version.

![![](http://jmzml.googlecode.com/svn/wiki/images/screenshots/jmzML_viewer_screenshot_1_small.png)](http://jmzml.googlecode.com/svn/wiki/images/screenshots/jmzML_viewer_screenshot_1.png) ![![](http://jmzml.googlecode.com/svn/wiki/images/screenshots/jmzML_viewer_screenshot_2_small.png)](http://jmzml.googlecode.com/svn/wiki/images/screenshots/jmzML_viewer_screenshot_2.png) ![![](http://jmzml.googlecode.com/svn/wiki/images/screenshots/jmzML_viewer_screenshot_3_small.png)](http://jmzml.googlecode.com/svn/wiki/images/screenshots/jmzML_viewer_screenshot_3.png)

_[top of page](#jmzML.md)_


---


## Getting jmzML ##
The zip file in the [downloads section](http://code.google.com/p/jmzml/downloads/list) contains the **jmzML** jar file and all other required libraries.

### Maven Dependency ###
If you wish to include jmzML in your own Java projects, and you use Maven 2, the following snippets could be useful for you:

> - Maven 2 repository definition for jmzML (and for a host of other EBI libraries):
```
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
```

> - jmzML dependency snippet:
```
        <dependency>
            <groupId>uk.ac.ebi.jmzml</groupId>
            <artifactId>jmzml</artifactId>
            <version>x.y.z</version>
        </dependency>

```

_[top of page](#jmzML.md)_


---

## Getting Help ##
If you have questions or need additional help, please contact the PRIDE Helpdesk at the EBI: **pride-support at ebi.ac.uk (replace at with @)**.

Please send us your feedback, including error reports, improvement suggestions, new feature requests and any other things you might want to suggest to the PRIDE team.


[top of page](#jmzML.md)

---


## About mzML ##
mzML is a community standard format for mass spectrometry data, created by the [Proteomics Standards Initiative](http://www.psidev.info) (PSI).

The current release, 1.6.1, implements and validates against the full mzML specification, version 1.1.

Please refer to the PSI mzML web page for the full specification documentation:

http://www.psidev.info/index.php?q=node/257

_[top of page](#jmzML.md)_