/*
 * $RCSfile: RescaleCRIF.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.1 $
 * $Date: 2005-02-11 04:56:41 $
 * $State: Exp $
 */
package com.sun.media.jai.opimage;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.CRIFImpl;
import javax.media.jai.ImageLayout;
import java.util.Map;

/**
 * A <code>CRIF</code> supporting the "Rescale" operation in the rendered
 * and renderable image layers.
 *
 * @see javax.media.jai.operator.RescaleDescriptor
 * @see RescaleOpImage
 *
 *
 * @since EA3
 */
public class RescaleCRIF extends CRIFImpl {

    /** Constructor. */
    public RescaleCRIF() {
        super("rescale");
    }

    /**
     * Creates a new instance of <code>RescaleOpImage</code> in the
     * rendered layer.
     *
     * @param args   The source image and the constants (slope, y-intercept).
     * @param hints  Optionally contains destination image layout.
     */
    public RenderedImage create(ParameterBlock args,
                                RenderingHints renderHints) {
        // Get ImageLayout from renderHints if any.
        ImageLayout layout = RIFUtil.getImageLayoutHint(renderHints);
        

        return new RescaleOpImage(args.getRenderedSource(0),
                                  renderHints,
                                  layout,
                                  (double[])args.getObjectParameter(0),
                                  (double[])args.getObjectParameter(1));
    }
}
