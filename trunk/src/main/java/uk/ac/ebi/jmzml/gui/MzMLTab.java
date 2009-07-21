/**
 * Created by IntelliJ IDEA.
 * User: martlenn
 * Date: 20-Jul-2009
 * Time: 16:23:04
 */
package uk.ac.ebi.jmzml.gui;

import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshaller;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshallerException;
import uk.ac.ebi.jmzml.model.mzml.*;
import uk.ac.ebi.jmzml.JmzMLViewer;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
/*
 * CVS information:
 *
 * $Revision$
 * $Date$
 */

/**
 * This class
 *
 * @author martlenn
 * @version $Id$
 */
public class MzMLTab extends JPanel {

    private MzMLUnmarshaller iUnmarshaller = null;
    private JmzMLViewer iParent = null;
    private JSplitPane spltMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);;

    public MzMLTab(JmzMLViewer aParent, MzMLUnmarshaller aUnmarshaller) {
        this.iParent = aParent;
        this.iUnmarshaller = aUnmarshaller;
        this.initDisplay();
    }

    private void initDisplay() {
        // A tree with spectra on the left, and a spectrum viewer on the right.
        TreeSet<String> specIDs = new TreeSet(iUnmarshaller.getSpectrumIDs());
        final JTree tree = new JTree(specIDs.toArray());
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreeNode node = (TreeNode) tree.getSelectionPath().getLastPathComponent();
                displaySpectrum(node.toString());
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

    private void displaySpectrum(String aSpecID) {
        try {
            Spectrum spectrum = iUnmarshaller.getSpectrumByRefId(aSpecID);
            List<BinaryDataArray> bdal = spectrum.getBinaryDataArrayList().getBinaryDataArray();
            BinaryDataArray mzBinaryDataArray = (BinaryDataArray) bdal.get(0);
            Number[] mzNumbers = mzBinaryDataArray.getBinaryDataAsNumberArray();
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
            PrecursorList plist = spectrum.getPrecursorList();
            double precursorMz = 0.0;
            String precursorCharge = "?";
            if(plist != null) {
                if(plist.getCount().intValue() == 1) {
                    List<CVParam> cvParams = plist.getPrecursor().get(0).getSelectedIonList().getSelectedIon().get(0).getCvParam();
                    for (Iterator lCVParamIterator = cvParams.iterator(); lCVParamIterator.hasNext();) {
                        CVParam lCVParam = (CVParam) lCVParamIterator.next();
                        if(lCVParam.getAccession().equals("MS:1000744")) {
                            precursorMz = Double.parseDouble(lCVParam.getValue().trim());
                        } else if(lCVParam.getAccession().equals("MS:1000041")) {
                            precursorCharge = lCVParam.getValue();
                        }
                    }
                }
            }
            int msLevel = -1;
            List<CVParam> specParams = spectrum.getCvParam();
            for (Iterator lCVParamIterator = specParams.iterator(); lCVParamIterator.hasNext();) {
                CVParam lCVParam = (CVParam) lCVParamIterator.next();
                if(lCVParam.getAccession().equals("MS:1000511")) {
                    msLevel = Integer.parseInt(lCVParam.getValue().trim());
                }
            }
            SpectrumPanel specPanel = new SpectrumPanel(mz, intensities, msLevel, precursorMz, precursorCharge, aSpecID);
            spltMain.setBottomComponent(specPanel);
        } catch(MzMLUnmarshallerException mue) {
            iParent.seriousProblem("Unable to access file: " + mue.getMessage(), "Problem reading spectrum!");
        }
    }

}
