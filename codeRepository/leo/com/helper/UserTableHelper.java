package com.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.domain.UserTableBean;
import com.util.SqlHelper;

public class UserTableHelper {
	Connection ct=null;
	PreparedStatement ps=null;
	static ResultSet rs=null;

	/**
	 * ����Ϣ����userTable��
	 * @param usertable
	 * @return 0�������ʧ�ܣ�1�������ɹ�
	 */
	public static int userTableInsert(UserTableBean usertable){
		int flag=0;
		String sql="insert into userTable values(?,?,?,?)";
		String []parameters={usertable.getNumber(),usertable.getType(),usertable.getUserId(),usertable.getState()};
		flag=SqlHelper.executeUpdate(sql, parameters);
		return flag;
		
	}

	/**
	 * У��������ֻ�����
	 * @author Lee
	 * @param number
	 * @return 0����number�Ѵ��ڣ�1����number������
	 */
	public static int checkNumber(String number){
		int flag=0;
		try {
			String sql="select* from userTable where number=?";
			String []parameters={number};
			rs=SqlHelper.executeQuery(sql, parameters);
			if(rs.next()){
				flag=1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * ����email�����ݿ�userTable����ȡ��userId
	 * @param email
	 * @return String
	 */
	public static String getUserIdByEmail(String email){
		String userId=null;
		try {
			String sql="select email from UserTable where number=?";
			String []parameters={email};
			rs=SqlHelper.executeQuery(sql, parameters);
			if(rs.next()){
				userId=rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userId;
		
	}

	/**
	 * ����email�����Ѽ���
	 * @param email
	 */
	public static void setStateValidate(String email){
		String sql="update userTable set state='Active' where email=?";
		String []parameters={email};
		SqlHelper.executeUpdate(sql, parameters);
		
	}
}
