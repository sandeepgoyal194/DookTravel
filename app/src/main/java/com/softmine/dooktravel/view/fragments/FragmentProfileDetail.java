package com.softmine.dooktravel.view.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.softmine.dooktravel.model.Profile;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfileDetail extends Fragment {

/*    TextView tvGender,tvDob,tvMaritalStatus, tvCategory, tvWorkingHour,
             tvCountry, tvCity, tvProvince, tvPostal;*/
    EditText etFirstName,etMiddleName,etLastName,etAddress,etContact,etSkype,etDescription,etEmail,etOraginization, etDesignation,etZipCode;
    EditText spnMaritalStatus,spnGender,spnCategory, spnCountry, spnCity, spnProvince,spnDateOfBirth,tvWorkingSince,etContact1;
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
     //   tvGender=(TextView)view.findViewById(R.id.tvGender);

      //  tvDob=(TextView)view.findViewById(R.id.tvdateOfBirth);
        spnDateOfBirth=(EditText)view.findViewById(R.id.edDob);
     //   tvMaritalStatus=(TextView)view.findViewById(R.id.tvMaritalStatus);
        spnMaritalStatus=(EditText) view.findViewById(R.id.edMatiral_status);
        spnGender=(EditText) view.findViewById(R.id.edGender);
     //   tvPostal = (TextView) view.findViewById(R.id.et_Postal);

        etContact=(EditText)view.findViewById(R.id.edContact1);
        etContact1=(EditText)view.findViewById(R.id.edContact2);
        etContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etContact.getText().toString().length()>8){
                    getDialPad(Utils.getSubstringPhone(etContact.getText().toString()));
                }
            }
        });


        //   edSecondary.getEditText().setHint(code1);
        etContact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etContact1.getText().toString().length()>8){
                    getDialPad(Utils.getSubstringPhone(etContact1.getText().toString()));
                }
            }
        });
        etSkype=(EditText)view.findViewById(R.id.edSkype);

        etDescription=(EditText)view.findViewById(R.id.edDescription);
        etZipCode = (EditText) view.findViewById(R.id.edZipCode);

    //    tvCategory = (TextView) view.findViewById(R.id.tvCategory);
      //  tvWorkingHour = (TextView) view.findViewById(R.id.tvWorkingHour);
     //   tvCountry = (TextView) view.findViewById(R.id.tvCountry);
     //   tvCity = (TextView) view.findViewById(R.id.tvCity);
     //   tvProvince = (TextView) view.findViewById(R.id.tvProvince);
        tvWorkingSince = (EditText) view.findViewById(R.id.edWorkingSince);

        spnCategory = (EditText) view.findViewById(R.id.edCategory);

        spnCountry = (EditText) view.findViewById(R.id.edCountry);
        spnCity = (EditText) view.findViewById(R.id.edCity);
        spnProvince = (EditText) view.findViewById(R.id.edProvince);
        etOraginization = (EditText) view.findViewById(R.id.edOrganization);

        etDesignation = (EditText) view.findViewById(R.id.edDesignation);

        showDetail();

    }

    void showDetail(){
        String name=profile.getFirstName();
        if(profile.getMiddleName()!=null&& profile.getMiddleName().length()>0){
            name=name+" "+profile.getMiddleName();
        }
        if(profile.getLastName()!=null && profile.getLastName().length()>0){
            name=name+" "+profile.getLastName();
        }
        etFirstName.setText(name);
      //  etLastName.setText(profile.getLastName());
      //  etMiddleName.setText(profile.getMiddleName());
        etAddress.setText(profile.getAddress());
        etContact.setText(profile.getPhone());
        etContact1.setText(profile.getMobile());

        etDescription.setText(profile.getAbout());
        etSkype.setText(profile.getSkype());
        etEmail.setText(profile.getEmailId());
        if(profile.getProfilePic()!=null && !profile.getProfilePic().equals("")) {
            Utils.displayImage(getActivity(),C.IMAGE_BASE_URL+profile.getProfilePic(),imgProfile);
           // Utils.displayImage(getActivity(),profile.getProfilePic(),imgProfile);
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
        etDesignation.setText(profile.getDesignation());
        spnCountry.setText(profile.getCountryName());
        spnProvince.setText(profile.getStateName());
        spnCity.setText(profile.getCityName());
        tvWorkingSince.setText(Utils.getFormattedDate(profile.getWorkingSince(),C.SERVER_DATE_FORMAT,C.DATE_FORMAT));
        spnCategory.setText(profile.getCategoryName());
    }
    void getDialPad(String dial_number){
        Intent dial = new Intent();
        dial.setAction("android.intent.action.DIAL");
        dial.setData(Uri.parse("tel:"+dial_number));
        startActivity(dial);
    }
}
