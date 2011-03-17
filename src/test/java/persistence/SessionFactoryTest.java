package persistence;

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
import java.util.List;


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
    String fileName = "sample_small.mzML";

    public void createSessionFactory() {

        //Build session factory
        cfg = new Configuration().configure("META-INF/hibernate.cfg.xml");//.setNamingStrategy(new OracleNamingStrategy());
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
        List<CV> cvList = mzML.getCvList().getCv();
        // Persist object
        session.getTransaction().begin();
        session.persist(mzML);
        session.getTransaction().commit();
        //now, retrieve it
        long MzML_id = 1;
        session.getTransaction().begin();

        List<CV> CVList_out = ((MzML) session.get(MzML.class, MzML_id)).getCvList().getCv();

        if (CVList_out == null) {
            System.out.println("Entity not found " + MzML_id);
        }
        session.getTransaction().commit();
        closeSessionFactory();
        int i = 0;
        for (CV cv : CVList_out) {
            if(!cv.equals(cvList.get(i))){
                assert false;
            }
            i ++;
        }
    }

    @Test
    public void testSampleList() throws Exception{

        createSessionFactory();
        unmarshallMzML(fileName);
        List<Sample> sampleList = mzML.getSampleList().getSample();
        // Persist object
        session.getTransaction().begin();
        session.persist(mzML);
        session.getTransaction().commit();
        //now, retrieve it
        long MzML_id = 1;
        session.getTransaction().begin();

        List<Sample> SampleList_out = ((MzML) session.get(MzML.class, MzML_id)).getSampleList().getSample();

        if (SampleList_out == null) {
            System.out.println("Entity not found " + MzML_id);
        }
        session.getTransaction().commit();
        closeSessionFactory();
        int i = 0;
        for (Sample sample : SampleList_out) {
            if(!sample.equals(sampleList.get(i))){
                assert false;
            }
            i ++;
        }
    }

    @Test
    public void testmzML() throws Exception {
        createSessionFactory();
        unmarshallMzML(fileName);
        // Persist object
        session.getTransaction().begin();
        Long id = (Long)session.save(mzML);
        //session.persist(mzML);
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
