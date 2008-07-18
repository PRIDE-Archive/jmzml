package uk.ac.ebi.jmzml.xml.xxindex;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;

public class FileUtils {

    private static Logger logger = Logger.getLogger(FileUtils.class);

    public static File getFileFromURL(URL url) {

        BufferedReader in = null;
        PrintWriter out = null;
        try {

            String tempDir = System.getProperty("java.io.tmpdir", ".");

            // Create temp file.
            File tempFile = File.createTempFile("xxindex", ".tmp", new File(tempDir));

            // Delete temp file when program exits.
            tempFile.deleteOnExit();

            //copy content of URL to local file
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            out = new PrintWriter(new FileWriter(tempFile));

            String oneLine;
            while ((oneLine = in.readLine()) != null) {
                out.println(oneLine);
            }

            logger.debug(url + " written to local file " + tempFile.getAbsolutePath());

            return tempFile;

        } catch (IOException e) {
            throw new IllegalStateException("Could not create local file for URL: " + url, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                /* ignore */
            }

        }

    }

}
