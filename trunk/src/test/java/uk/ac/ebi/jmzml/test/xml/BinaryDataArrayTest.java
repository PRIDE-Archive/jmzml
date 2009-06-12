package uk.ac.ebi.jmzml.test.xml;

import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshallerException;
import uk.ac.ebi.jmzml.xml.io.MzMLMarshaller;
import uk.ac.ebi.jmzml.model.mzml.*;
import uk.ac.ebi.jmzml.model.mzml.params.BinaryDataArrayCVParam;

import java.net.URL;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;

/**
 * @author Florian Reisinger
 *         Date: 11-Jun-2009
 * @since 0.5
 */
public class BinaryDataArrayTest extends TestCase {

    private final boolean VERBOSE = false;

    private CV cv;
    private CVParam prec64bit;
    private CVParam prec32bit;
    private CVParam compressed;
    private CVParam uncompressed;

    private float[] testData32bit = {
            1.123F, 1.234F, 1.345F, 1.456F, 1.567F, 1.678F, 1.789F, 1.890F,
            2.123F, 2.234F, 2.345F, 2.456F, 2.567F, 2.678F, 2.789F, 2.890F,
            3.123F, 3.234F, 3.345F, 3.456F, 3.567F, 3.678F, 3.789F, 3.890F,
            4.123F, 4.234F, 4.345F, 4.456F, 4.567F, 4.678F, 4.789F, 4.890F,
            5.123F, 5.234F, 5.345F, 5.456F, 5.567F, 5.678F, 5.789F, 5.890F,
            6.123F, 6.234F, 6.345F, 6.456F, 6.567F, 6.678F, 6.789F, 6.890F,
            7.123F, 7.234F, 7.345F, 7.456F, 7.567F, 7.678F, 7.789F, 7.890F,
            8.123F, 8.234F, 8.345F, 8.456F, 8.567F, 8.678F, 8.789F, 8.890F,
            9.123F, 9.234F, 9.345F, 9.456F, 9.567F, 9.678F, 9.789F, 9.890F};

    private double[] testData64Bit = {
            1.123, 1.234, 1.345, 1.456, 1.567, 1.678, 1.789, 1.890,
            2.123, 2.234, 2.345, 2.456, 2.567, 2.678, 2.789, 2.890,
            3.123, 3.234, 3.345, 3.456, 3.567, 3.678, 3.789, 3.890,
            4.123, 4.234, 4.345, 4.456, 4.567, 4.678, 4.789, 4.890,
            5.123, 5.234, 5.345, 5.456, 5.567, 5.678, 5.789, 5.890,
            6.123, 6.234, 6.345, 6.456, 6.567, 6.678, 6.789, 6.890,
            7.123, 7.234, 7.345, 7.456, 7.567, 7.678, 7.789, 7.890,
            8.123, 8.234, 8.345, 8.456, 8.567, 8.678, 8.789, 8.890,
            9.123, 9.234, 9.345, 9.456, 9.567, 9.678, 9.789, 9.890};

