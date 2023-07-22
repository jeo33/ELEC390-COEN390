package com.example.a390project.HomeFragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.a390project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private ImageView imageView;
    private StorageReference storageRef;
    private String stringValue;
    private FirebaseStorage storage;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private TextView emailView,account,phoneNumber;

    private DatabaseReference mDatabaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = getActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        stringValue = sharedPreferences.getString("userId", "Default Value if not found");

        // Initialize FirebaseStorage instance
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child(stringValue); // Change "images" to your desired folder name

        imageView = rootView.findViewById(R.id.imageView);
        progressBar = rootView.findViewById(R.id.progressBar2);
        loadProfileImage();

        Button chooseButton = rootView.findViewById(R.id.chooseButton);
        Button uploadButton = rootView.findViewById(R.id.uploadButton);
        chooseButton.setOnClickListener(view -> openImagePicker());
        uploadButton.setOnClickListener(view -> uploadImage());

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(stringValue);

        // Add a ValueEventListener to retrieve data
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called when data is changed in the database.
                // dataSnapshot contains the data at the specified database reference.

                // You can now access and process the retrieved data.
                String name = dataSnapshot.child("username").getValue(String.class);
                String email = dataSnapshot.child("Email").getValue(String.class);
                String phonenum = dataSnapshot.child("phone").getValue(String.class);
                emailView=rootView.findViewById(R.id.emailTextView);
                account=rootView.findViewById(R.id.accountTextView);
                phoneNumber=rootView.findViewById(R.id.phoneTextView);
                emailView.setText(email);
                account.setText(name);
                phoneNumber.setText(phonenum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // This method is called when the database operation is canceled.
                // Handle errors here (if any).
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });


        return rootView;
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void loadProfileImage() {
        // Replace "root/admin/profile.jpg" with the correct path to your image in Firebase Storage
        stringValue = sharedPreferences.getString("userId", "Default Value if not found");

        String imagePath = stringValue+"/profile.jpg";

        // Get a reference to the Firebase Storage location of the profile image
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference().child(imagePath);

        // Download the image data as a byte array
        final long MAX_SIZE = 1024 * 1024; // Maximum size of the image in bytes (adjust as needed)
        profileImageRef.getBytes(MAX_SIZE).addOnSuccessListener(bytes -> {
            // Successfully downloaded the image data as a byte array
            // Create a Bitmap from the byte array
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            // Set the Bitmap to the ImageView
            imageView.setImageBitmap(bitmap);
        }).addOnFailureListener(exception -> {
            // Handle any errors that occurred during image download
            Toast.makeText(requireContext(), "Failed to load profile image: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadImage() {
        if (filePath != null) {
            // Use a fixed filename for all uploads
            String newFilename = "profile.jpg";

            // Create a reference to the file in Firebase Storage with the fixed filename
            StorageReference imageRef = storageRef.child(newFilename);

            UploadTask uploadTask = imageRef.putFile(filePath);

            // Add an OnProgressListener to show real-time progress in the ProgressBar
            uploadTask.addOnProgressListener(taskSnapshot -> {
                long bytesTransferred = taskSnapshot.getBytesTransferred();
                long totalBytes = taskSnapshot.getTotalByteCount();
                int progress = (int) ((100 * bytesTransferred) / totalBytes);
                progressBar.setProgress(progress);
            });

            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Upload successful
                    Toast.makeText(requireContext(), "Upload successful!", Toast.LENGTH_SHORT).show();

                    // After a successful upload, update the ImageView with the uploaded image
                    loadProfileImage();
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(requireContext(), "Upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(requireContext(), "Please select an image to upload", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                // Set the selected image to the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}