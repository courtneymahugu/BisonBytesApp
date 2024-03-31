package com.example.net_app;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQrActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("build.gradle.kts");
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        final String documentId = result.getText();

        if (!TextUtils.isEmpty(documentId)) {
            databaseReference.child(documentId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String documentName = dataSnapshot.child("name").getValue(String.class);
                        String documentContent = dataSnapshot.child("content").getValue(String.class);
                        // Handle document details
                    } else {
                        // Document with the given ID not found
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        } else {
            // Document ID is empty or null
        }

        scannerView.resumeCameraPreview(this);
    }
}
