package opennomics.com.main.chairman.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;

import opennomics.com.common.email.SendEmail;
import opennomics.com.main.layer.service.LayerService;
import opennomics.com.main.qa.domain.QAErrReportVO;
import opennomics.com.main.qa.service.QAService;
import opennomics.com.main.receipt.domain.ReceiptVO;
import opennomics.com.main.receipt.service.ReceiptService;
import opennomics.com.main.user.domain.UserVO;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service
public class ChairmanServiceImpl extends EgovAbstractServiceImpl implements ChairmanService {
	
	
	@Autowired
	private LayerService layerService;
	
	@Autowired
	private ReceiptService receiptService;
	
	@Autowired
	private QAService qaService;
	
	
	
	@SuppressWarnings("unchecked")
	public JSONObject approvalRequestInfo(int rid, String tname){
		JSONObject returnMap = new JSONObject();
		
		Map<String, Object> infoMap = new HashMap<String, Object>();
		
		infoMap.put("rid", rid);
		infoMap.put("tname", tname);
		
		
		JSONObject qaPreLayer = new JSONObject();
		JSONObject qaAftLayer = new JSONObject();
		List<QAErrReportVO> errReports = new ArrayList<QAErrReportVO>();
		ReceiptVO receiptVO = new ReceiptVO();
		
		try {
			qaPreLayer = layerService.selectQApreLayer(infoMap);
			qaAftLayer = layerService.selectQAafLayer(infoMap);
			errReports = qaService.selectErrReports(rid);
			receiptVO = receiptService.selectReceipt(rid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		returnMap.put("qaPreLayer", qaPreLayer);
		returnMap.put("qaAftLayer", qaAftLayer);
		returnMap.put("errReports", errReports);
		returnMap.put("receiptVO", receiptVO);
		
		return returnMap;
	}

	
	@Transactional
	public void updateLastApprovalRequest(int rid, UserVO chairman){
		Map<String, Object> infoMap = new HashMap<String, Object>();
		
		infoMap.put("rid", rid);
		infoMap.put("userId", chairman.getId());
		
		try {
			receiptService.updateLastApprovalRequest(infoMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Integer> oids = new ArrayList<Integer>();
		oids = layerService.selectQaAfterOids(rid);
		layerService.insertLastApproval(rid, oids);
		
		ReceiptVO receiptVO = new ReceiptVO();
		try {
			receiptVO = receiptService.selectReceipt(rid);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		SendEmail email = new SendEmail();
		
		 // 메일 관련 정보
        String host = "smtp.gmail.com";
        String userName = "git.mail.master@gmail.com";
        String password = "git7100@";
         
        // 메일 내용
        String recipient = "ghre55@git.co.kr";
        String subject = "접수신청결과";
        String content = "안녕하세요. "+receiptVO.getUid()+"님께서 신청하신 접수는 승인처리 되었습니다. "
        		+ "문의사항이 있으시면 "+userName+"으로 메일을 보내주세요.";
		
		try {
			email.sendEmail(host, userName, password, recipient, subject, content);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void updateLastDisApprovalRequest(int rid, UserVO chairman){
		Map<String, Object> infoMap = new HashMap<String, Object>();
		
		infoMap.put("rid", rid);
		infoMap.put("userId", chairman.getId());
		
		try {
			receiptService.updateLastDisApprovalRequest(infoMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Integer> oids = new ArrayList<Integer>();
		oids = layerService.selectQaAfterOids(rid);
		layerService.insertLastDisApproval(rid, oids);
		
		ReceiptVO receiptVO = new ReceiptVO();
		try {
			receiptVO = receiptService.selectReceipt(rid);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		SendEmail email = new SendEmail();
		
		 // 메일 관련 정보
        String host = "smtp.gmail.com";
        String userName = "git.mail.master@gmail.com";
        String password = "git7100@";
         
        // 메일 내용
        String recipient = "ghre55@git.co.kr";
        String subject = "접수신청결과";
        String content = "안녕하세요. "+receiptVO.getUid()+"님께서 신청하신 접수는 거절처리 되었습니다. "
        		+ "문의사항이 있으시면 "+userName+"으로 메일을 보내주세요.";
		
			try {
				email.sendEmail(host, userName, password, recipient, subject, content);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
