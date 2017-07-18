package com.softmine.dooktravel.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.softmine.dooktravel.R;
import com.softmine.dooktravel.pojos.SideMenuItem;

import java.util.ArrayList;

/**
 * Created by gaurav.garg on 30-06-2017.
 */

public class Utils {

    ProgressDialog progressDialog=null;
    public static ArrayList<SideMenuItem> getSideMenuList() {
        ArrayList<SideMenuItem> sideMenuItems = new ArrayList<SideMenuItem>();
        sideMenuItems.add(new SideMenuItem(R.string.update_profile, R.drawable.ic_menu_camera));
        sideMenuItems.add(new SideMenuItem(R.string.change_password, R.drawable.ic_menu_camera));
        sideMenuItems.add(new SideMenuItem(R.string.logout, R.drawable.ic_menu_camera));

        return sideMenuItems;
    }
    public static boolean isInternetOn(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void showDialog(String msg, Context context){
        if(progressDialog==null || !progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(msg);
            progressDialog.show();
        }
    }
   public void hideDialog(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    public static Typeface getRegularTypeFace(Context context){
        return Typeface.createFromAsset(context.getAssets(), "font/proxima-nova-regular.otf");
    }
    public static Typeface getRegularItalicTypeFace(Context context){
        return Typeface.createFromAsset(context.getAssets(), "font/proxima-nova-regular-italic.otf");
    }
    public static Typeface getSemiBoldTypeFace(Context context){
        return Typeface.createFromAsset(context.getAssets(), "font/proxima-nova-semibold.otf");
    }

    public static Typeface getLightTypeFace(Context context){
        return Typeface.createFromAsset(context.getAssets(), "font/proxima-nova-light.otf");
    }
}
