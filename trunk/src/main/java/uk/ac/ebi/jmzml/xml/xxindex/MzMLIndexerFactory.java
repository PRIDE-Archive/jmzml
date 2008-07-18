package uk.ac.ebi.jmzml.xml.xxindex;

import org.apache.log4j.Logger;
import psidev.psi.tools.xxindex.StandardXmlElementExtractor;
import psidev.psi.tools.xxindex.StandardXpathAccess;
import psidev.psi.tools.xxindex.XmlElementExtractor;
import psidev.psi.tools.xxindex.XpathAccess;
import psidev.psi.tools.xxindex.index.IndexElement;
import psidev.psi.tools.xxindex.index.XpathIndex;
import uk.ac.ebi.jmzml.xml.Constants;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: rcote
 * Date: 11-Jun-2008
 * Time: 17:09:40
 * $Id: $
 */
public class MzMLIndexerFactory {

    private static final Logger logger = Logger.getLogger(MzMLIndexerFactory.class);

    private static final MzMLIndexerFactory instance = new MzMLIndexerFactory();
    private static final Pattern ID_PATTERN = Pattern.compile("id\\s*=\\s*['\"](\\w*)['\"]", Pattern.CASE_INSENSITIVE);

    private MzMLIndexerFactory() {
    }

    public static MzMLIndexerFactory getInstance() {
        return instance;
    }

    public MzMLIndexer buildIndex(File xmlFile) {
        return new MzMlIndexerImpl(xmlFile);
    }

    public MzMLIndexer buildIndex(URL xmlFileURL) {
        File xmlFile = FileUtils.getFileFromURL(xmlFileURL);
        return new MzMlIndexerImpl(xmlFile);
    }

    private class MzMlIndexerImpl implements MzMLIndexer {

        private File xmlFile = null;
        private XpathAccess xpathAccess = null;
        private XmlElementExtractor xmlExtractor = null;
        private XpathIndex index = null;
        private String root = null;

        private HashMap<String, IndexElement> cvIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> dataProcessingIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> instrConfigIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> refParamGroupIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> sampleIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> softwareIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> sourceFileIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> spectrumIdMap = new HashMap<String, IndexElement>();

        private MzMlIndexerImpl(File xmlFile) {

            if (xmlFile == null) {
                throw new IllegalStateException("XML File to index must not be null");
            }
            if (!xmlFile.exists()) {
                throw new IllegalStateException("XML File to index does not exist: " + xmlFile.getAbsolutePath());
            }

            try {

                //store file reference
                this.xmlFile = xmlFile;

                //generate XXINDEX
                logger.info("Creating index: ");
                xpathAccess = new StandardXpathAccess(xmlFile, Constants.XML_INDEXED_XPATHS);
                logger.debug("done!");

                //create xml element extractor
                xmlExtractor = new StandardXmlElementExtractor();
                xmlExtractor.setEncoding(xmlExtractor.detectFileEncoding(xmlFile.toURL()));

                //create index
                index = xpathAccess.getIndex();

                //initialize xpath root
                root = "/mzML";
                if (!index.containsXpath(root)) {
                    root = "/indexedmzML/mzML";
                    if (!index.containsXpath(root)) {
                        throw new IllegalStateException("Invalid XML - /mzML or /indexedmlML xpaths not found!");
                    }
                }

                //prefetch some elements that will be referenced multiple times
                //note that some of these prefetched elements also reference other elements
                //so need to pass in preconfigured Adapters to deal with the references

                //cv cache
                logger.info("Init CV cache");
                initIdMapCache(cvIdMap, "/cvList/cv");

                //dataProcessing cache
                logger.info("Init DataProcessing cache");
                initIdMapCache(dataProcessingIdMap, "/dataProcessingList/dataProcessing");

                //instrumentConfig cache
                logger.info("Init InstrumentConfiguration cache");
                initIdMapCache(instrConfigIdMap, "/instrumentConfigurationList/instrumentConfiguration");

                //refParamGroup cache
                logger.info("Init ReferenceableParamGroup cache");
                initIdMapCache(refParamGroupIdMap, "/referenceableParamGroupList/referenceableParamGroup");

                //sample cache
                logger.info("Init Sample cache");
                initIdMapCache(sampleIdMap, "/sampleList/sample");

                //software cache
                logger.info("Init Software cache");
                initIdMapCache(softwareIdMap, "/softwareList/software");

                //sourceFile cache
                logger.info("Init SourceFile cache");
                initIdMapCache(sourceFileIdMap, "/fileDescription/sourceFileList/sourceFile");

                //spectrum cache
                logger.info("Init Spectrum cache");
                initIdMapCache(spectrumIdMap, "/run/spectrumList/spectrum");

            } catch (IOException e) {
                logger.error("MzMLIndexerFactory$MzMlIndexerImpl.MzMlIndexerImpl", e);
                throw new IllegalStateException("Could not generate index file for: " + xmlFile);
            }

        }

