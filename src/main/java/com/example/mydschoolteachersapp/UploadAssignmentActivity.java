package com.example.mydschoolteachersapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mydschoolteachersapp.Adapters.AdapterRecyclerViewGetAssignment;
import com.example.mydschoolteachersapp.Classes.CheckInternetConnection;
import com.example.mydschoolteachersapp.Classes.Config;
import com.example.mydschoolteachersapp.Classes.Datum;
import com.example.mydschoolteachersapp.Classes.FilePath;
import com.example.mydschoolteachersapp.Classes.Singelton;
import com.example.mydschoolteachersapp.Model.AssignmentModel;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadAssignmentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    EditText editTextTitle, editTextMarks, editTextSubmissionDate;
    TextView textViewFileName;
    Spinner spinnerSubject;
    FloatingActionButton floatingActionButtonAdd, floatingActionButtonCamera, floatingActionButtonFile;
    Button buttonSubmit;
    ImageView imageViewPic;
    ArrayList<Datum> list_subjects = new ArrayList<Datum>();
    private String mClassId;
    private String mSectionId;
    private int mYear, mMonth, mDay;
    private String date;
    private static final String TAG = "AndroidUploadService";
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int FILE_SELECT_CODE = 55;
    private Uri filePath;
    private Cursor cursor;
    String path;
    private String mSubjectId;
    ArrayList<AssignmentModel> mListAssignment = new ArrayList<>();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    HashMap<String, String> data;
    private Uri imageUri;
    private Animation fab_open;
    private Animation fab_close;
    private Animation rotate_forward;
    private Animation rotate_backward;
    private boolean isFabOpen = true;
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    private String mSubjectName;
    FloatingActionButton floatingActionButtonUp;
    private View.OnClickListener l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_assignment);
        Intent intent = getIntent();
        mClassId = intent.getExtras().getString("CLASS_ID");
        mSectionId = intent.getExtras().getString("SECTION_ID");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Assignment");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        editTextTitle = findViewById(R.id.editTextTitle);

       // floatingActionButtonUp.setOnClickListener(l);
        editTextMarks = findViewById(R.id.editTextEnterMarks);
        editTextSubmissionDate = findViewById(R.id.editTextDate);
        textViewFileName = findViewById(R.id.fileName);
        spinnerSubject = findViewById(R.id.spinnerSelectSubject);
        imageViewPic = findViewById(R.id.imageViewPic);
        floatingActionButtonCamera = findViewById(R.id.floatingActionButtonCamera);

