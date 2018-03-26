package aksamedia.wikiloka.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import aksamedia.wikiloka.R;
import aksamedia.wikiloka.Class.class_adaptermessage;
import aksamedia.wikiloka.Class.class_message;
import aksamedia.wikiloka.Class.class_prosessql;

/**
 * Created by Dinar on 11/19/2016.
 */

public class activity_detail_inbox extends AppCompatActivity{
    RecyclerView rv;
    EditText etMessage;
    LinearLayout llSend;
    private ArrayList<aksamedia.wikiloka.Class.class_message> class_message;
    LinearLayoutManager mLayoutManager;
    class_adaptermessage class_adapterlistmessage;
    SimpleArcDialog[] mDialog = new SimpleArcDialog[1];
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    String messages, usernames, names;
    long sendtimes;
    String key_inbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_inbox);

        key_inbox = getIntent().getStringExtra("key_inbox");

        rv = (RecyclerView) findViewById(R.id.rv);
        etMessage = (EditText) findViewById(R.id.etMessage);
        llSend = (LinearLayout) findViewById(R.id.llSend);
        llSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp_key = root.child("messages").child(getIntent().getStringExtra("key_inbox")).push().getKey();
                DatabaseReference msg_root = root.child("messages").child(getIntent().getStringExtra("key_inbox")).child(temp_key);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("username", new class_prosessql(getApplication()).baca_profil()[1]);
                map.put("name", new class_prosessql(getApplication()).baca_profil()[4]);
                map.put("message", etMessage.getText().toString());
                map.put("timestamp", new Date().getTime());
                msg_root.updateChildren(map);

                root.child("chats").child(getIntent().getStringExtra("key_inbox")).child("last_message").setValue(etMessage.getText().toString());
                root.child("chats").child(getIntent().getStringExtra("key_inbox")).child("last_sender").setValue(new class_prosessql(getApplication()).baca_profil()[4]);
                root.child("chats").child(getIntent().getStringExtra("key_inbox")).child("last_time").setValue(new Date().getTime());
                root.child("chats").child(getIntent().getStringExtra("key_inbox")).child("title").setValue(getIntent().getStringExtra("title_inbox"));
                root.child("chats").child(getIntent().getStringExtra("key_inbox")).child("picture").setValue(getIntent().getStringExtra("picture_inbox"));
                etMessage.setText("");
            }
        });
        isiData();
    }

    private void isiData() {
        class_message = new ArrayList<class_message>();
        rv.setAdapter(null);
        class_message.clear();

        rv.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        class_adapterlistmessage = new class_adaptermessage(class_message);
        rv.setAdapter(class_adapterlistmessage);

        mDialog[0] = new SimpleArcDialog(this);
        mDialog[0].setConfiguration(new ArcConfiguration(this));
        mDialog[0].show();

        root.child("messages").child(getIntent().getStringExtra("key_inbox")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0){
                    mDialog[0].dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        root.child("messages").child(getIntent().getStringExtra("key_inbox")).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                names = (String) dataSnapshot.child("name").getValue();
                messages = (String) dataSnapshot.child("message").getValue();
                usernames = (String) dataSnapshot.child("username").getValue();
                sendtimes = Long.parseLong(dataSnapshot.child("timestamp").getValue().toString());
                class_message.add(new class_message(messages, names, usernames, sendtimes));
                class_adapterlistmessage.notifyDataSetChanged();
                mDialog[0].dismiss();
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

    }
}
