package leo.API;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import net.sf.json.JSONObject;

/**
 * ��¼�ӿ���
 * @author Lee
 *
 */
public class LoginAPI {
/**
 * ���Դ���
 * @param userId
 * @param loinPassword
 * @return
 */
	 public static void main(String[] args) { 
	       JSONObject json=setLoginMessage("123","123");
	       System.out.println(json);
	        
	    } 
	
	
	 /**�����û�id���û����룬������һ��jsonobject�����������֤������Ϣ��
	  * �Ƿ��Ѿ�����¼��Ϣд�����ݿ����Ϣ��
	  * @author Lee
	  * @param userId
	  * @param loinPassword
	  * @return JSONObject 
	  *
	  */
	 
	 public static JSONObject setLoginMessage(String userId,String loinPassword){
		 
		 JSONObject jsonobj=new JSONObject();
		 jsonobj.put("userId", userId);
		 jsonobj.put("loinPassword", loinPassword);
		 
		 //1. ���� HttpClient ��ʵ��
		 HttpClient client=new HttpClient();
		 
		 //����url
		 String url="http://110.64.89.205:8080/BigProject/servlet/LoginCt";
		 
		 //2. ����POST���ӷ�����ʵ��
		 PostMethod postMethod=new PostMethod(url);
		 
		 //3.��postMethod�����jsonobj
		 postMethod.addParameter("data",jsonobj.toString());
		 
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
