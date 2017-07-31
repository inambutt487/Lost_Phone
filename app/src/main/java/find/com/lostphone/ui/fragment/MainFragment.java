package find.com.lostphone.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import find.com.lostphone.R;
import find.com.lostphone.utils.LostPhoneConstant;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    public interface OnOptionSelectedListener {
        public void onFeatureClick(int which);
    }

    OnOptionSelectedListener mCallback;

    TextView txtLockPhoneViaSms, txtRingSilentPhone,
            txtGetLocation, txtAntiTheftSecurity;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnOptionSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        txtLockPhoneViaSms = view.findViewById(R.id.txtLockPhoneViaSms);
        txtRingSilentPhone = view.findViewById(R.id.txtRingSilentPhone);
        txtGetLocation = view.findViewById(R.id.txtGetLocation);
        txtAntiTheftSecurity = view.findViewById(R.id.txtAntiTheftSecurity);

        txtLockPhoneViaSms.setOnClickListener(this);
        txtRingSilentPhone.setOnClickListener(this);
        txtGetLocation.setOnClickListener(this);
        txtAntiTheftSecurity.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtLockPhoneViaSms:
                mCallback.onFeatureClick(LostPhoneConstant.LOCK_PHONE_VIA_SMS);
                break;
            case R.id.txtRingSilentPhone:
                mCallback.onFeatureClick(LostPhoneConstant.RING_SILENT_PHONE);

                break;
            case R.id.txtGetLocation:
                mCallback.onFeatureClick(LostPhoneConstant.GET_CURRENT_LOCATION_PHONE);

                break;
            case R.id.txtAntiTheftSecurity:
                mCallback.onFeatureClick(LostPhoneConstant.ANTI_THEFT_SECURITY);

                break;
        }
    }
}
