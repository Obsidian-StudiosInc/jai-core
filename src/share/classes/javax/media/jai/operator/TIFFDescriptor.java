/*
 * $RCSfile: TIFFDescriptor.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.1 $
 * $Date: 2005-02-11 04:57:45 $
 * $State: Exp $
 */
package javax.media.jai.operator;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.registry.RenderedRegistryMode;

/**
 * An <code>OperationDescriptor</code> describing the "TIFF" operation.
 *
 * <p> The "TIFF" operation reads TIFF data from a <code>SeekableStream</code>.
 *
 * <p><a href="http://partners.adobe.com/asn/developer/PDFS/TN/TIFF6.pdf"> 
 * TIFF version 6.0</a> was finalized in June, 1992.  Since that time
 * there have been two technical notes extending the specification.
 * There are also a number of
 * <a href="http://home.earthlink.net/~ritter/tiff/#extensions"> 
 * TIFF Extensions</a> including
 * <a href="http://www.remotesensing.org/geotiff/geotiff.html"> 
 * GeoTIFF</a>.
 *
 * <p> The TIFF format consists of a short header that points to a
 * linked list of Image File Directories (IFDs).  An IFD is
 * essentially a list of fields.  The <code>TIFFDirectory</code> class
 * encapsulates a set of common operations performed on an IFD; an instance
 * of the <code>TIFFField</code> class corresponds to a field in an IFD.
 * Each field has numeric value or <i>tag</i>, a data type, and a byte offset
 * at which the field's data may be found.  This mechanism allows TIFF
 * files to contain multiple images, each with its own IFD, and to order
 * its contents flexibly since (apart from the header) nothing is required to
 * appear at a fixed offset.
 *
 * <p> An image generated by the "TIFF" operation will have a property
 * named "tiff_directory" the value of which will be a <code>TIFFDirectory</code>
 * corresponding to the IFD of the image.  JAI's property inheritance mechanism
 * provides a mechanism by which the field information of the IFD may be
 * made available to applications in a straightforward way.  This mechanism
 * may be utilized by setting a <code>PropertyGenerator</code> on this
 * <code>OperationDescriptor</code> which extracts <code>TIFFField</code>s
 * from the <code>TIFFDirectory</code>-valued property and returns them either
 * as <code>TIFFField</code> instances or as some user-defined object
 * initialized from a <code>TIFFField</code>.  In the latter case application
 * code would be insulated from the uncommitted <code>TIFFField</code> class.
 *
 * <p> The second parameter contains an instance of
 * <code>TIFFDecodeParam</code> to be used during the decoding.
 * It may be set to <code>null</code> in order to perform default
 * decoding, or equivalently may be omitted.
 *
 * <p> Some TIFF extensions make use of a mechanism known as "private
 * IFDs."  A private IFD is one that is not referenced by the standard
 * linked list of IFDs that starts in the file header.  To a standard
 * TIFF reader, it appears as an unreferenced area in the file.
 * However, the byte offset of the private IFD is stored as the value
 * of a private tag, allowing readers that understand the tag to
 * locate and interpret the IFD.  The "TIFF" operation may read the data
 * at an arbitrary IFD by supplying the offset of the IFD via the
 * <code>setIFDOffset()</code> method of the <code>TIFFDecodeParam</code>
 * parameter.
 *
 * <p> The third parameter specifies which page of the TIFF data to
 * read.  This permits loading of multi-page TIFF files.  The value
 * provided is zero-relative and so may be interpreted as the index
 * of the IFD after the first IFD in the stream with zero of course
 * indicating the first IFD.  The default value is zero.
 *
 * <p> All pages of a multi-page TIFF stream may also be read by doing the
 * following:
 * <pre>
 * SeekableStream s; // initialization omitted
 * ParameterBlock pb = new ParameterBlock();
 * pb.add(s);
 *
 * TIFFDecodeParam param = new TIFFDecodeParam();
 * pb.add(param);
 *
 * java.util.ArrayList images = new ArrayList();
 * long nextOffset = 0;
 * do {
 *     RenderedOp op = JAI.create("tiff", pb);
 *     images.add(op);
 *     TIFFDirectory dir = (TIFFDirectory)op.getProperty("tiff_directory");
 *     nextOffset = dir.getNextIFDOffset();
 *     if(nextOffset != 0) {
 *         param.setIFDOffset(nextOffset);
 *     }
 * } while(nextOffset != 0);
 * </pre>
 *
 * <p><b> The classes in the <code>com.sun.media.jai.codec</code>
 * package are not a committed part of the JAI API.  Future releases
 * of JAI will make use of new classes in their place.  This
 * class will change accordingly.</b>
 * 
 * <p><table border=1>
 * <caption>Resource List</caption>
 * <tr><th>Name</th>        <th>Value</th></tr>
 * <tr><td>GlobalName</td>  <td>TIFF</td></tr>
 * <tr><td>LocalName</td>   <td>TIFF</td></tr>
 * <tr><td>Vendor</td>      <td>com.sun.media.jai</td></tr>
 * <tr><td>Description</td> <td>Reads a TIFF 6.0 file.</td></tr>
 * <tr><td>DocURL</td>      <td>http://java.sun.com/products/java-media/jai/forDevelopers/jai-apidocs/javax/media/jai/operator/TIFFDescriptor.html</td></tr>
 * <tr><td>Version</td>     <td>1.0</td></tr>
 * <tr><td>arg0Desc</td>    <td>The SeekableStream to read from.</td></tr>
 * <tr><td>arg1Desc</td>    <td>The TIFFDecodeParam to use.</td></tr>
 * <tr><td>arg2Desc</td>    <td>The page to be decoded.</td></tr>
 * </table></p>
 * <p><table border=1>
 * <caption>Parameter List</caption>
 * <tr><th>Name</th>  <th>Class Type</th>
 *                    <th>Default Value</th></tr>
 * <tr><td>stream</td>  <td>com.sun.media.jai.codec.SeekableStream</td>
 *                      <td>NO_PARAMETER_DEFAULT</td>
 * <tr><td>param</td>   <td>com.sun.media.jai.codec.TIFFDecodeParam</td>
 *                      <td>null</td>
 * <tr><td>page</td>    <td>java.lang.Integer</td>
 *                      <td>0</td>
 * </table></p>
 *
 * @see com.sun.media.jai.codec.SeekableStream
 * @see com.sun.media.jai.codec.TIFFDecodeParam
 * @see com.sun.media.jai.codec.TIFFDirectory
 * @see com.sun.media.jai.codec.TIFFEncodeParam
 * @see com.sun.media.jai.codec.TIFFField
 * @see javax.media.jai.OperationDescriptor
 * @see javax.media.jai.operator.EncodeDescriptor
 */
