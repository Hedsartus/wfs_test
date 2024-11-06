package com.example.test_wfs.geotools;

import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.http.HTTPClient;
import org.geotools.ows.ServiceException;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;

import java.io.IOException;
import java.util.Map;

public class WFSDataStoreFactoryImpl extends WFSDataStoreFactory {

	public WFSDataStore createDataStore(final Map<String, ?> params) throws IOException {

		final WFSConfig config = WFSConfig.fromParams(params);
		{
			String user = config.getUser();
			String password = config.getPassword();
			if (((user == null) && (password != null))
				|| ((config.getPassword() == null) && (config.getUser() != null))) {
				throw new IOException(
					"Cannot define only one of USERNAME or PASSWORD, must define both or neither");
			}
		}

		final java.net.URL capabilitiesURL = URL.lookUp(params);

		final HTTPClient http = getHttpClient(params);
		http.setTryGzip(config.isTryGZIP());
		http.setUser(config.getUser());
		http.setPassword(config.getPassword());
		int timeoutMillis = config.getTimeoutMillis();
		http.setConnectTimeout(timeoutMillis / 1000);
		http.setReadTimeout(timeoutMillis / 1000);

		// WFSClient performs version negotiation and selects the correct strategy
		WFSClientImpl wfsClient;
		try {
			wfsClient = new WFSClientImpl(capabilitiesURL, http, config);
		} catch (ServiceException e) {
			throw new IOException(e);
		}

		WFSDataStore dataStore = new WFSDataStore(wfsClient);
		// factories
		dataStore.setFilterFactory(CommonFactoryFinder.getFilterFactory(null));
		dataStore.setGeometryFactory(new GeometryFactory(PackedCoordinateSequenceFactory.DOUBLE_FACTORY));
		dataStore.setFeatureTypeFactory(new FeatureTypeFactoryImpl());
		dataStore.setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
		dataStore.setDataStoreFactory(this);
		dataStore.setNamespaceURI(config.getNamespaceOverride());


		return dataStore;
	}
}
