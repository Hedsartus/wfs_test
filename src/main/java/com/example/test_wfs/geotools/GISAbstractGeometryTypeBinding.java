package com.example.test_wfs.geotools;

import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.JTS;
import org.geotools.gml2.SrsSyntax;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.gml3.GML;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.gml3.bindings.GML3ParsingUtils;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Geometry;

import javax.xml.namespace.QName;

public class GISAbstractGeometryTypeBinding extends AbstractComplexBinding {
    Configuration config;
    SrsSyntax srsSyntax;

    public GISAbstractGeometryTypeBinding(Configuration config, SrsSyntax srsSyntax) {
        this.config = config;
        this.srsSyntax = srsSyntax;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    public void setSrsSyntax(SrsSyntax srsSyntax) {
        this.srsSyntax = srsSyntax;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return GML.AbstractGeometryType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return Geometry.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return value;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        Geometry geometry = (Geometry) object;

//        if ("srsName".equals(name.getLocalPart())) {
//            CoordinateReferenceSystem crs = JTS.getCRS(geometry);
//            if (crs != null) {
//                return GML3EncodingUtils.toURI(crs, srsSyntax);
//            }
//        }
//
//        if ("srsDimension".equals(name.getLocalPart())) {
//            return GML2EncodingUtils.getGeometryDimension(geometry, config);
//        }

        // FIXME: should be gml:id, but which GML?
        // Refactor bindings or introduce a new one for GML 3.2
        if ("id".equals(name.getLocalPart())) {
            return GML3EncodingUtils.getID(geometry);
        }


        if ("uomLabels".equals(name.getLocalPart())) {
            return GML3EncodingUtils.getUomLabels(geometry);
        }

        if ("axisLabels".equals(name.getLocalPart())) {
            return GML3EncodingUtils.getAxisLabels(geometry);
        }

        return null;
    }
}