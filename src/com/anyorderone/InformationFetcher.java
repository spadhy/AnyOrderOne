package com.anyorderone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class InformationFetcher {
    public static final String CLASSTAG = InformationFetcher.class.getSimpleName();

    private final Context mCtx;
    /**
     * Constructor - takes the context      * 
     * @param ctx the Context within which to work
     */
    public InformationFetcher(Context ctx) {
        this.mCtx = ctx;
    }
    
    @SuppressWarnings("finally")
	public JSONObject connect(String url)
    {
        HttpClient httpclient = new DefaultHttpClient();
        String currentLoc="";
        try{
        	currentLoc = "LATLONG="+new LocationHelper().getCurrentLocation(mCtx);
        } catch(Exception ex){
        	ex.printStackTrace();
        }
        Log.i("Latitude Longitude-->",currentLoc);
        url=url;
        HttpGet httpget = new HttpGet(url); 
        // prepare parameters 
        HttpParams params = new BasicHttpParams(); 
        params.setParameter("locationJSON", currentLoc); 
        httpget.setParams(params); 
     
        
        HttpResponse response;
        JSONObject json=null;        
        InputStream instream = null;
        try {
            response = httpclient.execute(httpget);
            Log.i("URL-->" + url+"--statusline-->",response.getStatusLine().toString());
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need to worry about connection release
            if (entity != null) {
            	instream = entity.getContent();
                String result= streamToString(instream);
                json=new JSONObject(result);
                Log.i("JSON Object-->","<jsonobject>\n"+json.toString()+"\n</jsonobject>");
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            if (instream != null)
				try {
					instream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            return json;
        }
    }
    
    private static String streamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }    
 }
