package com.gionee.gniflow.web.servicetask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeUtility;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.service.impl.sfm.SfmServiceImpl;
import com.gionee.gniflow.biz.service.impl.sfm.Supplier;
import com.gionee.gniflow.biz.sfm.SfmService;
import com.gionee.gniflow.web.util.PropertyHolder;
import com.gionee.gniflow.web.util.SpringTools;


public class SendMailServiceTask implements JavaDelegate {
	
	private static final String from = PropertyHolder.getContextProperty("email.from");
	private static final String fromName = PropertyHolder.getContextProperty("email.fromName");
	private static final String username = PropertyHolder.getContextProperty("email.username");
	private static final String password = PropertyHolder.getContextProperty("email.password");
	private static final String sfmUrl = PropertyHolder.getContextProperty("sfm.url");
	private final static Map<String, String> hostMap = new HashMap<String, String>();
	static {
		// 126
		hostMap.put("smtp.126", "smtp.126.com");
		// qq
		hostMap.put("smtp.qq", "smtp.qq.com");
		// 163
		hostMap.put("smtp.163", "smtp.163.com");
		// sina
		hostMap.put("smtp.sina", "smtp.sina.com.cn");
		// tom
		hostMap.put("smtp.tom", "smtp.tom.com");
		// 263
		hostMap.put("smtp.263", "smtp.263.net");
		// yahoo
		hostMap.put("smtp.yahoo", "smtp.mail.yahoo.com");
		// hotmail
		hostMap.put("smtp.hotmail", "smtp.live.com");
		// gmail
		hostMap.put("smtp.gmail", "smtp.gmail.com");
		hostMap.put("smtp.port.gmail", "465");
	}

