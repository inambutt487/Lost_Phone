package find.com.lostphone.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import find.com.lostphone.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AntiTheftFragment extends Fragment {


    public AntiTheftFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anti_theft, container, false);
    }

}
