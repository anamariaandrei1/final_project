package com.example.travelj2.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.travelj2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private StorageReference storageReference;

    private CircleImageView avatar;
    private TextView username;
    private TextView email;
    private TextView phone;
    private FloatingActionButton fabEdit;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private DatabaseReference databaseRef;

    private static final int CAMERA_OP = 100;
    private static final int STORAGE_OP = 200;
    private static final int IMAGE_GALLERY_PICK = 300;
    private static final int IMAGE_CAMERA_PICK = 400;

    String[] cameraPermissions;
    String[] storagePermissions;
    Uri imageUri;
    String storagePath = "Users_Images/";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        avatar = root.findViewById(R.id.profile_image);
        username = root.findViewById(R.id.usernameTextView);
        email = root.findViewById(R.id.emailTextView);
        phone = root.findViewById(R.id.phoneTextView);
        fabEdit = root.findViewById(R.id.fabEdit);

        Query query = databaseRef.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = "" + ds.child("name").getValue();
                    String emailAddress = "" + ds.child("email").getValue();
                    String phoneNumber = "" + ds.child("phone").getValue();
                    String imagePath = "" + ds.child("image").getValue();
                    username.setText(name);
                    email.setText(emailAddress);
                    phone.setText(phoneNumber);
                    if (!imagePath.equals("")) {
                        Picasso.get().load(imagePath).into(avatar);
                    } else {
                        Picasso.get().load(R.drawable.ic_add_photo).into(avatar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditProfileDialog();

            }
        });

        return root;
    }

    private boolean checkStoragePermissions() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermissions, STORAGE_OP);
    }

    private boolean checkCameraPermissions() {
        boolean camera = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean storage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return camera && storage;
    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermissions, CAMERA_OP);
    }

    private void showEditProfileDialog() {
        String[] options = {"Edit profile picture", "Edit Username", "Edit Phone"};
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    builder.setMessage("Updating profile picture");
                    showImageDialog();
                } else if (which == 1) {
                    builder.setMessage("Updating username");
                    showEditNamePhoneDialog("name");
                } else if (which == 2) {
                    builder.setMessage("Updating phone");
                    showEditNamePhoneDialog("phone");
                }
            }
        });
        builder.create().show();
    }

    private void showEditNamePhoneDialog(final String key) {
        AlertDialog.Builder builderTwo = new AlertDialog.Builder(getActivity());
        builderTwo.setTitle("Update" + key);
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter new" + key);
        linearLayout.addView(editText);
        builderTwo.setView(linearLayout);
        builderTwo.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(key, value);
                    databaseRef.child(user.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), key + "updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error occurred at updating the " + key, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Please enter a " + key, Toast.LENGTH_SHORT).show();
                }

            }
        });
        builderTwo.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builderTwo.create().show();
    }

    private void showImageDialog() {
        String[] options = {"Camera", "Gallery"};
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermissions()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else {
                    if (!checkStoragePermissions()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_OP: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && writeStorage) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "Please enable camera and storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_OP: {
                if (grantResults.length > 0) {
                    boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorage) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Please enable storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp description");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_CAMERA_PICK);


    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_GALLERY_PICK);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_GALLERY_PICK) {
                imageUri = data.getData();
                uploadProfilePic(imageUri);
            }

            if (requestCode == IMAGE_CAMERA_PICK) {
                uploadProfilePic(imageUri);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfilePic(Uri uri) {
        //progressDialog.show();
        String filePath = storagePath + "" + "image" + "_" + user.getUid();
        StorageReference refSecond = storageReference.child(filePath);
        refSecond.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadUri = uriTask.getResult();
                        if (uriTask.isSuccessful()) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("image", downloadUri.toString());
                            databaseRef.child(user.getUid()).updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Image updated", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Error occurred at updating", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
