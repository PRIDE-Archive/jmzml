package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.Sample;
import uk.ac.ebi.jmzml.model.mzml.SampleList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 24/02/2011
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */
public class SampleListAdapter extends XmlAdapter<SampleList, List<Sample>>{
    @Override
    public List<Sample> unmarshal(SampleList sampleList) throws Exception {
        return sampleList.getSample();
    }

    @Override
    public SampleList marshal(List<Sample> samples) throws Exception {
        SampleList sampleList = new SampleList();
        sampleList.getSample().addAll(samples);
        sampleList.setCount(new BigInteger(String.valueOf(samples.size())));
        return sampleList;
    }
}
