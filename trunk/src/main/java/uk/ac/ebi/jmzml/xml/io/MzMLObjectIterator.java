package uk.ac.ebi.jmzml.xml.io;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import uk.ac.ebi.jmzml.model.mzml.interfaces.MzMLObject;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.UnmarshallerFactory;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.filters.MzMLNamespaceFilter;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.StringReader;
import java.util.Iterator;

public class MzMLObjectIterator<X extends MzMLObject> implements Iterator<X> {

    private static Logger logger = Logger.getLogger(MzMLObjectIterator.class);

    private MzMLIndexer index;

    private Iterator<String> innerXpathIterator;
    private String xpath;
    private Class cls;
    private AdapterObjectCache cache;

    //package level constructor!
    MzMLObjectIterator(String xpath, Class cls, MzMLIndexer index, AdapterObjectCache cache) {
        innerXpathIterator = index.getXmlStringIterator(xpath);
        this.xpath = xpath;
        this.cls = cls;
        this.index = index;
        this.cache = cache;
    }

    public boolean hasNext() {
        return innerXpathIterator.hasNext();
    }

    public X next() {

        try {
            String xmlSt = innerXpathIterator.next();

            if (logger.isDebugEnabled()) {
                logger.debug("XML to unmarshal: " + xmlSt);
            }

            //required for the addition of namespaces to top-level objects
            MzMLNamespaceFilter xmlFilter = new MzMLNamespaceFilter();
            //initializeUnmarshaller will assign the proper reader to the xmlFilter
            Unmarshaller unmarshaller = UnmarshallerFactory.getInstance().initializeUnmarshaller(index, xmlFilter, cache);
            //unmarshall the desired object
            JAXBElement<X> holder = unmarshaller.unmarshal(new SAXSource(xmlFilter, new InputSource(new StringReader(xmlSt))), cls);

            X retval = holder.getValue();

            if (logger.isDebugEnabled()) {
                logger.debug("unmarshalled object = " + retval);
            }

            return retval;
        } catch (JAXBException e) {
            logger.error("MzMLObjectIterator.next", e);
            throw new IllegalStateException("Could not unmarshal object at xpath:" + xpath);
        }

    }

    public void remove() {
        throw new UnsupportedOperationException(MzMLObjectIterator.class.getName() + " can't be used to remove objects while iterating");
    }

}
