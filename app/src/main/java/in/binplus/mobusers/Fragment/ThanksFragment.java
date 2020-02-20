package in.binplus.mobusers.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.binplus.mobusers.HomeActivity;
import in.binplus.mobusers.OrderHistory;
import in.binplus.mobusers.R;


public class ThanksFragment extends Fragment implements View.OnClickListener{

    TextView tv_thank_info;
    Bundle bundle;
    RelativeLayout btn_thank_home,btn_track_order;
    String msg="";
    public ThanksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_thanks, container, false);
        tv_thank_info=(TextView)view.findViewById(R.id.tv_thank_info);
        btn_thank_home=(RelativeLayout) view.findViewById(R.id.btn_thank_home);
        btn_track_order=(RelativeLayout) view.findViewById(R.id.btn_track_order);
        ((HomeActivity) getActivity()).setTitle(getResources().getString(R.string.thank_you));
        bundle=getArguments();
        if(bundle!=null)
        {
            msg=bundle.getString("msg");
        }
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    HomeFragment fm = new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_panel, fm)
                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });


        tv_thank_info.setText(Html.fromHtml(msg));
        btn_thank_home.setOnClickListener(this);
        btn_track_order.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        int id=v.getId();
        if(id == R.id.btn_thank_home)
        {
            HomeFragment fm=new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_panel, fm)
                    .addToBackStack(null).commit();
        }
        else if(id == R.id.btn_track_order)
        {
            Intent intent=new Intent(getActivity(), OrderHistory.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
