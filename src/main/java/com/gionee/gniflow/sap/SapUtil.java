package com.gionee.gniflow.sap;

import com.gionee.gniflow.sap.rfc.RfcManager;
import com.gionee.gniflow.web.easyui.ComboBoxData;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SapUtil {
	public   Map<String,String> readSapInfoMaps(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception{
		HashMap<String, String> hashmap= new   HashMap<String, String>(); 
		
		JCoFunction function = RfcManager.getFunction(functionName);
		if (function == null){
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		}
		JCoParameterList input = function.getImportParameterList();
		
		
		if(inputMap!=null&&inputMap.size()>0){
		    Iterator i = inputMap.entrySet().iterator();
		    while(i.hasNext()) {
		    	Map.Entry me = (Map.Entry) i.next();
		    	//System.out.println(me.getKey() + ":"+me.getValue());
		    	input.setValue(me.getKey().toString(), me.getValue().toString());
		    	}
		}
		
	
		RfcManager.execute(function);
			
		
			
			if(exportTableName!=null&&!"".equals(exportTableName.trim())){
				// 从Function中取Table参数
				JCoParameterList tabput = function.getTableParameterList();
				JCoTable table = tabput.getTable(exportTableName);
				if(table.getNumRows()>0){
				if(exportVars!=null&&exportVars.length>0){
				    for(int x=0;x<exportVars.length;x++) {
				    	table.firstRow();
				    	String keylist="";
				    	String val=exportVars[x].toString().trim();
				    	for(int i = 0 ; i < table.getNumRows() ;i++,table.nextRow()){
				    		keylist+=table.getString(val)+"@";
						}
				    	hashmap.put(val,keylist);
				    	}
				}
			   }
			}
			
			if(hashmap!=null&&hashmap.size()>0){
			    Iterator i = hashmap.entrySet().iterator();
			    while(i.hasNext()) {
			    	Map.Entry me = (Map.Entry) i.next();
//			    	System.out.println(me.getKey().toString()+"===="+ me.getValue().toString());
			    }
			}
			return hashmap;	
	}
	
	//输出列表含有多个字段
	public   List<ComboBoxData> readSapInfoArray(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception{
		List<ComboBoxData> comlist=null;
		
		JCoFunction function = RfcManager.getFunction(functionName);
		if (function == null){
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		}
		JCoParameterList input = function.getImportParameterList();
		
		
		if(inputMap!=null&&inputMap.size()>0){
		    Iterator i = inputMap.entrySet().iterator();
		    while(i.hasNext()) {
		    	Map.Entry me = (Map.Entry) i.next();
		    	//System.out.println(me.getKey() + ":"+me.getValue());
		    	input.setValue(me.getKey().toString(), me.getValue().toString());
		    	}
		}
		
	
		RfcManager.execute(function);
			
			if(exportTableName!=null&&!"".equals(exportTableName.trim())){
				// 从Function中取Table参数
				JCoParameterList tabput = function.getTableParameterList();
				JCoTable table = tabput.getTable(exportTableName);
				String keylist="";
				comlist=new ArrayList<ComboBoxData>();
				ComboBoxData firstcom=new ComboBoxData();
				firstcom.setId("titletext");
				firstcom.setText("等级组---等级水平---基本工资标识---金额---底薪标识---金额");
	    		comlist.add(firstcom);
				if(table.getNumRows()>0){
					for(int i = 0 ; i < table.getNumRows() ;i++,table.nextRow()){
						keylist="";
				    if(exportVars!=null&&exportVars.length>0){
				    for(int x=0;x<exportVars.length;x++) {
				    	if(x<exportVars.length-1){
				    		keylist+=table.getString(exportVars[x].toString().trim())+"---";
				    	}else{
				    		keylist+=table.getString(exportVars[x].toString().trim());
				    	}
						}
					ComboBoxData com=new ComboBoxData();
		    		com.setId((i+1)+"");
		    		com.setText(keylist);
		    		comlist.add(com);
				    	}
				
				  }
			   }
			}
			
			if(comlist!=null&&comlist.size()>0){
				for (int a=0;a<comlist.size();a++){
					//System.out.println(comlist.get(a).getId()+":"+comlist.get(a).getText());
				}
			}
			
			return comlist;	
	}
	
	
	public      List<ComboBoxData>  readSapOptionsForJson(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception{
		HashMap<String, String> hashmap= new   HashMap<String, String>(); 
		String keylist="";
		List<ComboBoxData> comlist=null;
		comlist=new ArrayList<ComboBoxData>();
		JCoFunction function = RfcManager.getFunction(functionName);
		if (function == null){
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		}
		JCoParameterList input = function.getImportParameterList();
		
		
		if(inputMap!=null&&inputMap.size()>0){
		    Iterator i = inputMap.entrySet().iterator();
		    while(i.hasNext()) {
		    	Map.Entry me = (Map.Entry) i.next();
		    	//System.out.println(me.getKey() + ":"+me.getValue());
		    	input.setValue(me.getKey().toString(), me.getValue().toString());
		    	}
		}
		
	
		RfcManager.execute(function);
			
//			if(exportTableName!=null&&!"".equals(exportTableName.trim())){
//				// 从Function中取Table参数
//				JCoParameterList tabput = function.getTableParameterList();
//				JCoTable table = tabput.getTable(exportTableName);
//                List<ComboBoxData> comlist=new ArrayList<ComboBoxData>();
//				if(table.getNumRows()>0){
//				
//				if(code!=null&&!"".equals(code.trim())&&describe!=null&&!"".equals(describe.trim())){
//				    	for(int i = 0 ; i < table.getNumRows() ;i++,table.nextRow()){
//				    		ComboBoxData com=new ComboBoxData();
//				    		com.setId(table.getString(code));
//				    		com.setText(table.getString(describe));
//				    		comlist.add(com);
//						}
//				    	
//				    	jsonStr=JSON.toJSON(comlist);
//				    	}
//				}
//			   }
			
		
		if(exportTableName!=null&&!"".equals(exportTableName.trim())){
			// 从Function中取Table参数
			JCoParameterList tabput = function.getTableParameterList();
			JCoTable table = tabput.getTable(exportTableName);
			keylist="";
			
			if(table.getNumRows()>0){
//				keylist+="{\"sapobj\":[";
				
				for(int i = 0 ; i < table.getNumRows() ;i++,table.nextRow()){
					keylist="{";
				
			    if(exportVars!=null&&exportVars.length>0){
			    	
			      for(int x=0;x<exportVars.length;x++) {
			    	  
			    	if(x<exportVars.length-1){
			    		keylist+="\""+exportVars[x].toString().trim()+"\":\""+table.getString(exportVars[x].toString().trim())+"\",";
			    	}else{
			    		keylist+="\""+exportVars[x].toString().trim()+"\":\""+table.getString(exportVars[x].toString().trim())+"\"}";
			    	    }
					  }
			        ComboBoxData com=new ComboBoxData();
		    		com.setId((i+1)+"");
		    		com.setText(keylist);
		    		comlist.add(com);
			    	}
			    
//				if(i < table.getNumRows()-1){
//					 keylist+="},";
//				}else{
//					 keylist+="}";
//				}
			  }
//				keylist+="]}";
		   }
		
		   }
		
           //System.out.println("sapobj---"+keylist);
			return comlist;	
	}
	
	
	//输出列表只含有类似key,value的键值对
	public   List<ComboBoxData> readSapOptionsForList(String functionName,Map<String,String> inputMap,String exportTableName,String code,String describe) throws Exception{
		HashMap<String, String> hashmap= new   HashMap<String, String>(); 
		List<ComboBoxData> comlist=null;
		JCoFunction function = RfcManager.getFunction(functionName);
		if (function == null){
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		}
		JCoParameterList input = function.getImportParameterList();
		
		
		if(inputMap!=null&&inputMap.size()>0){
		    Iterator i = inputMap.entrySet().iterator();
		    while(i.hasNext()) {
		    	Map.Entry me = (Map.Entry) i.next();
//		    	System.out.println(me.getKey() + ":"+me.getValue());
		    	input.setValue(me.getKey().toString(), me.getValue().toString());
		    	}
		}
		
	
		RfcManager.execute(function);
			
			if(exportTableName!=null&&!"".equals(exportTableName.trim())){
				// 从Function中取Table参数
				JCoParameterList tabput = function.getTableParameterList();
				JCoTable table = tabput.getTable(exportTableName);
                comlist=new ArrayList<ComboBoxData>();
				if(table.getNumRows()>0){
				if(code!=null&&!"".equals(code.trim())&&describe!=null&&!"".equals(describe.trim())){
				    	for(int i = 0 ; i < table.getNumRows() ;i++,table.nextRow()){
				    		ComboBoxData com=new ComboBoxData();
				    		com.setId(table.getString(code));
				    		com.setText(table.getString(describe));
				    		comlist.add(com);
						}
				    	
				    	}
				}
			   }
			
			
			
			return comlist;	
	}
	
	//输出列表只含有类似key,value的键值对 专为岗位显示key-value
		public   List<ComboBoxData> readSapOptionsForList4Job(String functionName,Map<String,String> inputMap,String exportTableName,String code,String describe) throws Exception{
			HashMap<String, String> hashmap= new   HashMap<String, String>(); 
			List<ComboBoxData> comlist=null;
			JCoFunction function = RfcManager.getFunction(functionName);
			if (function == null){
				throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
			}
			JCoParameterList input = function.getImportParameterList();
			
			
			if(inputMap!=null&&inputMap.size()>0){
			    Iterator i = inputMap.entrySet().iterator();
			    while(i.hasNext()) {
			    	Map.Entry me = (Map.Entry) i.next();
//			    	System.out.println(me.getKey() + ":"+me.getValue());
			    	input.setValue(me.getKey().toString(), me.getValue().toString());
			    	}
			}
			
		
			RfcManager.execute(function);
				
				if(exportTableName!=null&&!"".equals(exportTableName.trim())){
					// 从Function中取Table参数
					JCoParameterList tabput = function.getTableParameterList();
					JCoTable table = tabput.getTable(exportTableName);
	                comlist=new ArrayList<ComboBoxData>();
					if(table.getNumRows()>0){
					if(code!=null&&!"".equals(code.trim())&&describe!=null&&!"".equals(describe.trim())){
					    	for(int i = 0 ; i < table.getNumRows() ;i++,table.nextRow()){
					    		ComboBoxData com=new ComboBoxData();
					    		com.setId(table.getString(code));
					    		com.setText(table.getString(code)+"-"+table.getString(describe));
					    		comlist.add(com);
							}
					    	
					    	}
					}
				   }
				
				
				
				return comlist;	
		}
		
	public  String writeToSap(String functionName,Map<String,String> inputMap) throws Exception{
		String[] strs=new String[2]; 
			
		JCoFunction function = RfcManager.getFunction(functionName);
		if (function == null){
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		}
		JCoParameterList input = function.getImportParameterList();
			
		if(inputMap!=null&&inputMap.size()>0){
		    Iterator i = inputMap.entrySet().iterator();
		    while(i.hasNext()) {
		    	Map.Entry me = (Map.Entry) i.next();
		    	input.setValue(me.getKey().toString().trim(), me.getValue().toString().trim());
		    	//System.out.println(me.getKey().toString().trim()+":-----"+me.getValue().toString().trim());
		    }
		}
		
	
		RfcManager.execute(function);
		JCoParameterList output=function.getExportParameterList();
		    String message=output.getString("MESSAGE");
		    String status=output.getString("SUBRC");
		    strs[0]=message;
		    strs[1]=status;
		    //System.out.println(message+"@@"+status);
			return strs[0];	
	}
	//返回SAP状态跟消息
	public  String writeToSapReturnStatusAndMessage(String functionName,Map<String,String> inputMap) throws Exception{
		String[] strs=new String[2]; 
			
		JCoFunction function = RfcManager.getFunction(functionName);
		if (function == null){
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		}
		JCoParameterList input = function.getImportParameterList();
			
		if(inputMap!=null&&inputMap.size()>0){
		    Iterator i = inputMap.entrySet().iterator();
		    while(i.hasNext()) {
		    	Map.Entry me = (Map.Entry) i.next();
		    	input.setValue(me.getKey().toString().trim(), String.valueOf(me.getValue()).trim());
		    	//System.out.println(me.getKey().toString().trim()+":-----"+me.getValue().toString().trim());
		    }
		}
		
	
		RfcManager.execute(function);
		JCoParameterList output=function.getExportParameterList();
		    String message=output.getString("MESSAGE");
		    String status=output.getString("SUBRC");
		    strs[0]=message;
		    strs[1]=status;
		    //System.out.println(message+"@@"+status);
			return strs[1]+strs[0];	
	}
	
	//非生产物资从SAP获取凭证号
	public    String writeToSapWithInputTableAndBanfn(String functionName,Map<String,String> inputMap,String inputTable,List<Map<String,String>> inputTableList) throws Exception{
		String[] strs=new String[3]; 
			
		JCoFunction function = RfcManager.getFunction(functionName);
		if (function == null){
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		}
		JCoParameterList input = function.getImportParameterList();
			
		if(inputMap!=null&&inputMap.size()>0){
		    Iterator i = inputMap.entrySet().iterator();
		    while(i.hasNext()) {
		    	Map.Entry me = (Map.Entry) i.next();
		    	input.setValue(me.getKey().toString().trim(), me.getValue().toString().trim());
		    }
		}
		
	
		if(inputTable!=null&&!"".equals(inputTable.trim())){
			// 从Function中取Table参数
			JCoParameterList tabput = function.getTableParameterList();
			JCoTable table = tabput.getTable(inputTable);
			for(Map<String,String> inputTableMap:inputTableList){
					table.appendRow();
				    Iterator iter = inputTableMap.entrySet().iterator();
				    while(iter.hasNext()) {
				    	Map.Entry entry = (Map.Entry) iter.next();
				    	//System.out.println(entry.getKey().toString().trim()+":"+entry.getValue().toString().trim());
				    	table.setValue(entry.getKey().toString().trim(), entry.getValue().toString().trim());
				    }
				}
			}
			
		
	
		strs[0]="";
		strs[1]="";
		strs[2]="";
		RfcManager.execute(function);
		JCoParameterList output=function.getExportParameterList();
		JCoTable outTable=function.getTableParameterList().getTable("ACK_MESSAGE");
		if(output!=null){
		    String banfn=output.getString("I_BANFN");
		    strs[0]=banfn;
		}
		int count=0;
		if("".equals(strs[0].trim())){
			strs[1]=outTable.getString("TYPE");
			StringBuilder sb = new StringBuilder();
			do{
				sb.append(++count+"."+outTable.getString("MESSAGE")).append(";");
			}while(outTable.nextRow());
			strs[2]=sb.toString();
		}
			return strs[0]+"@"+strs[1]+" @"+strs[2];	
	}
	//检查非生产物资的数据是否符合SAP要求
	public    String checkSapWithInputTable(String functionName,Map<String,String> inputMap,String inputTable,Map<String,String> inputTableMap) throws Exception{
		String[] strs=new String[1]; 
			
		JCoFunction function = RfcManager.getFunction(functionName);
		if (function == null){
			throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
		}
		JCoParameterList input = function.getImportParameterList();
			
		if(inputMap!=null&&inputMap.size()>0){
		    Iterator i = inputMap.entrySet().iterator();
		    while(i.hasNext()) {
		    	Map.Entry me = (Map.Entry) i.next();
		    	input.setValue(me.getKey().toString().trim(), me.getValue().toString().trim());
		    }
		}
		
	
		if(inputTable!=null&&!"".equals(inputTable.trim())){
			// 从Function中取Table参数
			JCoParameterList tabput = function.getTableParameterList();
			JCoTable table = tabput.getTable(inputTable);
			//for(Map<String,String> inputTableMap:inputTableList){
					table.appendRow();
				    Iterator iter = inputTableMap.entrySet().iterator();
				    while(iter.hasNext()) {
				    	Map.Entry entry = (Map.Entry) iter.next();
				    	//System.out.println(entry.getKey().toString().trim()+":"+entry.getValue().toString().trim());
				    	table.setValue(entry.getKey().toString().trim(), entry.getValue().toString().trim());
				    }
				//}
			}
			
		
	
		strs[0]="";
		
		RfcManager.execute(function);
		//JCoParameterList output=function.getExportParameterList();
		JCoTable outTable=function.getTableParameterList().getTable("ACK_MESSAGE");
		int count=0;
		if(outTable!=null&&outTable.getNumRows()>0){
			StringBuilder sb = new StringBuilder();
			do{
				sb.append(++count+"."+outTable.getString("MESSAGE")).append(";");
			}while(outTable.nextRow());
			strs[0]=sb.toString();
		}
			return strs[0];	
	}
	
	//输出列表含有多个字段
		public    String readEmpOrgInfo(String functionName,String inputTableName,String empId,String exportTableName,String[] exportVars) throws Exception{
			List<ComboBoxData> comlist=null;
			String keylist="";
			JCoFunction function = RfcManager.getFunction(functionName);
			if (function == null){
				throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
			}
			JCoParameterList input = function.getImportParameterList();
			input.setValue("I_DATUM",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			
			JCoParameterList tab = function.getTableParameterList();
			JCoTable table1 = tab.getTable(inputTableName);
			table1.appendRow();
			table1.setValue( "PERNR",empId.trim());
			
			RfcManager.execute(function);
				
				if(exportTableName!=null&&!"".equals(exportTableName.trim())){
					// 从Function中取Table参数
					JCoParameterList tabput = function.getTableParameterList();
					JCoTable table = tabput.getTable(exportTableName);
					
					comlist=new ArrayList<ComboBoxData>();
					ComboBoxData firstcom=new ComboBoxData();
		    		comlist.add(firstcom);
					if(table.getNumRows()>0){
							keylist="";
					    if(exportVars!=null&&exportVars.length>0){
					    for(int x=0;x<exportVars.length;x++) {
					    	if(x<exportVars.length-1){
					    		keylist+=table.getString(exportVars[x].toString().trim())+"---";
					    	}else{
					    		keylist+=table.getString(exportVars[x].toString().trim());
					    	}
							}
					    	}
					
					  }
				   }
				
				//System.out.println("keylist--"+keylist);
				return keylist;	
		}
		
	
		
		//获取SAP单行数据
		public    String readSapPersonInfo(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception{
			List<ComboBoxData> comlist=null;
			String keylist="";
			JCoFunction function = RfcManager.getFunction(functionName);
			if (function == null){
				throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
			}
			JCoParameterList input = function.getImportParameterList();
			
			
			if(inputMap!=null&&inputMap.size()>0){
			    Iterator i = inputMap.entrySet().iterator();
			    while(i.hasNext()) {
			    	Map.Entry me = (Map.Entry) i.next();
			    	//System.out.println(me.getKey() + ":"+me.getValue());
			    	input.setValue(me.getKey().toString(), me.getValue().toString());
			    	}
			}
			
		
			RfcManager.execute(function);
				
				if(exportTableName!=null&&!"".equals(exportTableName.trim())){
					// 从Function中取Table参数
					JCoParameterList tabput = function.getTableParameterList();
					JCoTable table = tabput.getTable(exportTableName);
					keylist="";
					
					if(table.getNumRows()>0){
							keylist="";
					    if(exportVars!=null&&exportVars.length>0){
					    for(int x=0;x<exportVars.length;x++) {
					    	if(x<exportVars.length-1){
					    		keylist+=table.getString(exportVars[x].toString().trim())+"---";
					    	}else{
					    		keylist+=table.getString(exportVars[x].toString().trim());
					    	}
							}
					    	}
					
					  }
				   }
				
				
			//System.out.println("keylist----"+keylist);
				
				return keylist;	
		}
		
		
		public   String readSapEmpInfoForJson(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception{
			HashMap<String, String> hashmap= new   HashMap<String, String>(); 
			String keylist="";
			JCoFunction function = RfcManager.getFunction(functionName);
			if (function == null){
				throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
			}
			JCoParameterList input = function.getImportParameterList();
			
			
			if(inputMap!=null&&inputMap.size()>0){
			    Iterator i = inputMap.entrySet().iterator();
			    while(i.hasNext()) {
			    	Map.Entry me = (Map.Entry) i.next();
			    	//System.out.println(me.getKey() + ":"+me.getValue());
			    	input.setValue(me.getKey().toString(), me.getValue().toString());
			    	}
			}
			
		
			RfcManager.execute(function);
			
			if(exportTableName!=null&&!"".equals(exportTableName.trim())){
				// 从Function中取Table参数
				JCoParameterList tabput = function.getTableParameterList();
				JCoTable table = tabput.getTable(exportTableName);
				keylist="";
				
				if(table.getNumRows()>0){
						keylist+="{";
				    if(exportVars!=null&&exportVars.length>0){
				    for(int x=0;x<exportVars.length;x++) {
				    	if(x<exportVars.length-1){
				    		keylist+="\""+exportVars[x].toString().trim()+"\":\""+table.getString(exportVars[x].toString().trim())+"\",";
				    	}else{
				    		keylist+="\""+exportVars[x].toString().trim()+"\":\""+table.getString(exportVars[x].toString().trim())+"\"";
				    	    }
						  }
				    	}
				    keylist+="}";
				  }
			   }
			
		
	//System.out.println("keylist---"+keylist);
				return keylist;	
		}
		
		public   String readSapAssetsInfoForJson(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception{
			HashMap<String, String> hashmap= new   HashMap<String, String>(); 
			String keylist="";
			JCoFunction function = RfcManager.getFunction(functionName);
			if (function == null){
				throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
			}
			JCoParameterList input = function.getImportParameterList();
			
			
			if(inputMap!=null&&inputMap.size()>0){
			    Iterator i = inputMap.entrySet().iterator();
			    while(i.hasNext()) {
			    	Map.Entry me = (Map.Entry) i.next();
			    	//System.out.println(me.getKey() + ":"+me.getValue());
			    	input.setValue(me.getKey().toString(), me.getValue().toString());
			    	}
			}
			
		
			RfcManager.execute(function);
			
			if(exportTableName!=null&&!"".equals(exportTableName.trim())){
				// 从Function中取Table参数
				JCoParameterList tabput = function.getTableParameterList();
				JCoTable table = tabput.getTable(exportTableName);
				keylist="";
				
				if(table.getNumRows()>0){
						keylist+="{";
				    if(exportVars!=null&&exportVars.length>0){
				    for(int x=0;x<exportVars.length;x++) {
				    	if(x<exportVars.length-1){
				    		keylist+="\""+exportVars[x].toString().trim()+"\":\""+table.getString(exportVars[x].toString().trim()).replaceAll("\"", "”")+"\",";
				    	}else{
				    		keylist+="\""+exportVars[x].toString().trim()+"\":\""+table.getString(exportVars[x].toString().trim()).replaceAll("\"", "”")+"\"";
				    	    }
						  }
				    	}
				    keylist+="}";
				  }
			   }
			
		
	//System.out.println("keylist---"+keylist);
				return keylist;	
		}
		
		public String readSapStockInfoForJson(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws JCoException{
			HashMap<String, String> hashmap= new   HashMap<String, String>(); 
			String keylist="";
			JCoFunction function = RfcManager.getFunction(functionName);
			//JCoDestination jcod=JCoDestinationManager.getDestination("SapService");
			//JCoFunction function=jcod.getRepository().getFunction(functionName);
			if (function == null){
				throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
			}
			JCoParameterList input = function.getImportParameterList();
			
			
			if(inputMap!=null&&inputMap.size()>0){
			    Iterator i = inputMap.entrySet().iterator();
			    while(i.hasNext()) {
			    	Map.Entry me = (Map.Entry) i.next();
			    	//System.out.println(me.getKey() + ":"+me.getValue());
			    	input.setValue(me.getKey().toString(), me.getValue().toString());
			    	}
			}
			
		
			RfcManager.execute(function);
			
			if(exportTableName!=null&&!"".equals(exportTableName.trim())){
				// 从Function中取Table参数
				JCoStructure table = function.getExportParameterList().getStructure(exportTableName);
				//JCoTable table = tabput.getTable(exportTableName);
				keylist="";
				
				//if(table.getNumRows()>0){
						keylist+="{";
				    if(exportVars!=null&&exportVars.length>0){
				    for(int x=0;x<exportVars.length;x++) {
				    	if(x<exportVars.length-1){
				    		keylist+="\""+exportVars[x].toString().trim()+"\":\""+table.getString(exportVars[x].toString().trim())+"\",";
				    	}else{
				    		keylist+="\""+exportVars[x].toString().trim()+"\":\""+table.getString(exportVars[x].toString().trim())+"\"";
				    	    }
						  }
				    	}
				    keylist+="}";
				  }
			   //}
			
		
				//System.out.println("keylist---"+keylist);
				return keylist;	
		}
		
		public    String writeToSapWithInputTable(String functionName,Map<String,String> inputMap,String inputTable,Map<String,String> inputTableMap) throws Exception{
			String[] strs=new String[2]; 
				
			JCoFunction function = RfcManager.getFunction(functionName);
			if (function == null){
				throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
			}
			JCoParameterList input = function.getImportParameterList();
				
			if(inputMap!=null&&inputMap.size()>0){
			    Iterator i = inputMap.entrySet().iterator();
			    while(i.hasNext()) {
			    	Map.Entry me = (Map.Entry) i.next();
			    	input.setValue(me.getKey().toString().trim(), me.getValue().toString().trim());
			    }
			}
			
		
			if(inputTable!=null&&!"".equals(inputTable.trim())){
				// 从Function中取Table参数
				JCoParameterList tabput = function.getTableParameterList();
				JCoTable table = tabput.getTable(inputTable);
				table.appendRow();
				if(inputTableMap!=null&&inputTableMap.size()>0){
				    Iterator iter = inputTableMap.entrySet().iterator();
				    while(iter.hasNext()) {
				    	Map.Entry entry = (Map.Entry) iter.next();
				    	//System.out.println(entry.getKey().toString().trim()+":"+entry.getValue().toString().trim());
				    	table.setValue(entry.getKey().toString().trim(), entry.getValue().toString().trim());
				    }
				}
				
			
			}
		
			strs[0]="";
			strs[1]="";
			RfcManager.execute(function);
			JCoParameterList output=function.getExportParameterList();
			if(output!=null){
			    String message=output.getString("MESSAGE");
			    String status=output.getString("SUBRC");
			    strs[0]=message;
			    strs[1]=status;
			    //System.out.println(message+"@@"+status);
			}
				return strs[0];	
		}
		
		//返回SAP的消息跟状态
		public    String writeToSapWithInputTableStatusAndMessage(String functionName,Map<String,String> inputMap,String inputTable,Map<String,String> inputTableMap) throws Exception{
			String[] strs=new String[2]; 
				
			JCoFunction function = RfcManager.getFunction(functionName);
			if (function == null){
				throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
			}
			JCoParameterList input = function.getImportParameterList();
				
			if(inputMap!=null&&inputMap.size()>0){
			    Iterator i = inputMap.entrySet().iterator();
			    while(i.hasNext()) {
			    	Map.Entry me = (Map.Entry) i.next();
			    	input.setValue(me.getKey().toString().trim(), me.getValue().toString().trim());
			    }
			}
			
		
			if(inputTable!=null&&!"".equals(inputTable.trim())){
				// 从Function中取Table参数
				JCoParameterList tabput = function.getTableParameterList();
				JCoTable table = tabput.getTable(inputTable);
				table.appendRow();
				if(inputTableMap!=null&&inputTableMap.size()>0){
				    Iterator iter = inputTableMap.entrySet().iterator();
				    while(iter.hasNext()) {
				    	Map.Entry entry = (Map.Entry) iter.next();
				    	//System.out.println(entry.getKey().toString().trim()+":"+entry.getValue().toString().trim());
				    	table.setValue(entry.getKey().toString().trim(), entry.getValue().toString().trim());
				    }
				}
				
			
			}
		
			strs[0]="";
			strs[1]="";
			RfcManager.execute(function);
			JCoParameterList output=function.getExportParameterList();
			if(output!=null){
			    String message=output.getString("MESSAGE");
			    String status=output.getString("SUBRC");
			    strs[0]=message;
			    strs[1]=status;
			    //System.out.println(message+"@@"+status);
			}
				return strs[1]+strs[0];	
		}
		
	public static void main(String[] args) throws Exception{
//		Map<String,String> map=new HashMap<String,String>();
//		map.put("P_PERNR","01018747");
//		map.put("P_DATE","20150102");
//		map.put("P_MASSG","15");
//		map.put("P_PERSG","G");
//		
//		String funName="ZHR_OA09";
//		writeToSap(funName,map);
		
//		Map<String,String> map=new HashMap<String,String>();
//		map.put("P_TRFAR","01");
//		map.put("P_TRFGB","B1");
		
//		String funName="ZHR_OA24";
//		String tableName="OUT";
		
//		String[] strs={"TRFGR","TRFST","LGART","BETRG"};
//		readSapInfoArray(funName,map,tableName,strs);
//		readSapInfoMaps(funName,map,tableName,strs);
		
//		String key="TRFGB";
//		String val="TGBTX";
//		readSapOptions(funName,map,tableName,key,val);
		
		
		
		
//		String funName="ZGNHR04_002";
//		String inputtableName="IT_PERNR";
//		String outtableName="IT_0001";
//		String empId="00000747";
//		String[] strs={"BUKRS","WERKS","PERSG","PERSK","VDSK1","BTRTL","ABKRS","ANSVH"};
//		readEmpOrgInfo(funName,inputtableName,empId,outtableName,strs);
		
		
//		String funName="ZHR_OA26";
//		Map<String,String> map=new HashMap<String,String>();
//		map.put("P_PERNR","01018747");
//		
//		String tableName="ZGNV_P0008";
//		
//		String[] strs={"PERNR","PREAS","TRFAR","TRFGB","TRFGR","TRFST","LGA01","BET01","LGA02","BET02"};
//		readSapInfoArray(funName,map,tableName,strs);
		
//		String funName="ZHR_OA26";
//		Map<String,String> map=new HashMap<String,String>();
//		map.put("P_PERNR","01003566");
//		String tableName="IT_0008";
//		String[] strs={"PERNR","PREAS","TRFAR","TRFGB","TRFGR","TRFST","LGA01","BET01","LGA02","BET02"};
		
//		readSapPersonInfo(funName,map,tableName,strs);
//		readSapEmpInfoForJson(funName,map,tableName,strs);
		
		
		Map<String,String> map=new HashMap<String,String>();
		map.put("P_TRFAR","01");
		map.put("P_TRFGB","B1");
		String funName="ZHR_OA24";
		String tableName="OUT";
		String[] strs={"TRFGR","TRFST","LGART","BETRG","LGART2","BETRG2"};
//		readSapInfoArray(funName,map,tableName,strs);
//		readSapOptionsForJson(funName,map,tableName,strs);
		
//		String funName="ZHR_OA25";
//		Map<String,String> map=new HashMap<String,String>();
//		map.put("I_PERNR","01003566");
//		
//		Map<String,String> tablemap=new HashMap<String,String>();
//		map.put("PERNR","01003566");
//		map.put("BEGDA","20150101");
//		map.put("PREAS","23");
//		map.put("TRFAR","01");
//		map.put("TRFGB","B1");
//		map.put("TRFGR","J3高级工程师");
//		map.put("TRFST","00");
//		map.put("LGA01","2000");
//		map.put("BET01","5000.00");
//		map.put("LGA02","2001");
//		map.put("BET02","2500.00");
//		
//		
//		String tableName="IN";
		
//		writeToSap(funName,map);
	}
	
}
