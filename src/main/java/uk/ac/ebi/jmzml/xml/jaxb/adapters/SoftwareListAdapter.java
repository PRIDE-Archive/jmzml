package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.Software;
import uk.ac.ebi.jmzml.model.mzml.SoftwareList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 24/02/2011
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
public class SoftwareListAdapter extends XmlAdapter<SoftwareList, List<Software>> {
    @Override
    public List<Software> unmarshal(SoftwareList softwareList) throws Exception {
        return softwareList.getSoftware();
    }

    @Override
    public SoftwareList marshal(List<Software> softwares) throws Exception {
        SoftwareList softwareList = new SoftwareList();
        softwareList.getSoftware().addAll(softwares);
        softwareList.setCount(new BigInteger(String.valueOf(softwares.size())));
        return softwareList;
    }
}
