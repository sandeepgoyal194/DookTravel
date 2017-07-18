package com.softmine.dooktravel.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Output;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softmine.dooktravel.ActivityHome;
import com.softmine.dooktravel.R;
import com.softmine.dooktravel.pojos.LoginStatus;
import com.softmine.dooktravel.pojos.Profile;
import com.softmine.dooktravel.pojos.ProfileDetail;
import com.softmine.dooktravel.pojos.ProfileStatus;
import com.softmine.dooktravel.serviceconnection.CompleteListener;
import com.softmine.dooktravel.serviceconnection.ServiceConnection;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.validations.ValidateEditText;
import com.softmine.dooktravel.validations.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBasicDetail extends Fragment implements CompleteListener{
    private DatePickerDialog DatePickerDialog;
    TextView tvbasicDetail,tvGender,tvDob,tvMaritalStatus,spnDateOfBirth;
    ValidateEditText edFirstName,edMiddleName,edLastName;
    Button btnNext;
    Spinner spnMaritalStatus,spnGender;
    int flags;
    Validations validation = new Validations();
    private SimpleDateFormat dateFormatter;
    private int year;
    private int month;
    private int day;
    List<Profile> profile;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        tvbasicDetail=(TextView)view.findViewById(R.id.tvBasicDetail);
        tvGender=(TextView)view.findViewById(R.id.tvGender);

        tvDob=(TextView)view.findViewById(R.id.tvdateOfBirth);
        spnDateOfBirth=(TextView)view.findViewById(R.id.spinner_dob);
        spnDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             DatePickerDialog.show();
            }
        });
        tvMaritalStatus=(TextView)view.findViewById(R.id.tvMaritalStatus);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edFirstName=new ValidateEditText((EditText)view.findViewById(R.id.edFirstname),getActivity(),flags);
        edMiddleName=new ValidateEditText((EditText)view.findViewById(R.id.edMiddleName),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edLastName=new ValidateEditText((EditText)view.findViewById(R.id.edlastName),getActivity(),flags);
        btnNext=(Button) view.findViewById(R.id.btnNext);
        spnMaritalStatus=(Spinner)view.findViewById(R.id.spinner_marital_status);
        spnGender=(Spinner)view.findViewById(R.id.spinner_gender);

        tvbasicDetail.setTypeface(Utils.getRegularTypeFace(getActivity()));
        tvGender.setTypeface(Utils.getLightTypeFace(getActivity()));
        edFirstName.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        edMiddleName.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));
        edLastName.getEditText().setTypeface(Utils.getRegularTypeFace(getActivity()));

        tvDob.setTypeface(Utils.getLightTypeFace(getActivity()));
        tvMaritalStatus.setTypeface(Utils.getLightTypeFace(getActivity()));
        btnNext.setTypeface(Utils.getSemiBoldTypeFace(getActivity()));
        btnNext.setOnClickListener(mNextClickListner);
        validation.addtoList(edLastName);
        validation.addtoList(edFirstName);
        Calendar newCalendar = Calendar.getInstance();
        year  = newCalendar.get(Calendar.YEAR);
        month = newCalendar.get(Calendar.MONTH);
        day   = newCalendar.get(Calendar.DAY_OF_MONTH);
       DatePickerDialog = new DatePickerDialog(getActivity(), new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int y, int monthOfYear, int dayOfMonth) {
                year  = y;
                month = monthOfYear;
                day   = dayOfMonth;
                Calendar newDate = Calendar.getInstance();
                newDate.set(y, monthOfYear, dayOfMonth);
                spnDateOfBirth.setText(dateFormatter.format(newDate.getTime()));
            }

        },year, month, day);

        getProfileDetail();
    }



    View.OnClickListener mNextClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(validation.validateAllEditText()) {
                if(isAllValid()) {
                    if(!SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)) {
                        ProfileDetail profileDetail = new ProfileDetail();
                        profileDetail.setFirstname(edFirstName.getString());
                        profileDetail.setMiddlename(edMiddleName.getString());
                        profileDetail.setLastname(edLastName.getString());
                        profileDetail.setGender(spnGender.getSelectedItem().toString());
                        profileDetail.setDob(tvDob.getText().toString());
                        profileDetail.setMarital(spnMaritalStatus.getSelectedItem().toString());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(C.DATA, profileDetail);
                        ((ActivityHome) getActivity()).fragmnetLoader(C.FRAGMENT_CONTACT_DETAIL, bundle);
                    }
                    else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(C.PROFILE_METHOD, profile.get(0));
                        ((ActivityHome) getActivity()).fragmnetLoader(C.FRAGMENT_CONTACT_DETAIL, bundle);
                    }
                }
            }
        }
    };
    boolean isAllValid(){

        if(spnGender.getSelectedItem().toString().equals(C.SELECT)){
            Toast.makeText(getActivity(),R.string.select_gender,Toast.LENGTH_LONG).show();
            return false;
        }
       else if(tvDob.getText().toString().equals("")){
            Toast.makeText(getActivity(),R.string.enter_dob,Toast.LENGTH_LONG).show();
            return false;
        }
       else if(spnMaritalStatus.getSelectedItem().toString().equals(C.SELECT)){
            Toast.makeText(getActivity(),R.string.selectMarital_status,Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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
    }

    public FragmentBasicDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_basic_detail, container, false);
    }


    void getProfileDetail(){

        if(SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)) {
            JSONObject jsonBody = new JSONObject();
            try {
             /*   jsonBody.put(C.EMAIL, "pradeep.bansal@techmobia.com");
                jsonBody.put(C.PASSWORD, "abc123");
                jsonBody.put(C.SOCAIL_ID, "");*/
                jsonBody.put(C.MEMBER_ID, SharedPreference.getInstance(getActivity()).getString(C.MEMBER_ID));
                jsonBody.put(C.TOKEN, SharedPreference.getInstance(getActivity()).getString(C.TOKEN));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ServiceConnection serviceConnection = new ServiceConnection();
            serviceConnection.makeJsonObjectRequest(C.PROFILE_METHOD, jsonBody, FragmentBasicDetail.this);
        }
    }
    @Override
    public void done(String response) {
        Log.e(FragmentBasicDetail.class.getName(),"RESPONSE=="+response);
        Gson gson = new Gson();
        ProfileStatus profileStatus= gson.fromJson(response,ProfileStatus.class);
        if(!profileStatus.getError()){
            profile=profileStatus.getMember();
            edFirstName.getEditText().setText(profile.get(0).getFirstName());
            edMiddleName.getEditText().setText(profile.get(0).getMiddleName());
            edLastName.getEditText().setText(profile.get(0).getLastName());
            if(profile.get(0).getGender().equalsIgnoreCase("m")){
                spnGender.setSelection(1);
            }
            else {
                spnGender.setSelection(2);
            }
            if(profile.get(0).getMaritalStatus().equalsIgnoreCase("single")){
                spnMaritalStatus.setSelection(1);
            }
            else {
                spnMaritalStatus.setSelection(2);
            }
            spnDateOfBirth.setText(profile.get(0).getDateOfBirth());
        }
        else{
            getDailogConfirm(profileStatus.getMessage(),"");
        }

    }

    @Override
    public Context getApplicationsContext() {
        return getActivity();
    }


    void getDailogConfirm(String dataText, String titleText) {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //tell the Dialog to use the dialog.xml as it's layout description
            dialog.setContentView(R.layout.dialog_with_two_button);
            // dialog.setTitle("Android Custom Dialog Box");
            dialog.setCancelable(false);
            TextView dataTextTv = (TextView) dialog.findViewById(R.id.dialog_data_text);
            TextView titleTextTv = (TextView) dialog.findViewById(R.id.dialog_title_text);
            TextView cancelTv = (TextView) dialog.findViewById(R.id.dialog_cancel_text);
            TextView okTextTv = (TextView) dialog.findViewById(R.id.dialog_ok_text);

            cancelTv.setVisibility(View.GONE);
            dataTextTv.setText(dataText);
            titleTextTv.setText(titleText);

            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            okTextTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
