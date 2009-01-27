/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.xml.Constants
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

package uk.ac.ebi.jmzml.xml;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Constants {
    // ToDo: ? move to ModelConstants ?

    public static final String JAXB_ENCODING_PROPERTY = "jaxb.encoding";
    public static final String JAXB_FORMATTING_PROPERTY = "jaxb.formatted.output";
    public static final String JAXB_SCHEMALOCATION_PROPERTY = "jaxb.schemaLocation";
    public static final String JAXB_FRAGMENT_PROPERTY = "jaxb.fragment";

    // ToDo: check if all necessary types are present
    public static enum ReferencedType {
        CV,
        DataProcessing,
        InstrumentConfiguration,
        ReferenceableParamGroup,
        Sample,
        Software,
        SourceFile,
        Spectrum,
        ScanSettings
    }

    private static Set<String> xpathsToIndex = new HashSet<String>();

    static {
        // Create the index inclusion list.
        // Note that both the '/mzML' and 'indexedmzML/mzML' versions are included.
        xpathsToIndex.add("/mzML");
        xpathsToIndex.add("/mzML/cvList");
        xpathsToIndex.add("/mzML/cvList/cv");
        xpathsToIndex.add("/mzML/fileDescription");
        xpathsToIndex.add("/mzML/fileDescription/sourceFileList/sourceFile");
        xpathsToIndex.add("/mzML/referenceableParamGroupList");
        xpathsToIndex.add("/mzML/referenceableParamGroupList/referenceableParamGroup");
        xpathsToIndex.add("/mzML/sampleList");
        xpathsToIndex.add("/mzML/sampleList/sample");
        xpathsToIndex.add("/mzML/scanSettingsList");
        xpathsToIndex.add("/mzML/scanSettingsList/scanSettings");
        xpathsToIndex.add("/mzML/softwareList");
        xpathsToIndex.add("/mzML/softwareList/software");
        xpathsToIndex.add("/mzML/instrumentConfigurationList");
        xpathsToIndex.add("/mzML/instrumentConfigurationList/instrumentConfiguration");
        xpathsToIndex.add("/mzML/dataProcessingList");
        xpathsToIndex.add("/mzML/dataProcessingList/dataProcessing");
        xpathsToIndex.add("/mzML/acquisitionSettingsList");
        xpathsToIndex.add("/mzML/run");
        xpathsToIndex.add("/mzML/run/spectrumList/spectrum");
        xpathsToIndex.add("/mzML/run/chromatogramList/chromatogram");

        //ensure that all /mzml keys are duplicated for /indexedmzML
        HashSet<String> mzmlKeys = new HashSet<String>(xpathsToIndex);
        for (Iterator<String> iterator = mzmlKeys.iterator(); iterator.hasNext();) {
            String mzmlString = iterator.next();
            xpathsToIndex.add("/indexedmzML" + mzmlString);
        }

        // add indexedmzML specific xpathes
        xpathsToIndex.add("/indexedmzML");
        xpathsToIndex.add("/indexedmzML/indexList");
        xpathsToIndex.add("/indexedmzML/fileChecksum");

        // finally make the set unmodifiable
        xpathsToIndex = Collections.unmodifiableSet(xpathsToIndex);
    }

    public static final Set<String> XML_INDEXED_XPATHS = xpathsToIndex;

}
