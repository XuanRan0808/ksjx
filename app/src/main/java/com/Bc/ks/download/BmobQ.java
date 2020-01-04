package com.Bc.ks.download;
import cn.bmob.v3.*;
import cn.bmob.v3.datatype.*;

public class BmobQ extends BmobObject
{
	BmobFile bf;
	String code;
	String msg;
	boolean qzgx;
	public boolean getgzgx(){
		return qzgx;
	}
	public BmobFile getbf(){
		return bf;
	}
	public String getcode(){
		return code;
	}
	public String getmsg(){
		return msg;
	}
}
