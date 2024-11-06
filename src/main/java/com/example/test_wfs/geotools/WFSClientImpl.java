package com.example.test_wfs.geotools;

import org.geotools.data.wfs.internal.*;
import org.geotools.http.HTTPClient;
import org.geotools.ows.ServiceException;

import java.io.IOException;
import java.net.URL;

public class WFSClientImpl extends WFSClient {
	public WFSClientImpl(URL capabilitiesURL, HTTPClient httpClient, WFSConfig config) throws IOException, ServiceException {
		super(capabilitiesURL, httpClient, config);
	}
	public TransactionRequest createTransaction() {

		WFSStrategy strategy = getStrategy();

		Strategy11Impl myStrategy = new Strategy11Impl();
//		Strategy20Impl myStrategy = new Strategy20Impl();

		myStrategy.setConfig(strategy.getConfig());
		myStrategy.setCapabilities(super.getCapabilities());

		return new TransactionRequest(config, myStrategy);
	}
}
