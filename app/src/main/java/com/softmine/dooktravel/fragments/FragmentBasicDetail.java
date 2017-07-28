package com.softmine.dooktravel.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmine.dooktravel.R;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBasicDetail extends Fragment implements CompleteListener{
    private DatePickerDialog DatePickerDialog;
    TextView tvbasicDetail,tvGender,tvDob,tvMaritalStatus,spnDateOfBirth,tvUpload;
    ValidateEditText edFirstName,edMiddleName,edLastName,edPassword,edConfirmPassword;
    Button btnNext;
    ImageView imgProfile;
    View viewUploadImage;
    Spinner spnMaritalStatus,spnGender;
    int flags;
    Validations validation ;
    private SimpleDateFormat dateFormatter;
    private int year;
    private int month;
    private int day;
    Utils util;
   Profile profile;
    ProfileDetail profileDetail ;
    ProfileDetail profileDtl;
    private int PICK_IMAGE_REQUEST = 1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        profileDetail = new ProfileDetail();
        if(SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)){
            C.isloggedIn=true;
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                profile = (Profile) bundle.getSerializable(C.DATA);
            }
        }
        else {
            C.isloggedIn=false;
            Bundle bundle = this.getArguments();
            if (bundle != null) {

                profileDtl = (ProfileDetail) bundle.getSerializable(C.DATA);
            }
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validation = new Validations();
        util=new Utils();
        dateFormatter = new SimpleDateFormat(C.DATE_FORMAT);
        tvbasicDetail=(TextView)view.findViewById(R.id.tvBasicDetail);
        tvGender=(TextView)view.findViewById(R.id.tvGender);

        tvDob=(TextView)view.findViewById(R.id.tvdateOfBirth);
        tvUpload=(TextView)view.findViewById(R.id.tvUpload);
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
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edPassword=new ValidateEditText((EditText)view.findViewById(R.id.edPassword),getActivity(),flags);
        flags = 0 | Validations.FLAG_NOT_EMPTY;
        edConfirmPassword=new ValidateEditText((EditText)view.findViewById(R.id.edConfirmPassword),getActivity(),flags);
        btnNext=(Button) view.findViewById(R.id.btnNext);
        spnMaritalStatus=(Spinner)view.findViewById(R.id.spinner_marital_status);
        spnGender=(Spinner)view.findViewById(R.id.spinner_gender);
        viewUploadImage=(View)view.findViewById(R.id.rl_image);
        viewUploadImage.setOnClickListener(mUploadImageClickListner);
        imgProfile=(ImageView)view.findViewById(R.id.imgProfile);

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
        if(C.isloggedIn) {
            tvUpload.setText(R.string.edit);
            edPassword.getEditText().setVisibility(View.GONE);
            edConfirmPassword.getEditText().setVisibility(View.GONE);
          //  getProfileDetail();
            showDetails(profile);
        }
        else {
            validation.addtoList(edPassword);
            validation.addtoList(edConfirmPassword);
        }

    }



    View.OnClickListener mUploadImageClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chooseImage();
        }
    };

    void chooseImage(){
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                imgProfile.setImageBitmap(bitmap);
                String profileImage= Utils.getBase64Image(bitmap);

                if(C.isloggedIn) {
                   profile.setProfilePic(profileImage);
                    profile.setProfilePicname(Utils.getCurrentTimeStamp()+".jpg");
                }
                else {
                    profileDetail.setPicture(profileImage);
                    profileDetail.setPicturename(Utils.getCurrentTimeStamp()+".jpg");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    View.OnClickListener mNextClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(validation.validateAllEditText()) {
                if(isAllValid()) {
                    if(util.isInternetOn(getActivity())) {

                        if (!SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)) {

                            profileDetail.setFirstname(edFirstName.getEditText().getText().toString());
                            profileDetail.setMiddlename(edMiddleName.getEditText().getText().toString());
                            profileDetail.setLastname(edLastName.getEditText().getText().toString());
                            profileDetail.setGender(spnGender.getSelectedItem().toString());
                            if (spnGender.getSelectedItem().toString().equalsIgnoreCase("Male")) {
                                profileDetail.setGender("m");

                            } else {
                                profileDetail.setGender("f");

                            }

                            profileDetail.setPassword(edPassword.getEditText().getText().toString());
                            profileDetail.setDob(Utils.getFormattedDate(spnDateOfBirth.getText().toString(), C.DATE_FORMAT, C.DESIRED_FORMAT));
                            profileDetail.setEmail(profileDtl.getEmail());
                            profileDetail.setSocialid(profileDtl.getSocialid());
                            profileDetail.setMarital(spnMaritalStatus.getSelectedItem().toString());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(C.DATA, profileDetail);
                            Intent i = new Intent(getContext(), FragmentProfessionalDetail.class);
                            i.putExtra("details", bundle);
                            startActivity(i);
                        } else {
                            Bundle bundle = new Bundle();
                            profile.setFirstName(edFirstName.getEditText().getText().toString());
                            profile.setMiddleName(edMiddleName.getEditText().getText().toString());
                            profile.setLastName(edLastName.getEditText().getText().toString());
                            profile.setMaritalStatus(spnMaritalStatus.getSelectedItem().toString());

                            if (spnGender.getSelectedItem().toString().equalsIgnoreCase("Male")) {
                                profile.setGender("m");
                            } else {
                                profile.setGender("f");
                            }
                            profile.setDateOfBirth(Utils.getFormattedDate(spnDateOfBirth.getText().toString(), C.DATE_FORMAT, C.DESIRED_FORMAT));
                            profile.setToken(SharedPreference.getInstance(getActivity()).getString(C.TOKEN));
                            bundle.putSerializable(C.PROFILE_METHOD, profile);
                            Intent i = new Intent(getContext(), FragmentProfessionalDetail.class);
                            i.putExtra("details", bundle);
                            startActivity(i);
                        }
                    }
                    else {
                        getDailogConfirm(getString(R.string.internet_issue)
                                , "Internet Issue");
                    }
                }
            }
        }
    };


    boolean isAllValid(){
        if(!C.isloggedIn && !edPassword.getEditText().getText().toString().equals(edConfirmPassword.getEditText().getText().toString())){
            edConfirmPassword.getEditText().setError(getString(R.string.password_validate));
            return false;
        }
        else if(spnGender.getSelectedItem().toString().equals(C.SELECT)){
            Utils.showToast(getActivity(),getString(R.string.select_gender));
            return false;
        }
       else if(tvDob.getText().toString().equals("")){
            Utils.showToast(getActivity(),getString(R.string.enter_dob));

            return false;
        }
       else if(spnMaritalStatus.getSelectedItem().toString().equals(C.SELECT)){
            Utils.showToast(getActivity(),getString(R.string.selectMarital_status));
            return false;
        }

        return true;
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
         //   profile=profileStatus.getMember();
           // showDetails(profile.get(0));
        }
        else{
            getDailogConfirm(profileStatus.getMessage(),"");
        }

    }


    void showDetails(Profile profile){
        edFirstName.getEditText().setText(profile.getFirstName());
        edMiddleName.getEditText().setText(profile.getMiddleName());
        edLastName.getEditText().setText(profile.getLastName());
        if(profile.getGender().equalsIgnoreCase("m")){
            spnGender.setSelection(1);
        }
        else {
            spnGender.setSelection(2);
        }
        if(profile.getMaritalStatus().equalsIgnoreCase("single")){
            spnMaritalStatus.setSelection(1);
        }
        else {
            spnMaritalStatus.setSelection(2);
        }
        spnDateOfBirth.setText(Utils.getFormattedDate(profile.getDateOfBirth(),C.SERVER_DATE_FORMAT,C.DATE_FORMAT));
        if(profile.getProfilePic()!=null && !profile.getProfilePic().equals("")) {



            Utils.displayImage(getActivity(),C.IMAGE_BASE_URL+profile.getProfilePic(),imgProfile);
            //TODO Image Display
            profile.setProfilePicname(profile.getProfilePic());
           new AsyncGettingBitmapFromUrl().execute();
            // imgProfile.setImageBitmap(Utils.getImageBitmapFromByte64(profile.get(0).getProfilePic()));
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
    private class AsyncGettingBitmapFromUrl extends AsyncTask<Void, Void, Bitmap> {




        @Override
        protected Bitmap doInBackground(Void... params) {
            return Utils.getBitmapFromUrl(C.IMAGE_BASE_URL+profile.getProfilePic());

            /*try {
                URL url = new URL(C.IMAGE_BASE_URL+profile.getProfilePic());
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return image;
            } catch(IOException e) {
                System.out.println(e);
            }
            return null;*/
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

           if(bitmap!=null)
            profile.setProfilePic(Utils.getBase64Image(bitmap));

        }
    }

}
