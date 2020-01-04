package com.Bc.ks.download;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.os.*;
import android.view.*;
import cn.bmob.v3.*;
import cn.bmob.v3.listener.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.datatype.*;
import java.io.*;
import android.widget.*;

public class MainActivity extends Activity 
{
	private ProgressDialog dialog;
	private long suiji;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		//取消标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏显示
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
		//设置竖屏显示
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);
		suiji=  (long)(Math.random() * 6);
		//初始化SDK
		Bmob.initialize(this,"3d59907b23d945195c9dcfd2e6f87a30");
		SharedPreferences bi=getSharedPreferences("first",0);
		bi.getBoolean("first",false);
		//管你是不是第一次用都检查更新
//		if(bi.getBoolean("first",false)){
//			jiancha();
//		}else{
//			jcgx();
//		}
//	
		if(suiji==5&&suiji==4&&suiji==3){
			jcgx();
		}else{
			jiancha();
		}
		//jcgx();
    }
	void fir(){
		AlertDialog.Builder npo=new AlertDialog.Builder(MainActivity.this);
		npo.setCancelable(false);
		npo.setTitle("免责声明");
		npo.setMessage("快手视频下载软件免责声明请仔细阅读:\n1.本软件目前仅能下载快手视频，快手图片等目前可以解析但无法下载，非快手链接无法解析，请注意。\n2.下载后的作品将保存到快手本地作品集，在SD卡目录为:SD卡根目录/gifshow/ 文件名称将以:BC+系统当前时间命名。\n3.本软件由菠菜独立开发，永久免费如果你是买来的那你就是被骗了。\n4为了防止软件被某些人盗用，特在此加上版权©菠菜QQ3135419729，应用已经全面加密请放心使用!\n\n该弹窗仅第一次使用显示。菠菜网络传媒，我们一直用心在做!");
		npo.setPositiveButton("我已阅读", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: I
					jiancha();
				}
			});
		npo.setNegativeButton("取消", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					finish();
					// TODO: Implement this method
				}
			});
			npo.show();
	}
	void jcgx(){
		//检查更新
		BmobQuery<BmobQ> query=new BmobQuery<BmobQ>();
		query.getObject("KuDrCCCG", new QueryListener<BmobQ>(){

				@Override
				public void done(BmobQ p1, BmobException p2)
				{
					if(p2==null){
						if(p1.getcode().toString().equals("1.0")){
							jiancha();
//							SharedPreferences bio=getSharedPreferences("first",0);
//							SharedPreferences.Editor kp=bio.edit();
//							kp.putBoolean("first",true);
//							kp.commit();
						}else{
							//提示更新
							up(p1.getmsg(),p1.getbf(),p1.getgzgx());
						}
					}else{
						jiancha();
					}
				}
			});
	}
	void up(String msg,final BmobFile bf,final boolean b){
		AlertDialog.Builder hu=new AlertDialog.Builder(MainActivity.this);
		hu.setTitle("应用更新");
		hu.setCancelable(b);
		hu.setMessage(msg);
		hu.setPositiveButton("更新", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
				
					bf.download(new File(Environment.getExternalStorageDirectory() + "/Zero/", "快手视频下载.apk"), new DownloadFileListener(){

							@Override
							public void done(String p1, BmobException p2)
							{
							if(p2==null){
								hideDialog();
						
									try{
										File file=new File(Environment.getExternalStorageDirectory()+"/Zero/"+"快手视频下载.apk");
										Intent intent = new Intent(Intent.ACTION_VIEW);
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										intent.setDataAndType(Uri.fromFile(file),
															  "application/vnd.android.package-archive");
										startActivity(intent);
									}catch(Exception e){
										Toast.makeText(MainActivity.this,"出现异常"+e.getMessage(),Toast.LENGTH_LONG).show();
										finish();
									}
								
							}else{
								Toast.makeText(MainActivity.this,"下载失败，错误原因:"+p2.getMessage(),Toast.LENGTH_LONG).show();
								finish();
							}
							}

							@Override
							public void onProgress(Integer p1, long p2)
							{
								showDialog("正在下载中..."+p1+"%");
							}
						});
				}
			});
		hu.setNegativeButton("取消", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					if(b){
						jiancha();
					}else{
						finish();
					}
				}
			});
		hu.show();
//		AlertDialog bk=hu.create();
//		bk.show();
//		bk.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
//
//				@Override
//				public void onClick(View p1)
//				{
//					// TODO: Implement this method
//				}
//			});
	}
	void jiancha(){
		SignCheck signCheck = new SignCheck(this,"9E:B3:94:54:C0:60:71:78:16:8A:61:F8:87:99:CB:A3:F0:70:B1:8F");
		if(signCheck.check()) {
			//TODO 签名正常
			//数据加载
			go();
		
			
		}else                {
			//TODO 签名不正确
			jksdialog();
		}
		
	}
	void showDialog(String message) {
		try {
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setTitle("数据加载中...");
				dialog.setCancelable(true);
			}
			dialog.setMessage(message);
			dialog.show();
		} catch (Exception e) {
			// 在其他线程调用dialog会报错
			e.printStackTrace();
		}
	}
	void hideDialog() {
		//用于下载完成后隐藏加载框
		if (dialog != null && dialog.isShowing())
			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	void jksdialog(){
		AlertDialog.Builder jks=new AlertDialog.Builder(this);
		jks.setTitle("应用安全");
		jks.setMessage("应用已被修改出现版本安全问题！请立即卸载，否则出现任何问题与开发者无关！");
		jks.setCancelable(false);
		jks.setNegativeButton("确定", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_DELETE); 
					intent.setData(Uri.parse("package:" + "com.Bc.ks.download")); 
					startActivity(intent);
					finish();
					// TODO: Implement this method
				}
			});
		jks.setPositiveButton("取消", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					finish();
					// TODO: Implement this method
				}
			});
		jks.setNeutralButton("联系作者", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					Intent intent_d= new Intent();        
					intent_d.setAction("android.intent.action.VIEW");    
					Uri content_url = Uri.parse("http://qm.qq.com/cgi-bin/qm/qr?k=X4K6tj6lqpGrt2iGe6AEr7X7zuN-A0ap");   
					intent_d.setData(content_url);  
					startActivity(intent_d);
					finish();
					// TODO: Implement this method
				}

			});
		jks.show();
	}
	void go(){
		new Handler()
			. postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					//如果跳转到另外一个Activity，可以在这里用Intent
					startActivity(new Intent(MainActivity.this,VideoActivity.class));
					finish();
				}
			}, 1000);
	}
}
