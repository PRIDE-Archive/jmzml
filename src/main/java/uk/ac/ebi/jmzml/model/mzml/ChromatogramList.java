package uk.ac.ebi.jmzml.model.mzml;

import uk.ac.ebi.jmzml.model.mzml.interfaces.MzMLObject;
import uk.ac.ebi.jmzml.xml.jaxb.adapters.DataProcessingAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * List of chromatograms.
 * <p/>
 * <p>Java class for ChromatogramListType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="ChromatogramListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="chromatogram" type="{http://psi.hupo.org/ms/mzml}ChromatogramType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="count" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="defaultDataProcessingRef" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChromatogramListType", propOrder = {
        "chromatogram"
})
public class ChromatogramList
        implements Serializable, MzMLObject {

    private final static long serialVersionUID = 100L;
    @XmlElement(required = true)
    protected List<Chromatogram> chromatogram;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger count;
    @XmlAttribute(name = "defaultDataProcessingRef", required = true)
    @XmlJavaTypeAdapter(DataProcessingAdapter.class)
    @XmlSchemaType(name = "IDREF")
    protected DataProcessing defaultDataProcessing;

    @XmlTransient
    protected long hid;


    /**
     * Gets the value of the chromatogram property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the chromatogram property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChromatogram().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link Chromatogram }
     */
    public List<Chromatogram> getChromatogram() {
        if (chromatogram == null) {
            chromatogram = new ArrayList<Chromatogram>();
        }
        return this.chromatogram;
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

    /**
     * Gets the value of the defaultDataProcessing property.
     *
     * @return possible object is
     *         {@link String }
     */
    public DataProcessing getDefaultDataProcessing() {
        return defaultDataProcessing;
    }

    /**
     * Sets the value of the defaultDataProcessing property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDefaultDataProcessing(DataProcessing value) {
        this.defaultDataProcessing = value;
    }

}
