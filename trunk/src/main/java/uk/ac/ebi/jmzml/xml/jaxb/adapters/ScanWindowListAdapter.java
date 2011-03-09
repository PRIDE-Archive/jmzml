package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ScanWindowList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 25/02/2011
 * Time: 09:54
 * To change this template use File | Settings | File Templates.
 */
public class ScanWindowListAdapter extends XmlAdapter<ScanWindowList, List<ParamGroup>> {
    @Override
    public List<ParamGroup> unmarshal(ScanWindowList scanWindowList) throws Exception {
        return scanWindowList.getScanWindow();
    }

    @Override
    public ScanWindowList marshal(List<ParamGroup> paramGroups) throws Exception {
        if (paramGroups == null) return null;
        ScanWindowList scanWindowList = new ScanWindowList();
        scanWindowList.getScanWindow().addAll(paramGroups);
        scanWindowList.setCount(paramGroups.size());
        return scanWindowList;
    }
}
