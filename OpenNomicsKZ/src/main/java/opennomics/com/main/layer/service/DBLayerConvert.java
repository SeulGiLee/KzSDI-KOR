package opennomics.com.main.layer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import opennomics.com.common.ConvertService;
import opennomics.com.common.qa.GeometryJSON;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.vividsolutions.jts.geom.Geometry;



/**
 * DB요청을 하기위해서 동적쿼리를 생성한다.
 * @author SG.Lee
 * @Date 2016.06
 * */
public class DBLayerConvert {

	
	String tname = "";
	
	/**
	 * 새로 요청한 레이어 정보조회를 위한 DB처리를 한다.
	 * 
	 * @author SG.LEE
	 * @data 2016.05
	 * @param infoMap - 저장요청정보
	 * @return List<Map<String,Object>>
	 */
	public JSONObject convertToGeoJSON(List<Map<String, Object>> layers){
		JSONObject obj = new JSONObject();
		if(layers!=null){
			obj=buildFeatureCollection(layers);
		}
		else
			obj=null;
		
		return obj;
	}
	
	/**
	 * 클라이언트에서 받은 레이어 정보를 GeoJSON형태로 변환한다.
	 * 
	 * @author SG.LEE
	 * @data 2016.06
	 * @param layers - 레이어정보
	 * @param tname - 테이블이름
	 * @return JSONObject
	 */
	public JSONObject convertToGeoJSON(List<Map<String, Object>> layers, String tname){
		JSONObject obj = new JSONObject();
		this.tname = tname;
		if(layers!=null){
			obj=buildFeatureCollection(layers);
		}
		else
			obj=null;
		
		return obj;
	}
	
	
	@SuppressWarnings("unchecked")
	private JSONObject buildFeatureCollection(List<Map<String, Object>> layers){
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("type", "FeatureCollection");

		for(Map<String,Object> layer : layers){
 			array.add(buildFeature(layer));
		}
		obj.put("features", array);
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject buildFeature(Map<String,Object> layer){
		JSONObject obj = new JSONObject();
		obj.put("type", "Feature");
		obj.put("geometry", buildGeometry((String) layer.get("coordinates")));
		if(tname.equals("")){
			obj.put("id", "new."+layer.get("objectid"));
		}
		else
			obj.put("id", tname + "_approval_new."+layer.get("objectid"));
		obj.put("properties", buildProperties(layer));
		return obj;
	}
	
	private JSONObject buildGeometry(String geometry) {

		Object obj = JSONValue.parse(geometry);
		JSONObject jsonObj = (JSONObject) obj;
		return jsonObj;

	}
	
	@SuppressWarnings("unchecked")
	private JSONObject buildProperties(Map<String,Object> layer) {
		JSONObject obj = new JSONObject();
		Iterator<String> keys = layer.keySet().iterator();
		while(keys.hasNext()){
			String key = keys.next();
			Object value = layer.get(key);
			if(!key.equals("geom")&&!key.equals("coordinates")){
				if(!key.equals("objectid")){
					obj.put(key, value);
				}
			}
		}
		return obj;
	}
	
	
	/**
	 * JSONObject형태의 레이어 정보를 List형태로 변환한다.
	 * 
	 * @author SG.LEE
	 * @data 2016.06
	 * @param JSONObject - 레이어정보
	 * @return List<Map<String,Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> layers(JSONObject featureCollection){
		 List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		 JSONArray features = new JSONArray();
		 
			 features = (JSONArray)featureCollection.get("features");
			 
			 for(int i=0; i<features.size();i++){
				 JSONObject feature = new JSONObject();
				 JSONObject properties = new JSONObject();
				 Map<String,Object> featureMap = new HashMap<String, Object>();
				 feature = (JSONObject) features.get(i);
				 properties = (JSONObject) feature.get("properties");
				 
				 if(properties!=null){
				 Iterator<String> keys = properties.keySet().iterator();
					 while(keys.hasNext()){
						 String key = keys.next();
						 featureMap.put(key, properties.get(key));
					 }
				 }
				 featureMap.put("geom", feature.get("geometry"));
				 list.add(featureMap);
			 }
		 return list;
	}
	
	
	/**
	 * 레이어정보로 저장 Query를 생성한다.
	 * 
	 * @author SG.LEE
	 * @data 2016.06
	 * @param layerMap - 레이어 정보
	 * @param attInfo - 레이어 속성정보
	 * @return Map<String, Object>
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public Map<String, Object> createQuery(Map<String, Object> layerMap,
			Map<String, String> attInfo) {
		Map<String, Object> requestMap = new HashMap<String, Object>();
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		ConvertService convertService = new ConvertService();
		
		Iterator<String> iters = layerMap.keySet().iterator();

		while (iters.hasNext()) {
			String key = iters.next();
			String type = (String) attInfo.get(key);
			// if (!key.equals("objectid")) {
			if(key.equals("objectid")){
				
			}
			else{
			keys.add(key);
			if (key.equals("geom")) {
				JSONObject geomJSON = new JSONObject();
				JSONObject crs = new JSONObject();
				JSONObject properties = new JSONObject();
				geomJSON = convertService.stringToJSON(layerMap.get(key).toString());

				if (geomJSON.equals(null) || geomJSON.equals("")) {
					values.add(getNullQuery());
				} else {
					properties.put("name", "EPSG:4326");
					crs.put("type", "name");
					crs.put("properties", properties);

					geomJSON.put("crs", crs);

					String geom = geomJSON.toString();
					values.add(getGeomQuery(geom));
				}
			} else {
				String value = layerMap.get(key).toString();
				if (type != null) {
					if (type.equals("String")) {
						if (value.equals(null) || value.trim().equals("")){
							values.add(getNullQuery());
						} else
							values.add(getStringQuery(value));
					} else if (type.equals("Double")) {
						if (value.equals(null) || value.trim().equals("")){
							values.add(getStringQuery("0.0"));
						} else
							values.add(getDoubleQuery(value));
					} else if (type.equals("Integer")) {
						if (value.equals(null) || value.trim().equals("")){
							values.add(getStringQuery("0"));
						} else
							values.add(getIntegerQuery(value));
					} else if(type.equals(null)) {
						values.add(getNullQuery());
					}
				}
				else
					values.add(getNullQuery());
				}
			}
		}
//		}
		requestMap.put("Key", keys);
		requestMap.put("Value", values);

		return requestMap;
	}
	
	private String getNullQuery(){
		String query = "";
		query = "null";
		return query;
	}
	
	private String getStringQuery(String value){
		String query ="";
		query = "'"+ value + "'";
		return query;
	}
	
	private String getIntegerQuery(String value){
		String query ="";
		query = "cast('"+value+"'as int)";
		return query;	
	}

	private String getDoubleQuery(String value){
		String query ="";
		query = "cast('"+value+"'as numeric)";
		return query;
	}
	
	private String getGeomQuery(String value){
		String query ="";
		query = "ST_GeomFromGeoJSON('"+value+"')";
		return query;
	}
}
