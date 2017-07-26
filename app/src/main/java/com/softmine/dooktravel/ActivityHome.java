package com.softmine.dooktravel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softmine.dooktravel.adapter.AdapterSideMenu;
import com.softmine.dooktravel.fragments.FragmentBasicDetail;
import com.softmine.dooktravel.fragments.FragmentChangePassword;
import com.softmine.dooktravel.fragments.FragmentContactDetail;
import com.softmine.dooktravel.fragments.FragmentProfessionalDetail;
import com.softmine.dooktravel.fragments.FragmentProfileDetail;
import com.softmine.dooktravel.fragments.FragmentSearchResult;
import com.softmine.dooktravel.pojos.RegisterStatus;
import com.softmine.dooktravel.serviceconnection.CompleteListener;
import com.softmine.dooktravel.serviceconnection.ServiceConnection;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,CompleteListener {
    private AdapterSideMenu adapterSideMenu;
    ListView listView;
    private Fragment fragment;
    TextView tvTitle;
    ImageView imgTitle;
    int mSelectedPos=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                if(Build.VERSION.SDK_INT>=21){
                    getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvTitle=(TextView)findViewById(R.id.tvTitle);
        imgTitle=(ImageView)findViewById(R.id.imgLogo);
        tvTitle.setTypeface(Utils.getRegularTypeFace(this));
        listView=(ListView) findViewById(R.id.lvMenuItem);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        adapterSideMenu = new AdapterSideMenu(this, Utils.getSideMenuList());
        listView.setAdapter(adapterSideMenu);
        fragmnetLoader(C.FRAGMENT_SEARCH_RESULT,null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                if(position==0 && mSelectedPos!=position){
                    mSelectedPos=position;
                    fragmnetLoader(C.FRAGMENT_BASIC_DETAIL,null);
                }
                else if(position==1 && mSelectedPos!=position){
                    mSelectedPos=position;
                    fragmnetLoader(C.FRAGMENT_CHANGE_PASSWORD,null);
                }
                else if(position==2&&mSelectedPos!=position){
                    mSelectedPos=position;
                    getDailogConfirm("Are you sure you want to logout?","");
                }
            }
        });
    }
    void getDailogConfirm(String dataText, String titleText) {
        try {
            final Dialog dialog = new Dialog(ActivityHome.this);
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

            cancelTv.setVisibility(View.VISIBLE);
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
                    logout();

                }
            });


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void logout(){
        if(SharedPreference.getInstance(this).getBoolean(C.IS_LOGIN)) {
            JSONObject jsonBody = new JSONObject();
            try {
             /*   jsonBody.put(C.EMAIL, "pradeep.bansal@techmobia.com");
                jsonBody.put(C.PASSWORD, "abc123");
                jsonBody.put(C.SOCAIL_ID, "");*/
                jsonBody.put(C.MEMBER_ID, SharedPreference.getInstance(this).getString(C.MEMBER_ID));
                jsonBody.put(C.TOKEN, SharedPreference.getInstance(this).getString(C.TOKEN));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ServiceConnection serviceConnection = new ServiceConnection();
            serviceConnection.makeJsonObjectRequest(C.LOGOUT, jsonBody, ActivityHome.this);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            getSupportFragmentManager().executePendingTransactions();
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

//            Log.e("CountPop", getSupportFragmentManager().getBackStackEntryCount() + "");

                int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
                FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(fragmentCount - 2);
                String fragmentTag = backEntry.getName();

                getSupportFragmentManager().popBackStack();

                getSupportFragmentManager().executePendingTransactions();

                if(fragmentTag.equals(C.TAG_FRAGMENT_BASIC_DETAIL)){
                    tvTitle.setText(getResources().getString(R.string.basic_details));
                }
                else if(fragmentTag.equals(C.TAG_FRAGMENT_PROFESSIONAL_DETAIL)){
                    tvTitle.setText(getResources().getString(R.string.profile));
                }
                else if(fragmentTag.equals(C.TAG_FRAGMENT_PROFILE_DETAIL)){
                    tvTitle.setText(getResources().getString(R.string.profile));
                }
                else if(fragmentTag.equals(C.TAG_FRAGMENT_SEARCH_RESULT)){
                    tvTitle.setText(getResources().getString(R.string.logo));

                }
                else if(fragmentTag.equals(C.TAG_FRAGMENT_CONTACT_DETAIL)){
                    tvTitle.setText(getResources().getString(R.string.profile));
                }


            } else {
                if(fragment instanceof FragmentBasicDetail){
                    imgTitle.setVisibility(View.VISIBLE);
                    tvTitle.setText(getResources().getString(R.string.logo));
                    tvTitle.setVisibility(View.GONE);
                    mSelectedPos=-1;
                }
                super.onBackPressed();

            }
        }
    }

    public void fragmnetLoader(int fragmentType, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        imgTitle.setVisibility(View.GONE);

        switch (fragmentType) {
            case C.FRAGMENT_BASIC_DETAIL:
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(getResources().getString(R.string.basic_details));
                fragment = new FragmentBasicDetail();
                fragmentTransaction.add(R.id.container, fragment);
                 fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_BASIC_DETAIL);
                break;
            case C.FRAGMENT_PROFILE_DETAIL:
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(getResources().getString(R.string.profile));
                fragment = new FragmentProfileDetail();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_PROFILE_DETAIL);
                break;

            case C.FRAGMENT_SEARCH_RESULT:
                imgTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(getResources().getString(R.string.logo));
                tvTitle.setVisibility(View.GONE);
                fragment = new FragmentSearchResult();
                fragmentTransaction.replace(R.id.container, fragment);
               // fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_SEARCH_RESULT);
                break;

            case C.FRAGMENT_CONTACT_DETAIL:
                tvTitle.setText(getResources().getString(R.string.profile));
                fragment = new FragmentContactDetail();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_CONTACT_DETAIL);
                break;

            case C.FRAGMENT_CHANGE_PASSWORD:
                imgTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(getResources().getString(R.string.logo));
                tvTitle.setVisibility(View.GONE);
                fragment = new FragmentChangePassword();
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_CHANGE_PASSWORD);
                break;
        }
        fragment.setArguments(bundle);

        fragmentTransaction.commit();
        getSupportFragmentManager().executePendingTransactions();


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void done(String response) {
        Log.e(ActivityHome.class.getName(),"RESPONSE=="+response);
        Gson gson = new Gson();
        RegisterStatus registerStatus= gson.fromJson(response,RegisterStatus.class);
        if(!registerStatus.getError()){
            SharedPreference.getInstance(ActivityHome.this).setBoolean(C.IS_LOGIN,false);
            SharedPreference.getInstance(ActivityHome.this).clearData();
            Intent intent = new Intent(ActivityHome.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else {
            getDailog(registerStatus.getMessage(),"");
        }
    }

    @Override
    public Context getApplicationsContext() {
        return ActivityHome.this;
    }

    void getDailog(String dataText, String titleText) {
        try {
            final Dialog dialog = new Dialog(this);
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
