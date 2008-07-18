package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.Spectrum;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

/**
 * User: rcote
 * Date: 10-Jun-2008
 * Time: 15:55:52
 * $Id: $
 */
public class SpectrumAdapter extends AbstractResolvingAdapter<String, Spectrum> {

    public SpectrumAdapter(MzMLIndexer index, AdapterObjectCache cache) {
        super(index, cache);
    }

    public Spectrum unmarshal(String refId) {
        Spectrum retval;
        if (cache.getCachedObject(refId, Constants.ReferencedType.Spectrum) != null) {
            retval = cache.getCachedObject(refId, Constants.ReferencedType.Spectrum);
            logger.debug("used cached value for ID: " + refId);
        } else {
            retval = super.unmarshal(refId, Constants.ReferencedType.Spectrum);
            cache.setCachedObject(refId, Constants.ReferencedType.Spectrum, retval);
            logger.debug("cached object at ID: " + refId);
        }
        return retval;
    }

    public String marshal(Spectrum spectrum) {
        if (spectrum != null) {
            return spectrum.getId();
        } else {
            return null;
        }
    }

}
