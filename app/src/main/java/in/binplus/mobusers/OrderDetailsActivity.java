package in.binplus.mobusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Module module;
    TextView tv_imei_no,tv_brand_name,tv_model_name,tv_pincode,tv_city,tv_address,tv_order_id,tv_order_date,tv_problem,tv_instructions,tv_status,tv_name;
    Button btn_cancel_order;
    EditText edt_feedback;
    Dialog loadingbar;
    CardView card_feedback;
    String order_id,order_date,barcode,address,city,pincode,imei_no,brand,model,problem,instructions,status,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        loadingbar=new Dialog(OrderDetailsActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
        loadingbar.setContentView(R.layout.progressbar);
        loadingbar.setCanceledOnTouchOutside(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.txt_order_details));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailsActivity.this, OrderHistory.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();

            }
        });

        module=new Module(OrderDetailsActivity.this);
        order_id=getIntent().getStringExtra("order_id");
        order_date=getIntent().getStringExtra("order_date");
        barcode=getIntent().getStringExtra("barcode");
        address=getIntent().getStringExtra("address");
        city=getIntent().getStringExtra("city");
        pincode=getIntent().getStringExtra("pincode");
        imei_no=getIntent().getStringExtra("imei_no");
        brand=getIntent().getStringExtra("brand");
        model=getIntent().getStringExtra("model");
        problem=getIntent().getStringExtra("problem");
        instructions=getIntent().getStringExtra("instructions");
        status=getIntent().getStringExtra("status");
        name=getIntent().getStringExtra("name");
        tv_imei_no=(TextView)findViewById(R.id.tv_imei_no);
        card_feedback=(CardView) findViewById(R.id.card_feedback);
        tv_brand_name=(TextView)findViewById(R.id.tv_brand_name);
        tv_model_name=(TextView)findViewById(R.id.tv_model_name);
        tv_pincode=(TextView)findViewById(R.id.tv_pincode);
        tv_city=(TextView)findViewById(R.id.tv_city);
        tv_address=(TextView)findViewById(R.id.tv_address);
        tv_order_id=(TextView)findViewById(R.id.tv_order_id);
        tv_order_date=(TextView)findViewById(R.id.tv_order_date);
        tv_problem=(TextView)findViewById(R.id.tv_problem);
        tv_instructions=(TextView)findViewById(R.id.tv_instructions);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_status=(TextView)findViewById(R.id.tv_status);
        edt_feedback=(EditText) findViewById(R.id.edt_feedback);
        btn_cancel_order=(Button) findViewById(R.id.btn_cancel_order);

        int st=Integer.parseInt(status);
        if(st>0)
        {
            card_feedback.setVisibility(View.GONE);
            btn_cancel_order.setVisibility(View.GONE);
        }
        else
        {
            card_feedback.setVisibility(View.VISIBLE);
            btn_cancel_order.setVisibility(View.VISIBLE);
        }

        tv_imei_no.setText(imei_no);
        tv_brand_name.setText(brand);
        tv_model_name.setText(model);
        tv_pincode.setText(pincode);
        tv_city.setText(city);
        tv_address.setText(address);
        tv_order_id.setText(order_id);
        tv_order_date.setText(order_date);
        tv_problem.setText(problem);
        tv_instructions.setText(instructions);
        tv_name.setText(name);
        if(status.equals("0"))
        {

           tv_status.setText(getResources().getString(R.string.st_pending));
        }
        else if (status.equals("1"))
        {
            tv_status.setText(getResources().getString(R.string.st_confirm));

        }
        else if (status.equals("2"))
        {

            tv_status.setText(getResources().getString(R.string.st_pick));

        }else if (status.equals("3"))
        {
          tv_status.setText(getResources().getString(R.string.st_submit));

        }else if (status.equals("4"))
        {

            tv_status.setText(getResources().getString(R.string.st_cancel));

        }
        else
        {
         tv_status.setText(getResources().getString(R.string.st_cancel));

        }

        btn_cancel_order.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();

        if(id == R.id.btn_cancel_order)
        {
            attemptcancel();
        }
    }

    private void attemptcancel() {

        String feedback=edt_feedback.getText().toString();
        if(feedback.equals("") || feedback.isEmpty())
        {
            edt_feedback.setError(getResources().getString(R.string.feedback_required));
            edt_feedback.requestFocus();
        }
        else if(feedback.length()<20)
        {
            edt_feedback.setError(getResources().getString(R.string.feedback_minimum));
            edt_feedback.requestFocus();

        }
        else
        {
            if(ConnectivityReceiver.isConnected())
            {
                makeAddCancelRequest(order_id,feedback);
            }
            else
            {
                Intent intent=new Intent(OrderDetailsActivity.this, NoInternetConnection.class);
                startActivity(intent);

            }

        }

    }

    private void makeAddCancelRequest(String order_id, String feedback) {

        loadingbar.show();
        String json_tag="json_feedback_tag";
        HashMap<String,String> params=new HashMap<>();
        params.put("order_id",order_id);
        params.put("feedback",feedback);

        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.CANCEL_ORDER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loadingbar.dismiss();
                try
                {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        Toast.makeText(OrderDetailsActivity.this,""+response.getString("message"),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(OrderDetailsActivity.this,OrderHistory.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(OrderDetailsActivity.this,""+response.getString("error"),Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(OrderDetailsActivity.this,""+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingbar.dismiss();
                String msg=module.VolleyError(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText(OrderDetailsActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(request,json_tag);


    }
}
