package uk.ac.ebi.jmzml.model.mzml;

import uk.ac.ebi.jmzml.model.mzml.interfaces.MzMLObject;
import uk.ac.ebi.jmzml.xml.jaxb.adapters.ReferenceableParamGroupAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;


/**
 * A reference to a previously defined ParamGroup, which is a reusable container of one or more cvParams.
 * <p/>
 * <p>Java class for ReferenceableParamGroupRefType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="ReferenceableParamGroupRefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferenceableParamGroupRefType")
public class ReferenceableParamGroupRef
        implements Serializable, MzMLObject {

    private final static long serialVersionUID = 100L;
    @XmlAttribute(name = "ref", required = true)
    @XmlJavaTypeAdapter(ReferenceableParamGroupAdapter.class)
    @XmlSchemaType(name = "IDREF")
    protected ReferenceableParamGroup referenceableParamGroup;
    
    @XmlTransient
    protected long hid;

    /**
     * Gets the value of the referenceableParamGroup property.
     *
     * @return possible object is
     *         {@link String }
     */
    public ReferenceableParamGroup getReferenceableParamGroup() {
        return referenceableParamGroup;
    }

    /**
     * Sets the value of the referenceableParamGroup property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setReferenceableParamGroup(ReferenceableParamGroup value) {
        this.referenceableParamGroup = value;
    }

}
