package in.binplus.mobusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.binplus.mobusers.Adapter.OrderHistoryAdapter;
import in.binplus.mobusers.Config.BaseURL;
import in.binplus.mobusers.Model.OrderHistoryModel;
import in.binplus.mobusers.Module.Module;
import in.binplus.mobusers.networkconnectivity.NoInternetConnection;
import in.binplus.mobusers.util.ConnectivityReceiver;
import in.binplus.mobusers.util.CustomVolleyJsonRequest;
import in.binplus.mobusers.util.RecyclerTouchListener;
import in.binplus.mobusers.util.Session_management;

import static in.binplus.mobusers.Config.Constants.KEY_ID;

public class OrderHistory extends AppCompatActivity {

    Dialog loadingbar;
    RecyclerView rec_order;
    Session_management session_management;
    String user_id="";
    Module module;
    ArrayList<OrderHistoryModel> list;
    OrderHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        loadingbar=new Dialog(OrderHistory.this,android.R.style.Theme_Translucent_NoTitleBar);
        loadingbar.setContentView(R.layout.progressbar);
        loadingbar.setCanceledOnTouchOutside(false);
        list=new ArrayList<>();
        session_management=new Session_management(OrderHistory.this);
        module=new Module(OrderHistory.this);
        user_id=session_management.getUserDetails().get(KEY_ID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.my_order));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderHistory.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();

            }
        });

        rec_order=(RecyclerView)findViewById(R.id.rec_order);
        if(ConnectivityReceiver.isConnected())
        {
            getAllOrders(user_id);
        }
        else
        {
         Intent intent=new Intent(OrderHistory.this, NoInternetConnection.class);
         startActivity(intent);
        }


   rec_order.addOnItemTouchListener(new RecyclerTouchListener(OrderHistory.this, rec_order, new RecyclerTouchListener.OnItemClickListener() {
       @Override
       public void onItemClick(View view, int position) {

           OrderHistoryModel model=list.get(position);
           Intent intent=new Intent(OrderHistory.this,OrderDetailsActivity.class);
           intent.putExtra("order_id",model.getOrder_id());
           intent.putExtra("order_date",model.getOrder_at());
           intent.putExtra("barcode",model.getBarcode());
           intent.putExtra("address",model.getAddress());
           intent.putExtra("city",model.getCity());
           intent.putExtra("pincode",model.getPincode());
           intent.putExtra("imei_no",model.getImei_no());
           intent.putExtra("brand",model.getBrand());
           intent.putExtra("model",model.getModel());
           intent.putExtra("problem",model.getProblem());
           intent.putExtra("instructions",model.getInstructions());
           intent.putExtra("status",model.getStatus());
           intent.putExtra("name",model.getName());
           startActivity(intent);
           finish();



       }

       @Override
       public void onLongItemClick(View view, int position) {

       }
   }));
    }

    private void getAllOrders(String user_id) {
        loadingbar.show();
        String json_tag="json_order_history_tag";
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);

        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.GET_ORDERS, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loadingbar.dismiss();
                try
                {
                    Log.d("history_respose",response.toString());
                    JSONArray array=response.getJSONArray("data");
                    for(int i=0; i<array.length();i++)
                    {
                        JSONObject object=array.getJSONObject(i);
                        OrderHistoryModel model=new OrderHistoryModel();
                        model.setOrder_id(object.getString("order_id"));
                        model.setOrder_at(object.getString("order_at"));
                        model.setBarcode(object.getString("barcode"));
                        model.setModel(object.getString("model"));
                        model.setBrand(object.getString("brand"));
                        model.setAddress(object.getString("address"));
                        model.setPincode(object.getString("pincode"));
                        model.setCity(object.getString("city"));
                        model.setProblem(object.getString("problem"));
                        model.setInstructions(object.getString("instructions"));
                        model.setStatus(object.getString("status"));
                        model.setImei_no(object.getString("imei_no"));
                        model.setName(object.getString("name"));
                        list.add(model);
                    }
                    adapter=new OrderHistoryAdapter(OrderHistory.this,list);
                    rec_order.setHasFixedSize(false);
                    rec_order.setLayoutManager(new LinearLayoutManager(OrderHistory.this));
                    rec_order.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                   // Toast.makeText(OrderHistory.this,""+response.toString(),Toast.LENGTH_SHORT).show();

                }catch (Exception ex)
                {
                    ex.printStackTrace();
                    String er=ex.getMessage();
                    if(er.equals("No value for data"))
                    {
                        Toast.makeText(OrderHistory.this,"No Orders Available",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(OrderHistory.this,""+ex.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingbar.dismiss();
                String msg=module.VolleyError(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText(OrderHistory.this,""+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(request,json_tag);
    }


}
