package svg;

import compat.ConvertToJavaCurves;
import compat.PathElement;
import org.w3c.dom.svg.SVGDocument;
import potracej.Bitmap;
import potracej.Param;
import potracej.Path;
import potracej.PoTraceJ;
import svg.utils.SVGUtils;

import javax.imageio.ImageIO;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class BitmapTracer {

	private static int DEFAULT_SCALE = 1;

	public static void main(String[] args) throws Exception {
		BufferedImage bufferedImage = ImageIO.read(new File("/Users/zhuruixian/git/potrace4j/girl.png"));
		long start = System.currentTimeMillis();
		SVGDocument svgDocument = BitmapTracer.toSVG(bufferedImage);
		System.out.println("cost:" + (System.currentTimeMillis() - start) + "ms");

		SVGUtils.saveSvgDocumentToFile(svgDocument, new File("/Users/zhuruixian/git/potrace4j/girl.svg"));
	}

	// Copy logic from MAIN about tracing - done
	// SAVE path to SVGPath - done
	// TODO : export SVG document to String
	public static SVGDocument toSVG(BufferedImage sourceImage) {
		return toSVG(sourceImage, false);
	}

	public static SVGDocument toSVG(BufferedImage sourceImage, boolean isIgnoreWhite) {
		WritableRaster raster = sourceImage.getRaster();
		int[] iarr = new int[4];
		Bitmap bmp = new Bitmap((int) (sourceImage.getWidth()), (int) (sourceImage.getHeight()));
		for (int y = 0; y < sourceImage.getHeight(); y++) {
			for (int x = 0; x < sourceImage.getWidth(); x++) {
				int[] pixel = raster.getPixel(x, y, iarr);

				int red = pixel[0];
				int green = pixel[1];
				int blue = pixel[2];
				int alpha = pixel[3];

				if (isIgnoreWhite && red >= 254 && red >= green && blue >= 254) {
					continue;
				}

				if (red + green + blue + alpha != 0) {
					bmp.put(x, y, 1);
				}
			}
		}

		//System.out.println(bmp.toDebugString());

		// Setting default (sharp) params
		Param param = new Param();
		/*param.opticurve = 0;
		param.opttolerance = 0;
		param.turdsize = 0;

		// TODO: calc alphamax (for rounded corners)
		param.alphamax = 0;*/

		return doTrace(bmp, sourceImage.getWidth(), sourceImage.getHeight(), param, DEFAULT_SCALE);
	}

	private static SVGDocument doTrace(Bitmap bmp, int width, int height, Param param, double scale) {
		PoTraceJ poTraceJ = new PoTraceJ(param);

		Path trace = poTraceJ.trace(bmp);

		ArrayList<PathElement> al = new ArrayList<PathElement>();
		ConvertToJavaCurves.convert(trace, new HashSet<ConvertToJavaCurves.Point>(), al);

		GeneralPath path = new GeneralPath();
		for (PathElement pathElement : al) {
			switch (pathElement.getType()) {
				case CLOSE_PATH:
					path.closePath();
					break;
				case LINE_TO:
					path.lineTo(pathElement.getP0x(), pathElement.getP0y());
					break;
				case MOVE_TO:
					path.moveTo(pathElement.getP0x(), pathElement.getP0y());
					break;
				case CURVE_TO:
					path.curveTo(pathElement.getP0x(), pathElement.getP0y(), pathElement.getP1x(), pathElement.getP1y(),
							pathElement.getP2x(), pathElement.getP2y());
					break;
			}
		}

		SVGDocument document = SVGUtils.createSvgDocument(width, height);
		SVGUtils.putPathToSvgDocument(document, path);
		return document;
	}
}
