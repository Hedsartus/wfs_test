//package com.example.test_wfs;
//
//
//import org.locationtech.jts.geom.Coordinate;
//import org.locationtech.jts.geom.Geometry;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.locationtech.jts.geom.LinearRing;
//import org.locationtech.jts.geom.Polygon;
//import org.locationtech.jts.geom.PrecisionModel;
//import org.locationtech.jts.geom.impl.CoordinateArraySequence;
//import org.opengis.filter.identity.FeatureId;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class Controller {
//    @GetMapping(value = "/add")
//    public FeatureId insert() {
//        try {
//            return GeotoolsXSDUtil.createGeometry(getGeometry());
//        } catch ( Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @GetMapping(value = "/edit/{sid}")
//    public boolean edit(@PathVariable String sid) {
//        return GeotoolsXSDUtil.updateGeometry(getGeometry(), sid);
//    }
//
//    @GetMapping(value = "/del/{sid}")
//    public boolean del(@PathVariable String sid) {
//        return GeotoolsXSDUtil.deleteGeometry(sid);
//    }
//
//    public static Geometry getGeometry() {
//        PrecisionModel pm = new PrecisionModel();
//        GeometryFactory gf = new GeometryFactory(pm, 4326);
//        CoordinateArraySequence cas =
//                new CoordinateArraySequence(new Coordinate[]{
//                        new Coordinate(59.93954262728195, 30.262776196255125),
//                        new Coordinate(59.93958105131431, 30.262907623301622),
//                        new Coordinate(59.9395526213441, 30.262941029017526 ),
//                        new Coordinate(59.93953962130284, 30.262956789606722 ),
//                        new Coordinate(59.939488541678394, 30.263027460562363),
//                        new Coordinate(59.93944742258056, 30.262897264629483 ),
//                        new Coordinate(59.93954262728195, 30.262776196255125)});
//
//        LinearRing lr = new LinearRing(cas, gf);
//        return new Polygon(lr, null, gf);
//    }
//}
