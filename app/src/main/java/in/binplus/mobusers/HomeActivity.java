package in.binplus.mobusers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import in.binplus.mobusers.Fragment.DetailsFragment;
import in.binplus.mobusers.Fragment.Contact_Fragment;
import in.binplus.mobusers.Fragment.HomeFragment;
import in.binplus.mobusers.Fragment.PassowrdFragment;
import in.binplus.mobusers.Fragment.PrivacyFragment;
import in.binplus.mobusers.Fragment.ProfileFragment;
import in.binplus.mobusers.Fragment.TermConditionFragment;
import in.binplus.mobusers.networkconnectivity.NoInternetConnection;
import in.binplus.mobusers.util.ConnectivityReceiver;
import in.binplus.mobusers.util.Session_management;

import static in.binplus.mobusers.Config.Constants.KEY_EMAIL;
import static in.binplus.mobusers.Config.Constants.KEY_MOBILE;
import static in.binplus.mobusers.Config.Constants.KEY_NAME;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Session_management session_management;
    TextView txt_name,txt_mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
      Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

         setSupportActionBar(toolbar);

         session_management=new Session_management(HomeActivity.this);
         drawerLayout = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        navigationView.bringToFront();
       View header=((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
       txt_mobile=(TextView)header.findViewById(R.id.txt_mobile);
       txt_name=(TextView)header.findViewById(R.id.txt_name);

       txt_name.setText(session_management.getUserDetails().get(KEY_NAME));
       txt_mobile.setText(session_management.getUserDetails().get(KEY_MOBILE)+"\n"+session_management.getUserDetails().get(KEY_EMAIL));

        getSupportActionBar().setTitle(getResources().getString(R.string.name));

        checkConnection();

        if (savedInstanceState == null) {
            HomeFragment fm = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_panel, fm, "HomeFragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    Fragment fr = getSupportFragmentManager().findFragmentById(R.id.content_panel);

                    final String fm_name = fr.getClass().getSimpleName();
                    Log.e("backstack: ", ": " + fm_name);
                    if (fm_name.contentEquals("HomeFragment")) {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        toggle.setDrawerIndicatorEnabled(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();

                    } else if (fm_name.contentEquals("My_order_fragment") ||
                            fm_name.contentEquals("Thanks_fragment")) {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HomeFragment fm = new HomeFragment();
                               FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.content_panel, fm)
                                        .addToBackStack(null).commit();
                            }
                        });
                    } else {

                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                onBackPressed();
                            }
                        });
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

//        HomeFragment home_fragment=new HomeFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_panel, home_fragment)
//                .addToBackStack(null).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setTitle(getResources().getString(R.string.name));
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id =menuItem.getItemId();
        Fragment fm=null;
        if(id == R.id.nav_home)
        {
      fm=new HomeFragment();
        }
        else if(id == R.id.nav_barcode)
        {
        fm=new DetailsFragment();
        }
        else if(id == R.id.nav_password)
        {
            fm=new PassowrdFragment();
        }else if(id == R.id.nav_profile)
        {
            fm=new ProfileFragment();
        }else if(id == R.id.nav_contact)
        {
            fm=new Contact_Fragment();
        }else if(id == R.id.nav_privacy)
        {
            fm=new PrivacyFragment();
        }else if(id == R.id.nav_terms)
        {
            fm=new TermConditionFragment();
        }
        else if(id == R.id.nav_logout)
        {
          session_management.logoutSession();
          Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
          finish();
        }
        else if( id == R.id.nav_history)
        {
            Intent intent=new Intent(HomeActivity.this,OrderHistory.class);
            startActivity(intent);
        }
        if (fm != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_panel, fm)
                    .addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;

        if (!isConnected) {
            Intent intent = new Intent(HomeActivity.this, NoInternetConnection.class);
            startActivity(intent);
        }
    }
}
