package aroundu.snvk.com.uaroundu.activities;

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

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import aroundu.snvk.com.uaroundu.R;
import aroundu.snvk.com.uaroundu.beans.FeedbackInfo;
import aroundu.snvk.com.uaroundu.configs.Constants;
import aroundu.snvk.com.uaroundu.utils.AppUtils;
import cz.msebera.android.httpclient.Header;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private Button submitButton;
    private Button cancelButton;
    private EditText feedbackBodyEditText, feedbackBusEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initUI();
    }

    private void initUI() {
        cancelButton = findViewById(R.id.cancel_button);
        submitButton = findViewById(R.id.submit_button);
        feedbackBodyEditText = findViewById(R.id.feedback_edittext);
        feedbackBusEditText = findViewById(R.id.bust_stop_edittext);
        feedbackBodyEditText.setSingleLine(false);
        feedbackBusEditText.setSingleLine(true);
        cancelButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
//                Log.d("FEEDBACK_11", String.valueOf(feedbackBodyEditText.getText().toString().length()));
                String feedbackBody = feedbackBodyEditText.getText().toString();
                String feedbackBus = feedbackBusEditText.getText().toString();
                if (feedbackBody.length() > 0 & feedbackBus.length() > 0) {
                    FeedbackInfo feedbackInfo = new FeedbackInfo();
                    feedbackInfo.setId(AppUtils.getId(this));
                    feedbackInfo.setFeedback(feedbackBody);
                    feedbackInfo.setFeedbackBus(feedbackBus);

                    ArrayList<FeedbackInfo> displaypoints = new ArrayList<>();
                    displaypoints.add(feedbackInfo);

                    AsyncHttpClient client = new AsyncHttpClient();
                    final RequestParams params = new RequestParams();
                    final List userList = displaypoints;
                    Gson gson = new GsonBuilder().create();
                    params.add("userFeedback", gson.toJson(displaypoints));
//                    Log.d("FEEDBACK_11", params.toString());
                    client.post(Constants.API_Insert_UserFeedback, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            finish();
                            Toast.makeText(FeedbackActivity.this, "Thank you!", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            // TODO Auto-generated method stub
                            Log.d("Sync", "OnFailure Function");
                            //prgDialog.hide();
                            if (statusCode == 404) {
                                Toast.makeText(FeedbackActivity.this, "Requested resource not found", Toast.LENGTH_LONG).show();
                            } else if (statusCode == 500) {
                                Toast.makeText(FeedbackActivity.this, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("Sync", error.getMessage());
                                Toast.makeText(FeedbackActivity.this, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(FeedbackActivity.this, "Please provide your Feedback before submitting.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancel_button:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

