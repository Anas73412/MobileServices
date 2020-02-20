package in.binplus.mobusers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.bluetooth.le.ScanCallback;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;

import in.binplus.mobusers.Config.BaseURL;
import in.binplus.mobusers.Module.Module;
import in.binplus.mobusers.networkconnectivity.NoInternetConnection;
import in.binplus.mobusers.util.ConnectivityReceiver;
import in.binplus.mobusers.util.CustomVolleyJsonRequest;
import in.binplus.mobusers.util.Session_management;

import static in.binplus.mobusers.Config.Constants.KEY_ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
   TextInputEditText edt_code,edt_imei;
    Button btn_scan;
    String barcode_data="";
    String status="";
    String user_id="";
    Session_management session_management;
    TextInputLayout layout_imei,layout_code;
    Module module;
    Dialog loadingbar;
    RelativeLayout rel_scanned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingbar = new Dialog(MainActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
        loadingbar.setContentView(R.layout.progressbar);
        loadingbar.setCanceledOnTouchOutside(false);

        session_management=new Session_management(MainActivity.this);
        module=new Module(MainActivity.this);
        edt_code=(TextInputEditText)findViewById(R.id.edt_code);
        edt_imei=(TextInputEditText)findViewById(R.id.edt_imei);
        rel_scanned=(RelativeLayout) findViewById(R.id.rel_scanned);
        layout_code=(TextInputLayout) findViewById(R.id.layout_code);
        layout_imei=(TextInputLayout) findViewById(R.id.layout_imei);
        btn_scan=(Button) findViewById(R.id.btn_scan);
        barcode_data=getIntent().getStringExtra("barcode");
        status=getIntent().getStringExtra("status");
       // edt_code.setText(barcode_data);
        if(status.equalsIgnoreCase("pending"))
        {

            btn_scan.setText(getResources().getString(R.string.btn_scan));
        }
        else if(status.equalsIgnoreCase("scanned"))
        {
            layout_code.setVisibility(View.VISIBLE);
            layout_imei.setVisibility(View.VISIBLE);
            btn_scan.setText(getResources().getString(R.string.btn_activate));
            rel_scanned.setVisibility(View.VISIBLE);

        }

        btn_scan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();

        if(id == R.id.btn_scan)
        {
            if(status.equalsIgnoreCase("pending"))
            {
                Intent intent=new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(intent);

            }
            else
            {
               if(ConnectivityReceiver.isConnected())
               {
                   attemptActivateBarcode();
               }
               else
               {
                   Intent intent=new Intent(MainActivity.this, NoInternetConnection.class);
                   startActivity(intent);
               }



            }

        }
    }

    private void attemptActivateBarcode() {

       String sale_code=edt_code.getText().toString();
       String imei=edt_imei.getText().toString();
        user_id=session_management.getUserDetails().get(KEY_ID);

        if(sale_code.equals("") || sale_code.isEmpty())
       {
           edt_code.setError(getResources().getString(R.string.sale_code_reuired));
           edt_code.requestFocus();
       }else if(imei.equals("") || imei.isEmpty())
       {
           edt_imei.setError(getResources().getString(R.string.imei_reuired));
           edt_imei.requestFocus();
       }
       else
       {

           setBarcodeActivationData(user_id,sale_code,barcode_data,imei);

       }
    }

    private void setBarcodeActivationData(String user_id, String sale_code, String barcode_data,String imei) {
        loadingbar.show();
        String json_tag="json_insert_barcode";
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("user_id",user_id);
        map.put("sale_code",sale_code);
        map.put("barcode",barcode_data);
        map.put("imei_no",imei);

//Toast.makeText(MainActivity.this,"user_id :- "+user_id+"\n sale_code :- "+sale_code+"\n bar_code:- "+barcode_data,Toast.LENGTH_SHORT
//        ).show();
        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.ACTIVATE_BARCODE_URL, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingbar.dismiss();
                try
                {
                    Log.e("barcode_response",response.toString());
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        JSONObject object=response.getJSONObject("data");
                        session_management.setBarcoderData(object.getString("barcode").toString(),object.getString("sale_code").toString(),object.getString("activation_date").toString(),object.getString("end_date").toString());
                        Toast.makeText(MainActivity.this,"Barcode Activated Successfully!",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,""+response.getString("error").toString(),Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception edx)
                {
                    edx.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingbar.dismiss();
                String msg=module.VolleyError(error);
                if(!(msg.equals("") || msg.isEmpty()))
                {
                    Toast.makeText(MainActivity.this,""+msg.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);
    }
}
