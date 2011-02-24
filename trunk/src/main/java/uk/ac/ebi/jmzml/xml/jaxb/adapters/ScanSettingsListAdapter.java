package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.ScanSettings;
import uk.ac.ebi.jmzml.model.mzml.ScanSettingsList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 24/02/2011
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class ScanSettingsListAdapter extends XmlAdapter<ScanSettingsList, List<ScanSettings>>{
    @Override
    public List<ScanSettings> unmarshal(ScanSettingsList scanSettingsList) throws Exception {
        return scanSettingsList.getScanSettings();
    }

    @Override
    public ScanSettingsList marshal(List<ScanSettings> scanSettingses) throws Exception {
        ScanSettingsList settingsList = new ScanSettingsList();
        settingsList.getScanSettings().addAll(scanSettingses);
        settingsList.setCount(new BigInteger(String.valueOf(scanSettingses.size())));
        return settingsList;
    }
}
