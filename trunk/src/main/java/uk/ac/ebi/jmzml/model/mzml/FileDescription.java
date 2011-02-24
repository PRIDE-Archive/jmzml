
package uk.ac.ebi.jmzml.model.mzml;

import uk.ac.ebi.jmzml.xml.jaxb.adapters.SourceFileListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Information pertaining to the entire mzML file (i.e. not specific to any part of the data set) is stored here.
 * 
 * <p>Java class for FileDescriptionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FileDescriptionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fileContent" type="{http://psi.hupo.org/ms/mzml}ParamGroupType"/>
 *         &lt;element name="sourceFileList" type="{http://psi.hupo.org/ms/mzml}SourceFileListType" minOccurs="0"/>
 *         &lt;element name="contact" type="{http://psi.hupo.org/ms/mzml}ParamGroupType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileDescriptionType", propOrder = {
    "fileContent",
    "sourceFileList",
    "contact"
})
public class FileDescription
    extends MzMLObject
    implements Serializable
{

    private final static long serialVersionUID = 100L;
    @XmlElement(required = true)
    protected ParamGroup fileContent;
    @XmlJavaTypeAdapter(value = SourceFileListAdapter.class)
    protected List<SourceFile> sourceFileList;
    protected List<ParamGroup> contact;

    /**
     * Gets the value of the fileContent property.
     * 
     * @return
     *     possible object is
     *     {@link ParamGroup }
     *     
     */
    public ParamGroup getFileContent() {
        return fileContent;
    }

    /**
     * Sets the value of the fileContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParamGroup }
     *     
     */
    public void setFileContent(ParamGroup value) {
        this.fileContent = value;
    }

    /**
     * Gets the value of the sourceFileList property.
     * 
     * @return
     *     possible object is
     *     {@link SourceFileList }
     *     
     */
    public List<SourceFile> getSourceFileList() {
        return sourceFileList;
    }

    /**
     * Gets the value of the contact property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contact property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContact().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParamGroup }
     * 
     * 
     */
    public List<ParamGroup> getContact() {
        if (contact == null) {
            contact = new ArrayList<ParamGroup>();
        }
        return this.contact;
    }

}
