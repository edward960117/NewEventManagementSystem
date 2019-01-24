package com.example.edward.neweventmanagementsystem;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edward.neweventmanagementsystem.Model.EventInfo;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import static android.widget.Toast.LENGTH_SHORT;

public class CreateEvent extends AppCompatActivity {

    private static final String TAG = "activity_create_event";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private DatabaseReference mDatabaseReference, mDatabaseReference1;


    private Button mRegisterButton;
    EditText mEventNameText, mContactNumText, mRegisterEventId, mRegisterEventPrice, mRegisterEventCapacity;
    AutoCompleteTextView mEventLocationText;
    TextView mEventDate;
    RadioGroup mEventType;
    FirebaseStorage storage;

    StorageReference storageRef,imageRef;
    FirebaseDatabase database;
    Uri uriImage ;
    public static final int PICK_IMAGE = 1;
    ImageView mimageToUpload;
    private long backPressedTime = 0;
    GoogleSignInClient mGoogleSignInClient;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        FirebaseDatabase firebaseDatabase;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ListOfEvent"); //For Organizer use.
        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference().child("ListOfEvent1"); //For User site use, handle the .child problem

        mRegisterButton = (Button)findViewById(R.id.btnRegisterEvent);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        mDisplayDate = (TextView) findViewById(R.id.RegisterEventStartDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: date: mm/dd/yyyy: " + month + "/" + day + "/" + year);
//                SimpleDateFormat date = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.ENGLISH);

                String date = month +  "/"  + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        //insert data to database
        mRegisterEventId = (EditText) findViewById(R.id.RegisterEventId);
        mEventNameText = (EditText) findViewById(R.id.RegisterEventName);
        mContactNumText = (EditText) findViewById(R.id.RegisterContactNumber);
        mEventDate = (TextView) findViewById(R.id.RegisterEventStartDate);
        mEventType =  (RadioGroup) findViewById(R.id.RegisterEventRadiogroup);
        final AutoCompleteTextView mEventLocationText = findViewById(R.id.RegisterEventLocation);
        mimageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        mRegisterButton = (Button) findViewById(R.id.btnRegisterEvent);
        ImageView image = (ImageView) findViewById(R.id.image);
        mRegisterEventPrice = (EditText) findViewById(R.id.EventPrice);
        mRegisterEventCapacity = (EditText) findViewById(R.id.EventCapacity);

        mEventType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.type1)
                {
                    mRegisterEventPrice.setVisibility(View.VISIBLE);
                }
                else if(checkedId==R.id.type2)
                {
                    mRegisterEventPrice.setVisibility(View.GONE);
                    mRegisterEventPrice.setText("Free");
                }
            }
        });

