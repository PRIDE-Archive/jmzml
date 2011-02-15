package uk.ac.ebi.jmzml;

import uk.ac.ebi.jmzml.model.mzml.UserParam;
import uk.ac.ebi.jmzml.model.mzml.interfaces.MzMLObject;
import uk.ac.ebi.jmzml.model.mzml.*;

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

    //                               tag name                        xpath                                                                                                                                         class-name
    //AdditionalSearchParams
    AnalyzerComponent("analyzer", "/mzML/instrumentConfigurationList/instrumentConfiguration/componentList/analyzer", AnalyzerComponent.class),
    BinaryDataArray("binaryDataArray", null /*multiple locations*/, BinaryDataArray.class),
    BinaryDataArrayList("binaryDataArrayList", null /*multiple locations*/, BinaryDataArrayList.class),
    Chromatogram("chromatogram","/mzML/run/chromatogramList/chromatogram", Chromatogram.class),
    ChromatogramList("chromatogramList","/mzML/run/chromatogramList",ChromatogramList.class),
    Component("component", null /*multiple locations*/, Component.class),
    ComponentList("componentList","/mzML/instrumentConfigurationList/instrumentConfiguration/componentList", ComponentList.class),
    CV("cv","/mzML/cvList/cv", CV.class),
    CVList("cvList", "/mzML/cvList", CVList.class),
    CVParam("cvParam", null /*multiple locations*/, CVParam.class),
    DataProcessing("dataProcessing","/mzML/dataProcessingList/dataProcessing", DataProcessing.class),
    DataProcessingList("dataProcessingList", "/mzML/dataProcessingList", DataProcessingList.class),
    DetectorComponent("detectorComponent","/mzML/instrumentConfigurationList/instrumentConfiguration/componentList/detectorComponent", DetectorComponent.class),
    FileDescription("fileDescription","/mzML/fileDescription", FileDescription.class),
    Index("index","/indexedmzML/index", Index.class),
    IndexedmzML("indexedmzML", "/indexedmzML", IndexedmzML.class),
    IndexList("indexList", "/indexedmzML/indexList/", IndexList.class),
    InstrumentConfiguration("instrumentConfiguration","/mzML/instrumentConfigurationList/instrumentConfiguration", InstrumentConfiguration.class),
    InstrumentConfigurationList("instrumentConfigurationList", "/mzML/instrumentConfigurationList", InstrumentConfigurationList.class),
    MzML("mzML", "/mzML", MzML.class),
    Offset("offset", "/indexedmzML/index/offset", Offset.class),
    ParamGroup("paramGroup", null /*multiple locations*/, ParamGroup.class),
    Precursor("precursor", "/mzML/run/spectrumList/spectrum/precursorList/precursor", Precursor.class),
    PrecurosrList("precursorList","/mzML/run/spectrumList/spectrum/precursorList", PrecursorList.class),
    ProcessingMethod("processingMethod","/mzML/dataProcessingList/dataProcessing/processingMethod", ProcessingMethod.class),
    Product("product", "/mzML/run/spectrumList/spectrum/productList/product", Product.class),
    ProductList("productList", "/mzML/run/spectrumList/spectrum/productList", ProductList.class),
    ReferenceableParamGroup("referenceableParamGroup","/mzML/referenceableParamGroupList/referenceableParamGroup", ReferenceableParamGroup.class),
    ReferenceableParamGroupList("referenceableParamGroupList","/mzML/referenceableParamGroupList", ReferenceableParamGroupList.class),
    ReferenceableParamGroupRef("referenceableParamGroupRef", null /*multiple locations*/, ReferenceableParamGroupRef.class),
    Run("run", "/mzML/run", Run.class),
    Sample("sample","/mzML/sampleList/sample", Sample.class),
    SampleList("sampleList", "/mzML/sampleList", SampleList.class),
    Scan("scan", "/mzML/run/spectrumList/spectrum/scanList/scan", Scan.class),
    ScanList("scanList", "/mzML/run/spectrumList/spectrum/scanList", ScanList.class),
    ScanSettings("scanSettings", "/mzML/scanSettingsList/scanSettings", ScanSettings.class),
    ScanSettingsList("scanSettingsList", "/mzML/scanSettingsList", ScanSettingsList.class),
    ScanWindowList("scanWindowList", "/mzML/run/spectrumList/spectrum/scanList/scan/scanWindowList", ScanWindowList.class),
    SelectedIonList("selectedIonList", "/mzML/run/spectrumList/spectrum/precursorList/precursor/selectedIonList", SelectedIonList.class),
    Software("software", "/mzML/softwareList/software", Software.class),
    SoftwareList("softwareList", "/mzML/softwareList", SoftwareList.class),
    SoftwareRef("softwareRef", null /*multiple locations*/, SoftwareRef.class),
    SourceComponent("sourceComponent", "/mzML/instrumentConfigurationList/instrumentConfiguration/componentList/sourceComponent", SourceComponent.class),
    SourceFile("sourceFile", "/mzML/fileDescription/sourceFileList/sourceFile", SourceFile.class),
    SourceFileList("sourceFileList", "/mzML/fileDescription/sourceFileList", SourceFileList.class),
    SourceFileRefList("sourceFileRefList", null /*multiple locations*/, SourceFileRefList.class),
    Spectrum("spectrum", "/mzML/run/spectrumList/spectrum", Spectrum.class),
    SpectrumList("spectrumList", "/mzML/run/spectrumList", SpectrumList.class),
    TargetList("targetList", "/mzML/scanSettingsList/scanSettings/targetList", TargetList.class),
    UserParam("userParam", null /*multiple locations*/, UserParam.class);



    private final String tagName;
    private final String xpath;
    private final Class clazz;


    private <T extends MzMLObject> MzMLElement(String tagName,
                                               String xpath,
                                               Class<T> clazz
                                               ) {
        this.tagName = tagName;
        this.xpath = xpath;
        this.clazz = clazz;


        // ToDo: perhaps statically load properties file to load parameters like caching or indexing?
        // for example:
        // CV (TypeProperties.getIndexing(Cv.class), "/mzIdentML/cvList/cv", TypeProperties.getCaching(Cv.class), Cv.class),
    }

    public String getTagName() {
        return tagName;
    }

    public String getXpath() {
        return xpath;
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

    @Override
    public String toString() {
        return "MzMLElement{" +
                ", xpath='" + xpath + '\'' +
                ", clazz=" + clazz +
                '}';
    }
    }
