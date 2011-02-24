package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.CV;
import uk.ac.ebi.jmzml.model.mzml.CVList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 24/02/2011
 * Time: 11:40
 * To change this template use File | Settings | File Templates.
 */
public class CVListAdapter extends XmlAdapter<CVList, List<CV>> {
    @Override
    public List<CV> unmarshal(CVList cvList) throws Exception {
        return cvList.getCv();
    }

    @Override
    public CVList marshal(List<CV> cvs) throws Exception {
        CVList cvList = new CVList();
        cvList.getCv().addAll(cvs);
        cvList.setCount(new BigInteger(String.valueOf(cvs.size())));
        return cvList;
    }
}
