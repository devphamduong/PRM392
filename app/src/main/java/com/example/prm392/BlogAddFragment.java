package com.example.prm392;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prm392.databinding.FragmentBlogAddBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;

public class BlogAddFragment extends Fragment {
    FragmentBlogAddBinding binding;
    Uri filepath;

    public BlogAddFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void selectImage() {
        binding.view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Your Image"), 101);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == 101 && data != null && data.getData() != null) {
            filepath = data.getData();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            binding.imgBlog.setVisibility(View.VISIBLE);
            binding.imgBlog.setImageBitmap(bitmap);
            binding.view2.setVisibility(View.VISIBLE);
            binding.tvSelect.setVisibility(View.VISIBLE);
            uploadData(filepath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadData(Uri filepath) {
        binding.btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.txtTitle.getText().toString().equals("")) {
                    binding.txtTitle.setError("Field is required!");
                } else if (binding.txtDescription.getText().toString().equals("")) {
                    binding.txtTitle.setError("Field is required!");
                } else if (binding.txtAuthor.getText().toString().equals("")) {
                    binding.txtTitle.setError("Field is required!");
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Upload");
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                    String title = binding.txtTitle.getText().toString();
                    String desc = binding.txtDescription.getText().toString();
                    String author = binding.txtAuthor.getText().toString();
                    if (filepath != null) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference reference = storage.getReference().child("images/" + filepath.toString() + "jpg");
                        reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String fileUrl = task.getResult().toString();
                                        String day = (String) DateFormat.format("dd", new Date());
                                        String month = (String) DateFormat.format("MMM", new Date());
                                        String date = day + " " + month;

                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("title", title);
                                        map.put("desc", desc);
                                        map.put("author", author);
                                        map.put("date", date);
                                        map.put("img", fileUrl);
                                        map.put("shareCount", "0");

                                        FirebaseFirestore.getInstance().collection("Blogs").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Upload successfully", Toast.LENGTH_SHORT).show();
                                                binding.imgBlog.setVisibility(View.VISIBLE);
                                                binding.view2.setVisibility(View.VISIBLE);
                                                binding.tvSelect.setVisibility(View.VISIBLE);

                                                binding.txtTitle.setText("");
                                                binding.txtDescription.setText("");
                                                binding.txtAuthor.setText("");
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });

    }
}
