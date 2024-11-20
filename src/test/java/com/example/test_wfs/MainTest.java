package com.example.test_wfs;

import org.geotools.data.wfs.WFSDataStore;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;

import java.io.IOException;
import java.util.stream.Stream;

class MainTest {
	public static WFSDataStore dataStore;
	public static String LNAME = "MO:Square";

	@BeforeAll
	static void init() {
		try {
			dataStore = Main.getDataStore();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@ParameterizedTest
	@MethodSource("getArguments")
	void insertAndUpdateFeature(String lName, String key, Geometry geometry) throws IOException {
		String fid = Main.insertFeature(lName, key, geometry, dataStore).stream().findFirst().get();
		assertNotNull(fid);
		System.out.println("_______________ Inserted key : " + fid);

		boolean isUpdate = GeotoolsXSDUtil.updateGeometry(lName, geometry, fid, dataStore);
		assertTrue(isUpdate);
		System.out.println("_______________ Is updated : " + isUpdate);
	}


	private static Stream<Arguments> getArguments() throws ParseException {
		return Stream.of(
			Arguments.of(
				LNAME,
				UUIDUtil.sqUUID().toString(),
				Main.fromWKT("MULTIPOLYGON (((136.50452556279708 50.64947612281738, 136.50452556279708 50.790671533584394, 136.7964825044434 50.790671533584394, 136.7964825044434 50.64947612281738, 136.50452556279708 50.64947612281738)))")
			),
			Arguments.of(
				LNAME,
				UUIDUtil.sqUUID().toString(),
				Main.fromWKT("MULTIPOLYGON (((136.50452556279708 50.64947612281738, 136.50452556279708 50.790671533584394, 136.7964825044434 50.790671533584394, 136.7964825044434 50.64947612281738, 136.50452556279708 50.64947612281738)))")
			),
			Arguments.of(
				LNAME,
				UUIDUtil.sqUUID().toString(),
				Main.fromWKT("MULTIPOINT ((29.511177197395636 60.19277128384842))")
			),
			Arguments.of(
				LNAME,
				UUIDUtil.sqUUID().toString(),
				Main.fromWKT("MULTILINESTRING ((32.442966 69.405878, 32.442976 69.405862, 32.442906 69.405858, 32.442833 69.405853, 32.442826 69.405864, 32.442821 69.405872, 32.442819 69.405879, 32.442805 69.4059, 32.442783 69.405936, 32.442773 69.405954, 32.442817 69.405956, 32.442818 69.405956), (32.441541 69.405723, 32.441922 69.405751, 32.442148 69.405772, 32.442096 69.405809, 32.442109 69.405829, 32.442119 69.405837, 32.442066 69.405924, 32.442037 69.405922), (32.442019 69.405953, 32.44205 69.405956, 32.442044 69.405968, 32.442034 69.405983, 32.442017 69.406012, 32.441991 69.406008), (32.441975 69.406034, 32.442002 69.406037, 32.441989 69.40606, 32.441974 69.406085, 32.441948 69.406135, 32.44193 69.406134, 32.441919 69.406132), (32.441897 69.40617, 32.44193 69.406171, 32.441886 69.406265, 32.441854 69.406262), (32.441836 69.406299, 32.441867 69.406301, 32.441808 69.406409, 32.441779 69.406407), (32.441755 69.406449, 32.441786 69.406452, 32.441742 69.40653, 32.441681 69.406526, 32.441689 69.406513), (32.442316 69.405846, 32.442453 69.405859, 32.442477 69.405821, 32.442274 69.405805, 32.442253 69.405834), (32.442192 69.405833, 32.442213 69.405802, 32.442167 69.405796, 32.442143 69.40583, 32.442151 69.405831))")
			)
		);
	}

}
