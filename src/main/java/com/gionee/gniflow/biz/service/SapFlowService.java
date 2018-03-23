package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SapRequisitionSheet;
import com.gionee.gniflow.dto.SapRequisitionTaskDto;

public interface SapFlowService {
	/**
	 * 根据单据获取工程部负责人
	 * @param preqNo
	 * @param preqItem
	 * @return
	 */
	public String getProjectLeader(String preqNo, Integer preqItem);
	/**
	 * 根据单据获取总经理
	 * @param preqNo
	 * @param preqItem
	 * @return
	 */
	public String getGeneralManager(String preqNo, Integer preqItem);
	/**
	 * 根据SAP账号获取Hr账号
	 * @param sapAccount
	 * @return
	 */
	public String getHrAccount4SapAccount(String sapAccount);
	/**
	 * 根据SAP审批代码获取Hr账号
	 * @param approvalCode
	 * @return
	 */
	public String getHrAccount4SapApprovalCode(String approvalCode);
	/**
	 * 根据Hr账号找到对应的SAP账号
	 * @param hrAccount
	 * @return
	 */
	public String getSapAccount4CurUserAccount(String hrAccount);
	/**
	 * 根据Hr账号找到对应的审批代码
	 * @param hrAccount
	 * @return
	 */
	public String getSapApprovalCode4HrAccount(String hrAccount);
	/**
	 * 流程参与人员采购单分页查询
	 * @param queryMap
	 * 			curUserAccount
	 * 			status,默认!=0
	 * 			preqNo
	 * 			preqItem
	 * @return
	 */
	public List<SapRequisitionSheet> queryPageSapRequisitionSheet4HisProcess(QueryMap queryMap);
	/**
	 * 流程参与人员采购单总数查询
	 * @param queryMap
	 * @return
	 */
	public Integer countSapRequisitionSheet4HisProcess(QueryMap queryMap);
	/**
	 * 用户任务的查询
	 * @param queryMap
	 * @return
	 */
	public List<SapRequisitionTaskDto> queryPageSapRequisitionSheet4UserTask(QueryMap queryMap);
	/**
	 * 用户任务的查询总数
	 * @param queryMap
	 * @return
	 */
	public Integer countSapRequisitionSheet4UserTask(QueryMap queryMap);
	/**
	 * 采购人员分页查询单据
	 * @param queryMap
	 * @return
	 */
	public List<SapRequisitionSheet> querySapReqSheet4ProcurementInquiry(QueryMap queryMap);
	/**
	 * 采购人员查询单据总数
	 * @param queryMap
	 * @return
	 */
	public Integer countSapReqSheet4ProcurementInquiry(QueryMap queryMap);
	
}
