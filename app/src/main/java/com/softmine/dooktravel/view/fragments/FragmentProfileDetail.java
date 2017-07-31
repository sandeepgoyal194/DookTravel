package com.softmine.dooktravel.view.fragments;


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
import android.widget.TextView;

import com.softmine.dooktravel.R;
import com.softmine.dooktravel.pojos.Profile;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfileDetail extends Fragment {

    TextView tvGender,tvDob,tvMaritalStatus,spnDateOfBirth, tvCategory, tvWorkingHour, tvWorkingSince,
             tvCountry, tvCity, tvProvince, tvPostal;
    EditText etFirstName,etMiddleName,etLastName,etAddress,etContact,etSkype,etDescription,etEmail,etOraginization, etDesignation,etZipCode;
    TextView spnMaritalStatus,spnGender,spnCategory, spnCountry, spnCity, spnProvince;
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
        tvGender=(TextView)view.findViewById(R.id.tvGender);

        tvDob=(TextView)view.findViewById(R.id.tvdateOfBirth);
        spnDateOfBirth=(TextView)view.findViewById(R.id.spinner_dob);
        tvMaritalStatus=(TextView)view.findViewById(R.id.tvMaritalStatus);
        spnMaritalStatus=(TextView)view.findViewById(R.id.spinner_marital_status);
        spnGender=(TextView)view.findViewById(R.id.spinner_gender);
        tvPostal = (TextView) view.findViewById(R.id.et_Postal);

        etContact=(EditText)view.findViewById(R.id.edContact);

        etSkype=(EditText)view.findViewById(R.id.edSkype);

        etDescription=(EditText)view.findViewById(R.id.edDescription);
        etZipCode = (EditText) view.findViewById(R.id.et_Postal);

        tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        tvWorkingHour = (TextView) view.findViewById(R.id.tvWorkingHour);
        tvCountry = (TextView) view.findViewById(R.id.tvCountry);
        tvCity = (TextView) view.findViewById(R.id.tvCity);
        tvProvince = (TextView) view.findViewById(R.id.tvProvince);
        tvWorkingSince = (TextView) view.findViewById(R.id.tv_working_since);

        spnCategory = (TextView) view.findViewById(R.id.spinner_category);

        spnCountry = (TextView) view.findViewById(R.id.spinner_country);
        spnCity = (TextView) view.findViewById(R.id.spinner_city);
        spnProvince = (TextView) view.findViewById(R.id.spinner_province);
        etOraginization = (EditText) view.findViewById(R.id.edOrganization);

        etDesignation = (EditText) view.findViewById(R.id.edDesignation);
        etFirstName.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etMiddleName.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etLastName.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etAddress.setTypeface(Utils.getRegularTypeFace(getActivity()));

        etContact.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etSkype.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etDescription.setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvGender.setTypeface(Utils.getLightTypeFace(getActivity()));
        tvDob.setTypeface(Utils.getLightTypeFace(getActivity()));
        spnDateOfBirth.setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvMaritalStatus.setTypeface(Utils.getLightTypeFace(getActivity()));



        tvCategory.setTypeface(Utils.getLightTypeFace(getActivity()));
        tvWorkingHour.setTypeface(Utils.getLightTypeFace(getActivity()));
        tvCountry.setTypeface(Utils.getLightTypeFace(getActivity()));
        tvCity.setTypeface(Utils.getLightTypeFace(getActivity()));
        tvProvince.setTypeface(Utils.getLightTypeFace(getActivity()));
        tvWorkingSince.setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvPostal.setTypeface(Utils.getRegularTypeFace(getActivity()));

        etOraginization.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etDesignation.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etAddress.setTypeface(Utils.getRegularTypeFace(getActivity()));
        etZipCode.setTypeface(Utils.getRegularTypeFace(getActivity()));

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
        if(profile.getGender().equalsIgnoreCase("m")||profile.getGender().equalsIgnoreCase("Male") ){
            spnGender.setText("Male");
        }
        else {
            spnGender.setText("Female");
        }
        spnDateOfBirth.setText(Utils.getFormattedDate(profile.getDateOfBirth(),C.SERVER_DATE_FORMAT,C.DATE_FORMAT));
        spnMaritalStatus.setText(profile.getMaritalStatus());
        etZipCode.setText(profile.getZipCode());
        etOraginization.setText(profile.getOrganization());
        spnCountry.setText(profile.getCountryName());
        spnProvince.setText(profile.getStateName());
        spnCity.setText(profile.getCityName());
        tvWorkingSince.setText(Utils.getFormattedDate(profile.getWorkingSince(),C.SERVER_DATE_FORMAT,C.DATE_FORMAT));
        spnCategory.setText(profile.getCategoryName());
    }
}