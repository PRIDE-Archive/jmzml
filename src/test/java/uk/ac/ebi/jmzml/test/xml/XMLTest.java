/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.test.xml.XMLTest
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

package uk.ac.ebi.jmzml.test.xml;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;
import uk.ac.ebi.jmzml.model.mzml.Chromatogram;
import uk.ac.ebi.jmzml.model.mzml.FileDescription;
import uk.ac.ebi.jmzml.model.mzml.MzML;
import uk.ac.ebi.jmzml.xml.io.MzMLMarshaller;
import uk.ac.ebi.jmzml.xml.io.MzMLObjectIterator;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;

import java.net.URL;

public class XMLTest extends TestCase {

    /**
     * todo - write toString & equals & hashcode
     * todo - make class diagram
     * todo - map to database layer
     */

    Logger logger = Logger.getLogger(XMLTest.class);

    public XMLTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testXMLIndex() throws Exception {

        URL url = this.getClass().getClassLoader().getResource("sample_small.mzML");
        assertNotNull(url);

        MzMLUnmarshaller um = new MzMLUnmarshaller(url);

//        DataProcessing dh = um.unmarshalFromXpath("/dataProcessingList/dataProcessing", DataProcessing.class);
//        logger.debug("dh = " + dh);

        MzML mz = um.unmarshall();
        logger.debug("mz = " + mz);
        FileDescription fd = um.unmarshalFromXpath("/fileDescription", FileDescription.class);
        logger.debug("fd = " + fd);

        fd = um.unmarshalFromXpath("/mzML/fileDescription", FileDescription.class);
        logger.debug("fd = " + fd);

        MzMLMarshaller mm = new MzMLMarshaller();
        String outFD = mm.marshall(fd, fd.getClass());

        String mzml = mm.marshall(mz, mz.getClass());

        int chromatogramCount = 0;
        MzMLObjectIterator<Chromatogram> iter = um.unmarshalCollectionFromXpath("/run/chromatogramList/chromatogram", Chromatogram.class);
        while (iter.hasNext()) {
            Chromatogram ch = iter.next();
            chromatogramCount++;
        }

        assertEquals("Chromatogram count not equal!", chromatogramCount, um.getObjectCountForXpath("/run/chromatogramList/chromatogram"));

    }


    public static Test suite() {
        return new TestSuite(XMLTest.class);
    }

}
