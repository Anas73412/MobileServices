package in.binplus.mobusers.Fragment;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.binplus.mobusers.Adapter.BarcodeDetailsAdapter;
import in.binplus.mobusers.HomeActivity;
import in.binplus.mobusers.Model.BarcodeDetailsModel;
import in.binplus.mobusers.Module.Module;
import in.binplus.mobusers.R;
import in.binplus.mobusers.util.ConnectivityReceiver;
import in.binplus.mobusers.util.Session_management;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    ArrayList<BarcodeDetailsModel> arrayList;
    Bundle bundle;
    BarcodeDetailsAdapter adapter;
    RecyclerView rec_barcodes;
    Dialog dialog;
    Session_management session_management;
    Module module;
    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_details, container, false);
        ((HomeActivity)getActivity()).setTitle(getActivity().getResources().getString(R.string.title_details));

            arrayList=HomeFragment.modelList;

        rec_barcodes=(RecyclerView)view.findViewById(R.id.rec_barcodes);
        if(ConnectivityReceiver.isConnected())
        {
            setBarcodeData();

        }
        else{
            ((HomeActivity)getActivity()).onNetworkConnectionChanged(false);
        }
        return view;

    }

    public void setBarcodeData()
    {
        adapter=new BarcodeDetailsAdapter(getActivity(),arrayList);
        rec_barcodes.setHasFixedSize(false);
        rec_barcodes.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec_barcodes.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
