/*
 * $RCSfile: MlibErode3SquareOpImage.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.1 $
 * $Date: 2005-02-11 04:55:55 $
 * $State: Exp $
 */ 
package com.sun.media.jai.mlib;
import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import javax.media.jai.AreaOpImage;
import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.KernelJAI;
import javax.media.jai.OpImage;
import java.util.Map;
import com.sun.medialib.mlib.*;
// import com.sun.media.jai.test.OpImageTester;

/**
 * An OpImage class to perform erosion on a source image
 * for the specific case when the kernel is a 3x3 square shape -
 * with all 1s for binary and 0s for gray image and 
 * the key position in the middle,
 * using mediaLib, of course :-).
 *
 * @see javax.media.jai.operator.ErodeDescriptor
 * @see KernelJAI
 */
final class MlibErode3SquareOpImage extends AreaOpImage {

    /**
     * Creates a MlibErode3SquareOpImage given the image source
     * The image dimensions are
     * derived from the source image.  The tile grid layout,
     * SampleModel, and ColorModel may optionally be specified by an
     * ImageLayout object.
     *
     * @param source a RenderedImage.
     * @param extender a BorderExtender, or null.

     *        or null.  If null, a default cache will be used.
     * @param layout an ImageLayout optionally containing the tile grid layout,
     *        SampleModel, and ColorModel, or null.
     */
    public MlibErode3SquareOpImage(RenderedImage source,
                                  BorderExtender extender,
                                  Map config,
                                  ImageLayout layout
                                  ) {
	super(source,
              layout,
              config,
              true,
              extender,
              1, //kernel.getLeftPadding(),
              1, //kernel.getRightPadding(),
              1, //kernel.getTopPadding(),
              1  //kernel.getBottomPadding()
	      );
    }


    /**
     * Performs dilation on a specified rectangle. The sources are
     * cobbled.
     *
     * @param sources an array of source Rasters, guaranteed to provide all
     *                necessary source data for computing the output.
     * @param dest a WritableRaster tile containing the area to be computed.
     * @param destRect the rectangle within dest to be processed.
     */
    protected void computeRect(Raster[] sources,
                               WritableRaster dest,
                               Rectangle destRect) {

        Raster source = sources[0];
        Rectangle srcRect = mapDestRect(destRect, 0);

        int formatTag = MediaLibAccessor.findCompatibleTag(sources,dest);

        MediaLibAccessor srcAccessor =
            new MediaLibAccessor(source,srcRect,formatTag,true);
        MediaLibAccessor dstAccessor =
            new MediaLibAccessor(dest,destRect,formatTag,true);
        int numBands = getSampleModel().getNumBands();


        mediaLibImage[] srcML = srcAccessor.getMediaLibImages();
        mediaLibImage[] dstML = dstAccessor.getMediaLibImages();
        for (int i = 0; i < dstML.length; i++) {
            switch (dstAccessor.getDataType()) {
            case DataBuffer.TYPE_BYTE:
            case DataBuffer.TYPE_USHORT:
            case DataBuffer.TYPE_SHORT:
            case DataBuffer.TYPE_INT:
                Image.Erode8(dstML[i], srcML[i]);
                break;
            case DataBuffer.TYPE_FLOAT:
            case DataBuffer.TYPE_DOUBLE:
                Image.Erode8_Fp(dstML[i], srcML[i]);
                break;
            default:
                String className = this.getClass().getName();
                throw new RuntimeException(JaiI18N.getString("Generic2"));
            }
        }
 
        if (dstAccessor.isDataCopy()) {
            dstAccessor.copyDataToRaster();
        }
    }

//     public static OpImage createTestImage(OpImageTester oit) {
//         float data[] = {0.05f,0.10f,0.05f,
//                         0.10f,0.40f,0.10f,
//                         0.05f,0.10f,0.05f};
//         KernelJAI kJAI = new KernelJAI(3,3,1,1,data);
//         return new MlibErode3SquareOpImage(oit.getSource(), null, null,
//                                           new ImageLayout(oit.getSource()));
//     }
 
//     public static void main (String args[]) {
//         String classname = "com.sun.media.jai.mlib.MlibErode3SquareOpImage";
//         OpImageTester.performDiagnostics(classname,args);
//     }
}
