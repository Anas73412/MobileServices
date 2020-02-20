package in.binplus.mobusers.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.binplus.mobusers.AppController;
import in.binplus.mobusers.Config.BaseURL;
import in.binplus.mobusers.CustomSlider;
import in.binplus.mobusers.HomeActivity;
import in.binplus.mobusers.MainActivity;
import in.binplus.mobusers.Model.BarcodeDetailsModel;
import in.binplus.mobusers.Module.Module;
import in.binplus.mobusers.R;
import in.binplus.mobusers.RenewActivity;
import in.binplus.mobusers.util.ConnectivityReceiver;
import in.binplus.mobusers.util.CustomVolleyJsonRequest;
import in.binplus.mobusers.util.Session_management;

import static in.binplus.mobusers.Config.Constants.KEY_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private static String TAG = HomeFragment.class.getSimpleName();
    CardView card_barcode;
 public static ArrayList<BarcodeDetailsModel> modelList;
    Dialog loadingbar,dialog;
    int version_code=0;
    String app_link="";
    Button btn_query;
    String user_id="";
    RelativeLayout rel_enquiry,rel_about,rel_terms,subscribeLay;
    SliderLayout home_img_slider;
    Session_management session_management;
    Module module;
    TextView dialog_btn_close;
    Button dialog_btn_activate,dialog_btn_renew;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        modelList=new ArrayList<>();
        loadingbar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingbar.setContentView(R.layout.progressbar);
        loadingbar.setCanceledOnTouchOutside(false);
        session_management=new Session_management(getActivity());
        module=new Module(getActivity());
        ((HomeActivity)getActivity()).setTitle(getActivity().getResources().getString(R.string.name));
        user_id=session_management.getUserDetails().get(KEY_ID);
