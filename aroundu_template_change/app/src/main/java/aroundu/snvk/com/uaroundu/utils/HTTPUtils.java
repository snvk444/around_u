package aroundu.snvk.com.uaroundu.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import aroundu.snvk.com.uaroundu.configs.Constants;

public class HTTPUtils {
    private static final String TAG = "Network";

    public static void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //URL url = new URL(API + "/locations");
                    URL url = new URL(Constants.API_EndPoint);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    //conn.setRequestProperty("x-api-key", API_KEY);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("time_stamp", 01);
                    //jsonParam.put("device_uuid", 105);
                    jsonParam.put("latitude", 75.25);
                    jsonParam.put("longitude", 120.30);

                    Log.i(TAG, jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i(TAG, String.valueOf(conn.getResponseCode()));
                    Log.i(TAG , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

}
