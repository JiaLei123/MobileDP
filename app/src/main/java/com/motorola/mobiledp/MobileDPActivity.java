package com.motorola.mobiledp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MobileDPActivity extends Activity {

	TextView v1;
	EditText et;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_dp);
        v1=(TextView)findViewById(R.id.textView1);
        et = (EditText) findViewById(R.id.editText1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mobile_d, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    String reulst;
    public void Start_DP_Service(View view)
    {
    	try
    	{
	    	String result1 = "{\"JobSummary\":{\"JobList\":[{\"JobUuid\":\"18948a8d-9b47-e511-a081-c4d9873a6e51\",\"JobName\":\"Read 08\\/21\\/2015 00:28:33(UTC)\",\"CreatedDate\":\"8/21/2015 12:28:33 AM\",\"JobStatus\":JOB_STATUS_PENDING,\"CruncherStatus\":NONE,\"RadioCurrentCodeplugUuid\":\"\",\"DeviceID\":\"8888888888\",\"SerialNumber\":\"8888888888\",\"ModelNumber\":\"\",\"PBBModelNumber\":\"\",\"FirmwareVer\":\"\",\"Matrix2ARSSuppression\":false,\"Matrix2SwitchoverDelayTimer\":0,\"PbaFileUuid\":\"\",\"RadioAlias\":\"\",\"CAI\":\"\",\"RadioID\":\"\",\"RegionName\":NA,\"MatrixOTAPKey\":\"\",\"FeaturesExchangeData\":\"\",\"ForeignIP\":\"\",\"ConnectionMethod\":1,\"LinkType\":\"\",\"LESystemInfo\":\"\",\"RRPLogicalID\":\"\",\"DMR3ControllerIP\":\"\",\"DMR3ControllerUDPPort\":\"\",\"DeviceIP\":\"\"}]}}";
	    	JSONObject jsonresult = new JSONObject(result1).getJSONObject("JobSummary");
	    	JSONArray jsonArray = jsonresult.getJSONArray("JobList");
	    	String SerialNumber = jsonresult.get("SerialNumber").toString();
			String RadioID=jsonresult.get("RadioID").toString();
    	}
		catch (JSONException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} 
    	
/*    	 new Thread() {
    	        @Override
    	        public void run() {
    	           // ��Ҫִ�еķ���
    	           // ִ����Ϻ��handler����һ������Ϣ
    	           handler.sendEmptyMessage(0);
    	        }
    	  }.start();*/
    	  
    	//����Handler����
      	final Handler handler =new Handler(){
      	 @Override
      	 //������Ϣ���ͳ�����ʱ���ִ��Handler���������
      	public void handleMessage(Message msg){
      	 super.handleMessage(msg);

      	 }
      	 };
      	 System.out.println("yse1");
      	 new Thread(){
          	 @Override
          	 public void run(){      	    	         	        
          		reulst = getRequest();
          	 }
          	 }.start();
          	 System.out.println("yse2");
    	  
    	  
          	v1.setText(reulst);
    }
    
    
   

  //����Handler����
/*  private Handler handler = new Handler() {
      //������Ϣ���ͳ�����ʱ���ִ��Handler�����������������Ϣ�ַ�
      @Override
      public void handleMessage(Message msg) {
             super.handleMessage(msg);
             //����UI
             getRequest();
      }
  };*/
    
    private String getRequest()
	{
/*		HttpClient hc=new DefaultHttpClient();
		HttpGet get=new HttpGet("http://10.0.2.2:55367/api/values");	
		//  HttpEntity entity = rp.getEntity();
		HttpResponse rp;
		try
		{
			rp = hc.execute(get);
			int statusCode = rp.getStatusLine().getStatusCode();
			if(statusCode == 0)
			{
				int a = 8;
			}
		} catch (ClientProtocolException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		*/
    	String url = "http://10.0.2.2:55367/api/values/1"; 
    	
    	HttpGet httpGet = new HttpGet(url); 
		//ʵ����httpCilent
		 	HttpClient httpCilent = new DefaultHttpClient();	
		 	HttpEntity httpentity;
		 	
		 	HttpResponse rp;
		String result="";
		try
		{
			rp = httpCilent.execute(httpGet);
			int statusCode = rp.getStatusLine().getStatusCode();
			result = EntityUtils.toString(rp.getEntity()); 
			
			System.out.println("result:" + result); 
		} catch (ClientProtocolException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		try
		{
			JSONObject jsonresult = new JSONObject(result).getJSONObject("JobSummary");
			String SerialNumber = jsonresult.get("SerialNumber").toString();
			String RadioID=jsonresult.get("RadioID").toString();
		} 
		catch (JSONException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
