package com.example.test_wfs.geotools;

import javax.xml.namespace.QName;

import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.simple.GenericGeometryEncoder;
import org.geotools.wfs.WFS;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.EncoderDelegate;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Geometry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

import net.opengis.wfs.PropertyType;
import net.opengis.wfs.WfsFactory;

public class PropertyTypeBinding11Impl extends AbstractComplexEMFBinding {

    private static final String VALUE = "Value";

    public PropertyTypeBinding11Impl(WfsFactory factory) {
        super(factory);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return WFS.PropertyType;
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        return value;
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
        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }

    @Override
    public Object getProperty(final Object object, QName name) throws Exception {
        if (VALUE.equals(name.getLocalPart())) {
            return (EncoderDelegate)
                    output -> {
                        Object value = ((PropertyType) object).getValue();

                        output.startElement(
                                WFS.NAMESPACE, VALUE, "wfs:" + VALUE, new AttributesImpl());
                        if (value instanceof Geometry) {
                            Geometry g = (Geometry) value;

                            Encoder encoder = new Encoder(new GMLConfiguration());
                            encoder.setNamespaceAware(false);
                            encoder.setOmitXMLDeclaration(false);


                            GenericGeometryEncoder geometryEncoder = new GenericGeometryEncoder(encoder);

                            NamespaceSupport namespaceSupport = new NamespaceSupport();
                            GMLWriter handler =
                                    new GMLWriter(
                                            output,
                                            namespaceSupport,
                                            6,
                                            false,
                                            true,
                                            "gml",
                                            true);
                            handler.startDocument();
                            geometryEncoder.encode(g, new AttributesImpl(), handler);
                            handler.endDocument();
                        } else {
                            String s = value.toString();
                            output.characters(s.toCharArray(), 0, s.length());
                        }
                        output.endElement(WFS.NAMESPACE, VALUE, "wfs:" + VALUE);
                    };
        }

        return super.getProperty(object, name);
    }
}
