package com.advance.sanatani_vivah.activities;

import static com.advance.sanatani_vivah.utility.AppConstants.MAX_IMAGE_SIZE_IN_MB_TO_UPLOAD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.ProgressRequestBody;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class RegisterPhotoUploadActivity extends AppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {

    private Common common;
    private SessionManager session;
    private RelativeLayout loader;

    private ImageView img_profile;
    private MaterialButton btn_choose, btn_six_submit;

    private BottomSheetBehavior sheetBehavior;
    private LinearLayout layoutBottomSheet;
    private TextView tv_cancel, tv_gallary, tv_camera;

    boolean isImageSelect = false;
    private final int CROP_PIC = 3;
    private final int PERMISSION_REQUEST_CODE = 122;
    private EasyImage easyImage = null;
    private File compressedFile = null;
    private File originalFile = null;
    private String originalFilePath = "", cropFilePath = "";
    private Uri cropUri;

    private String registerId = "";
    private String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_photo_upload);

        common = new Common(this);
        session = new SessionManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Photo");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        initialize();
        setUpEasyImage();
    }

    private void initialize() {

        registerId = getIntent().getStringExtra("registrationId");
        gender = getIntent().getStringExtra("gender");

        loader = findViewById(R.id.loader);
        img_profile = findViewById(R.id.img_profile);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_gallary = findViewById(R.id.tv_gallary);
        tv_camera = findViewById(R.id.tv_camera);
        btn_choose = findViewById(R.id.btn_choose);
        btn_six_submit = findViewById(R.id.btn_six_submit);

        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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

        if (gender.equals("Female")) {
            int placeHolder = R.drawable.female;
            img_profile.setImageResource(placeHolder);
        } else if (gender.equals("Male")) {
            int placeHolder = R.drawable.male;
            img_profile.setImageResource(placeHolder);
        }

        btn_choose.setOnClickListener(this);
        btn_six_submit.setOnClickListener(this);
        tv_gallary.setOnClickListener(this);
        tv_camera.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    private void setUpEasyImage() {
        easyImage = new EasyImage.Builder(RegisterPhotoUploadActivity.this)
                .setChooserTitle(getString(R.string.app_name))
                .setCopyImagesToPublicGalleryFolder(false)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName(AppConstants.DIRECTORY_NAME)
                .allowMultiple(false)
                .build();
    }

    private void pickImage(int pickFor) {
        switch (pickFor) {
            case 100:
                easyImage.openGallery(this);
                break;

            case 200:
                easyImage.openCameraForImage(this);
                break;
        }
    }

    private void openFileChooser() {
        requestPermission();
    }

    @AfterPermissionGranted(122)
    private void requestPermission() {
        if (!checkPermission()) {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    "This needs permission to use feature. You can grant them in app settings.",
                    PERMISSION_REQUEST_CODE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    //TODO Permission related
    private boolean checkPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_choose:
                openFileChooser();
                break;

            case R.id.tv_gallary:
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                pickImage(100);
                break;

            case R.id.tv_camera:
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                pickImage(200);
                break;

            case R.id.tv_cancel:
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.btn_six_submit:
                if (!isImageSelect) {
                    Toast.makeText(getApplicationContext(), "Please select image first", Toast.LENGTH_LONG).show();
                    return;
                }
                uploadFileToServer();
                break;
        }
    }

    private void uploadFileToServer() {
        if (!ConnectionDetector.isConnectingToInternet(this)) {
            Toast.makeText(this, getString(R.string.err_msg_no_intenet_connection), Toast.LENGTH_LONG).show();
            return;
        }

        common.showProgressRelativeLayout(loader);

        //File org = new File(org_path);
        File profileOriginalImageCompressedFile = Common.getCompressedImageFile(this, new File(Common.getPath(this, cropUri)));
        ProgressRequestBody originalFileBody = new ProgressRequestBody(profileOriginalImageCompressedFile, getMimeType(originalFilePath), this);
        MultipartBody.Part originalFilePart = MultipartBody.Part.createFormData("profil_photo_org", profileOriginalImageCompressedFile.getName().replaceAll("[^a-zA-Z0-9.]", ""), originalFileBody);

        RequestBody partParam1 = RequestBody.create(MediaType.parse("text/plain"), registerId);
        RequestBody partParam2 = RequestBody.create(MediaType.parse("text/plain"), "NI-AAPP");
        RequestBody partParam3 = RequestBody.create(MediaType.parse("text/plain"), session.getLoginData(SessionManager.TOKEN));

        Retrofit retrofit = RetrofitClient.getClient();
        AppApiService appApiService = retrofit.create(AppApiService.class);

        Call<JsonObject> call = null;
        File crop = new File(cropFilePath);
        ProgressRequestBody cropFileBody = new ProgressRequestBody(profileOriginalImageCompressedFile, getMimeType(cropFilePath), this);
        MultipartBody.Part cropFilePart = MultipartBody.Part.createFormData("profil_photo", crop.getName().replaceAll("[^a-zA-Z0-9.]", ""), cropFileBody);

        Map<String, RequestBody> params = new HashMap<>();
        params.put("id", partParam1);
        params.put("user_agent", partParam2);
        params.put("csrf_new_matrimonial", partParam3);

        long fileSizeMB = common.getFIleSizeInMB(profileOriginalImageCompressedFile);
        if (fileSizeMB > MAX_IMAGE_SIZE_IN_MB_TO_UPLOAD) {
            Toast.makeText(RegisterPhotoUploadActivity.this, "Image size more than " + MAX_IMAGE_SIZE_IN_MB_TO_UPLOAD + " MB", Toast.LENGTH_SHORT).show();
        } else {
            call = appApiService.uploadPhoto(cropFilePart, originalFilePart, params);
        }

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                common.hideProgressRelativeLayout(loader);

                JsonObject data = response.body();
                AppDebugLog.print("response in submitData : " + response.body());

                if (data != null) {
                    common.showToast(data.get("errmessage").getAsString());
                    if (data.get("status").getAsString().equals("success")) {
                        isImageSelect = false;
                        Intent i = new Intent(RegisterPhotoUploadActivity.this, SuccessActivity.class);
                        i.putExtra("ragistered_id", registerId);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(RegisterPhotoUploadActivity.this, getString(R.string.err_msg_try_again_later), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(RegisterPhotoUploadActivity.this, getString(R.string.err_msg_something_went_wrong), Toast.LENGTH_LONG).show();
                common.hideProgressRelativeLayout(loader);
            }
        });
    }

    public String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            openFileChooser();
        } else if (requestCode == CROP_PIC) {
            if (resultCode == Activity.RESULT_OK) {
                cropUri = UCrop.getOutput(data);
                cropFilePath = cropUri.getPath();

                AppDebugLog.print("cropUri.path : " + cropUri.getPath());
                compressedFile = common.getCompressedImageFile(this, new File(cropUri.getPath()));

                //show image in image view
                Picasso.get().load(cropUri).error(R.drawable.placeholder).placeholder(
                        R.drawable.placeholder).into(img_profile);

                isImageSelect = true;

            }
        } else {
            easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {

                @Override
                public void onMediaFilesPicked(MediaFile[] mediaFiles, MediaSource mediaSource) {
                    for (MediaFile mediaFile : mediaFiles) {
                        AppDebugLog.print("file : " + mediaFile.getFile().getAbsolutePath());
                        switch (mediaSource) {
                            case CAMERA_IMAGE:
                            case GALLERY:
                                originalFile = mediaFile.getFile();
                                originalFilePath = mediaFile.getFile().getPath();
                                cropImage(mediaFile.getFile());
                                break;

                            default:
                                cropImage(mediaFile.getFile());
                        }
                    }
                }

                @Override
                public void onImagePickerError(Throwable error, MediaSource source) {
                    super.onImagePickerError(error, source);
                }
            });
        }
    }

    private void cropImage(File attachmentFile) {
        Uri uri = Uri.fromFile(attachmentFile);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageExtension = Common.getExtensionFromPath(Common.getPath(RegisterPhotoUploadActivity.this, uri));
        AppDebugLog.print("imageExtension : " + imageExtension);

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), timeStamp + imageExtension)));
        uCrop.withAspectRatio(2, 3);
        uCrop.withMaxResultSize(512, 620);
        UCrop.Options options = new UCrop.Options();

        options.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        options.setToolbarWidgetColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        options.setRootViewBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        uCrop.withOptions(options);
        uCrop.start(this, CROP_PIC);

    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent(this, SuccessActivity.class);
        i.putExtra("ragistered_id", registerId);
        startActivity(i);
        finish();
    }
}