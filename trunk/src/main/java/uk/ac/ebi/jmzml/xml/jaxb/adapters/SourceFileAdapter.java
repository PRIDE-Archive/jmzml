package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.SourceFile;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

/**
 * User: rcote
 * Date: 10-Jun-2008
 * Time: 15:56:29
 * $Id: $
 */
public class SourceFileAdapter extends AbstractResolvingAdapter<String, SourceFile> {

    public SourceFileAdapter(MzMLIndexer index, AdapterObjectCache cache) {
        super(index, cache);
    }

    public SourceFile unmarshal(String refId) {
        SourceFile retval;
        if (cache.getCachedObject(refId, Constants.ReferencedType.SourceFile) != null) {
            retval = cache.getCachedObject(refId, Constants.ReferencedType.SourceFile);
            logger.debug("used cached value for ID: " + refId);
        } else {
            retval = super.unmarshal(refId, Constants.ReferencedType.SourceFile);
            cache.setCachedObject(refId, Constants.ReferencedType.SourceFile, retval);
            logger.debug("cached object at ID: " + refId);
        }
        return retval;
    }

    public String marshal(SourceFile sourceFile) {
        if (sourceFile != null) {
            return sourceFile.getId();
        } else {
            return null;
        }
    }

}
