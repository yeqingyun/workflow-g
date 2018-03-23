/**
 * @(#) GnifUtils.java Created on 2015年1月28日
 *
 * Copyright (c) 2015 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gionee.gniflow.dto.MapFormElement;

/**
 * The class <code>GnifUtils</code>
 *
 * @author lipw
 * @version 1.0
 */
public final class MapFormEleUtils {
	
	private MapFormEleUtils(){
		
	}
	
	/**
	 * 根据MapFormElement对象的key进行分组
	 * @param srcMapFormElement
	 * @return
	 */
	private static Map<String, List<MapFormElement>> groupMapFormElement4Key(List<MapFormElement> srcMapFormElement){
		Map<String, List<MapFormElement>> map = new LinkedHashMap<String, List<MapFormElement>>();
		List<MapFormElement> list;
		for (MapFormElement formEle : srcMapFormElement) {
			if (map.containsKey(formEle.getKey())) {
				map.get(formEle.getKey()).add(formEle);
			} else {
				list = new ArrayList<MapFormElement>();
				list.add(formEle);
				map.put(formEle.getKey(), list);
			}
		}
		return map;
	}
	
	/**
	 * 根据MapFormElement对象的key_index进行分组
	 * @param srcMapFormElement
	 * @return
	 */
	private static Map<String, List<MapFormElement>> groupMapFormElement4KeyAndIndex(List<MapFormElement> srcMapFormElement){
		Map<String, List<MapFormElement>> map = new LinkedHashMap<String, List<MapFormElement>>();
		List<MapFormElement> list;
		for (MapFormElement formEle : srcMapFormElement) {
			if (map.containsKey(formEle.getKey()+"_"+formEle.getRowIndex())) {
				map.get(formEle.getKey()+"_"+formEle.getRowIndex()).add(formEle);
			} else {
				list = new ArrayList<MapFormElement>();
				list.add(formEle);
				map.put(formEle.getKey()+"_"+formEle.getRowIndex(), list);
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @param mapResult
	 * 			最终存储的结果
	 * @param tempMap
	 * 			MapFormElement对象根据key_index分组后的Map对象
	 * @param key
	 * @return
	 */
	private static Map<String, List<List<MapFormElement>>> saveToMapResult(Map<String, List<List<MapFormElement>>> mapResult, 
			Map<String, List<MapFormElement>> tempMap, String key){
		List<List<MapFormElement>> list = new ArrayList<List<MapFormElement>>(tempMap.values());
		mapResult.put(key, list);
		return mapResult;
	}
	
	/**
	 * 
	 * @param srcList
	 * 		 源List<MapFormElement>集合
	 * @return
	 * 		输出最终的处理结果
	 */
	public static Map<String, List<List<MapFormElement>>> handleMapFormEle2MapData(List<MapFormElement> srcList){
		//先根据MapFormElement的key进行分组，如测试数据：以part1up，part1down 进行分组
		//分组后Map中存储为"part1up",List<MapFormElement>，"part1down",List<MapFormElement>
		Map<String, List<MapFormElement>> map = groupMapFormElement4Key(srcList);
		
		Map<String, List<List<MapFormElement>>> mapResult = new LinkedHashMap<String, List<List<MapFormElement>>>();
		
		for (Map.Entry<String, List<MapFormElement>> entry : map.entrySet()) {
			String key = entry.getKey();
			//根据上面分组后Map中存储为"part1up",List<MapFormElement>中的List<MapFormElement>根据
			//MapFormElement的key_index进行分组
			//分组后数据形如： "part1up_1",List<MapFormElement>,"part1up_2",List<MapFormElement>
			Map<String, List<MapFormElement>> tempMap = groupMapFormElement4KeyAndIndex(entry.getValue());
			//把分组后的数据存储到最终的Map中，数据如"part1up",List<List<MapFormElement(index统一结尾的对象)>>
			mapResult = saveToMapResult(mapResult, tempMap, key);
		}
		return mapResult;
	}
	
	public static void main(String[] args) {
		
		List<MapFormElement> result = initData();
		
		
		Collections.sort(result);
//		Collections.sort(result, new Comparator<MapFormElement>(){
//
//			@Override
//			public int compare(MapFormElement o1, MapFormElement o2) {
//				if(o1.getRowIndex() == o2.getRowIndex()) { 
//		            return Integer.parseInt(o1.getColIndex()) - Integer.parseInt(o2.getColIndex()); 
//		        } else{ 
//		            return Integer.parseInt(o1.getRowIndex()) - Integer.parseInt(o2.getRowIndex()); 
//		        } 
//			}
//			
//		});
		
		Map<String, List<List<MapFormElement>>> mapResult = handleMapFormEle2MapData(result);
		
		//System.out.println(mapResult);
	}
	
	private static List<MapFormElement> initData(){
		List<MapFormElement> list = new ArrayList<MapFormElement>();
		list.add(new MapFormElement("part1up","map_part1up_KPI_1","aaa", "3","1"));
		list.add(new MapFormElement("part1up","map_part1up_weight_1","10%", "2", "1"));
		list.add(new MapFormElement("part1up","map_part1up_basicgoal_1","aaa", "1","1"));
		
		list.add(new MapFormElement("part1up","map_part1up_KPI_2","bbb","1", "2"));
		list.add(new MapFormElement("part1up","map_part1up_weight_2","12%", "2","2"));
		list.add(new MapFormElement("part1up","map_part1up_basicgoal_2","bbb", "3","2"));
		
		list.add(new MapFormElement("part1down","map_part1down_plan_1","ccc", "1","1"));
		list.add(new MapFormElement("part1down","map_part1down_weight_1","10%", "2","1"));
		list.add(new MapFormElement("part1down","map_part1down_plandate_1","ccc", "3","1"));
		
		return list;
	}
}
