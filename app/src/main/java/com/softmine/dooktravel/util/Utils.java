package com.softmine.dooktravel.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softmine.dooktravel.R;
import com.softmine.dooktravel.pojos.SideMenuItem;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gaurav.garg on 30-06-2017.
 */

public class Utils {

    ProgressDialog progressDialog=null;
    public static ArrayList<SideMenuItem> getSideMenuList() {
        ArrayList<SideMenuItem> sideMenuItems = new ArrayList<SideMenuItem>();
        sideMenuItems.add(new SideMenuItem(R.string.search, R.drawable.search));
        sideMenuItems.add(new SideMenuItem(R.string.profile, R.drawable.user));
        //sideMenuItems.add(new SideMenuItem(R.string.update_profile, R.drawable.user));
       // sideMenuItems.add(new SideMenuItem(R.string.change_password, R.drawable.user));
        sideMenuItems.add(new SideMenuItem(R.string.logout, R.drawable.logout));

        return sideMenuItems;
    }
    public static boolean isInternetOn(Activity activity) {
        try {
            ConnectivityManager cm = (ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        }
        catch (Exception e){
            return false;
        }
        return false;
    }

    public void showDialog(String msg, Context context){
        try {

            if (progressDialog == null || !progressDialog.isShowing()) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage(msg);
                progressDialog.show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
   public void hideDialog(){
       try {

           if (progressDialog.isShowing()) {
               progressDialog.dismiss();
           }
       }
       catch (Exception e){
           e.printStackTrace();
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
    public static String getBase64Image(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        //  byte[] b = baos.toByteArray();
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
  public static  Bitmap getImageBitmapFromByte64(String encodedImage ) {
        try {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return  decodedByte;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmapFromUrl(String u){
        try {
            URL url = new URL(u);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        } catch(IOException e) {
            System.out.println(e);
        }
        return null;
    }
    public static void displayImage(Context context, String imageUrl, ImageView imageView){
        Picasso.with(context)
                .load(imageUrl)
                .resize(100,100)
                   .placeholder(R.drawable.placeholder_man)
                .error(R.drawable.placeholder_man)
                .into(imageView);
    }

    public  static String getFormattedDate(String date, String dateFormat, String DesiredFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DesiredFormat);
        String newFormat = formatter.format(testDate);
        return newFormat;
    }

    public static String getCurrentTimeStamp(){
       /* SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        Date date=new Date();
        String timeStamp = formatter.format(date);
        return timeStamp;*/
       return String.valueOf(Calendar.getInstance()
                .getTimeInMillis());
    }

    public  static void showToast(Context context,String msg){

        Toast toast=Toast.makeText(context,msg,Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTypeface(getRegularTypeFace(context));
        messageTextView.setTextSize(14);
        toast.show();
    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getSubstringPhone(String number){
        return number.substring(0,number.length());
    }

}
