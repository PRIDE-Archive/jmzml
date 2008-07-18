package uk.ac.ebi.jmzml.xml.xxindex;

import uk.ac.ebi.jmzml.xml.Constants;

import java.util.Iterator;

/**
 * User: rcote
 * Date: 11-Jun-2008
 * Time: 15:15:52
 * $Id: $
 */
public interface MzMLIndexer {

    public Iterator<String> getXmlStringIterator(String xpathExpression);

    public String getXmlString(String ID, Constants.ReferencedType type);

}
