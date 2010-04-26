/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller
 *
 * jmzml is Copyright 2008 The European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 */

package uk.ac.ebi.jmzml.xml.io;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import uk.ac.ebi.jmzml.model.mzml.*;
import uk.ac.ebi.jmzml.model.mzml.interfaces.MzMLObject;
import uk.ac.ebi.jmzml.model.mzml.utilities.ModelConstants;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.UnmarshallerFactory;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.filters.MzMLNamespaceFilter;
import uk.ac.ebi.jmzml.xml.xxindex.FileUtils;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexerFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.*;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MzMLUnmarshaller {

    private static final Logger logger = Logger.getLogger(MzMLUnmarshaller.class);
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    private final File mzMLFile;
    private final MzMLIndexer index;
    private final boolean useSpectrumCache;
    private final AdapterObjectCache cache = new AdapterObjectCache();

    private IndexList indexList = null;
    private boolean fileCorrupted = false;



    private final Pattern ID_PATTERN = Pattern.compile(".*id *= *\"([^\"]*)?\".*");
    private final Pattern AC_PATTERN = Pattern.compile(".*accession *= *\"([^\"]*)?\".*");
    private final Pattern VERSION_PATTERN = Pattern.compile(".*version *= *\"([^\"]*)?\".*");

    public MzMLUnmarshaller(URL mzMLFileURL) {
        this(mzMLFileURL, true);
    }

    public MzMLUnmarshaller(File mzMLFile) {
        this(mzMLFile, true);

    }

    public MzMLUnmarshaller(URL mzMLFileURL, boolean aUseSpectrumCache) {
        this(FileUtils.getFileFromURL(mzMLFileURL), aUseSpectrumCache);
    }

    public MzMLUnmarshaller(File mzMLFile, boolean aUseSpectrumCache) {
        this.mzMLFile = mzMLFile;
        index = MzMLIndexerFactory.getInstance().buildIndex(mzMLFile);
        useSpectrumCache = aUseSpectrumCache;
    }


    /**
     * USE WITH CAUTION - This will unmarshall a complete MzML object and
     * will likely cause an OutOfMemoryError for very large files
     * @return
     */
    public MzML unmarshall() {
        return unmarshalFromXpath("", MzML.class);
    }

    public String getMzMLVersion(){
        Matcher match = VERSION_PATTERN.matcher(index.getMzMLAttributeXMLString());
        if (match.matches()){
            return match.group();
        } else {
            return null;
        }
    }

    public String getMzMLAccession(){
        Matcher match = AC_PATTERN.matcher(index.getMzMLAttributeXMLString());
        if (match.matches()){
            return match.group();
        } else {
            return null;
        }
    }

    public String getMzMLId(){
        Matcher match = ID_PATTERN.matcher(index.getMzMLAttributeXMLString());
        if (match.matches()){
            return match.group();
        } else {
            return null;
        }
    }

    public int getObjectCountForXpath(String xpath) {
        return index.getCount(xpath);
    }

    public <T extends MzMLObject> T unmarshalFromXpath(String xpath, Class cls) {
        // ToDo: only unmarshalls first element in xxindex!! Document this!
        T retval = null;
        try {
            Iterator<String> xpathIter = index.getXmlStringIterator(xpath);

            if (xpathIter.hasNext()) {

                String xmlSt = xpathIter.next();

                if (logger.isDebugEnabled()) {
                    logger.trace("XML to unmarshal: " + xmlSt);
                }

                //required for the addition of namespaces to top-level objects
                MzMLNamespaceFilter xmlFilter = new MzMLNamespaceFilter();
                //initializeUnmarshaller will assign the proper reader to the xmlFilter
                Unmarshaller unmarshaller = UnmarshallerFactory.getInstance().initializeUnmarshaller(index, xmlFilter, cache, useSpectrumCache);
                //unmarshall the desired object
                JAXBElement<T> holder = unmarshaller.unmarshal(new SAXSource(xmlFilter, new InputSource(new StringReader(xmlSt))), cls);
                retval = holder.getValue();

                if (logger.isDebugEnabled()) {
                    logger.debug("unmarshalled object = " + retval);
                }

            }

        } catch (JAXBException e) {
            logger.error("MzMLUnmarshaller.unmarshalFromXpath", e);
            throw new IllegalStateException("Could not unmarshal object at xpath:" + xpath);
        }

        return retval;
    }

    public <T extends MzMLObject> MzMLObjectIterator<T> unmarshalCollectionFromXpath(String xpath, Class cls) {
        return new MzMLObjectIterator<T>(xpath, cls, index, cache, useSpectrumCache);
    }


    ///// ///// ///// ///// ///// ///// ///// ///// ///// //////
    // additional unmarshal operations for indexedmzML

    // ToDo: add schema validation step or implicit validation with the marshaller/unmarshaller

    public boolean isIndexedmzML() {
        // ToDo: find better way to check this?
        // ToDo: maybe change log level in StandardXpathAccess class
        // this check will log an ERROR if it is not an indexedmzML file, since we
        // are trying to retrieve an entry that will not be in the XML
        Iterator iter = index.getXmlStringIterator("/indexedmzML/indexList");
        return iter.hasNext();
    }

    public boolean isOkFileChecksum() throws MzMLUnmarshallerException {
        // if we already have established that the checksum has changed, then don't check again
        if (fileCorrupted) {
            return false;
        }

        // if it is not even an indexedmzML, then we throw an exception right away
        if (!isIndexedmzML()) {
            throw new MzMLUnmarshallerException("Attempted check of file checksum on un-indexed mzML file.");
        }

        // ok, now compare the two checksums (provided and calculated)
        String indexChecksum = getFileChecksumFromIndex();
        logger.info("provided checksum (index)  : " + indexChecksum);
        String calcChecksum = calculateChecksum();
        logger.info("calculated checksum (jmzml): " + calcChecksum);
        boolean checkSumOK = indexChecksum.equals(calcChecksum);
//        boolean checkSumOK = true;

        // if the checksums don't match, mark the file as corrupted
        if (!checkSumOK) {
            fileCorrupted = true;
        }

        return checkSumOK;
    }

    public IndexList getMzMLIndex() throws MzMLUnmarshallerException {
        IndexList retval;
        // check if already cached
        if (indexList == null) {
            // not yet cached, so we have to unmarshal it
            if (isOkFileChecksum()) {
                retval = unmarshalFromXpath("/indexedmzML/indexList", IndexList.class);
                indexList = retval; // save, so we don't have to generate it again
            } else {
                throw new MzMLUnmarshallerException("File checksum did not match! This file has been changed after the index was created. The index is invalid.");
            }
        } else {
            retval = indexList;
        }

        return retval;
    }

    public Spectrum getSpectrumById(String aID) throws MzMLUnmarshallerException {
        Spectrum result = null;
        String xml = index.getXmlString(aID, Constants.ReferencedType.Spectrum);
        try {
            //required for the addition of namespaces to top-level objects
            MzMLNamespaceFilter xmlFilter = new MzMLNamespaceFilter();
            //initializeUnmarshaller will assign the proper reader to the xmlFilter
            Unmarshaller unmarshaller = UnmarshallerFactory.getInstance().initializeUnmarshaller(index, xmlFilter, cache, useSpectrumCache);
            //unmarshall the desired object
            JAXBElement<Spectrum> holder = unmarshaller.unmarshal(new SAXSource(xmlFilter, new InputSource(new StringReader(xml))), Spectrum.class);
            result = holder.getValue();
        } catch(JAXBException je) {
            logger.error("MzMLUnmarshaller.getSpectrumByID", je);
            throw new IllegalStateException("Could not unmarshal spectrum with ID: " + aID);
        }
        return result;
    }

    public Chromatogram getChromatogramById(String aID) throws MzMLUnmarshallerException {
        Chromatogram result = null;
        String xml = index.getXmlString(aID, Constants.ReferencedType.Chromatogram);
        try {
            //required for the addition of namespaces to top-level objects
            MzMLNamespaceFilter xmlFilter = new MzMLNamespaceFilter();
            //initializeUnmarshaller will assign the proper reader to the xmlFilter
            Unmarshaller unmarshaller = UnmarshallerFactory.getInstance().initializeUnmarshaller(index, xmlFilter, cache, useSpectrumCache);
            //unmarshall the desired object
            JAXBElement<Chromatogram> holder = unmarshaller.unmarshal(new SAXSource(xmlFilter, new InputSource(new StringReader(xml))), Chromatogram.class);
            result = holder.getValue();
        } catch(JAXBException je) {
            logger.error("MzMLUnmarshaller.getChromatogramByID", je);
            throw new IllegalStateException("Could not unmarshal chromatogram with ID: " + aID);
        }
        return result;
    }



    public Spectrum getSpectrumByRefId(String refId) throws MzMLUnmarshallerException {
        // get the index entry for 'chromatogram'
        Index aIndexEntry = getIndex("spectrum");

        // find the offset for the specified refId
        for (Offset offset : aIndexEntry.getOffset()) {
            if ( offset.getIdRef().equalsIgnoreCase(refId) ) {
                return  getElementByOffset("spectrum", offset.getValue());
            }
        }

        return null;
    }

    public Spectrum getSpectrumBySpotId(String spotId) throws MzMLUnmarshallerException {
        // get the index entry for 'chromatogram'
        Index aIndexEntry = getIndex("spectrum");

        // find the offset for the specified spotId
        for (Offset offset : aIndexEntry.getOffset()) {
            if ( offset.getSpotID() != null && offset.getSpotID().equalsIgnoreCase(spotId) ) {
                return  getElementByOffset("spectrum", offset.getValue());
            }
        }

        return null;
    }

    public Spectrum getSpectrumByScanTime(double scanTime) throws MzMLUnmarshallerException {
        // get the index entry for 'chromatogram'
        Index aIndexEntry = getIndex("spectrum");

        // find the offset for the specified scanTime
        for (Offset offset : aIndexEntry.getOffset()) {
            if (offset.getScanTime() != null && offset.getScanTime() == scanTime ) {
                return  getElementByOffset("spectrum", offset.getValue());
            }
        }

        return null;
    }

    public Chromatogram getChromatogramByRefId(String refId) throws MzMLUnmarshallerException {
        // get the index entry for 'chromatogram'
        Index aIndexEntry = getIndex("chromatogram");

        // find the offset for the specified refId
        for (Offset offset : aIndexEntry.getOffset()) {
            // we are only interested in a particular refId
            if ( offset.getIdRef().equalsIgnoreCase(refId) ) {
                return  getElementByOffset("chromatogram", offset.getValue());
            }
        }

        return null;
    }

    public Set<String> getSpectrumIDs() {
        return this.index.getSpectrumIDs();
    }

    public Set<String> getChromatogramIDs() {
        return this.index.getChromatogramIDs();
    }

    ///// ///// ///// ///// ///// ///// ///// ///// ///// //////
    // private helper method primarily for indexedmzML stuff

    private String getFileChecksumFromIndex() throws MzMLUnmarshallerException {
        // there will only be a fileChecksum tag it is a indexedmzML
        if (!isIndexedmzML()) {
            throw new MzMLUnmarshallerException("Can not retrieve fileChecksum from a non indexed mzML file!");
        }

        // now fetch the fileChecksum stored in the indexedmzML
        String checksum;
        Iterator<String> snipIter = index.getXmlStringIterator("/indexedmzML/fileChecksum");
        if (snipIter.hasNext()) {
            String snippet = snipIter.next();
            // we need to cut of the start and stop tag
//            checksum = snippet.substring(14, snippet.length()-15).intern();
            String test = snippet.replace("<fileChecksum>", "");
            checksum = test.replace("</fileChecksum>", "").trim().intern();
        } else {
            throw new IllegalStateException("Could not find fileChecksum tag in indexedmzML: " + mzMLFile.getName());
        }

        return checksum;
    }

    private String calculateChecksum() {
        // we have to create the checksum for the mzML file (from its beginning to the
        // end of the fileChecksum start tag).
        // Since this stop location is very near the end of the file, we skip everything
        // until we come within a certain limit of the end of the file
        long limit = mzMLFile.length() - 200L;
        logger.debug("Looking for fileChecksum tag between byte " + limit +
                     " and byte " + mzMLFile.length() + " (the end) of the mzML file.");

        // initialize the hash algorithm
        MessageDigest hash;
        try {
            hash = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-1 not recognized as Secure Hash Algorithm.", e);
        }

        // create the input stream that will calculate the checksum
        FileInputStream fis;
        try {
            fis = new FileInputStream(mzMLFile);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("File " + mzMLFile.getAbsoluteFile() + " could not be found!", e);
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        DigestInputStream dis = new DigestInputStream(bis, hash);

        // prepare for input stream processing
        // we read through the file until we reach a specified limit before the end of the file
        // from there we populate a buffer with the read bytes (characters) and check if we have
        // already reached the position up to where we have to calculate the hash.
        CircularFifoBuffer bBuf = new CircularFifoBuffer(15);
        long cnt = 0; // counter to keep track of our position
        byte[] b = new byte[1]; // we only read one byte at a time
        try {
            while (dis.read(b) >= 0) {
                bBuf.add(b[0]);
                cnt++;
                // check if we have already reached the last bit of the file, where we have
                // to find the right position to stop (after the 'fileChecksum' start tag)
                if (cnt > limit) {
                    // we should have reached the start of the <fileChecksum> tag,
                    // now we have to find the end
                    String readBuffer = convert2String(bBuf);
                    if (readBuffer.endsWith("<fileChecksum>")) {
                        // we have found the end of the fileChecksum start tag, we have to stop the hash
                        if (b[0] != '>') { // check that we are really at the last character of the tag
                            throw new IllegalStateException("We are not at the end of <fileChecksum> tag!");
                        }
                        break;
                    }
                } // else if not yet near the end of the file, just keep on going
            }
            dis.close();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read from file '" + mzMLFile.getAbsolutePath() +
                    "' while trying ot calculate hash.", e);
        }
        logger.debug("Read over " + cnt + " bytes while calculating the file hash.");

        byte[] bytesDigest = dis.getMessageDigest().digest();

        return asHex(bytesDigest);
    }

    private String convert2String(CircularFifoBuffer bBuf) {
        byte[] tmp = new byte[bBuf.size()];
        int tmpCnt = 0;
        for (Object aBBuf : bBuf) {
            tmp[tmpCnt++] = (Byte) aBBuf;
        }
        return new String(tmp);
    }

    public static String asHex(byte[] buf) {
        // from: http://forums.xkcd.com/viewtopic.php?f=11&t=16666&p=553936
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    private Index getIndex(String elementName) throws MzMLUnmarshallerException {
        IndexList list = getMzMLIndex();

        for (Index entry : list.getIndex()) {
            if ( entry.getName().equalsIgnoreCase(elementName) ) {
                return entry;
            }
        }

        return null;
    }

    private <T extends MzMLObject> T getElementByOffset(String elementName, long offset) throws MzMLUnmarshallerException {
        // now check if we can map the elementName to a xpath from the xxindex
        String aXpath = null;
        for (String xxindexPath : index.getXpath()) {
            // we are looking for a xpath that ends in the elementName (e.g. points to
            // an element with the requested name)
            if ( xxindexPath.endsWith(elementName) ) {
                aXpath = xxindexPath;
            }
        }
        // if we don't have the xpath, then this method has been used incorrectly!
        if (aXpath == null) {
            throw new MzMLUnmarshallerException("Could not find a valid xpath " +
                    "(in xxindex) for the requested mzML index element '" + elementName + "'!");
        }

        // now that we have the xpath to use for the requested element, check if the xxindex
        // contains an element start position that matches the offset of the desired element
        String xmlSnippet = index.getXmlString(aXpath, offset);
        if (xmlSnippet == null) {
            throw new MzMLUnmarshallerException("No element '" + elementName + "' with the specified " +
                    "offset (" + offset + ") could be found (xpath: '" + aXpath + "')! Perhaps the " +
                    "mzML index containing the offset was corrupted.");
        }

        T retval;
        try {
            // ToDo: check this!! try to replace with standard unmarshaller!
            MzMLNamespaceFilter xmlFilter = new MzMLNamespaceFilter();
            // initializeUnmarshaller will assign the proper reader to the xmlFilter
            Unmarshaller unmarshaller = UnmarshallerFactory.getInstance().initializeUnmarshaller(index, xmlFilter, cache, useSpectrumCache);
            // unmarshall the desired object
            Class cls = ModelConstants.getClassForElementName(elementName);
            JAXBElement<T> holder = unmarshaller.unmarshal(new SAXSource(xmlFilter, new InputSource(new StringReader(xmlSnippet))), cls);
            retval = holder.getValue();
        } catch (JAXBException e) {
            logger.error("MzMLUnmarshaller.getObjectFromXml", e);
            throw new IllegalStateException("Could not unmarshal object from XML string:" + xmlSnippet);
        }

        return retval;
    }


}