    // binary test data: compressed, base64 encoded, 64 bit precision
    // extracted from file: MzMLFile_7_compressed.mzML (line 74) "m/z array"
    private final String c64bit = "eJwtkWlIVFEAhWUQERGREHFDokQUEQmREIlDoE" +
         "gbaUVI9GOQfgwiIiYxmkRZuVRWluWSmrPoLI7jzJvtJSGhIlYQYYJESERIDCYiJ" +
         "ZEleN995/4Z3ptzz/K9mBj9/HdtL5+prYL+a8TujbrXzU9NfG7E9RRv5bfWZr43" +
         "oyWaeyTe08b/b6Ls69me/oZ26u5g5kSioaCzg/oulAaXrs2+vMd7D6Bm392oVh/" +
         "y/mMMLxz0NZl66fMEX/7Ki/TrQ2bRWk7crWf0fY5LV6SS/gMYGpSHOUP4/OHP+e" +
         "/jL5g3jPTYi0mFyghzR1GjC5k/htNHu48b18fYw4Kf1ZrSwj5W3K//8c+1bWUvG" +
         "wo6U0WEjf3seG+pCP/as7PnOLRWabET7DuBhFVb47EEB3s74NZik53s78RJTZbq" +
         "4g4XZO0sN/e4obX7eGiSuyaRf/nR3E6eh/s8eCtUGUVT3DkFU+9mG0q83OuFMBP" +
         "R09w9jQsCjpjK/T7YRbtVm48cfPi9t2KO5vrJw49yCcBPLn6IcVoQ+SjQ7RRyUl" +
         "AsT4C8Arj95p0gFCC3AD6J9aIL+QVxWAqD5BjEVTkgRJ4hzO/kicUhcg3jgG5Iv" +
         "mFIt6UwOUegjGw5Fk9FyDsCgwyOkLuKczoY8ldh1WJXVH6HV9gH57SBqA==";

    // binary test data: uncompressed, base64 encoded, 64 bit precision
    // extracted from file: MzMLFile_7_uncompressed.mzML (line 74) "m/z array"
    private final String u64bit = "AAAAAAAAAAD8qfHSTWJQP/yp8dJNYmA/+n5qvH" +
         "STaD/8qfHSTWJwP3sUrkfhenQ/+n5qvHSTeD956SYxCKx8P/yp8dJNYoA/O99Pj" +
         "Zdugj97FK5H4XqEP7pJDAIrh4Y/+n5qvHSTiD85tMh2vp+KP3npJjEIrIw/uB6F" +
         "61G4jj/8qfHSTWKQP5zEILByaJE/O99PjZdukj/b+X5qvHSTP3sUrkfhepQ/Gy/" +
         "dJAaBlT+6SQwCK4eWP1pkO99PjZc/+n5qvHSTmD+amZmZmZmZPzm0yHa+n5o/2c" +
         "73U+Olmz956SYxCKycPxkEVg4tsp0/uB6F61G4nj9YObTIdr6fP/yp8dJNYqA/T" +
         "DeJQWDloD+cxCCwcmihP+xRuB6F66E/O99PjZduoj+LbOf7qfGiP9v5fmq8dKM/" +
         "K4cW2c73oz97FK5H4XqkP8uhRbbz/aQ/Gy/dJAaBpT9qvHSTGASmP7pJDAIrh6Y" +
         "/CtejcD0Kpz9aZDvfT42nP6rx0k1iEKg/+n5qvHSTqD9KDAIrhxapP5qZmZmZma" +
         "k/6SYxCKwcqj85tMh2vp+qP4lBYOXQIqs/2c73U+Olqz8pXI/C9SisP3npJjEIr" +
         "Kw/yXa+nxovrT8ZBFYOLbKtP2iR7Xw/Na4/uB6F61G4rj8IrBxaZDuvP1g5tMh2" +
         "vq8/VOOlm8QgsD/8qfHSTWKwP6RwPQrXo7A/TDeJQWDlsD/0/dR46SaxP5zEILB" +
         "yaLE/RIts5/upsT/sUbgeheuxP5MYBFYOLbI/O99PjZdusj/jpZvEILCyP4ts5/" +
         "up8bI/MzMzMzMzsz/b+X5qvHSzP4PAyqFFtrM/K4cW2c73sz/TTWIQWDm0P3sUr" +
         "kfherQ/I9v5fmq8tD/LoUW28/20P3Noke18P7U/Gy/dJAaBtT/D9Shcj8K1P2q8" +
         "dJMYBLY/EoPAyqFFtj+6SQwCK4e2P2IQWDm0yLY/CtejcD0Ktz+yne+nxku3P1p" +
         "kO99Pjbc/AiuHFtnOtz+q8dJNYhC4P1K4HoXrUbg/+n5qvHSTuD+iRbbz/dS4P0" +
         "oMAiuHFrk/";

