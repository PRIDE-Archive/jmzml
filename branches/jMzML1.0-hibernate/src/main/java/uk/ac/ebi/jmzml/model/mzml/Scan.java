package uk.ac.ebi.jmzml.model.mzml;

import uk.ac.ebi.jmzml.xml.jaxb.adapters.InstrumentConfigurationAdapter;
import uk.ac.ebi.jmzml.xml.jaxb.adapters.SourceFileAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;


/**
 * Scan or acquisition from original raw file used to create this peak list, as specified in sourceFile.
 * <p/>
 * <p>Java class for ScanType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="ScanType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://psi.hupo.org/ms/mzml}ParamGroupType">
 *       &lt;sequence>
 *         &lt;element name="scanWindowList" type="{http://psi.hupo.org/ms/mzml}ScanWindowListType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="spectrumRef" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sourceFileRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="externalSpectrumID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="instrumentConfigurationRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScanType", propOrder = {
        "scanWindowList"
})
public class Scan
        extends ParamGroup
        implements Serializable {

    private final static long serialVersionUID = 100L;
    protected ScanWindowList scanWindowList;
    @XmlAttribute
    protected String spectrumRef;
    @XmlAttribute(name = "sourceFileRef")
    @XmlJavaTypeAdapter(SourceFileAdapter.class)
    @XmlSchemaType(name = "IDREF")
    protected SourceFile sourceFile;
    @XmlAttribute
    protected String externalSpectrumID;
    @XmlAttribute(name = "instrumentConfigurationRef")
    @XmlJavaTypeAdapter(InstrumentConfigurationAdapter.class)
    @XmlSchemaType(name = "IDREF")
    protected InstrumentConfiguration instrumentConfiguration;

    /**
     * Gets the value of the scanWindowList property.
     *
     * @return possible object is
     *         {@link ScanWindowList }
     */
    public ScanWindowList getScanWindowList() {
        return scanWindowList;
    }

    /**
     * Sets the value of the scanWindowList property.
     *
     * @param value allowed object is
     *              {@link ScanWindowList }
     */
    public void setScanWindowList(ScanWindowList value) {
        this.scanWindowList = value;
    }

    /**
     * Gets the value of the spectrumRef property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSpectrumRef() {
        return spectrumRef;
    }

    /**
     * Sets the value of the spectrumRef property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSpectrumRef(String value) {
        this.spectrumRef = value;
    }

    /**
     * Gets the value of the sourceFile property.
     *
     * @return possible object is
     *         {@link String }
     */
    public SourceFile getSourceFile() {
        return sourceFile;
    }

    /**
     * Sets the value of the sourceFile property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSourceFile(SourceFile value) {
        this.sourceFile = value;
    }

    /**
     * Gets the value of the externalSpectrumID property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getExternalSpectrumID() {
        return externalSpectrumID;
    }

    /**
     * Sets the value of the externalSpectrumID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExternalSpectrumID(String value) {
        this.externalSpectrumID = value;
    }

    /**
     * Gets the value of the instrumentConfiguration property.
     *
     * @return possible object is
     *         {@link String }
     */
    public InstrumentConfiguration getInstrumentConfiguration() {
        return instrumentConfiguration;
    }

    /**
     * Sets the value of the instrumentConfiguration property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInstrumentConfiguration(InstrumentConfiguration value) {
        this.instrumentConfiguration = value;
    }

}
