/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.xml.jaxb.adapters.ReferenceableParamGroupAdapter
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

import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroup;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

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
