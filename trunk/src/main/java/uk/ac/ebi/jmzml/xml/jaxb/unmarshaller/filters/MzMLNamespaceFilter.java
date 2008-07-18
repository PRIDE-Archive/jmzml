package uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.filters;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * User: rcote
 * Date: 18-Jan-2008
 * Time: 16:41:19
 * $Id: $
 */
public class MzMLNamespaceFilter extends XMLFilterImpl {

    /*
        note that if elementFormDefault is set to unqualified, there should be no
        namespace associated with the startElement (so the uri should be empty). If
        this is not the case (elementFormDefault = qualified), the filter will replace
        the uri with "" to simplify processing.
     */

    private static final Logger logger = Logger.getLogger(MzMLNamespaceFilter.class);

    public MzMLNamespaceFilter() {
        logger.debug("MzMLNamespaceFilter created. Remember to call setParent(XMLReader)");
    }

    public MzMLNamespaceFilter(XMLReader reader) {
        super(reader);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        super.startElement("", localName, qName, atts);
    }
}
