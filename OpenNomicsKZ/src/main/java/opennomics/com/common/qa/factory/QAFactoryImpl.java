package opennomics.com.common.qa.factory;

import java.util.Iterator;
import java.util.Vector;

import opennomics.com.common.ConvertService;

import org.geotools.feature.SchemaException;
import org.json.simple.JSONArray;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class QAFactoryImpl implements QAFactory {

	// 허용범위이하 길이 오류
	@Override
	public SimpleFeature smallLength(SimpleFeature simpleFeature, double defaultLength) throws SchemaException {

		Geometry geometry = (Geometry) simpleFeature.getDefaultGeometry();

		double length = geometry.getLength();
		if (length < defaultLength) {
			ConvertService convertService = new ConvertService();
			String errFeatureID = simpleFeature.getID();

			Geometry returnGeom;

			if (geometry.getCoordinates().length > 2) {
				returnGeom = geometry.getInteriorPoint();
			} else {
				returnGeom = geometry.getInteriorPoint();
			}
			SimpleFeature returnfeature = convertService.createErrFeature(errFeatureID, returnGeom, "SmallLength");
			return returnfeature;
		} else {
			return null;
		}
	}

	// 허용범위이하 면적 오류
	@Override
	public SimpleFeature smallArea(SimpleFeature simpleFeature, double defaultArea) throws SchemaException {

		Geometry geometry = (Geometry) simpleFeature.getDefaultGeometry();

		double area = geometry.getArea();
		if (area < defaultArea) {
			ConvertService convertService = new ConvertService();
			String errFeatureID = simpleFeature.getID();
			SimpleFeature returnfeature = convertService.createErrFeature(errFeatureID, geometry, "SmallArea");
			return returnfeature;
		} else {
			return null;
		}
	}

	// 요소중복 오류
	@Override
	public Vector<SimpleFeature> entityDuplicated(SimpleFeature simpleFeatureI, SimpleFeature simpleFeatureJ) throws SchemaException {

		Vector<SimpleFeature> simpleFeatures = new Vector<SimpleFeature>();

		Geometry geometryI = (Geometry) simpleFeatureI.getDefaultGeometry();
		Geometry geometryJ = (Geometry) simpleFeatureJ.getDefaultGeometry();

		if (geometryI.equals(geometryJ)) {
			ConvertService convertService = new ConvertService();
			SimpleFeature returnfeatureI = convertService.createErrFeature(simpleFeatureI.getID(), geometryI.getInteriorPoint(), "EntityDuplicated");
			SimpleFeature returnfeatureJ = convertService.createErrFeature(simpleFeatureJ.getID(), geometryJ.getInteriorPoint(), "EntityDuplicated");

			simpleFeatures.add(returnfeatureI);
			simpleFeatures.add(returnfeatureJ);

			return simpleFeatures;
		} else {
			return null;
		}
	}

	// 단독 존재 오류 - 폴리곤
	@Override
	public Vector<SimpleFeature> selfEntity4Polygon(SimpleFeature simpleFeatureI, SimpleFeature simpleFeatureJ) throws SchemaException {

		Vector<SimpleFeature> simFeatures = new Vector<SimpleFeature>();
		ConvertService convertService = new ConvertService();

		Geometry geometryI = (Geometry) simpleFeatureI.getDefaultGeometry();
		Geometry geometryJ = (Geometry) simpleFeatureJ.getDefaultGeometry();

		if (geometryI.overlaps(geometryJ) || geometryI.within(geometryJ) || geometryI.contains(geometryJ)) {
			try {
				Geometry returnGeom = geometryJ.intersection(geometryJ);
				for (int i = 0; i < returnGeom.getNumGeometries(); i++) {
					SimpleFeature returnfeatureI = convertService.createErrFeature(simpleFeatureI.getID(), returnGeom.getGeometryN(i).getInteriorPoint(),
							"SelfEntity");
					SimpleFeature returnfeatureJ = convertService.createErrFeature(simpleFeatureJ.getID(), returnGeom.getGeometryN(i).getInteriorPoint(),
							"SelfEntity");
					simFeatures.add(returnfeatureI);
					simFeatures.add(returnfeatureJ);
				}
				return simFeatures;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	// 단독존재오류 - 라인
	@Override
	public Vector<SimpleFeature> selfEntity4Line(SimpleFeature simpleFeatureI, SimpleFeature simpleFeatureJ) throws SchemaException {

		Vector<SimpleFeature> simFeatures = new Vector<SimpleFeature>();

		Geometry geometryI = (Geometry) simpleFeatureI.getDefaultGeometry();
		Geometry geometryJ = (Geometry) simpleFeatureJ.getDefaultGeometry();

		if (geometryI.crosses(geometryJ)) {
			Geometry intersectionGeom = geometryI.intersection(geometryJ);
			Coordinate[] interCoor = intersectionGeom.getCoordinates();

			Coordinate[] geomeICoor = geometryI.getCoordinates();
			Coordinate[] geomeJCoor = geometryJ.getCoordinates();

			int intersectCount = 0;
			for (int b = 0; b < interCoor.length; b++) {
				for (int a = 0; a < geomeICoor.length; a++) {
					if (interCoor[b].equals2D(geomeICoor[a])) {
						intersectCount++;
					}
				}
				for (int a = 0; a < geomeJCoor.length; a++) {
					if (interCoor[b].equals2D(geomeJCoor[a])) {
						intersectCount++;
					}
				}
			}

			ConvertService convertService = new ConvertService();
			if (intersectCount == 0 || intersectCount % 2 != 0) {
				for (int i = 0; i < intersectionGeom.getNumGeometries(); i++) {

					SimpleFeature returnfeatureI = convertService.createErrFeature(simpleFeatureI.getID(), intersectionGeom.getGeometryN(i), "SelfEntity");
					SimpleFeature returnfeatureJ = convertService.createErrFeature(simpleFeatureJ.getID(), intersectionGeom.getGeometryN(i), "SelfEntity");

					simFeatures.add(returnfeatureI);
					simFeatures.add(returnfeatureJ);
				}
			}
		} else {
			return null;
		}
		return simFeatures;
	}

	// 중복점 오류
	@Override
	public Vector<SimpleFeature> pointDuplicated(SimpleFeature simpleFeature) throws SchemaException {

		Vector<SimpleFeature> simFeatures = new Vector<SimpleFeature>();
		Geometry geometry = (Geometry) simpleFeature.getDefaultGeometry();
		Coordinate[] coordinates = geometry.getCoordinates();

		for (int i = 0; i < coordinates.length - 1; i++) {
			if (coordinates[i].equals2D(coordinates[i + 1])) {
				Geometry returnGeom = new GeometryFactory().createPoint(coordinates[i + 1]);
				ConvertService convertService = new ConvertService();
				String errFeatureID = simpleFeature.getID();
				simFeatures.add(convertService.createErrFeature(errFeatureID, returnGeom, "PointDuplicated"));
				return simFeatures;
			}
		}
		return null;
	}

	// 등고선 교차 오류
	@Override
	public Vector<SimpleFeature> conIntersected(SimpleFeature simpleFeatureI, SimpleFeature simpleFeatureJ) throws SchemaException {

		Vector<SimpleFeature> simFeatures = new Vector<SimpleFeature>();

		Geometry geometryI = (Geometry) simpleFeatureI.getDefaultGeometry();
		Geometry geometryJ = (Geometry) simpleFeatureJ.getDefaultGeometry();

		if (geometryI.intersects(geometryJ)) {
			Geometry returnGeom = geometryJ.intersection(geometryJ);
			ConvertService convertService = new ConvertService();
			for (int i = 0; i < returnGeom.getNumGeometries(); i++) {
				SimpleFeature returnfeatureI = convertService.createErrFeature(simpleFeatureI.getID(), returnGeom.getGeometryN(i), "ConIntersected");
				SimpleFeature returnfeatureJ = convertService.createErrFeature(simpleFeatureJ.getID(), returnGeom.getGeometryN(i), "ConIntersected");
				simFeatures.add(returnfeatureI);
				simFeatures.add(returnfeatureJ);
			}
		} else {
			return null;
		}
		return simFeatures;
	}

	// 등고선 꺾임 오류
	@Override
	public Vector<SimpleFeature> conOverDegree(SimpleFeature simpleFeature, double degree) throws SchemaException {

		Vector<SimpleFeature> simFeatures = new Vector<SimpleFeature>();

		Geometry geometry = (Geometry) simpleFeature.getDefaultGeometry();
		Coordinate[] coordinates = geometry.getCoordinates();

		for (int i = 0; i < coordinates.length - 2; i++) {

			Coordinate a = coordinates[i];
			Coordinate b = coordinates[i + 1];
			Coordinate c = coordinates[i + 2];

			if (!a.equals2D(b) && !b.equals2D(c) && !c.equals2D(a)) {

				double angle = Angle.toDegrees(Angle.angleBetween(a, b, c));

				if (angle < degree) {

					ConvertService convertService = new ConvertService();
					String errFeatureID = simpleFeature.getID();

					GeometryFactory geometryFactory = new GeometryFactory();
					Point errPoint = geometryFactory.createPoint(b);

					SimpleFeature returnfeature = convertService.createErrFeature(errFeatureID, errPoint, "ConOverDegree");
					simFeatures.add(returnfeature);
				}
			}
		}
		return simFeatures;
	}

	// 등고선 끊김 오류
	@Override
	public Vector<SimpleFeature> conBreak(SimpleFeature simpleFeature) throws SchemaException {
		Vector<SimpleFeature> simFeatures = new Vector<SimpleFeature>();

		Geometry geometry = (Geometry) simpleFeature.getDefaultGeometry();
		Coordinate[] coordinates = geometry.getCoordinates();

		Coordinate start = coordinates[0];
		Coordinate end = coordinates[coordinates.length - 1];

		if (start.equals2D(end)) {

		} else {
			Coordinate[] temp = new Coordinate[] { start, end };
			ConvertService convertService = new ConvertService();
			String errFeatureID = simpleFeature.getID();

			for (int i = 0; i < temp.length; i++) {
				GeometryFactory geometryFactory = new GeometryFactory();
				Geometry returnGeom = geometryFactory.createPoint(temp[i]);
				SimpleFeature returnfeature = convertService.createErrFeature(errFeatureID, returnGeom, "ConBreak");
				simFeatures.add(returnfeature);
			}
		}
		return simFeatures;
	}

	// 속성 오류
	public SimpleFeature attributeFix(SimpleFeature simpleFeature, JSONArray attributes) throws SchemaException {

		boolean isError = false;
		if (attributes != null) {
			Iterator iterator = attributes.iterator();
			if (simpleFeature.getAttributeCount() > 1) {
				while (iterator.hasNext()) {
					String attribute = (String) iterator.next();
					for (int i = 1; i < simpleFeature.getAttributeCount(); i++) {
						String key = simpleFeature.getFeatureType().getType(i).getName().toString();
						if (key.equals(attribute)) {
							Object value = simpleFeature.getAttribute(i);
							if (value.toString().equals(" ") || value == null) {
								isError = true;
							}
						}
					}
				}
			} else {
				isError = true;
			}
		}
		if (isError) {
			ConvertService convertService = new ConvertService();
			String errFeatureID = simpleFeature.getID();
			Geometry geometry = (Geometry) simpleFeature.getDefaultGeometry();
			Geometry returnGeo = geometry.getInteriorPoint();
			SimpleFeature returnfeature = convertService.createErrFeature(errFeatureID, returnGeo, "AttributeFix");
			return returnfeature;
		} else {
			return null;
		}
	}

	@Override
	public Vector<SimpleFeature> selfEntity4Point(SimpleFeature simpleFeatureI, SimpleFeature simpleFeatureJ) throws SchemaException {

		Vector<SimpleFeature> simpleFeatures = new Vector<SimpleFeature>();

		Geometry geometryI = (Geometry) simpleFeatureI.getDefaultGeometry();
		Geometry geometryJ = (Geometry) simpleFeatureJ.getDefaultGeometry();

		if (geometryI.equals(geometryJ)) {
			ConvertService convertService = new ConvertService();
			SimpleFeature returnfeatureI = convertService.createErrFeature(simpleFeatureI.getID(), geometryI.getInteriorPoint(), "SelfEntity");
			SimpleFeature returnfeatureJ = convertService.createErrFeature(simpleFeatureJ.getID(), geometryJ.getInteriorPoint(), "SelfEntity");

			simpleFeatures.add(returnfeatureI);
			simpleFeatures.add(returnfeatureJ);

			return simpleFeatures;
		} else {
			return null;
		}
	}

	// 요소중복 오류 - 승인된데이터
	@Override
	public Vector<SimpleFeature> entityDuplicatedApp(SimpleFeature simpleFeatureI, SimpleFeature simpleFeatureJ) throws SchemaException {

		Vector<SimpleFeature> simpleFeatures = new Vector<SimpleFeature>();

		Geometry geometryI = (Geometry) simpleFeatureI.getDefaultGeometry();
		Geometry geometryJ = (Geometry) simpleFeatureJ.getDefaultGeometry();

		if (geometryI.equals(geometryJ)) {
			ConvertService convertService = new ConvertService();
			SimpleFeature returnfeatureI = convertService.createErrFeature(simpleFeatureI.getID(), geometryI.getInteriorPoint(), "EntityDuplicated");
			simpleFeatures.add(returnfeatureI);
			return simpleFeatures;
		} else {
			return null;
		}
	}

	@Override
	public SimpleFeature selfEntity4PointApp(SimpleFeature simpleFeatureI, SimpleFeature simpleFeatureJ) throws SchemaException {

		Geometry geometryI = (Geometry) simpleFeatureI.getDefaultGeometry();
		Geometry geometryJ = (Geometry) simpleFeatureJ.getDefaultGeometry();

		if (geometryI.equals(geometryJ)) {
			ConvertService convertService = new ConvertService();
			SimpleFeature returnfeatureI = convertService.createErrFeature(simpleFeatureI.getID(), geometryI.getInteriorPoint(), "SelfEntity");
			return returnfeatureI;
		} else {
			return null;
		}
	}

	@Override
	public Vector<SimpleFeature> selfEntity4LineApp(SimpleFeature simpleFeatureI, SimpleFeature simpleFeatureJ) throws SchemaException {

		Vector<SimpleFeature> simFeatures = new Vector<SimpleFeature>();

		Geometry geometryI = (Geometry) simpleFeatureI.getDefaultGeometry();
		Geometry geometryJ = (Geometry) simpleFeatureJ.getDefaultGeometry();

		if (geometryI.crosses(geometryJ)) {
			Geometry intersectionGeom = geometryI.intersection(geometryJ);
			Coordinate[] interCoor = intersectionGeom.getCoordinates();

			Coordinate[] geomeICoor = geometryI.getCoordinates();
			Coordinate[] geomeJCoor = geometryJ.getCoordinates();

			int intersectCount = 0;
			for (int b = 0; b < interCoor.length; b++) {
				for (int a = 0; a < geomeICoor.length; a++) {
					if (interCoor[b].equals2D(geomeICoor[a])) {
						intersectCount++;
					}
				}
				for (int a = 0; a < geomeJCoor.length; a++) {
					if (interCoor[b].equals2D(geomeJCoor[a])) {
						intersectCount++;
					}
				}
			}

			ConvertService convertService = new ConvertService();
			if (intersectCount == 0 || intersectCount % 2 != 0) {
				for (int i = 0; i < intersectionGeom.getNumGeometries(); i++) {

					SimpleFeature returnfeatureI = convertService.createErrFeature(simpleFeatureI.getID(), intersectionGeom.getGeometryN(i), "SelfEntity");
					simFeatures.add(returnfeatureI);
				}
			}
		} else {
			return null;
		}
		return simFeatures;
	}

	@Override
	public Vector<SimpleFeature> selfEntity4PolygonApp(SimpleFeature simpleFeatureI, SimpleFeature simpleFeatureJ) throws SchemaException {

		Vector<SimpleFeature> simFeatures = new Vector<SimpleFeature>();

		Geometry geometryI = (Geometry) simpleFeatureI.getDefaultGeometry();
		Geometry geometryJ = (Geometry) simpleFeatureJ.getDefaultGeometry();

		if (geometryI.overlaps(geometryJ) || geometryI.within(geometryJ) || geometryI.contains(geometryJ)) {
			Geometry returnGeom = geometryI.intersection(geometryJ);
			for (int i = 0; i < returnGeom.getNumGeometries(); i++) {
				ConvertService convertService = new ConvertService();
				SimpleFeature returnfeatureI = convertService.createErrFeature(simpleFeatureI.getID(), returnGeom.getGeometryN(i).getInteriorPoint(), "SelfEntity");
				simFeatures.add(returnfeatureI);
			}
			return simFeatures;
		} else {
			return null;
		}
	}

	@Override
	public Vector<SimpleFeature> conIntersectedApp(SimpleFeature simpleFeatureI, SimpleFeature simpleFeatureJ) throws SchemaException {

		Vector<SimpleFeature> simFeatures = new Vector<SimpleFeature>();

		Geometry geometryI = (Geometry) simpleFeatureI.getDefaultGeometry();
		Geometry geometryJ = (Geometry) simpleFeatureJ.getDefaultGeometry();

		if (geometryI.intersects(geometryJ)) {
			Geometry returnGeom = geometryI.intersection(geometryJ);
			ConvertService convertService = new ConvertService();
			for (int i = 0; i < returnGeom.getNumGeometries(); i++) {
				SimpleFeature returnfeatureI = convertService.createErrFeature(simpleFeatureI.getID(), returnGeom.getGeometryN(i), "ConIntersected");
				simFeatures.add(returnfeatureI);
			}
		} else {
			return null;
		}
		return simFeatures;
	}
}
