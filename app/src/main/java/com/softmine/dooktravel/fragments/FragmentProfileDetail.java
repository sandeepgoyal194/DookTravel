package com.softmine.dooktravel.fragments;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.softmine.dooktravel.R;
import com.softmine.dooktravel.pojos.Profile;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfileDetail extends Fragment {


    EditText etFirstName,etMiddleName,etLastName,etAddress,etContact,etSkype,etDescription,etEmail;
    ImageView imgProfile;
    Profile profile;
    public FragmentProfileDetail() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        if(SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)){

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                profile = (Profile) bundle.getSerializable(C.DATA);
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgProfile=(ImageView)view.findViewById(R.id.imgProfile);
        etFirstName=(EditText)view.findViewById(R.id.edFirstname);
        etMiddleName=(EditText)view.findViewById(R.id.edMiddleName);
        etEmail=(EditText)view.findViewById(R.id.edEmail);
        etLastName=(EditText)view.findViewById(R.id.edlastName);

        etAddress=(EditText)view.findViewById(R.id.edAddress);


        etContact=(EditText)view.findViewById(R.id.edContact);

        etSkype=(EditText)view.findViewById(R.id.edSkype);

        etDescription=(EditText)view.findViewById(R.id.edDescription);

        etFirstName.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etMiddleName.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etLastName.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etAddress.setTypeface(Utils.getRegularTypeFace(getActivity()));

        etContact.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etSkype.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etDescription.setTypeface(Utils.getRegularTypeFace(getActivity()));

        showDetail();

    }

    void showDetail(){
        etFirstName.setText(profile.getFirstName());
        etLastName.setText(profile.getLastName());
        etMiddleName.setText(profile.getMiddleName());
        etAddress.setText(profile.getAddress());
        etContact.setText(profile.getPhone());
        etDescription.setText(profile.getAbout());
        etSkype.setText(profile.getSkype());
        etEmail.setText(profile.getEmailId());
        if(profile.getProfilePic()!=null && !profile.getProfilePic().equals("")) {
            Utils.displayImage(getActivity(),C.IMAGE_BASE_URL+profile.getProfilePic(),imgProfile);

        }
    }
}
