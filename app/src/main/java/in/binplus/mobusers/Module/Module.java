package in.binplus.mobusers.Module;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 16,January,2020
 */
public class Module {

    Context context;

    public Module(Context context) {
        this.context = context;
    }


    public String VolleyError(VolleyError error)
    {
        String str_error="";
        if(error instanceof TimeoutError)
        {
            str_error="Slow Internet Connection";
        }
        else if (error instanceof AuthFailureError) {
            str_error="Session Timeout";
            //TODO
        } else if (error instanceof ServerError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof NetworkError) {
            str_error="Network Problem";
            //TODO
        } else if (error instanceof ParseError) {
            //TODO
            str_error="Something Went Wrong \n Please try again later";
        }else if(error instanceof NoConnectionError){
            str_error="no Internet Connection";
        }
        return str_error;
    }

}
