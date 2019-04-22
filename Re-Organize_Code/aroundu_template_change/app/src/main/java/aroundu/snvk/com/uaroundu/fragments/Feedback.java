package aroundu.snvk.com.uaroundu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import aroundu.snvk.com.uaroundu.R;

public class Feedback extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    public Feedback() {
    }

    public static Feedback newInstance() {
        return new Feedback();
    }
}
