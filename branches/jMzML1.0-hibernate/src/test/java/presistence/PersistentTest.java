package presistence;

import org.hibernate.Session;
import uk.ac.ebi.jmzml.model.mzml.MzML;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: rwang
 * Date: 08/02/11
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class PersistentTest extends DAO{

    public static void main(String[] args) throws Exception {

        URL xmlFileURL = PersistentTest.class.getClassLoader().getResource("sample_small.mzML");

        if (xmlFileURL != null) {
            MzMLUnmarshaller unmarshaller = new MzMLUnmarshaller(xmlFileURL);

            MzML mzml = unmarshaller.unmarshall();

            Session session = getSession();
            begin();
            session.persist(mzml);
            commit();
            close();
        }
    }
}
