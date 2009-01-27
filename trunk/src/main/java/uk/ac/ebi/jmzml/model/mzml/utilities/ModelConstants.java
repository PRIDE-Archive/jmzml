/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.model.mzml.utilities.ModelConstants
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

package uk.ac.ebi.jmzml.model.mzml.utilities;

import uk.ac.ebi.jmzml.model.mzml.*;
import uk.ac.ebi.jmzml.model.mzml.interfaces.MzMLObject;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: rcote
 * Date: 13-Jun-2008
 * Time: 10:45:32
 * $Id: $
 */
public class ModelConstants {

    public static final String PACKAGE = MzML.class.getPackage().getName();
    public static final String MZML_NS = "http://psi.hupo.org/ms/mzml";

    private static Map<Class, QName> modelQNames = new HashMap<Class, QName>();

    static {
        modelQNames.put(AnalyzerComponent.class, new QName(MZML_NS, "analyzer"));
        modelQNames.put(BinaryDataArray.class, new QName(MZML_NS, "binaryDataArray"));
        modelQNames.put(BinaryDataArrayList.class, new QName(MZML_NS, "binaryDataArrayList"));
        modelQNames.put(CV.class, new QName(MZML_NS, "cv"));
        modelQNames.put(CVList.class, new QName(MZML_NS, "cvList"));
        modelQNames.put(CVParam.class, new QName(MZML_NS, "cvParam"));
        modelQNames.put(Chromatogram.class, new QName(MZML_NS, "chromatogram"));
        modelQNames.put(ChromatogramList.class, new QName(MZML_NS, "chromatogramList"));
        modelQNames.put(Component.class, new QName(MZML_NS, "component"));
        modelQNames.put(ComponentList.class, new QName(MZML_NS, "componentList"));
        modelQNames.put(DataProcessing.class, new QName(MZML_NS, "dataProcessing"));
        modelQNames.put(DataProcessingList.class, new QName(MZML_NS, "dataProcessingList"));
        modelQNames.put(DetectorComponent.class, new QName(MZML_NS, "detector"));
        modelQNames.put(FileDescription.class, new QName(MZML_NS, "fileDescription"));
        modelQNames.put(InstrumentConfiguration.class, new QName(MZML_NS, "instrumentConfiguration"));
        modelQNames.put(InstrumentConfigurationList.class, new QName(MZML_NS, "instrumentConfigurationList"));
        modelQNames.put(MzML.class, new QName(MZML_NS, "mzML"));
        modelQNames.put(ParamGroup.class, new QName(MZML_NS, "paramGroup"));
        modelQNames.put(Precursor.class, new QName(MZML_NS, "precursor"));
        modelQNames.put(PrecursorList.class, new QName(MZML_NS, "precursorList"));
        modelQNames.put(ProcessingMethod.class, new QName(MZML_NS, "processingMethod"));
        modelQNames.put(ReferenceableParamGroup.class, new QName(MZML_NS, "referenceableParamGroup"));
        modelQNames.put(ReferenceableParamGroupList.class, new QName(MZML_NS, "referenceableParamGroupList"));
        modelQNames.put(ReferenceableParamGroupRef.class, new QName(MZML_NS, "referenceableParamGroupRef"));
        modelQNames.put(Run.class, new QName(MZML_NS, "run"));
        modelQNames.put(Sample.class, new QName(MZML_NS, "sample"));
        modelQNames.put(SampleList.class, new QName(MZML_NS, "sampleList"));
        modelQNames.put(Scan.class, new QName(MZML_NS, "scan"));
        modelQNames.put(ScanWindow.class, new QName(MZML_NS, "scanWindow"));
        modelQNames.put(ScanWindowList.class, new QName(MZML_NS, "scanWindowList"));
        modelQNames.put(SelectedIonList.class, new QName(MZML_NS, "selectedIonList"));
        modelQNames.put(Software.class, new QName(MZML_NS, "software"));
        modelQNames.put(SoftwareList.class, new QName(MZML_NS, "softwareList"));
        modelQNames.put(SoftwareRef.class, new QName(MZML_NS, "softwareRef"));
        modelQNames.put(SourceComponent.class, new QName(MZML_NS, "source"));
        modelQNames.put(SourceFile.class, new QName(MZML_NS, "sourceFile"));
        modelQNames.put(SourceFileList.class, new QName(MZML_NS, "sourceFileList"));
        modelQNames.put(SourceFileRef.class, new QName(MZML_NS, "sourceFileRef"));
        modelQNames.put(SourceFileRefList.class, new QName(MZML_NS, "sourceFileRefList"));
        modelQNames.put(Spectrum.class, new QName(MZML_NS, "spectrum"));
        modelQNames.put(SpectrumList.class, new QName(MZML_NS, "spectrumList"));
        modelQNames.put(TargetList.class, new QName(MZML_NS, "targetList"));
        modelQNames.put(UserParam.class, new QName(MZML_NS, "userParam"));

        //now make set unmodifiable
        modelQNames = Collections.unmodifiableMap(modelQNames);

    }


    public static boolean isRegisteredClass(Class cls) {
        return modelQNames.containsKey(cls);
    }

    public static QName getQNameForClass(Class cls) {
        if (isRegisteredClass(cls)) {
            return modelQNames.get(cls);
        } else {
            throw new IllegalStateException("No QName registered for class: " + cls);
        }
    }

    public static String getElementNameForClass(Class cls) {
        if (isRegisteredClass(cls)) {
            return modelQNames.get(cls).getLocalPart();
        } else {
            throw new IllegalStateException("No QName registered for class: " + cls);
        }
    }

    public static Class getClassForElementName(String name) {
        for (Map.Entry<Class, QName> entry : modelQNames.entrySet()) {
            if (entry.getValue().getLocalPart().equals(name)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
