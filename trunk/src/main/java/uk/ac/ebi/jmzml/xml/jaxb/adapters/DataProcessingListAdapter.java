package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.DataProcessing;
import uk.ac.ebi.jmzml.model.mzml.DataProcessingList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 24/02/2011
 * Time: 12:18
 * To change this template use File | Settings | File Templates.
 */
public class DataProcessingListAdapter extends XmlAdapter<DataProcessingList, List<DataProcessing>>{

    @Override
    public List<DataProcessing> unmarshal(DataProcessingList dataProcessingList) throws Exception {
        return dataProcessingList.getDataProcessing();
    }

    @Override
    public DataProcessingList marshal(List<DataProcessing> dataProcessings) throws Exception {
        DataProcessingList dataProcessingList = new DataProcessingList();
        dataProcessingList.getDataProcessing().addAll(dataProcessings);
        dataProcessingList.setCount(new BigInteger(String.valueOf(dataProcessings.size())));
        return dataProcessingList;
    }
}
