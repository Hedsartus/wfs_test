package com.example.test_wfs.geotools;

import org.geotools.wfs.bindings.*;
import org.geotools.wfs.v1_1.FeatureCollectionTypeBinding;
import org.geotools.wfs.v1_1.WFS;
import org.geotools.wfs.v1_1.WFSConfiguration;
import org.picocontainer.MutablePicoContainer;

public class WFSConfiguration11Impl extends WFSConfiguration {

    @Override
    protected void configureBindings(MutablePicoContainer container) {
        super.configureBindings(container);

        container.registerComponentImplementation(
                WFS.TransactionResponseType, TransactionResponseTypeBinding.class);
        container.registerComponentImplementation(
                WFS.InsertResultsType, InsertResultsTypeBinding.class);
        container.registerComponentImplementation(
                WFS.TransactionResultsType, TransactionResultsTypeBinding.class);
        container.registerComponentImplementation(
                WFS.LockFeatureResponseType, LockFeatureResponseTypeBinding.class);
        container.registerComponentImplementation(WFS.OperationsType, OperationsTypeBinding.class);
        container.registerComponentImplementation(WFS.PropertyType, PropertyTypeBinding11Impl.class);

        // override feature collection binding
        container.registerComponentImplementation(
                WFS.FeatureCollectionType, FeatureCollectionTypeBinding.class);
    }

}
