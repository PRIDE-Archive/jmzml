package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.SourceFile;
import uk.ac.ebi.jmzml.model.mzml.SourceFileList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 24/02/2011
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class SourceFileListAdapter extends XmlAdapter<SourceFileList, List<SourceFile>>{
    @Override
    public List<SourceFile> unmarshal(SourceFileList sourceFileList) throws Exception {
        return sourceFileList.getSourceFile();
    }

    @Override
    public SourceFileList marshal(List<SourceFile> sourceFiles) throws Exception {
        SourceFileList sourceFileList = new SourceFileList();
        sourceFileList.getSourceFile().addAll(sourceFiles);
        sourceFileList.setCount(new BigInteger(String.valueOf(sourceFiles.size())));
        return sourceFileList;
    }
}
