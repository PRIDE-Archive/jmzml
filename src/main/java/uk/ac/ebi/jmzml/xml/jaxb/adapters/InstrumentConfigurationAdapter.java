package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.InstrumentConfiguration;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

/**
 * User: rcote
 * Date: 10-Jun-2008
 * Time: 15:56:57
 * $Id: $
 */
public class InstrumentConfigurationAdapter extends AbstractResolvingAdapter<String, InstrumentConfiguration> {

    public InstrumentConfigurationAdapter(MzMLIndexer index, AdapterObjectCache cache) {
        super(index, cache);
    }

    public InstrumentConfiguration unmarshal(String refId) {
        InstrumentConfiguration retval;
        if (cache.getCachedObject(refId, Constants.ReferencedType.InstrumentConfiguration) != null) {
            retval = cache.getCachedObject(refId, Constants.ReferencedType.InstrumentConfiguration);
            logger.debug("used cached value for ID: " + refId);
        } else {
            retval = super.unmarshal(refId, Constants.ReferencedType.InstrumentConfiguration);
            cache.setCachedObject(refId, Constants.ReferencedType.InstrumentConfiguration, retval);
            logger.debug("cached object at ID: " + refId);
        }
        return retval;
    }

    public String marshal(InstrumentConfiguration instrumentConfiguration) {
        if (instrumentConfiguration != null) {
            return instrumentConfiguration.getId();
        } else {
            return null;
        }
    }

}
