package in.binplus.mobusers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;

import in.binplus.mobusers.Config.BaseURL;
import in.binplus.mobusers.networkconnectivity.NoInternetConnection;
import in.binplus.mobusers.util.ConnectivityReceiver;
import in.binplus.mobusers.util.CustomVolleyJsonRequest;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_login;
    Button btn_reg;
    TextInputEditText edt_con_pass,edt_pass,edt_email,edt_mobile,edt_name,edt_pincode,edt_address;
    Dialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loadingBar=new Dialog(RegistrationActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        txt_login=(TextView)findViewById(R.id.txt_login);
        btn_reg=(Button)findViewById(R.id.btn_reg);

        edt_name=(TextInputEditText)findViewById(R.id.edt_name);
        edt_mobile=(TextInputEditText)findViewById(R.id.edt_mobile);
        edt_email=(TextInputEditText)findViewById(R.id.edt_email);
        edt_pass=(TextInputEditText)findViewById(R.id.edt_pass);
        edt_con_pass=(TextInputEditText)findViewById(R.id.edt_con_pass);
        edt_address=(TextInputEditText)findViewById(R.id.edt_address);
        edt_pincode=(TextInputEditText)findViewById(R.id.edt_pincode);

        txt_login.setOnClickListener(this);
        btn_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();

        if(id == R.id.btn_reg)
        {
            attemptRegistration();
        } else if(id == R.id.txt_login)
        {
            Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void attemptRegistration() {

        String name=edt_name.getText().toString();
        String mobile=edt_mobile.getText().toString();
        String email=edt_email.getText().toString();
        String pass=edt_pass.getText().toString();
        String c_pass=edt_con_pass.getText().toString();
        String pincode=edt_pincode.getText().toString();
        String address=edt_address.getText().toString();

        if(name.equals(""))
        {
            edt_name.setError(getResources().getString(R.string.name_required));
            edt_name.requestFocus();
        }
        else if(mobile.equals(""))
        {
            edt_mobile.setError(getResources().getString(R.string.mobile_required));
            edt_mobile.requestFocus();

        }
        else if(email.equals(""))
        {
            edt_email.setError(getResources().getString(R.string.email_required));
            edt_email.requestFocus();
        }
        else if(pincode.equals(""))
        {
            edt_pincode.setError(getResources().getString(R.string.picode_required));
            edt_pincode.requestFocus();
        }
        else if(!(mobile.length()==10))
        {
            edt_mobile.setError(getResources().getString(R.string.invalid_mobile));
            edt_mobile.requestFocus();

        }
        else if(!(email.contains("@")))
        {
            edt_email.setError(getResources().getString(R.string.invalid_email));
            edt_email.requestFocus();

        }
        else if(pincode.length()!=6)
        {
            edt_pincode.setError(getResources().getString(R.string.invalid_pincode));
            edt_pincode.requestFocus();
        }
        else if(pass.equals(""))
        {
            edt_pass.setError("Enter Password");
            edt_pass.requestFocus();
        }
        else if(c_pass.equals(""))
        {
            edt_con_pass.setError("Enter Confirm Password");
            edt_con_pass.requestFocus();
        }
        else if(!(pass.length()>5))
        {
            edt_pass.setError(getResources().getString(R.string.password_length));
            edt_pass.requestFocus();

        }
        else
        {
            if(pass.equals(c_pass))
            {
                if(ConnectivityReceiver.isConnected())
                {
                    insertData(name,mobile,email,pass,pincode,address);
                }
                else
                {

                    Intent intent=new Intent(RegistrationActivity.this, NoInternetConnection.class);
                    startActivity(intent);
                }

            }
            else
            {
                Toast.makeText(RegistrationActivity.this,getResources().getString(R.string.password_matched),Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void insertData(String name, String mobile, String email, String pass,String pin, String adress) {
       loadingBar.show();
        String json_tag="json_register_tag";
        HashMap<String,String> params=new HashMap<>();
        params.put("name",name);
        params.put("mobile",mobile);
        params.put("email",email);
        params.put("password",pass);
        params.put("pincode",pin);
        params.put("address",adress);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.REGISTER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    loadingBar.dismiss();
                    boolean responce=response.getBoolean("responce");
                    if(responce)
                    {
                        Toast.makeText(RegistrationActivity.this,""+response.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(RegistrationActivity.this,""+response.getString("error"),Toast.LENGTH_SHORT).show();
                    }
                  //  Toast.makeText(RegistrationActivity.this,""+response.toString(),Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex)
                {
                    loadingBar.dismiss();
                    ex.printStackTrace();
                    Toast.makeText(RegistrationActivity.this,""+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadingBar.dismiss();
                Toast.makeText(RegistrationActivity.this,""+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);

    }
}
