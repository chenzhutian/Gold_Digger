package com.util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator{

	private String userName=null;
	private String password=null;
	
	public MyAuthenticator(String username, String password){
		this.userName=username;
		this.password=password;
	}
	protected PasswordAuthentication getPasswordAuthentication(){
		return new PasswordAuthentication(userName, password); 
	}
	
	
	/*
	 * ���Դ���
	 */
	public static void main(String []args){
		//�������Ҫ�������ʼ�
		MailSenderInfo mailInfo=new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.qq.com");
		mailInfo.setMailServerPort("465");
		mailInfo.setValidate(true);
		mailInfo.setUserName("452721007@qq.com");
		mailInfo.setPassword("lgb13539783913");
		mailInfo.setFromAddress("452721007@qq.com");
		mailInfo.setToAddress("452721007@qq.com");
		mailInfo.setSubject("����������� ��http://www.guihua.org �й�����");  
		mailInfo.setContent("������������ ��http://110.64.89.205:8080/BigProject/Login.jsp ���й�������վ=="); 
		 //�������Ҫ�������ʼ� 
		SimpleMailSender sms = new SimpleMailSender();
		sms.sendTextMail(mailInfo);//���������ʽ
		//sms.sendHtmlMail(mailInfo);//����html��ʽ
		
		
	}
	
}
