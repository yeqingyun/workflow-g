package com.gionee.gniflow.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gionee.gniflow.dto.MapFormElement;
import com.gionee.gniflow.web.support.AudiException;
/**
 * 提取请求中的数据，并存入对应的变量中
 * @author Administrator
 *
 */
public class RequestDataExtractUtil {
	
	public static void extractRequestData(HttpServletRequest request,Map<String, Object> formProperties,Map<String, String> attchmentMap,Boolean bool,String reqChannels){
		Map<String, String[]> parameterMap = request.getParameterMap();
		//判断页面传参是否为空，为空则抛出异常
		if(parameterMap == null || parameterMap.size() < 2){
			throw new AudiException("页面数据提交出现异常，请稍后再试");
		}
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		//遍历处理map_开头的元素，将其做类似group操作，保存到Map<String,Object>中
		List<MapFormElement> mapFormEleList = new ArrayList<MapFormElement>();
		MapFormElement mapFormEle = null;
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			if(key.indexOf("fp_reqChannels")!=-1){
				reqChannels=entry.getValue()[0];
			}
			if (key.startsWith("fp_")) {
				if (entry.getValue().length > 1) {
					formProperties.put(key.split("fp_")[1], GnifStringUtils.join(",", entry.getValue()));
				} else {
					formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
				}
			} else if (key.startsWith("attch_")) {
				String[] arrValue =  entry.getValue()[0].split("_");
				String value = entry.getValue()[0].substring(arrValue[0].length()+1);//+1将中间的连接符"_"截取掉
				attchmentMap.put(arrValue[0],value);
				bool=true;
			} else if (key.startsWith("gridJson_")){
				JSONObject jsonObject = JSON.parseObject(entry.getValue()[0]);
				List<Map> lists = JSONArray.parseArray(jsonObject.get("rows").toString(),  Map.class);
				formProperties.put(entry.getKey(), lists);
			} else if (key.startsWith("map_")) {
				mapFormEle = new MapFormElement();
				String[] mapFormKeyArr = key.split("_");
				mapFormEle.setKey(mapFormKeyArr[1]);
				mapFormEle.setName(key);
				mapFormEle.setColIndex(mapFormKeyArr[3]);
				mapFormEle.setRowIndex(mapFormKeyArr[4]);
				if (entry.getValue().length > 1) {
					mapFormEle.setValue(GnifStringUtils.join(",", entry.getValue()));
				} else {
					mapFormEle.setValue(entry.getValue()[0]);
				}
				
				if (mapFormEle != null) {
					mapFormEleList.add(mapFormEle);
				}
			}
			
		}
		//处理这个List
		if (!CollectionUtils.isEmpty(mapFormEleList)) {
			Map<String, List<List<MapFormElement>>> mapResult = MapFormEleUtils.handleMapFormEle2MapData(mapFormEleList);
			for (Map.Entry<String, List<List<MapFormElement>>> entry : mapResult.entrySet()) {
				formProperties.put(entry.getKey(), entry.getValue());
			}
		}
	}
}