    // binary test data: compressed, base64 encoded, 32 bit precision
    // extracted from file: MzMLFile_7_compressed.mzML (line 80) "intensity array"
    private final String c32bit = "eJwVxCFIQ2EAhdE/GAyGhQXDwoLBYFgwGAa+jQ" +
         "WDYcFgMCwYDIYFw4LhITLGGGOIyBAZDxkyhsgQkSFDHrJgNC4uGo1Gj5fv3BD+F" +
         "++6SMQkpCwJpRAy5CkQUaVGnZgWPfokjJgwJeWTLxYs+eaHX0I5hBVWWSNDlnVy" +
         "5Nlgky0KbLNDkYgKe+xT5YBDjqhxzAmn1DmjwTkxF1zSpEWbDl16XHHNDX1uuWN" +
         "Awj1DHhgx5pEnJjzzwitT3pjxTsoH8/IfQP5IBA==";

    // binary test data: uncompressed, base64 encoded, 32 bit precision
    // extracted from file: MzMLFile_7_uncompressed.mzML (line 80) "intensity array"
    private final String u32bit = "AAAAAAAAgD8AAABAAABAQAAAgEAAAKBAAADAQA" +
         "AA4EAAAABBAAAQQQAAIEEAADBBAABAQQAAUEEAAGBBAABwQQAAgEEAAIhBAACQQ" +
         "QAAmEEAAKBBAACoQQAAsEEAALhBAADAQQAAyEEAANBBAADYQQAA4EEAAOhBAADw" +
         "QQAA+EEAAABCAAAEQgAACEIAAAxCAAAQQgAAFEIAABhCAAAcQgAAIEIAACRCAAA" +
         "oQgAALEIAADBCAAA0QgAAOEIAADxCAABAQgAAREIAAEhCAABMQgAAUEIAAFRCAA" +
         "BYQgAAXEIAAGBCAABkQgAAaEIAAGxCAABwQgAAdEIAAHhCAAB8QgAAgEIAAIJCA" +
         "ACEQgAAhkIAAIhCAACKQgAAjEIAAI5CAACQQgAAkkIAAJRCAACWQgAAmEIAAJpC" +
         "AACcQgAAnkIAAKBCAACiQgAApEIAAKZCAACoQgAAqkIAAKxCAACuQgAAsEIAALJ" +
         "CAAC0QgAAtkIAALhCAAC6QgAAvEIAAL5CAADAQgAAwkIAAMRC";

    public void setUp() {
        // define the reference CV to use for the CVParams
        cv = new CV();
        cv.setId("MS");
        cv.setFullName("Proteomics Standards Initiative Mass Spectrometry Ontology");
        cv.setVersion("2.1.0");
        cv.setURI("http://psidev.cvs.sourceforge.net/*checkout*/psidev/psi/psi-ms/mzML/controlledVocabulary/psi-ms.obo");


        // define a CVParam for 64 bit precision
        prec64bit = new BinaryDataArrayCVParam();
        prec64bit.setAccession(BinaryDataArray.MS_64BIT_AC);
        prec64bit.setName(BinaryDataArray.MS_64BIT_NAME);
        prec64bit.setCV(cv);

        // define a CVParam for 32 bit precision
        prec32bit = new BinaryDataArrayCVParam();
        prec32bit.setAccession(BinaryDataArray.MS_32BIT_AC);
        prec32bit.setName(BinaryDataArray.MS_32BIT_NAME);
        prec32bit.setCV(cv);

        // define a CVParam for compression
        compressed = new BinaryDataArrayCVParam();
        compressed.setAccession(BinaryDataArray.MS_COMPRESSED_AC);
        compressed.setName(BinaryDataArray.MS_COMPRESSED_NAME);
        compressed.setCV(cv);

        // define a CVParam for no compression
        uncompressed = new BinaryDataArrayCVParam();
        uncompressed.setAccession(BinaryDataArray.MS_UNCOMPRESSED_AC);
        uncompressed.setName(BinaryDataArray.MS_UNCOMPRESSED_NAME);
        uncompressed.setCV(cv);


    }

