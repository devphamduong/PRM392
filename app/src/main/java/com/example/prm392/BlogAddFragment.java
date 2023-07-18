package com.example.prm392;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Date;
import java.util.List;

public class BlogAddFragment extends Fragment {
    FragmentBlogAddBinding binding;
    Uri filepath;
    private DatabaseReference mDatabase;

    public BlogAddFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBlogAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        selectImage();
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
            binding.imgBlog.setVisibility(View.VISIBLE);
            binding.imgBlog.setImageURI(filepath);
            binding.view2.setVisibility(View.VISIBLE);
            binding.tvSelect.setVisibility(View.INVISIBLE);
            uploadData(filepath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadData(Uri filePath) {
        binding.btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            if (binding.txtTitle.getText().toString().equals("")) {
                                binding.txtTitle.setError("Field is Required!");
                            } else if (binding.txtDescription.getText().toString().equals("")) {
                                binding.txtDescription.setError("Field is Required!");
                            } else if (binding.txtAuthor.getText().toString().equals("")) {
                                binding.txtAuthor.setError("Field is Required!");
                            } else {
                                ProgressDialog pd = new ProgressDialog(getContext());
                                pd.setTitle("Uploading...");
                                pd.setMessage("Please wait for a while until data uploaded");
                                pd.setCancelable(true);
                                pd.show();

                                String title = binding.txtTitle.getText().toString();
                                String desc = binding.txtDescription.getText().toString();
                                String author = binding.txtAuthor.getText().toString();

                                if (filePath != null) {
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference reference = storage.getReference().child("images/" + filePath.toString() + ".jpg");
                                    reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        String fileUrl = task.getResult().toString();
                                                        String day = (String) DateFormat.format("dd", new Date());
                                                        String month = (String) DateFormat.format("MMM", new Date());
                                                        String date = day + " " + month;

                                                        String key = mDatabase.child("Blogs").push().getKey();
                                                        Blog blog = new Blog(key, title, desc, author, date, fileUrl, "0", String.valueOf(System.currentTimeMillis()));
                                                        mDatabase.child("Blogs").child(key).setValue(blog, new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                                if (error == null) {
                                                                    pd.dismiss();
                                                                    Toast.makeText(getContext(), "Post uploaded!", Toast.LENGTH_SHORT).show();

                                                                    binding.imgBlog.setVisibility(View.INVISIBLE);
                                                                    binding.view2.setVisibility(View.VISIBLE);
                                                                    binding.tvSelect.setVisibility(View.VISIBLE);
                                                                    binding.txtTitle.setText("");
                                                                    binding.txtDescription.setText("");
                                                                    binding.txtAuthor.setText("");
                                                                } else {
                                                                    Toast.makeText(getContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }


                            }
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showSettingDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError dexterError) {
                        getActivity().finish();
                    }
                }).onSameThread().check();
            }
        });
    }

    private void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Need Permission");
        builder.setMessage("This app needs permission to use this feature. You can grant us these permission manually by clicking on below button");
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                getActivity().finish();
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}
