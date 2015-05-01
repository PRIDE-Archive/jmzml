# MSNumpress #

**MSNumpress** is a set of compression algorithms for numerical data, and can be applied with mzML to mass spectra and chromatograms. Each of the algorithms is appropriate for a particular type of data. However all of the compression algorithms can be considered 'lossy' or 'near-lossless'

## Linear prediction compression (numLin) ##
This type of compression is appropriate for any MS data type, i.e. m/z values, intensity values, time values. It tales advantage of the linearly increasing values of these data types.

## Short logged float compression (numSlof) ##
This type of compression is less appropriate than numLin for all data types. It should NOT be used for m/z values or time values. Unlike m/z values or time values which increase linearly, intensity values are sparse and unordered, but require less precision because of the low instrument precision; this compression algorithm is optimised for intensity data.

## Positive integer compression (numPic) ##
This type of compression is less appropriate than both numLin and numSlof for all data types. It should NOT be used with m/z values or time values, but may be used in most cases where numSlof would otherwise be used.

# Writing data with MSNumpress compression #

It is the data within a `BinaryDataArray` that is compressible. `BinaryDataArray` is used in spectra and chromatograms, and here we will cover writing MSNumpress compressed data in both.

Before the MSNumpress implementation was developed in jmzML, data was compressible with zlib like so:

```
// Create the BinaryDataArray.
BinaryDataArray array = new BinaryDataArray();
boolean doZlibCompression = true;
CV cv = new CV("MS");
array.set64BitIntArrayAsBinaryData(mzValues, doZlibCompression, cv);
```

The only change has been that now there are an additional set of methods with an extra parameter at the end of the current parameters:

```
BinaryDataArray array = new BinaryDataArray();
boolean doZlibCompression = true;
CV cv = new CV("MS");
CVParam numpressParam = CommonCvParams.MSNUMPRESS_LINEAR_COMPRESSION_PARAM;
array.set64BitIntArrayAsBinaryData(mzValues, doZlibCompression, cv, numpressParam);
```

It can be seen in this example that the chosen MSNumpress CVParam is the one for linear prediction compression (numLin).
Algorithms numSlof and numPic could be chosen by changing from `CommonCvParams.MSNUMPRESS_LINEAR_COMPRESSION_PARAM` to `CommonCvParams.MSNUMPRESS_SLOF_COMPRESSION_PARAM` or `CommonCvParams.MSNUMPRESS_PIC_COMPRESSION_PARAM`

`CommonCvParams` is a new utility class that provides a single instance of a given CVParam, cutting down on memory costs and removing some user error from the system.

It is important to note that the MSNumpress algorithms are designed to work with 64-bit double precision data. There are convenience methods for the _float_, _int_ and _long_ types that allow these compression methods to be used with non 64-bit data. However storing 32 bit values using a method designed for 64-bit values may not be very efficient. You should consider your source data before employing any particular compression algorithm.

You choose not to use zlib compression when using MSNumpress compression.

# Reading data with MSNumpress compression #

Reading MSNumpress compressed data is no different from reading non MSNumpress compressed data as far as users are concerned, with the caveat that as these compression algorithms are not lossless, the data values you retrieve will not be identical to those in your original data.

```
Number[] data = array.getBinaryDataAsNumberArray();
```

That's all there is to it! :)