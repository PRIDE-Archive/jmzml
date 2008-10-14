/*
 * Date: 22/7/2008
 * Author: rcote
 * File: uk.ac.ebi.jmzml.model.mzml.InstrumentConfiguration
 *
 * jmzml is Copyright 2008 The European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.19 at 10:48:08 AM BST
//


package uk.ac.ebi.jmzml.model.mzml;

import uk.ac.ebi.jmzml.model.mzml.params.InstrumentConfigurationCVParam;
import uk.ac.ebi.jmzml.model.mzml.params.InstrumentConfigurationUserParam;
import uk.ac.ebi.jmzml.model.mzml.utilities.ParamGroupUpdater;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;


/**
 * Description of a particular hardware configuration of a mass spectrometer. Each
 * configuration must have one (and only one) of the three different components used for an analysis. For
 * hybrid instruments, such as an LTQ-FT, there must be one configuration for each permutation of the
 * components that is used in the document. For software configuration, use a ReferenceableParamGroup
 * element.
 * <p/>
 * <p/>
 * <p>Java class for InstrumentConfigurationType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="InstrumentConfigurationType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://psi.hupo.org/schema_revision/mzML_1.0.0}ParamGroupType">
 *       &lt;sequence>
 *         &lt;element name="componentList" type="{http://psi.hupo.org/schema_revision/mzML_1.0.0}ComponentListType"/>
 *         &lt;element name="softwareRef" type="{http://psi.hupo.org/schema_revision/mzML_1.0.0}SoftwareRefType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstrumentConfigurationType", propOrder = {
        "componentList",
        "softwareRef"
        })
public class InstrumentConfiguration
        extends ParamGroup
        implements Serializable {

    private final static long serialVersionUID = 100L;
    @XmlElement(required = true)
    protected ComponentList componentList;
    protected SoftwareRef softwareRef;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the componentList property.
     *
     * @return possible object is
     *         {@link ComponentList }
     */
    public ComponentList getComponentList() {
        return componentList;
    }

    /**
     * Sets the value of the componentList property.
     *
     * @param value allowed object is
     *              {@link ComponentList }
     */
    public void setComponentList(ComponentList value) {
        this.componentList = value;
    }

    /**
     * Gets the value of the softwareRef property.
     *
     * @return possible object is
     *         {@link SoftwareRef }
     */
    public SoftwareRef getSoftwareRef() {
        return softwareRef;
    }

    /**
     * Sets the value of the softwareRef property.
     *
     * @param value allowed object is
     *              {@link SoftwareRef }
     */
    public void setSoftwareRef(SoftwareRef value) {
        this.softwareRef = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        try {
            //update our paramGroup list
            ParamGroupUpdater.updateParamGroupSubclasses(this, InstrumentConfigurationCVParam.class, InstrumentConfigurationUserParam.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(this.getClass().getName() + ".afterUnmarshall: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(this.getClass().getName() + ".afterUnmarshall: " + e.getMessage());
        }
    }

}