//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetViewgroup);
        if (CheckInternetConnection.checkConnection(this)) {
            getSubjectList();
            setDate();
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.container),
                    "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Go to Settings", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(
                            Settings.ACTION_WIFI_SETTINGS));

                }
            });
            snackbar.show();

        }
        floatingActionButtonAdd = findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonFile = findViewById(R.id.floatingActionButtonUpload);
        floatingActionButtonCamera = findViewById(R.id.floatingActionButtonCamera);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        floatingActionButtonAdd.setOnClickListener(this);
        floatingActionButtonFile.setOnClickListener(this);
        floatingActionButtonCamera.setOnClickListener(this);
        animateFAB();
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitAssignment();


            }
        });

    }
    private void animateBottomSheetArrows(float slideOffset) {
        // Animate counter-clockwise
        floatingActionButtonUp.setRotation(slideOffset * -180);
        // Animate clockwise
        floatingActionButtonUp.setRotation(slideOffset * 180);
    }
    public void getSubjectList() {
        final ArrayList<String> listSubjectNames=new ArrayList<>();
        StringRequest stringRequestGetSubjectList = new StringRequest(Request.Method.POST, Config.URL_GET_SUBJECT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listSubjectNames.add("Select subject");
                try {
                    data = new HashMap<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject jsonObject1;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Datum datum = new Datum();
                        jsonObject1 = jsonArray.getJSONObject(i);
                        datum.setSubjectId(jsonObject1.getString("subject_id"));
                        datum.setSubjectName(jsonObject1.getString("subject_name"));
                        String subject_name=jsonObject1.getString("subject_name");
                        list_subjects.add(datum);
                        listSubjectNames.add(subject_name);

                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UploadAssignmentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                final ArrayAdapter<String> arrayAdapterSubject = new ArrayAdapter(UploadAssignmentActivity.this
                        , R.layout.support_simple_spinner_dropdown_item, listSubjectNames);
                spinnerSubject.setAdapter(arrayAdapterSubject);

                spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(spinnerSubject.getSelectedItem().toString().equalsIgnoreCase("Select subject")){

                        }else{
                            mSubjectName = String.valueOf(parent.getItemAtPosition(position-1));
                            mSubjectId=list_subjects.get(position-1).getSubjectId();

                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {


                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UploadAssignmentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("class_id", mClassId);
                params.put("section_id", mSectionId);
                return params;
            }
        };
        Singelton.getInstance(UploadAssignmentActivity.this).addToRequestQue(stringRequestGetSubjectList);

    }


    public void setDate() {
        editTextSubmissionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int MM = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker1 = new DatePickerDialog(UploadAssignmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = year + "-" + (monthOfYear + 1)
                                + "-" + dayOfMonth;


                        Calendar c = Calendar.getInstance();
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int month = c.get(Calendar.MONTH);
                        int year1 = c.get(Calendar.YEAR);
                        String date_current = year1 + "-" + (month + 1) + "-" + day;

                        if (date_current.equalsIgnoreCase(date)) {
                            Toast.makeText(UploadAssignmentActivity.this, "Submission date and Upload Date Cannot be Same", Toast.LENGTH_LONG).show();
                        } else {
                            editTextSubmissionDate.setText(date);
                        }


                    }
                }, yy, MM, dd);
                datePicker1.show();
            }
        });

    }

    public void takePicture() {
        floatingActionButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    public void submitAssignment() {
        if (editTextTitle.getText().toString().equals("")) {
            editTextTitle.setError("Please Enter Title");
        } else if (editTextSubmissionDate.getText().toString().equals("")) {
            editTextSubmissionDate.setError("Please Select a date");
        } else if (editTextMarks.getText().toString().equals("")) {
            editTextMarks.getText().toString().equals("");
            {
                editTextMarks.setError("Please Enter Marks");
            }
        } else if (textViewFileName.getText().toString().equals("Upload File")) {
            textViewFileName.setError("Please Upload Assignment");
        } else {
            if (editTextTitle.getText().toString().equals("")) {
                editTextTitle.setError("Please Enter Title");
            } else if (editTextSubmissionDate.getText().toString().equals("")) {
                editTextSubmissionDate.setError("Please Select a date");
            } else if (editTextMarks.getText().toString().equals("")) {
                editTextMarks.getText().toString().equals("");
                {
                    editTextMarks.setError("Please Enter Marks");
                }
            } else if (textViewFileName.getText().toString().equals("Upload File")) {
                textViewFileName.setError("Please Upload Assignment");
            } else {
                final ProgressDialog progressDialog = new ProgressDialog(UploadAssignmentActivity.this);
                progressDialog.setTitle("Uploading");
                progressDialog.setMessage("Please Wait....");
                progressDialog.show();
                try {

                    path = FilePath.getPath(UploadAssignmentActivity.this, filePath);
                   // Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                   // System.out.println("PATH:" + path);


                    if (path.equals(null)) {
                        Toast.makeText(UploadAssignmentActivity.this, "No File Selected", Toast.LENGTH_LONG).show();
                    } else {

                        try {
                            String mSubmissionDate = editTextSubmissionDate.getText().toString();


                            String mTitle = editTextTitle.getText().toString();


                            if (path == null) {
                                Toast.makeText(UploadAssignmentActivity.this, "No File Selected to Upload", Toast.LENGTH_SHORT).show();
                            }
                            String mMarks = editTextMarks.getText().toString();
                            if (mMarks.isEmpty() || mMarks.equalsIgnoreCase("")) {
                                mMarks = "0";
                            }

                            SharedPreferences shared = UploadAssignmentActivity.this.getSharedPreferences(Config.MY_SHARED_PREFRENCE, MODE_PRIVATE);
                            String parent_id = shared.getString(Config.SCHOOL_ID, "");
                            final String uploadId = UUID.randomUUID().toString();

                            String UPLOAD_URL = Config.URL_UPLOAD_ASSIGNMENT;
                            new MultipartUploadRequest(UploadAssignmentActivity.this, uploadId, UPLOAD_URL)
                                    .addFileToUpload(path, "image") //Adding file
                                    .addParameter("class_id", mClassId)
                                    .addParameter("section_id", mSectionId)
                                    .addParameter("subject_id", mSubjectId)
                                    .addParameter("assignment_marks", editTextMarks.getText().toString())
                                    .addParameter("assignment_title", mTitle)
                                    .addParameter("submission_date", mSubmissionDate)
                                    .addParameter("school_id", parent_id)
                                    //Adding text parameter to the request
                                    .setNotificationConfig(new UploadNotificationConfig())
                                    .setMaxRetries(2)
                                    .setDelegate(new UploadStatusDelegate() {


                                        @Override
                                        public void onProgress(Context context, UploadInfo uploadInfo) {

                                        }


                                        @Override
                                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                            progressDialog.dismiss();
                                            Toast.makeText(UploadAssignmentActivity.this, "Error  while Uploading" + exception.getMessage(), Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                            progressDialog.dismiss();
                                            getAssignment();
                                            filePath = null;
                                            editTextTitle.setText("");
                                            editTextSubmissionDate.setText("");
                                            editTextMarks.setText("");
                                            textViewFileName.setText("");
                                            imageViewPic.setImageDrawable(null);

                                            Toast.makeText(UploadAssignmentActivity.this,   "Completed", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onCompleted: "+serverResponse.getBodyAsString());
//                                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                        }

                                        @Override
                                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                                            progressDialog.dismiss();
                                            filePath = null;


                                        }
                                    })
                                    .startUpload();
                            //Starting the upload
                        } catch (Exception exc) {
                            progressDialog.dismiss();
                           // Log.e(TAG, exc.getMessage(), exc);


                        }
                    }
                } catch (NullPointerException e) {
                    progressDialog.dismiss();

                    Toast.makeText(UploadAssignmentActivity.this, "No File Selected to Upload", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public void getAssignment() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Previous Assignment List !!");
        progressDialog.show();

        // Toast.makeText(this, "MCLASSID:"+mClassId+"MSECTIONID:"+mSectionId, Toast.LENGTH_SHORT).show();
        StringRequest stringRequestGetAssignment = new StringRequest(Request.Method.POST, Config.URL_GET_ASSIGNMENT_LIST1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Toast.makeText(UploadAssignmentActivity.this, response, Toast.LENGTH_SHORT).show();
                   // Log.d("@##@", response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (Integer.parseInt(jsonObject.getString("success")) == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("assigment_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            AssignmentModel assignmentModel = new AssignmentModel(jsonObject1.getString("assignment_id"),
                                    jsonObject1.getString("title"),
                                    jsonObject1.getString("document_path"));
                            progressDialog.dismiss();
                            mListAssignment.add(assignmentModel);
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(UploadAssignmentActivity.this, "No Previous Assignment Found !!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                   // System.out.println("EXCEPTION GET ASSIGNMENT" + e.getMessage());
                    Toast.makeText(UploadAssignmentActivity.this, "Something went wrong....", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(UploadAssignmentActivity.this, "Something went wrong....", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //send data to server and get respons from its in respons
                params.put("class_id", mClassId);
                params.put("section_id", mSectionId);
                //  Toast.makeText(UploadAssignmentActivity.this, mClassId+mSectionId, Toast.LENGTH_SHORT).show();

                return params;
            }
        };
        Singelton.getInstance(UploadAssignmentActivity.this).addToRequestQue(stringRequestGetAssignment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            filePath = imageUri;
            try {
                imageViewPic.setVisibility(View.VISIBLE);
                textViewFileName.setText("");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageViewPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();

            }
            System.out.println("Image " + imageUri);

        } else if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                filePath = data.getData();
                String uriString = filePath.toString();

                File myFile = new File(uriString);
                final String[] split = myFile.getPath().split(":");//split the path.
                path = split[1];//assign it to a string(your choice).


                String displayName = null;

                if (filePath.equals("")) {
                    Toast.makeText(UploadAssignmentActivity.this, "No File Selected", Toast.LENGTH_LONG).show();
                   // Toast.makeText(UploadAssignmentActivity.this, uriString, Toast.LENGTH_SHORT).show();
                    System.out.println("UURI"+uriString);
                } else {
                    if (uriString.startsWith("content://")) {

                        try {
                            cursor = UploadAssignmentActivity.this.getContentResolver().query(filePath, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                            }
                        } finally {
                            cursor.close();
                        }

                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }
                    textViewFileName.setText(displayName);

                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.floatingActionButtonAdd:

                animateFAB();
                break;
            case R.id.floatingActionButtonUpload:
                animateFAB();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, MULTIPLE_PERMISSIONS
                    );
                    if (ContextCompat.checkSelfPermission(UploadAssignmentActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                    } else {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        try {
                            startActivityForResult(
                                    Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // Potentially direct the user to the Market with a Dialog
                            Toast.makeText(UploadAssignmentActivity.this, "Please install a File Manager.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                else{
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    try {
                        startActivityForResult(
                                Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // Potentially direct the user to the Market with a Dialog
                        Toast.makeText(UploadAssignmentActivity.this, "Please install a File Manager.",
                                Toast.LENGTH_SHORT).show();
                    }
                }

              //  Log.d("Raj", "Fab 1");
                break;
            case R.id.floatingActionButtonCamera:
                animateFAB();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MULTIPLE_PERMISSIONS
                    );
                    if (ContextCompat.checkSelfPermission(UploadAssignmentActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                    }
                    else
                    {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        // Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent1.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent1, REQUEST_IMAGE_CAPTURE);
                        }
                    }

                }
                else
                {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    // Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent1.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent1, REQUEST_IMAGE_CAPTURE);
                    }
                }
                break;
        }
    }

    public void animateFAB() {

        if (isFabOpen) {

            floatingActionButtonAdd.startAnimation(rotate_backward);
            floatingActionButtonFile.startAnimation(fab_close);
            floatingActionButtonCamera.startAnimation(fab_close);
            floatingActionButtonFile.setClickable(false);
            floatingActionButtonCamera.setClickable(false);
            isFabOpen = false;
           // Log.d("Raj", "close");

        } else {

            floatingActionButtonAdd.startAnimation(rotate_forward);
            floatingActionButtonFile.startAnimation(fab_open);
            floatingActionButtonCamera.startAnimation(fab_open);
            floatingActionButtonFile.setClickable(true);
            floatingActionButtonCamera.setClickable(true);
            isFabOpen = true;
           // Log.d("Raj", "open");

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(UploadAssignmentActivity.this, NavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("FRAGMENTS", "ASSIGNMENT");
                startActivity(intent);
                finish();
                return true;
            // todo: goto back activity from here
//            case R.id.action_settings:
//                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
//                alertBuilder.setTitle("Logout");
//                alertBuilder.setMessage("Do you Really Want to Exit");
//
//                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        SharedPreferences.Editor editor = getSharedPreferences(Config.MY_SHARED_PREFRENCE, Context.MODE_PRIVATE).edit();
//                        editor.clear();
//                        editor.apply();
//                        Intent intent = new Intent(UploadAssignmentActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                    }
//                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                alertBuilder.create();
//                alertBuilder.show();




            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UploadAssignmentActivity.this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("FRAGMENTS", "ASSIGNMENT");
        startActivity(intent);
        finish();
    }
    public static String getPathForPie(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        Log.i("URI",uri+"");
        String result = uri+"";
        // DocumentProvider
        //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isKitKat && (result.contains("media.documents"))) {
            String[] ary = result.split("/");
            int length = ary.length;
            String imgary = ary[length-1];
            final String[] dat = imgary.split("%3A");
            final String docId = dat[1];
            final String type = dat[0];
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
            } else if ("audio".equals(type)) {
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[] {
                    dat[1]
            };
            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

}
