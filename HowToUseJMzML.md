# jmzML #

**jmzML** is a Java API to the PSI mzML data exchange standard. It uses JAXB and the xxindex random-access XML parser to allow the parsing of arbitrary large XML files. Internal XML references, which can link elements across the whole file and are widely used in the mzML schema, are resolved on-the-fly without the need to keep the whole document in memory. **jmzML** is optimized to have as small a memory requirement as possible without compromising speed of data retrieval.

# A Typical Code Sample #

The first step in using the **jmzML** API is to create an MzMLUnmarshaller object for a given mzML file. This will be the basis of getting access to the mzML data.

```
//create a new unmarshaller object
//you can use a URL or a File to initialize the unmarshaller
File xmlFile = new File("path/to/your/mzml/file");
MzMLUnmarshaller unmarshaller = new MzMLUnmarshaller(xmlFile);
```

Upon initialization, the unmarshaller will generate an XPath-based index for the specified mzML file, which will be used under the hood to retrieve individual XML snippets and resolve file-internal XML references. To build this index can be time consuming, in the order of minutes for a large, multi-gigabyte file. Once it is done however, data retrieval is very efficient, as only required chunks of XML are read and converted, using JAXB, into fully-fledged Java objects. It is also where most of the memory requirements of **jmzML** reside, as the index is kept in memory for rapid access.

While it is possible to directly unmarshall a complete mzML file into a MzML object in one go, this should be used with great care as, for large XML files, the memory requirements will most likely cause the JVM to rapidly run out of memory.

```
//For small files the complete mzML can be unmarshalled in one go.
MzML completeMzML = unmarshaller.unmarshall();
```

The alternative is to directly access the elements that are required (see below).

```
//Helper methods are provided to access the attibutes of the MzML Object
System.out.println("MzML Version: = " + unmarshaller.getMzMLVersion());
System.out.println("MzML ID: = " + unmarshaller.getMzMLId());
System.out.println("MzML Accession: = " + unmarshaller.getMzMLAccession());
```

As mentioned above, **jmzML** uses an XPath-based system to retrieve partial data from the full mzML file. It makes use of Java Generics wherever possible, to provide a consistent and simple usage.

```
//retrieve the cvList element of the mzML file, given its XPath
CVList cvList = unmarshaller.unmarshalFromXpath("/cvList", CVList.class);
//the object is now fully populated with the data from the XML file
System.out.println("Number of defined CVs in this mzML: " + cvList.getCount());
//retrieve the fileDescription element
FileDescription fd = unmarshaller.unmarshalFromXpath("/fileDescription", FileDescription.class);
System.out.println("Number of source files: " + fd.getSourceFileList().getCount());
```

Notice in the preceding code snippet that the same method, unmarshalFromXpath, is used. The first parameter is the XPath of the element to unmarshall and the second parameter is the class of the expected return object. Also note that the XPath does not start with /mzML or /indexedmzML. The root element of the XML file is automatically detected by the unmarshaller and the proper XPath is used accordingly. The code above would therefore work equally on both, indexed and non-indexed mzML files.

There are a few important things to keep in mind when using this method. Firstly, as you can see from the example above, the unmarshallFromXpath method requires a valid XPath and the class of the object to create. Currently the unmarshaller does not support every XPath that would be possible according to the mzML schema. This is partially to keep the index small and memory requirements as low as possible. Furthermore, there would be little use in retrieving each tiny XML fragment, especially if its content only makes sense in the context of the surrounding larger element (as is usually the case for CvParams or UserParams). Therefore the XPaths that can be unmarshalled into Java objects are limited to a selection of elements that we believe will be commonly used and are logical information units. The full list of supported XPaths is available via the Constants utility class.

```
//supported XPath for indexed and non-indexed mzML 
System.out.println("Supported XPath:" + Constants.XML_INDEXED_XPATHS);
```

Another important aspect of this method is that it returns a single object of the specified type. If there is more than one element for the provided XPath (as the case for ‘spectrum’), then it will only return the first such element. The MzMLUnmarshaller provides a method that will tell you how many elements you can expect for a given XPath.

```
//number of spectrum elements in the XML file
System.out.println("Number of spectrum elements: " + unmarshaller.getObjectCountForXpath("/run/spectrumList/spectrum"));
```

To deal with a collection of elements and in order to keep the memory requirements as low as possible, **jmzML** provides Iterators to load large data volumes sequentially. This approach should also be used instead of using potentially huge objects (like Run, SpectrumList or ChromatogramList) directly, as they contain the bulk of the information contained in the mzML file.

```
//dealing with element collections
MzMLObjectIterator<Spectrum> spectrumIterator = unmarshaller.unmarshalCollectionFromXpath("/run/spectrumList/spectrum", Spectrum.class);
while (spectrumIterator.hasNext()){
  //read next spectrum from XML file
  Spectrum spectrum = spectrumIterator.next();
  //use it
  System.out.println("Spectrum ID: " + spectrum.getId());
}
```

Instead of trying to load all spectra at once and make them available as Collection, this code will only unmarshal one spectrum element from the XML file per iteration. Thus the memory requirements are kept at a minimum.

# Conclusion #

**jmzML** provides an easy and very efficient way to parse large mzML XML files. Plans for future work include additional functionality, such as the ability to easily write XML files from populated Java objects as well as fine-tune the caching mechanism so that users can balance performance versus memory footprint.

Users are strongly encouraged to contact the development team with feedback and requirements. Email can be sent to pride-support@ebi.ac.uk or an ticket can be created in the [Issues](http://code.google.com/p/jmzml/issues/list) section of the Google code project page.