package com.util.mail;

public class MailHelper {

	public static void main(String[]args){
		//MailHelper.sendMail("123@qq.com","123");
	}
	public static void sendMail(String data,String ToAddress){
		//�������Ҫ�������ʼ�
		MailSenderInfo mailInfo=new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.qq.com");
		mailInfo.setMailServerPort("465");
		mailInfo.setValidate(true);
		mailInfo.setUserName("452721007@qq.com");
		mailInfo.setPassword("lgb13539783913");
		mailInfo.setFromAddress("452721007@qq.com");
		mailInfo.setToAddress(ToAddress);
		mailInfo.setSubject("ע��ByEmail");  
		mailInfo.setContent("�������µ�ַȷ����֤��Ϣ http://110.64.89.205:8080/BigProject/servlet/ReceiveVarificationByEmailCt?"+data); 
		 //�������Ҫ�������ʼ� 
		SimpleMailSender sms = new SimpleMailSender();
		sms.sendTextMail(mailInfo);//���������ʽ
		//sms.sendHtmlMail(mailInfo);//����html��ʽ
		
		
	}
}
