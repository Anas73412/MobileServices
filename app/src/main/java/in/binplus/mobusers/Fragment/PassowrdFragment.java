package in.binplus.mobusers.Fragment;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;

import in.binplus.mobusers.AppController;
import in.binplus.mobusers.HomeActivity;
import in.binplus.mobusers.Module.Module;
import in.binplus.mobusers.R;
import in.binplus.mobusers.util.ConnectivityReceiver;
import in.binplus.mobusers.util.CustomVolleyJsonRequest;
import in.binplus.mobusers.util.Session_management;

import static in.binplus.mobusers.Config.BaseURL.CHANGE_PASSWORD;
import static in.binplus.mobusers.Config.Constants.KEY_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassowrdFragment extends Fragment implements View.OnClickListener{

    TextInputEditText edt_con_pass,edt_new_pass,edt_old_pass;
    Button btn_update;
    Dialog loadingBar;
    Session_management session_management;
    Module module;

    public PassowrdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_passowrd, container, false);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView(R.layout.progressbar);
        loadingBar.setCanceledOnTouchOutside(false);
        ((HomeActivity)getActivity()).setTitle(getActivity().getResources().getString(R.string.title_password));
        session_management=new Session_management(getActivity());
        module=new Module(getActivity());
        btn_update=(Button)view.findViewById(R.id.btn_update);
        edt_con_pass=(TextInputEditText)view.findViewById(R.id.edt_con_pass);
        edt_new_pass=(TextInputEditText)view.findViewById(R.id.edt_new_pass);
        edt_old_pass=(TextInputEditText)view.findViewById(R.id.edt_old_pass);

        btn_update.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();
        if(id == R.id.btn_update)
        {

            attemptUpdatePassword();
        }
    }

    private void attemptUpdatePassword() {
        String old_password=edt_old_pass.getText().toString();
        String new_password=edt_new_pass.getText().toString();
        String con_password=edt_con_pass.getText().toString();

        if(old_password.isEmpty() || old_password.equals(""))
        {
            edt_old_pass.setError(getActivity().getResources().getString(R.string.old_pass_required));
            edt_old_pass.requestFocus();
        }
        else if(new_password.isEmpty() || new_password.equals(""))
        {
            edt_new_pass.setError(getActivity().getResources().getString(R.string.new_pass_required));
            edt_new_pass.requestFocus();
        }
        else if(con_password.isEmpty() || con_password.equals(""))
        {
            edt_con_pass.setError(getActivity().getResources().getString(R.string.con_pass_required));
            edt_con_pass.requestFocus();
        }
        else {
            if(new_password.equals(con_password))
            {
                if(ConnectivityReceiver.isConnected())
                {
                    updatePassword(old_password,new_password);
                }
                else
                {
                    ((HomeActivity)getActivity()).onNetworkConnectionChanged(false);
                }

            }
            else
            {
                Toast.makeText(getActivity(),""+getActivity().getResources().getString(R.string.password_matched),Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void updatePassword(String old_password, String new_password) {
        loadingBar.show();
        String id=session_management.getUserDetails().get(KEY_ID);

        String json_tag="json_change_pass";
        HashMap<String,String> map=new HashMap<>();
        map.put("user_id",id);
        map.put("current_password",old_password);
        map.put("new_password",new_password);


        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, CHANGE_PASSWORD, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
         loadingBar.dismiss();
                try {
                    try
                    {
                        boolean resp=response.getBoolean("responce");
                        if(resp)
                        {

                            Toast.makeText(getActivity(),""+response.getString("message").toString(),Toast.LENGTH_SHORT).show();
                            HomeFragment homeFragment=new HomeFragment();
                            FragmentManager manager=getActivity().getSupportFragmentManager();
                            manager.beginTransaction().replace(R.id.content_panel,homeFragment).addToBackStack(null).commit();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),""+response.getString("error").toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        Toast.makeText(getActivity(),""+ex.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getActivity(),""+response.toString(),Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex)
                {
                    Toast.makeText(getActivity(),""+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadingBar.dismiss();
                String msg=module.VolleyError(error);
                if(!(msg.equals("") || msg.isEmpty()))
                {
                    Toast.makeText(getActivity(),""+msg.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);
    }
}
