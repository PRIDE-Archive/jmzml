/**
 * Created by IntelliJ IDEA.
 * User: martlenn
 * Date: 20-Jul-2009
 * Time: 15:34:30
 */
package uk.ac.ebi.jmzml.gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
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
public class SpectrumPanel extends JPanel {

    /**
     * This status indicates that no annotation will be displayed,
     * but the user will have a fully functional interface (peak clicking, selecting,
     * sequencing etc.)
     */
    public static final int INTERACTIVE_STATUS = 0;
    /**
     * This status indicates that annotation (if present) will be displayed,
     * while limiting the user to zooming in/out.
     */
    public static final int ANNOTATED_STATUS = 1;
    /**
     * This is the color the spectrumfilename should be presented in.
     */
    private Color iSpectrumFilenameColor = null;
    /**
     * Color in which the actual m/z peaks are rendered. Defaults to red.
     */
    private Color iSpectrumColor = Color.red;
    /**
     * The spectrum filename.
     */
    private String iSpecFilename = null;
    /**
     * The deviation (both left and right) allowed for peak highlighting detection.
     */
    private int iPeakDetectionTolerance = 5;
    /**
     * Boolean that will be 'true' when a peak needs highlighting.
     */
    private boolean iHighLight = false;
    /**
     * Index of the peak that needs to be highlighted.
     */
    private int iHighLightIndex = 0;
    /**
     * When the mouse is dragged, this represents the
     * X-coordinate of the starting location.
     */
    private int iStartXLoc = 0;
    /**
     * When the mouse is dragged, this represents the
     * Y-coordinate of the starting location.
     */
    private int iStartYLoc = 0;
    /**
     * When the mouse is dragged, this represents the
     * X-coordinate of the ending location.
     */
    private int iEndXLoc = 0;
    /**
     * The current dragging location.
     */
    private int iDragXLoc = 0;
    /**
     * Scale unit for the X axis
     */
    private double iXScaleUnit = 0.0;
    /**
     * Scale unit for the Y axis
     */
    private double iYScaleUnit = 0.0;
    /**
     * Effective distance from the x-axis to the panel border.
     */
    private int iXPadding = 0;
    /**
     * Effective distance from the panel top border
     * to 5 pixels above the top of the highest peak (or y-tick mark).
     */
    private int iTopPadding = 0;
    /**
     * This boolean is set to 'true' if the mz axis should start at zero.
     */
    private boolean iMzAxisStartAtZero = true;
    /**
     * This boolean is set to 'true' when dragging is performed.
     */
    private boolean iDragged = false;
    /**
     * The number of X-axis tags.
     */
    private int xTagCount = 10;
    /**
     * The number of Y-axis tags.
     */
    private int yTagCount = 10;
    /**
     * The padding (distance between the axes and the border of the panel).
     */
    private int padding = 20;
    /**
     * The maximum padding (distance between the axes and the border of the panel).
     */
    private int maxPadding = 50;
    /**
     * The boolean is set to 'true' if the decimals should not be shown for the axis tags.
     */
    private boolean hideDecimals = false;
    /**
     * The boolean is set to 'true' if the file name is to be shown in the panel.
     */
    private boolean showFileName = true;
    /**
     * The double[] with all the masses. Should at all times be sorted from
     * high to low.
     */
    private double[] iMasses = null;
    /**
     * The minimum mass to display.
     */
    private double iMassMin = 0.0;
    /**
     * The maximum mass to display.
     */
    private double iMassMax = 0.0;
    /**
     * The minimum intensity to display.
     */
    private double iIntMin = 0.0;
    /**
     * The maximum intensity to display.
     */
    private double iIntMax = 0.0;
    /**
     * The index of the peak with the highest intensity.
     */
    private int iIntMaxIndex = 0;
    /**
     * The double[] with all the intensities. Related to the masses by index.
     * So the first intensity is the intensity for the first mass in the 'iMasses'
     * variable.
     */
    private double[] iIntensities = null;
    /**
     * This variable holds the precursor M/Z.
     */
    private double iPrecursorMZ = 0.0;
    /**
     * This String holds the charge for the precursor.
     */
    private String iPrecursorCharge = null;
    /**
     * This array will hold the x-coordinates in pixels for
     * all the masses. Link is through index.
     */
    private int[] iMassInPixels = null;
    /**
     * This array will hold the y-coordinates in pixels for
     * all the masses. Link is through index.
     */
    private int[] iIntensityInPixels = null;
    /**
     * Minimal dragging distance in pixels.
     */
    private int iMinDrag = 15;
    /**
     * The ms level of the current spectrum.
     */
    private int iMSLevel = 0;

