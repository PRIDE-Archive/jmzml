package uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache;

import uk.ac.ebi.jmzml.model.mzml.*;
import uk.ac.ebi.jmzml.xml.Constants;

import java.util.HashMap;

/**
 * User: rcote
 * Date: 17-Jun-2008
 * Time: 13:41:34
 * $Id: $
 */
public class AdapterObjectCache {

    private HashMap<String, CV> cvMap = new HashMap<String, CV>();
    private HashMap<String, DataProcessing> dataProcessingMap = new HashMap<String, DataProcessing>();
    private HashMap<String, InstrumentConfiguration> instrConfigMap = new HashMap<String, InstrumentConfiguration>();
    private HashMap<String, ReferenceableParamGroup> refParamGroupMap = new HashMap<String, ReferenceableParamGroup>();
    private HashMap<String, Sample> sampleMap = new HashMap<String, Sample>();
    private HashMap<String, Software> softwareMap = new HashMap<String, Software>();
    private HashMap<String, SourceFile> sourceFileMap = new HashMap<String, SourceFile>();
    private HashMap<String, Spectrum> spectrumMap = new HashMap<String, Spectrum>();

    public <MzMLObject> MzMLObject getCachedObject(String ID, Constants.ReferencedType type) {
        switch (type) {
            case CV:
                return (MzMLObject) cvMap.get(ID);
            case DataProcessing:
                return (MzMLObject) dataProcessingMap.get(ID);
            case InstrumentConfiguration:
                return (MzMLObject) instrConfigMap.get(ID);
            case ReferenceableParamGroup:
                return (MzMLObject) refParamGroupMap.get(ID);
            case Sample:
                return (MzMLObject) sampleMap.get(ID);
            case Software:
                return (MzMLObject) softwareMap.get(ID);
            case SourceFile:
                return (MzMLObject) sourceFileMap.get(ID);
            case Spectrum:
                return (MzMLObject) spectrumMap.get(ID);
            default:
                throw new IllegalStateException("Unkonwn cache type: " + type);
        }
    }

    public void setCachedObject(String ID, Constants.ReferencedType type, Object cacheObject) {
        switch (type) {
            case CV:
                cvMap.put(ID, (CV) cacheObject);
                break;
            case DataProcessing:
                dataProcessingMap.put(ID, (DataProcessing) cacheObject);
                break;
            case InstrumentConfiguration:
                instrConfigMap.put(ID, (InstrumentConfiguration) cacheObject);
                break;
            case ReferenceableParamGroup:
                refParamGroupMap.put(ID, (ReferenceableParamGroup) cacheObject);
                break;
            case Sample:
                sampleMap.put(ID, (Sample) cacheObject);
                break;
            case Software:
                softwareMap.put(ID, (Software) cacheObject);
                break;
            case SourceFile:
                sourceFileMap.put(ID, (SourceFile) cacheObject);
                break;
            case Spectrum:
                spectrumMap.put(ID, (Spectrum) cacheObject);
                break;
            default:
                throw new IllegalStateException("Unkonwn cache type: " + type);
        }
    }


}