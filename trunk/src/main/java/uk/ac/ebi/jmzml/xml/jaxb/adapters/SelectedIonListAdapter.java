package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.ParamGroup;
import uk.ac.ebi.jmzml.model.mzml.SelectedIonList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 25/02/2011
 * Time: 09:59
 * To change this template use File | Settings | File Templates.
 */
public class SelectedIonListAdapter extends XmlAdapter<SelectedIonList, List<ParamGroup>> {
    @Override
    public List<ParamGroup> unmarshal(SelectedIonList selectedIonList) throws Exception {
        return selectedIonList.getSelectedIon();
    }

    @Override
    public SelectedIonList marshal(List<ParamGroup> paramGroups) throws Exception {
        SelectedIonList selectedIonList = new SelectedIonList();
        selectedIonList.getSelectedIon().addAll(paramGroups);
        selectedIonList.setCount(new BigInteger(String.valueOf(paramGroups.size())));
        return selectedIonList;
    }
}
