/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.xml.jaxb.adapters.SpectrumAdapter
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

import uk.ac.ebi.jmzml.model.mzml.Spectrum;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

public class SpectrumAdapter extends AbstractResolvingAdapter<String, Spectrum> {

    public SpectrumAdapter(MzMLIndexer index, AdapterObjectCache cache, boolean aUseSpectrumCache) {
        super(index, cache, aUseSpectrumCache);
    }

    public Spectrum unmarshal(String refId) {
        Spectrum retval;
        // See if we're using cache at all.
        if(useSpectrumCache) {
            if (cache.getCachedObject(refId, Constants.ReferencedType.Spectrum) != null) {
                retval = cache.getCachedObject(refId, Constants.ReferencedType.Spectrum);
                logger.debug("used cached value for ID: " + refId);
            } else {
                retval = super.unmarshal(refId, Constants.ReferencedType.Spectrum);
                cache.setCachedObject(refId, Constants.ReferencedType.Spectrum, retval);
                logger.debug("cached object at ID: " + refId);
            }
        } else {
            retval = super.unmarshal(refId, Constants.ReferencedType.Spectrum);
            logger.debug("retrieved object at ID: " + refId + " without caching.");
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
