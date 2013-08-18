package com.futurePayment.http;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * @author luo
 * a class represents the response body of http request
 */
public class MyResponse {
	private JSONObject responseJson;  
	private JSONObject data;
	
	public MyResponse(String response){
		//int pos = response.lastIndexOf("{");
		//JSONTokener parser = new JSONTokener(response.substring(pos));
		JSONTokener parser = new JSONTokener(response);
		try {
			this.responseJson = (JSONObject)parser.nextValue();
			data = responseJson.getJSONObject("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return resultCode
	 */
	public int getResultCode(){
		int resultCode = 0;
		try {
			
			resultCode = responseJson.getInt("result");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resultCode;
	}
	
	public JSONObject getData()
	{
		return data;
	}
	
	/**
	 * 
	 * @return   JSONObject
	 */
	public JSONObject getResponseBody(){
		return this.responseJson;
	}
	/**
	 * 
	 * @return  resultArray
	 * 
	 */
	public JSONArray getResultArray(String name){
		JSONArray array = null;
		try{
			array = data.getJSONArray(name);
		}catch(Exception e){
			e.printStackTrace();
		}
		return array;
	}
	
	/**
	 * 
	 * @return resultObject
	 */
	public JSONObject getResultObject(String name){
		JSONObject object = null;
		try {
			object = data.getJSONObject(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return responseJson.toString();
	}
	
	
}