//        mEventType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup mEventType, int checkedId) {
//
//                if(checkedId==R.id.type1)
//                {
//                    mRegisterEventPrice.setVisibility(View.VISIBLE);
//
//                }
//                else if(checkedId==R.id.type2)
//                {
//                    mRegisterEventPrice.setVisibility(View.INVISIBLE);
//                }
//            }
//

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        //Create a new ArrayAdapter with your context and the simple layout for the dropdown menu provided by Android
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        database.child("Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                    //Get the suggestion by childing the key of the string you want to get.
                    String name = suggestionSnapshot.child("name").getValue(String.class);
                    //Add the retrieved string to the list
                    autoComplete.add(name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView RegisterEventLocation= (AutoCompleteTextView)findViewById(R.id.RegisterEventLocation);
        RegisterEventLocation.setAdapter(autoComplete);

        mimageToUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(CreateEvent.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    selectPdf();
                }
                else {
                    ActivityCompat.requestPermissions(CreateEvent.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(CreateEvent.this);


                int selectedId = mEventType.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton)findViewById(selectedId);

                final String id = mRegisterEventId.getText().toString().trim();
                final String name = mEventNameText.getText().toString().trim();
                final String contact = mContactNumText.getText().toString().trim();
                final String date = mEventDate.getText().toString().trim();
                final String type = radioButton.getText().toString().trim();
                final String location = mEventLocationText.getText().toString().trim();
                final String price = mRegisterEventPrice.getText().toString().trim();
                final String EventCapacity = mRegisterEventCapacity.getText().toString().trim();

                if(uriImage == null){
                    Toast.makeText(getApplicationContext(),"Please select an image.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(id)) {
                    mRegisterEventId.setError("Enter Event ID!");
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    mEventNameText.setError("Enter Event Name!");
                    return;
                }
                if(isValidPhone(contact)){
                    //Toast.makeText(getApplicationContext(),"Phone number is valid",Toast.LENGTH_SHORT).show();
                }else {
                    mContactNumText.setError("Phone number is not valid");
                    Toast.makeText(getApplicationContext(),"Phone number is not valid",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(location)) {
                    mEventLocationText.setError("Enter Location!");
                    return;
                }
                if (TextUtils.isEmpty(date)) {
                    mEventDate.setError("Please select event date!");
                    return;
                }
                if (TextUtils.isEmpty(type)) {
                    radioButton.setError("Please select type of event type!");
                    return;
                }
                if (TextUtils.isEmpty(price)) {
                    mRegisterEventPrice.setError("Enter the price!");
                    return;
                }

                if (TextUtils.isEmpty(EventCapacity)){
                    mRegisterEventCapacity.setError("Enter the capacity");
                    return;
                }


                mDialog.setMessage("Please waiting...");
                mDialog.show();

                final String fileName = System.currentTimeMillis()+"";

                final StorageReference storageReference = storage.getReference();

                System.out.println(uriImage);
                storageReference.child("profileImageUrl").child(fileName).putFile(uriImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        final String url = task.getResult().toString();
                                        System.out.println("Important" + url);

                                        //not yetsolve bypass the uuid and check the event id
                                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

                                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                                DatabaseReference uidRef = rootRef.child("ListOfEvent1"); //.child(uid);

                                                if(dataSnapshot.child(currentFirebaseUser.getUid()).child(mRegisterEventId.getText().toString()).exists()) {
                                                    mDialog.dismiss();
                                                    storageReference.child("profileImageUrl").child(fileName).delete();
                                                    Toast.makeText(CreateEvent.this, "ID already exists!", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    mDialog.dismiss();

//                                                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                                                    //Toast.makeText(CreateEvent.this, "" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();

                                                    EventInfo eventInfo = new EventInfo(mRegisterEventId.getText().toString().trim(),url,
                                                            mEventNameText.getText().toString().trim(), mContactNumText.getText().toString().trim(),
                                                            mEventDate.getText().toString().trim(), radioButton.getText().toString().trim(),
                                                            mEventLocationText.getText().toString().trim(), fileName, mRegisterEventPrice.getText().toString().trim(),
                                                            mRegisterEventCapacity.getText().toString().trim());

                                                    mDatabaseReference.child(currentFirebaseUser.getUid()).child(mRegisterEventId.getText().toString()).setValue(eventInfo);
                                                    mDatabaseReference1.child(mRegisterEventId.getText().toString()).setValue(eventInfo);
                                                    Toast.makeText(getApplicationContext(),"New event created successfully!",LENGTH_SHORT).show();
                                                    Intent ManageEventMenu =  new Intent(CreateEvent.this, com.example.edward.neweventmanagementsystem.ManageEventMenu.class);
                                                    startActivity(ManageEventMenu);
                                                }
                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"File not Successfully Uploaded",LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Alert!")
                .setMessage("All the information will erase if ok button is press on.")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        setResult(RESULT_OK, new Intent().putExtra("EXIT", true));
                        finish();
                    }

                }).create().show();
    }


    public boolean isValidPhone(CharSequence phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() < 10 || phone.length() > 11)
            {
                check = false;
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check=false;
        }
        return check;
    }

    private void selectPdf() {

        Intent photoPickerIntent  = new Intent();
        photoPickerIntent .setType("image/*");
        photoPickerIntent .setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(photoPickerIntent ,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 86 && resultCode == RESULT_OK && data != null){

            final Uri imageUri = data.getData();
            uriImage = imageUri;
            mimageToUpload.setImageURI(uriImage);
        }
        else {
            Toast.makeText(getApplicationContext(),"Please select file", LENGTH_SHORT).show();
        }
    }


}
