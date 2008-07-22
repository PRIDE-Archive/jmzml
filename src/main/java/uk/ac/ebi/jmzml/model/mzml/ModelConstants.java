/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.model.mzml.ModelConstants
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

package uk.ac.ebi.jmzml.model.mzml;

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

    private static Map<Class, String> qnames = new HashMap<Class, String>();

    static {
        qnames.put(Acquisition.class, "acquisition");
        qnames.put(AcquisitionList.class, "acquisitionList");
        qnames.put(AcquisitionSettings.class, "acquisitionSettings");
        qnames.put(AcquisitionSettingsList.class, "acquisitionSettingsList");
        qnames.put(AnalyzerComponent.class, "analyzer");
        qnames.put(BinaryDataArray.class, "binaryDataArray");
        qnames.put(BinaryDataArrayList.class, "binaryDataArrayList");
        qnames.put(CV.class, "cv");
        qnames.put(CVList.class, "cvList");
        qnames.put(CVParam.class, "cvParam");
        qnames.put(Chromatogram.class, "chromatogram");
        qnames.put(ChromatogramList.class, "chromatogramList");
        qnames.put(Component.class, "component");
        qnames.put(ComponentList.class, "componentList");
        qnames.put(DataProcessing.class, "dataProcessing");
        qnames.put(DataProcessingList.class, "dataProcessingList");
        qnames.put(DetectorComponent.class, "detector");
        qnames.put(FileDescription.class, "fileDescription");
        qnames.put(InstrumentConfiguration.class, "instrumentConfiguration");
        qnames.put(InstrumentConfigurationList.class, "instrumentConfigurationList");
        qnames.put(MzML.class, "mzML");
        qnames.put(ParamGroup.class, "paramGroup");
        qnames.put(Precursor.class, "precursor");
        qnames.put(PrecursorList.class, "precursorList");
        qnames.put(ProcessingMethod.class, "processingMethod");
        qnames.put(ReferenceableParamGroup.class, "referenceableParamGroup");
        qnames.put(ReferenceableParamGroupList.class, "referenceableParamGroupList");
        qnames.put(ReferenceableParamGroupRef.class, "referenceableParamGroupRef");
        qnames.put(Run.class, "run");
        qnames.put(Sample.class, "sample");
        qnames.put(SampleList.class, "sampleList");
        qnames.put(Scan.class, "scan");
        qnames.put(ScanWindow.class, "scanWindow");
        qnames.put(ScanWindowList.class, "scanWindowList");
        qnames.put(SelectedIonList.class, "selectedIonList");
        qnames.put(Software.class, "software");
        qnames.put(SoftwareList.class, "softwareList");
        qnames.put(SoftwareParam.class, "softwareParam");
        qnames.put(SoftwareRef.class, "softwareRef");
        qnames.put(SourceComponent.class, "source");
        qnames.put(SourceFile.class, "sourceFile");
        qnames.put(SourceFileList.class, "sourceFileList");
        qnames.put(SourceFileRef.class, "sourceFileRef");
        qnames.put(SourceFileRefList.class, "sourceFileRefList");
        qnames.put(Spectrum.class, "spectrum");
        qnames.put(SpectrumDescription.class, "spectrumDescription");
        qnames.put(SpectrumList.class, "spectrumList");
        qnames.put(TargetList.class, "targetList");
        qnames.put(UserParam.class, "userParam");

        //now make set unmodifiable
        qnames = Collections.unmodifiableMap(qnames);
    }

    public static String getQNameForClass(Class cls) {

        String qName = qnames.get(cls);
        if (qName != null) {
            return qName;
        } else {
            throw new IllegalStateException("No QName registered for class: " + cls);
        }

    }

}