        private void initIdMapCache(HashMap<String, IndexElement> idMap, String xpath) throws IOException {
            List<IndexElement> ranges = index.getElements(root + xpath);
            for (IndexElement byteRange : ranges) {
                String xml = readXML(byteRange);
                String id = getIdFromRawXML(xml);
                if (id != null) {
                    idMap.put(id, byteRange);
                }
            }
        }

        private String getIdFromRawXML(String xml) {
            Matcher match = ID_PATTERN.matcher(xml);
            if (match.find()) {
                return match.group(1);
            } else {
                throw new IllegalStateException("Invalid ID in xml: " + xml);
            }
        }

        public Iterator<String> getXmlStringIterator(String xpathExpression) {

            //since we're appending the root we've already checked, make
            //sure that the xpath doesn't erroneously contain that root
            String unrootedXpath = xpathExpression;
            if (unrootedXpath.startsWith("/indexedmzML")) {
                unrootedXpath = unrootedXpath.substring("/indexedmzML".length());
                logger.debug("removed /indexedmzML root expression");
            }
            if (unrootedXpath.startsWith("/mzML")) {
                unrootedXpath = unrootedXpath.substring("/mzML".length());
                logger.debug("removed /mzML root expression");
            }
            return xpathAccess.getXmlSnippetIterator(root + unrootedXpath);
        }

        public String getXmlString(String ID, Constants.ReferencedType type) {

            logger.debug("Getting cached ID: " + ID + " from cache: " + type);

            String xml;
            switch (type) {

                case CV:
                    xml = readXML(cvIdMap.get(ID));
                    break;
                case DataProcessing:
                    xml = readXML(dataProcessingIdMap.get(ID));
                    break;
                case InstrumentConfiguration:
                    xml = readXML(instrConfigIdMap.get(ID));
                    break;
                case ReferenceableParamGroup:
                    xml = readXML(refParamGroupIdMap.get(ID));
                    break;
                case Sample:
                    xml = readXML(sampleIdMap.get(ID));
                    break;
                case Software:
                    xml = readXML(softwareIdMap.get(ID));
                    break;
                case SourceFile:
                    xml = readXML(sourceFileIdMap.get(ID));
                    break;
                case Spectrum:
                    xml = readXML(spectrumIdMap.get(ID));
                    break;
                default:
                    throw new IllegalStateException("Unkonwn cache type: " + type);

            }

            return xml;

        }

        private String readXML(IndexElement byteRange) {
            try {
                if (byteRange != null) {
                    return xmlExtractor.readString(byteRange.getStart(), byteRange.getStop(), xmlFile);
                } else {
                    throw new IllegalStateException("Attempting to read NULL ByteRange");
                }
            } catch (IOException e) {
                logger.error("MzMLIndexerFactory$MzMlIndexerImpl.readXML", e);
                throw new IllegalStateException("Could not extract XML from file: " + xmlFile);
            }
        }

    }

}
