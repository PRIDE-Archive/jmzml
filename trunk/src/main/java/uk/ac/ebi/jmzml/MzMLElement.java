package uk.ac.ebi.jmzml;

import uk.ac.ebi.jmzml.model.mzml.*;
import uk.ac.ebi.jmzml.xml.jaxb.resolver.*;

/**
 * Created by IntelliJ IDEA.
 * User: dani
 * Date: 14-Feb-2011
 * Time: 11:31:10
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unused")
public enum MzMLElement {
// ToDo: define and document dependencies between flags/attributes
    // ToDo (for example: a element can not be ID mapped if it is not indexed,
    // ToDo: or an element can not be cached if it is not ID mapped)?
    // ToDo: implement according consistency checks

    // ToDo: complete xpath for all elements
    // ToDo: update indexed flag for elements that should be indexed
    // ToDo: check which elements should be cached
    // ToDo: check for which elements an id map should be generated

    //                               tag name      indexed                  xpath    idMapped class-name    refResolving, reference-resolver                                                                                                                                        class-name
    //AdditionalSearchParams
    AnalyzerComponent("analyzer", true, "/mzML/instrumentConfigurationList/instrumentConfiguration/componentList/analyzer", false, AnalyzerComponent.class, false, null),
    BinaryDataArray("binaryDataArray", true, null /*multiple locations*/, false, BinaryDataArray.class, false, BinaryDataArrayRefResolver.class),
    BinaryDataArrayList("binaryDataArrayList", true, null /*multiple locations*/, false, BinaryDataArrayList.class, false, null),
    Chromatogram("chromatogram", true, "/mzML/run/chromatogramList/chromatogram", false, Chromatogram.class, true, ChromatogramRefResolver.class),
    ChromatogramList("chromatogramList", true, "/mzML/run/chromatogramList", false, ChromatogramList.class, true, ChromatogramListRefResolver.class),
    Component("component", true, null /*multiple locations*/, false, Component.class, false, null),
    ComponentList("componentList", true, "/mzML/instrumentConfigurationList/instrumentConfiguration/componentList", false, ComponentList.class, false, null),
    CV("cv", true, "/mzML/cvList/cv", false, CV.class, false, null),
    CVList("cvList", true, "/mzML/cvList", false, CVList.class, false, null),
    CVParam("cvParam", true, null /*multiple locations*/, false, CVParam.class, true, CVParamRefResolver.class),
    DataProcessing("dataProcessing", true, "/mzML/dataProcessingList/dataProcessing", false, DataProcessing.class, false, null),
    DataProcessingList("dataProcessingList", true, "/mzML/dataProcessingList", false, DataProcessingList.class, false, null),
    DetectorComponent("detectorComponent", true, "/mzML/instrumentConfigurationList/instrumentConfiguration/componentList/detectorComponent", false, DetectorComponent.class, false, null),
    FileDescription("fileDescription", true, "/mzML/fileDescription", false, FileDescription.class, false, null),
    Index("index", true, "/indexedmzML/indexList/index", false, Index.class, false, null),
    IndexedmzML("indexedmzML", true, "/indexedmzML", false, IndexedmzML.class, false, null),
    IndexList("indexList", true, "/indexedmzML/indexList/", false, IndexList.class, false, null),
    InstrumentConfiguration("instrumentConfiguration", true, "/mzML/instrumentConfigurationList/instrumentConfiguration", false, InstrumentConfiguration.class, true, InstrumentConfigurationRefResolver.class),
    InstrumentConfigurationList("instrumentConfigurationList", true, "/mzML/instrumentConfigurationList", false, InstrumentConfigurationList.class, false, null),
    MzML("mzML", true, "/mzML", false, MzML.class, false, null),
    Offset("offset", true, "/indexedmzML/indexList/index/offset", false, Offset.class, false, null),
    ParamGroup("paramGroup", true, null /*multiple locations*/, false, ParamGroup.class, false, null),
    Precursor("precursor", true, "/mzML/run/spectrumList/spectrum/precursorList/precursor", false, Precursor.class, true, PrecursorRefResolver.class),
    PrecurosrList("precursorList", true, "/mzML/run/spectrumList/spectrum/precursorList", false, PrecursorList.class, false, null),
    ProcessingMethod("processingMethod", true, "/mzML/dataProcessingList/dataProcessing/processingMethod", false, ProcessingMethod.class, true, ProcessingMethodRefResolver.class),
    Product("product", true, "/mzML/run/spectrumList/spectrum/productList/product", false, Product.class, false, null),
    ProductList("productList", true, "/mzML/run/spectrumList/spectrum/productList", false, ProductList.class, false, null),
    ReferenceableParamGroup("referenceableParamGroup", true, "/mzML/referenceableParamGroupList/referenceableParamGroup", false, ReferenceableParamGroup.class, false, null),
    ReferenceableParamGroupList("referenceableParamGroupList", true, "/mzML/referenceableParamGroupList", false, ReferenceableParamGroupList.class, false, null),
    ReferenceableParamGroupRef("referenceableParamGroupRef", true, null /*multiple locations*/, false, ReferenceableParamGroupRef.class, true, ReferenceableParamGroupRefResolver.class),
    Run("run", true, "/mzML/run", false, Run.class, false, RunRefResolver.class),
    Sample("sample", true, "/mzML/sampleList/sample", false, Sample.class, false, null),
    SampleList("sampleList", true, "/mzML/sampleList", false, SampleList.class, false, null),
    Scan("scan", true, "/mzML/run/spectrumList/spectrum/scanList/scan", false, Scan.class, true, ScanRefResolver.class),
    ScanList("scanList", true, "/mzML/run/spectrumList/spectrum/scanList", false, ScanList.class, false, null),
    ScanSettings("scanSettings", true, "/mzML/scanSettingsList/scanSettings", false, ScanSettings.class, false, null),
    ScanSettingsList("scanSettingsList", true, "/mzML/scanSettingsList", false, ScanSettingsList.class, false, null),
    ScanWindowList("scanWindowList", true, "/mzML/run/spectrumList/spectrum/scanList/scan/scanWindowList", false, ScanWindowList.class, false, null),
    SelectedIonList("selectedIonList", true, "/mzML/run/spectrumList/spectrum/precursorList/precursor/selectedIonList", false, SelectedIonList.class, false, null),
    Software("software", true, "/mzML/softwareList/software", false, Software.class, false, null),
    SoftwareList("softwareList", true, "/mzML/softwareList", false, SoftwareList.class, false, null),
    SoftwareRef("softwareRef", true, null /*multiple locations*/, false, SoftwareRef.class, true, SoftwareRefResolver.class),
    SourceComponent("sourceComponent", true, "/mzML/instrumentConfigurationList/instrumentConfiguration/componentList/sourceComponent", false, SourceComponent.class, false, null),
    SourceFile("sourceFile", true, "/mzML/fileDescription/sourceFileList/sourceFile", false, SourceFile.class, false, null),
    SourceFileList("sourceFileList", true, "/mzML/fileDescription/sourceFileList", false, SourceFileList.class, false, null),
    SourceFileRefList("sourceFileRefList", true, null /*multiple locations*/, false, SourceFileRefList.class, false, null),
    SourceFileRef("sourceFileRef", true, "/mzML/fileDescription/sourceFileList/sourceFileRef", false, SourceFileRef.class, true, SourceFileRefResolver.class),
    Spectrum("spectrum", true, "/mzML/run/spectrumList/spectrum", false, Spectrum.class, true, SpectrumRefResolver.class),
    SpectrumList("spectrumList", true, "/mzML/run/spectrumList", false, SpectrumList.class, true, SpectrumRefResolver.class),
    TargetList("targetList", true, "/mzML/scanSettingsList/scanSettings/targetList", false, TargetList.class, false, null),
    UserParam("userParam", true, null /*multiple locations*/, false, UserParam.class, true, UserParamRefResolver.class);


    private final String tagName;
    private final boolean indexed;
    private final String xpath;
    private final boolean idMapped;
    private final Class clazz;
    private final boolean autoRefResolving;
    private final Class refResolverClass;

    private <T extends MzMLObject> MzMLElement(String tagName,
                                               boolean indexed,
                                               String xpath,
                                               boolean idMapped,
                                               Class<T> clazz,
                                               boolean autoRefResolving,
                                               Class refResolverClass
    ) {
        this.tagName = tagName;
        this.indexed = indexed;
        this.xpath = xpath;
        this.idMapped = idMapped;
        this.clazz = clazz;
        this.autoRefResolving = autoRefResolving;
        this.refResolverClass = refResolverClass;


        // ToDo: perhaps statically load properties file to load parameters like caching or indexing?
        // for example:
        // CV (TypeProperties.getIndexing(Cv.class), "/mzIdentML/cvList/cv", TypeProperties.getCaching(Cv.class), Cv.class),
    }

    public String getTagName() {
        return tagName;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public String getXpath() {
        return xpath;
    }

    public boolean isIdMapped() {
        return idMapped;
    }

    @SuppressWarnings("unchecked")
    public <T extends MzMLObject> Class<T> getClazz() {
        return clazz;
    }

    public static MzMLElement getType(Class clazz) {
        for (MzMLElement type : MzMLElement.values()) {
            if (type.getClazz() == clazz) {
                return type;
            }
        }
        return null;
    }

    public static MzMLElement getType(String xpath) {
        for (MzMLElement type : MzMLElement.values()) {
            if (type.getXpath() != null && type.getXpath().equals(xpath)) {
                return type;
            }
        }
        return null;
    }

    public boolean isAutoRefResolving() {
        return autoRefResolving;
    }

    @SuppressWarnings("unchecked")
    public <R extends AbstractReferenceResolver> Class<R> getRefResolverClass() {
        return refResolverClass;
    }

    @Override
    public String toString() {
        return "MzMLElement{" +
                ", xpath='" + xpath + '\'' +
                ", clazz=" + clazz +
                '}';
    }
}
