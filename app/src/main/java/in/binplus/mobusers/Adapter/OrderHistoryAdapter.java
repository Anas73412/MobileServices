package in.binplus.mobusers.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.binplus.mobusers.Model.OrderHistoryModel;
import in.binplus.mobusers.R;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 22,January,2020
 */
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    Activity activity;
    ArrayList<OrderHistoryModel> list;

    public OrderHistoryAdapter(Activity activity, ArrayList<OrderHistoryModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.row_order_history_layout,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderHistoryModel model=list.get(position);
        holder.tv_order_id.setText(activity.getResources().getString(R.string.txt_order_id)+" : #"+model.getOrder_id());
        holder.tv_order_date.setText(activity.getResources().getString(R.string.txt_order_date)+":\n"+model.getOrder_at());
        holder.tv_brand.setText(activity.getResources().getString(R.string.txt_brand_name)+" : "+model.getBrand());
        holder.tv_model.setText(activity.getResources().getString(R.string.txt_model_name)+" : "+model.getModel());
        holder.tv_imei_no.setText(activity.getResources().getString(R.string.txt_imei_no)+":\n"+model.getImei_no());

        String status=model.getStatus().toString();
        if(status.equals("0"))
        {
            holder.btn_status.setBackgroundColor(activity.getResources().getColor(R.color.green));
            holder.tv_status.setText(activity.getResources().getString(R.string.st_pending));
        }
        else if (status.equals("1"))
        {
            holder.btn_status.setBackgroundColor(activity.getResources().getColor(R.color.orange));
            holder.tv_status.setText(activity.getResources().getString(R.string.st_confirm));

        }
        else if (status.equals("2"))
        {
            holder.btn_status.setBackgroundColor(activity.getResources().getColor(R.color.cancel_pick));
            holder.tv_status.setText(activity.getResources().getString(R.string.st_pick));

        }else if (status.equals("3"))
        {
            holder.btn_status.setBackgroundColor(activity.getResources().getColor(R.color.color_submit));
            holder.tv_status.setText(activity.getResources().getString(R.string.st_submit));

        }else if (status.equals("4") || status.equals("6"))
        {
            holder.btn_status.setBackgroundColor(activity.getResources().getColor(R.color.color_cancel));
            holder.tv_status.setText(activity.getResources().getString(R.string.st_cancel));

        }
        else
        {
            holder.btn_status.setBackgroundColor(activity.getResources().getColor(R.color.color_delivered));
            holder.tv_status.setText(activity.getResources().getString(R.string.st_delivered));

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_id,tv_order_date,tv_brand,tv_model,tv_imei_no,tv_status;
        RelativeLayout btn_status;
        public ViewHolder(@NonNull View v) {
            super(v);

            tv_order_id=(TextView)v.findViewById(R.id.tv_order_id);
            tv_order_date=(TextView)v.findViewById(R.id.tv_order_date);
            tv_brand=(TextView)v.findViewById(R.id.tv_brand);
            tv_model=(TextView)v.findViewById(R.id.tv_model);
            tv_imei_no=(TextView)v.findViewById(R.id.tv_imei_no);
            tv_status=(TextView)v.findViewById(R.id.tv_status);
            btn_status=(RelativeLayout) v.findViewById(R.id.btn_status);
        }
    }
}
