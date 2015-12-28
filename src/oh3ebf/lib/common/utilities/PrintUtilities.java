/*
 **********************************************************
 * Software: common library
 *
 * Module: printing utility class
 *
 * Version: 0.1
 *
 * Licence: GPL2
 *
 * Owner: Kim Kristo
 *
 * Date creation : 2.1.2013
 *
 **********************************************************
 */
package oh3ebf.lib.common.utilities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.print.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

/**
 * A simple utility class that lets you very simply print an arbitrary
 * component. Just pass the component to the PrintUtilities.printComponent. The
 * component you want to print doesn't need a print method and doesn't have to
 * implement any interface or do anything special at all.
 * <P>
 * If you are going to be printing many times, it is marginally more efficient
 * to first do the following:
 * <PRE>
 *    PrintUtilities printHelper = new PrintUtilities(theComponent);
 * </PRE> then later do printHelper.print(). But this is a very tiny difference,
 * so in most cases just do the simpler
 * PrintUtilities.printComponent(componentToBePrinted).
 *
 * 7/99 Marty Hall, http://www.apl.jhu.edu/~hall/java/ May be freely used or
 * adapted.
 */
public class PrintUtilities implements Printable {

    private Component componentToBePrinted;

    /**
     * Creates new printutilities instance and sets component to be printed
     *
     * @param componentToBePrinted
     *
     */
    public PrintUtilities(Component componentToBePrinted) {
        /*
         DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
         PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
         //aset.add(MediaSizeName.ISO_A4);
         PrintService[] pservices = PrintServiceLookup.lookupPrintServices(flavor, aset);
         */
        this.componentToBePrinted = componentToBePrinted;
    }

    /**
     * Function prints given component
     *
     * @param c component to print
     *
     */
    public static void printComponent(Component c) {
        new PrintUtilities(c).print();
    }

    /**
     * Function executes print job
     *
     */
    public void print() {
        /*DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
         PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
         aset.add(MediaSizeName.ISO_A4);
         PrintService[] pservices = PrintServiceLookup.lookupPrintServices(flavor, aset);
         if (pservices.length > 0) {
         DocPrintJob pj = pservices[0].createPrintJob();
         // InputStreamDoc is an implementation of the Doc interface //
         Doc doc = new InputStreamDoc("test.ps", flavor);
         try {
         pj.print(doc, aset);
         } catch (PrintException e) {
         }
         }*/

        PrinterJob printJob = PrinterJob.getPrinterJob();
        /*
         PrintService[] pservices = printJob.lookupPrintServices();
         PaperSizes p = new PaperSizes(pservices[0]);
        
        
        
         PageFormat pf = printJob.defaultPage();
        
        
         MediaSize s = MediaSize.getMediaSizeForName(MediaSizeName.ISO_A4);

        
         Paper paper = pf.getPaper();
         paper.setSize(595, 842);
         paper.setImageableArea(0, 0, 595, 842);
         */
        //paper.setSize(s.getX(MediaSize.INCH), s.getY(MediaSize.INCH));
        //paper.setSize(500,500); // Large Address Dimension
        //paper.setImageableArea(0, 0, s.getX(MediaSize.INCH), s.getY(MediaSize.INCH));
        /*
         Book book = new Book();//java.awt.print.Book
         pf.setPaper(paper);
         book.append(this, pf);
         printJob.setPageable(book);
         */

        printJob.setPrintable(this);

        /*PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
         PageFormat format = printJob.pageDialog(attributes);
         if (format == null) {
         System.out.println("Cancelled");
         }
         */
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (PrinterException pe) {
                System.out.println("Error printing: " + pe);
            }
        }
    }

    /**
     * Function executes print job for given graphics
     *
     * @param g to print
     * @param pageFormat for printing
     * @param pageIndex for printing
     * @return printing status
     *
     */
    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return (NO_SUCH_PAGE);
        } else {
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            disableDoubleBuffering(componentToBePrinted);
            componentToBePrinted.paint(g2d);
            enableDoubleBuffering(componentToBePrinted);

            return (PAGE_EXISTS);
        }
    }

    /**
     * Function disables double buffering The speed and quality of printing
     * suffers dramatically if any of the containers have double buffering
     * turned on. So this turns if off globally.
     *
     * @see enableDoubleBuffering
     *
     * @param c repeint manager of component
     *
     */
    public static void disableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    /**
     * Function re-enables double buffering globally.
     *
     * @param c repeint manager of component
     *
     */
    public static void enableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }
}
