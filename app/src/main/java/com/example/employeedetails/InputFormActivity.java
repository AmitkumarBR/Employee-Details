package com.example.employeedetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InputFormActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private EditText etName, etId, etCompany;
    private Button btnAdd;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseStorage mStorage;

    private static final int Gallery_Code = 1;
    private Uri imageUrl = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_form);

        init();
    }

    private void init() {
        imageButton = (ImageButton) findViewById(R.id.image_button);
        etName = (EditText) findViewById(R.id.et_name);
        etId = (EditText) findViewById(R.id.et_id);
        etCompany = (EditText) findViewById(R.id.et_company);
        btnAdd = (Button) findViewById(R.id.btn_add);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("User Details");
        mStorage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(InputFormActivity.this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallery_Code);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Code && resultCode == RESULT_OK) {
            imageUrl = data.getData();
            imageButton.setImageURI(imageUrl);
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String id = etId.getText().toString().trim();
                String company = etCompany.getText().toString().trim();

                if (!(name.isEmpty() && id.isEmpty() && company.isEmpty() && imageUrl != null)) {
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    StorageReference filepath = mStorage.getReference().child("imagePost").child(imageUrl.getLastPathSegment());
                    filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String t = task.getResult().toString();
                                    DatabaseReference newPost = mRef.child(id);
                                    newPost.child("Name").setValue(name);
                                    newPost.child("Id").setValue(id);
                                    newPost.child("Company").setValue(company);
                                    newPost.child("Image").setValue(task.getResult().toString());
                                    progressDialog.dismiss();

                                    startActivity(new Intent(InputFormActivity.this, EmployeeListActivity.class));
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}