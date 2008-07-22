/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.model.mzml.utilities.ParamGroupUpdater
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

import uk.ac.ebi.jmzml.model.mzml.CVParam;
import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.UserParam;

import java.util.ArrayList;
import java.util.List;

/**
 * User: rcote
 * Date: 23-Jan-2008
 * Time: 10:05:00
 * $Id: $
 */
public class ParamGroupUpdater {

    public static void updateParamGroupSubclasses(ParamGroup input, Class cvParamClass, Class userParamClass) throws IllegalAccessException, InstantiationException {

        if (input != null) {

            //update CVParams with instances of desired subclass
            List<CVParam> cvList = input.getCvParam();

            if (cvList != null) {
                updateCVParamSubclasses(input.getCvParam(), cvParamClass);
            }

            //update UserParams with instances of desired subclass
            List<UserParam> userList = input.getUserParam();

            if (userList != null) {
                updateUserParamSubclasses(input.getUserParam(), userParamClass);
            }

        }

    }

    private static void updateUserParamSubclasses(List<UserParam> input, Class userParamClass) throws IllegalAccessException, InstantiationException {

        if (input != null && !input.isEmpty()) {
            //tmp holder for cvParamClass
            List<UserParam> newList = new ArrayList();

            //iterate and create new params
            for (UserParam param : input) {

                //create new object
                UserParam newParam = (UserParam) userParamClass.newInstance();

                //copy fields
                //it will be essential that any changes to the base UserParamClass be propagated here!!!
                newParam.setType(param.getType());
                newParam.setName(param.getName());
                newParam.setValue(param.getValue());
                newList.add(newParam);
            }
            //replace with new list of params
            input.clear();
            input.addAll(newList);
        }

    }

    public static void updateCVParamSubclasses(List<CVParam> input, Class cvParamClass) throws IllegalAccessException, InstantiationException {

        if (input != null && !input.isEmpty()) {

            //tmp holder for cvParamClass
            List<CVParam> newList = new ArrayList();

            //iterate and create new params
            for (CVParam param : input) {

                //create new object
                CVParam newParam = (CVParam) cvParamClass.newInstance();

                //copy fields
                //it will be essential that any changes to the base CVParamClass be propagated here!!!
                newParam.setAccession(param.getAccession());
                newParam.setCV(param.getCV());
                newParam.setName(param.getName());
                newParam.setUnitAccession(param.getUnitAccession());
                newParam.setUnitCV(param.getUnitCV());
                newParam.setUnitName(param.getUnitName());
                newParam.setValue(param.getValue());
                newList.add(newParam);
            }
            //replace with new list of params
            input.clear();
            input.addAll(newList);
        }

    }


}
