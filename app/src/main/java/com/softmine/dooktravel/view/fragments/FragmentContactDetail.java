package com.softmine.dooktravel.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softmine.dooktravel.R;
import com.softmine.dooktravel.model.Profile;
import com.softmine.dooktravel.model.ProfileDetail;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;
import com.softmine.dooktravel.view.ActivityHome;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentContactDetail extends Fragment {


    ValidateEditText etSecondaryEmail,etMobile1,etMobile2,etSkypeId;
    Button btnNext;
    TextView tvContactDetail;
    ProfileDetail profileDetail;
    Profile profile;
    int flags;
    Validations validation = new Validations();
    boolean isLogin=false;
    public FragmentContactDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if(SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)){
                isLogin=true;
                profile = (Profile) bundle.getSerializable(C.PROFILE_METHOD);
            }
            else {
                isLogin=false;
                profileDetail = (ProfileDetail) bundle.getSerializable(C.DATA);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        flags = flags | Validations.TYPE_EMAIL;
        etSecondaryEmail=new ValidateEditText((EditText)view.findViewById(R.id.edSecondaryEmail),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etMobile1=new ValidateEditText((EditText)view.findViewById(R.id.edMobile_no_1),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etMobile2=new ValidateEditText((EditText)view.findViewById(R.id.edMobile_no_2),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        etSkypeId=new ValidateEditText((EditText)view.findViewById(R.id.edSkype_id),getActivity(),flags);
        tvContactDetail=(TextView) view.findViewById(R.id.tvContactDetail);

        btnNext=(Button) view.findViewById(R.id.btnNext);

        etSecondaryEmail.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        etMobile1.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        etMobile2.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        etSkypeId.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvContactDetail.setTypeface(Utils.getRegularTypeFace(getActivity()));

        btnNext.setTypeface(Utils.getRegularTypeFace(getActivity()));
        btnNext.setOnClickListener(mBtnNextCLickLisner);

      //  validation.addtoList(etSkypeId);
      //  validation.addtoList(etMobile2);
        validation.addtoList(etMobile1);
        validation.addtoList(etSecondaryEmail);
        if(isLogin){
           etSecondaryEmail.getEditText().setText(profile.getEmailId());
            etMobile1.getEditText().setText(profile.getPhone());
        }
    }

    View.OnClickListener mBtnNextCLickLisner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                if(validation.validateAllEditText()) {
                    if(!isLogin) {
                        profileDetail.setSecondaryEmail(etSecondaryEmail.getString());
                        profileDetail.setMobile1(etMobile1.getString());
                        profileDetail.setPhone(etMobile1.getString());
                        profileDetail.setMobile2(etMobile2.getString());
                        profileDetail.setSkype(etSkypeId.getString());

                        Bundle bundle = new Bundle();
                        bundle.putSerializable(C.DATA, profileDetail);
                        ((ActivityHome) getActivity()).fragmnetLoader(C.FRAGMENT_PROFESSIONAL_DETAIL, bundle);
                    }
                    else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(C.PROFILE_METHOD, profile);
                        ((ActivityHome) getActivity()).fragmnetLoader(C.FRAGMENT_PROFESSIONAL_DETAIL, bundle);
                    }
                }

        }
    };
}
