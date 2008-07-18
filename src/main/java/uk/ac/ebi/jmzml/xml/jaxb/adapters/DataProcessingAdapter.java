package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.DataProcessing;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

/**
 * User: rcote
 * Date: 10-Jun-2008
 * Time: 15:57:13
 * $Id: $
 */
public class DataProcessingAdapter extends AbstractResolvingAdapter<String, DataProcessing> {

    public DataProcessingAdapter(MzMLIndexer index, AdapterObjectCache cache) {
        super(index, cache);
    }

    public DataProcessing unmarshal(String refId) {
        DataProcessing retval;
        if (cache.getCachedObject(refId, Constants.ReferencedType.DataProcessing) != null) {
            retval = cache.getCachedObject(refId, Constants.ReferencedType.DataProcessing);
            logger.debug("used cached value for ID: " + refId);
        } else {
            retval = super.unmarshal(refId, Constants.ReferencedType.DataProcessing);
            cache.setCachedObject(refId, Constants.ReferencedType.DataProcessing, retval);
            logger.debug("cached object at ID: " + refId);
        }
        return retval;
    }

    public String marshal(DataProcessing dataProcessing) {
        if (dataProcessing != null) {
            return dataProcessing.getId();
        } else {
            return null;
        }
    }

}
