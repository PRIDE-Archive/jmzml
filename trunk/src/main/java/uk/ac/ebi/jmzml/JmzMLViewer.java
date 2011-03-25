/**
 * Created by IntelliJ IDEA.
 * User: martlenn
 * Date: 20-Jul-2009
 * Time: 15:50:30
 */
package uk.ac.ebi.jmzml;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import org.xml.sax.SAXException;
import uk.ac.ebi.jmzml.gui.HelpWindow;
import uk.ac.ebi.jmzml.gui.MzMLTab;
import uk.ac.ebi.jmzml.gui.ProgressDialog;
import uk.ac.ebi.jmzml.gui.ProgressDialogParent;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
/*
 * CVS information:
 *
 * $Revision$
 * $Date$
 */

/**
 * This class provides a basic viewer for jmzML spectra.
 *
 * @author Lennart Martens
 * @author Harald Barsnes
 * @version $Id$
 */
public class JmzMLViewer extends JFrame implements ProgressDialogParent {

    /**
     * A reference to the last mzML file opened in the viewer. Defaults to
     * the user's home directory.
     */
    private String pathToLastOpenedFile = "user.home";

    /**
     * A small dialog containing a progress bar.
     */
    ProgressDialog progressDialog;

    private ArrayList<MzMLUnmarshaller> iUnmarshallers = null;
    private ArrayList<String> iFilenames = null;
    private JTabbedPane jtpMain = null;

    /**
     * Basic constructor.
     */
    public JmzMLViewer() {
        this(null);
    }

