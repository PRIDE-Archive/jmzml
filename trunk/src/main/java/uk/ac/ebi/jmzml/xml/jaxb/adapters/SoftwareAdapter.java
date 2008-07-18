package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.Software;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

/**
 * User: rcote
 * Date: 10-Jun-2008
 * Time: 15:57:57
 * $Id: $
 */
public class SoftwareAdapter extends AbstractResolvingAdapter<String, Software> {

    public SoftwareAdapter(MzMLIndexer index, AdapterObjectCache cache) {
        super(index, cache);
    }

    public Software unmarshal(String refId) {
        Software retval;
        if (cache.getCachedObject(refId, Constants.ReferencedType.Software) != null) {
            retval = cache.getCachedObject(refId, Constants.ReferencedType.Software);
            logger.debug("used cached value for ID: " + refId);
        } else {
            retval = super.unmarshal(refId, Constants.ReferencedType.Software);
            cache.setCachedObject(refId, Constants.ReferencedType.Software, retval);
            logger.debug("cached object at ID: " + refId);
        }
        return retval;
    }

    public String marshal(Software software) {
        if (software != null) {
            return software.getId();
        } else {
            return null;
        }
    }
}
