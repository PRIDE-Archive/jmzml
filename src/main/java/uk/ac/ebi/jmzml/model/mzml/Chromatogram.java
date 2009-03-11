
package uk.ac.ebi.jmzml.model.mzml;

import java.io.Serializable;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import uk.ac.ebi.jmzml.xml.jaxb.adapters.DataProcessingAdapter;


/**
 * A single chromatogram.
 * 
 * <p>Java class for ChromatogramType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ChromatogramType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://psi.hupo.org/ms/mzml}ParamGroupType">
 *       &lt;sequence>
 *         &lt;element name="binaryDataArrayList" type="{http://psi.hupo.org/ms/mzml}BinaryDataArrayListType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="defaultArrayLength" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="dataProcessingRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChromatogramType", propOrder = {
    "binaryDataArrayList"
})
public class Chromatogram
    extends ParamGroup
    implements Serializable
{

    private final static long serialVersionUID = 100L;
    @XmlElement(required = true)
    protected BinaryDataArrayList binaryDataArrayList;
    @XmlAttribute(required = true)
    protected String id;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger index;
    @XmlAttribute(required = true)
    protected int defaultArrayLength;
    @XmlAttribute(name = "dataProcessingRef")
    @XmlJavaTypeAdapter(DataProcessingAdapter.class)
    @XmlSchemaType(name = "IDREF")
    protected DataProcessing dataProcessing;

    /**
     * Gets the value of the binaryDataArrayList property.
     * 
     * @return
     *     possible object is
     *     {@link BinaryDataArrayList }
     *     
     */
    public BinaryDataArrayList getBinaryDataArrayList() {
        return binaryDataArrayList;
    }

    /**
     * Sets the value of the binaryDataArrayList property.
     * 
     * @param value
     *     allowed object is
     *     {@link BinaryDataArrayList }
     *     
     */
    public void setBinaryDataArrayList(BinaryDataArrayList value) {
        this.binaryDataArrayList = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the index property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIndex(BigInteger value) {
        this.index = value;
    }

    /**
     * Gets the value of the defaultArrayLength property.
     * 
     */
    public int getDefaultArrayLength() {
        return defaultArrayLength;
    }

    /**
     * Sets the value of the defaultArrayLength property.
     * 
     */
    public void setDefaultArrayLength(int value) {
        this.defaultArrayLength = value;
    }

    /**
     * Gets the value of the dataProcessing property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public DataProcessing getDataProcessing() {
        return dataProcessing;
    }

    /**
     * Sets the value of the dataProcessing property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataProcessing(DataProcessing value) {
        this.dataProcessing = value;
    }

}
