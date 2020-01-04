package com.Bc.ks.download;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.icu.util.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import cn.bmob.v3.datatype.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import cn.jzvd.*;
import java.io.*;
import java.net.*;
import java.util.regex.*;

public class VideoActivity extends Activity
{
	//声明控件对象
	Button bn,bv;
	EditText ed;
	MyImageView tv;
	String vUrl;
	ProgressDialog hi;
	JZVideoPlayerStandard jzVideoPlayerStandard;
	ProgressDialog dialog;
	//图片与视频下载的切换
	boolean pv=false;
	//下载的格式
	String geshi=".mp4";
	String finalVideoUrl="http://m.newvideoservers.top/uploads/20180802/V8aH4iEgow/index.m3u8";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		//设置竖屏显示
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.video);
		
		ed=(EditText) findViewById(R.id.videoEditText1);
		bn=(Button) findViewById(R.id.videoButton1);
		bv=(Button) findViewById(R.id.videoButton2);
		tv=(MyImageView) findViewById(R.id.videoTextView1);
	
		dialog=new ProgressDialog(VideoActivity.this);
		//视频播放控件
	    jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplay);
		//设置视频地址和标题
		jzVideoPlayerStandard.setUp(finalVideoUrl
									, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "使用教程");
									
	    //将开始下载按钮隐藏
		bv.setVisibility(View.VISIBLE);
		tv.setVisibility(View.GONE);
		tv.setImageURL("https://pic.cnblogs.com/avatar/1142647/20170416093225.png");
		bn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if(ed.getText().toString().equals("")){
						toast("链接地址不可为空!");
					}else{
						jiazai();
					}
				}
			});	
		bn.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					qq();
					// TODO: Implement this method
					return false;
				}
			});
			
		bv.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					download("http://ip.h5.ri01.sycdn.kuwo.cn/da70ed5b0def349bd0808c9179fadadb/5dfc96c6/resource/n1/13/24/1468172227.mp3");
					//showDialog1("数据下载中....");
					// TODO: Implement this method
				}
			});
							
	}
	void download(String uri){
		//获取系统时间
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);//获取年份
		int month=ca.get(Calendar.MONTH);//获取月份 
		int day=ca.get(Calendar.DATE);//获取日
	    int minute=ca.get(Calendar.MINUTE);//分 
		int hour=ca.get(Calendar.HOUR);//小时 
		int second=ca.get(Calendar.SECOND);//秒
		BmobFile file=new BmobFile("测试数据","",uri);
		if(pv){
			geshi=".jpg";
		}else{
			geshi=".mp4";
		}
		file.download(new File(Environment.getExternalStorageDirectory() + "/gifshow/", "Bc" + year + month + day + hour + minute + second+geshi), new DownloadFileListener(){

				@Override
				public void done(String p1, final BmobException p2)
				{
					if(p2==null){
						hideDialog1();
						if(pv){
							toast("下载完成!图片保存路径:"+p1);
						}else{
						toast("下载完成!视频保存地址:"+p1);}
					}else{ 
					toast("出现异常");
						if(p2.getErrorCode()==404){
							//链接地址无法找到，提示切换下载模式
							error404();
						}else if(p2.getErrorCode()==9021){
							//网络连接错误
							toast("网络连接错误！");
						} else{
							//未知错误
							error(p2.getErrorCode(),p2.getMessage());
						}
					}
					
				}

				@Override
				public void onProgress(Integer p1, long p2)
				{
					showDialog1("文件下载中..."+p1+"%");
				}
			});
	}
	void error404(){
		AlertDialog.Builder np=new AlertDialog.Builder(VideoActivity.this);
		np.setCancelable(true);
		np.setTitle("下载错误");
		np.setMessage("该视频(图片)由于解析后的链接地址为404造成无法下载，请尝试点击右上角切换下载模式(切换后并不保证一定下载成功)");
		np.setPositiveButton("确定",null);
		np.show();
	}
	 void error(final int p2,final String msg){
		 AlertDialog.Builder ko=new AlertDialog.Builder(VideoActivity.this);
		 ko.setCancelable(false);
		 ko.setTitle("解析异常");
		 ko.setMessage("啊哦，这段视频或图片由于某些原因解析失败导致无法下载，你可以先下载其他的视频或点击下方联系开发者按钮反馈，你的反馈就是我们的动力~本次错误详情:\n应用:快手视频下载，错误代码:"+p2+"错误内容:"+msg);
		 ko.setPositiveButton("联系开发者", new DialogInterface.OnClickListener(){

				 @Override
				 public void onClick(DialogInterface p1n, int p2d)
				 {
					 toast("错误详情已经复制!");
					 ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					 manager.setText("应用:快手视频下载，错误代码:"+p2+"错误内容:"+msg);

					 Intent intent = new Intent();//
					 intent.setData(Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=3135419729"));
					 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					 try {
						 startActivity(intent);
					 } catch (Exception e) {
// 未安装手Q或安装的版本不支持
						 toast("未安装手机QQ或版本较低");
					 }
				 }
			 });
		 ko.setNegativeButton("取消",null);
		 ko.show();
	 }
	
	void jiazai(){
		if(pv){
			showDialog("正在解析图片...");
		}	
		else{
		showDialog("正在解析视频...");}

		new Thread(){
            @Override
            public void run() {
                super.run();
                String videoUrl = null;
                String pictureUrl = null;
                String text = getPageSource(ed.getText().toString(), "UTF-8");//网页源码
                Log.i("wb",text);
				if(pv){
					String regex = "http://\\S{4,6}.a.yximgs.com(.*?)jpg";//匹配快熟视频
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(text);
					if (matcher.find()) {
						for (int i = 0; i <= matcher.groupCount(); i++) {
							System.out.println(i + ":" + matcher.group(i));
							videoUrl = matcher.group(0);
							vUrl = videoUrl;
							Log.i("bds",vUrl);
						}
						Handler handler = new Handler(Looper.getMainLooper());
						finalVideoUrl = videoUrl;
						handler.post(new Runnable() {
								@Override
								public void run() {
									toast("解析完成");
									bv.setVisibility(View.VISIBLE);
									hideDialog();
									jzVideoPlayerStandard.setUp(finalVideoUrl
																, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "视频");
								}
							});
					}
				}else{
					String regex = "http://\\S{4,6}.a.yximgs.com(.*?)mp4";//匹配快熟视频
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(text);
					if (matcher.find()) {
						for (int i = 0; i <= matcher.groupCount(); i++) {
							System.out.println(i + ":" + matcher.group(i));
							videoUrl = matcher.group(0);
							vUrl = videoUrl;
							Log.i("bds",vUrl);
						}
						Handler handler = new Handler(Looper.getMainLooper());
						finalVideoUrl = videoUrl;
						handler.post(new Runnable() {
								@Override
								public void run() {
									toast("解析完成");
									bv.setVisibility(View.VISIBLE);
									hideDialog();
									jzVideoPlayerStandard.setUp(finalVideoUrl
																, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "视频");
								}
							});
					}
				}
                
            }
        }.start();
	}
	public String getPageSource(String pageUrl, String encoding) {
        StringBuffer sb = new StringBuffer();
        try {
            //构建一URL对象
            URL url = new URL(pageUrl);
            //使用openStream得到一输入流并由此构造一个BufferedReader对象
            BufferedReader in = new BufferedReader(new InputStreamReader(url
																		 .openStream(), encoding));
            String line;
            //读取www资源
            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            in.close();
        } catch (Exception ex) {
            Log.i("wb", ex.toString());
        }
        return sb.toString();
    }
	
	public void toast(String msg){
		Toast.makeText(VideoActivity.this,msg,Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onBackPressed() {
		if (JZVideoPlayer.backPress()) {
			return;
		}
		super.onBackPressed();
	}
	@Override
	protected void onPause() {
		super.onPause();
		JZVideoPlayer.releaseAllVideos();
	}
	
	void showDialog(String message) {
		try {
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setTitle("正在解析中...");
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
		if (dialog != null && dialog.isShowing())
			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	void showDialog1(String message) {
		try {
			if (hi == null) {
				hi = new ProgressDialog(this);
				hi.setTitle("数据下载中...");
				hi.setCancelable(true);
			}
			hi.setMessage(message);
			hi.show();
		} catch (Exception e) {
			// 在其他线程调用dialog会报错
			e.printStackTrace();
		}
	}
	
	void hideDialog1() {
		//用于下载完成后隐藏加载框
		if (hi != null && hi.isShowing())
			try {
				hi.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}}
	void qq(){
		Intent intent_d= new Intent();        
		intent_d.setAction("android.intent.action.VIEW");    
		Uri content_url = Uri.parse("http://qm.qq.com/cgi-bin/qm/qr?k=X4K6tj6lqpGrt2iGe6AEr7X7zuN-A0ap");   
		intent_d.setData(content_url);  
		startActivity(intent_d);
	}
			
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(1,1,1,"加入我们");
		menu.add(2,2,2,"关于应用");
		menu.add(3,3,3,"退出应用");
		menu.add(4,4,4,"正在开发");
		menu.add(5,5,5,"下载切换");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		if(item.getItemId()==1){

			qq();
		}

		if(item.getItemId()==2){
			shiyongshuoming();
		}

		if(item.getItemId()==3){
			//Toast.makeText(MainActivity.this,"如有什么不好的地方请点联系作者反馈!",Toast.LENGTH_LONG).show();
			finish();
		}
		if(item.getItemId()==4){
		
		}
		if(item.getItemId()==5){
			if(pv){
				pv=false;
				//改变按钮文本
				bv.setText("下载该视频");
				//设置控件隐藏
				bv.setVisibility(View.GONE);
			}else{
				pv=true;
				//改变按钮文本
				bv.setText("下载该图片");
				//设置控件隐藏
				bv.setVisibility(View.GONE);
			}
			//判断当前模式
			if(pv){
				//控件心显示
				tv.setVisibility(View.VISIBLE);
				//视频播放控件隐藏
				jzVideoPlayerStandard.setVisibility(View.GONE);
				toast("模式切换成功，当前为图片下载");
			}else{
				toast("模式切换成功，当前为视频下载");
				tv.setVisibility(View.GONE);
				jzVideoPlayerStandard.setVisibility(View.VISIBLE);
			}
		}
		if(item.getItemId()==6){


		}
		return false;
	}
	
	void shiyongshuoming(){
		AlertDialog.Builder bio=new AlertDialog.Builder(VideoActivity.this);
		bio.setCancelable(false);
		bio.setTitle("使用说明");
		bio.setMessage("1.该软件目前仅能下载视频，更多下载功能菠菜正在开发者.使用时请先去快手复制你要下载的快手视频链接，粘贴到输入框之后点击解析(非快手视频或链接不正确将无法解析)。然后视频就可以播放下方会出现下载该视频的按钮，下载后的视频存放到快手本地作品集(SD卡根目录/gifshow/下，视频名称由系统时间决定)。\n2.本软件由菠菜独立开发，非经作者授权而发布的其他应用均属盗版。请注意区分。\n3.本软件在代码处已经进行加密，请不用担心你的数据安全!\n4.若发现该版本已经无法使用，请长按主页面的开始解析按钮或在QQ搜索3135419729添加作者QQ获取最新版本。\n\n菠菜网络传媒，我们一直用心在做。");
		bio.setPositiveButton("确定",null);
		bio.show();
	}
}
