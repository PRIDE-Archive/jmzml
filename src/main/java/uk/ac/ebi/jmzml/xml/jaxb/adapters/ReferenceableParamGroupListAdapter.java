package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroup;
import uk.ac.ebi.jmzml.model.mzml.ReferenceableParamGroupList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 24/02/2011
 * Time: 11:52
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceableParamGroupListAdapter extends XmlAdapter<ReferenceableParamGroupList, List<ReferenceableParamGroup>> {
    @Override
    public List<ReferenceableParamGroup> unmarshal(
            ReferenceableParamGroupList referenceableParamGroupList)
            throws Exception {
        return referenceableParamGroupList.getReferenceableParamGroup();
    }

    @Override
    public ReferenceableParamGroupList marshal(
            List<ReferenceableParamGroup> referenceableParamGroups)
            throws Exception {
        ReferenceableParamGroupList refPGList = new ReferenceableParamGroupList();
        refPGList.getReferenceableParamGroup().addAll(referenceableParamGroups);
        refPGList.setCount(new BigInteger(String.valueOf(referenceableParamGroups.size())));
        return refPGList;
    }
}