    public void testUnmarshallingUncompressed() throws MzMLUnmarshallerException, UnsupportedEncodingException {
        // find the test XML file
        URL url = this.getClass().getClassLoader().getResource("MzMLFile_7_uncompressed.mzML");
        assertNotNull(url);

        // create the marshaller and check that the file is indexed
        MzMLUnmarshaller um = new MzMLUnmarshaller(url);
        assertTrue( um.isIndexedmzML() );

        // the checksum is not correct, so we skip this test
        // assertTrue(um.isOkFileChecksum());

        MzML content = um.unmarshall();
        assertNotNull(content);

        // fine the spectrum with id "index=0"
        List<Spectrum> spectrumList = content.getRun().getSpectrumList().getSpectrum();
        Spectrum spectrum = null;
        for (Spectrum sp : spectrumList) {
            assertNotNull(sp);
            if ( sp.getId() != null && sp.getId().equalsIgnoreCase("index=0") ) {
                spectrum = sp;
            }
        }
        assertNotNull(spectrum);

        // check that we have binary data
        BinaryDataArrayList bdal = spectrum.getBinaryDataArrayList();
        assertNotNull(bdal);
        List<BinaryDataArray> dataArray = bdal.getBinaryDataArray();
        BinaryDataArray data = dataArray.iterator().next(); // get the first BinaryDataArray
        assertNotNull(data);

        // check that the base64 encoded binary string we get from the object is the
        // same as the string we copied from the file into the "u64bit" variable.
        String binaryString = new String(data.getBinary(), "ASCII");
        assertTrue(binaryString.equals(u64bit));

        if (VERBOSE) {
            // print to see what the data looks like
            System.out.println("\n----- testUnmarshallingUncompressed --------------------------");
            System.out.println("binary data (text):  " + new String(data.getBinary(), "ASCII") );
            System.out.println("");
            System.out.println("binary data (default):  " + new String(data.getBinary()) );
            System.out.println("");
            System.out.println("Data as double array:");
            for (double value : data.getBinaryDataAsDoubleArray()) {
                System.out.println("double value: " + value);
            }
            System.out.println("end of double array.");
            System.out.println("----- testUnmarshalling Uncompressed end ---------------------");
        }

    }

    public void testUnmarshallingCompressed() throws MzMLUnmarshallerException, UnsupportedEncodingException {
        // find the test XML file
        URL url = this.getClass().getClassLoader().getResource("MzMLFile_7_compressed.mzML");
        assertNotNull(url);

        // create the marshaller and check that the file is indexed
        MzMLUnmarshaller um = new MzMLUnmarshaller(url);
        assertTrue( um.isIndexedmzML() );

        // the checksum is not correct, so we skip this test
        // assertTrue(um.isOkFileChecksum());

        MzML content = um.unmarshall();
        assertNotNull(content);

        // fine the spectrum with id "index=0"
        List<Spectrum> spectrumList = content.getRun().getSpectrumList().getSpectrum();
        Spectrum spectrum = null;
        for (Spectrum sp : spectrumList) {
            assertNotNull(sp);
            if ( sp.getId() != null && sp.getId().equalsIgnoreCase("index=0") ) {
                spectrum = sp;
            }
        }
        assertNotNull(spectrum);

        // check that we have binary data
        BinaryDataArrayList bdal = spectrum.getBinaryDataArrayList();
        assertNotNull(bdal);
        List<BinaryDataArray> dataArray = bdal.getBinaryDataArray();
        BinaryDataArray data = dataArray.iterator().next(); // get the first BinaryDataArray
        assertNotNull(data);

        // check that the base64 encoded binary string we get from the object is the
        // same as the string we copied from the file into the "u64bit" variable.
        String binaryString = new String(data.getBinary(), "ASCII");
        assertTrue(binaryString.equals(c64bit));

        if (VERBOSE) {
            // print to see what the data looks like
            System.out.println("\n----- testUnmarshallingCompressed ----------------------------");
            System.out.println("binary data (text):  " + new String(data.getBinary(), "ASCII") );
            System.out.println("");
            System.out.println("binary data (default):  " + new String(data.getBinary()) );
            System.out.println("");
            System.out.println("Data as double array:");
            for (double value : data.getBinaryDataAsDoubleArray()) {
                System.out.println("double value: " + value);
            }
            System.out.println("end of double array.");
            System.out.println("----- testUnmarshalling Compressed end -----------------------");
        }

    }


