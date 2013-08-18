package com.billme.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.billme.logic.BillMeActivity;
import com.billme.widget.MyListViewAdapter;

import com.futurePayment.model.Friend;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PaymentConfirmActivity extends BaseActivity implements BillMeActivity
{
	
	private TextView text = null;
	private ListView choiceList = null;
	private GridView peopleList = null;
	private MyListViewAdapter choiceAdapter = null;
	private ArrayList<HashMap<String, Object>> cl = new ArrayList<HashMap<String, Object>>();
	private MyListViewAdapter peopleAdapter = null;
	private ArrayList<HashMap<String, Object>> pl = new ArrayList<HashMap<String, Object>>();
	private Button friendButton = null;
	private Button codeButton = null;
	
	boolean mutipay = false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_confirm);
		addTitle("交易确认");
		
		text = (TextView)findViewById(R.id.tv_payment_confirm_text);
		choiceList = (ListView)findViewById(R.id.lv_payment_confirm_choice);
		peopleList = (GridView)findViewById(R.id.gv_payment_confirm_people);
		friendButton = (Button)findViewById(R.id.btn_payment_confirm_friend);
		codeButton = (Button)findViewById(R.id.btn_payment_confirm_code);
		
		//得到交易信息并显示。。。
		
		
		
		bindChoiceAdapter();
		bindPeopleAdapter();
		
		codeButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				
			}
			
		});
		friendButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ArrayList<String> name = new ArrayList<String>();
				for(int i = 0; i < pl.size(); i++)
				{
					name.add((String)pl.get(i).get("text"));
				}
				Intent intent = new Intent();
				intent.putExtra("name", name);
				intent.setClass(PaymentConfirmActivity.this, FriendActivity.class);
				startActivityForResult(intent, 0);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_payment_confirm, menu);
		return true;
	}
	
	private void bindChoiceAdapter()
	{
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("text", "单人支付");
		map1.put("end", R.drawable.nav_left);
		cl.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("text", "多人支付");
		map2.put("end", R.drawable.nav_left);
		cl.add(map2);
		choiceAdapter = new MyListViewAdapter(this, cl);
		choiceList.setAdapter(choiceAdapter);
		choiceList.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				// TODO Auto-generated method stub
				switch(arg2)
				{
				case 0:
				//选中单人支付
					//跳转页面
					break;
				case 1:
					if(mutipay == false)
					//选中多人支付
					{
						cl.get(1).put("end", R.drawable.nav_left);
						peopleList.setVisibility(View.VISIBLE);
						mutipay = true;
						choiceAdapter.notifyDataSetChanged();
					}
					else
					{
						//跳转页面
//						cl.get(1).put("end", R.drawable.nav_left);
//						peopleList.setVisibility(View.INVISIBLE);
//						mutipay = false;
//						choiceAdapter.notifyDataSetChanged();
					}
				}
			}
			
		});
	}

	private void bindPeopleAdapter()
	{
//		HashMap<String, Object> map3 = new HashMap<String, Object>();
//		map3.put("icon", R.drawable.back);
//		pl.add(map3);
		peopleAdapter = new MyListViewAdapter(this, pl);
		peopleList.setAdapter(peopleAdapter);
		peopleList.setOnItemClickListener(new GridView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				// TODO Auto-generated method stub
//				if(arg2 == pl.size() - 1)
//				{
//					//添加好友
//				}
//				else
//				{
					pl.remove(arg2);
					peopleAdapter.notifyDataSetChanged();
//				}
			}			
		});
	}
	@Override
	public void init()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object... param)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		ArrayList<Friend> al = data.getParcelableArrayListExtra("friend");
		for(int i = 0; i < al.size(); i ++)
		{
			Friend temp = al.get(i);
			HashMap<String, Object> map3 = new HashMap<String, Object>();
			//查找放置头像
			map3.put("icon", temp.getPath());
			pl.add(map3);
		}
		peopleAdapter.notifyDataSetChanged();
	}

}
