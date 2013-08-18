package com.billme.widget;

import java.util.ArrayList;
import java.util.HashMap;

import com.billme.logic.MainService;
import com.billme.ui.R;
import com.billme.util.FileUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListViewAdapter extends BaseAdapter
{
	private Context context = null;
	private ArrayList<HashMap<String, Object>> list;
	private LayoutInflater inflater = null;
	/**
	 * 
	 * @param context the context of current activity
	 * @param list every hashmap have three keys:"icon","text","end"
	 * @param resource the list item layout
	 */
	public MyListViewAdapter(Context context, ArrayList<HashMap<String, Object>> list)
	{
		super();
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	public String getText(int n)
	{
		return (String) list.get(n).get("text");
	}
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = new ViewHolder();
		HashMap<String,Object> map = list.get(position);
		// TODO Auto-generated method stub
		if(convertView == null)
		{		
			convertView = inflater.inflate(R.layout.my_list_item, null);	
		}
		viewHolder.icon = (ImageView)convertView.findViewById(R.id.iv_mylistitem_icon);
		viewHolder.text = (TextView)convertView.findViewById(R.id.tv_mylistitem_text);
		viewHolder.end = (ImageView)convertView.findViewById(R.id.iv_mylistitem_end);	
		if(map != null)
		{					
			if(map.get("icon") instanceof Integer)
			{
				viewHolder.icon.setImageDrawable(context.getResources().getDrawable((Integer)(map.get("icon"))));
				viewHolder.text.setText((String)map.get("text"));
				viewHolder.end.setImageDrawable(context.getResources().getDrawable((Integer)(map.get("end"))));
			}
			else if(map.get("icon") instanceof String)
			{
				//¥”sdø®∂¡»°
				FileUtil fu = new FileUtil(MainService.getUser().getName());
				viewHolder.icon.setImageDrawable(fu.readImageFromSD((String)map.get("icon")));
				viewHolder.text.setText((String)map.get("text"));
				viewHolder.end.setImageDrawable(context.getResources().getDrawable((Integer)(map.get("end"))));
			}
		}	
		return convertView;
	}
	
	private class ViewHolder
	{
		public ImageView icon = null;
		public TextView text = null;
		public ImageView end = null;
	}

	public ArrayList<HashMap<String, Object>> getList()
	{
		return list;
	}
	public void setList(ArrayList<HashMap<String, Object>> list)
	{
		this.list = list;
	}
}
