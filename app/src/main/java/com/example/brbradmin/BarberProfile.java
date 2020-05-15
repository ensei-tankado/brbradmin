package com.example.brbradmin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class BarberProfile extends Activity {
    TextView tvFullName,tvUserName,tvPlace,tvMobile;
    String barberID;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barber_profile);
        tvFullName = (TextView) findViewById(R.id.barberFullName);
        tvUserName = (TextView) findViewById(R.id.barberUserName);
        tvPlace = (TextView) findViewById(R.id.barberPlace);
        tvMobile = (TextView) findViewById(R.id.barberMobile);
        Bundle bundle = getIntent().getExtras();
        barberID = bundle.getString("barberID");
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(barberID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvFullName.setText(dataSnapshot.child("fullname").getValue().toString());
                tvUserName.setText(dataSnapshot.child("username").getValue().toString());
                tvPlace.setText(dataSnapshot.child("place").getValue().toString());
                tvMobile.setText(dataSnapshot.child("mobile").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

    }
}
