package com.advance.sanatani_vivah.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddSuccessStoryActivity extends AppCompatActivity {

    private ImageView img_story;
    private TextView lblTerms, tv_cancel, tv_gallary, tv_camera;
    private String STORY_TAG = "story", mCurrentPhotoPath, org_path, crop_path;
    private TextInputEditText et_dob,  et_bride_name, et_groom_name;//et_bride_id, et_groom_id,
    private EditText et_about;
    private DatePickerDialog.OnDateSetListener date;
    final Calendar myCalendar = Calendar.getInstance();
    private LinearLayout layoutBottomSheet;
    private SessionManager session;
    private Common common;
    private Button btn_save_story;
    private ImageView btn_edit_id;
    private RelativeLayout loader;
    private BottomSheetBehavior sheetBehavior;
    private HashMap<String, String> image_map;
    private Uri resultUri;
    boolean isImageSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_success_story);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add Success Story");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        init();

        loader = findViewById(R.id.loader);
    }

    private void init() {
        session = new SessionManager(this);
        common = new Common(this);
        image_map = new HashMap<>();

        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

//        et_bride_id = findViewById(R.id.et_bride_id);
//        et_groom_id = findViewById(R.id.et_groom_id);
        et_bride_name = findViewById(R.id.et_bride_name);
        et_groom_name = findViewById(R.id.et_groom_name);
        et_about = findViewById(R.id.et_about);

        img_story = findViewById(R.id.img_story);

        tv_camera = findViewById(R.id.tv_camera);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_gallary = findViewById(R.id.tv_gallary);

        btn_edit_id = findViewById(R.id.btn_edit_id);

        btn_save_story = findViewById(R.id.btn_save_story);
        btn_save_story.setOnClickListener(v -> {
            validaddstory();
        });

        lblTerms = findViewById(R.id.lblTerms);
        singleTextView(lblTerms);

        datemarried();

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }

        });
        tv_camera.setOnClickListener(view -> {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            fromCamera();
        });
        tv_gallary.setOnClickListener(view -> {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            fromGallery();
        });
        tv_cancel.setOnClickListener(view -> {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
        btn_edit_id.setOnClickListener(view -> {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });
    }

    private void datemarried() {
        et_dob = findViewById(R.id.et_dob);
        et_dob.setOnClickListener(v -> {
            //For above 18 years date
            Calendar maxDateCalendar = Calendar.getInstance();
            //    maxDateCalendar.add(Calendar.YEAR, -18);
            maxDateCalendar.add(Calendar.DATE, -1);

            //For below 18 years date
            Calendar minDateCalendar = Calendar.getInstance();
            minDateCalendar.add(Calendar.YEAR, -3);
            minDateCalendar.add(Calendar.DATE, 1);

            DatePickerDialog dialog = new DatePickerDialog(AddSuccessStoryActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            //set max date in date picker
            dialog.getDatePicker().setMaxDate(maxDateCalendar.getTime().getTime());
            dialog.getDatePicker().setMinDate(minDateCalendar.getTime().getTime());
            dialog.show();
        });
        et_dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!et_dob.getText().toString().equals("")) {
                    et_dob.setError(null);
                }
            }
        });
        date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
    }

    private void validaddstory() {

//        String bid = et_bride_id.getText().toString().trim();
//        String gid = et_groom_id.getText().toString().trim();
        String bname = et_bride_name.getText().toString().trim();
        String gname = et_groom_name.getText().toString().trim();
        String dob = et_dob.getText().toString().trim();
        String about = et_about.getText().toString().trim();


        boolean isvalid = true;
//        if (TextUtils.isEmpty(bid)) {
//            et_bride_id.setError("Please enter Bride's ID");
//            isvalid = false;
//        }
//        if (TextUtils.isEmpty(gid)) {
//            et_groom_id.setError("Please enter Groom's ID");
//            isvalid = false;
//        }
        if (TextUtils.isEmpty(bname)) {
            et_bride_name.setError("Please enter Bride's name");
            isvalid = false;
        }
        if (TextUtils.isEmpty(gname)) {
            et_groom_name.setError("Please enter Groom's name");
            isvalid = false;
        }
        if (TextUtils.isEmpty(dob)) {
            et_dob.setError("Please enter date of birth");
            isvalid = false;
        }
        if (TextUtils.isEmpty(about)) {
            et_about.setError("Please enter you're How You Meet");
            isvalid = false;
        }
        if(resultUri == null) {
            Common.showToast("Please select photo");
            isvalid = false;
        }
        if (isvalid) {
            submitstory(bname, gname, dob, about);
        }
    }

    private void submitstory(String bname, String gname, String dob, String about) {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
//        param.put("brideid", bid);
//        param.put("groomid", gid);
        param.put("bridename", bname);
        param.put("groomname", gname);
        param.put("marriagedate", changeDate(dob));
        param.put("successmessage", about);
        param.put("weddingphoto", resultUri.getPath());
        param.put("terms_condition", "true");
        param.put("csrf_new_matrimonial", session.getLoginData(SessionManager.TOKEN));
        Log.d("resp", param.toString());

        common.makePostRequestWithTag(AppConstants.save_story, param, response -> {
            Log.d("resp", response);
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("token"));
                Toast.makeText(getApplicationContext(), object.getString("errmessage"), Toast.LENGTH_SHORT).show();
                if (object.getString("status").equals("success")) {
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }

        }, error -> {
            Log.d("resp", error.getMessage() + "   ");
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        }, STORY_TAG);

    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //yyyy-M-dd
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_dob.setText(sdf.format(myCalendar.getTime()));
    }

    public String changeDate(String time) {
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "yyyy-M-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void singleTextView(TextView textView) {
        String clickableTextStr = "Terms and Conditions.";
        SpannableStringBuilder spanText = new SpannableStringBuilder();
        spanText.append("By submitting you agree our " + clickableTextStr);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                openCMSDataDialog();
            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(ContextCompat.getColor(AddSuccessStoryActivity.this, R.color.blue_color));    // you can use custom color
                textPaint.setUnderlineText(false);    // this remove the underline
            }
        }, spanText.length() - clickableTextStr.length(), spanText.length(), 0);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(ContextCompat.getColor(AddSuccessStoryActivity.this, R.color.transparent));
        textView.setText(spanText, TextView.BufferType.SPANNABLE);
        //  lblTerms.setText(Html.fromHtml(getString(R.string.lbl_service_request)), TextView.BufferType.SPANNABLE);

    }

    private void openCMSDataDialog() {
        Intent intent = new Intent(this, AllCmsActivity.class);
        intent.putExtra(AppConstants.KEY_INTENT, "term");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        common.hideProgressRelativeLayout(loader);
        MyApplication.getInstance().cancelPendingRequests(STORY_TAG);
        startActivity(new Intent(AddSuccessStoryActivity.this, SuccessStoryActivity.class));
    }

    private void fromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    private void fromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG", "requestCode  " + requestCode + " resultCode  " + resultCode);
        //   callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 1) {
                Uri des = Uri.fromFile(new File(mCurrentPhotoPath));
                org_path = des.getPath();
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(des);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String encodedImage = encodeImage(selectedImage);
                    image_map.put("profile_photo_org", encodedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                cropImage(des);
            } else if (requestCode == 2) {
                Uri selectedImageUri = data.getData();
                org_path = Common.getPath(this, selectedImageUri);
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String encodedImage = encodeImage(selectedImage);
                    image_map.put("weddingphoto_path", encodedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                cropImage(selectedImageUri);
            } else if (requestCode == 3) {
                resultUri = UCrop.getOutput(data);
                crop_path = resultUri.getPath();

                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(resultUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    isImageSelect = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                img_story.setImageBitmap(bitmap);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void cropImage(Uri imagePath) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageExtension = Common.getExtensionFromPath(Common.getPath(AddSuccessStoryActivity.this, imagePath));
        AppDebugLog.print("imageExtension : " + imageExtension);

        UCrop uCrop = UCrop.of(imagePath, Uri.fromFile(new File(getCacheDir(), timeStamp + imageExtension)));
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(250, 250);
        UCrop.Options options = new UCrop.Options();

        options.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        options.setToolbarWidgetColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        options.setRootViewBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        uCrop.withOptions(options);
        uCrop.start(this, 3);

    }

    public String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}