package in.binplus.mobusers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
import in.binplus.mobusers.Module.Module;
import in.binplus.mobusers.networkconnectivity.NoInternetConnection;
import in.binplus.mobusers.util.ConnectivityReceiver;
import in.binplus.mobusers.util.CustomVolleyJsonRequest;
import in.binplus.mobusers.util.Session_management;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
Session_management session_management;
    Button btn_login,btn_reg;
    TextView btn_forgot;
    TextInputEditText edt_pass,edt_mobile;
    Dialog loadingBar;
    Module module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingBar=new Dialog(LoginActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView(R.layout.progressbar);
        loadingBar.setCanceledOnTouchOutside(false);
        module=new Module(LoginActivity.this);
        session_management=new Session_management(LoginActivity.this);
        edt_mobile=(TextInputEditText)findViewById(R.id.edt_mobile);
        edt_pass=(TextInputEditText)findViewById(R.id.edt_pass);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_reg=(Button)findViewById(R.id.btn_reg);
        btn_forgot=(TextView) findViewById(R.id.btn_forgot);

        btn_forgot.setOnClickListener(this);
        btn_reg.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();

        if(id == R.id.btn_login)
        {
          attemptLogin();
        }
        else if(id == R.id.btn_reg)
        {
            Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.btn_forgot)
        {
            Dialog dialog=new Dialog(LoginActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.forgot_password_layout);
            dialog.show();
        }
    }

    private void attemptLogin() {

        String mobile=edt_mobile.getText().toString();
        String pass=edt_pass.getText().toString();

        if(mobile.equals(""))
        {
            edt_mobile.setError("Enter Mobile Number");
            edt_mobile.requestFocus();
        }
        else if(mobile.length()!=10)
        {
            edt_mobile.setError("Invalid Mobile Number");
            edt_mobile.requestFocus();
        }
        else if(pass.equals(""))
        {
            edt_pass.setError("Enter Password");
            edt_pass.requestFocus();

        }
        else
        {
            if(ConnectivityReceiver.isConnected())
            {
                login(mobile,pass);
            }
            else
            {
                Intent intent=new Intent(LoginActivity.this, NoInternetConnection.class);
                startActivity(intent);
            }

        }
    }

    private void login(final String mobile, String pass) {

        loadingBar.show();
        String json_tag="json_login_tag";
        final HashMap<String,String> map=new HashMap<>();
        map.put("mobile",mobile);
        map.put("password",pass);

        CustomVolleyJsonRequest jsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.LOGIN_URL, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loadingBar.dismiss();
                try
                {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        JSONObject object=response.getJSONObject("data");
                      String user_id=object.getString("user_id").toString();
                      String user_name=object.getString("user_name").toString();
                      String user_mobile=object.getString("user_mobile").toString();
                      String user_email=object.getString("user_email").toString();
                      String user_pincode=object.getString("user_pincode").toString();
                      String user_address=object.getString("user_address").toString();
                      //Toast.makeText(LoginActivity.this,""+response.getString("data").toString(),Toast.LENGTH_SHORT).show();
                      session_management.createLoginSession(user_id,user_email,user_name,user_mobile,user_pincode,user_address);
                      Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      startActivity(intent);
                      finish();

                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,""+response.getString("error").toString(),Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(LoginActivity.this,""+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadingBar.dismiss();
                String msg=module.VolleyError(error);
                if(!(msg.equals("") || msg.isEmpty()))
                {
                    Toast.makeText(LoginActivity.this,""+msg.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        });

        AppController.getInstance().addToRequestQueue(jsonRequest,json_tag);
    }
}
