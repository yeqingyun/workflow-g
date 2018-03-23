package com.gionee.gniflow.biz.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gniflow.biz.service.M_Probation_ExamService;
import com.gionee.gniflow.integration.dao.M_Probation_ExamDao;
@Service
public class M_Probation_ExamServiceImpl implements M_Probation_ExamService{

	@Autowired
	private M_Probation_ExamDao  m_Probation_ExamDao;
	@Override
	public int deleteSalary(String processInstanceId) {
		int count=0;
		int count1=0;
		int count2=0;
		count1=m_Probation_ExamDao.deleteSalary1(processInstanceId);
		count2= m_Probation_ExamDao.deleteSalary2(processInstanceId);
		count=count1+count2;
		return count;
	}
}
