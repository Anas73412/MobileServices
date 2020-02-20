package in.binplus.mobusers.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

import in.binplus.mobusers.LoginActivity;
import in.binplus.mobusers.MainActivity;
import in.binplus.mobusers.Model.BarcodeDetailsModel;

import static in.binplus.mobusers.Config.Constants.*;
import static in.binplus.mobusers.Config.Constants.PREFS_NAME;


public class Session_management {

    SharedPreferences prefs;
    SharedPreferences prefs2;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    Context context;

    int PRIVATE_MODE = 0;

    public Session_management(Context context) {

        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();


    }

    public void createLoginSession(String id, String email, String name
            , String mobile,String pincode,String address) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_ADDRESS, address);

        editor.commit();
    }

    public void checkLogin() {

        if (!this.isLoggedIn()) {
            Intent loginsucces = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            loginsucces.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(loginsucces);
        }
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, prefs.getString(KEY_ID, null));
        // user email id
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, null));
        // user name
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));
        user.put(KEY_MOBILE, prefs.getString(KEY_MOBILE, null));
        user.put(KEY_PINCODE, prefs.getString(KEY_PINCODE, null));
        user.put(KEY_ADDRESS, prefs.getString(KEY_ADDRESS, null));

        // return user
        return user;
    }

    public void updateData(String name, String pincode,String address) {

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_ADDRESS, address);
        editor.apply();
    }



    public void logoutSession() {
        editor.clear();
        editor.commit();

        //cleardatetime();

        Intent logout = new Intent(context, MainActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void logoutSessionwithchangepassword() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }


    public void cleardatetime() {
        editor2.clear();
        editor2.commit();
    }

    // Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }


    public void setBarcoderData(String barcode,String sale_code,String activation_date,String end_date)
    {
        editor.putBoolean(IS_BARCODE,true);
        editor.putString(KEY_BARCODE,barcode);
        editor.putString(KEY_SALE_CODE,sale_code);
        editor.putString(KEY_ACTIVATION_DATE,activation_date);
        editor.putString(KEY_END_DATE,end_date);
        editor.commit();

    }

    public HashMap<String, String> getBarcodeDetails() {
        HashMap<String, String> barcode = new HashMap<String, String>();

        barcode.put(KEY_BARCODE, prefs.getString(KEY_BARCODE, null));
        barcode.put(KEY_SALE_CODE, prefs.getString(KEY_SALE_CODE, null));
        barcode.put(KEY_ACTIVATION_DATE, prefs.getString(KEY_ACTIVATION_DATE, null));
        barcode.put(KEY_END_DATE, prefs.getString(KEY_END_DATE, null));

        // return user
        return barcode;
    }
    public boolean isBarcodeIn() {
        return prefs.getBoolean(IS_BARCODE, false);
    }
}
