package com.example.test_wfs;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.impl.WFSDataAccessFactory;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.TransactionResponse;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.identity.FeatureId;

import com.example.test_wfs.geotools.WFSDataStoreFactoryImpl;


public class Main {

	public static void main(String[] args) throws IOException, ParseException {
		WFSDataStore dataStore = getDataStore();

		System.out.println(
			insertFeature(
				"te:districtForEdit",
				null,
				fromWKT("MULTIPOLYGON (((136.50452556279708 50.64947612281738, 136.50452556279708 50.790671533584394, 136.7964825044434 50.790671533584394, 136.7964825044434 50.64947612281738, 136.50452556279708 50.64947612281738)))"),
				dataStore
			));

		System.out.println(
			insertFeature(
				"MO:Square",
				UUIDUtil.sqUUID().toString(),
				fromWKT("MULTIPOLYGON (((136.50452556279708 50.64947612281738, 136.50452556279708 50.790671533584394, 136.7964825044434 50.790671533584394, 136.7964825044434 50.64947612281738, 136.50452556279708 50.64947612281738)))"),
				dataStore
			));
	}

	public static Collection<String> insertFeature ( String lName, String id, Geometry geom, WFSDataStore dataStore ) throws IOException {
		SimpleFeatureType sft = dataStore.getSchema(lName);

		geom = GeoUtil.transform(
			geom, 
			DefaultGeographicCRS.WGS84, 
			sft.getCoordinateReferenceSystem());

		QName qName = dataStore.getRemoteTypeName(sft.getName());

		String geomColumn = sft.getGeometryDescriptor()
			.getName()
			.getLocalPart();

		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setCRS(sft.getCoordinateReferenceSystem());
		typeBuilder.setNamespaceURI(qName.getNamespaceURI());
		typeBuilder.setName(qName.getLocalPart());

		typeBuilder.add(geomColumn, geom.getClass());

		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(typeBuilder.buildFeatureType());
		featureBuilder.add(geom);

		SimpleFeature feature = featureBuilder.buildFeature(id);

		if ( StringUtils.isNotBlank(id) )
			feature.getUserData()
				.put(Hints.USE_PROVIDED_FID, true);

		TransactionRequest transactionRequest = dataStore.getWfsClient()
			.createTransaction();

		TransactionRequest.Insert insert = transactionRequest.createInsert(qName);
		insert.add(feature);
		transactionRequest.add(insert);

		TransactionResponse response = dataStore.getWfsClient()
			.issueTransaction(transactionRequest);

		List<String> result = response.getInsertedFids()
			.stream()
			.map(Main::extractId)
			.filter(StringUtils::isNotBlank)
			.collect(Collectors.toList());

		if ( CollectionUtils.isEmpty(result) )
			throw new IllegalStateException("No result");

		return result;
	}
	
	public static String extractId ( FeatureId fid ) {
		if ( fid == null )
			return null;
		
		String rid = fid.getID();
		
		if (StringUtils.contains(rid, "."))
			rid = rid.substring(StringUtils.indexOf(rid, ".") + 1);

		return rid;
	}

	public static WFSDataStore getDataStore() throws IOException {
		WFSDataStoreFactoryImpl dsf = new WFSDataStoreFactoryImpl();

		return dsf.createDataStore(
			new HashMap<String, Object>() {
				private static final long serialVersionUID = 1L;
				{
					put(WFSDataAccessFactory.URL.getName(), "http://localhost:8083/geoserver/ows?service=wfs&request=GetCapabilities&version=1.1.0");
					put(WFSDataAccessFactory.PASSWORD.getName(), "Cbytahby5%");
					put(WFSDataAccessFactory.USERNAME.getName(), "admin");
					put(WFSDataAccessFactory.USE_HTTP_CONNECTION_POOLING.getName(), true);
				}
			});
	}

