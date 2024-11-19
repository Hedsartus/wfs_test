package com.example.test_wfs.geotools;

import net.opengis.wfs.IdentifierGenerationOptionType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.WfsFactory;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.v1_x.StrictWFS_1_x_Strategy;
import org.geotools.util.factory.Hints;
import org.geotools.xsd.Configuration;
import org.opengis.feature.simple.SimpleFeature;

import java.net.URI;

public class Strategy11Impl extends StrictWFS_1_x_Strategy {

    @Override
    public Configuration getWfsConfiguration() {
        return new WFSConfiguration11Impl();
    }


    @SuppressWarnings("unchecked")
    protected InsertElementType createInsert(WfsFactory factory, TransactionRequest.Insert elem) throws Exception {
        InsertElementType insert = factory.createInsertElementType();

        String srsName = getFeatureTypeInfo(elem.getTypeName()).getDefaultSRS();
        insert.setSrsName(new URI(srsName));

        SimpleFeature feature = elem.getFeatures().get(0);

        boolean useExisting =
                Boolean.TRUE.equals(feature.getUserData().get(Hints.USE_PROVIDED_FID));

        if(useExisting) {
            insert.setIdgen(IdentifierGenerationOptionType.USE_EXISTING_LITERAL);
        }

        insert.getFeature().add(feature);

        return insert;
    }
}