	public static String getHost(String email) throws Exception {
		Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
		Matcher matcher = pattern.matcher(email);
		String key = "unSupportEmail";
		if (matcher.find()) {
			key = "smtp." + matcher.group(1);
		}
		if (hostMap.containsKey(key)) {
			return hostMap.get(key);
		} else {
			throw new Exception("unSupportEmail");
		}
	}

	
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		Map<String, Object> variables = execution.getVariables();
		/*for(Entry<String, Object> entry : variables.entrySet()){
			System.out.println(entry.getKey()+" : "+entry.getValue());
		}*/
		sendSfmMail(variables);
		//System.out.println("==================================================================123234========================================================");

	}
	
	public void testSendMail() throws Exception{
		MultiPartEmail multiPartEmail = new MultiPartEmail();
		multiPartEmail.setHostName(getHost("workflow_test@163.com"));
		multiPartEmail.setAuthentication("workflow_test@163.com", "email123");
		multiPartEmail.setCharset("utf-8");
		multiPartEmail.addTo("shenshuangxi@gionee.com", "shenshuangxi@gionee.com", "utf-8");
		multiPartEmail.setFrom("workflow_test@163.com", "Gionee工作流平台","utf-8");
		multiPartEmail.setSubject("附件邮件");
		multiPartEmail.setMsg("测试邮件");
		multiPartEmail.addBcc("1695904589@qq.com", "申双喜");
		multiPartEmail.addBcc("313732936@qq.com", "申双喜");
		EmailAttachment emailAttachment = new EmailAttachment();
		emailAttachment.setURL(new URL("https://mirrors.tuna.tsinghua.edu.cn/apache//commons/io/source/commons-io-2.5-src.zip"));
		emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
		emailAttachment.setDescription("描述信息");
		emailAttachment.setName(MimeUtility.encodeText("commons-io-2.5-src.zip"));
		multiPartEmail.attach(emailAttachment);
		multiPartEmail.send();
	}
	
	public void sendEmail(Map<String,Object> variables) throws Exception{
		MultiPartEmail multiPartEmail = new MultiPartEmail();
		multiPartEmail.setHostName(getHost(SendMailServiceTask.from));
		multiPartEmail.setFrom(SendMailServiceTask.from, SendMailServiceTask.fromName,"utf-8");
		multiPartEmail.setAuthentication(SendMailServiceTask.username, SendMailServiceTask.password);
		multiPartEmail.setCharset("utf-8");
		multiPartEmail.addTo((String)variables.get("toemail"), (String)variables.get("toemail"), "utf-8");
		multiPartEmail.setSubject((String)variables.get("subject"));
		multiPartEmail.setMsg((String)variables.get("content"));
		String ccstr = (String) variables.get("ccs");
		if(ccstr!=null){
			String[] ccs = ccstr.split(";(\r){0,1}(\n){0,1}");
			for(String cc : ccs)
				multiPartEmail.addCc(cc);
		}
		String[] filePaths = ((String)variables.get("filePaths")).split(";(\r){0,1}(\n){0,1}");
		for(String filePath : filePaths){
			EmailAttachment emailAttachment = new EmailAttachment();
			try {
				emailAttachment.setURL(new URL(filePath.trim()));
				String[] fs = filePath.split("/");
				emailAttachment.setName(MimeUtility.encodeText(fs[fs.length-1]));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				emailAttachment.setPath(filePath);
				String[] fs = filePath.split("\\\\");
				emailAttachment.setName(MimeUtility.encodeText(fs[fs.length-1]));
			}
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setDescription("描述信息");
			
			multiPartEmail.attach(emailAttachment);
		}
		multiPartEmail.send();
	}
	
	
	public void sendSfmMail(Map<String, Object> params) throws Exception{
		
		String applyUserId = (String) params.get("applyUserId");
		MultiPartEmail multiPartEmail = new MultiPartEmail();
		multiPartEmail.setHostName(getHost("workflow_test@163.com"));
		multiPartEmail.setFrom("workflow_test@163.com", "Gionee工作流平台","utf-8");
		multiPartEmail.setAuthentication("workflow_test@163.com", "email123");
		multiPartEmail.setCharset("utf-8");
		String toName = params.get("toName")==null?(String)params.get("toemail"):(String)params.get("toName");
		multiPartEmail.addTo((String)params.get("toemail"), toName, "utf-8");
		multiPartEmail.setSubject((String)params.get("subject"));
		boolean isAddAttace = isSendToCCAddAttache(multiPartEmail,false,params);//发送给抄送人员
		String content =  null;
		if(!isAddAttace){
			content = (String)params.get("content")+"\r\n"+(String)params.get("dispFilePaths")+"			这些文件已知悉"+ params.get("ccs")+"  !!!";
			
		}else{
			content = (String)params.get("content");
		}
		multiPartEmail.setMsg(content);
		multiPartEmail.setSSL(true);
		multiPartEmail.setSslSmtpPort("465");
		
		
		String[] filePaths = ((String)params.get("filePaths")).split(";(\r){0,1}(\n){0,1}");
		String[] fileNames = ((String)params.get("dispFilePaths")).split(";(\r){0,1}(\n){0,1}");
		for(int i=0;i<filePaths.length;i++){
			String urlNameString = SendMailServiceTask.sfmUrl+"/api/downFile?account="+applyUserId+"&path="+filePaths[i].trim();
			multiPartEmail.attach(new URL(urlNameString), MimeUtility.encodeText(fileNames[i]), "send mail", EmailAttachment.ATTACHMENT);
		}
		multiPartEmail.send();
		
		
		//检查供应商是否存在，不存在则插入
//		SfmService sfmService = new SfmServiceImpl();
//		Supplier supplier = sfmService.getSupplierByEmail((String) params.get("toemail"));
//		if(supplier==null){
//			supplier = new Supplier();
//			supplier.setCategory((String) params.get("supplierCategory"));
//			supplier.setEmail((String) params.get("toemail"));
//			supplier.setSupplierName((String) params.get("supplierName"));
//			supplier.setUsername((String) params.get("toName"));;
//			sfmService.addSupplier(supplier);
//		}
		
	}
	
	
	public boolean isSendToCCAddAttache(MultiPartEmail multiPartEmail,boolean canSeeAttache,Map<String, Object> params) throws Exception{
		if(canSeeAttache){
			String ccstr = (String) params.get("ccs");
			if(ccstr!=null){
				String[] ccs = ccstr.split(";(\r){0,1}(\n){0,1}");
				for(String cc : ccs)
					multiPartEmail.addCc(cc);
			}
			return true;
		}else{
			String toName = params.get("toName")==null?(String)params.get("toemail"):(String)params.get("toName");
			String ccstr = (String) params.get("ccs");
			if(ccstr!=null){
				String[] ccs = ccstr.split(";(\r){0,1}(\n){0,1}");
				for(int i=0;i<ccs.length;i++){
					MultiPartEmail ccmultiPartEmail = new MultiPartEmail();
					ccmultiPartEmail.setHostName(getHost("workflow_test@163.com"));
					ccmultiPartEmail.setFrom("workflow_test@163.com", "Gionee工作流平台","utf-8");
					ccmultiPartEmail.setAuthentication("workflow_test@163.com", "email123");
					ccmultiPartEmail.setCharset("utf-8");
					ccmultiPartEmail.addTo(ccs[i], ccs[i], "utf-8");
					ccmultiPartEmail.setSubject((String)params.get("subject"));
					String content = (String)params.get("content")+"\r\n"+(String)params.get("dispFilePaths")+"			这些文件已发送"+toName+"，请知悉!!!";
					ccmultiPartEmail.setMsg(content);
					ccmultiPartEmail.setSSL(true);
					ccmultiPartEmail.setSslSmtpPort("465");
					ccmultiPartEmail.send();
				}
			}
			return false;
		}
	}
	
	
	public byte[] loadAttachmentFromSfm(String appliyUserId,String path) throws Exception{
	    BufferedReader in = null;
	    String urlNameString = SendMailServiceTask.sfmUrl+"/api/downFile?account="+appliyUserId+"&path="+path;
		try {
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36");
			connection.connect();
			InputStream is = connection.getInputStream();
			byte[] buf = new byte[connection.getContentLength()];
			is.read(buf);
			return buf;
		} finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	

}
