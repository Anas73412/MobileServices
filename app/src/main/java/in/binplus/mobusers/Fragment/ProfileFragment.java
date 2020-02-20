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

import static in.binplus.mobusers.Config.BaseURL.EDIT_PROFILE_URL;
import static in.binplus.mobusers.Config.Constants.KEY_ADDRESS;
import static in.binplus.mobusers.Config.Constants.KEY_EMAIL;
import static in.binplus.mobusers.Config.Constants.KEY_ID;
import static in.binplus.mobusers.Config.Constants.KEY_MOBILE;
import static in.binplus.mobusers.Config.Constants.KEY_NAME;

import static in.binplus.mobusers.Config.Constants.KEY_PINCODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    String user_id,user_name,user_mobile,user_email,user_picode,user_address="";
    TextInputEditText edt_address,edt_pincode,edt_email,edt_mobile,edt_name;
    Button btn_update;
    Dialog loadingBar;
    Session_management session_management;
    Module module;

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView(R.layout.progressbar);
        loadingBar.setCanceledOnTouchOutside(false);
        ((HomeActivity)getActivity()).setTitle(getActivity().getResources().getString(R.string.title_profile));
        session_management=new Session_management(getActivity());
        module=new Module(getActivity());
        btn_update=(Button)view.findViewById(R.id.btn_update);
        edt_address=(TextInputEditText)view.findViewById(R.id.edt_address);
        edt_pincode=(TextInputEditText)view.findViewById(R.id.edt_pincode);
        edt_email=(TextInputEditText)view.findViewById(R.id.edt_email);
        edt_mobile=(TextInputEditText)view.findViewById(R.id.edt_mobile);
        edt_name=(TextInputEditText)view.findViewById(R.id.edt_name);

        initUserData();
        btn_update.setOnClickListener(this);
        return view;
    }

    private void initUserData() {
        user_id=session_management.getUserDetails().get(KEY_ID);
        user_name=session_management.getUserDetails().get(KEY_NAME);
        user_mobile=session_management.getUserDetails().get(KEY_MOBILE);
        user_email=session_management.getUserDetails().get(KEY_EMAIL);
        user_picode=session_management.getUserDetails().get(KEY_PINCODE);
        user_address=session_management.getUserDetails().get(KEY_ADDRESS);

        edt_name.setText(user_name);
        edt_mobile.setText(user_mobile);
        edt_email.setText(user_email);
        edt_mobile.setEnabled(false);
        edt_email.setEnabled(false);
        edt_pincode.setText(user_picode);
        edt_address.setText(user_address);
    }


    @Override
    public void onClick(View v) {

        int id=v.getId();
        if(id == R.id.btn_update)
        {
            attemptUpdateProfile();
        }

    }

    private void attemptUpdateProfile() {

        if(edt_name.getText().toString().equals("") || edt_name.getText().toString().isEmpty())
        {
            edt_name.setError(getActivity().getResources().getString(R.string.name_required));
            edt_name.requestFocus();
        }
        else if(edt_mobile.getText().toString().equals("") || edt_mobile.getText().toString().isEmpty())
        {
            edt_mobile.setError(getActivity().getResources().getString(R.string.mobile_required));
            edt_mobile.requestFocus();
        }
       else if(edt_email.getText().toString().equals("") || edt_email.getText().toString().isEmpty())
        {
            edt_email.setError(getActivity().getResources().getString(R.string.email_required));
            edt_email.requestFocus();
        }
       else if(edt_pincode.getText().toString().equals("") || edt_pincode.getText().toString().isEmpty())
        {
            edt_pincode.setError(getActivity().getResources().getString(R.string.picode_required));
            edt_pincode.requestFocus();
        } else if(edt_address.getText().toString().equals("") || edt_address.getText().toString().isEmpty())
        {
            edt_address.setError(getActivity().getResources().getString(R.string.address_required));
            edt_address.requestFocus();
        }
       else {

           if(ConnectivityReceiver.isConnected())
           {
               String name=edt_name.getText().toString();
               String mobile=edt_mobile.getText().toString();
               String email=edt_email.getText().toString();
               String pincode=edt_pincode.getText().toString();
               String address=edt_address.getText().toString();

                updateProfile(user_id,name,mobile,email,pincode,address);
           }
           else
           {
               ((HomeActivity)getActivity()).onNetworkConnectionChanged(false);
           }
        }

    }

    private void updateProfile(String user_id, String name, String mobile, String email, String pincode, String address) {

        loadingBar.show();
        String json_tag="json_update_profile_tag";
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("name",name);
        params.put("mobile",mobile);
        params.put("email",email);
        params.put("pincode",pincode);
        params.put("address",address);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, EDIT_PROFILE_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loadingBar.dismiss();
                try
                {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        JSONObject object=response.getJSONObject("data");
                        session_management.updateData(object.getString("name"),object.getString("pincode"),object.getString("address"));
                        Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_SHORT).show();
                        HomeFragment homeFragment=new HomeFragment();
                        FragmentManager manager=getActivity().getSupportFragmentManager();
                        manager.beginTransaction().replace(R.id.content_panel,homeFragment).addToBackStack(null).commit();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),""+response.getString("error").toString(),Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getActivity(),""+response.toString(),Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
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
