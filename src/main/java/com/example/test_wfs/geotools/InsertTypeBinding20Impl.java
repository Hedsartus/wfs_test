package com.example.test_wfs.geotools;

import net.opengis.wfs20.InsertType;
import net.opengis.wfs20.Wfs20Factory;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.gml3.v3_2.GML;
import org.geotools.wfs.v2_0.bindings.InsertTypeBinding;

import java.util.ArrayList;
import java.util.List;

public class InsertTypeBinding20Impl extends InsertTypeBinding {
    public InsertTypeBinding20Impl(Wfs20Factory factory) {
        super(factory);
    }

    @Override
    public List<Object[]> getProperties(Object object, XSDElementDeclaration element)
            throws Exception {
        InsertType insert = (InsertType) object;

        List<Object[]> properties = new ArrayList<>();

        for (final Object feature : insert.getAny()) {
            properties.add(new Object[]{GML.AbstractFeature, feature});
        }
        return properties;
    }
}
