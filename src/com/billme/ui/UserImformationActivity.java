package com.billme.ui;

import java.util.ArrayList;

import android.os.Bundle;

import com.billme.logic.BillMeActivity;
import com.billme.widget.TradingRecordAdapter;
import com.billme.widget.TradingRecordListView;
import com.billme.widget.TradingRecordListView.IXListViewListener;

public class UserImformationActivity extends BaseActivity implements
		BillMeActivity, IXListViewListener {
	TradingRecordListView expandableListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userimformation);

		ArrayList<String[]> tempGroup = new ArrayList<String[]>();
		ArrayList<ArrayList<String[]>> tempItem = new ArrayList<ArrayList<String[]>>();
		for (int i = 0; i < 12; ++i) {
			String[] tempStr = new String[] { i + " mounth", "100" + i,
					"500" + i };
			tempGroup.add(tempStr);
			ArrayList<String[]> temp = new ArrayList<String[]>();
			for (int j = 0; j <= i; ++j) {
				tempStr = new String[] { "a test", "2012.01." + j, "200" + j,
						"deal!" };
				temp.add(tempStr);
			}
			tempItem.add(temp);
		}

		expandableListView = (TradingRecordListView) findViewById(R.id.lv_trading_records);
		TradingRecordAdapter adapter = new TradingRecordAdapter(this,
				tempGroup, tempItem, expandableListView);
		// expandableListView.setPinnedHeaderView(getLayoutInflater().inflate(
		// R.layout.trading_record_group_header, expandableListView, false));
		expandableListView.setAdapter(adapter);
		expandableListView.setXListViewListener(this);
		expandableListView.expandGroup(0);
		// expandableListView.setOnScrollListener(adapter);

		// expandableListView
		// .setOnGroupExpandListener(new OnGroupExpandListener() {
		//
		// @Override
		// public void onGroupExpand(int groupPosition) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//
		// expandableListView
		// .setOnGroupCollapseListener(new OnGroupCollapseListener() {
		//
		// @Override
		// public void onGroupCollapse(int groupPosition) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}
}

/*
 * public class UserImformationActivity extends BaseActivity implements
 * BillMeActivity, IXListViewListener {
 * 
 * public static final int OUT = -1; public static final int SUCCESS = 1;
 * private QuickContactBadge userIcon; private TextView infoUserName; private
 * TextView infoUserBalance; private TradingRecordListView lvTradingRecords;
 * 
 * private SimpleAdapter myTradingRecords; private ArrayList<HashMap<String,
 * String>> myList; private Handler mHandler;
 * 
 * @Override protected void onCreate(Bundle savedInstanceState) { // TODO
 * Auto-generated method stub super.onCreate(savedInstanceState);
 * setContentView(R.layout.activity_userimformation);
 * 
 * userIcon = (QuickContactBadge) this.findViewById(R.id.qcb_userIcon);
 * 
 * infoUserName = (TextView) this .findViewById(R.id.tv_imformation_userName);
 * // infoUserName.setText(MainService.getUser().getName());
 * 
 * infoUserBalance = (TextView) this
 * .findViewById(R.id.tv_imformation_acountBalance); //
 * infoUserBalance.setText(MainService.getUser().getUserId());
 * 
 * lvTradingRecords = (TradingRecordListView) this
 * .findViewById(R.id.lv_trading_records); myList = new
 * ArrayList<HashMap<String, String>>();
 * 
 * myTradingRecords = new SimpleAdapter(this, myList,
 * R.layout.trading_record_list_item, new String[] { "commodityName",
 * "tradeDate", "tradeMoney", "tradeState", }, new int[] { R.id.tv_record_name,
 * R.id.tv_trading_date, R.id.tv_trading_money, R.id.tv_trading_state });
 * lvTradingRecords.setOnItemClickListener(new OnItemClickListener() {
 * 
 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
 * long arg3) { // TODO Auto-generated method stub
 * 
 * } }); lvTradingRecords.setAdapter(myTradingRecords);
 * lvTradingRecords.setPullLoadEnable(true);
 * lvTradingRecords.setXListViewListener(this);
 * 
 * mHandler = new Handler(); MainService.allActivities.add(this); }
 * 
 * @Override protected void onResume() { // TODO Auto-generated method stub
 * super.onResume();
 * 
 * for (int i = 0; i < 15; i++) { HashMap<String, String> map = new
 * HashMap<String, String>(); map.put("commodityName", "a commodity");
 * map.put("tradeDate", "2013.1.1"); map.put("tradeMoney", "10000");
 * map.put("tradeState", "Deal!"); myList.add(map); }
 * 
 * }
 * 
 * @Override public void init() { // TODO Auto-generated method stub
 * 
 * }
 * 
 * @Override public void refresh(Object... param) { switch (((Integer)
 * param[0]).intValue()) { case OUT:
 * Toast.makeText(UserImformationActivity.this, "Out of Boundary",
 * Toast.LENGTH_SHORT).show(); break; case SUCCESS: { // LinkedList<TradeRecord>
 * tradeRecord = MainService.getUser() // .getTradeRecords(); // myList.clear();
 * 
 * // for (int i = 0; i < tradeRecord.size(); i++) {
 * 
 * // HashMap<String, String> map = new HashMap<String, String>(); //
 * TradeRecord tempRecord = tradeRecord.get(i); // map.put("commodityName",
 * tempRecord.getReceiver()); // map.put("tradeDate", tempRecord.getDate()); //
 * map.put("tradeMoney", String.valueOf(tempRecord.getAmount())); //
 * map.put("tradeState", "Deal!"); // myList.add(map); // }
 * myTradingRecords.notifyDataSetChanged(); } } }
 * 
 * class bankCardsOnClickListener implements OnClickListener {
 * 
 * @Override public void onClick(View arg0) { // TODO Auto-generated method stub
 * 
 * }
 * 
 * }
 * 
 * private void onLoad() { lvTradingRecords.stopRefresh();
 * lvTradingRecords.stopLoadMore(); lvTradingRecords.setRefreshTime("wwww"); }
 * 
 * @Override public void onRefresh() { mHandler.postDelayed(new Runnable() {
 * 
 * @Override public void run() { for (int i = 0; i < 5; i++) { HashMap<String,
 * String> map = new HashMap<String, String>(); map.put("commodityName",
 * "a commodity"); map.put("tradeDate", "2013.1.1"); map.put("tradeMoney",
 * "10000"); map.put("tradeState", "Deal!"); myList.add(map); }
 * myTradingRecords.notifyDataSetChanged(); onLoad(); } }, 2000);
 * 
 * }
 * 
 * @Override public void onLoadMore() {
 * 
 * Log.i("error", "onLoad"); HashMap<String, String> map = new HashMap<String,
 * String>(); map.put("commodityName", "a commodity"); map.put("tradeDate",
 * "2013.1.1"); map.put("tradeMoney", "10000"); map.put("tradeState", "Deal!");
 * myList.add(map); myTradingRecords.notifyDataSetChanged(); onLoad(); }
 * 
 * }
 */