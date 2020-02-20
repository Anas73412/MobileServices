package in.binplus.mobusers.Fragment;


import android.app.Dialog;
import android.app.DownloadManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.binplus.mobusers.AppController;
import in.binplus.mobusers.Config.BaseURL;
import in.binplus.mobusers.HomeActivity;
import in.binplus.mobusers.Model.BarcodeDetailsModel;
import in.binplus.mobusers.Module.Module;
import in.binplus.mobusers.R;
import in.binplus.mobusers.util.ConnectivityReceiver;
import in.binplus.mobusers.util.CustomVolleyJsonRequest;
import in.binplus.mobusers.util.Session_management;

import static in.binplus.mobusers.Config.Constants.KEY_ID;
import static in.binplus.mobusers.Config.Constants.KEY_MOBILE;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements View.OnClickListener{

    ArrayList<BarcodeDetailsModel> arrayList;
    EditText edt_instructions,edt_problems,edt_model,edt_brand,edt_address,edt_city,edt_pincode,edt_name;
    TextView tv_imei;
    Bundle bundle;
    Dialog loadingbar;
    Session_management session_management;
    Module module;
    Button btn_order;
    String user_id,mobile,barcode,imei_no;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_order, container, false);
        loadingbar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingbar.setContentView(R.layout.progressbar);
        loadingbar.setCanceledOnTouchOutside(false);

            arrayList=HomeFragment.modelList;
        barcode=arrayList.get(0).getBardcode().toString();
        imei_no=arrayList.get(0).getImei_no().toString();
        session_management=new Session_management(getActivity());
        module=new Module(getActivity());
        user_id=session_management.getUserDetails().get(KEY_ID);
        mobile=session_management.getUserDetails().get(KEY_MOBILE);
        edt_instructions=(EditText)view.findViewById(R.id.edt_instructions);
        edt_problems=(EditText)view.findViewById(R.id.edt_problems);
        edt_model=(EditText)view.findViewById(R.id.edt_model);
        edt_brand=(EditText)view.findViewById(R.id.edt_brand);
        edt_address=(EditText)view.findViewById(R.id.edt_address);
        edt_city=(EditText)view.findViewById(R.id.edt_city);
        edt_pincode=(EditText)view.findViewById(R.id.edt_pincode);
        edt_name=(EditText)view.findViewById(R.id.edt_name);
        tv_imei=(TextView) view.findViewById(R.id.tv_imei);
        btn_order=(Button) view.findViewById(R.id.btn_order);

   tv_imei.setText(arrayList.get(0).getImei_no().toString());
        //tv_imei.setOnClickListener(this);
        btn_order.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id == R.id.btn_order)
        {
            attemptOrder();
        }
    }

    private void attemptOrder() {

        String brand_name=edt_brand.getText().toString();
        String model_name=edt_model.getText().toString();
        String problems=edt_problems.getText().toString();
        String instructions=edt_instructions.getText().toString();
        String pincode=edt_pincode.getText().toString();
        String city=edt_city.getText().toString();
        String address=edt_address.getText().toString();
        String name=edt_name.getText().toString();

        if(brand_name.equals("") || brand_name.isEmpty())
        {
            edt_brand.setError(getActivity().getResources().getString(R.string.brand_required));
            edt_brand.requestFocus();
        }else if(model_name.equals("") || model_name.isEmpty())
        {
            edt_model.setError(getActivity().getResources().getString(R.string.model_required));
            edt_model.requestFocus();
        }else if(problems.equals("") || problems.isEmpty())
        {
            edt_problems.setError(getActivity().getResources().getString(R.string.problems_required));
            edt_problems.requestFocus();
        }else if(name.equals("") || name.isEmpty())
        {
            edt_name.setError(getActivity().getResources().getString(R.string.name_required));
            edt_name.requestFocus();
        }
        else if(pincode.equals("") || pincode.isEmpty())
        {
            edt_pincode.setError(getActivity().getResources().getString(R.string.picode_required));
            edt_pincode.requestFocus();
        }else if(city.equals("") || city.isEmpty())
        {
            edt_city.setError(getActivity().getResources().getString(R.string.city_required));
            edt_city.requestFocus();
        }else if(address.equals("") || address.isEmpty())
        {
            edt_address.setError(getActivity().getResources().getString(R.string.address_required));
            edt_address.requestFocus();
        }else if(pincode.length()!=6)
        {
            edt_pincode.setError(getActivity().getResources().getString(R.string.invalid_pincode));
            edt_pincode.requestFocus();
        }
        else
        {
            if(ConnectivityReceiver.isConnected())
            {
                makeOrderRequest(user_id,mobile,barcode,imei_no,brand_name,model_name,problems,instructions,pincode,city,address,name);
            }
            else
            {
                ((HomeActivity)getActivity()).onNetworkConnectionChanged(false);
            }

        }
    }

    private void makeOrderRequest(String user_id, String mobile, String barcode, String imei_no, String brand_name,
                                  String model_name, String problems, String instructions, String pincode, String city, String address, String name) {

        loadingbar.show();
        String json_tag="json_order_tag";
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("mobile",mobile);
        params.put("barcode",barcode);
        params.put("imei_no",imei_no);
        params.put("brand",brand_name);
        params.put("model",model_name);
        params.put("problem",problems);
        params.put("instruction",instructions);
        params.put("pincode",pincode);
        params.put("city",city);
        params.put("address",address);
        params.put("name",name);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.SET_ORDER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loadingbar.dismiss();
                try
                {
                    Log.d("order",response.toString());
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        Toast.makeText(getActivity(),"Order Placed",Toast.LENGTH_SHORT).show();
                        String msg=response.getString("data");
                        Bundle bundle=new Bundle();
                        ThanksFragment fm=new ThanksFragment();
                        bundle.putString("msg",msg);
                        fm.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_panel, fm)
                                .addToBackStack(null).commit();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),""+response.getString("error"),Toast.LENGTH_SHORT).show();

                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(getActivity(),""+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingbar.dismiss();
                String msg=module.VolleyError(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);

    }
}
