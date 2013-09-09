package com.billme.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.billme.logic.BillMeActivity;

import com.billme.logic.MainService;
import com.billme.widget.MyCouponAdapter;
import com.billme.widget.MyListViewAdapter;
import com.futurePayment.constant.Task;
import com.futurePayment.model.Coupon;
import com.futurePayment.model.Friend;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentConfirmActivity extends BaseActivity implements
		BillMeActivity {
	public final static int APPLY_SUCCESS = 1;
	public final static int APPLY_FAILURE = -1;
	public final static int QUERY_SUCCESS = 2;
	public final static int QUERY_FAILURE = -2;

	private TextView text = null;
	private ListView choiceList = null;
	private GridView peopleList = null;
	private MyListViewAdapter choiceAdapter = null;
	private ArrayList<HashMap<String, Object>> cl = new ArrayList<HashMap<String, Object>>();
	private MyListViewAdapter peopleAdapter = null;
	private ArrayList<HashMap<String, Object>> pl = new ArrayList<HashMap<String, Object>>();
	private ArrayList<Friend> fl = new ArrayList<Friend>();
	private Button friendButton = null;
	private Button codeButton = null;
	private ProgressDialog pd = null;

	private String receiver;
	private double money;
	private String method;
	boolean mutipay = false;

	private MyCouponAdapter couponAdapter = null;
	private LinkedList<Coupon> ll = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_confirm);
		addTitle("����ȷ��");

		text = (TextView) findViewById(R.id.tv_payment_confirm_text);
		choiceList = (ListView) findViewById(R.id.lv_payment_confirm_choice);
		peopleList = (GridView) findViewById(R.id.gv_payment_confirm_people);
		friendButton = (Button) findViewById(R.id.btn_payment_confirm_friend);
		codeButton = (Button) findViewById(R.id.btn_payment_confirm_code);

		// �õ�������Ϣ����ʾ������
		Intent intent = getIntent();
		receiver = intent.getStringExtra("receiver");
		money = intent.getDoubleExtra("money", 0);
		method = intent.getStringExtra("method");
		text.setText("������������" + receiver + "֧��" + money + "Ԫ");

		bindChoiceAdapter();
		bindPeopleAdapter();

		codeButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});
		friendButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<String> name = new ArrayList<String>();
				for (int i = 0; i < pl.size(); i++) {
					name.add((String) pl.get(i).get("text"));
				}
				Intent intent = new Intent();
				intent.putExtra("name", name);
				intent.setClass(PaymentConfirmActivity.this,
						FriendActivity.class);
				startActivityForResult(intent, 0);
			}

		});
		new AlertDialog.Builder(this).setTitle("�Ƿ�ʹ���Ż�ȯ")
				.setPositiveButton("��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (pd == null) {
							pd = new ProgressDialog(PaymentConfirmActivity.this);
						}
						pd.setMessage("Loading..");
						pd.show();
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("name", receiver);
						map.put("money", money);
						Task task = new Task(Task.TASK_GET_COUPON, map);
						MainService.newTask(task);
					}

				}).setNegativeButton("��", null).create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_payment_confirm, menu);
		return true;
	}

	private void bindChoiceAdapter() {
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("text", "����֧��");
		map1.put("end", R.drawable.nav_left);
		cl.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("text", "����֧��");
		map2.put("end", R.drawable.nav_left);
		cl.add(map2);
		choiceAdapter = new MyListViewAdapter(this, cl);
		choiceList.setAdapter(choiceAdapter);
		choiceList.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					// ѡ�е���֧��
					// ��תҳ��
					Intent intent = new Intent();
					// ���븶����

					// �����տ���
					intent.putExtra("receiver", receiver);
					// ������
					intent.putExtra("money", money);
					intent.putExtra("method", method);
					intent.setClass(PaymentConfirmActivity.this,
							PaymentActivity.class);
					startActivity(intent);
					break;
				case 1:
					if (mutipay == false)
					// ѡ�ж���֧��
					{
						cl.get(1).put("end", R.drawable.nav_left);
						peopleList.setVisibility(View.VISIBLE);
						mutipay = true;
						choiceAdapter.notifyDataSetChanged();
					} else {
						if (pd == null) {
							pd = new ProgressDialog(PaymentConfirmActivity.this);
						}
						pd.setMessage("Sending..");
						pd.show();
						HashMap<String, Object> param = new HashMap<String, Object>();
						ArrayList<HashMap<String, Object>> tempList = new ArrayList<HashMap<String, Object>>();
						for (int i = 0; i < pl.size(); i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("name", fl.get(i).getName());
							// ��̯��ʽ��Ҫ����
							map.put("money", money / (pl.size() + 1));
							tempList.add(map);
						}
						param.put("sender", tempList);
						Task task = new Task(Task.TASK_MULTI_USER_PAY, param);
						MainService.newTask(task);
					}
				}
			}

		});
	}

	private void bindPeopleAdapter() {
		// HashMap<String, Object> map3 = new HashMap<String, Object>();
		// map3.put("icon", R.drawable.back);
		// pl.add(map3);
		peopleAdapter = new MyListViewAdapter(this, pl);
		peopleList.setAdapter(peopleAdapter);
		peopleList.setOnItemClickListener(new GridView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// if(arg2 == pl.size() - 1)
				// {
				// //��Ӻ���
				// }
				// else
				// {
				pl.remove(arg2);
				peopleAdapter.notifyDataSetChanged();
				// }
			}
		});
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		switch (((Integer) param[0]).intValue()) {
		case APPLY_SUCCESS: {
			Toast.makeText(this, "Send messages successfully",
					Toast.LENGTH_SHORT).show();
			// ��תҳ��
			Intent intent = new Intent();
			// �����տ���
			intent.putExtra("receiver", receiver);
			// ������
			intent.putExtra("money", money / (pl.size() + 1));
			intent.putExtra("method", method);
			intent.setClass(PaymentConfirmActivity.this, PaymentActivity.class);
			startActivity(intent);
		}
			break;
		case APPLY_FAILURE: {
			pd.cancel();
			Toast.makeText(this, "Send messages failurily", Toast.LENGTH_SHORT)
					.show();
		}
			break;
		case QUERY_SUCCESS: {
			ll = (LinkedList<Coupon>) param[1];
			couponAdapter = new MyCouponAdapter(PaymentConfirmActivity.this, ll);

			new AlertDialog.Builder(this).setTitle("ѡ���Ż�ȯ")
			// .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// // TODO Auto-generated method stub
			// if (pd == null) {
			// pd = new ProgressDialog(PaymentConfirmActivity.this);
			// }
			// pd.setMessage("Loading..");
			// pd.show();
			// HashMap<String, Object> map = new HashMap<String, Object>();
			// map.put("name", receiver);
			// map.put("money", money);
			// Task task = new Task(Task.TASK_GET_COUPON, map);
			// MainService.newTask(task);
			// }
			//
			// })
			// .setNegativeButton("ȡ��", null)
					.setAdapter(couponAdapter,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// ѡ���Ż�ȯ
									ll.get(which);
								}

							}).create();
		}
			break;
		case QUERY_FAILURE: {
			pd.cancel();
			Toast.makeText(this, "Get coupons failurily", Toast.LENGTH_SHORT)
					.show();
		}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		fl = (ArrayList<Friend>) data.getSerializableExtra("friend");
		for (int i = 0; i < fl.size(); i++) {
			Friend temp = fl.get(i);
			HashMap<String, Object> map3 = new HashMap<String, Object>();
			// ���ҷ���ͷ��
			map3.put("icon", temp.getPath());
			pl.add(map3);
		}
		peopleAdapter.notifyDataSetChanged();
	}
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // TODO Auto-generated method stub
	// super.onActivityResult(requestCode, resultCode, data);
	// if (resultCode == RESULT_OK) {
	// Bundle bundle = data.getExtras();
	// String scanResult = bundle.getString("result");
	//
	// String[] tempString = scanResult.split(",");
	// for (int i = 0; i < tempString.length; ++i) {
	// Log.i("error", tempString[i]);
	// }
	// }
	// }
}
