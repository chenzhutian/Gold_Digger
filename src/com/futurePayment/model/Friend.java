package com.futurePayment.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements Parcelable
{
private String name = null;
private String path = null;
private int size;
public String getName()
{
	return name;
}
public void setName(String name)
{
	this.name = name;
}
public String getPath()
{
	return path;
}
public void setPath(String path)
{
	this.path = path;
}
@Override
public int describeContents()
{
	// TODO Auto-generated method stub
	return 0;
}
@Override
public void writeToParcel(Parcel arg0, int arg1)
{
	// TODO Auto-generated method stub
	
}
public int getSize()
{
	return size;
}
public void setSize(int size)
{
	this.size = size;
}
}
