package com.firstapp.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    Spinner statenames;
    EditText edtname, edtmail, edtmobile;
    RadioGroup radioGroup;
    RadioButton radioButton;

    String nameStr, mailStr, mobileStr, genderStr, stateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtname = findViewById(R.id.edtname);
        edtmail = findViewById(R.id.edtmail);
        edtmobile = findViewById(R.id.edtmobile);
        radioGroup = findViewById(R.id.rg);
        statenames = findViewById(R.id.state);


        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


    }

    public void submit(View view) {
        progressDialog.show();
        progressDialog.setMessage("Verifying Mobile Number-----");

        nameStr = edtname.getText().toString();
        mailStr = edtmail.getText().toString();
        mobileStr = edtmobile.getText().toString();
        radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        genderStr = radioButton.getText().toString();
        stateStr = statenames.getSelectedItem().toString();

        DocumentReference documentReference = firebaseFirestore.collection("Hostel").document("" + mobileStr);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                progressDialog.dismiss();
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();

                } else {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Toast.makeText(MainActivity.this, "Mobile Number Already Existed", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        AddData(nameStr, mailStr, mobileStr, genderStr, stateStr);
                    }


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void AddData(String nameStr, String mailStr, String mobilsStr, String genderStr, String stateStr) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", nameStr);
        map.put("Mail", mailStr);
        map.put("Mobile", mobilsStr);
        map.put("Gender", genderStr);
        map.put("State", stateStr);

        firebaseFirestore.collection("Hostel").document("" + mobilsStr).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Data Add Successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Failed to load the data", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void Next(View view) {
        startActivity(new Intent(MainActivity.this, ReadActivity.class));

    }

}
