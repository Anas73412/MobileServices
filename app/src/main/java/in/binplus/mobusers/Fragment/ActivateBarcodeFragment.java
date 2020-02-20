package in.binplus.mobusers.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.binplus.mobusers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivateBarcodeFragment extends Fragment {


    public ActivateBarcodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_activate_barcode, container, false);
        return view;
    }

}
