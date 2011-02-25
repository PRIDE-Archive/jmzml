package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.Index;
import uk.ac.ebi.jmzml.model.mzml.IndexList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 25/02/2011
 * Time: 09:44
 * To change this template use File | Settings | File Templates.
 */
public class IndexListAdapter extends XmlAdapter<IndexList, List<Index>> {
    @Override
    public List<Index> unmarshal(IndexList indexList) throws Exception {
        return indexList.getIndex();
    }

    @Override
    public IndexList marshal(List<Index> indexes) throws Exception {
        IndexList indexList = new IndexList();
        indexList.getIndex().addAll(indexes);
        indexList.setCount(new BigInteger(String.valueOf(indexes.size())));
        return indexList;
    }
}
