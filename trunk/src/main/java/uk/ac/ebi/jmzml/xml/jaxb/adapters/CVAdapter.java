package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.CV;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

/**
 * User: rcote
 * Date: 10-Jun-2008
 * Time: 15:57:42
 * $Id: $
 */
public class CVAdapter extends AbstractResolvingAdapter<String, CV> {

    public CVAdapter(MzMLIndexer index, AdapterObjectCache cache) {
        super(index, cache);
    }

    public CV unmarshal(String refId) {
        CV retval;
        if (cache.getCachedObject(refId, Constants.ReferencedType.CV) != null) {
            retval = cache.getCachedObject(refId, Constants.ReferencedType.CV);
            logger.debug("used cached value for ID: " + refId);
        } else {
            retval = super.unmarshal(refId, Constants.ReferencedType.CV);
            cache.setCachedObject(refId, Constants.ReferencedType.CV, retval);
            logger.debug("cached object at ID: " + refId);
        }
        return retval;
    }

    public String marshal(CV cv) {
        if (cv != null) {
            return cv.getId();
        } else {
            return null;
        }
    }

}
