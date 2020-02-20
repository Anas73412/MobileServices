package in.binplus.mobusers.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import in.binplus.mobusers.AppController;
import in.binplus.mobusers.Config.BaseURL;
import in.binplus.mobusers.HomeActivity;
import in.binplus.mobusers.Model.PagesModel;
import in.binplus.mobusers.Module.Module;
import in.binplus.mobusers.R;
import in.binplus.mobusers.util.ConnectivityReceiver;
import in.binplus.mobusers.util.CustomVolleyJsonArrayRequest;


public class PrivacyFragment extends Fragment {

    TextView tv_info;
    Dialog loadingBar;

    Module module;
    public PrivacyFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_privacy, container, false);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView(R.layout.progressbar);
        loadingBar.setCanceledOnTouchOutside(false);
        module=new Module(getActivity());
        tv_info=(TextView)view.findViewById(R.id.tv_info);
        ((HomeActivity)getActivity()).setTitle(getResources().getString(R.string.title_privacy));

        if(ConnectivityReceiver.isConnected())
        {
            getContactUsData();

        }
        else
        {
            ((HomeActivity)getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    private void getContactUsData() {
        loadingBar.show();
        String jsonn_tag="json_contact_tag";
        HashMap<String,String> map=new HashMap<>();
        CustomVolleyJsonArrayRequest arrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.POST, BaseURL.GET_SETTINGS, map, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadingBar.dismiss();
                try
                {
                    JSONObject object=response.getJSONObject(1);
                    PagesModel model=new PagesModel();
                    model.setId(object.getString("id"));
                    model.setTitle(object.getString("title"));
                    model.setContent(object.getString("content"));

                    tv_info.setText(model.getContent());


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
        AppController.getInstance().addToRequestQueue(arrayRequest,jsonn_tag);


    }
}
