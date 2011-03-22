package persistence;

import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uk.ac.ebi.jmzml.MzMLElement;
import uk.ac.ebi.jmzml.model.mzml.*;
import uk.ac.ebi.jmzml.xml.io.MzMLObjectIterator;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshallerException;

import java.math.BigInteger;
import java.net.URL;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: dani
 * Date: 04/03/11
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public class Persister {
    Session session = null;
    //map objects containing id->hid
    Map<String, Long> cvMap = new HashMap<String, Long>();
    Map<String, Long> sourceFileMap = new HashMap<String, Long>();
    Map<String, Long> referenceableParamGroupMap = new HashMap<String, Long>();
    Map<String, Long> sampleMap = new HashMap<String, Long>();
    Map<String, Long> softwareMap = new HashMap<String, Long>();
    Map<String, Long> scanSettingsMap = new HashMap<String, Long>();
    Map<String, Long> dataProcessingMap = new HashMap<String, Long>();
    Map<String, Long> instrumentConfigurationMap = new HashMap<String, Long>();
    Map<String, Long> spectrumMap = new HashMap<String, Long>();
    //number of Spectrum to be persisted in one go, should reduce memory usage
    public static final int BATCH_SPECTRUM = 100;

    public Persister(SessionFactory factory) {
        openSessionFactory(factory);
    }

    //will persist the whole mzML
    public long persistmzML(String fileName) {


        CVList cvList;
        FileDescription fileDescription;
        SourceFileList sourceFileList;
        ReferenceableParamGroupList referenceableParamGroupList;
        SampleList sampleList;
        SoftwareList softwareList;
        ScanSettingsList scanSettingsList;
        DataProcessingList dataProcessingList;
        InstrumentConfigurationList instrumentConfigurationList;
        Run run;

        long mzML_hid = 0;

        //order to persist elements in mzML
        try {
            URL xmlFileURL = this.getClass().getClassLoader().getResource(fileName);
//            session.setCacheMode(CacheMode.IGNORE);
            session.getTransaction().begin();
            if (xmlFileURL == null) {
                System.out.println("File not found");
                return 0;
            } else {
                // 1. parse file
                MzMLUnmarshaller unmarshaller = new MzMLUnmarshaller(xmlFileURL);
//                session.getTransaction().begin();
                //persists CVList and get Map with CV id->hid for future reference
                cvList = persistCVList(unmarshaller);
                //need to persiste ReferenceableParamGroup and RefParamGroup id->hid for future reference
                referenceableParamGroupList = persistReferenceableParamGroupList(unmarshaller);
                //persist FileDescription info
                fileDescription = persistFileDescription(unmarshaller);
                //persiste SampleList info
                sampleList = persistSampleList(unmarshaller);
                //persist SoftwareList info
                softwareList = persistSoftwareList(unmarshaller);
                //persist ScanSettingsList
                scanSettingsList = persistScanSettingsList(unmarshaller);
                //persist DataProcessingList
                dataProcessingList = persistDataProcessingList(unmarshaller);
                //persist InstrumentConfigurationList
                instrumentConfigurationList = persistInstrumentConfigurationList(unmarshaller);
                //persist Run
                run = persistRun(unmarshaller);
                //and finally, persist the mzML document

                MzML mzML = new MzML();
                //add elements
                mzML.setCvList(cvList);
                mzML.setFileDescription(fileDescription);
                mzML.setReferenceableParamGroupList(referenceableParamGroupList);
                mzML.setSampleList(sampleList);
                mzML.setSoftwareList(softwareList);
                mzML.setScanSettingsList(scanSettingsList);
                mzML.setInstrumentConfigurationList(instrumentConfigurationList);
                mzML.setDataProcessingList(dataProcessingList);
                mzML.setRun(run);
                //and add attributes
                Map<String, String> attributes = unmarshaller.getSingleElementAttributes(MzMLElement.MzML.getXpath());
                if (attributes.containsKey("accession")) {
                    mzML.setAccession(attributes.get("accession"));
                }
                mzML.setVersion(attributes.get("version"));
                if (attributes.containsKey("id")) {
                    mzML.setId(attributes.get("id"));
                }
                CacheMode cache = session.getCacheMode();
                session.save(mzML);

                mzML_hid = mzML.getHid();
            }
            session.flush();
            session.getTransaction().commit();
            closeSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return mzML_hid;
    }

    private Run persistRun(MzMLUnmarshaller unmarshaller) {
        Run run;
        ChromatogramList chromatogramList;


        SpectrumList spectrumList;
        //first, unmarshal and persist the SpectrumList
        spectrumList = persistSpectrumList(unmarshaller);
        //then, unmarshal and persist ChromatogramList from XML
        chromatogramList = persistChromatogramList(unmarshaller);
        //finally, persist the run
        run = new Run();
        //proxy objects already in database
        run.setChromatogramList(chromatogramList);
        run.setSpectrumList(spectrumList);
        //and the attributes
        Map<String, String> attributes = unmarshaller.getSingleElementAttributes(MzMLElement.Run.getXpath());
        if (attributes.containsKey("startTimeStamp")) {
            run.setStartTimeStamp(javax.xml.bind.DatatypeConverter.parseDateTime(attributes.get("startTimeStamp")));
        }
        if (attributes.containsKey("sampleRef")) {
            if (sampleMap.containsKey(attributes.get("sampleRef"))) {
//                run.setSampleRef(sampleMap.get(attributes.get("sampleRef")).toString());
                Sample sampleProxy = (Sample) session.load(Sample.class, sampleMap.get(attributes.get("sampleRef")));
                run.setSample(sampleProxy);
            } else {
                throw new IllegalStateException("Sample Ref not present in database!: " + attributes.get("sampleRef"));
            }
        }
        if (attributes.containsKey("defaultSourceFileRef")) {
            if (sourceFileMap.containsKey(attributes.get("defaultSourceFileRef"))) {
//                run.setDefaultSourceFileRef(sourceFileMap.get(attributes.get("defaultSourceFileRef")).toString());
                SourceFile sourceFileProxy = (SourceFile) session.load(SourceFile.class, sourceFileMap.get(attributes.get("defaultSourceFileRef")));
                run.setDefaultSourceFile(sourceFileProxy);
            } else {
                throw new IllegalStateException("SourceFile Ref not present in database!: " + attributes.get("defaultSourceFileRef"));
            }
        }
        run.setId(attributes.get("id"));
        if (instrumentConfigurationMap.containsKey(attributes.get("defaultInstrumentConfigurationRef"))) {
//            run.setDefaultInstrumentConfigurationRef(instrumentConfigurationMap.get(attributes.get("defaultInstrumentConfigurationRef")).toString());
            InstrumentConfiguration instrumentConfigurationProxy = (InstrumentConfiguration) session.load(InstrumentConfiguration.class, instrumentConfigurationMap.get(attributes.get("defaultInstrumentConfigurationRef")));
            run.setDefaultInstrumentConfiguration(instrumentConfigurationProxy);
        } else {
            throw new IllegalStateException("InstrumentConfiguration Ref not present in database!: " + attributes.get("defaultInstrumentConfigurationRef"));
        }
        //finally, update the RefParam and CV
        //TODO: how to extract info RefParam and CvParam from XML for Run ??
        session.persist(run);
        session.flush();
        session.clear();
        return (Run) session.load(Run.class, run.getHid());
    }

    //method responsible for persisting batches of Spectrum in the database and updating all references
    private SpectrumList persistSpectrumList(MzMLUnmarshaller unmarshaller) {
        SpectrumList spectrumList;
        //should contain proxy objects for the spectrumList
        List<Spectrum> spectrumArray = new ArrayList<Spectrum>();
        //should contain spectrum that couldn't be stored in database because are referencing a spectrum not in database yet
        Map<String, Spectrum> missedSpectrum = new HashMap<String, Spectrum>();
        //unmarshal the batch of spectrum
        //first, get all spectrumIds
        String[] spectrumIds = Arrays.copyOf(unmarshaller.getSpectrumIDs().toArray(), unmarshaller.getSpectrumIDs().size(), String[].class);
        //then unmarshal and persist them
        for (int i = 0; i < spectrumIds.length; i = i + BATCH_SPECTRUM) {
            for (int j = i; (j < BATCH_SPECTRUM + i) && (j < spectrumIds.length); j++) {
                //unmarshal the Spectrum
                try {
                    Spectrum spectrum = unmarshaller.getSpectrumById(spectrumIds[j]);
                    //method to update all references in the Spectrum object
                    if (updateSpectrum(spectrum)) {
                        //if all could be replaced, persist the object and add the id->hid to the cache
                        session.persist(spectrum);
                        spectrumMap.put(spectrum.getId(), spectrum.getHid());
                    } else {
                        //if it couldn't be updated all references, add it to the missed ones for later persistance
                        missedSpectrum.put(spectrum.getId(), spectrum);

                    }
                } catch (MzMLUnmarshallerException ex) {
                    System.err.println("Could not unmarshal spectrum " + spectrumIds[j] + " from file: " + ex.getMessage());

                }
            }
            //flush and clear session to prevent memory issues
            session.flush();
            session.clear();
//            session.evict();
        }
        //TODO:: check if the missed spectrum can be now persisted
//        persistMissedSpectrum();
        //finally, persist the spectrumList
        spectrumList = new SpectrumList();
        //add the persisted proxy objects to the array to be stored
        for (Long spectrum_hid : spectrumMap.values()) {
            Spectrum spectrum_proxy = (Spectrum) session.load(Spectrum.class, spectrum_hid);
            spectrumList.getSpectrum().add(spectrum_proxy);
        }

        //and the 2 attributes
        Map<String, String> attributes = unmarshaller.getSingleElementAttributes(MzMLElement.SpectrumList.getXpath());
        spectrumList.setCount(new BigInteger(attributes.get("count")));
        if (dataProcessingMap.containsKey(attributes.get("defaultDataProcessingRef"))) {
//            spectrumList.setDefaultDataProcessingRef(dataProcessingMap.get(attributes.get("defaultDataProcessingRef")).toString());
            DataProcessing defaultDataProcessingProxy = (DataProcessing) session.load(DataProcessing.class, dataProcessingMap.get(attributes.get("defaultDataProcessingRef")));
            spectrumList.setDefaultDataProcessing(defaultDataProcessingProxy);
        } else {
            throw new IllegalStateException("DataProcessing Ref not present in database!: " + attributes.get("defaultDataProcessingRef"));
        }
        session.persist(spectrumList);
        return (SpectrumList) session.load(SpectrumList.class, spectrumList.getHid());
    }

    //method will update all references to other objects, if spectrum_ref could be updated as well, will return true, false otherwise
    private boolean updateSpectrum(Spectrum spectrum) {
        boolean allReferencesUpdated = true;
        boolean referencesUpdated = false;
        //update dataProcessingRef
        if (spectrum.getDataProcessingRef() != null) {
            if (spectrumMap.containsKey(spectrum.getDataProcessingRef())) {
//                spectrum.setDataProcessingRef(spectrumMap.get(spectrum.getDataProcessingRef()).toString());
                DataProcessing dataProcessingProxy = (DataProcessing) session.load(DataProcessing.class, spectrum.getDataProcessingRef());
                spectrum.setDataProcessing(dataProcessingProxy);
            } else {
                throw new IllegalStateException("DataProcessing Ref not present in database!: " + spectrum.getDataProcessingRef());
            }
        }
        //update SourceFile Ref
        if (spectrum.getSourceFileRef() != null) {
            if (spectrumMap.containsKey(spectrum.getSourceFileRef())) {
//                spectrum.setSourceFileRef(sourceFileMap.get(spectrum.getSourceFileRef()).toString());
                SourceFile sourceFileProxy = (SourceFile) session.load(SourceFile.class, sourceFileMap.get(spectrum.getSourceFileRef()));
                spectrum.setSourceFile(sourceFileProxy);
            } else {
                throw new IllegalStateException("SourceFile Ref not present in database!: " + spectrum.getSourceFileRef());
            }
        }
        //update ParamGroup info
        if (spectrum.getReferenceableParamGroupRef().size() > 0) {
            updateParamGroupRef(spectrum.getReferenceableParamGroupRef());
        }
        //update CV info
        if (spectrum.getCvParam().size() > 0) {
            updateCVParam(spectrum.getCvParam());
        }
        //update ScanList
        if (spectrum.getScanList() != null) {
            referencesUpdated = updateScanList(spectrum.getScanList());
            if (!referencesUpdated) {
                allReferencesUpdated = false; //this spectrum cannot be persisted, some spectrum_ref were not replaced
            }
        }
        //update PrecursorList
        if (spectrum.getPrecursorList() != null) {
            Iterator it = spectrum.getPrecursorList().getPrecursor().iterator();
            while (it.hasNext()) {
                Precursor precursor = (Precursor) it.next();
                referencesUpdated = updatePrecursor(precursor);
                if (!referencesUpdated) {
                    allReferencesUpdated = false; //this spectrum cannot be persisted, some spectrum_ref were not replaced
                }
            }
        }
        //update BinaryDataArrayList
        if (spectrum.getBinaryDataArrayList() != null) {
            updateBinaryDataArrayList(spectrum.getBinaryDataArrayList().getBinaryDataArray());
        }
        //update ProductList
        if (spectrum.getProductList() != null) {
            Iterator it = spectrum.getProductList().getProduct().iterator();
            while (it.hasNext()) {
                Product product = (Product) it.next();
                updateProduct(product);
            }
        }
        return allReferencesUpdated;
    }

    //method that will update references in the ScanList element and all its subelements
    private boolean updateScanList(ScanList scanList) {
        boolean referencesUpdated = true;

        if (scanList.getReferenceableParamGroupRef().size() > 0) {
            updateParamGroupRef(scanList.getReferenceableParamGroupRef());
        }
        if (scanList.getCvParam().size() > 0) {
            updateCVParam(scanList.getCvParam());
        }
        Iterator it = scanList.getScan().iterator();
        while (it.hasNext()) {
            Scan scan = (Scan) it.next();
            if (scan.getSpectrumRef() != null) {
                if (spectrumMap.containsKey(scan.getSpectrumRef())) {
//                    scan.setSpectrumRef(spectrumMap.get(scan.getSpectrumRef()).toString());
                    Spectrum spectrumProxy = (Spectrum) session.load(Spectrum.class, spectrumMap.get(scan.getSpectrumRef()));
                    scan.setSpectrum(spectrumProxy);
                } else {
                    referencesUpdated = false; //spectrum not in database yet
                }
            }

            if (scan.getSourceFileRef() != null) {
                if (sourceFileMap.containsKey(scan.getSourceFileRef())) {
//                    scan.setSourceFileRef(sourceFileMap.get(scan.getSourceFileRef()).toString());
                    SourceFile sourceFileProxy = (SourceFile) session.load(SourceFile.class, sourceFileMap.get(scan.getSourceFileRef()));
                    scan.setSourceFile(sourceFileProxy);
                } else {
                    throw new IllegalStateException("SourceFile Ref not present in database!: " + scan.getSourceFileRef());
                }
            }

            if (scan.getInstrumentConfigurationRef() != null) {
                if (instrumentConfigurationMap.containsKey(scan.getInstrumentConfigurationRef())) {
//                    scan.setInstrumentConfigurationRef(instrumentConfigurationMap.get(scan.getInstrumentConfigurationRef()).toString());
                    InstrumentConfiguration instrumentConfigurationProxy = (InstrumentConfiguration) session.load(InstrumentConfiguration.class, instrumentConfigurationMap.get(scan.getInstrumentConfigurationRef()));
                    scan.setInstrumentConfiguration(instrumentConfigurationProxy);
                } else {
                    throw new IllegalStateException("InstrumentConfiguration Ref not present in database!: " + scan.getInstrumentConfigurationRef());
                }
            }

            if (scan.getReferenceableParamGroupRef().size() > 0) {
                updateParamGroupRef(scan.getReferenceableParamGroupRef());
            }

            if (scan.getCvParam().size() > 0) {
                updateCVParam(scan.getCvParam());
            }

            //and finally update the ScanWindowList
            if (scan.getScanWindowList() != null) {
                Iterator it1 = scan.getScanWindowList().getScanWindow().iterator();
                while (it1.hasNext()) {
                    ParamGroup scanWindow = (ParamGroup) it1.next();
                    if (scanWindow.getReferenceableParamGroupRef().size() > 0) {
                        updateParamGroupRef(scanWindow.getReferenceableParamGroupRef());
                    }
                    if (scanWindow.getCvParam().size() > 0) {
                        updateCVParam(scanWindow.getCvParam());
                    }
                }
            }
        }
        return referencesUpdated;
    }

    private void updateBinaryDataArrayList(List<BinaryDataArray> binaryDataArrayList) {

        Iterator it1 = binaryDataArrayList.iterator();
        while (it1.hasNext()) {
            BinaryDataArray binaryDataArray = (BinaryDataArray) it1.next();
            if (binaryDataArray.getDataProcessingRef() != null) {
                if (dataProcessingMap.containsKey(binaryDataArray.getDataProcessingRef())) {
//                    binaryDataArray.setDataProcessingRef(dataProcessingMap.get(binaryDataArray.getDataProcessingRef()).toString());
                    DataProcessing dataProcessingProxy = (DataProcessing) session.load(DataProcessing.class, dataProcessingMap.get(binaryDataArray.getDataProcessingRef()));
                    binaryDataArray.setDataProcessing(dataProcessingProxy);
                } else {
                    throw new IllegalStateException("DataProcessing Ref not present in database!: " + binaryDataArray.getDataProcessingRef());
                }
            }
            if (binaryDataArray.getReferenceableParamGroupRef().size() > 0) {
                updateParamGroupRef(binaryDataArray.getReferenceableParamGroupRef());
            }
            if (binaryDataArray.getCvParam().size() > 0) {
                updateCVParam(binaryDataArray.getCvParam());
            }

        }
    }

    private void updateProduct(Product product) {
        if (product.getIsolationWindow() != null) {
            if (product.getIsolationWindow().getReferenceableParamGroupRef().size() > 0) {
                updateParamGroupRef(product.getIsolationWindow().getReferenceableParamGroupRef());
            }
            if (product.getIsolationWindow().getCvParam().size() > 0) {
                updateCVParam(product.getIsolationWindow().getCvParam());
            }
        }
    }

    private ChromatogramList persistChromatogramList(MzMLUnmarshaller unmarshaller) {
        //extract ChromatogramList from XML
        ChromatogramList chromatogramList = unmarshaller.unmarshalFromXpath(MzMLElement.ChromatogramList.getXpath(), MzMLElement.ChromatogramList.getClazz());
        //need to update the ref ParamGroup and DataProcessing
        if (dataProcessingMap.containsKey(chromatogramList.getDefaultDataProcessingRef())) {
//            chromatogramList.setDefaultDataProcessingRef(dataProcessingMap.get(chromatogramList.getDefaultDataProcessingRef()).toString());
            DataProcessing dataProcessingProxy = (DataProcessing) session.load(DataProcessing.class, dataProcessingMap.get(chromatogramList.getDefaultDataProcessingRef()));
            chromatogramList.setDefaultDataProcessing(dataProcessingProxy);
        } else {
            throw new IllegalStateException("DataProcessing Ref not present in database!: " + chromatogramList.getDefaultDataProcessingRef());
        }

        Iterator it = chromatogramList.getChromatogram().iterator();
        while ((it.hasNext())) {
            //for the Chromatogram info, update the dataProcessingRef
            Chromatogram chromatogram = (Chromatogram) it.next();
            if (chromatogram.getDataProcessingRef() != null) {
                if (dataProcessingMap.containsKey(chromatogram.getDataProcessingRef())) {
//                chromatogram.setDataProcessingRef(dataProcessingMap.get(chromatogram.getDataProcessingRef()).toString());
                    DataProcessing dataProcessingProxy = (DataProcessing) session.load(DataProcessing.class, dataProcessingMap.get(chromatogram.getDataProcessingRef()));
                    chromatogram.setDataProcessing(dataProcessingProxy);

                } else {
                    throw new IllegalStateException("DataProcessing Ref not present in database!: " + chromatogram.getDataProcessingRef());
                }
            }
            //and the refParamGroup and CVParam
            if (chromatogram.getReferenceableParamGroupRef().size() > 0) {
                updateParamGroupRef(chromatogram.getReferenceableParamGroupRef());
            }
            if (chromatogram.getCvParam().size() > 0) {
                updateCVParam(chromatogram.getCvParam());
            }
            //update BinaryDataArray
            updateBinaryDataArrayList(chromatogram.getBinaryDataArrayList().getBinaryDataArray());
            //update the Product, if any
            if (chromatogram.getProduct() != null) {
                updateProduct(chromatogram.getProduct());
            }
            //finally, update the Precursor, if any
            if (chromatogram.getPrecursor() != null) {
                boolean spectrumMissing = updatePrecursor(chromatogram.getPrecursor());
                if (spectrumMissing) {
                    //this should not happen, all spectrum should already be in database
                    throw new IllegalStateException("Spectrum Ref not present in database!: " + chromatogram.getPrecursor().getSpectrumRef());
                }
            }
        }
        session.persist(chromatogramList);
        //and return the proxy object
        return (ChromatogramList) session.load(ChromatogramList.class, chromatogramList.getHid());
    }

    //will replace different references in the Precursor object
    private boolean updatePrecursor(Precursor precursor) {

        boolean referencesUpdated = true;

        if (precursor.getIsolationWindow() != null) {
            if (precursor.getIsolationWindow().getReferenceableParamGroupRef().size() > 0) {
                updateParamGroupRef(precursor.getIsolationWindow().getReferenceableParamGroupRef());
            }
            if (precursor.getIsolationWindow().getCvParam().size() > 0) {
                updateCVParam(precursor.getIsolationWindow().getCvParam());
            }
        }
        if (precursor.getSelectedIonList() != null) {
            Iterator it2 = precursor.getSelectedIonList().getSelectedIon().iterator();
            while (it2.hasNext()) {
                ParamGroup selectedIon = (ParamGroup) it2.next();
                if (selectedIon.getReferenceableParamGroupRef().size() > 0) {
                    updateParamGroupRef(selectedIon.getReferenceableParamGroupRef());
                }
                if (selectedIon.getCvParam().size() > 0) {
                    updateCVParam(selectedIon.getCvParam());
                }
            }
        }
        //update in Activation
        if (precursor.getActivation().getReferenceableParamGroupRef().size() > 0) {
            updateParamGroupRef(precursor.getActivation().getReferenceableParamGroupRef());
        }
        if (precursor.getActivation().getCvParam().size() > 0) {
            updateCVParam(precursor.getActivation().getCvParam());
        }
        //and finally update the spectrumRef and sourceFileRef, if present
        if (precursor.getSourceFileRef() != null) {
            if (sourceFileMap.containsKey(precursor.getSourceFileRef())) {
//                precursor.setSourceFileRef(sourceFileMap.get(precursor.getSourceFileRef()).toString());
                SourceFile sourceFileProxy = (SourceFile) session.load(SourceFile.class, sourceFileMap.get(precursor.getSourceFileRef()));
                precursor.setSourceFile(sourceFileProxy);
            } else {
                throw new IllegalStateException("SourceFile Ref not present in database!: " + precursor.getSourceFileRef());
            }
        }
        if (precursor.getSpectrumRef() != null) {
            if (spectrumMap.containsKey(precursor.getSpectrumRef())) {
//                precursor.setSpectrumRef(spectrumMap.get(precursor.getSpectrumRef()).toString());
                Spectrum spectrumProxy = (Spectrum) session.load(Spectrum.class, spectrumMap.get(precursor.getSpectrumRef()));
                precursor.setSpectrum(spectrumProxy);
            } else {
                referencesUpdated = false; //the spectrum referenced by this precursor is not in the database yet
            }
        }
        return referencesUpdated;
    }

    //will persist InstrumentConfigurationList, update instrumentConfigurationMap with the id->hid reference, and update the scanSettings and Software references
    private InstrumentConfigurationList persistInstrumentConfigurationList(MzMLUnmarshaller unmarshaller) {
        //extract InstrumentConfigurationList from XML
        InstrumentConfigurationList instrumentConfigurationList = unmarshaller.unmarshalFromXpath(MzMLElement.InstrumentConfigurationList.getXpath(), MzMLElement.InstrumentConfigurationList.getClazz());
        //need to update the ref paramGroup first, if present
        Iterator it1 = instrumentConfigurationList.getInstrumentConfiguration().iterator();
        while (it1.hasNext()) {
            InstrumentConfiguration instrumentConfiguration = (InstrumentConfiguration) it1.next();
            if (instrumentConfiguration.getReferenceableParamGroupRef().size() > 0) {
                updateParamGroupRef(instrumentConfiguration.getReferenceableParamGroupRef());
            }
            if (instrumentConfiguration.getCvParam().size() > 0) {
                updateCVParam(instrumentConfiguration.getCvParam());
            }
            //now, update it in the component list: the source
            ComponentList componentList = instrumentConfiguration.getComponentList();
            if (componentList != null) {
                Iterator it2 = componentList.getSource().iterator();
                while (it2.hasNext()) {
                    SourceComponent sourceComponent = (SourceComponent) it2.next();
                    if (sourceComponent.getReferenceableParamGroupRef().size() > 0) {
                        updateParamGroupRef(sourceComponent.getReferenceableParamGroupRef());
                    }
                    if (sourceComponent.getCvParam().size() > 0) {
                        updateCVParam(sourceComponent.getCvParam());
                    }
                }
                //the analyzer
                Iterator it3 = componentList.getAnalyzer().iterator();
                while (it3.hasNext()) {
                    AnalyzerComponent analyzerComponent = (AnalyzerComponent) it3.next();
                    if (analyzerComponent.getReferenceableParamGroupRef().size() > 0) {
                        updateParamGroupRef(analyzerComponent.getReferenceableParamGroupRef());
                    }
                    if (analyzerComponent.getCvParam().size() > 0) {
                        updateCVParam(analyzerComponent.getCvParam());
                    }
                }

                //the detector
                Iterator it4 = componentList.getDetector().iterator();
                while (it4.hasNext()) {
                    DetectorComponent detectorComponent = (DetectorComponent) it4.next();
                    if (detectorComponent.getReferenceableParamGroupRef().size() > 0) {
                        updateParamGroupRef(detectorComponent.getReferenceableParamGroupRef());
                    }
                    if (detectorComponent.getCvParam().size() > 0) {
                        updateCVParam(detectorComponent.getCvParam());
                    }
                }
            }
            //now update the scanSettings Ref
            if (instrumentConfiguration.getScanSettingsRef() != null) {
                if (scanSettingsMap.containsKey(instrumentConfiguration.getScanSettingsRef())) {
//                    instrumentConfiguration.setScanSettingsRef(scanSettingsMap.get(instrumentConfiguration.getScanSettingsRef()).toString());
                    ScanSettings scanSettingsProxy = (ScanSettings) session.load(ScanSettings.class, scanSettingsMap.get(instrumentConfiguration.getScanSettingsRef()));
                } else {
                    throw new IllegalStateException("ScanSettings Ref not present in database!: " + instrumentConfiguration.getScanSettingsRef());
                }
            }
            //and finally, update the software Ref
            String softwareRef = instrumentConfiguration.getSoftwareRef();
            if (softwareRef != null) {
                if (softwareMap.containsKey(softwareRef)) {
                    Software proxySoftware = (Software) session.load(Software.class, softwareMap.get(softwareRef));

                    instrumentConfiguration.setSoftware(proxySoftware);
//                instrumentConfiguration.setSoftwareRef(softwareMap.get(softwareRef).toString());
                } else {
                    throw new IllegalStateException("Software Ref not present in database!: " + instrumentConfiguration.getSoftwareRef());
                }
            }
        }
        //and persist to database
        session.persist(instrumentConfigurationList);

        //load will return the object from the cache or proxy object
        Iterator it = instrumentConfigurationList.getInstrumentConfiguration().iterator();
        while (it.hasNext()) {
            InstrumentConfiguration instrumentConfiguration = (InstrumentConfiguration) it.next();
            instrumentConfigurationMap.put(instrumentConfiguration.getId(), instrumentConfiguration.getHid());
        }
        session.flush();
        session.clear();
        //return proxy object
        return (InstrumentConfigurationList) session.load(InstrumentConfigurationList.class, instrumentConfigurationList.getHid());
    }

    //will persist DataProcessingList, update DataProcessingMap with the id->hid reference and update refParamGroup and SoftwareMap
    private DataProcessingList persistDataProcessingList(MzMLUnmarshaller unmarshaller) {
        //extract DataProcessingList from XML
        DataProcessingList dataProcessingList = unmarshaller.unmarshalFromXpath(MzMLElement.DataProcessingList.getXpath(), MzMLElement.DataProcessingList.getClazz());
        //and persist to database
        //need to update Ref paramGroup first, if present for any
        Iterator it1 = dataProcessingList.getDataProcessing().iterator();
        while (it1.hasNext()) {
            DataProcessing dataProcessing = (DataProcessing) it1.next();
            Iterator it2 = dataProcessing.getProcessingMethod().iterator();
            while (it2.hasNext()) {
                ProcessingMethod processingMethod = (ProcessingMethod) it2.next();
                if (processingMethod.getReferenceableParamGroupRef().size() > 0) {
                    updateParamGroupRef(processingMethod.getReferenceableParamGroupRef());
                }
                if (processingMethod.getCvParam().size() > 0) {
                    updateCVParam(processingMethod.getCvParam());
                }
                //need to replace the Software reference
                if (softwareMap.containsKey(processingMethod.getSoftwareRef())) {
                    Software softwareProxy = (Software) session.load(Software.class, softwareMap.get(processingMethod.getSoftwareRef()));
                    processingMethod.setSoftware(softwareProxy);
                } else {
                    //software should already be in database
                    throw new IllegalStateException("Software Ref not present in database!: " + processingMethod.getSoftwareRef());
                }

            }

        }
        session.persist(dataProcessingList);
        session.flush();
        session.clear();
        //finally update the DataProcessingMap with the id->hid from the database
        Iterator it2 = dataProcessingList.getDataProcessing().iterator();
        while (it2.hasNext()) {
            DataProcessing dataProcessing = (DataProcessing) it2.next();
            dataProcessingMap.put(dataProcessing.getId(), dataProcessing.getHid());
        }
        //return proxy object
        return (DataProcessingList) session.load(DataProcessingList.class, dataProcessingList.getHid());
    }

    //will persist ScanSettingsList, update the ScanSettingsMap with the id->hid, and update refParamGroup and SourceFileMap
    private ScanSettingsList persistScanSettingsList(MzMLUnmarshaller unmarshaller) {
        //extract ScanSettingsList from XML
        ScanSettingsList scanSettingsList = unmarshaller.unmarshalFromXpath(MzMLElement.ScanSettingsList.getXpath(), MzMLElement.ScanSettingsList.getClazz());
        if (scanSettingsList == null) {
            return null;
        }
        //and persist to database
        //need to update Ref paramGroup first, if present for any ScanSettings
        Iterator it1 = scanSettingsList.getScanSettings().iterator();
        while (it1.hasNext()) {
            ScanSettings scanSettings = (ScanSettings) it1.next();
            if (scanSettings.getReferenceableParamGroupRef().size() > 0) {
                updateParamGroupRef(scanSettings.getReferenceableParamGroupRef());
            }
            if (scanSettings.getCvParam().size() > 0) {
                updateCVParam(scanSettings.getCvParam());
            }
            //and update any RefParamGroup in the TargetList
            Iterator it2 = scanSettings.getTargetList().getTarget().iterator();
            while (it2.hasNext()) {
                ParamGroup paramGroup = (ParamGroup) it2.next();
                if (paramGroup.getReferenceableParamGroupRef().size() > 0) {
                    updateParamGroupRef(paramGroup.getReferenceableParamGroupRef());
                }
                if (paramGroup.getCvParam().size() > 0) {
                    updateCVParam(paramGroup.getCvParam());
                }
            }
            //now, need to update SourceFile refs
            if (scanSettings.getSourceFileRefList().getSourceFileRef().size() > 0) {
                updateSourceFileRef(scanSettings.getSourceFileRefList().getSourceFileRef());
            }
        }
        //and persist to database
        session.persist(scanSettingsList);
        //finally update the ScanSettingsMap with the id->hid from the database
        Iterator it2 = scanSettingsList.getScanSettings().iterator();
        while (it2.hasNext()) {
            ScanSettings scanSettings = (ScanSettings) it2.next();
            scanSettingsMap.put(scanSettings.getId(), scanSettings.getHid());
        }
        session.flush();
        session.clear();
        //return proxy object
        return (ScanSettingsList) session.load(ScanSettingsList.class, scanSettingsList.getHid());
    }

    //will persist software List, update the Software map id->hid and update any refParamGroup in the software element
    private SoftwareList persistSoftwareList(MzMLUnmarshaller unmarshaller) {
        //extract SoftwareList from XML
        SoftwareList softwareList = unmarshaller.unmarshalFromXpath(MzMLElement.SoftwareList.getXpath(), MzMLElement.SoftwareList.getClazz());
        //and persist to database
        //need to update Ref paramGroup first, if present for any
        Iterator it1 = softwareList.getSoftware().iterator();
        while (it1.hasNext()) {
            Software software = (Software) it1.next();
            if (software.getReferenceableParamGroupRef().size() > 0) {
                updateParamGroupRef(software.getReferenceableParamGroupRef());
            }
            if (software.getCvParam().size() > 0) {
                updateCVParam(software.getCvParam());
            }
        }
        session.persist(softwareList);
        session.flush();
        session.clear();
        //finally update the SoftwareMap with the id->hid from the database
        Iterator it2 = softwareList.getSoftware().iterator();
        while (it2.hasNext()) {
            Software software = (Software) it2.next();
            softwareMap.put(software.getId(), software.getHid());
        }
        //return proxy object
        return (SoftwareList) session.load(SoftwareList.class, softwareList.getHid());
    }

    //will persist SampleList and store the Sample id->hid relation
    private SampleList persistSampleList(MzMLUnmarshaller unmarshaller) {
        //extract SampleList from XML
        SampleList sampleList = unmarshaller.unmarshalFromXpath(MzMLElement.SampleList.getXpath(), MzMLElement.SampleList.getClazz());
        if (sampleList == null) {
            //might not contain any
            return null;
        }
        Iterator it1 = sampleList.getSample().iterator();
        while (it1.hasNext()) {
            Sample sample = (Sample) it1.next();
            if (sample.getReferenceableParamGroupRef().size() > 0) {
                updateParamGroupRef(sample.getReferenceableParamGroupRef());
            }
            if (sample.getCvParam().size() > 0) {
                updateCVParam(sample.getCvParam());
            }
        }
        //and persist to database
        session.persist(sampleList);
        session.flush();
        session.clear();
        //load will return the object from the cache or proxy object
        Iterator it = sampleList.getSample().iterator();
        while (it.hasNext()) {
            Sample sample = (Sample) it.next();
            sampleMap.put(sample.getId(), sample.getHid());
        }
        //return proxy object
        return (SampleList) session.load(SampleList.class, sampleList.getHid());
    }

    //will persist ReferenceableParamGroupList from XML and
    private ReferenceableParamGroupList persistReferenceableParamGroupList(MzMLUnmarshaller unmarshaller) {
        //extract RefParamGroupList from XML
        ReferenceableParamGroupList referenceableParamGroupList = unmarshaller.unmarshalFromXpath(MzMLElement.ReferenceableParamGroupList.getXpath(), MzMLElement.ReferenceableParamGroupList.getClazz());
        if (referenceableParamGroupList == null) {
            return null;
        }
        //update cvParam
        Iterator it1 = referenceableParamGroupList.getReferenceableParamGroup().iterator();
        while (it1.hasNext()) {
            ReferenceableParamGroup referenceableParamGroup = (ReferenceableParamGroup) it1.next();
            if (referenceableParamGroup.getCvParam().size() > 0) {
                updateCVParam(referenceableParamGroup.getCvParam());
            }
        }
        //and persist to database
        session.persist(referenceableParamGroupList);
        Iterator it = referenceableParamGroupList.getReferenceableParamGroup().iterator();
        while (it.hasNext()) {
            ReferenceableParamGroup referenceableParamGroup = (ReferenceableParamGroup) it.next();
            referenceableParamGroupMap.put(referenceableParamGroup.getId(), referenceableParamGroup.getHid());
        }
        session.flush();
        session.clear();
        //return proxy object
        return (ReferenceableParamGroupList) session.load(ReferenceableParamGroupList.class, referenceableParamGroupList.getHid());
    }

    //will persist FileDescription elements in the XML
    private FileDescription persistFileDescription(MzMLUnmarshaller unmarshaller) {
        //extract FileDescription from XML
        FileDescription fileDescription = unmarshaller.unmarshalFromXpath(MzMLElement.FileDescription.getXpath(), MzMLElement.FileDescription.getClazz());
        //need to update RefParamGroup
        //for FileContent, will update the RefParamGroup with the hid from the database appeared before
        if (fileDescription.getFileContent().getCvParam().size() > 0) {
            updateCVParam(fileDescription.getFileContent().getCvParam());
        }
        if (fileDescription.getFileContent().getReferenceableParamGroupRef().size() > 0) {
            updateParamGroupRef(fileDescription.getFileContent().getReferenceableParamGroupRef());
        }
        if (fileDescription.getContact().size() > 0) {
            Iterator it = fileDescription.getContact().iterator();
            while (it.hasNext()) {
                ParamGroup paramGroup = (ParamGroup) it.next();
                if (paramGroup.getCvParam().size() > 0) {
                    updateCVParam(paramGroup.getCvParam());
                }
                if (paramGroup.getReferenceableParamGroupRef().size() > 0) {
                    updateParamGroupRef(paramGroup.getReferenceableParamGroupRef());
                }
            }
        }
        if (fileDescription.getSourceFileList() != null) {
            Iterator it = fileDescription.getSourceFileList().getSourceFile().iterator();
            while (it.hasNext()) {
                SourceFile sourceFile = (SourceFile) it.next();
                if (sourceFile.getReferenceableParamGroupRef().size() > 0) {
                    updateParamGroupRef(sourceFile.getReferenceableParamGroupRef());
                }
                if (sourceFile.getCvParam().size() > 0) {
                    updateCVParam(sourceFile.getCvParam());
                }
            }
        }
        //and persist to database
        session.persist(fileDescription);
        session.flush();
        session.clear();
        //
        Iterator it = fileDescription.getSourceFileList().getSourceFile().iterator();
        while (it.hasNext()) {
            SourceFile sourceFile = (SourceFile) it.next();
            sourceFileMap.put(sourceFile.getId(), sourceFile.getHid());
        }
        //return proxy object
        return (FileDescription) session.load(FileDescription.class, fileDescription.getHid());
    }

    //for a given list of SourceFileRef and the map with the id->hid mapping, will update with the appropriate value from database
    private void updateSourceFileRef(List<SourceFileRef> sourceFileRefs) {
        Iterator it = sourceFileRefs.iterator();
        while (it.hasNext()) {
            //update it with the SourceFile already in the database
            SourceFileRef sourceFileRef = (SourceFileRef) it.next();
            if (sourceFileMap.containsKey(sourceFileRef.getRef())) {
                SourceFile proxySourceFile = (SourceFile) session.load(SourceFile.class, sourceFileMap.get(sourceFileRef.getRef()));
                sourceFileRef.setSourceFile(proxySourceFile);
//                sourceFileRef.setRef(sourceFileMap.get(sourceFileRef.getRef()).toString());
            } else {
                throw new IllegalStateException("SourceFile Ref not present in database!: " + sourceFileRef.getRef());
            }
        }

    }

    //for a given list of CVParam and the map with the id->hid mapping, will update with the appropriate values from the database
    private void updateCVParam(List<CVParam> cvParams) {
        Iterator it = cvParams.iterator();
        while (it.hasNext()) {
            //update with the cvParam_hid already in the database
            CVParam cvParam = (CVParam) it.next();
            //only persist CVParam that are not referenceable !!
            if (!cvParam.isInferredFromReferenceableParamGroupRef()) {
                if (cvMap.containsKey(cvParam.getCvRef())) {
                    CV cv = (CV) session.load(CV.class, cvMap.get(cvParam.getCvRef()));
                    cvParam.setCv(cv);
                } else {
                    throw new IllegalStateException("CV not present in database!: " + cvParam.getCvRef());
                }
                if (cvParam.getUnitCvRef() != null) {
                    if (cvMap.containsKey(cvParam.getUnitCvRef())) {
                        CV cv = (CV) session.load(CV.class, cvMap.get(cvParam.getUnitCvRef()));
                        cvParam.setUnitCv(cv);
                    } else {
                        throw new IllegalStateException("UnitCV not present in database!: " + cvParam.getUnitCvRef());
                    }
                }
            } else {
                //remove CVParam from the list
                it.remove();
            }
        }

    }

    //for a given list of RefParamGroupRef and the map with the id->hid mapping, will update with the appropriate value from database
    private void updateParamGroupRef(List<ReferenceableParamGroupRef> refParamGroups) {
        Iterator it = refParamGroups.iterator();
        while (it.hasNext()) {
            //update it with the RefParamGroup already in the database
            ReferenceableParamGroupRef refParamGroup = (ReferenceableParamGroupRef) it.next();
            if (referenceableParamGroupMap.containsKey(refParamGroup.getRef())) {
                ReferenceableParamGroup proxyRefParamGroup = (ReferenceableParamGroup) session.load(ReferenceableParamGroup.class, referenceableParamGroupMap.get(refParamGroup.getRef()).longValue());
                refParamGroup.setReferenceableParamGroup(proxyRefParamGroup);
            } else {
                throw new IllegalStateException("RefParamGroup not present in database!: " + refParamGroup.getRef());
            }
        }

    }
    //will persist CVList elements in the XML:

    private CVList persistCVList(MzMLUnmarshaller unmarshaller) {
        //extract CVList from XML
        CVList cvList = unmarshaller.unmarshalFromXpath(MzMLElement.CVList.getXpath(), MzMLElement.CVList.getClazz());
        //and persist to database
        session.persist(cvList);
        //get proxy objects for the CV : load will return the object from the cache or proxy object
        Iterator it = cvList.getCv().iterator();
        while (it.hasNext()) {
            CV cv = (CV) it.next();
            cvMap.put(cv.getId(), cv.getHid());
        }
        session.flush();
        session.clear();
        //return proxy object
        return (CVList) session.load(CVList.class, cvList.getHid());
    }

    //will persist ParamGroup from the XML: Difference between List<ParamGroup> and a ParamGroup
    private ParamGroup persistParamGroup(MzMLUnmarshaller unmarshaller, String xpath) {
        ParamGroup paramGroup = null;
        //unmarshal the part of the XML we want to persist
        MzMLObjectIterator iterator = unmarshaller.unmarshalCollectionFromXpath(xpath, MzMLElement.ParamGroup.getClazz());

        while (iterator.hasNext()) {
            paramGroup = (ParamGroup) iterator.next();
            //persist the object
            session.persist(paramGroup);
        }
        return (ParamGroup) session.load(ParamGroup.class, paramGroup.getHid());
    }

    public void openSessionFactory(SessionFactory factory) {
        //Build session factory
        session = factory.openSession();
    }

    public void closeSessionFactory() {
        session.close();
    }

}
