/**
 * Created by IntelliJ IDEA.
 * User: martlenn
 * Date: 03-Sep-2009
 * Time: 11:54:59
 */
package uk.ac.ebi.jmzml.gui.model;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeModelListener;
import java.util.ArrayList;
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
public class MzmlTreeModel implements TreeModel {

    private ArrayList<String> iSpectrumIDs = new ArrayList<String>();
    private ArrayList<String> iChromatogramIDs = new ArrayList<String>();

    public final static String ROOT_TERM = "mzML";
    public final static String SPECTRUM_SUBROOT = "Spectrum";
    public final static String CHROMATOGRAM_SUBROOT = "Chromatogram";

    /**
     * Constructor for the tree model that takes a list of spectrum IDs
     * and a list of chromatogram IDs, both of which can be empty or 'null'.
     *
     * @param aSpectrumIds ArrayList<String> with the spectrum IDs, can be empty or
     *                     'null' for no spectra.
     * @param aChromatogramIds ArrayList<String> with the chromatogram IDs, can be empty or
     *                         'null' for no chromatograms.
     */
    public MzmlTreeModel(ArrayList<String> aSpectrumIds, ArrayList<String> aChromatogramIds) {
        if(aSpectrumIds != null) {
            iSpectrumIDs = aSpectrumIds;
        }
        if(aChromatogramIds != null) {
            iChromatogramIDs = aChromatogramIds;
        }
    }

    public Object getRoot() {
        return ROOT_TERM;
    }

    public Object getChild(Object parent, int index) {
        Object result = null;
        if(ROOT_TERM.equals(parent)) {
            switch(index) {
                case 0:
                    result = SPECTRUM_SUBROOT;
                    break;
                case 1:
                    result = CHROMATOGRAM_SUBROOT;
                    break;
                default:
                    break;
            }
        } else if(SPECTRUM_SUBROOT.equals(parent)) {
            if((0 <= index) && (index < iSpectrumIDs.size())) {
                result = iSpectrumIDs.get(index);
            }
        } else if(CHROMATOGRAM_SUBROOT.equals(parent)) {
            if((0 <= index) && (index < iChromatogramIDs.size())) {
                result = iChromatogramIDs.get(index);
            }
        }
        return result;
    }

    public int getChildCount(Object parent) {
        int result = 0;
        if(ROOT_TERM.equals(parent)) {
            result = 2;
        } else if(SPECTRUM_SUBROOT.equals(parent)) {
            result = iSpectrumIDs.size();
        } else if(CHROMATOGRAM_SUBROOT.equals(parent)) {
            result = iChromatogramIDs.size();
        }
        return result;
    }

    public boolean isLeaf(Object node) {
        boolean result = true;
        if(ROOT_TERM.equals(node) || SPECTRUM_SUBROOT.equals(node) || CHROMATOGRAM_SUBROOT.equals(node)) {
            result = false;
        }
        return result;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        // Not implemented
    }

    public int getIndexOfChild(Object parent, Object child) {
        int result = -1;
        if(ROOT_TERM.equals(parent)) {
            if(SPECTRUM_SUBROOT.equals(child)) {
                result = 0;
            } else if(CHROMATOGRAM_SUBROOT.equals(child)) {
                result = 1;
            }
        } else if(SPECTRUM_SUBROOT.equals(parent)) {
            result = iSpectrumIDs.indexOf(child);
        } else if(CHROMATOGRAM_SUBROOT.equals(parent)) {
            result = iChromatogramIDs.indexOf(child);
        }
        return result;
    }

    public void addTreeModelListener(TreeModelListener l) {
        // Not implemented
    }

    public void removeTreeModelListener(TreeModelListener l) {
        // Not implemented
    }
}
