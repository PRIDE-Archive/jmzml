package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.Software;
import uk.ac.ebi.jmzml.model.mzml.ScanSettings;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.Constants;

/**
 * @author Florian Reisinger
 * @since 0.4
 */
public class ScanSettingsAdapter  extends AbstractResolvingAdapter<String, ScanSettings> {

    public ScanSettingsAdapter(MzMLIndexer index, AdapterObjectCache cache) {
        super(index, cache);
    }

    public ScanSettings unmarshal(String refId) {
        ScanSettings retval;
        if (cache.getCachedObject(refId, ScanSettings.class) != null) {
            retval = (ScanSettings) cache.getCachedObject(refId, ScanSettings.class);
            logger.debug("used cached value for ID: " + refId);
        } else {
            retval = super.unmarshal(refId, Constants.ReferencedType.ScanSettings);
            cache.putInCache(refId, retval);
            logger.debug("cached object at ID: " + refId);
        }
        return retval;
    }

    public String marshal(ScanSettings scanSettings) {
        if (scanSettings != null) {
            return scanSettings.getId();
        } else {
            return null;
        }
    }
    
}
