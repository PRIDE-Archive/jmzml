/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer
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

package uk.ac.ebi.jmzml.xml.xxindex;

import psidev.psi.tools.xxindex.index.IndexElement;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface MzMLIndexer {

    Iterator<String> getXmlStringIterator(String xpathExpression);

    String getXmlString(String ID, Class clazz);

    String getXmlString(IndexElement indexElement);

    int getCount(String xpathExpression);

    String getXmlString(String xpath, long offset);

    List<IndexElement> getIndexElements(String xpathExpression);

    Set<String> getXpath();

    Set<String> getSpectrumIDs();

    Set<Integer> getSpectrumIndexes();

    String getSpectrumIDFromSpectrumIndex(Integer index);

    Set<String> getChromatogramIDs();

    String getMzMLAttributeXMLString();

    String getStartTag(String xpath);

    /**
     * @param id    the unique ID (from the id attribute) of an XML element.
     * @param clazz the Java Class representing the element.
     * @return the complete start tag for the XML element with all specified attributes.
     */
    String getStartTag(String id, Class clazz);

}
