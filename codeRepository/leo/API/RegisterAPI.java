package API;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.domain.RegisterBean;

import net.sf.json.JSONObject;
/**
 * ע��ӿ�
 * @author Lee
 *
 */

public class RegisterAPI {
	
	 public static void main(String[] args) {   
		 //��������json��Ӧ����email,userId,loginPassword
		 JSONObject jsonobj=new JSONObject();
		 jsonobj.put("userId", "201304");
		 jsonobj.put("email", "452721007@qq.com");
		 jsonobj.put("loginPassword", "12345");
		 String flag=varifyByEmail(jsonobj);
		 System.out.println("flag="+flag);
	       
	    } 
	
	/**
	 * ���������ֻ��Ƿ��Ѿ�ע��
	 * @author Lee
	 * @param number����������ֻ�
	 * @return 0����number�Ѵ��ڣ�1����number������
	 */
	public static int checkNumber(String number){
		//1. ���� HttpClient ��ʵ��
		 HttpClient client=new HttpClient();
		 
		 //����url
		 String url="http://110.64.89.205:8080/BigProject/servlet/CheckNumberCt";
		 
		 //2. ����POST���ӷ�����ʵ��
		 PostMethod postMethod=new PostMethod(url);
		 
		 //3.��postMethod�����number
		 postMethod.addParameter("number",number);
		 
		 //4.��������
		 try {
			client.executeMethod(postMethod);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 //5.�������󣬻�ȡresponse
		 int response=0;
		 try {
			response=Integer.parseInt(postMethod.getResponseBodyAsString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
		    
		
	}
	
	/**
	 * ����û��˺��Ƿ��Ѿ�ע��
	 * @author Lee
	 * @param userId
	 * @return 0�����˺��Ѵ��ڣ�1�����˺Ų�����
	 */
	public static int checkUserId(String userId){
		//1. ���� HttpClient ��ʵ��
		 HttpClient client=new HttpClient();
		 
		 //����url
		 String url="http://110.64.89.205:8080/BigProject/servlet/CheckUserIdCt";
		 
		 //2. ����POST���ӷ�����ʵ��
		 PostMethod postMethod=new PostMethod(url);
		 
		 //3.��postMethod�����number
		 postMethod.addParameter("userId",userId);
		 
		 //4.��������
		 try {
			client.executeMethod(postMethod);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 //5.�������󣬻�ȡresponse
		 int response=0;
		 try {
			response=Integer.parseInt(postMethod.getResponseBodyAsString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * ������ע��
	 * ��������json��Ӧ��Ҫ��userId,email,loginPassword,name(�ǳ�),Rgtype(ע�����ͣ�person or company),state("Unactive","Active")
	 * @author Lee
	 * @param jsonobj
	 * @return 0����ע��ʧ�ܣ�1����ע��ɹ�
	 */
	public static JSONObject registerByEmail(JSONObject jsonobj){
		
		//1. ���� HttpClient ��ʵ��
		 HttpClient client=new HttpClient();
		 
		 //����url
		 String url="http://110.64.89.205:8080/BigProject/servlet/RegisterCt";
		 
		 //2. ����POST���ӷ�����ʵ��
		 PostMethod postMethod=new PostMethod(url);

		 //3.��postMethod�����ע����Ϣ
		 postMethod.addParameter("dataByEmail",jsonobj.toString());
		 
		 System.out.println(jsonobj);
		 
		 //4.��������
		 try {
			client.executeMethod(postMethod);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 //5.�������󣬻�ȡresponse
		 String response=null;
		 try {
			response=postMethod.getResponseBodyAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 JSONObject jsonBack=JSONObject.fromObject(response);
		return jsonBack;

		
		
	}
	
	
	
	
	
	/**
	 * ��������֤ע��
	 * ��������json��Ӧ����email,userId,loginPassword
	 * @param jsonobj
	 * @return
	 */
	public static String varifyByEmail(JSONObject jsonobj){
	
		//1. ���� HttpClient ��ʵ��
		 HttpClient client=new HttpClient();
		 
		 //����url
		 String url="http://110.64.89.205:8080/BigProject/servlet/VarifyByEmailCt";
		 
		 //2. ����POST���ӷ�����ʵ��
		 PostMethod postMethod=new PostMethod(url);

		 //3.��postMethod�����ע����Ϣ
		 postMethod.addParameter("dataByEmail",jsonobj.toString());
		 
		 System.out.println(jsonobj);
		 
		 //4.��������
		 try {
			client.executeMethod(postMethod);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 //5.�������󣬻�ȡresponse
		 String response=null;
		 try {
			response=postMethod.getResponseBodyAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
		
		
		
	}
	
	
	
	
	
	/**
	 * ���ֻ���ע��
	 */
	public static JSONObject registerByPhone(JSONObject jsonobj){
		//1. ���� HttpClient ��ʵ��
		 HttpClient client=new HttpClient();
		 
		 //����url
		 String url="http://110.64.89.205:8080/BigProject/servlet/RegisterCt";
		 
		 //2. ����POST���ӷ�����ʵ��
		 PostMethod postMethod=new PostMethod(url);
		 
		 
		 //3.��postMethod�����ע����Ϣ
		 postMethod.addParameter("dataByPhone",jsonobj.toString());
		 
		 //4.��������
		 try {
			client.executeMethod(postMethod);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 //5.�������󣬻�ȡresponse
		 String response=null;
		 try {
			response=postMethod.getResponseBodyAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 JSONObject jsonBack=JSONObject.fromObject(response);
		return jsonBack;
		
		
	}
	
	
}
