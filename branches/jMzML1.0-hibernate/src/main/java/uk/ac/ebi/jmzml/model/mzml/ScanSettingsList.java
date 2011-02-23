package uk.ac.ebi.jmzml.model.mzml;

import uk.ac.ebi.jmzml.model.mzml.interfaces.MzMLObject;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * List with the descriptions of the acquisition settings applied prior to the start of data acquisition.
 * <p/>
 * <p>Java class for ScanSettingsListType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="ScanSettingsListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="scanSettings" type="{http://psi.hupo.org/ms/mzml}ScanSettingsType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="count" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScanSettingsListType", propOrder = {
        "scanSettings"
})
public class ScanSettingsList
        implements Serializable, MzMLObject {

    private final static long serialVersionUID = 100L;
    @XmlElement(required = true)
    protected List<ScanSettings> scanSettings;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger count;

    @XmlTransient
    protected long hid;

    /**
     * Gets the value of the scanSettings property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scanSettings property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScanSettings().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link ScanSettings }
     */
    public List<ScanSettings> getScanSettings() {
        if (scanSettings == null) {
            scanSettings = new ArrayList<ScanSettings>();
        }
        return this.scanSettings;
    }

    /**
     * Gets the value of the count property.
     *
     * @return possible object is
     *         {@link BigInteger }
     */
    public BigInteger getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setCount(BigInteger value) {
        this.count = value;
    }

}
