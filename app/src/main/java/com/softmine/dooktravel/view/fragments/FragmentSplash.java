package com.softmine.dooktravel.view.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.softmine.dooktravel.view.ActivityHome;
import com.softmine.dooktravel.view.MainActivity;
import com.softmine.dooktravel.R;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSplash extends Fragment {
    Utils util;
    Handler handler = new Handler();
    public FragmentSplash() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        util=new Utils();
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    Log.e("DEBUG", "ggg");
                    if (isStoragePermissionGranted()) {
                        if (util.isInternetOn(getActivity())) {
                            if (!SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)) {
                                ((MainActivity) getActivity()).fragmnetLoader(C.FRAGMENT_LOGIN, null);
                            } else {
                                Intent intent = new Intent(getActivity(), ActivityHome.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } else {
                            getDailogConfirm(getString(R.string.internet_issue)
                                    , "Internet Issue");
                        }
                    } else {
                        requestPermissionForStorage();
                    }
                }
            }, C.SPLASH_LOADER_TIME);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void requestPermissionForStorage() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //     ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Utils.PERMISSION_REQUEST_CODE);
            getDailogConfirm("Please allow storage permission in App Settings for additional functionality.", "");
            //  Toast.makeText(getActivity(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                //  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
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
