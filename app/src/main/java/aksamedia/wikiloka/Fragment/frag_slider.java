package aksamedia.wikiloka.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import aksamedia.wikiloka.R;


public class frag_slider extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ImageView img;
    private TextView id;


    public frag_slider() {
    }
    public static frag_slider newInstance(String param1, String param2) {
        frag_slider fragment = new frag_slider();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_slider, container, false);
        img=(ImageView)view.findViewById(R.id.img_slider);
        id=(TextView) view.findViewById(R.id.id_slider);
        Picasso.with(getActivity()).load(getArguments().getString("image")).fit().centerCrop().into(img);
        id.setText(getArguments().getString("id"));
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
