package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.BinaryDataArray;
import uk.ac.ebi.jmzml.model.mzml.BinaryDataArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 24/02/2011
 * Time: 17:58
 * To change this template use File | Settings | File Templates.
 */
public class BinaryDataArrayListAdapter extends XmlAdapter<BinaryDataArrayList, List<BinaryDataArray>>{
    @Override
    public List<BinaryDataArray> unmarshal(BinaryDataArrayList binaryDataArrayList) throws Exception {
        return binaryDataArrayList.getBinaryDataArray();
    }

    @Override
    public BinaryDataArrayList marshal(List<BinaryDataArray> binaryDataArrays) throws Exception {
        BinaryDataArrayList binaryDataArrayList = new BinaryDataArrayList();
        binaryDataArrayList.getBinaryDataArray().addAll(binaryDataArrays);
        binaryDataArrayList.setCount(new BigInteger(String.valueOf(binaryDataArrays.size())));
        return binaryDataArrayList;
    }
}
