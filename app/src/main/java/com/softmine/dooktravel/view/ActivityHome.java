package com.softmine.dooktravel.view;

import android.app.Dialog;
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
import com.softmine.dooktravel.R;
import com.softmine.dooktravel.adapter.AdapterSideMenu;
import com.softmine.dooktravel.model.Profile;
import com.softmine.dooktravel.model.RegisterStatus;
import com.softmine.dooktravel.pojos.ProfileStatus;
import com.softmine.dooktravel.presenter.ActivityHomePresenterImpl;
import com.softmine.dooktravel.presenter.IActivityHomePresenter;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.SharedPreference;
import com.softmine.dooktravel.util.Utils;
import com.softmine.dooktravel.view.fragments.FragmentBasicDetail;
import com.softmine.dooktravel.view.fragments.FragmentChangePassword;
import com.softmine.dooktravel.view.fragments.FragmentContactDetail;
import com.softmine.dooktravel.view.fragments.FragmentProfileDetail;
import com.softmine.dooktravel.view.fragments.FragmentSearchResult;
import com.softmine.dooktravel.view.fragments.IFragmentView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,IFragmentView {
    private AdapterSideMenu adapterSideMenu;
    ListView listView;
    private Fragment fragment;
    TextView tvTitle;
   public static TextView tvEdit;
    ImageView imgTitle;
    int mSelectedPos=0;
    CircleImageView circleImageView;
    String action;
    int save = 0;
    int s=6;
    List<Profile> profile;
    Utils utils;
    TextView tvSideMenu;
    IActivityHomePresenter mIActivityHomePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                if(Build.VERSION.SDK_INT>=21){
                    getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_home);
        utils=new Utils();
        mIActivityHomePresenter=new ActivityHomePresenterImpl(this,ActivityHome.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        circleImageView=(CircleImageView)findViewById(R.id.profile_image);
        tvTitle=(TextView)findViewById(R.id.tvTitle);
        imgTitle=(ImageView)findViewById(R.id.imgLogo);
        tvEdit=(TextView)findViewById(R.id.tvEdit);
        tvEdit.setOnClickListener(mTvEditClickListner);

        listView=(ListView) findViewById(R.id.lvMenuItem);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        try {
            Bundle bundle = getIntent().getExtras();
             s = getIntent().getIntExtra(C.SCREEN, C.FRAGMENT_SEARCH_RESULT);
            if(s==C.FRAGMENT_BASIC_DETAIL){
                save=1;
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }
        adapterSideMenu = new AdapterSideMenu(this, Utils.getSideMenuList(),s);
        listView.setAdapter(adapterSideMenu);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);


                if(position!=2) {
                    parent.getChildAt(position).setBackgroundColor(
                            getResources().getColor(R.color.hint));

                    if (tvSideMenu == null) {

                        TextView t = (TextView) AdapterSideMenu.view.findViewById(R.id.tvMenuName);

                        t.setTextColor(getResources().getColor(R.color.side_item_color));
                    } else {
                        tvSideMenu.setTextColor(getResources().getColor(R.color.side_item_color));

                    }

                    TextView t = (TextView) view.findViewById(R.id.tvMenuName);
                    t.setTextColor(getResources().getColor(R.color.background_blue));

                    tvSideMenu = t;

                    if (save != -1 && save != position) {
                        parent.getChildAt(save).setBackgroundColor(
                                getResources().getColor(R.color.white));

                   /* RelativeLayout relativeLayout = (RelativeLayout) view.getParent();
                    TextView textView = (TextView) relativeLayout.getChildAt(1);
                    textView.setTextColor(getResources().getColor(R.color.side_item_color));*/
                    }

                    save = position;


                }
                if(position==0&& mSelectedPos!=position){
                    mSelectedPos=position;
                    fragmnetLoader(C.FRAGMENT_SEARCH_RESULT,null);
                }
                else if(position==0 &&fragment !=null && fragment instanceof FragmentProfileDetail){
                    fragmnetLoader(C.FRAGMENT_SEARCH_RESULT,null);
                   // onBackPressed();
                }
               else if(position==1 && mSelectedPos!=position){
                    mSelectedPos=position;
                    Bundle bundle=new Bundle();
                    if(profile!=null) {
                        bundle.putSerializable(C.DATA, profile.get(0));
                    }
                    bundle.putBoolean(C.ADD_TO_BACK,true);
                    bundle.putBoolean(C.IS_EDIT_PROFILE,false);
                    if(profile!=null && profile.size()>0) {
                        fragmnetLoader(C.FRAGMENT_BASIC_DETAIL, bundle);
                    }
                }
                else if(position==4 && mSelectedPos!=position){
                    mSelectedPos=position;
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(C.DATA,profile.get(0));
                    bundle.putBoolean(C.IS_EDIT_PROFILE,true);
                    if(profile!=null && profile.size()>0) {
                        fragmnetLoader(C.FRAGMENT_BASIC_DETAIL, bundle);
                    }
                }
                else if(position==3&& mSelectedPos!=position){
                    mSelectedPos=position;
                    fragmnetLoader(C.FRAGMENT_CHANGE_PASSWORD,null);
                }
                else if(position==2&&mSelectedPos!=position){
                   // mSelectedPos=position;
                    getDailogConfirm("Are you sure you want to logout?","1");
                }
            }
        });
        try {
            Bundle bundle = getIntent().getExtras();
            int screen = getIntent().getIntExtra(C.SCREEN, C.FRAGMENT_SEARCH_RESULT);
            fragmnetLoader(screen, bundle);
        }
        catch (Exception e){
            e.printStackTrace();
           fragmnetLoader(C.FRAGMENT_SEARCH_RESULT, null);

        }
        getProfileDetail();
    }
    void getDailogConfirm(final String dataText, final String titleText) {
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
            titleTextTv.setTypeface(Utils.getRegularTypeFace(ActivityHome.this));
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
                    if(titleText.equals("1")) {
                        logout();
                    }

                }
            });


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    View.OnClickListener mTvEditClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSelectedPos=1;
            Bundle bundle=new Bundle();
            bundle.putSerializable(C.DATA,profile.get(0));
            bundle.putBoolean(C.IS_EDIT_PROFILE,true);
            bundle.putBoolean(C.ADD_TO_BACK,true);
            if(profile!=null && profile.size()>0) {
                fragmnetLoader(C.FRAGMENT_BASIC_DETAIL, bundle);
            }
        }
    };
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
            action=C.LOGOUT;
            mIActivityHomePresenter.logout(jsonBody);
        }
    }

    void getProfileDetail(){

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
            action=C.PROFILE_METHOD;
            mIActivityHomePresenter.getProfileInfo(jsonBody,false);

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
                    tvTitle.setText(getResources().getString(R.string.profile));
                }
              /*  else if(fragmentTag.equals(C.TAG_FRAGMENT_PROFESSIONAL_DETAIL)){
                    tvTitle.setText(getResources().getString(profile));
                }*/
                /*else if(fragmentTag.equals(C.TAG_FRAGMENT_PROFILE_DETAIL)){
                    tvTitle.setText(getResources().getString(profile));
                }*/
                else if(fragmentTag.equals(C.TAG_FRAGMENT_SEARCH_RESULT)){
                    imgTitle.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.VISIBLE);
                    tvTitle.setText(getResources().getString(R.string.find_people));

                }
               /* else if(fragmentTag.equals(C.TAG_FRAGMENT_CONTACT_DETAIL)){
                    tvTitle.setText(getResources().getString(profile));
                }*/


            } else {
                if(fragment instanceof FragmentProfileDetail){
                    imgTitle.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.VISIBLE);
                    tvTitle.setText(getResources().getString(R.string.find_people));
                    mSelectedPos=0;
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
                tvTitle.setText(getResources().getString(R.string.profile));
                fragment = new FragmentBasicDetail();
                fragmentTransaction.replace(R.id.container, fragment);

                     if(bundle.getBoolean(C.IS_EDIT_PROFILE)){
                         mSelectedPos=1;
                         tvTitle.setText(getResources().getString(R.string.updateprofile));

                     }
                     else {
                         mSelectedPos=1;
                     }
                 //    fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_BASIC_DETAIL);

                break;
            case C.FRAGMENT_PROFILE_DETAIL:
                tvTitle.setVisibility(View.VISIBLE);
                imgTitle.setVisibility(View.GONE);
                tvTitle.setText(getResources().getString(R.string.profile));
                fragment = new FragmentProfileDetail();
                fragmentTransaction.add(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_PROFILE_DETAIL);
                break;

            case C.FRAGMENT_SEARCH_RESULT:
                mSelectedPos=0;
                getSupportFragmentManager().popBackStack(0,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                imgTitle.setVisibility(View.GONE);
                tvTitle.setText(getResources().getString(R.string.find_people));
                tvTitle.setVisibility(View.VISIBLE);
                fragment = new FragmentSearchResult();
                fragmentTransaction.replace(R.id.container, fragment);
               // fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_SEARCH_RESULT);
                break;

            case C.FRAGMENT_CONTACT_DETAIL:
//                tvTitle.setText(getResources().getString(profile));
                fragment = new FragmentContactDetail();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_CONTACT_DETAIL);
                break;

            case C.FRAGMENT_CHANGE_PASSWORD:
                imgTitle.setVisibility(View.GONE);
                tvTitle.setText(getResources().getString(R.string.change_password));
                tvTitle.setVisibility(View.VISIBLE);
                fragment = new FragmentChangePassword();
                fragmentTransaction.replace(R.id.container, fragment);
             //   fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_CHANGE_PASSWORD);
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
    public void getResponseSuccess(String response) {
        Log.e(ActivityHome.class.getName(), "RESPONSE==" + response);
        if(action.equals(C.LOGOUT)) {

            Gson gson = new Gson();
            RegisterStatus registerStatus = gson.fromJson(response, RegisterStatus.class);
            if (!registerStatus.getError()) {
                SharedPreference.getInstance(ActivityHome.this).setBoolean(C.IS_LOGIN, false);
                C.isloggedIn = false;
                SharedPreference.getInstance(ActivityHome.this).clearData();
                Intent intent = new Intent(ActivityHome.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                if(registerStatus.getMessage().equals(C.InvalidToken)){
                    Utils.showToast(this, C.Session_expired);
                    SharedPreference.getInstance(this).setBoolean(C.IS_LOGIN,false);
                    Intent intent=new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(C.SCREEN,C.FRAGMENT_LOGIN);
                    startActivity(intent);
                }
                else {
                    getDailog(registerStatus.getMessage(), "");
                }
            }
        }
        else if(action.equals(C.PROFILE_METHOD)) {
            Gson gson = new Gson();
            ProfileStatus profileStatus = gson.fromJson(response, ProfileStatus.class);
            if (!profileStatus.getError()) {
                profile = profileStatus.getMember();
                if(profile.get(0).getProfilePic()!=null && !profile.get(0).getProfilePic().equals("")) {
                    Utils.displayImage(this, C.IMAGE_BASE_URL + profile.get(0).getProfilePic(), circleImageView);
                 //   Utils.displayImage(this, profile.get(0).getProfilePic(), circleImageView);
                }
                //TODO Image Display
                // imgProfile.setImageBitmap(Utils.getImageBitmapFromByte64(profile.get(0).getProfilePic()));
            } else {
                if(profileStatus.getMessage().equals(C.InvalidToken)){
                    Utils.showToast(this, C.Session_expired);
                    SharedPreference.getInstance(this).setBoolean(C.IS_LOGIN,false);
                    Intent intent=new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(C.SCREEN,C.FRAGMENT_LOGIN);
                    startActivity(intent);
                }
                else {
                    getDailogConfirm(profileStatus.getMessage(), "");
                }
            }

        }
    }

    @Override
    public void getResponseError(String response) {

    }

    @Override
    public void showProgress() {
        utils.showDialog(C.MSG,this);
    }

    @Override
    public void hideProgress() {
        utils.hideDialog();
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
