package com.example.test_wfs.geotools;

import net.opengis.wfs20.PropertyType;
import net.opengis.wfs20.Wfs20Factory;
import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.simple.GenericGeometryEncoder;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xsd.*;
import org.locationtech.jts.geom.Geometry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

import javax.xml.namespace.QName;

public class PropertyTypeBinding20Impl extends AbstractComplexEMFBinding {

    public PropertyTypeBinding20Impl(Wfs20Factory factory) {
        super(factory);
    }

    @Override
    public QName getTarget() {
        return WFS.PropertyType;
    }

    @Override
    public Class<?> getType() {
        return PropertyType.class;
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        return value;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }

    @Override
    public Object getProperty(final Object object, QName name) throws Exception {
        if (WFS.Value.equals(name)) {
            return (EncoderDelegate)
                    output -> {
                        Object value = ((PropertyType) object).getValue();

                        output.startElement(
                                WFS.NAMESPACE,
                                WFS.Value.getLocalPart(),
                                "wfs:" + WFS.Value.getLocalPart(),
                                new AttributesImpl());
                        if (value instanceof Geometry) {
                            Geometry g = (Geometry) value;
                            Encoder encoder = new Encoder(new GMLConfiguration());
                            encoder.setNamespaceAware(false);
                            encoder.setOmitXMLDeclaration(true);

                            GenericGeometryEncoder geometryEncoder = new GenericGeometryEncoder(encoder, "gml", "http://www.opengis.net/gml/3.2");

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
                            String s = value != null ? value.toString() : "";
                            output.characters(s.toCharArray(), 0, s.length());
                        }
                        output.endElement(
                                WFS.NAMESPACE,
                                WFS.Value.getLocalPart(),
                                "wfs:" + WFS.Value.getLocalPart());
                    };
        }

        return super.getProperty(object, name);
    }
}
