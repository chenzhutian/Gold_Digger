package com.billme.logic;

import java.sql.Date;
import java.util.ArrayList;
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
import com.billme.ui.LoginActivity;
import com.billme.ui.MainActivity;
import com.billme.ui.R;
import com.billme.ui.RegistActivity;

import com.futurePayment.model.*;

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
		// �ص�����
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Task.TASK_USER_LOGIN: {
				// ��½
				Log.i("error", "��½�ص���");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("LoginActivity");
//				if (msg.obj instanceof PaymentException) {
//					PaymentException e = (PaymentException) msg.obj;
//					// TODO ����ʧ����Ϣ
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
				Log.i("error", "��ȡ�û���Ϣ�ص���");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("MainActivity");
				ba.refresh(new Integer(MainActivity.REFRESH_USERINFO), msg.obj);
				break;
			}
			case Task.TASK_CHECK_NAME_AVAILABLE: {
				Log.i("error", "�����û����Ƿ���Ч�ص���");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("RegistActivity");
				if (msg.obj instanceof PaymentException) {
					PaymentException e = (PaymentException) msg.obj;
					// TODO ����ʧ����Ϣ
					ba.refresh(new Integer(RegistActivity.NAME_NOT_AVAILABLE),
							e);
				} else {
					ba.refresh(new Integer(RegistActivity.NAME_AVAILABLE), null);
				}
				break;
			}
			case Task.TASK_USER_REGIST: {
				Log.i("error", "�û�ע��ص���");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("RegistActivity");
				if (msg.obj instanceof PaymentException) {
					PaymentException e = (PaymentException) msg.obj;
					// TODO ����ʧ����Ϣ
					ba.refresh(new Integer(RegistActivity.REGIST_FAILURE), e);
				} else {
					ba.refresh(new Integer(RegistActivity.REGIST_SUCCESS), null);
				}
				break;
			}
			case Task.TASK_GET_BANK_CARD: {
				Log.i("error", "��ȡ���п��б�ص���");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("BankCardActivity");
				if (msg.obj instanceof PaymentException) {
					PaymentException e = (PaymentException) msg.obj;
					// TODO ����ʧ����Ϣ
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
				Log.i("error", "������п��ص���");
				BillMeActivity ba = (BillMeActivity) MainService
						.getActivityByName("BankCardActivity");
				if (msg.obj instanceof PaymentException) {
					PaymentException e = (PaymentException) msg.obj;
					// TODO ����ʧ����Ϣ
					ba.refresh(new Integer(
							BankCardActivity.ADD_BANK_CARD_FAILURE), e);
				} else if (msg.obj instanceof ArrayList<?>) {
					ba.refresh(new Integer(
							BankCardActivity.ADD_BANK_CARD_SUCCESS), msg.obj);
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
//				Log.i("error", "��½��");
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
				Log.i("error", "��ȡ�û���Ϣ");

				msg.obj = task.getTaskParam();
				break;
			}
			case Task.TASK_CHECK_NAME_AVAILABLE: {
				Log.i("error", "�����û����Ƿ���Ч");
				try {
					futurePayment.checkUserExistence((String) task
							.getTaskParam().get("name"));
				} catch (PaymentException e) {
					msg.obj = e;
				}
				break;
			}
			case Task.TASK_USER_REGIST: {
				Log.i("error", "�û�ע��");
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
				Log.i("error", "��ȡ���п��б�");
				try {
					msg.obj = futurePayment.queryAccount();
				} catch (PaymentException e) {
					msg.obj = e;
				}
				break;
			}
			case Task.TASK_ADD_BANK_CARD: {
				Log.i("error", "������п�");
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
		Log.i("error", " �����ʼ����");
		super.onCreate();
		this.isRun = true;
		Thread t = new Thread(this);
		t.start();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("error", "����������");
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
					lastTask = allTask.get(0); // ȡ����
					doTask(lastTask); // ִ������
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
