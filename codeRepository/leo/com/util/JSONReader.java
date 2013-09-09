package com.util;

import net.sf.json.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {
	
    private  JSONObject jsonObj;
    private  JSONArray jsonArray;
    
    public JSONReader(String jsonStr)
    {
    	/*
    	 * JSONArray��JSONObject��������ģ��ҿ����Ϻܶ�ʾ�������һ����
    	 * ������涼��jsonStr�����������׳��쳣��
    	 * ����: Servlet.service() for servlet jsp threw exception
         * net.sf.json.JSONException: A JSONArray text must start with '[' 
         * at character 1 of {'username':'we','password':'we','major':'we','gender':'male'}
	     * at net.sf.json.util.JSONTokener.syntaxError(JSONTokener.java:512)
	     * at net.sf.json.JSONArray._fromJSONTokener(JSONArray.java:903)
	     * at net.sf.json.JSONArray._fromString(JSONArray.java:983)
	     * at net.sf.json.JSONArray.fromObject(JSONArray.java:141)
	     * at net.sf.json.JSONArray.fromObject(JSONArray.java:120)
	     * at JSON.JSONReader.<init>(JSONReader.java:18)
    	 */
    	String jsonArr="["+jsonStr+"]";
    	jsonObj=JSONObject.fromObject(jsonStr);
    	jsonArray=JSONArray.fromObject(jsonArr);
    }
    /**
     * ��JSONObject����ת��java Map
     * @return
     */
    public  Map getMap4JSON()
    {
    	Map map=new HashMap();
    	Iterator keys=jsonObj.keys();
    	String key;
    	Object value;
    	while(keys.hasNext())
    	{
    		key=(String)keys.next();
    		value=jsonObj.get(key);
    		map.put(key, value);
    	}
    	return map;
    }
    
    /**
     * ת������
     * @return Object[]
     */
    public Object[] getArray4JSON()
    {
    	return jsonArray.toArray();
    }
    
    /**
     * ���еõ�String[],int[]�Լ�����������������Ĺ���
     * @return String[]
     */
    public String[] getStringArray4JSON()
    {
    	String str[]=new String[jsonArray.size()];
    	for(int i=0;i<str.length;i++)
    	{
    		str[i]=jsonArray.getString(i);
    	}
    	return str;
    }
    
    /**
     * �õ�Java ����
     * @param beanClass
     * @return
     */
    public Object getObject4JSON(Class beanClass)
    {
    	/**
    	 * ��֪��Ϊʲô���ڵ��Ե�ʱ����ֹ��Ҳ�������������쳣
    	 * ��������json-lib-2.1.jar�ֻ������ͺ��ˣ������
    	 * Ҳ���������������Editor����ȷ�����
    	 * 
    	 */
    	Object obj=JSONObject.toBean(jsonObj, beanClass);
    	return obj;
    }
    
    /**
     * �õ�java �����б�
     * @param beanClass
     * @return
     */
    public List getList4JSON(Class beanClass)
    {
    	List list=new ArrayList();
    	Object obj;
    	for(int i=0;i<jsonArray.size();i++)
    	{
    		obj=JSONObject.toBean(jsonObj, beanClass);
    		list.add(obj);
    	}
    	return list;
    }
}

