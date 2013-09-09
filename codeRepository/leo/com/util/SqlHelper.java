package com.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class SqlHelper {
	private static Connection ct=null;
	private static PreparedStatement ps=null;
	private static ResultSet rs=null;
	private static CallableStatement cs=null;
	
	public static Connection getCt() {
		return ct;
	}
	public static PreparedStatement getPs() {
		return ps;
	}
	public static ResultSet getRs() {
		return rs;
	}
	public static CallableStatement getCs() {
		return cs;
	}
	
	//�������ݿ����
	private static String url="jdbc:sqlserver://localhost:1433;DatabaseName=test1";
	private static String username="lgb";
	private static String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String password="123456";
	//�������ļ�
	//private static Properties pp=null;
	//private static FileInputStream fis=null;
	
	//����������ֻ��Ҫһ��
	static {
		try{
			/*pp=new Properties();
			fis=new FileInputStream("dbinfo.properties");
			pp.load(fis);
			url=pp.getProperty("url");
			username=pp.getProperty("username");
			driver=pp.getProperty("driver");
			password=pp.getProperty("password");*/
			Class.forName(driver);
			System.out.println("���سɹ�");
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		}
	}
	
	//�õ�����
	public static Connection getConnection(){
		try {
			ct=DriverManager.getConnection(url,username,password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ct;
		
	}
	
	//ͳһ��select
	public static ResultSet executeQuery(String sql,String[] parameters){
		try {
			ct=getConnection();
			ps=ct.prepareStatement(sql);
			if(parameters!=null&&!parameters.equals("")){
				for(int i=0;i<parameters.length;i++){
					ps.setString(i+1, parameters[i]);	
				}
				rs=ps.executeQuery();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
		}
	
	//ͳһ�ĵ��ô洢����
	
	
	//ͳһ��update/delete/insert
	public static int executeUpdate(String sql,String []parameters){
		ct=getConnection();
		int flag=0;
		try {
			ps=ct.prepareStatement(sql);
			if(parameters!=null&&!parameters.equals("")){
				for(int i=0;i<parameters.length;i++){
					ps.setString(i+1, parameters[i]);
				}
			}
			ps.executeUpdate();
			flag=1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;

		
		
	}
	
	
	

}
