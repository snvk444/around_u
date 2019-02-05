package aroundu.snvk.com.aroundu_template_change.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import aroundu.snvk.com.aroundu_template_change.R;
import aroundu.snvk.com.aroundu_template_change.vo.FeedbackInfo;
import cz.msebera.android.httpclient.Header;

public class feedback_activity extends AppCompatActivity {

    Button feedback_submit, feedback_cancel;
    EditText EditTextFeedbackBody;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        feedback_cancel = (Button) findViewById(R.id.feedback_cancel);
        feedback_submit = (Button) findViewById(R.id.feedback_submit);
        EditTextFeedbackBody = (EditText) findViewById(R.id.EditTextFeedbackBody);
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());


        EditTextFeedbackBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EDITTEXT", "Has focus: " + EditTextFeedbackBody.hasFocus());
            }
        });

        feedback_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        feedback_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("FEEDBACK_11", String.valueOf(EditTextFeedbackBody.getText().toString().length()));
                if (EditTextFeedbackBody.getText().toString().length() > 0) {
                    FeedbackInfo fi = new FeedbackInfo();
                    fi.setId(prefs.getString("ad_id", "-1"));
                    fi.setFeedback(EditTextFeedbackBody.getText().toString());
                    ArrayList<FeedbackInfo> displaypoints = new ArrayList<>();
                    displaypoints.add(fi);
                    AsyncHttpClient client = new AsyncHttpClient();
                    final RequestParams params = new RequestParams();
                    final List userList = displaypoints;
                    Gson gson = new GsonBuilder().create();
                    params.add("userFeedback", gson.toJson(displaypoints));
                    Log.d("FEEDBACK_11", params.toString());
                    client.post("http://limitmyexpense.com/arounduuserdatasync/insert_userFeedback.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            onBackPressed();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            // TODO Auto-generated method stub
                            Log.d("Sync", "OnFailure Function");
                            //prgDialog.hide();
                            if (statusCode == 404) {
                                Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                            } else if (statusCode == 500) {
                                Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("Sync", error.getMessage());
                                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                            }
                        }

                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please provide your Feedback before submitting.", Toast.LENGTH_LONG).show();
                }

                //popupWindow.dismiss();
            }
        });

    }
}

