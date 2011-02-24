package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.InstrumentConfiguration;
import uk.ac.ebi.jmzml.model.mzml.InstrumentConfigurationList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 24/02/2011
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public class InstrumentConfigurationListAdapter extends XmlAdapter<InstrumentConfigurationList, List<InstrumentConfiguration>>{
    @Override
    public List<InstrumentConfiguration> unmarshal(InstrumentConfigurationList instrumentConfigurationList) throws Exception {
        return instrumentConfigurationList.getInstrumentConfiguration();
    }

    @Override
    public InstrumentConfigurationList marshal(List<InstrumentConfiguration> instrumentConfigurations) throws Exception {
        InstrumentConfigurationList insConfList = new InstrumentConfigurationList();
        insConfList.getInstrumentConfiguration().addAll(instrumentConfigurations);
        insConfList.setCount(new BigInteger(String.valueOf(instrumentConfigurations.size())));
        return insConfList;
    }
}
