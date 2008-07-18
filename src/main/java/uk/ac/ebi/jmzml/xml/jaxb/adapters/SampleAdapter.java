package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.Sample;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

/**
 * User: rcote
 * Date: 10-Jun-2008
 * Time: 15:59:22
 * $Id: $
 */
public class SampleAdapter extends AbstractResolvingAdapter<String, Sample> {

    public SampleAdapter(MzMLIndexer index, AdapterObjectCache cache) {
        super(index, cache);
    }

    public Sample unmarshal(String refId) {
        Sample retval;
        if (cache.getCachedObject(refId, Constants.ReferencedType.Sample) != null) {
            retval = cache.getCachedObject(refId, Constants.ReferencedType.Sample);
            logger.debug("used cached value for ID: " + refId);
        } else {
            retval = super.unmarshal(refId, Constants.ReferencedType.Sample);
            cache.setCachedObject(refId, Constants.ReferencedType.Sample, retval);
            logger.debug("cached object at ID: " + refId);
        }
        return retval;
    }

    public String marshal(Sample sample) {
        if (sample != null) {
            return sample.getId();
        } else {
            return null;
        }
    }

}
