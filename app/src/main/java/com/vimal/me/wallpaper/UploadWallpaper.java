package com.vimal.me.wallpaper;



import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vimal.me.wallpaper.Common.Common;
import com.vimal.me.wallpaper.Model.AnalyzeModel.ComputerVision;
import com.vimal.me.wallpaper.Model.AnalyzeModel.URLUpload;
import com.vimal.me.wallpaper.Model.CategoryItem;
import com.vimal.me.wallpaper.Model.WallpaperItem;
import com.vimal.me.wallpaper.Remote.IComputerVision;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadWallpaper extends AppCompatActivity {

    ImageView image_preview;
    Button btn_upload, btn_browser,btn_submit;
    MaterialSpinner spinner;

    //Material Spinner Data
    Map<String, String> spinnerData = new HashMap<>();

    private Uri filePath;

    String categoryIdSelect = "",directUrl="",nameOfFile="";


    //FireStorage
    FirebaseStorage storage;
    StorageReference storageReference;
    IComputerVision mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_wallpaper);

        mService = Common.getComputerVisionAPI();


        //Firebase Storage init
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        //View
        image_preview = (ImageView) findViewById(R.id.image_preview);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_browser = (Button) findViewById(R.id.btn_browser);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);


        //Load spinner Data
        loadCategoryToSpinner();


        //Button  Event

        btn_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedIndex() == 0)   // hint not choose anymore
                    Toast.makeText(UploadWallpaper.this,"Please choose category",Toast.LENGTH_SHORT).show();

                else
                    upload();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectAdultContent(directUrl);
            }
        });

    }

    private void detectAdultContent(final String directUrl) {
        if(directUrl.isEmpty())
            Toast.makeText(this,"Picture not Uploaded",Toast.LENGTH_SHORT).show();
        else
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Analyzing Image...");
            progressDialog.show();
            mService.analyzeImage(Common.getAPIAdultEndPoint(),new URLUpload(directUrl))
                    .enqueue(new Callback<ComputerVision>() {
                        @Override
                        public void onResponse(Call<ComputerVision> call, Response<ComputerVision> response) {
                            if(response.isSuccessful())
                            {
                                if(!response.body().getAdult().isAdultContent())
                                {
                                    //If picture is not contain adult content
                                    //we will save it to our background gallery
                                    progressDialog.dismiss();
                                    saveUrlCategory(categoryIdSelect,directUrl);
                                    Toast.makeText(UploadWallpaper.this,"Uploaded !!!",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    //If url is adult content ,we will delete it from firebase storage
                                    deleteFileFromStorage(nameOfFile);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ComputerVision> call, Throwable t) {
                            Toast.makeText(UploadWallpaper.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

    private void deleteFileFromStorage(String nameOfFile) {
        storageReference.child("images/"+nameOfFile)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UploadWallpaper.this,"Your image is adult content and will be deleted",Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void upload() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            nameOfFile =UUID.randomUUID().toString();
            final StorageReference ref = storageReference.child(new StringBuilder("image/*").append(nameOfFile)
                    .toString());

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            taskSnapshot.getStorage()
                                    .getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            directUrl = uri.toString();
                                            btn_submit.setEnabled(true);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(UploadWallpaper.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });





                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadWallpaper.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded :"+(int)progress+"%");
                        }
                    });
        }


    }

    private void saveUrlCategory(String categoryIdSelect, String imageLink) {

        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPER)
                .push()
                .setValue(new WallpaperItem(imageLink, categoryIdSelect))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UploadWallpaper.this, "Success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }


    //CTRL + 0


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image_preview.setImageBitmap(bitmap);
                btn_upload.setEnabled(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures :"), Common.PICK_IMAGE_REQUEST);
    }

    private void loadCategoryToSpinner() {
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_CATEGORY_BACKGROUND)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            CategoryItem item = postSnapshot.getValue(CategoryItem.class);
                            String key = postSnapshot.getKey();
                            spinnerData.put(key, item.getName());
                        }
                        //Beacause Material spinner will not receive hint so we neeed  custom hint
                        //this is my tip
                        Object[] valueArray = spinnerData.values().toArray();
                        List<Object> valueList = new ArrayList<>();
                        valueList.add(0, "Category");//we will add first item hint  :D
                        valueList.addAll(Arrays.asList(valueArray));
                        spinner.setItems(valueList);
                        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                //when user choose category , we will get categoryId (key)
                                Object[] keyArray = spinnerData.keySet().toArray();
                                List<Object> keyList = new ArrayList<>();
                                keyList.add(0, "Category_key");//we will add first item hint  :D
                                keyList.addAll(Arrays.asList(keyArray));
                                categoryIdSelect = keyList.get(position).toString(); // Assign key  when user choose category

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });
    }


    @Override
    public void onBackPressed() {
        deleteFileFromStorage(nameOfFile);
        super.onBackPressed();
    }
}