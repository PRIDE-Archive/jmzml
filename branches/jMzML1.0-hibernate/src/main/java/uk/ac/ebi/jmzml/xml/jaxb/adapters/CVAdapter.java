/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.xml.jaxb.adapters.CVAdapter
 *
 * jmzml is Copyright 2008 The European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 */

package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.CV;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

public class CVAdapter extends AbstractResolvingAdapter<String, CV> {

    public CVAdapter(MzMLIndexer index, AdapterObjectCache cache) {
        super(index, cache);
    }

    public CV unmarshal(String refId) {
        CV retval;
        if (cache.getCachedObject(refId, CV.class) != null) {
            // ToDo: unchecked cast, may produce runtime exception! check if we can get around this
            // ToDo: maybe check returned type (but then we would have to throw exception anyway)
            retval = (CV) cache.getCachedObject(refId, CV.class);
            logger.debug("used cached value for ID: " + refId);
        } else {
            retval = super.unmarshal(refId, Constants.ReferencedType.CV);
            cache.putInCache(refId, retval);
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
