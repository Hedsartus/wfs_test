package com.example.test_wfs.geotools;

import org.geotools.wfs.bindings.*;
import org.geotools.wfs.v1_1.FeatureCollectionTypeBinding;
import org.geotools.wfs.v1_1.WFS;
import org.geotools.wfs.v1_1.WFSConfiguration;
import org.picocontainer.MutablePicoContainer;

public class WFSConfiguration11Impl extends WFSConfiguration {

    public WFSConfiguration11Impl() {


//        super.registerBindings(Map.of(WFS.PropertyType, PropertyTypeBindingImpl.class));


//        getContext().registerComponentInstance(WFS.PropertyType, PropertyTypeBindingImpl.class);
//        getDependencies().forEach(d -> {
//            d.getDependencies().forEach(dep -> dep.);
//        });
    }

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



//    @Override
//    protected void registerBindings(Map<QName, Object> bindings) {
//
//        // Types
//        //
//        // container.registerComponentImplementation(WFS.AbstractTransactionActionType,AbstractTransactionActionTypeBinding.class);
//        binding(bindings, WFS.ActionResultsType);
//        bindings.put(WFS.AllSomeType, new EnumSimpleBinding(AllSomeType.class, WFS.AllSomeType));
//        //
//        // container.registerComponentImplementation(WFS.BaseRequestType,BaseRequestTypeBinding.class);
//        binding(bindings, WFS.CreatedOrModifiedFeatureType);
//        binding(bindings, WFS.CreateStoredQueryResponseType);
//        binding(bindings, WFS.CreateStoredQueryType);
//        binding(bindings, WFS.DeleteType);
//        binding(bindings, WFS.DescribeFeatureTypeType);
//        binding(bindings, WFS.DescribeStoredQueriesResponseType);
//        binding(bindings, WFS.DescribeStoredQueriesType);
//        //
//        // container.registerComponentImplementation(WFS.ElementType,ElementTypeBinding.class);
//        //        container.registerComponentImplementation(WFS.EmptyType,EmptyTypeBinding.class);
//        bindings.put(WFS.EnvelopePropertyType, EnvelopePropertyTypeBinding.class);
//        binding(bindings, WFS.ExecutionStatusType);
//        //
//        // container.registerComponentImplementation(WFS.ExtendedDescriptionType,ExtendedDescriptionTypeBinding.class);
//        bindings.put(WFS.FeatureCollectionType, FeatureCollectionTypeBinding.class);
//        binding(bindings, WFS.FeaturesLockedType);
//        binding(bindings, WFS.FeaturesNotLockedType);
//        bindings.put(WFS.FeatureTypeListType, FeatureTypeListTypeBinding.class);
//        bindings.put(WFS.FeatureTypeType, FeatureTypeTypeBinding.class);
//        binding(bindings, WFS.GetCapabilitiesType);
//        binding(bindings, WFS.GetFeatureType);
//        binding(bindings, WFS.GetFeatureWithLockType);
//        binding(bindings, WFS.GetPropertyValueType);
//        bindings.put(WFS.InsertType, InsertTypeBinding.class);
//        binding(bindings, WFS.ListStoredQueriesResponseType);
//        binding(bindings, WFS.ListStoredQueriesType);
//        binding(bindings, WFS.LockFeatureResponseType);
//        binding(bindings, WFS.LockFeatureType);
//        bindings.put(WFS.MemberPropertyType, MemberPropertyTypeBinding.class);
//        bindings.put(WFS.MetadataURLType, MetadataURLTypeBinding.class);
//        binding(bindings, WFS.NativeType);
//        //
//        // container.registerComponentImplementation(WFS.nonNegativeIntegerOrUnknown,NonNegativeIntegerOrUnknownBinding.class);
//        //
//        // container.registerComponentImplementation(WFS.OutputFormatListType,OutputFormatListTypeBinding.class);
//        binding(bindings, WFS.ParameterExpressionType);
//        binding(bindings, WFS.ParameterType);
//        //
//        // container.registerComponentImplementation(WFS.positiveIntegerWithStar,PositiveIntegerWithStarBinding.class);
//        bindings.put(WFS.PropertyType, PropertyTypeBindingImpl.class);
//        bindings.put(WFS.QueryExpressionTextType, QueryExpressionTextTypeBinding.class);
//        bindings.put(WFS.QueryType, QueryTypeBinding.class);
//        binding(bindings, WFS.ReplaceType);
//        bindings.put(
//                WFS.ResolveValueType,
//                new EnumSimpleBinding(ResolveValueType.class, WFS.ResolveValueType));
//        bindings.put(
//                WFS.ResultTypeType,
//                new EnumSimpleBinding(ResultTypeType.class, WFS.ResultTypeType));
//        bindings.put(WFS.ReturnFeatureTypesListType, ReturnFeatureTypesListTypeBinding.class);
//        //
//        // container.registerComponentImplementation(WFS.SimpleFeatureCollectionType,SimpleFeatureCollectionTypeBinding.class);
//        //
//        // container.registerComponentImplementation(WFS.StarStringType,StarStringTypeBinding.class);
//        //
//        // container.registerComponentImplementation(WFS.StateValueType,StateValueTypeBinding.class);
//        binding(bindings, WFS.StoredQueryDescriptionType);
//        binding(bindings, WFS.StoredQueryListItemType);
//        binding(bindings, WFS.StoredQueryType);
//        binding(bindings, WFS.TransactionResponseType);
//        binding(bindings, WFS.TransactionSummaryType);
//
//        bindings.put(WFS.TransactionType, TransactionTypeBinding.class);
//        bindings.put(WFS.TupleType, TupleTypeBinding.class);
//        bindings.put(
//                WFS.UpdateActionType,
//                new EnumSimpleBinding(UpdateActionType.class, WFS.UpdateActionType));
//        binding(bindings, WFS.UpdateType);
//        bindings.put(WFS.ValueCollectionType, ValueCollectionTypeBinding.class);
//        //
//        // container.registerComponentImplementation(WFS.ValueListType,ValueListTypeBinding.class);
//        bindings.put(WFS.WFS_CapabilitiesType, WFS_CapabilitiesTypeBinding.class);
//        //        container.registerComponentImplementation(WFS._Abstract,_AbstractBinding.class);
//        //
//        // container.registerComponentImplementation(WFS._additionalObjects,_additionalObjectsBinding.class);
//        //
//        // container.registerComponentImplementation(WFS._additionalValues,_additionalValuesBinding.class);
//        //        bindings(WFS._DropStoredQuery,_DropStoredQueryBinding.class);
//        bindings.put(WFS._PropertyName, XSQNameBinding.class);
//        //        container.registerComponentImplementation(WFS._Title,_TitleBinding.class);
//        //
//        // container.registerComponentImplementation(WFS._truncatedResponse,_truncatedResponseBinding.class);
//        //
//        // container.registerComponentImplementation(WFS.FeatureTypeType_NoCRS,FeatureTypeType_NoCRSBinding.class);
//        bindings.put(WFS.PropertyType_ValueReference, ValueReferenceTypeBinding.class);
//        //
//        // container.registerComponentImplementation(WFS.WFS_CapabilitiesType_WSDL,WFS_CapabilitiesType_WSDLBinding.class);
//
//        bindings.put(WFS.ParameterType, ParameterTypeBinding.class);
//
//        bindings.put(
//                WFS.Abstract,
//                new SimpleContentComplexEMFBinding(
//                        Wfs20Factory.eINSTANCE, new QName(WFS.NAMESPACE, "AbstractType")));
//        bindings.put(
//                WFS.DropStoredQuery,
//                new ComplexEMFBinding(
//                        Wfs20Factory.eINSTANCE, WFS.DropStoredQuery, DropStoredQueryType.class));
//        bindings.put(
//                WFS.Title,
//                new SimpleContentComplexEMFBinding(
//                        Wfs20Factory.eINSTANCE, new QName(WFS.NAMESPACE, "TitleType")));
//
//        bindings.put(WFS.WFS_CapabilitiesType, WFS_CapabilitiesTypeBinding.class);
//        bindings.put(WFS.FeatureTypeListType, FeatureTypeListTypeBinding.class);
//        bindings.put(WFS.FeatureTypeType, FeatureTypeTypeBinding.class);
//    }
//
//    void binding(Map<QName, Object> bindings, QName name) {
//        bindings.put(name, new ComplexEMFBinding(Wfs20Factory.eINSTANCE, name));
//    }
}
