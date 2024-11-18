package com.example.test_wfs;

import com.example.test_wfs.geotools.WFSDataStoreFactoryImpl;
//import com.github.benmanes.caffeine.cache.Cache;
//import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.experimental.UtilityClass;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.TransactionResponse;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;

import javax.xml.namespace.QName;
import java.awt.geom.NoninvertibleTransformException;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.isNull;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

@UtilityClass
public class GeotoolsXSDUtil {
    public final static FilterFactory ff = CommonFactoryFinder.getFilterFactory();
//    private final static Cache<Integer, WFSDataStore> dataStoreCache =
//            Caffeine.newBuilder()
//                    .maximumSize(1000)
//                    .expireAfterWrite(Duration.ofMinutes(2))
//                    .build();
//
//    private final static Cache<Integer, SimpleFeatureSource> sourceStoreCache =
//            Caffeine.newBuilder()
//                    .maximumSize(1000)
//                    .expireAfterWrite(Duration.ofMinutes(2))
//                    .build();
    private final static WFSDataStoreFactoryImpl dsf = new WFSDataStoreFactoryImpl();
//    private final static WFSDataStoreFactory dsf = new WFSDataStoreFactory();

    /** Формирование параметров соединения с геосервером */
    public static Map<String, Object> connectionProperties() {
        Map<String, Object> params = new HashMap<>();
        params.put(WFSDataStoreFactory.URL.getName(), "http://localhost:8383/geoserver/ows?service=wfs&REQUEST=GetCapabilities&version=1.1.0");
        params.put(WFSDataStoreFactory.PASSWORD.getName(), "geoserver");
        params.put(WFSDataStoreFactory.USERNAME.getName(), "admin");
        params.put(WFSDataStoreFactory.USE_HTTP_CONNECTION_POOLING.getName(), true);

        return params;
    }