public class TIFFDescriptor extends OperationDescriptorImpl {

    /**
     * The resource strings that provide the general documentation and
     * specify the parameter list for the "TIFF" operation.
     */
    private static final String[][] resources = {
        {"GlobalName",  "TIFF"},
        {"LocalName",   "TIFF"},
        {"Vendor",      "com.sun.media.jai"},
        {"Description", JaiI18N.getString("TIFFDescriptor0")},
        {"DocURL",      "http://java.sun.com/products/java-media/jai/forDevelopers/jai-apidocs/javax/media/jai/operator/TIFFDescriptor.html"},
        {"Version",     JaiI18N.getString("DescriptorVersion")},
        {"arg0Desc",    JaiI18N.getString("TIFFDescriptor1")},
        {"arg1Desc",    JaiI18N.getString("TIFFDescriptor2")},
        {"arg2Desc",    JaiI18N.getString("TIFFDescriptor3")}
    };

    /** The parameter names for the "TIFF" operation. */
    private static final String[] paramNames = {
        "stream", "param", "page"
    };

    /** The parameter class types for the "TIFF" operation. */
    private static final Class[] paramClasses = {
	com.sun.media.jai.codec.SeekableStream.class,
        com.sun.media.jai.codec.TIFFDecodeParam.class,
        java.lang.Integer.class
    };

    /** The parameter default values for the "TIFF" operation. */
    private static final Object[] paramDefaults = {
        NO_PARAMETER_DEFAULT, null, new Integer(0)
    };

    /** Constructor. */
    public TIFFDescriptor() {
        super(resources, 0, paramClasses, paramNames, paramDefaults);
    }


    /**
     * Reads TIFF 6.0 data from an SeekableStream.
     *
     * <p>Creates a <code>ParameterBlockJAI</code> from all
     * supplied arguments except <code>hints</code> and invokes
     * {@link JAI#create(String,ParameterBlock,RenderingHints)}.
     *
     * @see JAI
     * @see ParameterBlockJAI
     * @see RenderedOp
     *
     * @param stream The SeekableStream to read from.
     * @param param The TIFFDecodeParam to use.
     * May be <code>null</code>.
     * @param page The page to be decoded.
     * May be <code>null</code>.
     * @param hints The <code>RenderingHints</code> to use.
     * May be <code>null</code>.
     * @return The <code>RenderedOp</code> destination.
     * @throws IllegalArgumentException if <code>stream</code> is <code>null</code>.
     */
    public static RenderedOp create(SeekableStream stream,
                                    TIFFDecodeParam param,
                                    Integer page,
                                    RenderingHints hints)  {
        ParameterBlockJAI pb =
            new ParameterBlockJAI("TIFF",
                                  RenderedRegistryMode.MODE_NAME);

        pb.setParameter("stream", stream);
        pb.setParameter("param", param);
        pb.setParameter("page", page);

        return JAI.create("TIFF", pb, hints);
    }
}
