package com.example.edward.neweventmanagementsystem;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edward.neweventmanagementsystem.Model.EventInfo;
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

import java.util.Calendar;
import java.util.regex.Pattern;

import static android.widget.Toast.LENGTH_SHORT;

public class CreateEvent extends AppCompatActivity {

    private static final String TAG = "activity_create_event";
    private Uri filePath;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatabaseReference mDatabaseReference;
    private Button mRegisterButton;
    EditText mEventNameText, mContactNumText, mEventLocationText, mRegisterEventId;;
    TextView mEventDate;
    RadioGroup mEventType;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    Uri uriImage ;//= Uri.parse("com.example.edward.eventmanagementsystem.ManageEvent/"+ R.drawable.ic_launcher_background);
    public static final int PICK_IMAGE = 1;
    ImageView mimageToUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        FirebaseDatabase firebaseDatabase;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ListOfEvent"); //.push();

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
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: date: mm/dd/yyyy: " + month + "/" + day + "/" + year);

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
        mEventLocationText = (EditText) findViewById(R.id.RegisterEventLocation);
        mimageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        mRegisterButton = (Button) findViewById(R.id.btnRegisterEvent);

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
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                int selectedId = mEventType.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton)findViewById(selectedId);

                final String id = mRegisterEventId.getText().toString().trim();
                final String name = mEventNameText.getText().toString().trim();
                final String contact = mContactNumText.getText().toString().trim();
                final String date = mEventDate.getText().toString().trim();
                final String type = radioButton.getText().toString().trim();
                final String location = mEventLocationText.getText().toString().trim();

                if (TextUtils.isEmpty(id)) {
                    mRegisterEventId.setError("Enter Event ID!");
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    mEventNameText.setError("Enter Event Name!");
                    return;
                }


                if (TextUtils.isEmpty(location)) {
                    mEventLocationText.setError("Enter Location!");
                    return;
                }


                if (TextUtils.isEmpty(type)) {
                    radioButton.setError("Please select type of event type!");
                    return;
                }
                if(isValidPhone(contact)){

                    Toast.makeText(getApplicationContext(),"Phone number is valid",Toast.LENGTH_SHORT).show();
                }else {
                    mContactNumText.setError("Phone number is not valid");
                    Toast.makeText(getApplicationContext(),"Phone number is not valid",Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(date)) {
                    mEventDate.setError("Please select event date!");
                    return;
                }

                if(uriImage == null){
                    Toast.makeText(getApplicationContext(),"Please select an image.",Toast.LENGTH_SHORT).show();
                    return;
                }

//                Map userInfo = new HashMap();
//                userInfo.put("mEventNameText", name);
//                userInfo.put("mContactNumText", contact);
//                userInfo.put("mEventDate", date);
//                userInfo.put("radioButton", type);
//                userInfo.put("mEventLocationText", location);
//
//                mDatabaseReference.updateChildren(userInfo);
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
                                        mDatabaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.child(mRegisterEventId.getText().toString()).exists()) {
                                                    mDialog.dismiss();
                                                    storageReference.child("profileImageUrl").child(fileName).delete();
                                                    Toast.makeText(CreateEvent.this, "ID already exists!", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    mDialog.dismiss();
                                                    EventInfo eventInfo = new EventInfo(mRegisterEventId.getText().toString().trim(),url,mEventNameText.getText().toString().trim(), mContactNumText.getText().toString().trim(), mEventDate.getText().toString().trim(), radioButton.getText().toString().trim(), mEventLocationText.getText().toString().trim());
                                                    System.out.println(eventInfo);
                                                    mDatabaseReference.child(mRegisterEventId.getText().toString()).setValue(eventInfo);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                storageReference.child("profileImageUrl").child(fileName).delete();
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




                Toast.makeText(getApplicationContext(),"New event created successfully!",LENGTH_SHORT).show();
                Intent ManageEventMenu =  new Intent(CreateEvent.this, com.example.edward.neweventmanagementsystem.ManageEventMenu.class);
                startActivity(ManageEventMenu);
            }
        });
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
//        startActivityForResult(photoPickerIntent , Selected);
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
