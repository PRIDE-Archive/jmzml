
package uk.ac.ebi.jmzml.model.mzml;

import org.apache.commons.codec.binary.Base64;
import uk.ac.ebi.jmzml.model.mzml.params.BinaryDataArrayCVParam;
import uk.ac.ebi.jmzml.xml.jaxb.adapters.DataProcessingAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


/**
 * The structure into which encoded binary data goes. Byte ordering is always little endian (Intel style). Computers using a different endian style must convert to/from little endian when writing/reading mzML
 * 
 * <p>Java class for BinaryDataArrayType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BinaryDataArrayType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://psi.hupo.org/ms/mzml}ParamGroupType">
 *       &lt;sequence>
 *         &lt;element name="binary" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *       &lt;attribute name="arrayLength" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="dataProcessingRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="encodedLength" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BinaryDataArrayType", propOrder = {
    "binary"
})
public class BinaryDataArray
    extends ParamGroup
    implements Serializable
{

    /**
     * Defines the number of bytes required in an UNENCODED byte array to hold
     * a single double value.
     */
    public static final int BYTES_TO_HOLD_DOUBLE = 8;

    /**
     * Defines the number of bytes required in an UNENCODED byte array to hold
     * a single float value.
     */
    public static final int BYTES_TO_HOLD_FLOAT = 4;

    public static final String MS_COMPRESSED_AC     = "MS:1000574";
    public static final String MS_COMPRESSED_NAME   = "zlib compression";
    public static final String MS_UNCOMPRESSED_AC   = "MS:1000576";
    public static final String MS_UNCOMPRESSED_NAME = "no compression";
    public static final String MS_32BIT_AC   = "MS:1000521";
    public static final String MS_32BIT_NAME = "32-bit float";
    public static final String MS_64BIT_AC   = "MS:1000523";
    public static final String MS_64BIT_NAME = "64-bit float";

    public enum Precision {FLOAT32BIT, FLOAT64BIT}

    private final static long serialVersionUID = 100L;
    // @XmlElement(required = true)
    // protected byte[] binary;
    @XmlElement(required = true)
    @XmlMimeType("text/plain")
    @XmlSchemaType(name = "base64Binary")
    protected String binary;
    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger arrayLength;
    @XmlAttribute(name = "dataProcessingRef")
    @XmlJavaTypeAdapter(DataProcessingAdapter.class)
    @XmlSchemaType(name = "IDREF")
    protected DataProcessing dataProcessing;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger encodedLength;

    /**
     * Low level method to retrieve the binary data.
     *
     * Note that the data returned will be unmodified, e.g.
     * will not be base64 decoded and therefore (according
     * to the mzML specifications) will remain base64 encoded.
     * 
     * @return the byte[] containing the (base64 encoded) binary (spectrum) data.
     */
    public byte[] getBinary() {
        return binary.getBytes();
    }

    /**
     * Allows access to the binary data as double array.
     * Note: this will implicitly read the CVParams which define the
     * precision (32/64 bit) and if compression has been applied.
     *
     * @return the binary data as double array.
     */
    public double[] getBinaryDataAsDoubleArray() {

        // 1. decode the base64 encoded data (data is assumed to always be base64 encoded)
        byte[] decodedData = decodeBase64(binary.getBytes());
        if (decodedData == null) {
            throw new IllegalStateException("Decoding of binary data produced no data (null)!");
        }

        // 2. Decompression of the data (if required)
        byte[] data;
        if (needsUncompressing()) {
            data = decompress(decodedData);
        } else {
            data = decodedData;
        }

        // 3. apply the specified precision (32 or 64 bit) when converting into double values
        double[] dataArray;
        if (getPrecision() == Precision.FLOAT64BIT) {
            dataArray = convert64bit(data);
        } else if (getPrecision() == Precision.FLOAT32BIT){
            dataArray = convert32bit(data);
        } else {
            throw new IllegalStateException("Not supported precision in BinaryDataArray!");
        }

        return dataArray;
    }

    private double[] convert32bit(byte[] data) {
        return convertData(data, BYTES_TO_HOLD_FLOAT);
    }

    private double[] convert64bit(byte[] data) {
        return convertData(data, BYTES_TO_HOLD_DOUBLE);
    }

    private double[] convertData(byte[] data, int precision) {
        // create a double array of sufficient size
        double[] doubleArray = new double[data.length / precision];
        // create a buffer around the data array for easier retrieval
        ByteBuffer bb = ByteBuffer.wrap(data);
        bb.order(ByteOrder.LITTLE_ENDIAN); // the order is always LITTLE_ENDIAN
        // now read 4/8 bit at a time and create a double from them
        for (int indexOut = 0; indexOut < data.length; indexOut += precision) {
            // Note that the 'getFloat(index)' method gets the next 4 bytes and
            // the 'getDouble(index)' method gets the next 8 bytes.
            doubleArray[indexOut / precision] = (precision == BYTES_TO_HOLD_FLOAT) ? (double) bb.getFloat(indexOut)
                    : bb.getDouble(indexOut);
        }
        return doubleArray;
    }

    private byte[] decompress(byte[] compressedData) {
        byte[] decompressedData;

        /* disadvantage that size of array has to be specified
        Inflater decompresser = new Inflater();
        decompresser.setInput(compressedData, 0, compressedData.length);
        byte[] temp = new byte[compressedData.length * 10]; // assume 10x compression ToDo: which size is enough?
        int rl = 0;
        try {
            rl = decompresser.inflate(temp);
        } catch (DataFormatException e) {
            e.printStackTrace();  // ToDo: handle
        }
        decompresser.end();
        // reduce the array size to the actual used size
        decompressedData = new byte[rl];
        System.arraycopy(temp, 0, decompressedData, 0, rl);
          */

        // alternative using a ByteArrayOutputStream
        Inflater decompressor = new Inflater();
        decompressor.setInput(compressedData);
        // Create an expandable byte array to hold the decompressed data
        ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedData.length);
        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            try {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            } catch (DataFormatException e) {
                throw new IllegalStateException("Encountered wrong data format " +
                        "while trying to decompress binary data!", e);
            }
        }
        try {
            bos.close();
        } catch (IOException e) {
            // ToDo: add logging
            e.printStackTrace();
        }
        // Get the decompressed data
        decompressedData = bos.toByteArray();

        if (decompressedData == null) {
            throw new IllegalStateException("Decompression of binary data prodeuced no result (null)!");
        }
        return decompressedData;
    }

    private byte[] compress(byte[] uncompressedData) {
        byte[] data;// Decompress the data

        // create a temporary byte array big enough to hold the compressed data
        // with the worst compression (the length of the initial (uncompressed) data)
        byte[] temp = new byte[uncompressedData.length];
        // compress
        Deflater compresser = new Deflater();
        compresser.setInput(uncompressedData);
        compresser.finish();
        int cdl = compresser.deflate(temp);
        // create a new array with the size of the compressed data (cdl)
        data = new byte[cdl];
        System.arraycopy(temp, 0, data, 0, cdl);

        /*
        Deflater compressor = new Deflater();
        compressor.setInput(uncompressedData);
        // Create an expandable byte array to hold the decompressed data
        ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressedData.length);
        byte[] buf = new byte[1024];
        while (!compressor.finished()) {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (IOException e) {
            // ToDo: add logging
            e.printStackTrace();
        }
        // Get the decompressed data
        data = bos.toByteArray();
        */

        return data;
    }

    private byte[] decodeBase64(byte[] encodedData) {
        // the following does not seem to work properly
//        if ( !Base64.isArrayByteBase64(encodedData) ) {
//            throw new IllegalArgumentException("Binary data does not seem to be base64 encoded!");
//        }
        return Base64.decodeBase64(encodedData);
    }

    private byte[] encodeBase64(byte[] data){
        return Base64.encodeBase64(data);
    }

    private byte[] decodeBase64alt(byte[] encodedData) {
        return com.sun.org.apache.xerces.internal.impl.dv.util.Base64.decode(new String(encodedData));
    }

    private byte[] encodeBase64alt(byte[] data){
        String base64String = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(data);
        return base64String.getBytes();
    }

    private Precision getPrecision() {
        Precision p;

        // first get all registered CV parameters
        List<String> cvs2 = new ArrayList<String>();
        for (CVParam param : this.getCvParam()) {
            cvs2.add(param.getAccession());
        }
        // then check if we have 64 or 32 bit precision
        if ( cvs2.contains(MS_64BIT_AC) ) {
            if ( cvs2.contains(MS_32BIT_AC) ) {
                throw new IllegalStateException("Found conflicting CV parameters for BinaryDataArray: '"
                        + MS_64BIT_NAME + "' AND '" + MS_32BIT_NAME + "'!");
            }
            p = Precision.FLOAT64BIT;
        } else if ( cvs2.contains(MS_32BIT_AC) ) {
            p = Precision.FLOAT32BIT;
        } else {
            throw new IllegalStateException("Required precision CV parameter ('" + MS_64BIT_NAME
                    + "' or '" + MS_32BIT_NAME + "') not found in BinaryDataArray!");
        }

        return p;
    }

    private boolean needsUncompressing() {
        boolean uncompress;

        // first get all registered CV parameters
        List<String> cvs = new ArrayList<String>();
        for (CVParam param : this.getCvParam()) {
            cvs.add(param.getAccession());
        }

        // now check if we have compressed or uncompressed data
        if (cvs.contains(MS_COMPRESSED_AC)) {
            if (cvs.contains(MS_UNCOMPRESSED_AC)) {
                throw new IllegalStateException("Found conflicting CV parameters for BinaryDataArray: '"
                        + MS_COMPRESSED_NAME + "' AND '" + MS_UNCOMPRESSED_NAME + "'!");
            }
            uncompress = true;
        } else if (cvs.contains(MS_UNCOMPRESSED_AC)) {
            // no de-compressing needed
            uncompress = false;
        } else {
            throw new IllegalStateException("Required compression CV parameter ('" + MS_COMPRESSED_NAME
                    + "' or '" + MS_UNCOMPRESSED_NAME + "') not found in BinaryDataArray!");
        }

        return uncompress;
    }

    /**
     * Low level method to set the binary data.
     *
     * Note that the binary data is expected to already be base64 encoded.
     * For non base64 encoded binary data, please refer to one of the
     * other methods in this class.
     * @see #set32BitArrayAsBinaryData(float[], boolean, CV)
     * @see #set64BitArrayAsBinaryData(double[], boolean, CV)
     *
     * Also note that this method does not add any CVParams for compression
     * or precision. These will have to be added manually.
     * 
     * @param value the byte[] representing the binary (spectrum) data.
     */
    public void setBinary(byte[] value) {
        try {
            this.binary = new String(value, "ASCII");
        } catch (UnsupportedEncodingException e) {
            // ToDo: throw IllegalState
            e.printStackTrace();
        }
    }

    /**
     * Sets the value of the binary property for data in double values.
     * Additionally, according CVParams 
     * Note that double values imply a precision of 64 bit.
     *
     * @param value the data as double array.
     * @param compress flag whether or not the data should be compressed.
     * @param cv The CV that will be used as reference CV for the generated
     *           compression and precision CVParams.
     */
    public void set64BitArrayAsBinaryData(double[] value, boolean compress, CV cv) {
        ByteBuffer buffer = ByteBuffer.allocate(value.length * BYTES_TO_HOLD_DOUBLE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (double aDoubleArray : value) {
            buffer.putDouble(aDoubleArray);
        }

        setBinaryData(buffer.array(), compress, cv);

        // add a cv parameter stating that the data uses 64 bit float (double) precision
        CVParam cvParam = new BinaryDataArrayCVParam();
        cvParam.setAccession(MS_64BIT_AC);
        cvParam.setName(MS_64BIT_NAME);
        cvParam.setCV(cv);
        this.getCvParam().add(cvParam);

    }

    /**
     * Sets the value of the binary property for data in double values.
     * Note that float values imply a precision of 32 bit.
     *
     * @param value the data as float array.
     * @param compress flag whether or not the data should be compressed.
     * @param cv The CV that will be used as reference CV for the generated
     *           compression and precision CVParams.
     */
    public void set32BitArrayAsBinaryData(float[] value, boolean compress, CV cv) {
        ByteBuffer buffer = ByteBuffer.allocate(value.length * BYTES_TO_HOLD_FLOAT);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (float aFloatArray : value) {
            buffer.putFloat(aFloatArray);
        }

        setBinaryData(buffer.array(), compress, cv);

        // add a cv parameter stating that the data uses 32-bit float precision
        CVParam cvParam = new BinaryDataArrayCVParam();
        cvParam.setAccession(MS_32BIT_AC);
        cvParam.setName(MS_32BIT_NAME);
        cvParam.setCV(cv);
        this.getCvParam().add(cvParam);

    }

    private void setBinaryData(byte[] input, boolean compress, CV cv) {
        byte[] output;
        if (compress) {
            // data needs compressing
            output = compress(input);
            // add a cv parameter stating that the data was compressed
            CVParam cvParam = new BinaryDataArrayCVParam();
            cvParam.setAccession(MS_COMPRESSED_AC);
            cvParam.setName(MS_COMPRESSED_NAME);
            cvParam.setCV(cv);
            this.getCvParam().add(cvParam);
        } else {
            // the data will not be compressed
            output = input;
            // add a cv parameter stating that the data was not compressed
            CVParam cvParam = new BinaryDataArrayCVParam();
            cvParam.setAccession(MS_UNCOMPRESSED_AC);
            cvParam.setName(MS_UNCOMPRESSED_NAME);
            cvParam.setCV(cv);
            this.getCvParam().add(cvParam);
        }

        // now encode base64 and store
        setBinary(encodeBase64(output));
    }

    /**
     * Gets the value of the arrayLength property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getArrayLength() {
        return arrayLength;
    }

    /**
     * Sets the value of the arrayLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setArrayLength(BigInteger value) {
        this.arrayLength = value;
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

    /**
     * Gets the value of the encodedLength property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEncodedLength() {
        return encodedLength;
    }

    /**
     * Sets the value of the encodedLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEncodedLength(BigInteger value) {
        this.encodedLength = value;
    }

}
