package com.example.edward.neweventmanagementsystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.edward.neweventmanagementsystem.Model.OrganizerChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class chat_room extends AppCompatActivity {
    //      txtFullName.setText(Common.currentUser.getName());
    Button btnSendMsg;
    MaterialEditText edittextmsg;
    private DatabaseReference myRef;
    private ListView mListView;
    int Avoid =0;
    private ListAdapter listAdapter;
    ProgressDialog mDialog;

    //  Intent discuss = getIntent();
    //   String name =  discuss.getStringExtra("name");
    //String name =  Discuss.getStringExtra("location");
    // String RoomName = name;

    String RoomName = "Organizer_Discussion_Room:"   +(DEventName.EName);

    //  Intent mapsActivity = getIntent();
    //String location=  mapsActivity.getStringExtra("location");
    ArrayList<OrganizerChatMessage> phoneNumbers = new ArrayList<>();

    //  FirebaseRecyclerAdapter<UserChatMessage> adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    DatabaseReference chat_info = database.getReference("ChatRoom(Organizer)").push();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ChatRoom(Organizer)");

    // Date currentTime = Calendar.getInstance().getTime();
    CharSequence currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        mDialog = new ProgressDialog(chat_room.this);
        edittextmsg = (MaterialEditText)findViewById(R.id.edittextmsg);
        mListView = (ListView) findViewById(R.id.listview);
        btnSendMsg =(Button)findViewById(R.id.btnSendMsg);

        final ArrayAdapter<OrganizerChatMessage> arrayAdapter = new ArrayAdapter<OrganizerChatMessage>(this,android.R.layout.simple_expandable_list_item_1,phoneNumbers);
        mListView.setAdapter(arrayAdapter);

        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        if(dataSnapshot.getValue()!=null)
                            collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    public void buttonSendClick(View v){


        mDialog.setMessage("Sending...");
        mDialog.show();
        currentTime = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", new java.util.Date());

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        OrganizerChatMessage organizerChatMessage = new OrganizerChatMessage(currentFirebaseUser.getDisplayName(),edittextmsg.getText().toString(),currentTime);
        chat_info.setValue(organizerChatMessage);
        chat_info = database.getReference(RoomName).push();//-----
        //  chat_info = database.getReference("ChatRoom(1)").push();//-----
        chat_info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(mDialog.isShowing())
                    mDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        edittextmsg.setText("");
    }
    private void collectPhoneNumbers(Map<String,Object> users) {

        phoneNumbers.clear();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            OrganizerChatMessage temp = new OrganizerChatMessage((String)singleUser.get("userName"),(String)singleUser.get("message"),(CharSequence)singleUser.get("time") );
            phoneNumbers.add(temp);
        }

        //  System.out.println(phoneNumbers.toString());
        // System.out.println(phoneNumbers.toString());
        //    Toast.makeText(chat_room1.this,phoneNumbers.toString(),LENGTH_SHORT).show();
        // listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, phoneNumbers);
        Collections.sort(phoneNumbers, new Comparator<OrganizerChatMessage>() {
            @Override
            public int compare(OrganizerChatMessage chat2, OrganizerChatMessage chat1)
            {
                return  chat2.compareTo(chat1);
            }
        });
    }

    /*
        public void moveNewActivity(){

            Intent MainMenuPage = new Intent(register.this,MainMenuPage.class);
            startActivity(MainMenuPage);
            finish();
            //onCreate();
        }

    */
    /**
     public boolean onKeyDown(int keyCode, KeyEvent event) {
     // TODO Auto-generated method stub

     if (keyCode == KeyEvent.KEYCODE_BACK) {
     new AlertDialog.Builder(chat_room.this)
     .setTitle("Back to Main Menu")
     .setMessage("Are you sure you want to quit?")
     //                    .setIcon(R.drawable.jmham)
     .setPositiveButton("sure",
     new DialogInterface.OnClickListener() {

    @Override
    public void onClick(DialogInterface dialog,
    int which) {
    Intent intent = new Intent(chat_room.this, MainActivity.class);
    startActivity(intent);
    }
    })
     .setNegativeButton("cancel",
     new DialogInterface.OnClickListener() {

    @Override
    public void onClick(DialogInterface dialog,
    int which) {
    // TODO Auto-generated method stub

    }
    }).show();
     }
     return true;
     } **/



}