    /**
     * This constructor creates a SpectrumPanel based on the passed parameters.
     *
     * @param aMZ                   double[] with all the masses to anotate.
     * @param aIntensity            double[] with all the intensities for the peaks.
     * @param aMSLevel              int with the ms level for the spectrum
     * @param aPrecursorMZ          Double with the precursor m/z.
     * @param aPrecursorCharge      String with the precursor charge.
     * @param aFileName             String with the title of the spectrum.
     */
    public SpectrumPanel(double[] aMZ, double[] aIntensity, int aMSLevel, Double aPrecursorMZ, String aPrecursorCharge, String aFileName) {
        this(aMZ, aIntensity, aMSLevel, aPrecursorMZ, aPrecursorCharge, aFileName, 50, false, true);
    }

    /**
     * This constructor creates a SpectrumPanel based on the passed parameters.
     *
     * @param aMZ                   double[] with all the masses to anotate.
     * @param aIntensity            double[] with all the intensities of the peaks.
     * @param aMSLevel              int with the ms level for the spectrum
     * @param aPrecursorMZ          Double with the precursor m/z.
     * @param aPrecursorCharge      String with the precursor charge.
     * @param aFileName             String with the title of the spectrum.
     * @param aMaxPadding   int the sets the maximum padding size.
     * @param aHideDecimals boolean that specifies if the decimals for the axis tags should be shown.
     * @param aShowFileName boolean that specifies if the file name should be shown in the panel.
     */
    public SpectrumPanel(double[] aMZ, double[] aIntensity, int aMSLevel, double aPrecursorMZ, String aPrecursorCharge,
        String aFileName, int aMaxPadding, boolean aHideDecimals, boolean aShowFileName) {
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.setBackground(Color.WHITE);
        processMzsAndIntensities(aMZ, aIntensity);
        iMSLevel = aMSLevel;
        iPrecursorMZ = aPrecursorMZ;
        iPrecursorCharge = aPrecursorCharge;
        iSpecFilename = aFileName;
        this.maxPadding = aMaxPadding;
        this.hideDecimals = aHideDecimals;
        this.showFileName = aShowFileName;
        this.addListeners();
    }

    /**
     * This method sets the start value of the mz axis to zero.
     */
    public void setMzAxisStartAtZero(boolean aMzAxisStartAtZero) {
        iMzAxisStartAtZero = aMzAxisStartAtZero;
    }

    /**
     * This method sets the display color for the spectrumfilename on the panel.
     * Can be 'null' for default coloring.
     *
     * @param aSpectrumFilenameColor    Color to render the filename in on the panel.
     *                                  Can be 'null' for default coloring.
     */
    public void setSpectrumFilenameColor(Color aSpectrumFilenameColor) {
        iSpectrumFilenameColor = aSpectrumFilenameColor;
    }

    /**
     * Invoked by Swing to draw components.
     * Applications should not invoke <code>paint</code> directly,
     * but should instead use the <code>repaint</code> method to
     * schedule the component for redrawing.
     * <p/>
     * This method actually delegates the work of painting to three
     * protected methods: <code>paintComponent</code>,
     * <code>paintBorder</code>,
     * and <code>paintChildren</code>.  They're called in the order
     * listed to ensure that children appear on top of component itself.
     * Generally speaking, the component and its children should not
     * paint in the insets area allocated to the border. Subclasses can
     * just override this method, as always.  A subclass that just
     * wants to specialize the UI (look and feel) delegate's
     * <code>paint</code> method should just override
     * <code>paintComponent</code>.
     *
     * @param g the <code>Graphics</code> context in which to paint
     * @see #paintComponent
     * @see #paintBorder
     * @see #paintChildren
     * @see #getComponentGraphics
     * @see #repaint
     */
    public void paint(Graphics g) {
        super.paint(g);
        if (iMasses != null) {
            if (iDragged && iDragXLoc > 0) {
                g.drawLine(iStartXLoc, iStartYLoc, iDragXLoc, iStartYLoc);
                g.drawLine(iStartXLoc, iStartYLoc - 2, iStartXLoc, iStartYLoc + 2);
                g.drawLine(iDragXLoc, iStartYLoc - 2, iDragXLoc, iStartYLoc + 2);
            }
            // @TODO scale.
            drawAxes(g, iMassMin, iMassMax, 2, iIntMin, iIntMax);
            drawPeaks(g);
            if (iHighLight) {
                this.highLightPeak(iHighLightIndex, g);
                iHighLight = false;
            }
        }
    }

