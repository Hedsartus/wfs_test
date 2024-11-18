package com.example.test_wfs.geotools;

import net.opengis.wfs.IdentifierGenerationOptionType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.WfsFactory;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.v1_x.StrictWFS_1_x_Strategy;
import org.geotools.xsd.Configuration;
import org.opengis.feature.simple.SimpleFeature;

import java.net.URI;
import java.util.List;

public class Strategy11Impl extends StrictWFS_1_x_Strategy {

    @Override
    public Configuration getWfsConfiguration() {
        return new WFSConfiguration11Impl();
    }


    @SuppressWarnings("unchecked")
    protected InsertElementType createInsert(WfsFactory factory, TransactionRequest.Insert elem) throws Exception {
        InsertElementType insert = factory.createInsertElementType();

        insert.setIdgen(IdentifierGenerationOptionType.USE_EXISTING_LITERAL);

        String srsName = getFeatureTypeInfo(elem.getTypeName()).getDefaultSRS();
        insert.setSrsName(new URI(srsName));

        List<SimpleFeature> features = elem.getFeatures();

        insert.getFeature().addAll(features);

        return insert;
    }
}
