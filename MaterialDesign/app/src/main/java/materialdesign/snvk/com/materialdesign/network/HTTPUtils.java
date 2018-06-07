package materialdesign.snvk.com.materialdesign.network;

import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jjackson on 4/26/2018.
 */

public class HTTPUtils {
    private static final String TAG = "Network";
    private static final String API = "https://e47ujwphnj.execute-api.us-west-1.amazonaws.com/test";
    private static final String API_KEY = "hNK9taaLsx9MpHp7HwphH67vSgajOrch5Evuf3zt";

    public static void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(API + "/locations");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setRequestProperty("x-api-key", API_KEY);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("time_stamp", 0);
                    jsonParam.put("device_uuid", 15);
                    jsonParam.put("latitude", 75.25);
                    jsonParam.put("longitude", 112.30);

                    Log.i(TAG, jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
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
