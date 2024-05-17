package com.example.eventexplorer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class profileRegisterActivity extends AppCompatActivity {
    ImageView uploadimage;
    EditText name, name2, address1, address2, landmark, town, state, pinCode;
    Button registerButton;
    String imageURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_register);

        uploadimage = findViewById(R.id.uploadImage);
        name = findViewById(R.id.editTextName);
        name2 = findViewById(R.id.editTextName2);
        address1 = findViewById(R.id.editTextAddressLine1);
        address2 = findViewById(R.id.editTextAddressLine2);
        landmark = findViewById(R.id.editTextLandmark);
        town = findViewById(R.id.editTextTownCity);
        state = findViewById(R.id.editTextState);
        pinCode = findViewById(R.id.editTextPincode);
        registerButton = findViewById(R.id.register_button);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadimage.setImageURI(uri);
                        } else {
                            Toast.makeText(profileRegisterActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    public void saveData() {
        if (uri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String enteredName = name.getText().toString();

        checkNameUniqueness(enteredName, new NameCheckCallback() {
            @Override
            public void onNameChecked(boolean isUnique) {
                if (isUnique) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("User Profile").child(uri.getLastPathSegment());

                    AlertDialog.Builder builder = new AlertDialog.Builder(profileRegisterActivity.this);
                    builder.setCancelable(false);
                    builder.setView(R.layout.progress_layout);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri urlImage = task.getResult();
                                        imageURL = urlImage.toString();
                                        dialog.dismiss();
                                        uploadData();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(profileRegisterActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(profileRegisterActivity.this, "This name is already taken. Please choose another one.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkNameUniqueness(String name, NameCheckCallback callback) {
        FirebaseDatabase.getInstance().getReference("User Data")
                .orderByChild("name")
                .equalTo(name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        callback.onNameChecked(!snapshot.exists());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
    }

    public void uploadData() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String Name = name.getText().toString();
        String Name2 = name2.getText().toString();
        String Email = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // get the current user's email
        String Address1 = address1.getText().toString();
        String Address2 = address2.getText().toString();
        String LandMark = landmark.getText().toString();
        String Town = town.getText().toString();
        String State = state.getText().toString();
        String PinCode = pinCode.getText().toString();

        dataClass DataClass = new dataClass(Name, Name2, Email, Address1, Address2, LandMark, Town, State, PinCode, imageURL);

        FirebaseDatabase.getInstance().getReference("User Data").child(userID)
                .setValue(DataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(profileRegisterActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(profileRegisterActivity.this, mainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(profileRegisterActivity.this, "Failed to upload data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    interface NameCheckCallback {
        void onNameChecked(boolean isUnique);
    }
}
