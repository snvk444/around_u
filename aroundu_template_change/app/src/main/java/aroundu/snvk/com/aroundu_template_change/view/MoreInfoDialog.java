package aroundu.snvk.com.aroundu_template_change.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import aroundu.snvk.com.aroundu_template_change.R;
import aroundu.snvk.com.aroundu_template_change.interfaces.TrackingListener;

public class MoreInfoDialog extends Dialog implements View.OnClickListener{

    public Activity rootActivity;
    public Button goBack, track;
    public TrackingListener mListener;

    public MoreInfoDialog(Activity a, TrackingListener m){
        super(a);
        rootActivity = a;
        mListener = m;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        track = (Button) findViewById(R.id.btn_yes);
        goBack = (Button) findViewById(R.id.btn_no);
        track.setOnClickListener(this);
        goBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                mListener.trackMyPath();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
