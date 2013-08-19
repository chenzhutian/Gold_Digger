package com.billme.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.billme.ui.R;
import com.billme.widget.TradingRecordListView.PinnedHeaderAdapter;

public class TradingRecordAdapter extends BaseExpandableListAdapter implements
		PinnedHeaderAdapter {

	private ArrayList<String[]> group;
	private ArrayList<ArrayList<String[]>> item;
	// private Context context;
	private LayoutInflater inflater;
	private TradingRecordListView listView;

	public TradingRecordAdapter(Context context, ArrayList<String[]> group,
			ArrayList<ArrayList<String[]>> item, TradingRecordListView listView) {
		// this.context = context;
		inflater = LayoutInflater.from(context);
		this.group = group;
		this.item = item;
		this.listView = listView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return item.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition * 10000 + childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater
					.inflate(R.layout.trading_record_listview_item, null);
		}
		TextView recordName = (TextView) view
				.findViewById(R.id.tv_TRLVitem_record_name);
		TextView tradeDate = (TextView) view
				.findViewById(R.id.tv_TRLVitem_trading_date);
		TextView tradePrice = (TextView) view
				.findViewById(R.id.tv_TRLVitem_trading_price);
		TextView tradeState = (TextView) view
				.findViewById(R.id.tv_TRLVitem_trading_state);
		String[] tempItem = item.get(groupPosition).get(childPosition);
		recordName.setText(tempItem[0]);
		tradeDate.setText(tempItem[1]);
		tradePrice.setText(tempItem[2]);
		tradeState.setText(tempItem[3]);
		// recordName.setOnClickListener(new ChildOnClickListener());
		// tradeDate.setOnClickListener(new ChildOnClickListener());
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition > -1 && groupPosition < item.size()) {
			return item.get(groupPosition).size();
		}
		return -1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return group.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.trading_record_listview_group,
					null);
		}
		String[] tempItem = group.get(groupPosition);
		((TextView) view.findViewById(R.id.tv_TRLVgroup_mounth_label))
				.setText(tempItem[0]);
		((TextView) view.findViewById(R.id.tv_TRLVgroup_income_amount_label))
				.setText(tempItem[1]);
		((TextView) view.findViewById(R.id.tv_TRLVgroup_outcome_amount_label))
				.setText(tempItem[2]);

		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public int getPinnedHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);

		if (childPosition == (childCount - 1)) {
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1
				&& !listView.isGroupExpanded(groupPosition)) {
			return PINNED_HEADER_GONE;
		} else {

			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configurePinnedHeader(View header, int groupPosition,
			int childPosition, int alpha) {

		if (groupPosition > -1 && groupPosition < group.size()) {
			String[] str = group.get(groupPosition);
			((TextView) header.findViewById(R.id.tv_TRLVgroup_mounth_label))
					.setText(str[0]);
			((TextView) header
					.findViewById(R.id.tv_TRLVgroup_income_amount_label))
					.setText(str[1]);
			((TextView) header
					.findViewById(R.id.tv_TRLVgroup_outcome_amount_label))
					.setText(str[2]);
		}
	}

	class ChildOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Log.i("error", "child has been touch");
		}

	}

}
