package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.domain.RegisterBean;
import com.domain.LoginMessageBean;
import com.helper.RegisterHelper;
import com.helper.LoginMessageHelper;
import com.util.JSONReader;

public class LoginCt extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);

	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("@json:test;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		//��������
		String data=request.getParameter("data");
		
		/**
		 * ����������JSON
		 */
		JSONObject jsonobj=JSONObject.fromObject(data);
	    RegisterBean register=new RegisterBean();
		register.setuserId(jsonobj.getString("userId"));
		register.setLoginPassword(jsonobj.getString("loinPassword"));
		
		
		//��֤�˺�
		RegisterHelper helper=new RegisterHelper();
		//Ҫ���ص���֤��Ϣ
		String CheckCodeMessage=null;
		int status=helper.checkAccount(register);
		if(status==1){
			CheckCodeMessage="The password is correct.";
		}
		if(status==0){
			CheckCodeMessage="The password is wrong.";
		}
		if(status==-1){
			CheckCodeMessage="The user is not exist.";
		}
		
		/**
		 * ��loginMessage���м�����Ϣ
		 */
		String DBMessage=null;
		if(status==1){
			LoginMessageBean logMessage=new LoginMessageBean();
			logMessage.setUserId(jsonobj.getString("userId"));
			logMessage.setLoginAddress(request.getRemoteHost());//��ȡ��¼��ַ
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
			logMessage.setLoginTime(df.format(new Date()));
			LoginMessageHelper lMHelper=new LoginMessageHelper();//ʹ��loginMessage������
			/**
			 * ����status�ж������Ƿ����loginMessage��
			 */
			status=lMHelper.setLoginMessage(logMessage);
			if(status==1){
				DBMessage="We have write the loginMessage into the database.";
			}
			if(status==0){
				DBMessage="Error.";
			}
		}
		
		/**
		 * ����������ݴ����JSONObject�����ؿͻ���
		 */
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("CheckCodeMessage", CheckCodeMessage);
		jsonObject.put("DBMessage", DBMessage);
		 
		out.print(jsonObject);
		
		
		
	}

}
