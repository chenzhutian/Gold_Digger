package com.futurePayment.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import org.json.JSONObject;

import com.billme.util.FileUtil;

/**
 * 
 * @author luo
 * 
 */
public class FuturePayment {
	private static FuturePayment instance = null;
	private User user = new User();
	private FileUtil fileUtil = null;
	private FuturePaymentSupport supporter;

	/**
	 * 
	 * @param userId
	 *            �û���
	 * @param password
	 *            ��½���� ���캯��
	 */
	private FuturePayment(String name) {
		user.setName(name);
		fileUtil = new FileUtil(name);
		supporter = new FuturePaymentSupport(name);
	}

	public static FuturePayment getInstance() {
		if (instance != null)
			return instance;
		return new FuturePayment(null);
	}

	public static FuturePayment getInstance(String name) {
		instance = new FuturePayment(name);
		return instance;
	}

	/**
	 * �û���½
	 */
	public BasicInformation loginUser(String password) throws PaymentException {
		try {
			return supporter.loginUser(password);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * �û��ǳ�
	 * 
	 */
	public void logoutUser() {
		supporter.logoutUser();
	}

	/**
	 * �õ��û���Ϣ
	 * 
	 * @return JSONObject
	 * 
	 */
	public JSONObject getUserInfo() throws PaymentException {
		try {
			return supporter.getUserInfo();
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ����֧��
	 * 
	 * @param transfer
	 *            transfer to be handled.
	 * @param password
	 *            the payment password of the user
	 * 
	 */
	public boolean personalPay(Transfer transfer, String password)
			throws PaymentException {
		try {
			return supporter.personalPay(transfer, password);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ��ҳ��ȡ���׼�¼
	 * 
	 * @param page
	 *            page number
	 * @param perPage
	 *            number of records per page
	 * @param condition
	 *            the search condition
	 * @return list of records
	 * 
	 */
	public LinkedList<TradeRecord> getBill(int page, int perpage,
			HashMap<String, Object> condition) throws PaymentException {
		try {
			return supporter.getBill(page, perpage, condition);
		} catch (PaymentException e) {
			throw e;
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * ע��
	 * 
	 * @param name
	 *            ����
	 * @param password
	 *            ����
	 * @return result of regist ע����
	 * 
	 */
	public boolean regist(RegistInformation ri) throws PaymentException {
		try {
			return supporter.regist(ri);
		} catch (PaymentException e) {

			throw e;
		}
	}

	/**
	 * ����û��Ƿ����
	 * 
	 * @return �����
	 * @throws PaymentException
	 */
	public boolean checkUserExistence(String name) throws PaymentException {
		try {
			return supporter.checkUserExistence(name);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ����֧��
	 * 
	 * @param payerlist
	 *            ����������
	 * @return ֧�����
	 * @throws PaymentException
	 */
	public boolean multiplePay(ArrayList<HashMap<String, Object>> payerlist)
			throws PaymentException {
		try {
			return supporter.multiplePay(payerlist);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ��ѯ�������п�
	 * 
	 * @return
	 * @return �����ʺ���Ϣ
	 * @throws PaymentException
	 */
	public ArrayList<BankCard> queryAccount() throws PaymentException {
		try {
			return supporter.queryAccount();
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ���п���
	 * 
	 * @param bank
	 *            ����
	 * @param cardNumber
	 *            ���п�����
	 * @return �󶨽��
	 * @throws PaymentException
	 */
	public boolean bindAccount(BankCard bc) throws PaymentException {
		try {
			return supporter.bindAccount(bc);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ������п���
	 * 
	 * @param bank
	 *            ����
	 * @param cardNumber
	 *            ���п�����
	 * @return ����󶨽��
	 * @throws PaymentException
	 */
	public boolean unbindAccount(String bank, String cardNumber)
			throws PaymentException {
		try {
			return supporter.unbindAccount(bank, cardNumber);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ��ѯ��Ա return ��Ա�б�
	 **/
	public JSONObject queryVip() throws PaymentException {
		try {
			return supporter.queryVip();
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * �����Ա
	 * 
	 * @param enterpriseId
	 *            �̼�id
	 * @return ������
	 * @throws PaymentException
	 */
	public boolean applyForVip(String enterpriseId) throws PaymentException {
		try {
			return supporter.applyForVip(enterpriseId);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ʹ�û�Ա��
	 * 
	 * @param enterpriseId
	 *            �̼�id
	 * @param amount
	 *            ���ѽ��
	 * @return
	 * @throws PaymentException
	 */
	public boolean useVip(String enterpriseId, double amount)
			throws PaymentException {
		try {
			return supporter.useVip(enterpriseId, amount);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ��ѯ�˺Ż���
	 * 
	 * @return ��ѯ���
	 * @throws PaymentException
	 */
	public JSONObject queryUserGrade() throws PaymentException {
		try {
			return supporter.queryUserGrade();
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * �˺Ż��ֶһ�
	 * 
	 * @param grade
	 *            ʹ�û���
	 * @return �һ����
	 * @throws PaymentException
	 */
	public boolean userGradeSwap(int grade) throws PaymentException {
		try {
			return supporter.userGradeSwap(grade);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ��ѯ�Ż�ȯ
	 * 
	 * @return ��ѯ���
	 * @throws PaymentException
	 */
	public JSONObject queryCoupon() throws PaymentException {
		try {
			return supporter.queryCoupon();
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * �����Ż�ȯ
	 * 
	 * @param receiver
	 *            ������
	 * @param couponId
	 *            �Ż�ȯid
	 * @param amount
	 *            ����
	 * @return ���ͽ��
	 * @throws PaymentException
	 */
	public boolean sendCoupon(String receiver, String couponId, int amount)
			throws PaymentException {
		try {
			return supporter.sendCoupon(receiver, couponId, amount);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ʹ���Ż�ȯ
	 * 
	 * @param coupun
	 *            �Ż�ȯ
	 * @return ʹ�ý��
	 * @throws PaymentException
	 */
	public boolean useCoupon(JSONObject[] coupun) throws PaymentException {
		try {
			return supporter.useCoupon(coupun);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * �޸ĸ�����Ϣ
	 * 
	 * @param name
	 *            ����
	 * @param sex
	 *            �Ա�
	 * @param birthday
	 *            ����
	 * @param phone
	 *            �绰
	 * @param email
	 *            ����
	 * @return �޸Ľ��
	 * @throws PaymentException
	 */
	public boolean modifyPersonalDetail(String name, String sex, Date birthday,
			String phone, String email) throws PaymentException {
		try {
			return supporter.modifyPersonalDetail(name, sex, birthday, phone,
					email);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ��ѯ�˵�
	 * 
	 * @param page
	 *            �ڼ�ҳ
	 * @param perpage
	 *            ÿҳ����
	 * @param condition
	 *            ����
	 * @return ��ѯ���
	 * @throws PaymentException
	 */
	public boolean queryBill(int page, int perpage, JSONObject condition)
			throws PaymentException {
		try {
			return supporter.queryBill(page, perpage, condition);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ��ѯ�����б�
	 * 
	 * @return �����б�
	 * @throws PaymentException
	 */
	public ArrayList<Friend> queryFriend() throws PaymentException {
		try {
			ArrayList<Friend> friend = supporter.queryFriend();
			for (int i = 0; i < friend.size(); i++) {
				modelToAddress(friend.get(i));
			}
			return friend;
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ��Ӻ���
	 * 
	 * @param friendId
	 *            ����id
	 * @return ��ӽ��
	 * @throws PaymentException
	 */
	public boolean addFriend(String friendId) throws PaymentException {
		try {
			return supporter.addFriend(friendId);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ɾ������
	 * 
	 * @param friendId
	 *            ����ID
	 * @return ɾ�����
	 * @throws PaymentException
	 */
	public boolean deleteFriend(String friendId) throws PaymentException {
		try {
			return supporter.deleteFriend(friendId);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ��ע�̼�
	 * 
	 * @param enterpriseId
	 *            �̼�id
	 * @return ��ע���
	 * @throws PaymentException
	 */
	public boolean attentEnterprise(String enterpriseId)
			throws PaymentException {
		try {
			return supporter.attentEnterprise(enterpriseId);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ȡ����ע�̼�
	 * 
	 * @param enterpriseId
	 *            �̼�id
	 * @return ���
	 * @throws PaymentException
	 */
	public boolean inattentEnterprise(String enterpriseId)
			throws PaymentException {
		try {
			return supporter.inattentEnterprise(enterpriseId);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ������������
	 * 
	 * @param enterpriseId
	 *            �̼�id
	 * @param grade
	 *            ����
	 * @param content
	 *            ����
	 * @param time
	 *            ʱ��
	 * @return ������
	 * @throws PaymentException
	 */
	public boolean shareExperience(ShareInfo si) throws PaymentException {
		try {
			return supporter.shareExperience(si);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ���λ��ͼƬ
	 * 
	 * @return λ��ͼƬ
	 * @throws PaymentException
	 */
	public JSONObject getLocalPicture() throws PaymentException {
		try {
			return supporter.getLocalPicture();
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ��ù��
	 * 
	 * @return ���
	 * @throws PaymentException
	 */
	public JSONObject getAdvertising() throws PaymentException {
		try {
			return supporter.getAdvertising();
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ����̼���ϸ��Ϣ
	 * 
	 * @param enterpriseId
	 *            �̼�id
	 * @return �̼���ϸ��Ϣ
	 * @throws PaymentException
	 */
	public JSONObject getEnterpriseDetail(String enterpriseId)
			throws PaymentException {
		try {
			return supporter.getEnterpriseDetail(enterpriseId);
		} catch (PaymentException e) {
			throw e;
		}
	}

	/**
	 * ����ܱ��̼�
	 * 
	 * @return �ܱ��̼�
	 * @throws PaymentException
	 */
	public JSONObject getSurroundingEnterprise() throws PaymentException {
		try {
			return supporter.getSurroundingEnterprise();
		} catch (PaymentException e) {
			throw e;
		}
	}

	// /**
	// * ����ļ��Ƿ����
	// * @param path Ŀ¼·��
	// * @param name �ļ���
	// * @return �ļ��Ƿ����
	// */
	// public boolean isFileExist(String path, String name)
	// {
	// FileUtil fu = new FileUtil();
	// //��Ӧ�ø�Ŀ¼���������û���
	// return fu.isFileExists(getUser().getName() + File.separator + path,
	// name);
	// }
	// /**
	// * ����ļ��Ƿ�һ�£�ͨ����С
	// * @param path Ŀ¼·��
	// * @param name �ļ���
	// * @return �ļ��Ƿ�һ��
	// */
	// public boolean isSameFile(String path, String name, int size)
	// {
	// FileUtil fu = new FileUtil();
	// //��Ӧ�ø�Ŀ¼���������û���
	// return fu.isSameFile(getUser().getName() + File.separator + path, name,
	// size);
	// }
	/**
	 * ��ʵ��ת���ɴ洢��ַ
	 * 
	 * @param urlStr
	 * @return
	 */
	public String modelToAddress(Object model) {
		String result = null;
		if (model instanceof Friend) {
			Friend f = (Friend) model;
			String dir = "Friend";
			String name = f.getName();
			result = dir + File.separator + name;
			if (fileUtil.isSameFile(dir, name, f.getSize())) {

			} else {
				if (fileUtil.downloadFile(f.getPath(), dir, name, true)) {
					f.setPath(result);
					f.setSize(fileUtil.getFileSize(dir, name));
				} else {
					// ����ʧ��
				}
			}
		}
		return result;
	}
}
