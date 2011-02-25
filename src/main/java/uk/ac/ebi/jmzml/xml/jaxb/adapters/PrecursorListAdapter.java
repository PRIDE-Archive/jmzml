package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.Precursor;
import uk.ac.ebi.jmzml.model.mzml.PrecursorList;

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
public class PrecursorListAdapter extends XmlAdapter<PrecursorList, List<Precursor>>{
    @Override
    public List<Precursor> unmarshal(PrecursorList precursorList) throws Exception {
        return precursorList.getPrecursor();
    }

    @Override
    public PrecursorList marshal(List<Precursor> precursors) throws Exception {
        PrecursorList precursorList = new PrecursorList();
        precursorList.getPrecursor().addAll(precursors);
        precursorList.setCount(new BigInteger(String.valueOf(precursors.size())));
        return precursorList;
    }
}
