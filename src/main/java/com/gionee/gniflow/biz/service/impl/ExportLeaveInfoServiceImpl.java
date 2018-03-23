package com.gionee.gniflow.biz.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gniflow.biz.service.ExportLeaveInfoService;
import com.gionee.gniflow.integration.dao.ExportLeaveInfoDao;
import com.gionee.gniflow.util.StringUtils;

@Service
public class ExportLeaveInfoServiceImpl implements ExportLeaveInfoService{

	@Autowired
	private ExportLeaveInfoDao  exportLeaveInfoDao;
	@Override
	public List<Map> queryLeaveInfo(Map<String, Object> map) {
		List<Map> list=exportLeaveInfoDao.getLeaveInfo(map);
		try {
			list=StringUtils.convert(list);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	private String clob2String(java.sql.Clob clob) throws IOException, SQLException{
		char[] cs=new char[(int)clob.length()];
		clob.getCharacterStream().read(cs);
		clob.free();
		return new String(cs);
	}
	@Override
	public List<Map> query(Map<String, Object> hashMap) {
		// TODO Auto-generated method stub
		return null;
	}
}
