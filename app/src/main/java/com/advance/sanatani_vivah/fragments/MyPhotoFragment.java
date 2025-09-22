package com.advance.sanatani_vivah.fragments;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.retrofit.AppApiService;
import com.advance.sanatani_vivah.retrofit.ProgressRequestBody;
import com.advance.sanatani_vivah.retrofit.RetrofitClient;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.ContentUriUtils;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
import timber.log.Timber;

public class MyPhotoFragment extends Fragment implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {
    private Common common;
    private SessionManager session;
    private Context context;
    private ProgressDialog pd;
    private RelativeLayout loader;
    private TextView tv_cancel, tv_gallary, tv_camera;
    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private int image_id;
    private Uri resultUri;
    private long totalSize = 0;
    private boolean isfirst = true;
    private ImageView img_one, img_two, img_three, img_four, img_five, img_six,
            img_plus_one, img_plus_two, img_plus_three, img_plus_four, img_plus_five, img_plus_six,
            img_edit_one, img_edit_two, img_edit_three, img_edit_four, img_edit_five, img_edit_six, img_cover, img_edit_cover, img_plus_cover;
    private int placeHolder, photoProtectPlaceHolder;

    private int coverImageHeight = 0;
    private int coverImageWidth = 0;
    private int smallImageW = 0;
    private int bigImageW = 0;
    private int smallImageH = 0;
    private int bigImageH = 0;

    private EasyImage easyImage = null;
    private final int PERMISSION_REQUEST_CODE = 122;
    private File compressedFile = null;
    private File originalFile = null;
    private String originalFilePath = "", cropFilePath = "";
    private Uri cropUri;
    private final int CROP_PIC = 3;

    final private int SELECT_PICTURE = 201;

    public MyPhotoFragment() {
        // Required empty public constructor
    }

    public static MyPhotoFragment newInstance(String param1, String param2) {
        MyPhotoFragment fragment = new MyPhotoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_photo, container, false);