    /**
     * This method reports on the largest m/z in the peak collection.
     *
     * @return double with the largest m/z in the peak collection.
     */
    public double getMaxMass() {
        return iMasses[iMasses.length - 1];
    }

    /**
     * This method reports on the smallest m/z in the peak collection.
     *
     * @return double with the smallest m/z in the peak collection.
     */
    public double getMinMass() {
        return iMasses[0];
    }

    /**
     * This method adds the event listeners to the panel.
     */
    private void addListeners() {
        this.addMouseListener(new MouseAdapter() {

            /**
             * Invoked when a mouse button has been released on a component.
             */
            public void mouseReleased(MouseEvent e) {
                if (iMasses != null) {
                    if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
                        if (iMzAxisStartAtZero) {
                            rescale(0.0, iMasses[iMasses.length - 1]);
                        } else {
                            rescale(iMasses[0], iMasses[iMasses.length - 1]);
                        }
                        iDragged = false;
                        repaint();
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        iEndXLoc = e.getX();
                        int min = Math.min(iEndXLoc, iStartXLoc);
                        int max = Math.max(iEndXLoc, iStartXLoc);
                        double start = iMassMin + ((min - iXPadding) * iXScaleUnit);
                        double end = iMassMin + ((max - iXPadding) * iXScaleUnit);
                        if (iDragged) {
                            iDragged = false;
                            // Rescale.
                            if ((max - min) > iMinDrag) {
                                rescale(start, end);
                            }
                            iDragXLoc = 0;
                            repaint();
                        }
                    }
                }
            }

            /**
             * Invoked when a mouse button has been pressed on a component.
             */
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    iStartXLoc = e.getX();
                    iStartYLoc = e.getY();
                }
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {

            /**
             * Invoked when a mouse button is pressed on a component and then
             * dragged.  Mouse drag events will continue to be delivered to
             * the component where the first originated until the mouse button is
             * released (regardless of whether the mouse position is within the
             * bounds of the component).
             */
            public void mouseDragged(MouseEvent e) {
                iDragged = true;
                iDragXLoc = e.getX();
                repaint();
            }

            /**
             * Invoked when the mouse button has been moved on a component
             * (with no buttons no down).
             */
            public void mouseMoved(MouseEvent e) {
                if (iMasses != null && iMassInPixels != null) {
                    int x = e.getX();
                    int y = e.getY();
                    for (int i = 0; i < iMassInPixels.length; i++) {
                        int delta = iMassInPixels[i] - x;
                        if (Math.abs(delta) < iPeakDetectionTolerance) {
                            int deltaYPixels = y - iIntensityInPixels[i];
                            if (deltaYPixels < 0 && Math.abs(deltaYPixels) < (getHeight() - iIntensityInPixels[i])) {
                                iHighLight = true;
                                iHighLightIndex = i;
                                repaint();
                            }
                        } else if (delta >= iPeakDetectionTolerance) {
                            break;
                        }
                    }
                    repaint();
                }
            }
        });
    }

    /**
     * This method rescales the X-axis while notifying the observers.
     *
     * @param aMinMass  double with the new minimum mass to display.
     * @param aMaxMass  double with the new maximum mass to display.
     */
    public void rescale(double aMinMass, double aMaxMass) {
        this.rescale(aMinMass, aMaxMass, true);
    }

    /**
     * This method sets the color in which the m/z peaks will be rendered.
     *
     * @param aColor Color to render the m/z peaks in.
     */
    public void setSpectrumColor(Color aColor) {
        this.iSpectrumColor = aColor;
    }

    /**
     * This method rescales the X-axis, allowing the caller to specify whether the
     * observers need be notified.
     *
     * @param aMinMass  double with the new minimum mass to display.
     * @param aMaxMass  double with the new maximum mass to display.
     * @param aNotifyListeners  boolean to indicate whether the observers should be notified.
     */
    public void rescale(double aMinMass, double aMaxMass, boolean aNotifyListeners) {
        // Calculate the new max intensity.
        double maxInt = 1.0;
        for (int i = 0; i < iMasses.length; i++) {
            double lMass = iMasses[i];
            if (lMass < aMinMass) {
                continue;
            } else if (lMass > aMaxMass) {
                break;
            } else {
                if (iIntensities[i] > maxInt) {
                    maxInt = iIntensities[i];
                    iIntMaxIndex = i;
                }
            }
        }
        // Init the new params.
        double delta = aMaxMass - aMinMass;

        // Round to nearest order of 10, based on displayed delta.
        double tempOoM = (Math.log(delta)/Math.log(10))-1;
        if(tempOoM < 0) {
            tempOoM--;
        }
        int orderOfMagnitude = (int)tempOoM;
        double power = Math.pow(10, orderOfMagnitude);
        iMassMin = aMinMass - (aMinMass % power);
        iMassMax = aMaxMass + (power-(aMaxMass % power));

        iIntMax = maxInt + (maxInt / 10);
    }

    /**
     * This method will draw a highlighting triangle + mass on top of the marked peak.
     *
     * @param aIndex int with the index of the peak to highlight.
     * @param g Graphics object to draw the highlighting on.
     */
    private void highLightPeak(int aIndex, Graphics g) {
        this.highLight(aIndex, g, Color.blue, null, 0, true);
    }

    /**
     * This method reads the peaks and their intensities from the specified
     * arrays and stores these internally for drawing. The masses are sorted
     * in this step.
     *
     * @param aMzs double[] with the m/z values.
     * @param aInts double[] with the corresponding intensity values.
     */
    private void processMzsAndIntensities(double[] aMzs, double[] aInts) {
        HashMap peaks = new HashMap(aMzs.length);
        iMasses = new double[aMzs.length];
        iIntensities = new double[aMzs.length];
        for (int i = 0; i < aMzs.length; i++) {
            peaks.put(new Double(aMzs[i]), new Double(aInts[i]));
        }
        // Maximum intensity of the peaks.
        double maxInt = 0.0;
        // TreeSets are sorted.
        TreeSet masses = new TreeSet(peaks.keySet());
        Iterator iter = masses.iterator();
        int count = 0;
        while (iter.hasNext()) {
            Double key = (Double) iter.next();
            double mass = key.doubleValue();
            double intensity = ((Double) peaks.get(key)).doubleValue();
            if (intensity > maxInt) {
                maxInt = intensity;
            }
            iMasses[count] = mass;
            iIntensities[count] = intensity;
            count++;
        }
        if (iMzAxisStartAtZero) {
            this.rescale(0.0, iMasses[iMasses.length - 1]);
        } else {
            this.rescale(iMasses[0], iMasses[iMasses.length - 1]);
        }
    }

    /**
     * This method draws the axes and their labels on the specified Graphics object,
     * taking into account the padding.
     *
     * @param g Graphics object to draw on.
     * @param aXMin double with the minimal x value.
     * @param aXMax double with the maximum x value.
     * @param aXScale int with the scale to display for the X-axis labels (as used in BigDecimal's setScale).
     * @param aYMin double with the minimal y value.
     * @param aYMax double with the maximum y value.
     * @return int[] with the length of the X axis and Y axis respectively.
     */
    private int[] drawAxes(Graphics g, double aXMin, double aXMax, int aXScale, double aYMin, double aYMax) {
        // Recalibrate padding so that it holds the axis labels.
        FontMetrics fm = g.getFontMetrics();
        int intWidth = fm.stringWidth("Int");
        int mzWidth = fm.stringWidth("m/z");
        int minWidth = fm.stringWidth(Double.toString(aYMin));
        int maxWidth = fm.stringWidth(Double.toString(aYMax));
        int max = Math.max(Math.max(intWidth, mzWidth), Math.max(minWidth, maxWidth));
        int tempPadding = padding;
        if ((padding - max) < 0) {
            tempPadding += max;
            if (tempPadding > maxPadding) {
                tempPadding = maxPadding;
            }
        } else {
            tempPadding *= 2;
        }
        // X-axis.
        int xaxis = (this.getWidth() - (2 * tempPadding));
        g.drawLine(tempPadding, this.getHeight() - tempPadding, this.getWidth() - tempPadding, this.getHeight() - tempPadding);
        // Arrowhead on X-axis.
        g.fillPolygon(new int[]{this.getWidth() - tempPadding - 5, this.getWidth() - tempPadding - 5, this.getWidth() - tempPadding},
                new int[]{this.getHeight() - tempPadding + 5, this.getHeight() - tempPadding - 5, this.getHeight() - tempPadding},
                3);
        // X-axis label
        g.drawString("m/z", this.getWidth() - (tempPadding - (padding / 2)), this.getHeight() - tempPadding + 4);
        // Y-axis.
        g.drawLine(tempPadding, this.getHeight() - tempPadding, tempPadding, tempPadding / 2);
        iXPadding = tempPadding;
        int yaxis = this.getHeight() - tempPadding - (tempPadding / 2);
        // Arrowhead on Y axis.
        g.fillPolygon(new int[]{tempPadding - 5, tempPadding + 5, tempPadding},
                new int[]{(tempPadding / 2) + 5, (tempPadding / 2) + 5, tempPadding / 2},
                3);
        // Y-axis label
        g.drawString("Int", tempPadding - intWidth, (tempPadding / 2) - 4);

        // Now the tags along the axes.
        this.drawXTags(g, aXMin, aXMax, aXScale, xaxis, tempPadding);
        int yTemp = yaxis;
        iTopPadding = this.getHeight() - yTemp - 5;
        this.drawYTags(g, aYMin, aYMax, yTemp, tempPadding);

        return new int[]{xaxis, yaxis};
    }

    /**
     * This method draws tags on the X axis.
     *
     * @param aMin  double with the minimum value for the axis.
     * @param aMax  double with the maximum value for the axis.
     * @param aXScale int with the scale to display for the X-axis labels (as used in BigDecimal's setScale).
     * @param g Graphics object to draw on.
     * @param aXAxisWidth   int with the axis width in pixels.
     * @param aPadding  int with the amount of padding to take into account.
     */
    private void drawXTags(Graphics g, double aMin, double aMax, int aXScale, int aXAxisWidth, int aPadding) {
        // Font Metrics. We'll be needing these.
        FontMetrics fm = g.getFontMetrics();
        // Find out how many tags we will have. At most, we'll have xTagCount tags, and if the resolution
        // of the screen is too small, we'll have less.
        int tagWidthEstimate = fm.stringWidth("1545.99") + 15;
        int numberTimes = (aXAxisWidth / tagWidthEstimate);
        if (numberTimes > xTagCount) {
            numberTimes = xTagCount;
        } else if (numberTimes == 0) {
            numberTimes = 1;
        }
        // Calculate the graphical unit, ...
        int xUnit = aXAxisWidth / numberTimes;
        // ... as well as the scale unit.
        double delta = aMax - aMin;
        double scaleUnit = delta / numberTimes;
        iXScaleUnit = delta / aXAxisWidth;
        // Since we know the scale unit, we also know the resolution.
        // This will be displayed on the bottom line.
        String resolution = "Resolution: " + new BigDecimal(iXScaleUnit).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        String msLevel_and_optional_precursor = "MS level: " + iMSLevel;
        if(iMSLevel > 1) {
            // Also print the precursor MZ and charge (if known, '?' otherwise).
            msLevel_and_optional_precursor += "   Precursor M/Z: " + this.iPrecursorMZ + " (" + this.iPrecursorCharge + ")";
        }
        // Finally, we also want the filename.
        String filename = "";
        if (showFileName) {
            filename = "Filename: " + iSpecFilename;
        }
        int precLength = fm.stringWidth(msLevel_and_optional_precursor);
        int resLength = fm.stringWidth(resolution);
        int xDistance = ((this.getWidth() - (iXPadding * 2)) / 4) - (precLength / 2);
        int fromBottom = fm.getAscent() / 2;
        Font oldFont = this.getFont();

        int smallFontCorrection = 0;
        int yHeight = this.getHeight() - fromBottom;
        int xAdditionForResolution = precLength + 15;
        int xAdditionForFilename = xAdditionForResolution + resLength + 15;
        if (precLength + resLength + 45 + fm.stringWidth(filename) > aXAxisWidth) {
            g.setFont(new Font(oldFont.getName(), oldFont.getStyle(), oldFont.getSize() - 2));
            smallFontCorrection = g.getFontMetrics().getAscent();
            xAdditionForFilename = g.getFontMetrics().stringWidth(msLevel_and_optional_precursor) + 5;
            xAdditionForResolution = g.getFontMetrics().stringWidth(msLevel_and_optional_precursor) / 2;
            xDistance = aPadding;
        }
        g.drawString(msLevel_and_optional_precursor, xDistance, yHeight - smallFontCorrection);
        g.drawString(resolution, xDistance + xAdditionForResolution, yHeight);
        Color foreground = null;
        if (iSpectrumFilenameColor != null) {
            foreground = g.getColor();
            g.setColor(iSpectrumFilenameColor);
        }
        g.drawString(filename, xDistance + xAdditionForFilename, yHeight - smallFontCorrection);
        if (foreground != null) {
            g.setColor(foreground);
        }
        // Restore original font.
        g.setFont(oldFont);
        int labelHeight = fm.getAscent() + 5;
        // Now mark each unit.
        for (int i = 0; i < numberTimes; i++) {
            int xLoc = (xUnit * i) + aPadding;
            g.drawLine(xLoc, this.getHeight() - aPadding, xLoc, this.getHeight() - aPadding + 3);
            BigDecimal bd = new BigDecimal(aMin + (scaleUnit * i));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            String label = bd.toString();

            if (hideDecimals) {
                label = "" + bd.intValue();
            }

            int labelWidth = fm.stringWidth(label);
            g.drawString(label, xLoc - (labelWidth / 2), this.getHeight() - aPadding + labelHeight);
        }
    }

    /**
     * This method draws tags on the Y axis.
     *
     * @param aMin  double with the minimum value for the axis.
     * @param aMax  double with the maximum value for the axis.
     * @param g Graphics object to draw on.
     * @param aYAxisHeight   int with the axis height in pixels.
     * @param aPadding  int with the amount of padding to take into account.
     */
    private void drawYTags(Graphics g, double aMin, double aMax, int aYAxisHeight, int aPadding) {
        // Font Metrics. We'll be needing these.
        FontMetrics fm = g.getFontMetrics();
        int labelHeight = fm.getAscent();
        // Find out how many tags we will have. At most, we'll have xTagCount tags, and if the resolution
        // of the screen is too small, we'll have less.
        int tagHeightEstimate = labelHeight + 10;
        int numberTimes = (aYAxisHeight / tagHeightEstimate);
        if (numberTimes > yTagCount) {
            numberTimes = yTagCount;
        } else if (numberTimes == 0) {
            numberTimes = 1;
        }
        // Calculate the graphical unit, ...
        int yUnit = aYAxisHeight / numberTimes;
        // ... as well as the scale unit.
        double delta = aMax - aMin;
        double scaleUnit = delta / numberTimes;
        iYScaleUnit = delta / aYAxisHeight;

        // Find the largest display intensity.
        BigDecimal bdLargest = new BigDecimal(aMin + (scaleUnit * (numberTimes - 1)));
        bdLargest = bdLargest.setScale(2, BigDecimal.ROUND_HALF_UP);
        String largestLabel = bdLargest.toString();

        if (hideDecimals) {
            largestLabel = "" + bdLargest.intValue();
        }

        int largestWidth = 0;
        // Old font storage.
        Font oldFont = g.getFont();
        int sizeCounter = 0;
        int margin = aPadding - 10;
        while ((largestWidth = g.getFontMetrics().stringWidth(largestLabel)) >= margin) {
            sizeCounter++;
            g.setFont(new Font(oldFont.getName(), oldFont.getStyle(), oldFont.getSize() - sizeCounter));
        }

        // Now mark each unit.
        for (int i = 0; i < numberTimes; i++) {
            int yLoc = (yUnit * i) + aPadding;
            g.drawLine(aPadding, this.getHeight() - yLoc, aPadding - 3, this.getHeight() - yLoc);
            BigDecimal bd = new BigDecimal(aMin + (scaleUnit * i));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            String label = bd.toString();

            if (hideDecimals) {
                label = "" + bd.intValue();
            }

            int labelWidth = g.getFontMetrics().stringWidth(label) + 5;
            g.drawString(label, aPadding - labelWidth, this.getHeight() - yLoc + (g.getFontMetrics().getAscent() / 2) - 1);
        }
        // Restore original font.
        g.setFont(oldFont);
    }

    /**
     * This method draws all of the peaks in the current massrange
     * on the panel.
     *
     * @param g Graphics object to draw on.
     */
    private void drawPeaks(Graphics g) {
        // Switch the color to red for the time being.
        Color originalColor = g.getColor();
        g.setColor(iSpectrumColor);

        // Cycle the masses and corresponding intensities.
        // Each peak is a line.
        // We also init an array that holds pixel coordinates for each peak.
        iMassInPixels = new int[iMasses.length];
        iIntensityInPixels = new int[iMasses.length];
        for (int i = 0; i < iMasses.length; i++) {
            double lMass = iMasses[i];
            // Only draw those masses within the ('low mass', 'high mass') window.
            if (lMass < iMassMin) {
                continue;
            } else if (lMass > iMassMax) {
                break;
            } else {
                double lIntensity = iIntensities[i];
                // Calculate pixel coordinates for mass and intensity.
                // Mass first.
                double tempDouble = (lMass - iMassMin) / iXScaleUnit;
                int temp = (int) tempDouble;
                if ((tempDouble - temp) >= 0.5) {
                    temp++;
                }
                int massPxl = temp + iXPadding;
                iMassInPixels[i] = massPxl;
                // Now intensity.
                tempDouble = (lIntensity - iIntMin) / iYScaleUnit;
                temp = (int) tempDouble;
                if ((tempDouble - temp) >= 0.5) {
                    temp++;
                }
                int intPxl = this.getHeight() - (temp + iXPadding);
                iIntensityInPixels[i] = intPxl;
                // Draw the line.
                g.drawLine(massPxl, this.getHeight() - iXPadding, massPxl, intPxl);
            }
        }
        // Change the color back to its original setting.
        g.setColor(originalColor);
    }

    /**
     * This method will highlight the specified peak in the specified color by
     * drawing a floating triangle and mass above it.
     *
     * @param aIndex    int with the index.
     * @param g Graphics object to draw on
     * @param aColor    Color to draw the highlighting in.
     * @param aComment  String with an optional comment. Can be 'null' in which case
     *                  it will be omitted.
     * @param aPixelsSpacer int that gives the vertical spacer in pixels for the highlighting.
     * @param aShowArrow boolean that indicates whether a downward-pointing arrow and dotted line
     *                           should be drawn over the peak.
     */
    private void highLight(int aIndex, Graphics g, Color aColor, String aComment, int aPixelsSpacer, boolean aShowArrow) {
        int x = iMassInPixels[aIndex];
        int y = 0;
        if (aPixelsSpacer < 0) {
            y = iTopPadding;
        } else {
            y = iIntensityInPixels[aIndex] - aPixelsSpacer;
            // Correct for absurd heights.
            if (y < iTopPadding / 3) {
                y = iTopPadding / 3;
            }
        }
        // Temporarily change the color to blue.
        Color originalColor = g.getColor();
        g.setColor(aColor);
        // Draw the triangle first, if appropriate.
        int arrowSpacer = 6;
        if (aShowArrow) {
            g.fillPolygon(new int[]{x - 3, x + 3, x},
                    new int[]{y - 6, y - 6, y - 3},
                    3);
            arrowSpacer = 9;
        }
        // Now the mass.
        // If there is any, print the comment instead of the mass.
        if (aComment != null && !aComment.trim().equals("")) {
            aComment = aComment.trim();
            g.drawString(aComment, x - g.getFontMetrics().stringWidth(aComment) / 2, y - arrowSpacer);
        } else {
            // No comment, so print the mass.
            String mass = Double.toString(iMasses[aIndex]);
            int halfWayMass = g.getFontMetrics().stringWidth(mass) / 2;
            g.drawString(mass, x - halfWayMass, y - arrowSpacer);
        }
        // If we drew above the peak, drop a dotted line.
        if (aPixelsSpacer != 0 && aShowArrow) {
            dropDottedLine(aIndex, y + 2, g);
        }
        // Restore original color.
        g.setColor(originalColor);

    }

    /**
     * This method drops a dotted line from the specified total height to the
     * top of the indicated peak.
     *
     * @param aPeakIndex    int with the index of the peak to draw the dotted line for.
     * @param aTotalHeight  int with the height (in pixels) to drop the dotted line from.
     * @param g Graphics object to draw the dotted line on.
     */
    private void dropDottedLine(int aPeakIndex, int aTotalHeight, Graphics g) {
        int x = iMassInPixels[aPeakIndex];
        int y = iIntensityInPixels[aPeakIndex];

        // Draw the dotted line.
        if ((y - aTotalHeight) > 10) {
            int start = aTotalHeight;
            while (start < y) {
                g.drawLine(x, start, x, start + 2);
                start += 7;
            }
        }
    }
}
