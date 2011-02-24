package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.Component;
import uk.ac.ebi.jmzml.model.mzml.ComponentList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 24/02/2011
 * Time: 18:01
 * To change this template use File | Settings | File Templates.
 */
public class ComponentListAdapter extends XmlAdapter<ComponentList, List<Component>>{
    @Override
    public List<Component> unmarshal(ComponentList componentList) throws Exception {
        return componentList.getComponents();
    }

    @Override
    public ComponentList marshal(List<Component> components) throws Exception {
        ComponentList componentList = new ComponentList();
        componentList.getComponents().addAll(components);
        componentList.setCount(new BigInteger(String.valueOf(components.size())));
        return componentList;
    }
}
