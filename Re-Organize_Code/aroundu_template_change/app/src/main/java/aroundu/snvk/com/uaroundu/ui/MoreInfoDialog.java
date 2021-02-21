package aroundu.snvk.com.uaroundu.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aroundu.snvk.com.uaroundu.R;
import aroundu.snvk.com.uaroundu.interfaces.TrackingListener;

public class MoreInfoDialog extends Dialog implements View.OnTouchListener{

    private Activity rootActivity;
    private TextView backTextView, trackTextView;
    private RelativeLayout backLayout;
    private RelativeLayout trackLayout;
    private TrackingListener mListener;

    private Animation animPressDown;
    private Animation animPressRealease;

    public MoreInfoDialog(Activity a, TrackingListener m){
        super(a);
        rootActivity = a;
        mListener = m;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_track_path);

        backLayout = findViewById(R.id.no_button_layout);
        backTextView = findViewById(R.id.btn_no_textview);

        trackLayout = findViewById(R.id.yes_button_layout);
        trackTextView = findViewById(R.id.btn_yes_textview);

        backLayout.setOnTouchListener(this);
        trackLayout.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.yes_button_layout:
                if (event.getAction() != MotionEvent.ACTION_DOWN) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        animPressRealease = AnimationUtils.loadAnimation(rootActivity, R.anim.alpha_animation_released);
                        trackTextView.startAnimation(animPressRealease);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mListener.trackMyPath();
                                dismiss();
                            }
                        }, 200);
                        break;
                    }
                }
                animPressDown = AnimationUtils.loadAnimation(rootActivity, R.anim.alpha_animation_pressed);
                trackTextView.startAnimation(animPressDown);
                break;
            case R.id.no_button_layout:
                if (event.getAction() != MotionEvent.ACTION_DOWN) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        animPressRealease = AnimationUtils.loadAnimation(rootActivity, R.anim.alpha_animation_released);
                        backTextView.startAnimation(animPressRealease);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dismiss();
                            }
                        }, 200);
                        break;
                    }
                }
                animPressDown = AnimationUtils.loadAnimation(rootActivity, R.anim.alpha_animation_pressed);
                backTextView.startAnimation(animPressDown);
                break;
        }
        return true;
    }
}
