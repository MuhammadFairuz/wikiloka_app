package aksamedia.wikiloka.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;

import java.util.ArrayList;

import aksamedia.wikiloka.Class.class_adapterinbox;
import aksamedia.wikiloka.Class.class_inbox;
import aksamedia.wikiloka.Class.class_prosessql;
import aksamedia.wikiloka.R;

/**
 * Created by Dinar on 11/17/2016.
 */

public class frag_inbox extends Fragment implements View.OnClickListener {

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private ArrayList<aksamedia.wikiloka.Class.class_inbox> class_inbox;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    TextView tvEmptyRV;
    String title_inbox, name_inbox, message_inbox, picture_inbox,key_inbox;
    long time_inbox;
    SimpleArcDialog[] mDialog = new SimpleArcDialog[1];
    class_adapterinbox class_adapterlistinbox;

    public frag_inbox(){}

    public static frag_inbox newInstance(){
        frag_inbox fragment = new frag_inbox();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        isiData();
    }

    private void isiData() {
        class_inbox = new ArrayList<class_inbox>();
        mRecyclerView.setAdapter(null);
        class_inbox.clear();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        class_adapterlistinbox = new class_adapterinbox(class_inbox);
        mRecyclerView.setAdapter(class_adapterlistinbox);

        mDialog[0] = new SimpleArcDialog(getActivity());
        mDialog[0].setConfiguration(new ArcConfiguration(getActivity()));
        mDialog[0].show();

        root.child("users").child(new class_prosessql(getActivity()).baca_member()[0]).child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()==0){
                    mDialog[0].dismiss();
                    tvEmptyRV.setText("Tidak ada pesan");
                    tvEmptyRV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        root.child("users").child(new class_prosessql(getActivity()).baca_member()[0]).child("chats").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                root.child("chats").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            tvEmptyRV.setVisibility(View.GONE);
                            Log.e("Firebase c", "ambil data");
                            Log.e("Firebase key", dataSnapshot.getKey());
                            title_inbox = dataSnapshot.child("title").getValue().toString();
                            Log.e("Firebase title", title_inbox);
                            message_inbox = dataSnapshot.child("last_message").getValue().toString();
                            name_inbox = dataSnapshot.child("last_sender").getValue().toString();
                            picture_inbox = dataSnapshot.child("picture").getValue().toString();
                            time_inbox = Long.parseLong(dataSnapshot.child("last_time").getValue().toString());
                            key_inbox = dataSnapshot.getKey();
                            class_inbox.add(new class_inbox(title_inbox, picture_inbox, message_inbox, name_inbox, time_inbox, key_inbox));
                            class_adapterlistinbox.notifyDataSetChanged();
                            mDialog[0].dismiss();
                        }
                        else {
                            mDialog[0].dismiss();
                            tvEmptyRV.setText("Tidak ada pesan");
                            tvEmptyRV.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        /*root.child("users").child(new class_prosessql(getActivity()).baca_member()[0]).child("chats").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Log.e("Firebase chats", "ada");
                    final long[] pendingLoadCount = { dataSnapshot.getChildrenCount() };
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        Log.e("Firebase c", "ada");
                        DataSnapshot ds = iterator.next();
                        String key = ds.getKey();
                        key_inbox = key;
                        //c_inbox.clear();
                        root.child("chats").child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.e("Firebase c", "ambil data");
                                title_inbox = dataSnapshot.child("title").getValue().toString();
                                Log.e("Firebase title", title_inbox);
                                message_inbox = dataSnapshot.child("last_message").getValue().toString();
                                name_inbox = dataSnapshot.child("last_sender").getValue().toString();
                                picture_inbox = dataSnapshot.child("picture").getValue().toString();
                                time_inbox = Long.parseLong(dataSnapshot.child("last_time").getValue().toString());
                                //c_inbox.add(new class_inbox(title_inbox, picture_inbox, message_inbox, name_inbox, time_inbox));
                                class_inbox.add(new class_inbox(title_inbox, picture_inbox, message_inbox, name_inbox, time_inbox, key_inbox));
                                pendingLoadCount[0]--;
                                if (pendingLoadCount[0] == 0) {
                                    Log.e("Firebase adapter", "dimasukin");
                                    mRecyclerView.setHasFixedSize(true);
                                    mLayoutManager = new LinearLayoutManager(getActivity());
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                                    mRecyclerView.setLayoutManager(llm);
                                    class_adapterinbox class_adapterlistinbox = new class_adapterinbox(class_inbox);
                                    class_adapterlistinbox.notifyDataSetChanged();
                                    mRecyclerView.setAdapter(class_adapterlistinbox);
                                    mDialog[0].dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    //Collections.reverse(c_inbox);
                    //adapter.notifyDataSetChanged();
                } else {
                    mDialog[0].dismiss();
                    tvEmptyRV.setText("Tidak ada pesan");
                    tvEmptyRV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //listInbox();
        //inisialisasi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_inbox, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv);
        tvEmptyRV = (TextView) v.findViewById(R.id.tvEmptyRV);
        return v;
    }

    /*private class_adapterinbox adapter;
    private ArrayList<class_inbox> c_inbox;








    private void listInbox() {
        mRecyclerView.setAdapter(null);

    }



    void inisialisasi() {
        adapter = new class_adapterinbox(c_inbox);
        mRecyclerView.setAdapter(adapter);
    }*/



    @Override
    public void onClick(View v) {

    }
}
