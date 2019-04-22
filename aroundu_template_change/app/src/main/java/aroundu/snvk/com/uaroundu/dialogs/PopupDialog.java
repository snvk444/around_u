package aroundu.snvk.com.uaroundu.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import aroundu.snvk.com.uaroundu.R;
import aroundu.snvk.com.uaroundu.utils.AppUtils;

public class PopupDialog extends Dialog implements View.OnTouchListener, CompoundButton.OnCheckedChangeListener {
    public Activity activity;
    private RelativeLayout closeLayout;
    private TextView closePopupBtnTextView;
    private TextView popupTextView;
    private CheckBox checkBox;
    private int type;

    private Animation animPressDown;
    private Animation animPressRealease;

    public PopupDialog(Activity activity, String popupText, int type) {
        super(activity);
        this.activity = activity;
        this.type = type;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_popup);

        popupTextView = findViewById(R.id.popup_textview);
        checkBox = findViewById(R.id.checkbox);
        closeLayout = findViewById(R.id.close_button_layout);
        closePopupBtnTextView = findViewById(R.id.btn_close_textview);

        popupTextView.setText(popupText);
        closeLayout.setOnTouchListener(this);
        checkBox.setOnCheckedChangeListener(this);

        if(type == 3) {
            checkBox.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                animPressRealease = AnimationUtils.loadAnimation(activity, R.anim.alpha_animation_released);
                closePopupBtnTextView.startAnimation(animPressRealease);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 200);
            }
        } else {
            animPressDown = AnimationUtils.loadAnimation(activity, R.anim.alpha_animation_pressed);
            closePopupBtnTextView.startAnimation(animPressDown);
        }
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (type) {
            case 0:
                AppUtils.setIsBusTipRemembered(activity, isChecked);
                break;
            case 1:
                AppUtils.setIsCoverageTipRemembered(activity, isChecked);
                break;
            case 2:
                AppUtils.setIsBusStopTipRemembered(activity, isChecked);
                break;
        }
    }
}