package uk.ac.ebi.jmzml.xml.io;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import uk.ac.ebi.jmzml.model.mzml.MzML;
import uk.ac.ebi.jmzml.model.mzml.interfaces.MzMLObject;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.UnmarshallerFactory;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.filters.MzMLNamespaceFilter;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexerFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;

/**
 * User: rcote
 * Date: 13-Jun-2008
 * Time: 10:39:29
 * $Id: $
 */
public class MzMLUnmarshaller {

    private static final Logger logger = Logger.getLogger(MzMLUnmarshaller.class);

    private MzMLIndexer index;
    private AdapterObjectCache cache = new AdapterObjectCache();

    public MzMLUnmarshaller(URL mzMLFileURL) {
        index = MzMLIndexerFactory.getInstance().buildIndex(mzMLFileURL);
    }

    public MzMLUnmarshaller(File mzMLFile) {
        index = MzMLIndexerFactory.getInstance().buildIndex(mzMLFile);
    }

    public MzML unmarshall() {
        return unmarshalFromXpath("", MzML.class);
    }

    public <T extends MzMLObject> T unmarshalFromXpath(String xpath, Class cls) {

        T retval = null;
        try {

            Iterator<String> xpathIter = index.getXmlStringIterator(xpath);

            while (xpathIter.hasNext()) {

                String xmlSt = xpathIter.next();

                if (logger.isDebugEnabled()) {
                    logger.debug("XML to unmarshal: " + xmlSt);
                }

                //required for the addition of namespaces to top-level objects
                MzMLNamespaceFilter xmlFilter = new MzMLNamespaceFilter();
                //initializeUnmarshaller will assign the proper reader to the xmlFilter
                Unmarshaller unmarshaller = UnmarshallerFactory.getInstance().initializeUnmarshaller(index, xmlFilter, cache);
                //unmarshall the desired object
                JAXBElement<T> holder = unmarshaller.unmarshal(new SAXSource(xmlFilter, new InputSource(new StringReader(xmlSt))), cls);
                retval = holder.getValue();

                if (logger.isDebugEnabled()) {
                    logger.debug("unmarshalled object = " + retval);
                }

                break;

            }

        } catch (JAXBException e) {
            logger.error("MzMLUnmarshaller.unmarshalFromXpath", e);
            throw new IllegalStateException("Could not unmarshal object at xpath:" + xpath);
        }

        return retval;

    }

    public <T extends MzMLObject> MzMLObjectIterator<T> unmarshalCollectionFromXpath(String xpath, Class cls) {
        return new MzMLObjectIterator<T>(xpath, cls, index, cache);
    }

}
