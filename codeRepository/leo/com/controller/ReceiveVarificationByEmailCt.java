package com.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.helper.UserTableHelper;
import com.util.md5;

public class ReceiveVarificationByEmailCt extends HttpServlet {

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
		
		doPost(request,response);
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

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		/*
		 * ���ܴ�����ȡ��������Ϣ���ָ��email��userId
		 * ����email�����ݿ�userTable����ȡ��userId,�����ܳ�һ�ַ���
		 * �����ַ�������յ������Ƚ�
		 * ����ȣ���userTable�е�state���óɡ�Active��
		 * ���ɹ�����VarifyByEmailCt��������
		 */
		String queryString=request.getQueryString();
		//����ָ��email��userId
		String queryStrings []=queryString.split("&");
		String email=null;
		String userIdFromEmail=null;
		for(String s:queryStrings){
			String []name_val=s.split("=");
			if(name_val[0].equals("email")){
				email=name_val[1];
			}
			if(name_val[0].equals("userId")){
				userIdFromEmail=name_val[1];
			}
		}
		
		//����email�����ݿ�userTable����ȡ��userId
		String userIdFromDB=UserTableHelper.getUserIdByEmail(email);
		//���ܳ�һ�ַ���
		md5 md=new md5();
		String code_userIdFromDB=md.calcMD5(userIdFromDB);
		//�����ַ�������յ������Ƚ�
		//����ȣ���userTable�е�state���óɡ�Active��
		if(userIdFromEmail.equals(code_userIdFromDB)){
			UserTableHelper.setStateValidate(email);
		}
		//���ɹ�����VarifyByEmailCt��������
		request.setAttribute("Varification", "true");
		request.getRequestDispatcher("/BigProject/servlet/VarifyByEmailCt").forward(request, response);
		
		
				
	}

}
