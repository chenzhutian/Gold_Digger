package com.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.domain.RegisterBean;
import com.domain.UserTableBean;
import com.helper.RegisterHelper;
import com.helper.UserTableHelper;
import com.util.SqlHelper;

public class RegisterCt extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("@json:test;charset=utf-8");
		PrintWriter out = response.getWriter();
		String message=null;
		String dataByEmail=request.getParameter("dataByEmail");
		String dataByPhone=request.getParameter("dataByPhone");
		/*
		 * ����Email��ע��
		 */
		if(dataByEmail!=null){
			JSONObject jsonobj=JSONObject.fromObject(dataByEmail);
			//ȡ���˺�
			String userId=jsonobj.getString("userId");
			//����register����
			RegisterBean register=new RegisterBean();
			register.setuserId(userId);
			//ȡ���ǳ�
			String name=jsonobj.getString("name");
			register.setName(name);
			//ȡ��ע������
			String Rgtype=jsonobj.getString("Rgtype");
			register.setRgtype(Rgtype);
			//ȡ����¼����
			String loginPassword=jsonobj.getString("loginPassword");
			register.setLoginPassword(loginPassword);
			//���뵽register��
			int flag1=RegisterHelper.registerInsert(register);
			
			//�½�usertable
			UserTableBean usertable=new UserTableBean();
			usertable.setNumber(jsonobj.getString("email"));
			usertable.setType("Email");
			usertable.setUserId(userId);
			usertable.setState(jsonobj.getString("state"));
			//���뵽usertable��
			int flag2=UserTableHelper.userTableInsert(usertable);
			
			
			if(flag1==1&&flag2==1){
				message="The registration is successful!";
			}else{
				message="Wrong registration!";
			}
		}
		/*
		 * �����ֻ���ע��
		 */
		if(dataByPhone!=null){
			JSONObject jsonobj=JSONObject.fromObject(dataByPhone);
			//ȡ���˺�
			String userId=jsonobj.getString("userId");
			//����register����
			RegisterBean register=new RegisterBean();
			register.setuserId(userId);
			//ȡ���ǳ�
			String name=jsonobj.getString("name");
			register.setName(name);
			//ȡ��ע������
			String Rgtype=jsonobj.getString("Rgtype");
			register.setRgtype(Rgtype);
			//ȡ����¼����
			String loginPassword=jsonobj.getString("loginPassword");
			register.setLoginPassword(loginPassword);
			//���뵽register��
			int flag1=RegisterHelper.registerInsert(register);
			
			//�½�usertable
			UserTableBean usertable=new UserTableBean();
			usertable.setNumber(jsonobj.getString("phone"));
			usertable.setType("Phone");
			usertable.setUserId(userId);
			//���뵽usertable��
			int flag2=UserTableHelper.userTableInsert(usertable);
			
			if(flag1==1&&flag2==1){
				message="The registration is successful!";
			}else{
				message="Wrong registration!";
			}
		}
		/*
		 * ����������ݴ����JSONObject�����ؿͻ���
		 */
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("message", message);
		out.print(jsonObject);
	}

}
