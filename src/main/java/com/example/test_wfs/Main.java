package com.example.test_wfs;


import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;


public class Main {
//    public static void main(String[] args) throws Exception {
//        FilterFactory ff = GeotoolsXSDUtil.ff;
//
//        WFSDataStore dataStore = GeotoolsXSDUtil.getDataStore();
//
//        SimpleFeatureType sft = dataStore.getSchema("bis:Square");
//
//        QName qName = dataStore.getRemoteTypeName(sft.getName());
//        String geomColumn = GeotoolsXSDUtil.getGeomColumn(dataStore, qName);
//        // получение системы координат слоя
//        var crs = sft.getCoordinateReferenceSystem();
//
//        //QName qNameCol = new QName(GML.Version.GML3.name(), geomColumn);
//        Filter idFilter = ff.id(ff.featureId("7196237a-d16d-41e0-b089-06d9d10cdb8c"));
//
//
//
//        SimpleFeatureTypeBuilder typeBuilder = GeotoolsXSDUtil.getFeatureTypeBuilder(qName, crs);
//        typeBuilder.add(geomColumn, Geometry.class);
//
//        String key = UUIDUtil.sqUUID().toString();
//
//        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(typeBuilder.buildFeatureType());
//        featureBuilder.add(getGeometry());
//        //featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, Boolean.TRUE);
//        //featureBuilder.featureUserData(Hints.PROVIDED_FID, key);
//
//        SimpleFeature feature = featureBuilder.buildFeature(key);
//
//        try (OutputStream out = new ByteArrayOutputStream()) {
//            org.geotools.gml3.GMLConfiguration configuration = new GMLConfiguration();
//            WFSConfiguration config = new WFSConfiguration();
//
//
////            TransactionRequest transactionRequest = dataStore.getWfsClient().createTransaction();
////            TransactionRequest.Update update = transactionRequest.createUpdate(qName, List.of(qNameCol), List.of(getGeometry()), idFilter);
////            transactionRequest.add(update);
//
////            TransactionRequest transactionRequest = dataStore.getWfsClient().createTransaction();
////            TransactionRequest.Insert insert = transactionRequest.createInsert(qName);
////            insert.add(feature);
////            transactionRequest.add(insert);
//
//
//            //SimpleFeature simpleFeature = new SimpleFeatureImpl();
//
//            StrictWFS_1_x_Strategy strategy = new StrictWFS_1_x_Strategy(new Version("1.1.0"));
//
//            strategy.cr
//
//            WfsFactory factory = WfsFactory.eINSTANCE;
//            InsertElementType insert = factory.createInsertElementType();
//            insert.setIdgen(IdentifierGenerationOptionType.USE_EXISTING_LITERAL);
//            insert.getFeature().add(feature);
//
//            Encoder encoder = new Encoder(config);
//            encoder.encode(insert, WFS.TransactionResults, out);
//            System.out.println("GEOMETRY : \n" + prettyFormat(out.toString(), 2));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static Geometry getGeometry() {
        PrecisionModel pm = new PrecisionModel();
        GeometryFactory gf = new GeometryFactory(pm, 4326);
        CoordinateArraySequence cas =
                new CoordinateArraySequence(new Coordinate[]{
                        new Coordinate(59.93954262728195, 30.262776196255125),
                        new Coordinate(59.93958105131431, 30.262907623301622),
                        new Coordinate(59.9395526213441, 30.262941029017526),
                        new Coordinate(59.93953962130284, 30.262956789606722),
                        new Coordinate(59.939488541678394, 30.263027460562363),
                        new Coordinate(59.93944742258056, 30.262897264629483),
                        new Coordinate(59.93954262728195, 30.262776196255125)});

        LinearRing lr = new LinearRing(cas, gf);
        return new Polygon(lr, null, gf);
    }


//    public static void main(String[] args) {
//
//        try (OutputStream out = new ByteArrayOutputStream()) {
//
//            CustomGMLConfiguration config = new CustomGMLConfiguration();
//
//            Geometry g = getGeometry();
//
//            Encoder encoder = new Encoder(config);
//
//            encoder.setNamespaceAware(false);
//
////            System.out.println("______: " + encoder.getSchema().getSchemaForSchemaQNamePrefix());
//            //System.out.println("Schema prefix : " + encoder.getNamespaces().declarePrefix("gml", "http://www.opengis.net/gml"));
//
//
//            encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove("sch");
//            encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove("xmlns:gml");
//            encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove("xmlns:xs");
//            encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove("xs");
//            encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove("xsd");
//            encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove("xlink");
//            encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove(null);
//
//            System.out.println(encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap());
//            //gml -> http://www.opengis.net/gml
//
//            encoder.getNamespaces().declarePrefix("gml", "http://www.opengis.net/gml");
//
//
//            encoder.encode(g, GML._Geometry, out);
//
//
//            System.out.println("GEOMETRY : \n" + prettyFormat(out.toString(), 1));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public static void main(String[] args) throws Exception {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        Encoder encoder = new Encoder(new GMLConfiguration());
//
//
//        GenericGeometryEncoder geometryEncoder = new GenericGeometryEncoder(encoder, "gml", "http://www.opengis.net/gml/3.2");
//
//        // create the document serializer
//        SAXTransformerFactory txFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
//        TransformerHandler xmls = txFactory.newTransformerHandler();
//
//        xmls.setResult(new StreamResult(out));
//
//        GMLWriter handler =
//                new GMLWriter(
//                        xmls,
//                        encoder.getNamespaces(),
//                        6,
//                        false,
//                        false,
//                        "gml",
//                        false);
//        handler.startDocument();
//        geometryEncoder.encode(getGeometry(), new AttributesImpl(), handler);
//        handler.endDocument();
//
//        System.out.println("GEOMETRY : \n" + prettyFormat(out.toString(), 2));
//    }

    public static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }
}