    public void test64BitSetGetNoCompression() throws UnsupportedEncodingException {

        // create some test data with 64 bit precision (double)
        double[] testData = testData64Bit;

        // create the BinaryDataArray object that will store the data and do all the conversions
        BinaryDataArray bda = new BinaryDataArray();
        bda.set64BitArrayAsBinaryData(testData, false, cv); // store data, not compressing

        if (VERBOSE) {
            System.out.println("\n----- test64BitSetGetNoCompression ---------------------------");

            // print the binary data (for reference)
            String data = new String(bda.getBinary(), "ASCII");
            System.out.println("data (base64 encoded): " + data);
        }

        // retrieve the data and check if the values are still the same
        double[] retrievedData = bda.getBinaryDataAsDoubleArray();
        int i = 0;
        for (double v : retrievedData) {
            // the retrieved value has to be the same as the original value
            if (VERBOSE) { System.out.println("double value: " + v); }
            assertTrue(v == testData[i++]);
        }
        if (VERBOSE) { System.out.println("----- test64BitSetGetNoCompression end------------------------"); }

    }

    public void test64BitSetGetCompressed() throws UnsupportedEncodingException {

        // create some test data with 64 bit precision (double)
        double[] testData = testData64Bit;

        // create the BinaryDataArray object that will store the data and do all the conversions
        BinaryDataArray bda = new BinaryDataArray();
        bda.set64BitArrayAsBinaryData(testData, true, cv); // store data, compressed

        // retrieve the data and check if the values are still the same
        double[] retrievedData = bda.getBinaryDataAsDoubleArray();
        int i = 0;
        for (double v : retrievedData) {
            // the retrieved value has to be the same as the original value
            assertTrue(v == testData[i++]);
        }
    }

    public void test32BitSetGetNoCompression() throws UnsupportedEncodingException {

        // create some test data with 32 bit precision (float)
        float[] testData = testData32bit;

        // create the BinaryDataArray object that will store the data and do all the conversions
        BinaryDataArray bda = new BinaryDataArray();
        bda.set32BitArrayAsBinaryData(testData, false, cv);

        // retrieve the data and check if the values are still the same
        double[] retrievedData = bda.getBinaryDataAsDoubleArray();
        int i = 0;
        for (double v : retrievedData) {
            // the retrieved value has to be the same as the original value
            assertTrue(testData[i++] == (float)v); // cast, since we have 32 bit precision (float)
        }

    }

    public void test32BitSetGetCompressed() throws UnsupportedEncodingException {

        // create some test data with 32 bit precision (float)
        float[] testData = testData32bit;

        // create the BinaryDataArray object that will store the data and do all the conversions
        BinaryDataArray bda = new BinaryDataArray();
        bda.set32BitArrayAsBinaryData(testData, true, cv);

        // retrieve the data and check if the values are still the same
        double[] retrievedData = bda.getBinaryDataAsDoubleArray();
        int i = 0;
        for (double v : retrievedData) {
            // the retrieved value has to be the same as the original value
            assertTrue(testData[i++] == (float)v); // cast, since we have 32 bit precision (float)
        }

    }


