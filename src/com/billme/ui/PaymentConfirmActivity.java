package com.billme.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.billme.logic.BillMeActivity;
import com.billme.widget.MyListViewAdapter;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
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
		addTitle("����ȷ��");
		
		text = (TextView)findViewById(R.id.tv_payment_confirm_text);
		choiceList = (ListView)findViewById(R.id.lv_payment_confirm_choice);
		peopleList = (GridView)findViewById(R.id.gv_payment_confirm_people);
		friendButton = (Button)findViewById(R.id.btn_payment_confirm_friend);
		codeButton = (Button)findViewById(R.id.btn_payment_confirm_code);
		
		//�õ�������Ϣ����ʾ������
		
		
		
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
		map1.put("text", "����֧��");
		map1.put("end", R.drawable.nav_left);
		cl.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("text", "����֧��");
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
				//ѡ�е���֧��
					break;
				case 1:
					if(mutipay == false)
					//ѡ�ж���֧��
					{
						cl.get(1).put("end", R.drawable.nav_left);
						peopleList.setVisibility(View.VISIBLE);
						mutipay = true;
						choiceAdapter.notifyDataSetChanged();
					}
					else
					{
						cl.get(1).put("end", R.drawable.nav_left);
						peopleList.setVisibility(View.INVISIBLE);
						mutipay = false;
						choiceAdapter.notifyDataSetChanged();
					}
				}
			}
			
		});
	}

	private void bindPeopleAdapter()
	{
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("icon", R.drawable.back);
		pl.add(map3);
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
//					//��Ӻ���
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

}
