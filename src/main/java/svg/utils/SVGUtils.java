package svg.utils;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGPath;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import svg.models.VectorFigure;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.io.*;

public class SVGUtils {

	public static SVGDocument createSvgDocument(float width, float height) {
		DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
		SVGDocument document = (SVGDocument) domImpl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg",
				null);
		Element svgTag = document.getRootElement();
		svgTag.setAttribute("width", String.valueOf(width));
		svgTag.setAttribute("height", String.valueOf(height));
		return document;
	}

	public static void putPathToSvgDocument(SVGDocument document, GeneralPath path) {
		putPathToSvgDocument(document, new VectorFigure(path, null, Color.BLACK));
	}

	public static void putPathToSvgDocument(SVGDocument document, VectorFigure figure) {
		if (figure.getNonStrokingColor() == null) {
			return;
		}

		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);

		SVGPath svgPath = new SVGPath(ctx);
		Element svgElement = svgPath.toSVG(figure.getFigurePath());

		svgElement.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, getRgbColorString(figure.getNonStrokingColor()));

		// Stroke check
		Color strokingColor = figure.getStrokingColor();
		if (strokingColor != null) {
			svgElement.setAttribute(SVGConstants.SVG_STROKE_ATTRIBUTE, getRgbColorString(strokingColor));
			svgElement.setAttribute(SVGConstants.SVG_STROKE_WIDTH_ATTRIBUTE, String.valueOf(figure.getStrokeWidth()));
			svgElement.setAttribute(SVGConstants.SVG_STROKE_LINECAP_ATTRIBUTE, figure.getLineCap());
			svgElement.setAttribute(SVGConstants.SVG_STROKE_LINEJOIN_ATTRIBUTE, figure.getLineJoin());

			String dashArray = figure.getDashArray();
			if (dashArray != null) {
				svgElement.setAttribute(SVGConstants.SVG_STROKE_DASHARRAY_ATTRIBUTE, dashArray);
			}
		}
		document.getRootElement().appendChild(svgElement);
	}

	private static String getRgbColorString(Color color) {
		return String.format("rgb(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue());
	}

	public static void saveSvgDocumentToFile(SVGDocument document, File file)
			throws FileNotFoundException, IOException {
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
		try (Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) {
			svgGenerator.stream(document.getDocumentElement(), out);
		}
	}

	/**
	 * 文档转字符串
	 *
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public static String toString(SVGDocument document) throws Exception {
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (Writer out = new OutputStreamWriter(bos, "UTF-8")) {
			svgGenerator.stream(document.getDocumentElement(), out);
		}

		return new String(bos.toByteArray());
	}
}
