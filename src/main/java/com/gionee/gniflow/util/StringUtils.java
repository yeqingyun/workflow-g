package com.gionee.gniflow.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class StringUtils {

	public static String convert(java.sql.Clob clob) throws SQLException, IOException{
		if(clob==null)
			return null;
		char[] cs = new char[(int)clob.length()];
		clob.getCharacterStream().read(cs);
		return new String(cs);
	}
	public static Map convert(Map map) throws SQLException, IOException{
		if(map==null)
			return null;
		for(Object obj:map.keySet()){
			if(map.get(obj) instanceof java.sql.Clob){
				map.put(obj, convert((java.sql.Clob)map.get(obj)));
			}
		}
		return map;
	}
	public static List convert(List list) throws SQLException, IOException{
		if(list==null)
			return null;
		for(int i=0,im=list.size();i<im;i++){
			Object obj=list.remove(i);
			list.add(i,convert(obj));
		}
		return list;
	}
	public static Object convert(Object obj) throws SQLException, IOException{
		if(obj==null)
			return null;
		if(obj instanceof java.sql.Clob){
			return convert((java.sql.Clob)obj);
		}
		if(obj instanceof Map){
			return convert((Map)obj);
		}
		if(obj instanceof List){
			return convert((List)obj);
		}
		return obj;
	}
}
