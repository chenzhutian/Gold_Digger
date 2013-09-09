package com.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.domain.RegisterBean;
import com.domain.UserTableBean;
import com.util.SqlHelper;
/**
 * ��¼��ע��Ĳ�����
 * @author Lee
 *
 */
public class RegisterHelper {

	Connection ct=null;
	PreparedStatement ps=null;
	static ResultSet rs=null;
	
	
	/**
	 * У���˺ź���
	 * @param register
	 * @return
	 */
	public int checkAccount(RegisterBean register){
		int flag=-1;//��֤��Ϣ
		try {
			String sql="select* from register where userId=?";
			String parameters[]={register.getuserId()};
			System.out.println(parameters[0]);
			rs=SqlHelper.executeQuery(sql, parameters);
			if(rs.next()){
				if(rs.getString(4).equals(register.getLoginPassword())){
					flag=1;
					return flag;
				}else{
					flag=0;
					return flag;
				}
			}else{
				return flag;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return flag;
		}
	}

	
	/**
	 * У���û��˺�
	 * @param �˺�
	 * @return 0����number�Ѵ��ڣ�1����number������
	 */
	public static int checkUserId(String userId){
		int flag=0;
		try {
			String sql="select* from register where userId=?";
			String []parameters={userId};
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

//	/**
//	 * ������ע��
//	 * ����Ϣ����register��
//	 * @param jsonobj
//	 * @return 0�������ʧ�ܣ�1�������ɹ�
//	 */
//	public static int registerByEmail(JSONObject jsonobj){
//		//ȡ���˺�
//		String userId=jsonobj.getString("userId");
//		//����register����
//		RegisterBean register=new RegisterBean();
//		register.setuserId(userId);
//		//ȡ���ǳ�
//		String name=jsonobj.getString("name");
//		register.setName(name);
//		//ȡ��ע������
//		String Rgtype=jsonobj.getString("Rgtype");
//		register.setRgtype(Rgtype);
//		//ȡ����¼����
//		String loginPassword=jsonobj.getString("loginPassword");
//		register.setLoginPassword(loginPassword);
//		//��register�������
//		int flag=registerInsert(register);
//		return flag;
//		
//	}
	
	/**
	 * ����Ϣ����register��
	 * @param register
	 * @return 0�������ʧ�ܣ�1�������ɹ�
	 */
	public static int registerInsert(RegisterBean register){
		
		String sql="insert into register values(?,?,?,?,?)";
		String []parameters={register.getuserId(),register.getName(),register.getRgtype(),register.getLoginPassword(),"NULL"};
		int flag=SqlHelper.executeUpdate(sql, parameters);
		return flag;
		
	}
	
	
}

