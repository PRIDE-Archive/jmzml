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
        System.out.println("outFD = " + "\n" + outFD);

        String mzml = mm.marshall(mz, mz.getClass());
        System.out.println("mzml = " + mzml);

        MzMLObjectIterator<Chromatogram> iter = um.unmarshalCollectionFromXpath("/run/chromatogramList/chromatogram", Chromatogram.class);
        while (iter.hasNext()) {
            Chromatogram ch = iter.next();
            System.out.println("ch = " + ch);
        }


    }


    public static Test suite() {
        return new TestSuite(XMLTest.class);
    }

}
