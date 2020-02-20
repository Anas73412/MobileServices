package in.binplus.mobusers.Fragment;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import static in.binplus.mobusers.Config.BaseURL.ADD_ENQUIRY;
import static in.binplus.mobusers.Config.Constants.KEY_EMAIL;
import static in.binplus.mobusers.Config.Constants.KEY_ID;
import static in.binplus.mobusers.Config.Constants.KEY_MOBILE;
import static in.binplus.mobusers.Config.Constants.KEY_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnquiryFragment extends Fragment implements View.OnClickListener{

    TextInputEditText edt_name,edt_mobile,edt_email;
    EditText edt_msg;
    String user_id,email="";
    Button btn_send;
    Dialog loadingBar;
    Session_management session_management;
    Module module;
    public EnquiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_enquiry, container, false);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView(R.layout.progressbar);
        loadingBar.setCanceledOnTouchOutside(false);
        ((HomeActivity)getActivity()).setTitle(getActivity().getResources().getString(R.string.title_enquiry));
        edt_name=(TextInputEditText)view.findViewById(R.id.edt_name);
        edt_mobile=(TextInputEditText)view.findViewById(R.id.edt_mobile);
        edt_email=(TextInputEditText)view.findViewById(R.id.edt_email);
        session_management=new Session_management(getActivity());
        module=new Module(getActivity());
        edt_msg=(EditText)view.findViewById(R.id.edt_msg);
        btn_send=(Button)view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        initData();
        return view;
    }

    private void initData() {
        user_id=session_management.getUserDetails().get(KEY_ID);
        email=session_management.getUserDetails().get(KEY_EMAIL);
        edt_name.setText(session_management.getUserDetails().get(KEY_NAME));
        edt_mobile.setText(session_management.getUserDetails().get(KEY_MOBILE));
        edt_email.setText(session_management.getUserDetails().get(KEY_EMAIL));
        edt_email.setEnabled(false);
        edt_mobile.setEnabled(false);
        edt_name.setEnabled(false);
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();
        if(id == R.id.btn_send)
        {
      sendQuery();
        }
    }

    private void sendQuery() {
        String message=edt_msg.getText().toString();
        if(message.isEmpty() || message.equals(""))
        {
            edt_msg.setError(getActivity().getResources().getString(R.string.msg_required));
            edt_msg.requestFocus();
        }
        else if(message.length()<=10)
        {
            edt_msg.setError(getActivity().getResources().getString(R.string.msg_length));
            edt_msg.requestFocus();

        }
        else
        {
            if(ConnectivityReceiver.isConnected())
            {
                sendEnquiryData(user_id,email,message);
            }
            else
            {
                ((HomeActivity)getActivity()).onNetworkConnectionChanged(false);
            }

        }

    }

    private void sendEnquiryData(String user_id, String email, String message) {
        loadingBar.show();
        String json_tag="json_enquiry_tag";
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("email",email);
        params.put("message",message);
        params.put("type","user");

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, ADD_ENQUIRY, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loadingBar.dismiss();
                try
                {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        Toast.makeText(getActivity(),""+response.getString("message"),Toast.LENGTH_SHORT).show();
                        HomeFragment home_fragment=new HomeFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_panel, home_fragment)
                                .addToBackStack(null).commit();

                    }
                    else {
                        Toast.makeText(getActivity(),""+response.getString("error"),Toast.LENGTH_SHORT).show();
                    }
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
