package com.example.test_wfs.geotools;

import net.opengis.wfs20.AbstractTransactionActionType;
import net.opengis.wfs20.InsertType;
import net.opengis.wfs20.Wfs20Factory;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.v2_0.StrictWFS_2_0_Strategy;
import org.geotools.xsd.Configuration;
import org.opengis.feature.simple.SimpleFeature;

import java.util.List;

public class Strategy20Impl extends StrictWFS_2_0_Strategy {

    @Override
    public Configuration getWfsConfiguration() {
        return new WFSConfiguration20Impl();
    }


    protected AbstractTransactionActionType createInsert(Wfs20Factory factory, TransactionRequest.Insert elem)
            throws Exception {
        InsertType insert = factory.createInsertType();

        String srsName = getFeatureTypeInfo(elem.getTypeName()).getDefaultSRS();
        insert.setSrsName(srsName);

        List<SimpleFeature> features = elem.getFeatures();

        insert.getAny().addAll(features);

        return insert;
    }
}
