package com.billme.logic;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.billme.ui.BankCardActivity;
import com.billme.ui.FriendActivity;
import com.billme.ui.LoginActivity;
import com.billme.ui.MainActivity;
import com.billme.ui.PaymentActivity;
import com.billme.ui.PaymentConfirmActivity;
import com.billme.ui.R;
import com.billme.ui.RegistActivity;
import com.futurePayment.constant.Task;
import com.futurePayment.model.BankCard;
import com.futurePayment.model.Friend;
import com.futurePayment.model.FuturePayment;
import com.futurePayment.model.PaymentException;
import com.futurePayment.model.RegistInformation;
import com.futurePayment.model.Transfer;
import com.futurePayment.model.User;


@SuppressLint({ "UseValueOf", "HandlerLeak" })
public class MainService extends Service implements Runnable {

	/**
	 * @param args
	 */
	public static ArrayList<Activity> allActivities = new ArrayList<Activity>();
	public static int lastActivityId;
	public static ArrayList<Task> allTask = new ArrayList<Task>();
	public boolean isRun = true;
	private static FuturePayment futurePayment = FuturePayment.getInstance();

	public static void newTask(Task task) {
		Log.i("error", "add task");
		allTask.add(task);
	}

	private Handler handler = new Handler() {
		// 回调函数
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Task.TASK_USER_LOGIN: {
				// 登陆
				Log.i("error", "登陆回调中");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("LoginActivity");
//				if (msg.obj instanceof PaymentException) {
//					PaymentException e = (PaymentException) msg.obj;
//					// TODO 返回失败信息
//					ba.refresh(new Integer(LoginActivity.LOGIN_FAILURE), e);
//				} else if (msg.obj instanceof BasicInformation) {
//					BasicInformation bi = (BasicInformation) msg.obj;
//					futurePayment.getUser().setName(bi.getName());
//					futurePayment.getUser().setBalance(bi.getBalance());
//					futurePayment.getUser().setGrade(bi.getGrade());
					ba.refresh(new Integer(LoginActivity.LOGIN_SECCUSS));
//				}
			}
				break;
			case Task.TASK_GET_USER_INFO: {
				Log.i("error", "获取用户信息回调中");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("MainActivity");
				ba.refresh(new Integer(MainActivity.REFRESH_USERINFO), msg.obj);
				break;
			}
			case Task.TASK_CHECK_NAME_AVAILABLE: {
				Log.i("error", "测试用户名是否有效回调中");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("RegistActivity");
				if (msg.obj instanceof PaymentException) {
					PaymentException e = (PaymentException) msg.obj;
					// TODO 返回失败信息
					ba.refresh(new Integer(RegistActivity.NAME_NOT_AVAILABLE),
							e);
				} else {
					ba.refresh(new Integer(RegistActivity.NAME_AVAILABLE), null);
				}
				break;
			}
			case Task.TASK_USER_REGIST: {
				Log.i("error", "用户注册回调中");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("RegistActivity");
				if (msg.obj instanceof PaymentException) {
					PaymentException e = (PaymentException) msg.obj;
					// TODO 返回失败信息
					ba.refresh(new Integer(RegistActivity.REGIST_FAILURE), e);
				} else {
					ba.refresh(new Integer(RegistActivity.REGIST_SUCCESS), null);
				}
				break;
			}
			case Task.TASK_GET_BANK_CARD: {
				Log.i("error", "获取银行卡列表回调中");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("BankCardActivity");
				if (msg.obj instanceof PaymentException) {
					PaymentException e = (PaymentException) msg.obj;
					// TODO 返回失败信息
					ba.refresh(new Integer(
							BankCardActivity.GET_BANK_CARD_FAILURE), e);
				} else if (msg.obj instanceof ArrayList<?>) {
					futurePayment.getUser().setBankCardList(
							(ArrayList<BankCard>) msg.obj);
					ba.refresh(new Integer(
							BankCardActivity.GET_BANK_CARD_SECCUSS));
				}
				break;
			}
			case Task.TASK_ADD_BANK_CARD: {
				Log.i("error", "添加银行卡回调中");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("BankCardActivity");
				if (msg.obj instanceof PaymentException) {
					PaymentException e = (PaymentException) msg.obj;
					// TODO 返回失败信息
					ba.refresh(new Integer(
							BankCardActivity.ADD_BANK_CARD_FAILURE), e);
				} else if (msg.obj instanceof ArrayList<?>) {
					ba.refresh(new Integer(
							BankCardActivity.ADD_BANK_CARD_SUCCESS), msg.obj);
				}
			}
			case Task.TASK_GET_FRIENDS:{
				Log.i("error", "查询好友列表回调中");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("FriendActivity");
				if (msg.obj instanceof PaymentException) {
					PaymentException e = (PaymentException) msg.obj;
					// TODO 返回失败信息
					ba.refresh(new Integer(
							FriendActivity.GET_FRIEND_SUCCESS), e);
				}else if (msg.obj instanceof ArrayList<?>) {
					futurePayment.getUser().setFriendList(
							(ArrayList<Friend>) msg.obj);
					ba.refresh(new Integer(
							FriendActivity.GET_FRIEND_FAILURE));
				}
			}
			case Task.TASK_SINGLE_USER_PAY:{
				Log.i("error", "单人支付回调中");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("PaymentActivity");
				if (msg.obj instanceof PaymentException) {
					PaymentException e = (PaymentException) msg.obj;
					// TODO 返回失败信息
					ba.refresh(new Integer(
							PaymentActivity.PAY_SUCCESS), e);
				}
				else
				{
					ba.refresh(new Integer(
							PaymentActivity.PAY_SUCCESS), null);
				}
			}
			case Task.TASK_MULTI_USER_PAY:{
				Log.i("error", "多人支付回调中");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("PaymentConfirmActivity");
				if (msg.obj instanceof PaymentException) {
					PaymentException e = (PaymentException) msg.obj;
					// TODO 返回失败信息
					ba.refresh(new Integer(
							PaymentConfirmActivity.APPLY_FAILURE), e);
				}
				else
				{
					ba.refresh(new Integer(
							PaymentConfirmActivity.APPLY_SUCCESS), null);
				}
			}
			default:
				break;
			}

		}
	};

	public void doTask(Task task) {
		Message msg = handler.obtainMessage();
		msg.what = task.getTaskId();
		try {
			switch (task.getTaskId()) {
			case Task.TASK_USER_LOGIN: {
				// TODO
//				Log.i("error", "登陆中");
//				String userName = (String) task.getTaskParam().get("userName");
//				String userPassword = (String) task.getTaskParam().get(
//						"userPassword");
//				futurePayment = FuturePayment.getInstance(userName);
//				try {
//					msg.obj = futurePayment.loginUser(userPassword);
//				} catch (PaymentException e) {
//					msg.obj = e;
//				}
				break;
			}
			case Task.TASK_GET_USER_INFO: {
				Log.i("error", "获取用户信息");

				msg.obj = task.getTaskParam();
				break;
			}
			case Task.TASK_CHECK_NAME_AVAILABLE: {
				Log.i("error", "测试用户名是否有效");
				try {
					futurePayment.checkUserExistence((String) task
							.getTaskParam().get("name"));
				} catch (PaymentException e) {
					msg.obj = e;
				}
				break;
			}
			case Task.TASK_USER_REGIST: {
				Log.i("error", "用户注册");
				RegistInformation ri = new RegistInformation();
				try {
					ri.setName((String) task.getTaskParam().get("name"));
					ri.setLoginPassword((String) task.getTaskParam().get(
							"loginPassword"));
					ri.setPayPassword((String) task.getTaskParam().get(
							"payPassword"));
					ri.setRealName((String) task.getTaskParam().get("realName"));
					ri.setBirthday((Date) task.getTaskParam().get("birthday"));
					ri.setSex((String) task.getTaskParam().get("sex"));
					ri.appendContact("phone",
							(String) task.getTaskParam().get("phone"));
					ri.appendContact("email",
							(String) task.getTaskParam().get("email"));
					futurePayment.regist(ri);
				} catch (PaymentException e) {
					msg.obj = e;
				}
				break;
			}
			case Task.TASK_GET_BANK_CARD: {
				Log.i("error", "获取银行卡列表");
				try {
					msg.obj = futurePayment.queryAccount();
				} catch (PaymentException e) {
					msg.obj = e;
				}
				break;
			}
			case Task.TASK_ADD_BANK_CARD: {
				Log.i("error", "添加银行卡");
				try {
					BankCard bc = new BankCard();
					bc.setBankName((String) task.getTaskParam().get("bank"));
					bc.setCardNumber((String) task.getTaskParam().get(
							"cardNumber"));
					msg.obj = bc;
					futurePayment.bindAccount(bc);
				} catch (PaymentException e) {
					msg.obj = e;
				}
				break;
			}
			case Task.TASK_GET_FRIENDS:{
				Log.i("error", "获取好友列表");
				try{
					msg.obj = futurePayment.queryFriend();
				}catch (PaymentException e) {
					msg.obj = e;
				}
			}
			case Task.TASK_SINGLE_USER_PAY:{
				Log.i("error", "个人支付");
				try{
					Transfer t = new Transfer();
					t.setSender(getUser().getName());
					t.setReceiver((String)task.getTaskParam().get("receiver"));
					t.setAmount((Double)task.getTaskParam().get("money"));
					t.setMethod((String)task.getTaskParam().get("method"));
					futurePayment.personalPay(t, (String)task.getTaskParam().get("password"));
				}catch (PaymentException e) {
					msg.obj = e;
				}
				break;
			}
			case Task.TASK_MULTI_USER_PAY:{
				Log.i("error", "多人支付");
				try{
					ArrayList<HashMap<String, Object>> paramList = (ArrayList<HashMap<String, Object>>) task.getTaskParam().get("sender");
					ArrayList<HashMap<String, Object>> tempList = new ArrayList<HashMap<String, Object>>();
					for(int i = 0; i < paramList.size(); i ++)
					{
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("payer", paramList.get(i).get("name"));
						map.put("amount", paramList.get(i).get("money"));
						tempList.add(map);
					}
					futurePayment.multiplePay(tempList);
				}catch (PaymentException e) {
					msg.obj = e;
				}
				break;
			}
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		handler.sendMessage(msg);
		allTask.remove(task);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.i("error", " 服务初始化中");
		super.onCreate();
		this.isRun = true;
		Thread t = new Thread(this);
		t.start();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("error", "服务启动中");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.isRun = false;
	}

	public static void exitApp(Context context) {
		for (Activity ac : allActivities) {
			ac.finish();
		}
		Intent it = new Intent("com.billme.logic.MainService");
		context.stopService(it);
	}

	public static FuturePayment getFuturePayment() {
		return futurePayment;
	}

	public static User getUser() {
		return futurePayment.getUser();
	}

	public static Activity getActivityByName(String name) {
		for (Activity ac : allActivities) {
			if (ac.getClass().getName().indexOf(name) >= 0) {
				return ac;
			}
		}
		return null;
	}

	public static void AlertNetError(final Context con) {
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle(R.string.Error);
		ab.setMessage(R.string.NoSignalException);
		ab.setNegativeButton(R.string.btn_exit, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				MainService.exitApp(con);
			}
		});

		ab.setPositiveButton(R.string.btn_setnet, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				con.startActivity(new Intent(
						android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		ab.create().show();
	}

	@Override
	public void run() {
		while (isRun) {
			Task lastTask = null;
			synchronized (allTask) {
				if (allTask.size() > 0) {
					lastTask = allTask.get(0); // 取任务
					doTask(lastTask); // 执行任务
				}
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
