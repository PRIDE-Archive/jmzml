/**
 * Created by IntelliJ IDEA.
 * User: martlenn
 * Date: 20-Jul-2009
 * Time: 16:23:04
 */
package uk.ac.ebi.jmzml.gui;

import com.compomics.util.gui.spectrum.ChromatogramPanel;
import com.compomics.util.gui.spectrum.SpectrumPanel;
import uk.ac.ebi.jmzml.JmzMLViewer;
import uk.ac.ebi.jmzml.gui.model.MzmlTreeModel;
import uk.ac.ebi.jmzml.model.mzml.*;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshallerException;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
/*
 * CVS information:
 *
 * $Revision$
 * $Date$
 */

/**
 * This class contains the  MzMLTab JPanel, the main GUI component of the JmzMLViewer.
 *
 * @author Lennart Martens
 * @author Harald Barsnes
 * @version $Id$
 */
public class MzMLTab extends JPanel {

    private MzMLUnmarshaller iUnmarshaller = null;
    private JmzMLViewer iParent = null;
    private JSplitPane spltMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    /**
     * The maximum padding allowed in the spectrum and chromatogram panels.
     * Increase if font size on the y-axis becomes too small.
     */
    private int spectrumPanelMaxPadding = 80;

    /**
     * Creates a new MzMLTab JPanel.
     *
     * @param aParent       a reference to the parent JmzMLViewer
     * @param aUnmarshaller the unmarshaller of the mzML file
     */
    public MzMLTab(JmzMLViewer aParent, MzMLUnmarshaller aUnmarshaller) {
        this.iParent = aParent;
        this.iUnmarshaller = aUnmarshaller;
        this.initDisplay();
    }