	public static Geometry fromWKT(String wkt) throws ParseException {
		WKTReader reader = new WKTReader();
		return reader.read(wkt);
	}


//	public static void main(String[] args) throws Exception {
//		FilterFactory ff = GeotoolsXSDUtil.ff;
//
//		WFSDataStore dataStore = GeotoolsXSDUtil.getDataStore();
//
//		SimpleFeatureType sft = dataStore.getSchema("bis:Square");
//
//		QName qName = dataStore.getRemoteTypeName(sft.getName());
//		String geomColumn = GeotoolsXSDUtil.getGeomColumn(dataStore, qName);
//		// получение системы координат слоя
//		var crs = sft.getCoordinateReferenceSystem();
//
//		//QName qNameCol = new QName(GML.Version.GML3.name(), geomColumn);
//		Filter idFilter = ff.id(ff.featureId("7196237a-d16d-41e0-b089-06d9d10cdb8c"));
//
//
//
//		SimpleFeatureTypeBuilder typeBuilder = GeotoolsXSDUtil.getFeatureTypeBuilder(qName, crs);
//		typeBuilder.add(geomColumn, Geometry.class);
//
//		String key = UUIDUtil.sqUUID().toString();
//
//		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(typeBuilder.buildFeatureType());
//		featureBuilder.add(getGeometry());
//		//featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, Boolean.TRUE);
//		//featureBuilder.featureUserData(Hints.PROVIDED_FID, key);
//
//		SimpleFeature feature = featureBuilder.buildFeature(key);
//
//		try (OutputStream out = new ByteArrayOutputStream()) {
//			org.geotools.gml3.GMLConfiguration configuration = new GMLConfiguration();
//			WFSConfiguration config = new WFSConfiguration();
//
//
////			TransactionRequest transactionRequest = dataStore.getWfsClient().createTransaction();
////			TransactionRequest.Update update = transactionRequest.createUpdate(qName, List.of(qNameCol), List.of(getGeometry()), idFilter);
////			transactionRequest.add(update);
//
////			TransactionRequest transactionRequest = dataStore.getWfsClient().createTransaction();
////			TransactionRequest.Insert insert = transactionRequest.createInsert(qName);
////			insert.add(feature);
////			transactionRequest.add(insert);
//
//
//			//SimpleFeature simpleFeature = new SimpleFeatureImpl();
//
//			StrictWFS_1_x_Strategy strategy = new StrictWFS_1_x_Strategy(new Version("1.1.0"));
//
//			strategy.cr
//
//			WfsFactory factory = WfsFactory.eINSTANCE;
//			InsertElementType insert = factory.createInsertElementType();
//			insert.setIdgen(IdentifierGenerationOptionType.USE_EXISTING_LITERAL);
//			insert.getFeature().add(feature);
//
//			Encoder encoder = new Encoder(config);
//			encoder.encode(insert, WFS.TransactionResults, out);
//			System.out.println("GEOMETRY : \n" + prettyFormat(out.toString(), 2));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

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


//	public static void main(String[] args) {
//
//		try (OutputStream out = new ByteArrayOutputStream()) {
//
//			CustomGMLConfiguration config = new CustomGMLConfiguration();
//
//			Geometry g = getGeometry();
//
//			Encoder encoder = new Encoder(config);
//
//			encoder.setNamespaceAware(false);
//
////			System.out.println("______: " + encoder.getSchema().getSchemaForSchemaQNamePrefix());
//			//System.out.println("Schema prefix : " + encoder.getNamespaces().declarePrefix("gml", "http://www.opengis.net/gml"));
//
//
//			encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove("sch");
//			encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove("xmlns:gml");
//			encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove("xmlns:xs");
//			encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove("xs");
//			encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove("xsd");
//			encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove("xlink");
//			encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap().remove(null);
//
//			System.out.println(encoder.getSchema().getSchema().getQNamePrefixToNamespaceMap());
//			//gml -> http://www.opengis.net/gml
//
//			encoder.getNamespaces().declarePrefix("gml", "http://www.opengis.net/gml");
//
//
//			encoder.encode(g, GML._Geometry, out);
//
//
//			System.out.println("GEOMETRY : \n" + prettyFormat(out.toString(), 1));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public static void main(String[] args) throws Exception {
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Encoder encoder = new Encoder(new GMLConfiguration());
//
//
//		GenericGeometryEncoder geometryEncoder = new GenericGeometryEncoder(encoder, "gml", "http://www.opengis.net/gml/3.2");
//
//		// create the document serializer
//		SAXTransformerFactory txFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
//		TransformerHandler xmls = txFactory.newTransformerHandler();
//
//		xmls.setResult(new StreamResult(out));
//
//		GMLWriter handler =
//				new GMLWriter(
//						xmls,
//						encoder.getNamespaces(),
//						6,
//						false,
//						false,
//						"gml",
//						false);
//		handler.startDocument();
//		geometryEncoder.encode(getGeometry(), new AttributesImpl(), handler);
//		handler.endDocument();
//
//		System.out.println("GEOMETRY : \n" + prettyFormat(out.toString(), 2));
//	}

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
