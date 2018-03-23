package com.gionee.gniflow.web.listener.filesign;

import java.math.BigDecimal;
import java.util.List;

import oracle.net.aso.s;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.SpringTools;

public class UpdateSignFileInfoListener implements TaskListener {
	private Logger logger = LoggerFactory.getLogger(UpdateSignFileInfoListener.class);
	
	
	SignFileInformationService sfInfoService;
	
	/**
	 * 通过流程ID更新审批状态
	 * 
	 * @author wjq
	 */
	private static final long serialVersionUID = -7349967222331685937L;

	@Override
	public void notify(DelegateTask delegateTask) {
		logger.debug("Running in updateFSAudiStateListener begin!");
		/**
		 * 查询相同类文件编号最终确定文件编号
		 */
		sfInfoService = (SignFileInformationService) SpringTools.getBean(SignFileInformationService.class);
		
		String processInstanceId = delegateTask.getProcessInstanceId();

		List<SignFileInformation> list = null;
		QueryMap queryMap = new QueryMap();
		queryMap.put("procInstId", processInstanceId);
		list = sfInfoService.querySignFileInformation(queryMap);
		if (!CollectionUtils.isEmpty(list)) {
			SignFileInformation sfInfo = new SignFileInformation();
			sfInfo = list.get(0);
			//sfInfo.setAudiStatus(2);
			if(sfInfo.getFileOperateType()==WebReqConstant.SIGN_FILE_OPERATE_TYPE_ADD){//新增的时候给编号赋值，修订的时候不做任何处理
			QueryMap querySql = new QueryMap();
			querySql.put("status", BPMConstant.STATUS_NORMAL);
			querySql.put("orgCode", sfInfo.getOrgCode());
			querySql.put("audiStatus", WebReqConstant.SIGN_FILE_PRO_AUDIED);
			querySql.put("fileType", sfInfo.getFileType());
			SignFileInformation maxSignFile = new SignFileInformation();
			maxSignFile = sfInfoService.getMaxSignFile(querySql);
			if (null == maxSignFile) {
				sfInfo.setFileEndSerNo("000");
			} else {
				String fileEndSerNo = maxSignFile.getFileEndSerNo();
				// 将字符串转换为数字
				BigDecimal d_id = new BigDecimal(fileEndSerNo);
				Integer endNumber = d_id.intValue() + 1;
				// 将数字转换成想要的格式 如数字为2， 转换为002
				String str = String.format("%03d", endNumber);
				sfInfo.setFileEndSerNo(str);// 给最终序列号赋值
			}
			StringBuffer fileNo = new StringBuffer();
			fileNo.append(sfInfo.getOrgCode()).append("-")
					.append(sfInfo.getFileType()).append("-")
					.append(sfInfo.getFileEndSerNo());
			sfInfo.setFileNo(fileNo.toString());// 给文件编号赋值
			sfInfoService.save(sfInfo);
			
			}
		}
		/**
		 * 查询相同类文件编号最终确定文件编号
		 */
		logger.debug("Running in updateFSAudiStateListener end!");
	}

}