        context = getActivity();
        session = new SessionManager(context);
        common = new Common(getActivity());

        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
            placeHolder = R.drawable.female;
        } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
            placeHolder = R.drawable.male;
        }

        setUpEasyImage();

        int deviceWidth = Common.getDisplayWidth(getActivity());
        int smallWidth = deviceWidth / 3;
        int bigWidth = (int) ((deviceWidth / 3) * 2);
        coverImageHeight = Common.convertDpToPixels(200, getActivity());
        coverImageWidth = deviceWidth - Common.convertDpToPixels(10, getActivity());
        smallImageW = smallWidth; //Common.convertDpToPixels(smallWidth, getActivity());
        bigImageW = bigWidth; //Common.convertDpToPixels(bigWidth, getActivity());
        smallImageH = (int) (smallImageW * 1.210);
        bigImageH = (int) (bigImageW * 1.210);

        loader = view.findViewById(R.id.loader);
        img_cover = view.findViewById(R.id.img_cover);
        img_edit_cover = view.findViewById(R.id.img_edit_cover);
        img_edit_cover.setOnClickListener(this);
        img_plus_cover = view.findViewById(R.id.img_plus_cover);
        img_plus_cover.setOnClickListener(this);

        img_one = view.findViewById(R.id.img_one);
        img_two = view.findViewById(R.id.img_two);
        img_three = view.findViewById(R.id.img_three);
        img_four = view.findViewById(R.id.img_four);
        img_five = view.findViewById(R.id.img_five);
        img_six = view.findViewById(R.id.img_six);

        img_plus_one = view.findViewById(R.id.img_plus_one);
        img_plus_one.setOnClickListener(this);
        img_plus_two = view.findViewById(R.id.img_plus_two);
        img_plus_two.setOnClickListener(this);
        img_plus_three = view.findViewById(R.id.img_plus_three);
        img_plus_three.setOnClickListener(this);
        img_plus_four = view.findViewById(R.id.img_plus_four);
        img_plus_four.setOnClickListener(this);
        img_plus_five = view.findViewById(R.id.img_plus_five);
        img_plus_five.setOnClickListener(this);
        img_plus_six = view.findViewById(R.id.img_plus_six);
        img_plus_six.setOnClickListener(this);

        img_edit_one = view.findViewById(R.id.img_edit_one);
        img_edit_one.setOnClickListener(this);
        img_edit_two = view.findViewById(R.id.img_edit_two);
        img_edit_two.setOnClickListener(this);
        img_edit_three = view.findViewById(R.id.img_edit_three);
        img_edit_three.setOnClickListener(this);
        img_edit_four = view.findViewById(R.id.img_edit_four);
        img_edit_four.setOnClickListener(this);
        img_edit_five = view.findViewById(R.id.img_edit_five);
        img_edit_five.setOnClickListener(this);
        img_edit_six = view.findViewById(R.id.img_edit_six);
        img_edit_six.setOnClickListener(this);

        layoutBottomSheet = view.findViewById(R.id.bottom_sheet);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_gallary = view.findViewById(R.id.tv_gallary);
        tv_camera = view.findViewById(R.id.tv_camera);
        tv_gallary.setOnClickListener(this);
        tv_camera.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
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
        getMyProfile();
        return view;
    }

    private void setUpEasyImage() {
        easyImage = new EasyImage.Builder(getActivity())
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

    private void openFileChooser(int pickFor) {
        requestPermission(pickFor);
    }

    @AfterPermissionGranted(122)
    private void requestPermission(int pickFor) {
        if (!checkPermission()) {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    "This needs permission to use feature. You can grant them in app settings.",
                    PERMISSION_REQUEST_CODE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        } else {
            pickImage(pickFor);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    //TODO Permission related
    private boolean checkPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            return true;
        } else {
            return false;
        }
    }

    private void getMyProfile() {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));

        common.makePostRequest(AppConstants.get_my_profile, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                session.setUserData(SessionManager.TOKEN, object.getString("tocken"));
                if (object.getString("status").equals("success")) {
                    JSONObject data = object.getJSONObject("data");

                    String photo1 = data.getString("photo1");
                    String photo2 = data.getString("photo2");
                    String photo3 = data.getString("photo3");
                    String photo4 = data.getString("photo4");
                    String photo5 = data.getString("photo5");
                    String photo6 = data.getString("photo6");
                    String cover_photo = data.getString("cover_photo");

                    if (isValidImage(cover_photo)) {
                        Picasso.get().load(cover_photo).resize(coverImageWidth, coverImageHeight).centerInside().placeholder(R.drawable.ic_cover_place_holder).into(img_cover);
                        img_plus_cover.setVisibility(View.GONE);
                        img_edit_cover.setVisibility(View.VISIBLE);
                    } else {
                        img_plus_cover.setVisibility(View.VISIBLE);
                        img_edit_cover.setVisibility(View.GONE);
                        img_cover.setImageResource(R.drawable.ic_cover_place_holder);
                    }

                    if (isValidImage(photo1)) {
                        Picasso.get().load(photo1).resize(bigImageW, bigImageH).centerCrop(Gravity.TOP | Gravity.START).placeholder(placeHolder).error(placeHolder).into(img_one);
                        img_plus_one.setVisibility(View.GONE);
                        img_edit_one.setVisibility(View.VISIBLE);
                    } else {
                        img_plus_one.setVisibility(View.VISIBLE);
                        img_edit_one.setVisibility(View.GONE);
                        img_one.setImageResource(placeHolder);
                        //img_one.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                    }
                    if (isValidImage(photo2)) {
                        Picasso.get().load(photo2).resize(smallImageW, smallImageH).centerCrop(Gravity.TOP | Gravity.START).placeholder(placeHolder).error(placeHolder).into(img_two);
                        img_plus_two.setVisibility(View.GONE);
                        img_edit_two.setVisibility(View.VISIBLE);
                    } else {
                        img_plus_two.setVisibility(View.VISIBLE);
                        img_edit_two.setVisibility(View.GONE);
                        img_two.setImageResource(placeHolder);
                        //img_two.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                    }
                    if (isValidImage(photo3)) {
                        Picasso.get().load(photo3).resize(smallImageW, smallImageH).centerCrop(Gravity.TOP | Gravity.START).placeholder(placeHolder).error(placeHolder).into(img_three);
                        img_plus_three.setVisibility(View.GONE);
                        img_edit_three.setVisibility(View.VISIBLE);
                    } else {
                        img_plus_three.setVisibility(View.VISIBLE);
                        img_edit_three.setVisibility(View.GONE);
                        img_three.setImageResource(placeHolder);
                        //img_three.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                    }
                    if (isValidImage(photo4)) {
                        Picasso.get().load(photo4).resize(smallImageW, smallImageH).centerCrop(Gravity.TOP | Gravity.START).placeholder(placeHolder).error(placeHolder).into(img_four);
                        img_plus_four.setVisibility(View.GONE);
                        img_edit_four.setVisibility(View.VISIBLE);
                    } else {
                        img_plus_four.setVisibility(View.VISIBLE);
                        img_edit_four.setVisibility(View.GONE);
                        img_four.setImageResource(placeHolder);
                        //img_four.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                    }
                    if (isValidImage(photo5)) {
                        Picasso.get().load(photo5).resize(smallImageW, smallImageH).centerCrop(Gravity.TOP | Gravity.START).placeholder(placeHolder).error(placeHolder).into(img_five);
                        img_plus_five.setVisibility(View.GONE);
                        img_edit_five.setVisibility(View.VISIBLE);
                    } else {
                        img_plus_five.setVisibility(View.VISIBLE);
                        img_edit_five.setVisibility(View.GONE);
                        img_five.setImageResource(placeHolder);
                        //img_five.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                    }
                    if (isValidImage(photo6)) {
                        Picasso.get().load(photo6).resize(smallImageW, smallImageH).centerCrop(Gravity.TOP | Gravity.START).placeholder(placeHolder).error(placeHolder).into(img_six);
                        img_plus_six.setVisibility(View.GONE);
                        img_edit_six.setVisibility(View.VISIBLE);
                    } else {
                        img_plus_six.setVisibility(View.VISIBLE);
                        img_edit_six.setVisibility(View.GONE);
                        img_six.setImageResource(placeHolder);
                        //img_six.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                    }

                }
                isfirst = false;

            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    private boolean isValidImage(String url) {
        return !url.equals("") && !url.equals("null");
    }

    private void editClick(final int img_id, View v) {
        image_id = img_id;
        PopupMenu popup = new PopupMenu(context, v);
        popup.inflate(R.menu.photo_edit_menu);
        if (img_id == 1 || img_id == 0)
            popup.getMenu().getItem(2).setVisible(false);

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(Intent.createChooser(intent,"Select Photo"), SELECT_PICTURE);
                    }else{
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    return true;
                case R.id.delete:
                    deletePhotoAlert();
                    return true;
                case R.id.profile:
                    setProfilePhotoApi();
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

    private void deletePhotoAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("Are you sure you want to delete photo?");
        alert.setNegativeButton("No", null);
        alert.setPositiveButton("Yes", (dialogInterface, i) -> {
            if (image_id != 0) {
                deletePhotoApi();
            } else {
                deleteCoverPhotoApi();
            }

        });
        alert.show();
    }

    private void deleteCoverPhotoApi() {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", session.getLoginData(SessionManager.KEY_USER_ID));
        param.put("delete_cover_photo", "delete");

        common.makePostRequest(AppConstants.delete_cover_photo, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                if (object.getString("status").equals("success")) {
                    getMyProfile();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    private void deletePhotoApi() {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        param.put("photo_number", String.valueOf(image_id));
        param.put("delete_photo", "delete");

        common.makePostRequest(AppConstants.delete_photo, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                if (object.getString("status").equals("success")) {
                    getMyProfile();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    private void setProfilePhotoApi() {
        common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        param.put("photo_number", String.valueOf(image_id));
        param.put("set_profile", "set_profile");

        common.makePostRequest(AppConstants.set_profile_pic, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                if (object.getString("status").equals("success")) {
                    getMyProfile();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                common.showToast(getString(R.string.err_msg_try_again_later));
            }
        }, error -> {
            common.hideProgressRelativeLayout(loader);
            if (error.networkResponse != null) {
                common.showToast(Common.getErrorMessageFromErrorCode(error.networkResponse.statusCode));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppDebugLog.print("resultCode : " + resultCode);
        AppDebugLog.print("requestCode : " + requestCode);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            openFileChooser(200);
        } else if(requestCode == SELECT_PICTURE){
            if (resultCode == Activity.RESULT_OK && data != null) {
                AppDebugLog.print("image pick : "+data);
                try {
                    String tempPath = ContentUriUtils.INSTANCE.getFilePath(requireActivity(), data.getData());
                    AppDebugLog.print("tempPath : "+tempPath);
                    originalFile = new File(tempPath);
                    originalFilePath = tempPath;
                    if (image_id != 0) {
                        cropImage(originalFile);
                    }else{
                        uploadFileToServer();
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    AppDebugLog.print("Error in image pick1");
                }
            }else{
                AppDebugLog.print("Error in image pick2");
            }
        } else if (requestCode == CROP_PIC) {
            AppDebugLog.print("resultCode in CROP_PIC: " + resultCode);
            AppDebugLog.print("requestCode  in CROP_PIC: " + requestCode);
            if (resultCode == Activity.RESULT_OK) {
                cropUri = UCrop.getOutput(data);
                cropFilePath = cropUri.getPath();

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), cropUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                switch (image_id) {
                    case 1:
                        img_one.setImageBitmap(bitmap);
                        break;
                    case 2:
                        img_two.setImageBitmap(bitmap);
                        break;
                    case 3:
                        img_three.setImageBitmap(bitmap);
                        break;
                    case 4:
                        img_four.setImageBitmap(bitmap);
                        break;
                    case 5:
                        img_five.setImageBitmap(bitmap);
                        break;
                    case 6:
                        img_six.setImageBitmap(bitmap);
                        break;
                }
                uploadFileToServer();
            }
        } else {
            easyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
                @Override
                public void onMediaFilesPicked(MediaFile[] mediaFiles, MediaSource mediaSource) {
                    for (MediaFile mediaFile : mediaFiles) {
                        AppDebugLog.print("file : " + mediaFile.getFile().getAbsolutePath());
                        switch (mediaSource) {
                            case DOCUMENTS:
                            case CAMERA_IMAGE:
                            case GALLERY:
                                originalFile = mediaFile.getFile();
                                originalFilePath = mediaFile.getFile().getPath();
                                if (image_id != 0) {
                                    cropImage(originalFile);
                                }else{
                                    uploadFileToServer();
                                }
                                break;
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

    private void uploadFileToServer() {
        if (!ConnectionDetector.isConnectingToInternet(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.err_msg_no_intenet_connection), Toast.LENGTH_LONG).show();
            return;
        }

        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        // setting progress bar to zero
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Uploading...");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setProgress(0);
        pd.setCancelable(false);
        pd.show();

        RequestBody partParam1 = RequestBody.create(MediaType.parse("text/plain"), session.getLoginData(SessionManager.KEY_USER_ID));
        RequestBody partParam2 = RequestBody.create(MediaType.parse("text/plain"), "NI-AAPP");
        RequestBody partParam3 = RequestBody.create(MediaType.parse("text/plain"), session.getLoginData(SessionManager.TOKEN));

        Map<String, RequestBody> params = new HashMap<>();
        params.put("member_id", partParam1);
        params.put("user_agent", partParam2);
        params.put("csrf_new_matrimonial", partParam3);



        Retrofit retrofit = RetrofitClient.getClient();
        AppApiService appApiService = retrofit.create(AppApiService.class);

        Call<JsonObject> call = null;
        if (image_id != 0) {
            String org_param = "profile_photo" + image_id + "_org";
            String crop_param = "profile_photo" + image_id + "_crop";
            AppDebugLog.print("org_param: " + org_param + "  crop_param   " + crop_param);

            File sourceFile_crop = new File(cropFilePath);
            File sourceFile = new File(originalFilePath);
            ProgressRequestBody cropFileBody = new ProgressRequestBody(sourceFile_crop, getMimeType(cropFilePath), this);
            MultipartBody.Part cropFilePart = MultipartBody.Part.createFormData(crop_param,
                    sourceFile_crop.getName().replaceAll("[^a-zA-Z0-9.]", ""), cropFileBody);

            File profileOriginalImageCompressedFile = Common.getCompressedImageFile(getActivity(), sourceFile);
            AppDebugLog.print("profileOriginalImageCompressedFile name : " + profileOriginalImageCompressedFile.getName().replaceAll("[^a-zA-Z0-9.]", ""));
            ProgressRequestBody originalFileBody = new ProgressRequestBody(profileOriginalImageCompressedFile, getMimeType(profileOriginalImageCompressedFile.getAbsolutePath()), this);
            MultipartBody.Part originalFilePart = MultipartBody.Part.createFormData(org_param,
                    profileOriginalImageCompressedFile.getName().replaceAll("[^a-zA-Z0-9.]", ""),
                    originalFileBody);

            call = appApiService.uploadMyPhotoWithCrop(cropFilePart, originalFilePart, params);
        } else {
            File sourceFile = new File(originalFilePath);

            File profileOriginalImageCompressedFile = Common.getCompressedImageFile(getActivity(), sourceFile);
//            Toast.makeText(context, String.valueOf(profileOriginalImageCompressedFile.length() + "   " + sourceFile.length()), Toast.LENGTH_LONG).show();
            ProgressRequestBody orgFileBody = new ProgressRequestBody(profileOriginalImageCompressedFile, getMimeType(profileOriginalImageCompressedFile.getAbsolutePath()), this);
            MultipartBody.Part orgFilePart = MultipartBody.Part.createFormData("cover_photo", profileOriginalImageCompressedFile.getName().replaceAll("[^a-zA-Z0-9.]", ""), orgFileBody);

            call = appApiService.uploadMyPhotoWithoutCrop(orgFilePart, params);
        }


        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

              //  Toast.makeText(context, String.valueOf(call..toString()), Toast.LENGTH_LONG).show();

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }


                JsonObject data = response.body();
                AppDebugLog.print("response in submitData : " + response.body());

                if (data != null) {

                    common.showToast(data.get("errmessage").getAsString());
                    if (data.get("status").getAsString().equals("success")) {
                        getMyProfile();
                    }
                } else {
                   //String d = new Gson().toJson(response.code());
                    Log.i("datatttttttt", session.getLoginData(SessionManager.KEY_USER_ID) + response.code() +session.getLoginData(SessionManager.TOKEN));

                    Toast.makeText(getActivity(), getString(R.string.err_msg_try_again_later), Toast.LENGTH_LONG).show();
                 // Toast.makeText(getActivity(), "new Gson().toJson(response)", Toast.LENGTH_LONG).show();




                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.err_msg_something_went_wrong), Toast.LENGTH_LONG).show();
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });
    }

    // url = file path or whatever suitable URL you want.
    public String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    public void onProgressUpdate(int percentage) {
        // set current progress
        if (pd != null && pd.isShowing()) {
            pd.setProgress(percentage);
        }

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        //set finish progress
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void cropImage(File attachmentFile) {
        Uri uri = Uri.fromFile(attachmentFile);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageExtension = Common.getExtensionFromPath(Common.getPath(getActivity(), uri));
        AppDebugLog.print("imageExtension : " + imageExtension);

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getActivity().getCacheDir(), timeStamp + imageExtension)));
        uCrop.withAspectRatio(2, 3);
        uCrop.withMaxResultSize(512, 620);
        UCrop.Options options = new UCrop.Options();

        options.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        options.setToolbarWidgetColor(ContextCompat.getColor(getActivity(), R.color.white));
        options.setRootViewBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        uCrop.withOptions(options);
        uCrop.start(context, this, CROP_PIC);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.tv_camera:
                openFileChooser(200);
                break;
            case R.id.tv_gallary:
                openFileChooser(100);
                break;
            case R.id.img_plus_one:
                image_id = 1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent,"Select Photo"), SELECT_PICTURE);
                }else{
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.img_plus_two:
                image_id = 2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent,"Select Photo"), SELECT_PICTURE);
                }else{
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.img_plus_three:
                image_id = 3;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent,"Select Photo"), SELECT_PICTURE);
                }else{
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.img_plus_four:
                image_id = 4;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent,"Select Photo"), SELECT_PICTURE);
                }else{
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.img_plus_five:
                image_id = 5;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent,"Select Photo"), SELECT_PICTURE);
                }else{
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.img_plus_six:
                image_id = 6;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent,"Select Photo"), SELECT_PICTURE);
                }else{
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.img_plus_cover:
                image_id = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent,"Select Photo"), SELECT_PICTURE);
                }else{
                    openFileChooser(100);
                }
                break;
            case R.id.img_edit_cover:
                editClick(0, view);
                break;
            case R.id.img_edit_one:
                editClick(1, view);
                break;
            case R.id.img_edit_two:
                editClick(2, view);
                break;
            case R.id.img_edit_three:
                editClick(3, view);
                break;
            case R.id.img_edit_four:
                editClick(4, view);
                break;
            case R.id.img_edit_five:
                editClick(5, view);
                break;
            case R.id.img_edit_six:
                editClick(6, view);
                break;
        }
    }
}