    /** Получение из кеша WFSDataStore */
    public static WFSDataStore getDataStore() {
        try {
            return dsf.createDataStore(connectionProperties());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        return getCachedOrNew(dataStoreCache, 1, () -> {
//            try {
//                return dsf.createDataStore(connectionProperties());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }

    /** Получение из кеша SimpleFeatureSource */
    public static SimpleFeatureSource getFeatureSource() {

        try {
            return getDataStore().getFeatureSource("MO:Square");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        return getCachedOrNew(sourceStoreCache, 1, () -> {
//            try {
//                return getDataStore().getFeatureSource("MO:Square");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }

//    /** Метод получения объекта из кеша либо добавление нового объекта с возвратом */
//    private static <K, V> V getCachedOrNew(Cache<K, V> cache, K key, Supplier<V> supplier) {
//        V value = cache.getIfPresent(key);
//        if (value == null) {
//            value = supplier.get();
//            cache.put(key, value);
//        }
//        return value;
//    }

    /** Извлечение идентификатора */
    public static String getId(String featureId) {
        String[] splitId = featureId.split("\\.");

        if (splitId.length != 2 && splitId[1].isEmpty())
            throw new RuntimeException("Error parse id feature!");

        return splitId[1];
    }

//    public static boolean updateGeometry1(Geometry geometry, String sid) {
//        WFSDataStore dataStore = getDataStore();
//
//        Set<FeatureId> ids = new HashSet();
//        ids.add(ff.featureId(sid));
//        Filter idFilter = ff.id(ids);
//
//        try {
//            try (var w = dataStore.getFeatureWriter("bis:Square", idFilter, Transaction.AUTO_COMMIT)) {
//                if(w.hasNext()) {
//                    SimpleFeature f = w.next();
//                    System.out.println("FEATURE : \n"+f);
//                    f.setDefaultGeometry(geometry);
//
//                    w.write();
//                    return true;
//                }
//
//                throw new RuntimeException("EMPTY!");
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }

    public static <T> List<T> extractFeature(SimpleFeatureCollection fc, Function<SimpleFeature, T> action) {
        List<T> result = new ArrayList<>();
        try (SimpleFeatureIterator sfc = fc.features()) {
            while (sfc.hasNext()) {
                SimpleFeature sf = sfc.next();

                if (sf != null && sf.getDefaultGeometry() != null)
                    result.add(action.apply(sf));
            }
        }

        return result;
    }

    private SimpleFeature getFeature(String sid, Filter filter, SimpleFeatureCollection fc) {
            Query query = new Query();
            query.setFilter(filter);
            query.setCoordinateSystem(DefaultGeographicCRS.WGS84);

            SimpleFeature feature = extractFeature(fc, (sf) -> sf).get(0);

            if (isNull(feature))
                throw new IllegalStateException("SimpleFeature not found!");

            return feature;
    }

    public static boolean updateGeometry(Geometry geometry, String sid) {
        WFSDataStore dataStore = getDataStore();

        try {
            SimpleFeatureType sft = dataStore.getSchema("bis:Square");

            QName qName = dataStore.getRemoteTypeName(sft.getName());
            String geomColumn = dataStore.getSchema("bis:Square").getGeometryDescriptor().getName().getLocalPart();

            QName qNameCol = new QName(geomColumn);

            Filter idFilter = ff.id(Collections.singleton(ff.featureId(sid)));

            return updateTransaction(dataStore, qName, listOf(qNameCol), listOf(geometry), idFilter);
        } catch (IOException e) {
            System.out.println("ERROR : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> listOf(T obj) {
        List<T> result = new ArrayList<>();
        result.add(obj);
        return result;
    }

    public <T> Set<T> setOf(T obj) {
        Set<T> result = new HashSet<>();
        result.add(obj);
        return result;
    }

    public static FeatureId createGeometry1(Geometry geometry) throws IOException {
        WFSDataStore dataStore = getDataStore();

        SimpleFeatureType sft = dataStore.getSchema("bis:Square");
        QName qName = dataStore.getRemoteTypeName(sft.getName());


        // получение системы координат слоя
        CoordinateReferenceSystem crs = sft.getCoordinateReferenceSystem();

        // получение названия колонки с геометрией
        String geomColumn = dataStore.getSchema("bis:Square").getGeometryDescriptor().getName().getLocalPart();

        String key = UUIDUtil.sqUUID().toString();

        SimpleFeatureTypeBuilder typeBuilder = getFeatureTypeBuilder(qName, crs);
        typeBuilder.add(geomColumn, Geometry.class);

        SimpleFeatureType type = typeBuilder.buildFeatureType();

        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        featureBuilder.add(geometry);
        featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, true);
        //featureBuilder.featureUserData(Hints.PROVIDED_FID, key);

        SimpleFeature feature = featureBuilder.buildFeature(key);
        feature.getUserData().put(Hints.USE_PROVIDED_FID, true);

        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        collection.add(feature);

        SimpleFeatureSource featureSource = dataStore.getFeatureSource(sft.getName());

        System.out.println("SUPPORT : " + featureSource.getQueryCapabilities().isUseProvidedFIDSupported());



        SimpleFeatureStore store = (SimpleFeatureStore) featureSource;
        List<FeatureId> fids =  store.addFeatures(collection);

        return fids.get(0);
    }



    public static FeatureId createGeometry(Geometry geometry)
            throws IOException, FactoryException, NoninvertibleTransformException, ServiceException {
        WFSDataStore dataStore = getDataStore();

        SimpleFeatureType sft = dataStore.getSchema("bis:Square");
        QName qName = dataStore.getRemoteTypeName(sft.getName());

        // получение системы координат слоя
        CoordinateReferenceSystem crs = sft.getCoordinateReferenceSystem();

        // получение названия колонки с геометрией
//        String geomColumn = getGeomColumn(dataStore, qName);
        String geomColumn = dataStore.getSchema("bis:Square").getGeometryDescriptor().getName().getLocalPart();

        String key = UUIDUtil.sqUUID().toString();

        SimpleFeatureTypeBuilder typeBuilder = getFeatureTypeBuilder(qName, crs);
        typeBuilder.add(geomColumn, Geometry.class);



        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(typeBuilder.buildFeatureType());
        featureBuilder.add(geometry);

        SimpleFeature feature = featureBuilder.buildFeature(key);
        feature.getUserData().put(Hints.USE_PROVIDED_FID, true);


        return insertTransaction(dataStore, feature, qName);
    }

    public static SimpleFeatureTypeBuilder getFeatureTypeBuilder(QName qName, CoordinateReferenceSystem crs) {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setCRS(crs);
        typeBuilder.setNamespaceURI(qName.getNamespaceURI());
        typeBuilder.setName(qName.getLocalPart());
        return typeBuilder;
    }

    private FeatureId insertTransaction(WFSDataStore dataStore, SimpleFeature feature, QName qName) throws IOException {
        TransactionRequest transactionRequest = dataStore.getWfsClient().createTransaction();

        TransactionRequest.Insert insert = transactionRequest.createInsert(qName);
        insert.add(feature);
        transactionRequest.add(insert);
        transactionRequest.setProperty("idgen", "UseExisting");

        Function<TransactionResponse, FeatureId> action = (response) ->
                response
                        .getInsertedFids()
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Can not extract inserted feature identifier"));

        return transaction(dataStore, transactionRequest, action);
    }

    private boolean updateTransaction(WFSDataStore dataStore, QName qName, List<QName> columns, List<Object> values, Filter filter) throws IOException {
        TransactionRequest transactionRequest = dataStore.getWfsClient().createTransaction();
        TransactionRequest.Update update = transactionRequest.createUpdate(qName, columns, values, filter);
        transactionRequest.add(update);
//        transactionRequest.setProperty("encoding", GML._Geometry.getLocalPart());

        return transaction(dataStore, transactionRequest, TransactionResponse::getUpdatedCount) > 0;
    }

    public boolean deleteGeometry(String sid) {
        WFSDataStore dataStore = getDataStore();

        try {
            SimpleFeatureType sft = dataStore.getSchema("MO:Square");

            QName qName = dataStore.getRemoteTypeName(sft.getName());

            TransactionRequest transactionRequest = dataStore.getWfsClient().createTransaction();
            TransactionRequest.Delete delete = transactionRequest.createDelete(qName, ff.id(setOf(ff.featureId(sid))));
            transactionRequest.add(delete);

            return transaction(dataStore, transactionRequest, TransactionResponse::getDeleteCount) > 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /** Выполнение транзакции */
    public static <T> T transaction(WFSDataStore dataStore, TransactionRequest transactionRequest, Function<TransactionResponse, T> action) throws IOException {
        TransactionResponse response =
                dataStore
                        .getWfsClient()
                        .issueTransaction(transactionRequest);

        return action.apply(response);
    }
}
