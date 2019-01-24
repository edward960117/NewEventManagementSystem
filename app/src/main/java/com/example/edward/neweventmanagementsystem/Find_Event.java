package com.example.edward.neweventmanagementsystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.edward.neweventmanagementsystem.Model.EventInfo;
import com.example.edward.neweventmanagementsystem.ViewHolder.MenuViewHolder;
import com.example.edward.neweventmanagementsystem.dialog.EventDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Find_Event extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton SearchButtton;
    private EditText SearchInputText;

    private RecyclerView SearchResultList;
    DatabaseReference mDatabaseReference;
    FirebaseRecyclerAdapter<EventInfo, MenuViewHolder> adapter;
    RecyclerView recycle_menu;
    DatabaseReference eventInfo;
    private View viewTable;
    private Dialog dialogTable;
    private TextView RegisterEventId,RegisterEventName,EventCapacity,RegisterEventLocation,
            RegisterContactNumber, RegisterEventStartDate,EventPrice ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__event);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ListOfEvent").child(currentFirebaseUser.getUid());

        SearchResultList = (RecyclerView) findViewById(R.id.search_result_list);
        SearchResultList.setHasFixedSize(true);
        SearchResultList.setLayoutManager(new LinearLayoutManager(this));

        SearchButtton = (ImageButton) findViewById(R.id.search_button1);
        SearchInputText = (EditText) findViewById(R.id.search_box_input);

        SearchButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchBoxInput = SearchInputText.getText().toString();
                if (adapter != null)
                    adapter.stopListening();

                SearchEvent(searchBoxInput);
                if (adapter != null)
                    adapter.startListening();
            }
        });

    }
    public void SearchEvent(String searchBoxInput){


        Query query = mDatabaseReference.orderByChild("registerEventName").startAt(searchBoxInput).endAt(searchBoxInput+"\uf8ff");
        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<EventInfo>().setQuery(query, EventInfo.class).build();
        adapter = new FirebaseRecyclerAdapter<EventInfo, MenuViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override


            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_list,viewGroup,false);
                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull EventInfo model) {

                holder.txtRegisterEventId.setText(model.getRegisterEventId());
                holder.txtRegisterEventStartDate.setText(model.getRegisterEventStartDate());
                holder.txtRegisterEventName.setText(model.getRegisterEventName());
//                holder.txtContact_number.setText(model.getRegisterContactNumber());
                holder.txtRegisterEventRadiogroup.setText(model.getRegisterEventRadiogroup());
                holder.txtRegisterEventLocation.setText(model.getRegisterEventLocation());
                holder.fileName.setText(model.getFileName());

                Glide.with(getBaseContext()).load(model.getImageToUpload()
                ).into(holder.imageView);
                System.out.println(model.getRegisterEventName());
                System.out.println(model.getImageToUpload());
            }
        };
        SearchResultList.setAdapter(adapter);
    }
}