//        card_request=(CardView)view.findViewById(R.id.card_request);
//        card_expired=(CardView)view.findViewById(R.id.card_expired);
        card_barcode=(CardView)view.findViewById(R.id.card_barcode);
        rel_terms=(RelativeLayout) view.findViewById(R.id.rel_terms);
        rel_about=(RelativeLayout) view.findViewById(R.id.rel_about);
        rel_enquiry=(RelativeLayout) view.findViewById(R.id.rel_enquiry);
        subscribeLay=(RelativeLayout) view.findViewById(R.id.subscribeLay);
        home_img_slider=(SliderLayout) view.findViewById(R.id.home_img_slider);
        btn_query=(Button) view.findViewById(R.id.btn_query);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure want to exit?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            ((HomeActivity) getActivity()).finish();
                            //getActivity().finishAffinity();


                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog dialog=builder.create();
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
    btn_query.setOnClickListener(this);
    rel_about.setOnClickListener(this);
    rel_enquiry.setOnClickListener(this);
    rel_terms.setOnClickListener(this);
    card_barcode.setOnClickListener(this);
    subscribeLay.setOnClickListener(this);

    if(ConnectivityReceiver.isConnected())
    {
        getAppSettingData();
    }
    else
    {
        ((HomeActivity)getActivity()).onNetworkConnectionChanged(false);
    }

        return view;
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();
        if(id == R.id.btn_query)
        {
            EnquiryFragment home_fragment=new EnquiryFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_panel, home_fragment)
            .addToBackStack(null).commit();

        }
        else if( id == R.id.rel_about)
        {

            AboutUsFragment home_fragment=new AboutUsFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_panel, home_fragment)
                    .addToBackStack(null).commit();

        }else if( id == R.id.rel_terms)
        {
           TermConditionFragment termConditionFragment=new TermConditionFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_panel, termConditionFragment)
                    .addToBackStack(null).commit();
        }else if( id == R.id.rel_enquiry)
        {
            EnquiryFragment termConditionFragment=new EnquiryFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_panel, termConditionFragment)
                    .addToBackStack(null).commit();

        }
        else if(id == R.id.card_barcode)
        {
            getBarcodeDetails(user_id,1);
        }
        else if(id == R.id.subscribeLay)
        {
            getBarcodeDetails(user_id,2);
        }
    }

    private void makeGetSliderRequest() {
        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_SLIDER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("slider_data", response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("slider_url", jsonObject.getString("slider_url"));
                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                            }
                            for (final HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("slider_url"));
                                home_img_slider.addSlider(textSliderView);
//                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
//                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//                                    @Override
//                                    public void onSliderClick(BaseSliderView slider) {
//                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                String msg=module.VolleyError(error);
                if(!(msg.equals("") || msg.isEmpty())) {
                    Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(req);
//
    }


    public void getAppSettingData()
    {
        loadingbar.show();
        String json_tag="json_app_tag";
        HashMap<String,String> map=new HashMap<>();

        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.GET_VERSTION_DATA, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingbar.dismiss();
                try
                {
                    boolean sts=response.getBoolean("responce");

                    if(sts)
                    {
                        JSONObject object=response.getJSONObject("data");
                        version_code=Integer.parseInt(object.getString("app_version"));
                        app_link=object.getString("data");

                        if(getUpdaterInfo())
                        {
                            makeGetSliderRequest();

                        }
                        else
                        {

                            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                            builder.setCancelable(false);
                            builder.setMessage("The new version of app is available please update to get access.");
                            builder.setPositiveButton("Update now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    String url = app_link;
                                    Intent in = new Intent(Intent.ACTION_VIEW);
                                    in.setData(Uri.parse(url));
                                    startActivity(in);
                                    getActivity().finish();
                                    //Toast.makeText(getActivity(),"updating",Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                    getActivity().finishAffinity();
                                }
                            });
                            AlertDialog dialog=builder.create();
                            dialog.show();
                        }


                    }
                    else
                    {
                        Toast.makeText(getActivity(),""+response.getString("error"),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(getActivity(),""+error.getMessage(),Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
                String msg=module.VolleyError(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(request,json_tag);
    }

    public boolean getUpdaterInfo()
    {
        boolean st=false;
        try
        {
            PackageInfo packageInfo=getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0);
            int ver_code=packageInfo.versionCode;
            if(ver_code == version_code)
            {
                st=true;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return st;
    }

    public void getBarcodeDetails(String user_id, final int flag)
    {
        loadingbar.show();
        String json_tag="json_details_tag";
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("user_id",user_id);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.GET_BARCODE_DATA, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               loadingbar.dismiss();
                try
                {
                    Log.d("barcode_data",response.toString());
                    boolean resp=response.getBoolean("responce");
                    if(!resp)
                    {
                        Toast.makeText(getActivity(),""+response.getString("error").toString(),Toast.LENGTH_SHORT).show();
                       createDialog();
                    }
                    else
                    {

                       JSONArray array=response.getJSONArray("data");
                       for(int i=0; i<array.length();i++)
                       {
                           JSONObject object=array.getJSONObject(i);
                           BarcodeDetailsModel model=new BarcodeDetailsModel();
                           model.setId(object.getString("id"));
                           model.setBardcode(object.getString("barcode"));
                           model.setStatus(object.getString("status"));
                           model.setAssigned_to(object.getString("assigned_to"));
                           model.setImei_no(object.getString("imei_no"));
                           model.setActivation_date(object.getString("activation_date"));
                           model.setEnd_date(object.getString("end_date"));

                           modelList.add(model);

                       }

                       if(flag == 1)
                       {
                           DetailsFragment fm=new DetailsFragment();
                           FragmentManager fragmentManager = getFragmentManager();
                           fragmentManager.beginTransaction().replace(R.id.content_panel, fm)
                                   .addToBackStack(null).commit();

                       }
                       else if(flag == 2)
                       {

                           OrderFragment fm=new OrderFragment();
                           FragmentManager fragmentManager = getFragmentManager();
                           fragmentManager.beginTransaction().replace(R.id.content_panel, fm)
                                   .addToBackStack(null).commit();
                       }
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

    public void createDialog()
    {
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activate_layout);

        dialog_btn_close=(TextView)dialog.findViewById(R.id.btn_close);
        dialog_btn_activate=(Button) dialog.findViewById(R.id.btn_activate);
        dialog_btn_renew=(Button) dialog.findViewById(R.id.btn_renew);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog_btn_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), MainActivity.class);
                intent.putExtra("status","pending");
                intent.putExtra("barcode","");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog_btn_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), RenewActivity.class);

                startActivity(intent);
                dialog.dismiss();
            }
        });

    }


}
