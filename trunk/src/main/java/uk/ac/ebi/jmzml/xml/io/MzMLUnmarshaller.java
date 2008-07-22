/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller
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

package uk.ac.ebi.jmzml.xml.io;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import uk.ac.ebi.jmzml.model.mzml.MzML;
import uk.ac.ebi.jmzml.model.mzml.interfaces.MzMLObject;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.UnmarshallerFactory;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.filters.MzMLNamespaceFilter;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexerFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;

public class MzMLUnmarshaller {

    private static final Logger logger = Logger.getLogger(MzMLUnmarshaller.class);

    private MzMLIndexer index;
    private AdapterObjectCache cache = new AdapterObjectCache();

    public MzMLUnmarshaller(URL mzMLFileURL) {
        index = MzMLIndexerFactory.getInstance().buildIndex(mzMLFileURL);
    }

    public MzMLUnmarshaller(File mzMLFile) {
        index = MzMLIndexerFactory.getInstance().buildIndex(mzMLFile);
    }

    public MzML unmarshall() {
        return unmarshalFromXpath("", MzML.class);
    }

    public int getObjectCountForXpath(String xpath) {
        return index.getCount(xpath);
    }

    public <T extends MzMLObject> T unmarshalFromXpath(String xpath, Class cls) {

        T retval = null;
        try {

            Iterator<String> xpathIter = index.getXmlStringIterator(xpath);

            while (xpathIter.hasNext()) {

                String xmlSt = xpathIter.next();

                if (logger.isDebugEnabled()) {
                    logger.debug("XML to unmarshal: " + xmlSt);
                }

                //required for the addition of namespaces to top-level objects
                MzMLNamespaceFilter xmlFilter = new MzMLNamespaceFilter();
                //initializeUnmarshaller will assign the proper reader to the xmlFilter
                Unmarshaller unmarshaller = UnmarshallerFactory.getInstance().initializeUnmarshaller(index, xmlFilter, cache);
                //unmarshall the desired object
                JAXBElement<T> holder = unmarshaller.unmarshal(new SAXSource(xmlFilter, new InputSource(new StringReader(xmlSt))), cls);
                retval = holder.getValue();

                if (logger.isDebugEnabled()) {
                    logger.debug("unmarshalled object = " + retval);
                }

                break;

            }

        } catch (JAXBException e) {
            logger.error("MzMLUnmarshaller.unmarshalFromXpath", e);
            throw new IllegalStateException("Could not unmarshal object at xpath:" + xpath);
        }

        return retval;

    }

    public <T extends MzMLObject> MzMLObjectIterator<T> unmarshalCollectionFromXpath(String xpath, Class cls) {
        return new MzMLObjectIterator<T>(xpath, cls, index, cache);
    }

}