    /**
     * Sets up the main display and adds the tree listeners.
     */
    private void initDisplay() {

        // Sort spectra by index, but display by ID.
        TreeSet<BigInteger> specIndexes = new TreeSet<BigInteger>(iUnmarshaller.getSpectrumIndexes());
        ArrayList<String> specIDs = new ArrayList(specIndexes.size());

        Iterator<BigInteger> iter = specIndexes.iterator();
        while (iter.hasNext()) {
            specIDs.add(iUnmarshaller.getSpectrumIDFromSpectrumIndex(iter.next()));
        }

        // A tree with spectra on the left, and a spectrum viewer on the right.
        ArrayList<String> chromIDs = new ArrayList(iUnmarshaller.getChromatogramIDs());
        final JTree tree = new JTree(new MzmlTreeModel(specIDs, chromIDs));

        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {

                TreePath path = tree.getSelectionPath();

                if (path != null) {

                    final String node = (String) path.getLastPathComponent();
                    // Only do this if we have something that can be shown,
                    // such as a spectrum or a chromatogram, which are at
                    // the third level of the tree.
                    int pathCount = path.getPathCount();
                    if (pathCount > 2) {
                        final String parent = (String) path.getPathComponent(pathCount - 2);

                        // First create and start a progress bar.
                        final ProgressDialog progressDialog = new ProgressDialog(iParent, iParent, true);
                        progressDialog.setIntermidiate(true);

                        java.awt.EventQueue.invokeLater(new Runnable() {

                            public void run() {
                                progressDialog.setIntermidiate(true);
                                progressDialog.setVisible(true);
                            }
                        });

                        // Now get the spectrum or chromatogram displayed.
                        java.awt.EventQueue.invokeLater(new Runnable() {

                            public void run() {
                                if (parent.equals(MzmlTreeModel.SPECTRUM_SUBROOT)) {
                                    progressDialog.setTitle("Loading Spectrum...");
                                    displaySpectrum(node);
                                } else if (parent.equals(MzmlTreeModel.CHROMATOGRAM_SUBROOT)) {
                                    progressDialog.setTitle("Loading Chromatogram...");
                                    displayChromatogram(node);
                                }
                                progressDialog.setVisible(false);
                                progressDialog.dispose();
                            }
                        });
                    }
                }
            }
        });

        JScrollPane treeScroller = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        spltMain.setTopComponent(treeScroller);
        spltMain.setBottomComponent(new JPanel());

        this.setLayout(new BorderLayout());
        this.add(spltMain, BorderLayout.CENTER);
        spltMain.setOneTouchExpandable(true);
        spltMain.setDividerLocation(0.2);
    }

    /**
     * Reads the given spectrum and displays it on screen.
     *
     * @param aSpecID the ID of the spectrum to display
     */
    private void displaySpectrum(String aSpecID) {

        try {
            Spectrum spectrum = iUnmarshaller.getSpectrumById(aSpecID);
            List<BinaryDataArray> bdal = spectrum.getBinaryDataArrayList().getBinaryDataArray();
            BinaryDataArray mzBinaryDataArray = (BinaryDataArray) bdal.get(0);
            Number[] mzNumbers = mzBinaryDataArray.getBinaryDataAsNumberArray();
            if (mzNumbers.length < 1) {
                // no biinary data found, so no spectrum can be created!
                // reset the view and give some feedback to the user
                JPanel noBinaryErrorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                noBinaryErrorPanel.add(new Label("No binary data for selected Item!"));
                spltMain.setBottomComponent(noBinaryErrorPanel);
                return; // no need to look for SpectrumPanel data, as there is no binary data!
            }
            double[] mz = new double[mzNumbers.length];
            for (int i = 0; i < mzNumbers.length; i++) {
                mz[i] = mzNumbers[i].doubleValue();
            }
            BinaryDataArray intBinaryDataArray = (BinaryDataArray) bdal.get(1);
            Number[] intNumbers = intBinaryDataArray.getBinaryDataAsNumberArray();
            double[] intensities = new double[intNumbers.length];
            for (int i = 0; i < intNumbers.length; i++) {
                intensities[i] = intNumbers[i].doubleValue();
            }
            //PrecursorList plist = spectrum.getPrecursorList();
            double precursorMz = 0.0;
            String precursorCharge = "?";
            if (spectrum.getPrecursorList() != null) {
                List<Precursor> plist = spectrum.getPrecursorList().getPrecursor();

                if (plist != null) {
                    if (plist.size() == 1) {
                        //SelectedIonList sIonList = plist.getPrecursor().get(0).getSelectedIonList()
                        if (plist.get(0).getSelectedIonList() != null) {
                            List<ParamGroup> sIonList = plist.get(0).getSelectedIonList().getSelectedIon();
                            if (sIonList != null) {
                                List<CVParam> cvParams = sIonList.get(0).getCvParam();
                                for (Object cvParam : cvParams) {
                                    CVParam lCVParam = (CVParam) cvParam;
                                    if (lCVParam.getAccession().equals("MS:1000744")) {
                                        precursorMz = Double.parseDouble(lCVParam.getValue().trim());
                                    } else if (lCVParam.getAccession().equals("MS:1000041")) {
                                        precursorCharge = lCVParam.getValue();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            int msLevel = -1;
            boolean isCentroid = false;
            List<CVParam> specParams = spectrum.getCvParam();
            for (Iterator lCVParamIterator = specParams.iterator(); lCVParamIterator.hasNext();) {
                CVParam lCVParam = (CVParam) lCVParamIterator.next();
                if (lCVParam.getAccession().equals("MS:1000511")) {
                    msLevel = Integer.parseInt(lCVParam.getValue().trim());
                }
                if (lCVParam.getAccession().equals("MS:1000127")) {
                    isCentroid = true;
                }
            }
            JPanel specPanel = new SpectrumPanel(
                    mz, intensities, precursorMz, precursorCharge, aSpecID, spectrumPanelMaxPadding, true, true, true, msLevel, !isCentroid);
            spltMain.setBottomComponent(specPanel);
        } catch (MzMLUnmarshallerException mue) {
            iParent.seriousProblem("Unable to access file: " + mue.getMessage(), "Problem reading spectrum!");
        }
    }

    /**
     * Reads the given chromatogram and displays it on screen.
     *
     * @param aChromatogramID the ID of the chromatogram to display
     */
    private void displayChromatogram(String aChromatogramID) {

        try {
            Chromatogram chromatogram = iUnmarshaller.getChromatogramById(aChromatogramID);
            List<BinaryDataArray> bdal = chromatogram.getBinaryDataArrayList().getBinaryDataArray();
            BinaryDataArray xAxisBinaryDataArray = (BinaryDataArray) bdal.get(0);
            Number[] xAxisNumbers = xAxisBinaryDataArray.getBinaryDataAsNumberArray();
            double[] xAxis = new double[xAxisNumbers.length];
            for (int i = 0; i < xAxisNumbers.length; i++) {
                xAxis[i] = xAxisNumbers[i].doubleValue();
            }
            String xAxisLabel = null;
            List<CVParam> xArrayParams = xAxisBinaryDataArray.getCvParam();
            for (Iterator lCVParamIterator = xArrayParams.iterator(); lCVParamIterator.hasNext();) {
                CVParam lCVParam = (CVParam) lCVParamIterator.next();
                if (lCVParam.getUnitAccession() != null) {
                    xAxisLabel = lCVParam.getName() + " (" + lCVParam.getUnitName() + ")";
                }
            }
            BinaryDataArray yAxisBinaryDataArray = (BinaryDataArray) bdal.get(1);
            Number[] yAxisNumbers = yAxisBinaryDataArray.getBinaryDataAsNumberArray();
            double[] yAxis = new double[yAxisNumbers.length];
            for (int i = 0; i < yAxisNumbers.length; i++) {
                yAxis[i] = yAxisNumbers[i].doubleValue();
            }
            String yAxisLabel = null;
            List<CVParam> yArrayParams = yAxisBinaryDataArray.getCvParam();
            for (Iterator lCVParamIterator = yArrayParams.iterator(); lCVParamIterator.hasNext();) {
                CVParam lCVParam = (CVParam) lCVParamIterator.next();
                // @TODO parse out relevant bits.
                if (lCVParam.getUnitAccession() != null) {
                    yAxisLabel = lCVParam.getName() + " (" + lCVParam.getUnitName() + ")";
                }
            }
            ChromatogramPanel chromPanel = new ChromatogramPanel(xAxis, yAxis, xAxisLabel, yAxisLabel);
            chromPanel.setMaxPadding(spectrumPanelMaxPadding);
            spltMain.setBottomComponent(chromPanel);
        } catch (MzMLUnmarshallerException mue) {
            iParent.seriousProblem("Unable to access file: " + mue.getMessage(), "Problem reading spectrum!");
        }
    }
}
