package in.binplus.mobusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import in.binplus.mobusers.Config.BaseURL;
import in.binplus.mobusers.Module.Module;
import in.binplus.mobusers.networkconnectivity.NoInternetConnection;
import in.binplus.mobusers.util.ConnectivityReceiver;
import in.binplus.mobusers.util.CustomVolleyJsonRequest;
import in.binplus.mobusers.util.Session_management;

import static in.binplus.mobusers.Config.Constants.KEY_BARCODE;
import static in.binplus.mobusers.Config.Constants.KEY_ID;

public class RenewActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_renew_barcode;
    Dialog loadingbar;
    Session_management session_management;
    Module module;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew);
        loadingbar=new Dialog(RenewActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
        loadingbar.setContentView(R.layout.progressbar);
        loadingbar.setCanceledOnTouchOutside(false);
        module=new Module(RenewActivity.this);
        session_management=new Session_management(RenewActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_renew));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenewActivity.this, OrderHistory.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();

            }
        });
        btn_renew_barcode=(Button)findViewById(R.id.btn_renew_barcode);
        btn_renew_barcode.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        int id=v.getId();
        if(id == R.id.btn_renew_barcode)
        {

            String user_id=session_management.getUserDetails().get(KEY_ID);
            String barcode=session_management.getBarcodeDetails().get(KEY_BARCODE);

            if(!session_management.isBarcodeIn())
            {
                Toast.makeText(RenewActivity.this,"First Activate any barcode",Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(ConnectivityReceiver.isConnected())
                {
                    makeRenewRequest(user_id,barcode);
                }
                else {
                    Intent intent=new Intent(RenewActivity.this, NoInternetConnection.class);
                    startActivity(intent);
                }

            }

        }
    }

    private void makeRenewRequest(String user_id, String barcode) {

        loadingbar.show();
        String json_tag="json_renew_tag";
        HashMap<String,String> map=new HashMap<>();
        map.put("user_id",user_id);
        map.put("barcode",barcode);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.RENEW_BARCODE, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        Toast.makeText(RenewActivity.this,""+response.getString("message").toString(),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RenewActivity.this,HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(RenewActivity.this,""+response.getString("error").toString(),Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(RenewActivity.this,""+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingbar.dismiss();
                String msg=module.VolleyError(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText(RenewActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);

    }
}
