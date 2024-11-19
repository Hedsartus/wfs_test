package com.example.test_wfs;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.geotools.coverage.util.FeatureUtilities;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.util.XRectangle2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateFilter;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;


@Slf4j
@UtilityClass
public class GeoUtil {
	public static final String ERROR_WKT_MESSAGE = "Wrong WKT!";
	public static final String ERROR_CRS_MESSAGE = "Wrong CRS!";
	public static GeometryFactory geometryFactory = new GeometryFactory();

	public static MathTransform getTransform(String crs) throws FactoryException {
		return getMathTransform(DefaultGeographicCRS.WGS84, extractCRS(crs));
	}

	public static MathTransform getTransform(CoordinateReferenceSystem crs) {
		return getMathTransform(DefaultGeographicCRS.WGS84, crs);
	}


	public static MathTransform getMathTransform(CoordinateReferenceSystem source, CoordinateReferenceSystem target) {
		try {
			return CRS.findMathTransform(source, target);
		} catch (FactoryException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static MathTransform getMathTransform(String crsSource, CoordinateReferenceSystem target) {
		try {
			return CRS.findMathTransform(extractCRS(crsSource), target);
		} catch (FactoryException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public CoordinateReferenceSystem extractCRS(String crsStr) throws FactoryException {
		return CRS.decode(crsStr);
	}

	public CoordinateReferenceSystem parseWKT(String wkt) throws FactoryException {
		return CRS.parseWKT(wkt);
	}

	public static Geometry getTransformGeometry(Geometry geometry, MathTransform transform) {
		try {
			return JTS.transform(geometry, transform);
		} catch (TransformException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static class InvertCoordinateFilter implements CoordinateFilter {
		public void filter(Coordinate coord) {
			double oldX = coord.x;
			coord.x = coord.y;
			coord.y = oldX;
		}
	}

}
