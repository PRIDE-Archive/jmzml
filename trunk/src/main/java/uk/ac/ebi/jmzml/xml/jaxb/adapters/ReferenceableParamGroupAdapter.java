package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroup;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

/**
 * User: rcote
 * Date: 10-Jun-2008
 * Time: 15:58:55
 * $Id: $
 */
public class ReferenceableParamGroupAdapter extends AbstractResolvingAdapter<String, ReferenceableParamGroup> {

    public ReferenceableParamGroupAdapter(MzMLIndexer index, AdapterObjectCache cache) {
        super(index, cache);
    }

    public ReferenceableParamGroup unmarshal(String refId) {
        ReferenceableParamGroup retval;
        if (cache.getCachedObject(refId, Constants.ReferencedType.ReferenceableParamGroup) != null) {
            retval = cache.getCachedObject(refId, Constants.ReferencedType.ReferenceableParamGroup);
            logger.debug("used cached value for ID: " + refId);
        } else {
            retval = super.unmarshal(refId, Constants.ReferencedType.ReferenceableParamGroup);
            cache.setCachedObject(refId, Constants.ReferencedType.ReferenceableParamGroup, retval);
            logger.debug("cached object at ID: " + refId);
        }
        return retval;
    }

    public String marshal(ReferenceableParamGroup referenceableParamGroup) {
        if (referenceableParamGroup != null) {
            return referenceableParamGroup.getId();
        } else {
            return null;
        }
    }

}
