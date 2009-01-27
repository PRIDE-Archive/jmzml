/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.xml.jaxb.marshaller.MarshallerFactory
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


package uk.ac.ebi.jmzml.xml.jaxb.marshaller;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import org.apache.log4j.Logger;
import uk.ac.ebi.jmzml.model.mzml.ModelConstants;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.adapters.*;
import uk.ac.ebi.jmzml.xml.jaxb.marshaller.listeners.ObjectClassListener;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;

public class MarshallerFactory {

    private static final Logger logger = Logger.getLogger(MarshallerFactory.class);
    private static MarshallerFactory instance = new MarshallerFactory();
    private static JAXBContext jc = null;

    public static MarshallerFactory getInstance() {
        return instance;
    }

    private MarshallerFactory() {
    }

    public Marshaller initializeMarshaller() {

        try {
            // Lazy caching of JAXB context.
            if(jc == null) {
                jc = JAXBContext.newInstance(ModelConstants.PACKAGE);
            }
            //create marshaller and set basic properties
            Marshaller marshaller = jc.createMarshaller();

            marshaller.setProperty(Constants.JAXB_ENCODING_PROPERTY, "UTF-8");
            marshaller.setProperty(Constants.JAXB_FORMATTING_PROPERTY, true);
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MzMLNamespaceMapper());

            //set adapters so that jaxb can write the proper IDREF tags in the XML
            marshaller.setAdapter(new CVAdapter(null, null));
            marshaller.setAdapter(new DataProcessingAdapter(null, null));
            marshaller.setAdapter(new InstrumentConfigurationAdapter(null, null));
            marshaller.setAdapter(new ReferenceableParamGroupAdapter(null, null));
            marshaller.setAdapter(new SampleAdapter(null, null));
            marshaller.setAdapter(new SoftwareAdapter(null, null));
            marshaller.setAdapter(new SourceFileAdapter(null, null));
            marshaller.setAdapter(new SpectrumAdapter(null, null, false));
            marshaller.setEventHandler(new DefaultValidationEventHandler());


            marshaller.setListener(new ObjectClassListener());
            
            logger.info("Marshaller initialized");

            return marshaller;

        } catch (JAXBException e) {
            logger.error("MarshallerFactory.initializeMarshaller", e);
            throw new IllegalStateException("Can't initialize marshaller: " + e.getMessage());
        }

    }

    private class MzMLNamespaceMapper extends NamespacePrefixMapper {

        public String[] getPreDeclaredNamespaceUris() {
            return new String[0];
        }

        public String[] getPreDeclaredNamespaceUris2() {
            return new String[0];
        }

        public String[] getContextualNamespaceDecls() {
            return new String[0];
        }

        public String getPreferredPrefix(String namespaceUri,
                                         String suggestion,
                                         boolean requirePrefix) {
            logger.debug("set prefix to >< when asked for uri >" + namespaceUri + "< with requirePrefix: " + requirePrefix + "");
            return "";
        }
    }

}
