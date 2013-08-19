package com.billme.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.billme.logic.BillMeActivity;
import com.billme.logic.MainService;
import com.billme.widget.MyListViewAdapter;
import com.futurePayment.constant.ResultCode;
import com.futurePayment.constant.Task;
import com.futurePayment.model.Friend;
import com.futurePayment.model.PaymentException;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FriendActivity extends BaseActivity implements BillMeActivity {
	public static final int GET_FRIEND_SUCCESS = 1;
	public static final int GET_FRIEND_FAILURE = -1;

	private ListView friendList = null;
	private Button okButton = null;
	private ProgressDialog pd = null;
	private MyListViewAdapter adapter = null;
	private ArrayList<HashMap<String, Object>> fl = new ArrayList<HashMap<String, Object>>();
	private ArrayList<Boolean> isChosen = new ArrayList<Boolean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);
		MainService.allActivities.add(this);
		addTitle("选择好友");

		friendList = (ListView) findViewById(R.id.lv_friend_list);
		okButton = (Button) findViewById(R.id.btn_friend_ok);

		if (MainService.getFuturePayment().getUser().getFriendList().size() == 0) {
			if (this.pd == null) {
				this.pd = new ProgressDialog(this);
			}
			pd.setMessage("Loading..");
			pd.show();
			Task task = new Task(Task.TASK_GET_FRIENDS);
			MainService.newTask(task);
		} else {
			bindAdapter();
		}

		okButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ArrayList<Friend> al = new ArrayList<Friend>();
				// TODO Auto-generated method stub
				for (int i = 0; i < fl.size(); i++) {
					if (isChosen.get(i)) {
						Friend temp = new Friend();
						temp.setName((String) fl.get(i).get("text"));
						temp.setPath((String) fl.get(i).get("icon"));
						al.add(temp);
					}
				}
				Intent intent = new Intent();
				intent.putExtra("friend", al);
				setResult(0, intent);
				finish();
			}
		});
	}

	private void bindAdapter() {
		Intent intent = getIntent();
		ArrayList<String> nl = intent.getStringArrayListExtra("name");
		ArrayList<Friend> al = MainService.getFuturePayment().getUser()
				.getFriendList();
		for (int i = 0; i < al.size(); i++) {
			Friend f = al.get(i);
			HashMap<String, Object> map = new HashMap<String, Object>();
			// 加入图片
			map.put("icon", f.getPath());
			map.put("text", f.getName());
			fl.add(map);
			// 初始化选择
			if (nl.contains(f.getName())) {
				map.put("back", R.drawable.click);
				isChosen.add(true);
			} else {
				isChosen.add(false);
			}
		}
		adapter = new MyListViewAdapter(this, fl);
		friendList.setAdapter(adapter);
		friendList.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// 选中某位好友
				if (isChosen.get(arg2) == true) {
					fl.get(arg2).remove("back");
					isChosen.set(arg2, false);
				} else {
					fl.get(arg2).put("back", R.drawable.click);
					isChosen.set(arg2, true);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_friend, menu);
		return true;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		switch (((Integer) param[0]).intValue()) {
		case GET_FRIEND_SUCCESS:
			bindAdapter();
			pd.cancel();
			break;
		case GET_FRIEND_FAILURE:
			int state = ((PaymentException) param[1]).getResultCode();
			String hint = null;
			switch (state) {
			case ResultCode.EMPTY:
				hint = "No friend";
				pd.cancel();
				Toast.makeText(this, hint, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

}
