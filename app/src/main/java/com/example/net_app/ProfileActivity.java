package com.example.net_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private String userId = "uniqueUserId"; // This should dynamically set based on the logged-in user
    private ImageView qrCodeImageView;
    private TextView userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile); // Make sure you have a layout file for this activity

        qrCodeImageView = findViewById(R.id.qrCodeImageView); // Assuming you have an ImageView in your layout

        checkAndDisplayUserInfo();
    }

    private void checkAndDisplayUserInfo() {
        db.collection("users").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Check if the name field exists and is not null
                    String userName = document.contains("name") ? document.getString("name") : "Name not set";
                    userNameTextView.setText(userName);

                    // Check for the QR code URL
                    if (document.contains("qrCodeUrl") && document.getString("qrCodeUrl") != null) {
                        String qrCodeUrl = document.getString("qrCodeUrl");
                        loadQrCode(qrCodeUrl);
                    } else {
                        // QR code does not exist, generate a new one
                        try {
                            generateAndUploadQRCode(userId);
                        } catch (WriterException e) {
                            Log.e("ProfileActivity", "Error generating QR code", e);
                        }
                    }
                } else {
                    Log.d("ProfileActivity", "Document does not exist.");
                    // Handle the case where the document does not exist
                }
            } else {
                Log.e("ProfileActivity", "Failed to fetch user document.", task.getException());
            }
        });
    }

    private void loadQrCode(String qrCodeUrl) {
        StorageReference qrCodeRef = storage.getReferenceFromUrl(qrCodeUrl);
        final long ONE_MEGABYTE = 1024 * 1024;
        qrCodeRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            qrCodeImageView.setImageBitmap(bmp);
        }).addOnFailureListener(exception -> {
            Log.e("ProfileActivity", "Error loading QR code", exception);
        });
    }

    private void generateAndUploadQRCode(String userId) throws WriterException {
        Bitmap qrCodeBitmap = generateQRCodeImage(userId, 200, 200);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference qrCodeRef = storage.getReference().child("qrCodes/" + userId + ".png");

        qrCodeRef.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> qrCodeRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Update Firestore with the new QR code URL
                    db.collection("users").document(userId).update("qrCodeUrl", uri.toString());
                    // Display the newly generated QR code
                    qrCodeImageView.setImageBitmap(qrCodeBitmap);
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileActivity.this, "Failed to upload QR code", Toast.LENGTH_SHORT).show();
                    Log.e("ProfileActivity", "Upload failed", e);
                });
    }

    public static Bitmap generateQRCodeImage(String text, int width, int height) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE);
            }
        }
        return bitmap;
    }
}
