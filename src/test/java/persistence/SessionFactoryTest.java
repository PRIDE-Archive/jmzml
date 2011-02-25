package persistence;

import hbm.namingStrategy.OracleNamingStrategy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import uk.ac.ebi.jmzml.model.mzml.*;
import uk.ac.ebi.jmzml.xml.io.MzMLMarshaller;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;

import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;


/**
 * Created by IntelliJ IDEA.
 * User: dani
 * Date: 08-Feb-2011
 * Time: 14:36:48
 * To change this template use File | Settings | File Templates.
 */
public class SessionFactoryTest {

    Configuration cfg = null;
    SessionFactory factory = null;
    Session session = null;
    MzML mzML = null;
    String fileName = "xxx.mzML";

    public void createSessionFactory() {

        //Build session factory
        cfg = new Configuration().configure("META-INF/hibernate.cfg.xml").setNamingStrategy(new OracleNamingStrategy());
        factory = cfg.buildSessionFactory();
        session = factory.openSession();
    }

    public void closeSessionFactory() {
        session.close();
        factory.close(); // close at application end
    }

    public void unmarshallMzML(String fileName) {

        try {
            URL xmlFileURL = this.getClass().getClassLoader().getResource(fileName);

            if (xmlFileURL != null) {

                // 1. parse file
                MzMLUnmarshaller unmarshaller = new MzMLUnmarshaller(xmlFileURL);

                mzML = (MzML) unmarshaller.unmarshall();
            } else {
                System.out.println("File not found");
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void testCvList() throws Exception{

        createSessionFactory();
        unmarshallMzML(fileName);
        CVList cvList = null;//mzML.getCvList();
        // Persist object
        session.getTransaction().begin();
        session.persist(cvList);
        session.getTransaction().commit();
        //now, retrieve it
        long CVList_id = 1;
        session.getTransaction().begin();

        CVList CVList_out = (CVList) session.get(CVList.class, CVList_id);

        if (CVList_out == null) {
            System.out.println("Entity not found " + CVList_id);
        }
        session.getTransaction().commit();

        Writer writer = new FileWriter("output_test_CVList.xml");
        //Check marshaling back into a file
        MzMLMarshaller marshaller = new MzMLMarshaller();
        marshaller.marshall(CVList_out, writer);
        closeSessionFactory();
    }

    @Test
    public void testFileDescription() throws Exception {
        createSessionFactory();
        unmarshallMzML(fileName);
        FileDescription fileDescription = mzML.getFileDescription();
        // Persist object
        session.getTransaction().begin();
        session.persist(fileDescription);
        session.getTransaction().commit();
        //now, retrieve it
        long id = 1;
        session.getTransaction().begin();

        FileDescription FileDescription_out = (FileDescription) session.get(FileDescription.class, id);

        if (FileDescription_out == null) {
            System.out.println("Entity not found " + id);
        }
        session.getTransaction().commit();

        Writer writer = new FileWriter("output_test_FileDescription.xml");
        //Check marshaling back into a file
        MzMLMarshaller marshaller = new MzMLMarshaller();
        marshaller.marshall(FileDescription_out, writer);
        closeSessionFactory();
    }

    @Test
    public void testReferenceableParamGroupList() throws Exception{
        createSessionFactory();
        unmarshallMzML(fileName);
        ReferenceableParamGroupList refParamGroupList = null;//mzML.getReferenceableParamGroupList();
        // Persist object
        session.getTransaction().begin();
        session.persist(refParamGroupList);
        session.getTransaction().commit();
        //now, retrieve it
        long id = 1;
        session.getTransaction().begin();

        ReferenceableParamGroupList referenceableParamGroupList_out = (ReferenceableParamGroupList) session.get(ReferenceableParamGroupList.class, id);

        if (referenceableParamGroupList_out == null) {
            System.out.println("Entity not found " + id);
        }
        session.getTransaction().commit();

        Writer writer = new FileWriter("output_test_ReferenceableParamGroupList.xml");
        //Check marshaling back into a file
        MzMLMarshaller marshaller = new MzMLMarshaller();
        marshaller.marshall(referenceableParamGroupList_out, writer);
        closeSessionFactory();
    }

    @Test
    public void testSampleList() throws Exception{
        createSessionFactory();
        unmarshallMzML(fileName);
        SampleList sampleList = null;//mzML.getSampleList();
        if (sampleList == null){
            return;    
        }
        // Persist object
        session.getTransaction().begin();
        session.persist(sampleList);
        session.getTransaction().commit();
        //now, retrieve it
        long id = 1;
        session.getTransaction().begin();

        SampleList SampleList_out = (SampleList) session.get(SampleList.class, id);

        if (SampleList_out == null) {
            System.out.println("Entity not found " + id);
        }
        session.getTransaction().commit();

        Writer writer = new FileWriter("output_test_SampleList.xml");
        //Check marshaling back into a file
        MzMLMarshaller marshaller = new MzMLMarshaller();
        marshaller.marshall(SampleList_out, writer);
        closeSessionFactory();
    }

    @Test
    public void testScanSettingsList() throws Exception{
        createSessionFactory();
        unmarshallMzML(fileName);
        ScanSettingsList scanSettingsList = null;//mzML.getScanSettingsList();
        if (scanSettingsList == null){
            return;
        }
        // Persist object
        session.getTransaction().begin();
        session.persist(scanSettingsList);
        session.getTransaction().commit();
        //now, retrieve it
        long id = 1;
        session.getTransaction().begin();

        ScanSettingsList ScanSettingsList_out = (ScanSettingsList) session.get(ScanSettingsList.class, id);

        if (ScanSettingsList_out == null) {
            System.out.println("Entity not found " + id);
        }
        session.getTransaction().commit();

        Writer writer = new FileWriter("output_test_ScanSettingsList.xml");
        //Check marshaling back into a file
        MzMLMarshaller marshaller = new MzMLMarshaller();
        marshaller.marshall(ScanSettingsList_out, writer);
        closeSessionFactory();
    }

    @Test
    public void testSoftwareList() throws Exception {
        createSessionFactory();
        unmarshallMzML(fileName);
        SoftwareList softwareList = null;//mzML.getSoftwareList();
        // Persist object
        session.getTransaction().begin();
        session.persist(softwareList);
        session.getTransaction().commit();
        //now, retrieve it
        long id = 1;
        session.getTransaction().begin();

        SoftwareList SoftwareList_out = (SoftwareList) session.get(SoftwareList.class, id);

        if (SoftwareList_out == null) {
            System.out.println("Entity not found " + id);
        }
        session.getTransaction().commit();

        Writer writer = new FileWriter("output_test_SoftwareList.xml");
        //Check marshaling back into a file
        MzMLMarshaller marshaller = new MzMLMarshaller();
        marshaller.marshall(SoftwareList_out, writer);
        closeSessionFactory();
    }

    @Test
    public void testInstrumentConfigurationList() throws Exception{
        createSessionFactory();
        unmarshallMzML(fileName);
        InstrumentConfigurationList instrumentConfigurationList = null;//mzML.getInstrumentConfigurationList();
        // Persist object
        session.getTransaction().begin();
        session.persist(instrumentConfigurationList);
        session.getTransaction().commit();
        //now, retrieve it
        long id = 1;
        session.getTransaction().begin();

        InstrumentConfigurationList InstrumentConfigurationList_out = (InstrumentConfigurationList) session.get(InstrumentConfigurationList.class, id);

        if (InstrumentConfigurationList_out == null) {
            System.out.println("Entity not found " + id);
        }
        session.getTransaction().commit();

        Writer writer = new FileWriter("output_test_InstrumentConfigurationList.xml");
        //Check marshaling back into a file
        MzMLMarshaller marshaller = new MzMLMarshaller();
        marshaller.marshall(InstrumentConfigurationList_out, writer);
        closeSessionFactory();
    }

    @Test
    public void testDataProcessingList() throws Exception{
        createSessionFactory();
        unmarshallMzML(fileName);
        DataProcessingList dataProcessingList = null;//mzML.getDataProcessingList();
        // Persist object
        session.getTransaction().begin();
        session.persist(dataProcessingList);
        session.getTransaction().commit();
        //now, retrieve it
        long id = 1;
        session.getTransaction().begin();

        DataProcessingList DataProcessingList_out = (DataProcessingList) session.get(DataProcessingList.class, id);

        if (DataProcessingList_out == null) {
            System.out.println("Entity not found " + id);
        }
        session.getTransaction().commit();

        Writer writer = new FileWriter("output_test_DataProcessingList.xml");
        //Check marshaling back into a file
        MzMLMarshaller marshaller = new MzMLMarshaller();
        marshaller.marshall(DataProcessingList_out, writer);
        closeSessionFactory();
    }

    @Test
    public void testChromatogramList() throws Exception {
        createSessionFactory();
        unmarshallMzML(fileName);
        ChromatogramList chromatogramList = mzML.getRun().getChromatogramList();
        // Persist object
        session.getTransaction().begin();
        session.persist(chromatogramList);
        session.getTransaction().commit();
        //now, retrieve it
        long id = 1;
        session.getTransaction().begin();

        ChromatogramList ChromatogramList_out = (ChromatogramList) session.get(ChromatogramList.class, id);

        if (ChromatogramList_out == null) {
            System.out.println("Entity not found " + id);
        }
        session.getTransaction().commit();

        Writer writer = new FileWriter("output_test_ChromatogramList.xml");
        //Check marshaling back into a file
        MzMLMarshaller marshaller = new MzMLMarshaller();
        marshaller.marshall(ChromatogramList_out, writer);
        closeSessionFactory();
    }

    @Test
    public void testRun() throws Exception{
        createSessionFactory();
        unmarshallMzML(fileName);
        Run run = mzML.getRun();
        // Persist object
        session.getTransaction().begin();
        session.persist(run);
        session.getTransaction().commit();
        //now, retrieve it
        long id = 1;
        session.getTransaction().begin();

        Run Run_out = (Run) session.get(Run.class, id);

        if (Run_out == null) {
            System.out.println("Entity not found " + id);
        }
        session.getTransaction().commit();

        Writer writer = new FileWriter("output_test_Run.xml");
        //Check marshaling back into a file
        MzMLMarshaller marshaller = new MzMLMarshaller();
        marshaller.marshall(Run_out, writer);
        closeSessionFactory();
    }

    @Test
    public void testmzML() throws Exception {
        createSessionFactory();
        unmarshallMzML(fileName);
        // Persist object
        session.getTransaction().begin();
        session.persist(mzML);
        session.getTransaction().commit();
        long MzML_id = 1;
        session.getTransaction().begin();

        MzML mzML_out = (MzML) session.get(MzML.class,new Long("1"));

        if (mzML_out == null) {
            System.out.println("Entity not found " + MzML_id);
        }
        session.getTransaction().commit();

        Writer writer = new FileWriter("output_mzML.xml");
        //Check marshaling back into a file
        MzMLMarshaller marshaller = new MzMLMarshaller();
        marshaller.marshall(mzML_out, writer);
        closeSessionFactory();
        
    }
}
