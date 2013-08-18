package com.futurePayment.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.futurePayment.constant.ResultCode;
import com.futurePayment.constant.ServiceType;
import com.futurePayment.http.MyHttpClient;
import com.futurePayment.http.MyResponse;
import com.google.gson.Gson;

import android.util.Log;

/**
 * 
 * @author luo
 *
 */
public class FuturePaymentSupport {
	private String name;
	private MyHttpClient http;
	public FuturePaymentSupport(String name)
	{
		this.name = name;
		http = new MyHttpClient(name);
	}
	/**
	 * 
	 * @return 
	 * @throws PaymentException
	 * try to begin session with the server
	 */
	public BasicInformation loginUser(String password) throws PaymentException{
		BasicInformation bi = new BasicInformation();
		try {
			JSONObject jobj = new JSONObject();
			jobj.put("name", name);
			jobj.put("password", password);
			MyResponse response = http.post(ServiceType.LOGIN, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS){
				Gson gson = new Gson();
				JSONObject ob = response.getData();
				bi = gson.fromJson(ob.toString(), BasicInformation.class);
//				bi.setName(name);
//				bi.setGrade(ob.getInt("grade"));
//				bi.setMoney(ob.getDouble("money"));	
				Log.i("error","name:" + bi.getName() + "----" + "grade" + bi.getGrade() + "=====" + "balance" + bi.getBalance());
			}
			else
				throw new PaymentException(response.getResultCode());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bi;
	}
	
	/**
	 * end session with the server
	 */
	public void logoutUser(){
		http.post(ServiceType.LOGOUT, null);
	}

	public JSONObject getUserInfo() throws PaymentException{
		JSONObject info = null;
		JSONObject jobj = new JSONObject();
		MyResponse response = http.post(ServiceType.GET_USER_INFO, jobj);
		if(response.getResultCode() == ResultCode.SUCCESS){
			info = response.getData();		
		}
		else
			throw new PaymentException(response.getResultCode());
		return info;
	}
	/**
	 * 
	 * @param transfer 
	 * @param password
	 */
	public boolean personalPay(Transfer transfer,String password) throws PaymentException{
		JSONObject jobj = new JSONObject();
		try{
			jobj.put("password", password);	
			jobj.put("sender", transfer.getSender());
			jobj.put("receiver", transfer.getReceiver());
			jobj.put("amount",transfer.getAmount());
			jobj.put("method", transfer.getMethod());
			String bank = transfer.getBank();
			String cardNumber = transfer.getCardNumber();
			if(bank != null && cardNumber != null)
			{
				jobj.put("bank", bank);
				jobj.put("cardNumber", cardNumber);
			}
			
			MyResponse response = http.post(ServiceType.PERSONAL_PAY, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
		}catch(Exception e){
			e.printStackTrace();
		}
		//throw new PaymentException(ResultCode.TRANSFER_FAILURE);
		return false;
	}
	
//	public boolean get(Transfer transfer)throws PaymentException{
//		JSONObject jobj = new JSONObject();
//		try{
//			jobj.put("serviceType", ServiceType.TRANSFER_GET);
//			jobj.put("sender", transfer.getSender());
//			jobj.put("receiver", transfer.getReceiver());
//			jobj.put("amount",transfer.getAmount());
//			MyResponse response = http.post(Uris.TRANSFER, jobj);
//			if(response.getResultCode() == ResultCode.SUCCESS)
//				return true;
//		}catch(Exception e){
//			e.printStackTrace();
//			throw new PaymentException(ResultCode.FAILURE);
//		}
//		throw new PaymentException();
//	}
	
	/**
	 * @param bank 
	 * @param cardNumber       
	 */
	public boolean bindAccount(BankCard bc) throws PaymentException
	{
		boolean result = false;
		Gson gson = new Gson();
		try {
			JSONObject jobj = new JSONObject(gson.toJson(bc));
			jobj.put("name", name);
			MyResponse response = http.post(ServiceType.BIND_ACCOUNT, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * @param page page number
	 * @param perPage  number of records per page
	 * @param condition the search condition
	 * @return list of records
	 * ��ҳ��ȡ���׼�¼
	 */
	public LinkedList<TradeRecord> getBill(int page,int perPage, HashMap<String,Object> condition) throws PaymentException
	{
		LinkedList<TradeRecord> records = null;
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("name", name);
			jobj.put("page", page);
			jobj.put("perPage", perPage);
			jobj.put("condition", new JSONObject(condition));
			
			MyResponse response = http.post(ServiceType.QUERY_BILL, jobj);
			int resultCode = response.getResultCode();
			if(resultCode != ResultCode.EMPTY){
				records = new LinkedList<TradeRecord>();
				JSONArray array = response.getResultArray("tradeRecords");
				for(int i = 0 ; i < array.length(); i++){
					JSONObject ob = array.getJSONObject(i);
					TradeRecord record = new TradeRecord();
					record.setSender(ob.getString("sender"));
					record.setReceiver(ob.getString("receiver"));
					record.setAmount(ob.getDouble("amount"));
					record.setDate(ob.getString("date"));
					records.add(record);
				}
			}
			else
				throw new PaymentException(response.getResultCode());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return records;
	}
	
//	public String lookUpUserNameByCardNumber(String cardNumber){
//		JSONObject jobj = new JSONObject();
//		try {
//			jobj.put("serviceType", ServiceType.LOOK_UP_NAME_BY_CARDNUMBER);
//			MyResponse response = http.post(Uris.ACCOUNT_SERVICE, jobj);
//			if(response.getResultCode() == ResultCode.SUCCESS){
//				return response.getResponseBody().getString("userName");
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	

	/**
	 * ע��
	 * @param name ����
	 * @param password ����
	 * @return result of regist ע����
	 * 
	 */
	public boolean regist(RegistInformation ri)throws PaymentException{
		Gson gson = new Gson();	
		boolean result = false; 
		try {
			JSONObject jobj = new JSONObject(gson.toJson(ri));
			MyResponse response = http.post(ServiceType.REGIST, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * ����û��Ƿ����
	 * @return �����
	 * @throws PaymentException
	 */
	public boolean checkUserExistence(String name)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false; 
		try {
			jobj.put("name", name);
			MyResponse response = http.post(ServiceType.CHECK_USER_EXISTENCE, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * �ύ��Ʒ�嵥
	 * @param enterpriseId �̼�id
	 * @param goodlist ��Ʒ�嵥
	 * @return �ύ���
	 * @throws PaymentException
	 */
//  	public boolean sumit_goods_list(String enterpriseId,JSONObject[] goodlist)throws PaymentException{
//		JSONObject jobj = new JSONObject();
//		boolean result = false; 
//		try {
//			jobj.put("enterpriseId", enterpriseId);
//			jobj.put("goodlist", goodlist);			
//			MyResponse response = http.post(ServiceType.SUBMIT_GOODS_LIST, jobj);
//			if(response.getResultCode() == ResultCode.SUCCESS)
//				return true;
//			else
//				throw new PaymentException(response.getResultCode());
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/**
	 * �����Ʒ��Ϣ
	 * @param enterpriseId �̼�id
	 * @param goodsId ��Ʒid
	 * @return ��Ʒ��Ϣ
	 * @throws PaymentException
	 */
//	public JSONObject get_goods_detail(String enterpriseId,String goodsId)throws PaymentException{
//		JSONObject jobj = new JSONObject();
//		JSONObject goodsDetail = null;
//		boolean result = false; 
//		try {
//			jobj.put("enterpriseId",enterpriseId);
//			jobj.put("goodsId",goodsId);
//			MyResponse response = http.post(ServiceType.GET_GOODS_DETAIL, jobj);
//			if(response.getResultCode() == ResultCode.SUCCESS)
//				{
//					goodsDetail=response.getResultObject("goodsDetail");				
//				}
//			else
//				throw new PaymentException(response.getResultCode());
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return goodsDetail;
//	}

	/**
	 * ����֧��
	 * @param payerlist ����������
	 * @return ֧�����
	 * @throws PaymentException
	 */
	public boolean multiplePay(JSONObject[] payerlist)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false; 
		try {
			jobj.put("payerlist",payerlist);
			MyResponse response = http.post(ServiceType.MUTIPLE_PAY, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ��ѯ�����ʺ�
	 * @return �����ʺ���Ϣ
	 * @throws PaymentException
	 */
	public ArrayList<BankCard> queryAccount()throws PaymentException{
		Gson gson = new Gson();
		ArrayList<BankCard> accountInfo = new ArrayList<BankCard>();
		try {
			MyResponse response = http.post(ServiceType.QUERY_ACCOUNT, null);
			if(response.getResultCode() == ResultCode.SUCCESS)
			{
				JSONArray ja = response.getResultArray("cardList");
				for(int i = 0; i < ja.length(); i++)
				{
					BankCard bc = gson.fromJson(ja.getJSONObject(i).toString(), BankCard.class);
					accountInfo.add(bc);
				}			
			}
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountInfo;
	}
	
	/**
	 * ������п���
	 * @param bank ����
	 * @param cardNumber ���п�����
	 * @return ����󶨽��
	 * @throws PaymentException
	 */
	public boolean unbindAccount(String bank,String cardNumber)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false; 
		try {
			jobj.put("bank", bank);
			jobj.put("cardNumber",cardNumber);
			MyResponse response = http.post(ServiceType.UNBIND_ACCOUNT, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * �����Ʒ�嵥
	 * @param enterpriseId �̼�id
	 * @return
	 * @throws PaymentException
	 */
//	public JSONObject get_goods_list(String enterpriseId)throws PaymentException{
//		JSONObject jobj = new JSONObject();
//		JSONObject goodsList = null;
//		try {
//			jobj.put("enterpriseId",enterpriseId);
//			MyResponse response = http.post(ServiceType.GET_GOODS_LIST, jobj);
//			if(response.getResultCode() == ResultCode.SUCCESS)
//				goodsList = response.getResultObject("goodsList");
//			else
//				throw new PaymentException(response.getResultCode());
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return goodsList;
//	}
	
	/**
	 * ��ѯ��Ա
	 * return ��Ա�б�
	 **/
	public JSONObject queryVip()throws PaymentException{
		JSONObject jobj = new JSONObject();
		JSONObject vipList = null;
		try {
			MyResponse response = http.post(ServiceType.QUERY_VIP, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				vipList = response.getResultObject("vipList");
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vipList;
	}
	
	/**
	 * �����Ա
	 * @param enterpriseId �̼�id
	 * @return ������
	 * @throws PaymentException
	 */
	public boolean applyForVip(String enterpriseId)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false;
		try {
			jobj.put("enterpriseId",enterpriseId);
			MyResponse response = http.post(ServiceType.APPLY_FOR_VIP, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ʹ�û�Ա��
	 * @param enterpriseId �̼�id
	 * @param amount ���ѽ��
	 * @return
	 * @throws PaymentException
	 */
	public boolean useVip(String enterpriseId,double amount)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false;
		try {
			jobj.put("enterpriseId",enterpriseId);
			jobj.put("amount",amount);
			MyResponse response = http.post(ServiceType.USE_VIP, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * ��Ա�����ֶһ�
	 * @param enterpriseId �̼�id
	 * @param grade ʹ�û���
	 * @param goodsId ��ƷID
	 * @return
	 * @throws PaymentException
	 */
//	public boolean vip_grade_swap(String enterpriseId,int grade,String goodsId)throws PaymentException{
//		JSONObject jobj = new JSONObject();
//		boolean result = false;
//		try {
//			jobj.put("enterpriseId",enterpriseId);
//			jobj.put("grade",grade);
//			jobj.put("goodsId",goodsId);
//			MyResponse response = http.post(ServiceType.VIP_GRADE_SWAP, jobj);
//			if(response.getResultCode() == ResultCode.SUCCESS)
//				return true;
//			else
//				throw new PaymentException(response.getResultCode());
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/**
	 * ��ѯ�˺Ż���
	 * @return ��ѯ���
	 * @throws PaymentException
	 */
	public JSONObject queryUserGrade()throws PaymentException{
		JSONObject jobj = new JSONObject();
		JSONObject userGrade = null;
		try {
			MyResponse response = http.post(ServiceType.QUERY_USER_GRADE, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				userGrade = response.getResultObject("userGrade");
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userGrade;
	}
	/**
	 * �˺Ż��ֶһ�
	 * @param grade ʹ�û���
	 * @return �һ����
	 * @throws PaymentException
	 */
	public boolean userGradeSwap(int grade)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false;
		try {
			jobj.put("grade",grade);
			MyResponse response = http.post(ServiceType.USER_GRADE_SWAP, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * ��ѯ�Ż�ȯ
	 * @return ��ѯ���
	 * @throws PaymentException
	 */
	public JSONObject queryCoupon()throws PaymentException{
		JSONObject jobj = new JSONObject();
		JSONObject coupunList = null;
		try {
			MyResponse response = http.post(ServiceType.QUERY_COUPON, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				coupunList = response.getResultObject("coupunList");
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return coupunList;
	}
	
	/**
	 * 
	 * ��ѯ�Ż�ȯ��ϸ��Ϣ
	 * @param couponId �Ż�ȯid
	 * @return ��ѯ���
	 * @throws PaymentException
	 */
//	public boolean query_coupun_detail(String couponId)throws PaymentException{
//		JSONObject jobj = new JSONObject();
//		boolean result = false;
//		try {
//			jobj.put("couponId",couponId);
//			MyResponse response = http.post(ServiceType.QUERY_COUPON_DETAIL, jobj);
//			if(response.getResultCode() == ResultCode.SUCCESS)
//				return true;
//			else
//				throw new PaymentException(response.getResultCode());
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	/**
	 * �����Ż�ȯ
	 * @param receiver ������
	 * @param couponId �Ż�ȯid
	 * @param amount ����
	 * @return ���ͽ��
	 * @throws PaymentException
	 */
	public boolean sendCoupon(String receiver,String couponId,int amount)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false;
		try {
			jobj.put("receiver",receiver);
			jobj.put("couponId",couponId);
			jobj.put("amount",amount);
			MyResponse response = http.post(ServiceType.SEND_COUPON, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * ʹ���Ż�ȯ
	 * @param coupun �Ż�ȯ
	 * @return ʹ�ý��
	 * @throws PaymentException
	 */
	public boolean useCoupon(JSONObject[] coupun)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false;
		try {
			jobj.put("coupun",coupun);
			MyResponse response = http.post(ServiceType.USE_COUPON, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * ��ѯ������Ϣ
	 * @return ��ѯ���
	 * @throws PaymentException
	 */
//	public JSONObject query_personal_detail()throws PaymentException{
//		JSONObject jobj = new JSONObject();
//		JSONObject personal_detail = null;
//		try {
//			MyResponse response = http.post(ServiceType.QUERY_PERSONAL_DETAIL, jobj);
//			if(response.getResultCode() == ResultCode.SUCCESS)
//				personal_detail = response.getResultObject("personal_detail");
//			else
//				throw new PaymentException(response.getResultCode());
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return personal_detail;
//	}
	
	/**
	 * �޸ĸ�����Ϣ
	 * @param name ����
	 * @param sex �Ա�
	 * @param birthday ����
	 * @param phone �绰
	 * @param email ����
	 * @return �޸Ľ��
	 * @throws PaymentException
	 */
	public boolean modifyPersonalDetail(String name,String sex,Date birthday,String phone,String email)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false;
		try {
			jobj.put("name",name);
			jobj.put("sex",sex);
			jobj.put("birthday",birthday);
			jobj.put("phone",phone);
			jobj.put("email",email);
			MyResponse response = http.post(ServiceType.MODIFY_PERSONAL_DETAIL, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	/**
	 * ��ѯ�˵�
	 * @param page �ڼ�ҳ
	 * @param perpage ÿҳ����
	 * @param condition ����
	 * @return ��ѯ���
	 * @throws PaymentException
	 */
	public boolean queryBill(int page,int perpage,JSONObject condition)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false;
		try {
			jobj.put("page",page);
			jobj.put("condition",condition);
			MyResponse response = http.post(ServiceType.QUERY_BILL, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public ArrayList<Friend> queryFriend()throws PaymentException{
		ArrayList<Friend> al = null;
		try {
			Gson gson = new Gson();
			MyResponse response = http.post(ServiceType.QUERY_BILL, null);
			if(response.getResultCode() == ResultCode.SUCCESS)
			{
				JSONArray ja = response.getResultArray("friend");
				for(int i = 0; i < ja.length(); i++)
				{
					Friend f = gson.fromJson(ja.getJSONObject(i).toString(), Friend.class);
					al.add(f);
				}
			}
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return al;
	}
	/**
	 * ��Ӻ���
	 * @param friendId ����id
	 * @return ��ӽ��
	 * @throws PaymentException
	 */
	public boolean addFriend(String friendId)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false;
		try {
			jobj.put("friendId",friendId);
			MyResponse response = http.post(ServiceType.ADD_FRIEND, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ɾ������
	 * @param friendId ����ID
	 * @return ɾ�����
	 * @throws PaymentException
	 */
	public boolean deleteFriend(String friendId)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false;
		try {
			jobj.put("friendId",friendId);
			MyResponse response = http.post(ServiceType.DELETE_FRIEND, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ��ע�̼�
	 * @param enterpriseId �̼�id
	 * @return ��ע���
	 * @throws PaymentException
	 */
	public boolean attentEnterprise(String enterpriseId)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false;
		try {
			jobj.put("enterpriseId",enterpriseId);
			MyResponse response = http.post(ServiceType.ATTENT_ENTERPRISE, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ȡ����ע�̼�
	 * @param enterpriseId �̼�id
	 * @return ���
	 * @throws PaymentException
	 */
	public boolean inattentEnterprise(String enterpriseId)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false;
		try {
			jobj.put("enterpriseId",enterpriseId);
			MyResponse response = http.post(ServiceType.INATTENT_ENTERPRISE, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ������������
	 * @param enterpriseId �̼�id
	 * @param grade ����
	 * @param content ����
	 * @param time ʱ��
	 * @return ������
	 * @throws PaymentException
	 */
	public boolean shareExperience(String enterpriseId,int grade,String content,Date time)throws PaymentException{
		JSONObject jobj = new JSONObject();
		boolean result = false;
		try {
			jobj.put("enterpriseId",enterpriseId);
			jobj.put("grade",grade);
			jobj.put("content",content);
			jobj.put("time",time);
			MyResponse response = http.post(ServiceType.SHARE_EXPERIENCE, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				return true;
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ���λ��ͼƬ
	 * @return λ��ͼƬ
	 * @throws PaymentException
	 */
	public JSONObject getLocalPicture()throws PaymentException{
		JSONObject jobj = new JSONObject();
		JSONObject local_picture = null;
		try {
			MyResponse response = http.post(ServiceType.GET_LOCAL_PICTURE, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				local_picture = response.getResultObject("local_picture");
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return local_picture;
	}
	
	/**
	 * ��ù��
	 * @return ���
	 * @throws PaymentException
	 */
	public JSONObject getAdvertising()throws PaymentException{
		JSONObject jobj = new JSONObject();
		JSONObject advertising = null;
		try {
			MyResponse response = http.post(ServiceType.GET_ADVERTISING, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				advertising = response.getResultObject("advertising");
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return advertising;
	}
	
	/**
	 * ����̼���ϸ��Ϣ
	 * @param enterpriseId �̼�id
	 * @return �̼���ϸ��Ϣ
	 * @throws PaymentException
	 */
	public JSONObject getEnterpriseDetail(String enterpriseId)throws PaymentException{
		JSONObject jobj = new JSONObject();
		JSONObject enterprise_detail = null;
		try {
			MyResponse response = http.post(ServiceType.GET_ENTERPRISE_DETAIL, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				enterprise_detail = response.getResultObject("enterprise_detail");
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enterprise_detail;
	}
	/**
	 * ����ܱ��̼�
	 * @return �ܱ��̼�
	 * @throws PaymentException
	 */
	public JSONObject getSurroundingEnterprise()throws PaymentException{
		JSONObject jobj = new JSONObject();
		JSONObject surrounding_enterprise = null;
		try {
			MyResponse response = http.post(ServiceType.GET_SURROUNDIND_ENTERPRISE, jobj);
			if(response.getResultCode() == ResultCode.SUCCESS)
				surrounding_enterprise = response.getResultObject("surrounding_enterprise");
			else
				throw new PaymentException(response.getResultCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return surrounding_enterprise;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}