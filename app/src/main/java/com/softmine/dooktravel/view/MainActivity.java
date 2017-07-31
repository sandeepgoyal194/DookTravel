package com.softmine.dooktravel.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.softmine.dooktravel.R;
import com.softmine.dooktravel.view.fragments.FragmentBasicDetail;
import com.softmine.dooktravel.view.fragments.FragmentContactDetail;
import com.softmine.dooktravel.view.fragments.FragmentForgotPassword;
import com.softmine.dooktravel.view.fragments.FragmentLogin;
import com.softmine.dooktravel.view.fragments.FragmentSignUp;
import com.softmine.dooktravel.view.fragments.FragmentSplash;
import com.softmine.dooktravel.util.C;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tvTitle;
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        Bundle bundle = getIntent().getExtras();

        if(bundle !=null)
        {
            int screen = bundle.getInt(C.SCREEN);
            fragmnetLoader(screen,null);
            // DO SOMETHING HERE
        }
        else
        {
            fragmnetLoader(C.FRAGMENT_SPLASH,null);
        }

    }


    public void fragmnetLoader(int fragmentType, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (fragmentType) {
            case C.FRAGMENT_SPLASH:
                getSupportActionBar().hide();
                fragment = new FragmentSplash();
                fragmentTransaction.replace(R.id.container, fragment);
               // fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_SPLASH);
                break;
          case C.FRAGMENT_LOGIN:
                getSupportActionBar().hide();
                fragment = new FragmentLogin();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_LOGIN);
                break;
            case C.FRAGMENT_SIGNUP:
                getSupportActionBar().hide();
                fragment = new FragmentSignUp();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_SIGNUP);
                break;
            case C.FRAGMENT_BASIC_DETAIL:
                tvTitle.setText(getResources().getString(R.string.profile));
                fragment = new FragmentBasicDetail();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_BASIC_DETAIL);
                break;
            case C.FRAGMENT_CONTACT_DETAIL:
                tvTitle.setText(getResources().getString(R.string.profile));
                fragment = new FragmentContactDetail();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_CONTACT_DETAIL);
                break;
            case C.FRAGMENT_FORGOT_PASSWORD:

                fragment = new FragmentForgotPassword();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_FORGOT_PASSWORD);
                break;
        }
        fragment.setArguments(bundle);

        fragmentTransaction.commit();
        getSupportFragmentManager().executePendingTransactions();


    }
    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    @Override
    public void onBackPressed() {

        getSupportFragmentManager().executePendingTransactions();
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

//            Log.e("CountPop", getSupportFragmentManager().getBackStackEntryCount() + "");

            int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(fragmentCount - 2);
            String fragmentTag = backEntry.getName();

            getSupportFragmentManager().popBackStack();

            getSupportFragmentManager().executePendingTransactions();


        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            fragment=getVisibleFragment();
            if (fragment instanceof FragmentLogin || fragment instanceof FragmentSignUp ) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