    /**
     * Constructor, that also tries to open any files provided as paramaters.
     *
     * @param aMzMLFiles array of mzML files to open
     */
    public JmzMLViewer(ArrayList<File> aMzMLFiles) {
        super("jmzML Viewer");

        // add the version number to the title
        this.setTitle(this.getTitle() + " " + getVersion());

        this.initDisplay();
        if(aMzMLFiles == null) {
            this.loadMzmlFile(false);
        } else {
            iUnmarshallers = new ArrayList<MzMLUnmarshaller>();
            iFilenames = new ArrayList<String>();
            for (Iterator lFileIterator = aMzMLFiles.iterator(); lFileIterator.hasNext();) {
                File lFile = (File) lFileIterator.next();
                addUnmarshaller(new MzMLUnmarshaller(lFile), lFile.getName());
            }
        }
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                terminate(0);
            }
        });
    }

    /**
     * Retrieves the version number set in the pom file.
     *
     * @return the version number of jmzML
     */
    private String getVersion() {

        java.util.Properties p = new java.util.Properties();

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("jmzML.properties");
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p.getProperty("jmzML.version");
    }

    /**
     * Main method. Starts the JmzMLViewer and tries to open any files provided as
     * paramaters.
     *
     * @param args array of mzML files to open
     */
    public static void main(String[] args) {
        ArrayList<File> mzMLFiles = new ArrayList<File>();
        if(args != null) {
            for (int i = 0; i < args.length; i++) {
                String inFile = args[i];
                File inputFile = new File(args[i]);
                if(!inputFile.exists()) {
                    System.err.println("\n\n  ** The mzML file you specified ('" + inFile + "') does not exist!\n     Skipping file...\n");
                }
                if(inputFile.isDirectory()) {
                    System.err.println("\n\n  ** The mzML file you specified ('" + inFile + "') is a folder, not a file!\n     Skipping file...\n");
                }
                mzMLFiles.add(inputFile);
                System.out.println("Using file "+inputFile.getName());
            }
        }

        // Set the Java Look and Feel
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JmzMLViewer jmzMLViewer = null;
        if(mzMLFiles == null || mzMLFiles.isEmpty()) {
            jmzMLViewer = new JmzMLViewer();
        } else {
            jmzMLViewer = new JmzMLViewer(mzMLFiles);
        }

        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        jmzMLViewer.setBounds(50,50, width-250, height-250);

        // locate the viewer in the middle of the screen
        jmzMLViewer.setLocationRelativeTo(null);
        jmzMLViewer.setVisible(true);
    }

    /**
     * Initiates the display.
     */
    private void initDisplay() {
        // Add menubar.
        this.addMenuBar();
        // View consists of multiple tabs.
        jtpMain = new JTabbedPane(JTabbedPane.TOP);

        this.getContentPane().add(jtpMain, BorderLayout.CENTER);
    }

    /**
     * Adds the menu bar to the graphical user interface.
     */
    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem openItem = new JMenuItem("Open...", KeyEvent.VK_O);
        openItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 
                java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMzmlFile(true);
            }
        });

        JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_E);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                terminate(0);
            }
        });
        
        fileMenu.add(openItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_A);
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openHelpDialog("/helpfiles/AboutJmzML.html");
            }
        });
        JMenuItem helpItem = new JMenuItem("Help", KeyEvent.VK_H);
        helpItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openHelpDialog("/helpfiles/JmzMLViewer.html");
            }
        });

        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);

        this.getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    /**
     * Openes the help dialog.
     *
     * @param urlAsString the URL (as a String) of the help file to display
     */
    private void openHelpDialog(String urlAsString){
        new HelpWindow(this, getClass().getResource(urlAsString));
    }

    /**
     * This method loads an mzML file from disk.
     *
     * @param addTab    boolean to indicate whether the loaded file
     *                  should be added to the existing tabs, or
     *                  whether it should replace all existing tabs.
     */
    private void loadMzmlFile(boolean addTab) {
        JFileChooser jfc = new JFileChooser(pathToLastOpenedFile);
        jfc.setDialogTitle("Select mzML file to view...");
        jfc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                boolean result = false;
                if(f.isDirectory() || f.getName().toLowerCase().endsWith(".mzml") || f.getName().toLowerCase().endsWith(".xml")) {
                    result = true;
                }
                return result;
            }

            @Override
            public String getDescription() {
                return ".mzml or .xml";
            }
        });
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
        int result = jfc.showOpenDialog(this);
        if(result == JFileChooser.CANCEL_OPTION) {
            if(addTab) {
                return;
            } else {
                terminate(0);
            }
        } else {
            final File selected = jfc.getSelectedFile();
            pathToLastOpenedFile = selected.getAbsolutePath();
            if(!addTab) {
                iUnmarshallers = new ArrayList<MzMLUnmarshaller>();
                iFilenames = new ArrayList<String>();
            }

            // First create and start a progress bar.
            progressDialog = new ProgressDialog(this, this, true);
            progressDialog.setIntermidiate(true);

            new Thread(new Runnable() {
                public void run() {
                    progressDialog.setIntermidiate(true);
                    progressDialog.setTitle("Validating mzML File. Please Wait...");
                    progressDialog.setVisible(true);
                }
            }, "ProgressDialog").start();

            // Now validate file, and open it if it is valid.
            new Thread("validateAndOpenMzMLFileThread") {

                @Override
                public void run() {
                    // validate the mzML file before opening it
                    String errors = validateMzMLFile(selected);

                    if(errors != null) {
                        JOptionPane.showMessageDialog(JmzMLViewer.this,
                                new String[] {"jmzML validation failed. Please check your mzML file.\n",
                                              "Error messages returned by validation:\n",
                                              errors},
                                "Your mzML File Failed Validation!",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        progressDialog.setTitle("Reading mzML File. Please Wait...");
                        addUnmarshaller(new MzMLUnmarshaller(selected), selected.getName());
                    }
                    progressDialog.setVisible(false);
                    progressDialog.dispose();
                }
            }.start();
        }
    }

    /**
     * Adds a new tab to the viewer with a new unmarshaller.
     *
     * @param aUnmarshaller the unmarshaller to add
     * @param aFilename the name of the mzML file to unmarshall
     */
    private void addUnmarshaller(MzMLUnmarshaller aUnmarshaller, String aFilename) {
        iUnmarshallers.add(aUnmarshaller);
        iFilenames.add(aFilename);
        jtpMain.addTab(aFilename, new MzMLTab(this, aUnmarshaller));
    }

    /**
     * Opens a dialog with an error message to show the user. Then closes the JmzMLViewer.
     *
     * @param aMessage the message to display
     * @param aTitle the title of the dialog
     */
    public void seriousProblem(String aMessage, String aTitle) {
        JOptionPane.showMessageDialog(this, new String[] {aMessage, "", "Quitting application"}, aTitle, JOptionPane.ERROR_MESSAGE);
        terminate(1);
    }

    /**
     * Closes the JmzMLViewer and terminates the JVM.
     *
     * @param aStatus exit status
     */
    private void terminate(int aStatus) {
        this.setVisible(false);
        this.dispose();
        System.exit(aStatus);
    }

    /**
     * Cancels the opening of a jmzML file and closes the tool.
     */
    public void cancelProgress() {
        terminate(0);
    }

    /**
     * Validates the mzML file. Returns 'null' String if the file is valid,
     * or a non-null String with the errors if there was a problem.
     *
     * @param mzML the mzML file to validate
     * @return String with the error msg, or 'null' if the file is valid.
     */
    private String validateMzMLFile(File mzML){

        // 1. Lookup a factory for the W3C XML Schema language
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

        // 2. Compile the schema.
        URL schemaLocation;
        schemaLocation = this.getClass().getClassLoader().getResource("mzML1.1.1-idx.xsd");

        if(schemaLocation == null){
            return "Schema (mzML1.1.1-idx.xsd) not found!";
        }

        Schema schema;

        try {
            schema = factory.newSchema(schemaLocation);
        } catch (SAXException e) {
            e.printStackTrace();
            return "Could not compile Schema for file:\n" + schemaLocation;
        }

        // 3. Get a validator from the schema.
        Validator validator = schema.newValidator();
        
        // 4. Parse the document you want to check.
        Source source = new StreamSource(mzML);

        // 5. Check the document
        String errorMsg = null;
        try {
            validator.validate(source);
        } catch (SAXException ex) {
            System.out.println(mzML.getName() + " is not valid because ");
            System.out.println(ex.getMessage());
            errorMsg = ex.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            errorMsg = "Could not validate file because of file read problems for source:\n" + mzML.getAbsolutePath();
        }

        return errorMsg;
    }
}
