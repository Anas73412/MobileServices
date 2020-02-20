package in.binplus.mobusers.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import in.binplus.mobusers.Model.BarcodeDetailsModel;
import in.binplus.mobusers.R;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 22,January,2020
 */
public class BarcodeDetailsAdapter extends RecyclerView.Adapter<BarcodeDetailsAdapter.ViewHolder> {
    Activity activity;
    ArrayList<BarcodeDetailsModel> list;

    public BarcodeDetailsAdapter(Activity activity, ArrayList<BarcodeDetailsModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.row_barcode_details,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BarcodeDetailsModel model=list.get(position);
        holder.tv_sale_code.setText(model.getAssigned_to());
        holder.tv_activation_date.setText(model.getActivation_date());
        holder.tv_expiry.setText(model.getEnd_date());
        holder.tv_imei.setText(model.getImei_no());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sale_code,tv_imei,tv_activation_date,tv_expiry;
        public ViewHolder(@NonNull View v) {
            super(v);

            tv_sale_code=(TextView)v.findViewById(R.id.tv_sale_code);
            tv_activation_date=(TextView)v.findViewById(R.id.tv_activation_date);
            tv_expiry=(TextView)v.findViewById(R.id.tv_expiry);
            tv_imei=(TextView)v.findViewById(R.id.tv_imei);


        }
    }
}