    public void testDataFromFile() throws UnsupportedEncodingException {
        double[] one = createBDAFromC64Bit().getBinaryDataAsDoubleArray(); // 64 bit, compressed
        double[] two = createBDAFromU64Bit().getBinaryDataAsDoubleArray(); // 64 bit, uncompressed
        double[] three = createBDAFromC32Bit().getBinaryDataAsDoubleArray(); // 32 bit, compressed
        double[] four = createBDAFromU32Bit().getBinaryDataAsDoubleArray();  // 32 bit, uncompressed
        assertTrue(one.length == two.length);
        assertTrue(three.length == four.length);
        assertTrue(one.length == four.length);

        // no matter the processing, the information should stay the same
        for (int i = 0; i < two.length; i++) {
            assertTrue(one[i] == two[i]); // 64 bit (double), "m/z array"
            assertTrue((float)three[i] == (float)four[i]); // 32 bit (float), "intensity array"

            // unfortunatley the values for "m/z array" and "intensity array" are not the same
            // assertTrue((float)one[i] == (float)four[i]);
        }
    }


    public void testMarshaller() throws UnsupportedEncodingException {

        BinaryDataArray bda = createBDAFromC64Bit(); // compressed, 64 bit precision

        MzMLMarshaller m = new MzMLMarshaller();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        m.marshall(bda, baos);

        String c64bitTest = new String(baos.toByteArray());

        if (VERBOSE) {
            System.out.println("\n----- testMarshaller -----------------------------------------");
            m.marshall(bda, System.out);
            System.out.println("\n");
            System.out.println(c64bitTest);
            System.out.println("----- testMarshaller end -------------------------------------");
        }

        // the BinaryDataArray was created from the String in "c64bit"
        // therefore the marshalled output should contain the same string
        // (in its binary XML tag)
        assertTrue(c64bitTest.contains(c64bit));

    }


    private BinaryDataArray createBDAFromC64Bit() throws UnsupportedEncodingException {

        // manually construct a BinaryDataArray with the according CVParams
        BinaryDataArray bda = new BinaryDataArray();
        // set compressed, 64 bit precision data
        bda.setBinary(c64bit.getBytes("ASCII"));
        // set CVParam for 64 bit precision
        bda.getCvParam().add(prec64bit);
        // set CVParam for compressed data
        bda.getCvParam().add(compressed);

        return bda;
    }

    private BinaryDataArray createBDAFromU64Bit() throws UnsupportedEncodingException {

        // manually construct a BinaryDataArray with the according CVParams
        BinaryDataArray bda = new BinaryDataArray();
        // set uncompressed, 64 bit precision data
        bda.setBinary(u64bit.getBytes("ASCII"));
        // set CVParam for 64 bit precision
        bda.getCvParam().add(prec64bit);
        // set CVParam for not compressed data
        bda.getCvParam().add(uncompressed);

        return bda;
    }

    private BinaryDataArray createBDAFromC32Bit() throws UnsupportedEncodingException {

        // manually construct a BinaryDataArray with the according CVParams
        BinaryDataArray bda = new BinaryDataArray();
        // set uncompressed, 32 bit precision data
        bda.setBinary(c32bit.getBytes("ASCII"));
        // set CVParam for 32 bit precision
        bda.getCvParam().add(prec32bit);
        // set CVParam for compressed data
        bda.getCvParam().add(compressed);

        return bda;
    }

    private BinaryDataArray createBDAFromU32Bit() throws UnsupportedEncodingException {

        // manually construct a BinaryDataArray with the according CVParams
        BinaryDataArray bda = new BinaryDataArray();
        // set uncompressed, 32 bit precision data
        bda.setBinary(u32bit.getBytes("ASCII"));
        // set CVParam for 32 bit precision
        bda.getCvParam().add(prec32bit);
        // set CVParam for not compressed data
        bda.getCvParam().add(uncompressed);

        return bda;
    }


}
