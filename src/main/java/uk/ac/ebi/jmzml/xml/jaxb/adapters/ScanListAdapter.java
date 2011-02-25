package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.Scan;
import uk.ac.ebi.jmzml.model.mzml.ScanList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 25/02/2011
 * Time: 09:32
 * To change this template use File | Settings | File Templates.
 */
public class ScanListAdapter extends XmlAdapter<ScanList, List<Scan>> {
    @Override
    public List<Scan> unmarshal(ScanList scanList) throws Exception {
        return scanList.getScan();
    }

    @Override
    public ScanList marshal(List<Scan> scans) throws Exception {
        ScanList scanList = new ScanList();
        scanList.getScan().addAll(scans);
        scanList.setCount(new BigInteger(String.valueOf(scans.size())));
        return scanList;
    }
}
