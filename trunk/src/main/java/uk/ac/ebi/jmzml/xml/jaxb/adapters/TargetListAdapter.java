package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.TargetList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 25/02/2011
 * Time: 10:05
 * To change this template use File | Settings | File Templates.
 */
public class TargetListAdapter extends XmlAdapter<TargetList, List<ParamGroup>> {
    @Override
    public List<ParamGroup> unmarshal(TargetList targetList) throws Exception {
        return targetList.getTarget();
    }

    @Override
    public TargetList marshal(List<ParamGroup> paramGroupList) throws Exception {
        TargetList targetList = new TargetList();
        targetList.getTarget().addAll(paramGroupList);
        targetList.setCount(new BigInteger(String.valueOf(paramGroupList.size())));
        return targetList;
    }
}
