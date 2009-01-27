/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.listeners.RawXMLListener
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

package uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.listeners;

import uk.ac.ebi.jmzml.model.mzml.*;
import uk.ac.ebi.jmzml.model.mzml.params.*;
import uk.ac.ebi.jmzml.model.mzml.utilities.ParamGroupUpdater;

import javax.xml.bind.Unmarshaller;
import java.util.List;
import java.util.ArrayList;

public class RawXMLListener extends Unmarshaller.Listener {


    @Override
    public void afterUnmarshal(Object target, Object parent) {

        // Here we handle all the referenced ParamGroups
        // Whenever we encounter a ParamGroup, there could be referenced params.
        // Since some params could be used more than in one location, they are not
        // duplicated in each XML snippet that needs them, but localized in a central
        // part of the XML and only referenced. Here we have to fetch those refernced
        // params from the central location and add them to the local element.  

        try {

            ///// ///// ///// ///// ///// ///// ///// ///// ///// /////
            // Update all ParamGroup Subclasses

            if ( target instanceof BinaryDataArray ) {
                ParamGroupUpdater.updateParamGroupSubclasses((BinaryDataArray) target, BinaryDataArrayCVParam.class, BinaryDataArrayUserParam.class);
            }

            if ( target instanceof Chromatogram ) {
                ParamGroupUpdater.updateParamGroupSubclasses((Chromatogram) target, ChromatogramCVParam.class, ChromatogramUserParam.class);
            }

            if ( target instanceof Component) {
                ParamGroupUpdater.updateParamGroupSubclasses((Component) target, ComponentCVParam.class, ComponentUserParam.class);
            }

            if ( target instanceof InstrumentConfiguration ) {
                ParamGroupUpdater.updateParamGroupSubclasses((InstrumentConfiguration) target, InstrumentConfigurationCVParam.class, InstrumentConfigurationUserParam.class);
            }

            if ( target instanceof ProcessingMethod ) {
                ParamGroupUpdater.updateParamGroupSubclasses((ProcessingMethod) target, ProcessingMethodCVParam.class, ProcessingMethodUserParam.class);
            }

            if ( target instanceof Run ) {
                ParamGroupUpdater.updateParamGroupSubclasses((Run) target, RunCVParam.class, RunUserParam.class);
            }

            if ( target instanceof Sample ) {
                ParamGroupUpdater.updateParamGroupSubclasses((Sample) target, SampleCVParam.class, SampleUserParam.class);
            }

            if ( target instanceof Scan ) {
                ParamGroupUpdater.updateParamGroupSubclasses((Scan) target, ScanCVParam.class, ScanUserParam.class);
            }

//            if ( target instanceof ScanList ) {
//                ParamGroupUpdater.updateParamGroupSubclasses((ScanList) target, ScanListCVParam.class, ScanListUserParam.class);
//            }

//            if ( target instanceof ScanSettings ) {
//                ParamGroupUpdater.updateParamGroupSubclasses((ScanSettings) target, ScanSettingsCVParam.class, ScanSettingsUserParam.class);
//            }

//            if ( target instanceof Software ) {
//                ParamGroupUpdater.updateParamGroupSubclasses((Software) target, SoftwareCVParam.class, SoftwareUserParam.class);
//            }

            if ( target instanceof SourceFile ) {
                ParamGroupUpdater.updateParamGroupSubclasses((SourceFile) target, SourceFileCVParam.class, SourceFileUserParam.class);
            }

            if ( target instanceof Spectrum ) {
                ParamGroupUpdater.updateParamGroupSubclasses((Spectrum) target, SpectrumCVParam.class, SpectrumUserParam.class);
            }

            ///// ///// ///// ///// ///// ///// ///// ///// ///// /////
            // Update all classes with ParamGroup members

            if ( target instanceof FileDescription ) {
                FileDescription tmp = (FileDescription) target;
                // update fileContent (ParamGroup)
                ParamGroupUpdater.updateParamGroupSubclasses(tmp.getFileContent(), FileDescriptionCVParam.class, FileDescriptionUserParam.class);
                // update contact (ParamGroup list)
                if (tmp.getContact() != null && !tmp.getContact().isEmpty()) {
                    List<ParamGroup> tmpContact = new ArrayList<ParamGroup>();
                    for (ParamGroup aContact : tmp.getContact()) {
                        ParamGroupUpdater.updateParamGroupSubclasses(aContact, ContactCVParam.class, ContactUserParam.class);
                        tmpContact.add(aContact);
                    }
                    tmp.getContact().clear();
                    tmp.getContact().addAll(tmpContact);
                }
            }

//            if ( target instanceof NeutralLoss ) {
//                Precursor tmp = (NeutralLoss) target;
//                //update activation (ParamGroup)
//                ParamGroupUpdater.updateParamGroupSubclasses(tmp.getActivation(), ActivationCVParam.class, ActivationUserParam.class);
//                //update ionSelection (ParamGroup)
//                ParamGroupUpdater.updateParamGroupSubclasses(tmp.getIsolationWindow(), IsolationWindowCVParam.class, IsolationWindowUserParam.class);
//            }

            if ( target instanceof Precursor ) {
                Precursor tmp = (Precursor) target;
                //update activation (ParamGroup)
                ParamGroupUpdater.updateParamGroupSubclasses(tmp.getActivation(), ActivationCVParam.class, ActivationUserParam.class);
                //update ionSelection (ParamGroup)
                ParamGroupUpdater.updateParamGroupSubclasses(tmp.getIsolationWindow(), IsolationWindowCVParam.class, IsolationWindowUserParam.class);
            }

//            if ( target instanceof Product ) {
//                Precursor tmp = (Product) target;
//                //update activation (ParamGroup)
//                ParamGroupUpdater.updateParamGroupSubclasses(tmp.getActivation(), ActivationCVParam.class, ActivationUserParam.class);
//                //update ionSelection (ParamGroup)
//                ParamGroupUpdater.updateParamGroupSubclasses(tmp.getIsolationWindow(), IsolationWindowCVParam.class, IsolationWindowUserParam.class);
//            }

            if ( target instanceof SelectedIonList ) {
                SelectedIonList tmp = (SelectedIonList) target;
                if (tmp.getSelectedIon() != null && !tmp.getSelectedIon().isEmpty()) {
                    List<ParamGroup> tmpList = new ArrayList<ParamGroup>();
                    for (ParamGroup pg : tmp.getSelectedIon()) {
                        ParamGroupUpdater.updateParamGroupSubclasses(pg, SelectedIonCVParam.class, SelectedIonUserParam.class);
                        tmpList.add(pg);
                    }
                    tmp.getSelectedIon().clear();
                    tmp.getSelectedIon().addAll(tmpList);
                }
            }

            if ( target instanceof TargetList ) {
                TargetList tmp = (TargetList) target;
                if (tmp.getTarget() != null && !tmp.getTarget().isEmpty()) {
                    List<ParamGroup> tmpList = new ArrayList<ParamGroup>();
                    for (ParamGroup pg : tmp.getTarget()) {
                        ParamGroupUpdater.updateParamGroupSubclasses(pg, TargetCVParam.class, TargetUserParam.class);
                        tmpList.add(pg);
                    }
                    tmp.getTarget().clear();
                    tmp.getTarget().addAll(tmpList);
                }
            }

            ///// ///// ///// ///// ///// ///// ///// ///// ///// /////
            // Update all CvParam Subclasses

            if ( target instanceof ScanWindow ) {
                ScanWindow tmp = (ScanWindow) target;
                // update CvParam
                ParamGroupUpdater.updateCVParamSubclasses(tmp.getCvParam(), ScanWindowCVParam.class);
            }

        } catch (InstantiationException e) {
            throw new RuntimeException(this.getClass().getName() + ".afterUnmarshall: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(this.getClass().getName() + ".afterUnmarshall: " + e.getMessage());
        }


    }
}
