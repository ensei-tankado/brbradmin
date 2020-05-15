package com.example.brbradmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BarberList extends Activity {
    ArrayList<String> array;
    ArrayList<String> barberIDs;
    DatabaseReference mDatabase;
    String barberID;
    String username;
    ListView listView;
    Button btnApprove;
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barber_list);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        btnApprove = (Button) findViewById(R.id.pendingApproval);
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarberList.this, BarberApprovalList.class);
                startActivity(intent);
            }
        });
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BarberList.this, BarberProfile.class);
                intent.putExtra("barberID",barberIDs.get(position));
                startActivity(intent);
            }
        });

        array = new ArrayList<>();
        barberIDs = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.orderByChild("role").equalTo("barber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    barberID = ds.getKey();
                    if(Boolean.parseBoolean(ds.child("status").getValue().toString()))
                    {
                        barberIDs.add(barberID);
                        username = ds.child("username").getValue().toString();
                        array.add(username);
                    }

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(BarberList.this,android.R.layout.simple_list_item_1,array);
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}
