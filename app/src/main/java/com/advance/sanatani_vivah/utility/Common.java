package com.advance.sanatani_vivah.utility;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.advance.sanatani_vivah.application.MyApplication;
import com.advance.sanatani_vivah.custom.BadgeDrawable;
import com.advance.sanatani_vivah.model.VendorParentModel;
import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.tapadoo.alerter.Alerter;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;

import static android.os.Environment.getExternalStorageDirectory;

public class Common {

    private static Context context;
    SessionManager session;
    JSONObject dataStr;

    public Common(Context context) {
        this.context = context;
        session = new SessionManager(context);
    }

    public static String getJsonFromAssets(Context context, String fileName) {

        String jsonString = "";

        try {
            AssetManager manager = context.getAssets();
            InputStream file = manager.open(fileName);
            byte[] formArray = new byte[file.available()];
            file.read(formArray);
            file.close();
            jsonString = new String(formArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static void showToast(String m) {
        Toast.makeText(context, m, Toast.LENGTH_LONG).show();
    }

    public void showAlert(String title, String message, int icon) {
        Alerter.create((Activity) context)
                .setTitle(title)
                .setText(message)
                .setIcon(icon)
                .setDuration(4000)
                .setBackgroundColorRes(R.color.colorAccent)
                .show();

//        MySnake.with((Activity) context)
//                .setTitle(title, R.color.red)
//                .setTitleSize(16)
//                .setMessage(message, R.color.red)
//                .setMessageSize(14)
//                .setDuration(4000)
//                .autoHide(true)
//                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//                .setIcon(icon)
//                .setCornerRadius(10, 10)
//                .sneak(R.color.white);
    }

    public String getAge(String dobString, SimpleDateFormat sdf) {
        if (dobString.equals("null")) {
            return "";
        }
        Date date = null;
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) return "0";

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return String.valueOf(age);
    }

    public String validImage(String url, String approve, String photo_protect, String photo_view_status) {
        String resp = "";
        switch (photo_view_status) {
            case "0":
                if (photo_protect.equals("Yes")) {
                    if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
                        resp = "male_password";
                    } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
                        resp = "female_password";
                    }

                } else {
                    if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
                        resp = "male";
                    } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
                        resp = "female";
                    }
                }
                break;
            case "1":
                if (photo_protect.equals("No")) {
                    if (!url.equals("")) {
                        if (approve.equals("UNAPPROVED")) {
                            if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
                                resp = "male";
                            } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
                                resp = "female";
                            }
                        } else {
                            resp = "url";
                        }
                    } else {
                        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
                            resp = "male";
                        } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
                            resp = "female";
                        }
                    }

                } else if (photo_protect.equals("Yes")) {
                    if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
                        resp = "male_password";
                    } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
                        resp = "female_password";
                    }
                }

                break;
            case "2":
                if (session.getLoginData(SessionManager.KEY_PLAN_STATUS).equals("Paid")) {
                    if (photo_protect.equals("No")) {
                        if (!url.equals("")) {
                            if (approve.equals("UNAPPROVED")) {
                                if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
                                    resp = "male";
                                } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
                                    resp = "female";
                                }
                            } else {
                                resp = "url";
                            }
                        } else {
                            if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
                                resp = "male";
                            } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
                                resp = "female";
                            }
                        }
                    } else if (photo_protect.equals("Yes")) {
                        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
                            resp = "male_password";
                        } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
                            resp = "female_password";
                        }
                    }
                } else {
                    if (photo_protect.equals("Yes")) {
                        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
                            resp = "male_password";
                        } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
                            resp = "female_password";
                        }
                    } else {
                        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
                            resp = "male";
                        } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
                            resp = "female";
                        }
                    }
                }

                break;
        }
        return resp;
    }

    public String imageCheck(String approve) {
        String resp = "";
        if (approve.equals("UNAPPROVED")) {
            if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
                resp = "male";
            } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
                resp = "female";
            }
        } else {
            resp = "url";
        }
        return resp;
    }

    public String calculateHeight(String height) {
        String res = "";
        if (!height.equals("") && !height.equals("null")) {
            if (height.equals("48")) {
                res = "Below 4ft";
            } else if (height.equals("85")) {
                res = "Above 7ft";
            } else {
                try {
                    int foot = Integer.parseInt(height) / 12;
                    int inch = Integer.parseInt(height) % 12;
                    if (inch > 0) {
                        res = foot + "ft " + inch + "in";
                    } else {
                        res = foot + "ft";
                    }
                } catch (Exception e) {
                    return height;
                }
            }
        }
        return res;
    }

    public void setDrawableLeftEditText(int icon, EditText v) {
        Drawable img_white = context.getResources().getDrawable(icon);
        int drawableSize = convertDpToPixels(AppConstants.DRAWABLE_SIZE, context);
        img_white.setBounds(0, 0, drawableSize, drawableSize);
        v.setCompoundDrawables(img_white, null, null, null);
    }

    public void setDrawableLeftButton(int icon, Button v) {
        Drawable img_white = context.getResources().getDrawable(icon);
        int drawableSize = convertDpToPixels(AppConstants.DRAWABLE_SIZE, context);
        img_white.setBounds(0, 0, drawableSize, drawableSize);
        v.setCompoundDrawables(img_white, null, null, null);
    }

    public void setDrawableLeftTextView(int icon, TextView v) {
        Drawable img_white = context.getResources().getDrawable(icon);
        int drawableSize = convertDpToPixels(AppConstants.DRAWABLE_SIZE, context);
        img_white.setBounds(0, 0, drawableSize, drawableSize);
        v.setCompoundDrawables(null, null, img_white, null);
    }

    public void setDrawableRightRadio(int icon, RadioButton v) {
        Drawable img_white = context.getResources().getDrawable(icon);
        int drawableSize = convertDpToPixels(150, context);
        img_white.setBounds(0, 0, drawableSize, 80);
        v.setCompoundDrawables(null, null, img_white, null);
    }

    public void setDrawableLeftTextViewLeft(int icon, TextView v) {
        Drawable img_white = context.getResources().getDrawable(icon);
        int drawableSize = convertDpToPixels(AppConstants.DRAWABLE_SIZE, context);
        img_white.setBounds(0, 0, drawableSize, drawableSize);
        v.setCompoundDrawables(img_white, null, null, null);
    }

    public void setDrawableLeftTextViewLefttab(int icon, TextView v) {
        Drawable img_white = context.getResources().getDrawable(icon);
        int drawableSize = convertDpToPixels(AppConstants.DRAWABLE_SIZE, context);
        img_white.setBounds(0, 0, drawableSize, drawableSize);
        v.setCompoundDrawablePadding(convertDpToPixels(5, context));
        v.setCompoundDrawables(img_white, null, null, null);
    }

    public static int convertDpToPixels(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public void setSelection(Spinner spinner, List<String> array, String selected) {
        if (!selected.equals("")) {
            for (int i = 0; i < array.size(); i++) {
                if (array.get(i).equals(selected)) {
                    spinner.setSelection(i);
                }
            }
        } else {
            spinner.setSelection(0);
        }
    }

    public JSONArray getArray(String keyName) throws JSONException {
        dataStr = MyApplication.getSpinData();
        JSONObject obj = new JSONObject();
        Iterator<String> iter = dataStr.keys();
        while (iter.hasNext()) {
            //  Log.d("TokenRes", iter.toString());
            String key = iter.next();
            if (key.equals(keyName)) {
                try {
                    Object value = dataStr.get(key);
                    obj.put(key, value);
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }

        }

        return obj.getJSONArray(keyName);
    }

    //30122019 nasirali for new searchable spinner
    public static List<KeyPairBoolData> getSpinnerListFromArray(JsonArray arr) throws JsonIOException {
        List<KeyPairBoolData> list = new ArrayList<>();

        for (int i = 0; i < arr.size(); i++) {
            JsonObject obj = arr.get(i).getAsJsonObject();
            if (obj.get("val").getAsString().equals("Select Option")) continue;
            KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
            keyPairBoolData.setId(obj.get("id").getAsString());
            keyPairBoolData.setSelected(false);
            keyPairBoolData.setName(obj.get("val").getAsString());
            keyPairBoolData.setObject(obj);
            list.add(keyPairBoolData);
        }
        return list;
    }

    public static List<KeyPairBoolData> getDefaultSpinnerListFromArray(String title) {
        List<KeyPairBoolData> list = new ArrayList<>();
        KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
        keyPairBoolData.setId("0");
        keyPairBoolData.setSelected(false);
        keyPairBoolData.setName("Select " + title);
        keyPairBoolData.setObject(keyPairBoolData);
        list.add(keyPairBoolData);

        return list;
    }

    public static List<KeyPairBoolData> getSpinnerListFromArrayWithAll(JsonArray arr) throws JsonIOException {
        List<KeyPairBoolData> list = new ArrayList<>();

        for (int i = 0; i < arr.size(); i++) {
            JsonObject obj = arr.get(i).getAsJsonObject();
            KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
            keyPairBoolData.setId(obj.get("id").getAsString());
            keyPairBoolData.setSelected(false);
            keyPairBoolData.setName(obj.get("val").getAsString());
            keyPairBoolData.setObject(obj);
            list.add(keyPairBoolData);
        }
        return list;
    }

    public List<String> getListFromArray(JSONArray arr, String title) throws JSONException {
        // JSONArray arr=MyApplication.getSpinData().getJSONArray(name);
        List<String> list = new ArrayList<>();
        list.add("" + title);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (!obj.getString("val").equals("Select Option"))
                list.add(obj.getString("val"));
        }
        return list;
    }

    public List<String> getListFromArray_id(JSONArray arr, String title) throws JSONException {
        // JSONArray arr=MyApplication.getSpinData().getJSONArray(name);
        List<String> list = new ArrayList<>();
        list.add("Select " + title);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            list.add(obj.getString("id"));
        }
        return list;
    }

    public List<String> getListFromArray(JSONArray arr) throws JSONException {
        // JSONArray arr=MyApplication.getSpinData().getJSONArray(name);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            list.add(obj.getString("val"));
        }
        return list;
    }

    public List<String> getListFromArrayId(JSONArray arr) throws JSONException {
        // JSONArray arr=MyApplication.getSpinData().getJSONArray(name);
        List<String> list = new ArrayList<>();
        list.add("0");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            if (!obj.getString("val").equals("Select Option"))
                list.add(obj.getString("id"));
        }
        return list;
    }

    public String changeDate(String time, String outputPattern) {

        //String outputPattern = "h:mm a,dd MMMM, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        // TimeZone tz = TimeZone.getTimeZone(session.getLoginData(SessionManager.TIME_ZONE));
        // outputFormat.setTimeZone(tz);

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

    public HashMap<String, String> getMapFromArray(JSONArray arr, String title) throws JSONException {
        //JSONArray arr=MyApplication.getSpinData().getJSONArray(name);
        HashMap<String, String> list = new HashMap<>();
        if (title.equals("Total Children")) {
            list.put("Select " + title, "total");
        } else
            list.put("Select " + title, "0");

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            list.put(obj.getString("val"), obj.getString("id"));
        }
        return list;
    }

    public void spinnerSetError(Spinner s, String m) {
        ((TextView) s.getSelectedView()).setError(m);
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isValidMobileNumber(String mobile) {
        boolean isValid = true;
        if (mobile == null) {
            isValid = false;
        } else if (mobile.length() < 7 || mobile.length() > 13) {
            isValid = false;
        } else {
            isValid = true;
        }

        return isValid;
    }

    public void makePostRequestWithTag(String url, final HashMap<String, String> param, Response.Listener<String> listener, Response.ErrorListener errorListener, String tag) {
        if (!ConnectionDetector.isConnectingToInternet(context)) {
            Toast.makeText(context, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        AppDebugLog.print("Api Url : " + url);

        StringRequest strRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                param.put("user_agent", AppConstants.USER_AGENT);
                param.put("csrf_new_matrimonial", session.getLoginData(SessionManager.TOKEN));
                param.put("logged_in_user_id", session.getLoginData(SessionManager.KEY_USER_ID));

                for (String name : param.keySet()) {
                    String key = name.toString();
                    String value = param.get(name).toString();
                    AppDebugLog.print("params : " + key + ":" + value + "\n");
                }
                return param;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                AppConstants.REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strRequest.setRetryPolicy(mRetryPolicy);


        MyApplication.getInstance().addToRequestQueue(strRequest, tag);
    }

    public void makePostRequest(String url, final HashMap<String, String> param, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        if (!ConnectionDetector.isConnectingToInternet(context)) {
            Toast.makeText(context, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        AppDebugLog.print("Api Url : " + url);

        StringRequest strRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                param.put("user_agent", AppConstants.USER_AGENT);
                param.put("csrf_new_matrimonial", session.getLoginData(SessionManager.TOKEN));
                param.put("logged_in_user_id", session.getLoginData(SessionManager.KEY_USER_ID));

                for (String name : param.keySet()) {
                    String key = name.toString();
                    String value = param.get(name).toString();
                    AppDebugLog.print("params : " + key + ":" + value + "\n");
                }
                return param;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                AppConstants.REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strRequest.setRetryPolicy(mRetryPolicy);


        MyApplication.getInstance().addToRequestQueue(strRequest, "req");
    }

    public void makePostRequestTime(String url, final HashMap<String, String> param, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        if (!ConnectionDetector.isConnectingToInternet(context)) {
            Toast.makeText(context, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            return;
        }

        AppDebugLog.print("Api Url : " + url);

        StringRequest strRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                param.put("user_agent", AppConstants.USER_AGENT);
                param.put("csrf_new_matrimonial", session.getLoginData(SessionManager.TOKEN));
                param.put("logged_in_user_id", session.getLoginData(SessionManager.KEY_USER_ID));

                for (String name : param.keySet()) {
                    String key = name.toString();
                    String value = param.get(name).toString();
                    AppDebugLog.print("params : " + key + ":" + value + "\n");
                }

                return param;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                AppConstants.REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strRequest.setRetryPolicy(mRetryPolicy);

        MyApplication.getInstance().addToRequestQueue(strRequest, "req");
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    public static String get12HrTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        return new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
    }

    public static final String GSONDateTimeFormat = "MMM dd, yyyy hh:mm:ss a";

    //bean object convert in to json string using gson
    public static String getJsonStringFromObject(HashMap<String, String> beanObject) {
        Gson gson = new GsonBuilder().setDateFormat(GSONDateTimeFormat).create();
        return gson.toJson(beanObject);
    }

    //get bean object from json string using gson
    public static HashMap<String, String> getBeanObjectFromJsonString(String json) {
        Gson gson = new GsonBuilder().setDateFormat(GSONDateTimeFormat).create();
        return gson.fromJson(json, HashMap.class);
    }

    private static String getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024) + " mb";
    }

    private static String getFileSizeKiloBytes(File file) {
        return (double) file.length() / 1024 + "  kb";
    }

    private static String getFileSizeBytes(File file) {
        return file.length() + " bytes";
    }

    public static File getCompressedImageFile(Context context, File imageFile) {

        AppDebugLog.print("imageFile size before compressed : " + getFileSizeMegaBytes(imageFile));
        File compressedImage = null;
        AppDebugLog.print("create file path in getCompressedImageFile :" + getFilePathAsPerFileName1(getFileNameFromfilePath(imageFile.getAbsolutePath())));
        File tempFile = new File(getFilePathAsPerFileName1(getFileNameFromfilePath(imageFile.getAbsolutePath())) + "/" + getFileNameFromfilePath(imageFile.getAbsolutePath()));
        if (tempFile.exists()) {
            Toast.makeText(context, "compressedImage size in : "+ tempFile.length(), Toast.LENGTH_SHORT).show();

            AppDebugLog.print("file exists in getCompressedImageFile : " + tempFile.length());
            return imageFile;
        }
        try {
            Bitmap.CompressFormat compressFormat;
            if (getFileNameFromfilePath(imageFile.getAbsolutePath()).contains("png")) {
                compressFormat = Bitmap.CompressFormat.PNG;
            } else if (getFileNameFromfilePath(imageFile.getAbsolutePath()).contains("gif")) {
                compressFormat = Bitmap.CompressFormat.WEBP;
            } else {
                compressFormat = Bitmap.CompressFormat.JPEG;
            }

            compressedImage = new Compressor(context)
                    .setMaxWidth(720)
                    .setMaxHeight(1280)
                    .setQuality(90)
                    .setCompressFormat(compressFormat)
                    .setDestinationDirectoryPath(getFilePathAsPerFileName1(getFileNameFromfilePath(imageFile.getAbsolutePath())))
                    .compressToFile(imageFile);


            AppDebugLog.print("compressedImage size in : " + compressedImage.length());
        } catch (IOException | NullPointerException e) {
            compressedImage = imageFile;
            AppDebugLog.print("compressedImage size in : " + compressedImage.length());
            e.printStackTrace();
        }
        if (compressedImage.length() > imageFile.length()) {
            return imageFile;
        }
        AppDebugLog.print("imageFile size after compressed : " + getFileSizeMegaBytes(compressedImage));
        return compressedImage;
    }

    public static String getFileNameFromfilePath(String filePath) {
        AppDebugLog.print("filename :" + filePath.substring(filePath.lastIndexOf("/") + 1));
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    private static String getFilePathAsPerFileName1(String fileName) {
        String filePath = "";
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + AppConstants.DIRECTORY_NAME + File.separator);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        filePath = directory.getPath();
        AppDebugLog.print("file path :" + filePath);

        return filePath;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {
//        AppDebugLog.print(" File -" +
//                "Authority: " + uri.getAuthority() +
//                ", Fragment: " + uri.getFragment() +
//                ", Port: " + uri.getPort() +
//                ", Query: " + uri.getQuery() +
//                ", Scheme: " + uri.getScheme() +
//                ", Host: " + uri.getHost() +
//                ", Segments: " + uri.getPathSegments().toString()
//        );

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                //            // LocalStorageProvider
                //            if (isLocalStorageDocument(uri)) {
                //                // The path is the id
                //                return DocumentsContract.getDocumentId(uri);
                //            }
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    AppDebugLog.print("In getPath : " + type + " : " + split[1]);
                    if ("primary".equalsIgnoreCase(type)) {
                        return getExternalStorageDirectory() + "/" + split[1];
                    } else {
                        return "/storage" + File.separator + split[0] + File.separator + split[1];
                    }
                }
                // GoogleDriveProvider
                else if (isGoogleDriveDocument(uri) || isDownloadsDocument(uri)) {
                    Cursor returnCursor = null;
                    try {
                        returnCursor = MyApplication.getInstance().getContentResolver().query(uri, null, null, null, null);
                        int nameIndex = 0;
                        int sizeIndex = 0;
                        if (returnCursor != null) {
                            nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                            returnCursor.moveToFirst();
                            AppDebugLog.print("name : " + returnCursor.getString(nameIndex));
                            AppDebugLog.print("size : " + Long.toString(returnCursor.getLong(sizeIndex)));
                            AppDebugLog.print("Drive file path : " + getDriveFileAbsolutePath(MyApplication.getInstance(), uri, returnCursor.getString(nameIndex)));
                            return getDriveFileAbsolutePath(MyApplication.getInstance(), uri, returnCursor.getString(nameIndex));
                        }
                    } finally {
                        if (returnCursor != null)
                            returnCursor.close();
                    }
                }
                // DownloadsProvider
                //            else if (isDownloadsDocument(uri)) {
                //
                //                final String id = DocumentsContract.getDocumentId(uri);
                //                final Uri contentUri = ContentUris.withAppendedId(
                //                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                //
                //                return getDataColumn(context, contentUri, null, null);
                //            }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is {@linkLocalStorageProvider}.
//     * @author paulburke
//     */
//    public static boolean isLocalStorageDocument(Uri uri) {
//        return LocalStorageProvider.AUTHORITY.equals(uri.getAuthority());
//    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "user.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "user.google.android.apps.docs.storage".equals(uri.getAuthority());
    }

    public static boolean isGoogleDriveDocument(Uri uri) {
        return "user.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    public static boolean isMediaDocument(Uri uri) {
        return "user.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "user.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static String getDriveFileAbsolutePath(Context context, Uri uri, String fileName) {
        if (uri == null) return null;
        ContentResolver resolver = context.getContentResolver();
        FileInputStream input = null;
        FileOutputStream output = null;
        String outputFilePath = new File(context.getCacheDir(), fileName).getAbsolutePath();
        try {
            ParcelFileDescriptor pfd = resolver.openFileDescriptor(uri, "r");
            FileDescriptor fd = pfd.dup().getFileDescriptor();
            input = new FileInputStream(fd);
            output = new FileOutputStream(outputFilePath);
            int read = 0;
            byte[] bytes = new byte[4096];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            return new File(outputFilePath).getAbsolutePath();
        } catch (IOException ignored) {
            // nothing we can do
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                DatabaseUtils.dumpCursor(cursor);

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = null;

            inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
            }
        } catch (Exception e) {
            AppDebugLog.print("Error in hideSoftKeyboard : " + e.getMessage());
        }
    }

    public static int getDisplayWidth(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static String getAgeFromBirthDate(Date birthDate) {
        int years = 0;
        int months = 0;
        int days = 0;

        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());

        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);

        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        //Get difference between months
        months = currMonth - birthMonth;

        //if month difference is in negative then reduce years by one
        //and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }

        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        return years + " Years";
    }

    public static String getDetailsFromValue(String createdBy, String age, String height, String caste, String religion, String city, String Country, String education) {
        String detail = "";

        // AppDebugLog.print("age : " + age + " height : " + height + " caste : " + caste + " religion : " + religion + " city : " + city + " Country : " + Country);

//        if(isValidValue(birthDateStr)){
//            Date birthDate = getDateFromDateString(AppConstants.filterDateFormat, birthDateStr);
//            if(birthDate!=null) {
//                detail = getAgeFromBirthDate(birthDate);
//            }
//        }
        if (isValidValue(createdBy)) {
            detail = "Profile Created By " + createdBy;
        }
        if (isValidValue(age)) {
            if (detail.length() > 0) {
                detail = detail + ", " + age; // getCalculateHeight(height);
            } else {
                detail = age;
            }
        }
        if (isValidValue(height)) {
            if (detail.length() > 0) {
                detail = detail + ", " + height; // getCalculateHeight(height);
            } else {
                detail = height;
            }
        }
        if (isValidValue(caste)) {
            if (detail.length() > 0) {
                detail = detail + ", " + caste;
            } else {
                detail = caste;
            }
        }
        if (isValidValue(religion)) {
            detail = detail + ", " + religion;
        }
        if (isValidValue(city)) {
            detail = detail + ", " + city;
        }
        if (isValidValue(Country)) {
            detail = detail + ", " + Country;
        }
        if (isValidValue(education)) {
            detail = detail + ", " + education;
        }

        detail = detail + "...<font color='#f9692a'>Read More</font>";

        return detail;
    }

    private static boolean isValidValue(String str) {
        if (str != null && str.length() > 0 && !str.trim().equalsIgnoreCase("null")) return true;
        else return false;
    }

    public static String getDateStringFromDate(SimpleDateFormat dateFormat, Date date) {
        return dateFormat.format(date);
    }

    public static Date getDateFromDateString(SimpleDateFormat dateFormat, String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCalculateHeight(String value) {
        if (value.length() > 0 && !value.equalsIgnoreCase(MyApplication.getInstance().getString(R.string.lbl_not_available))) {
            switch (value) {
                case "48":
                    return "Below 4ft";
                case "85":
                    return "Above 7ft";
                default:
                    int foot = (int) Float.parseFloat(value.trim()) / 12;
                    int inch = (int) Float.parseFloat(value.trim()) % 12;
                   /* double cm = 0;
                    if (foot > 0) {
                        cm = foot * 30.48;
                    }
                    if (inch > 0) {
                        cm = cm + (inch * 2.54);
                    }
                    int newcm = (int) cm;*/

                    return foot + "ft " + inch + "in";// -  + newcm + "cm";
            }
        } else {
            return MyApplication.getInstance().getString(R.string.lbl_not_available);
        }
    }

    public static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            AppDebugLog.print("Exception In getAppVersionName : " + e.getLocalizedMessage());
        }
        return "";
    }

    public static String getErrorMessageFromErrorCode(int errCode) {
        String errorMessage = "Something went wrong, Please try again later!!";

        switch (errCode) {
            case 300:
                errorMessage = "The requested page has moved to a new url.";
                break;
            case 301:
                errorMessage = "The requested page has moved to a new url.";
                break;
            case 302:
                errorMessage = "The requested page has moved temporarily to a new url.";
                break;
            case 304:
                errorMessage = "The URL has not been modified.";
                break;
            case 400:
                errorMessage = "The server did not understand the request.";
                break;
            case 401:
                errorMessage = "The requested page needs a username and a password.";
                break;
            case 403:
                errorMessage = "Access is forbidden to the requested page.";
                break;
            case 404:
                errorMessage = "The server can not find the requested page.";
                break;
            case 408:
                errorMessage = "The request took longer than the server was prepared to wait.";
                break;
            case 500:
                errorMessage = "The request was not completed. The server met an unexpected condition.";
                break;
            case 501:
                errorMessage = "The request was not completed. The server did not support the functionality required.";
                break;
            case 502:
                errorMessage = "The request was not completed. The server received an invalid response from the upstream server.";
                break;
            case 503:
                errorMessage = "The request was not completed. The server is temporarily overloading or down.";
                break;
            case 504:
                errorMessage = "The gateway has timed out.";
                break;
            case 505:
                errorMessage = "The server does not support the \"http protocol\" version.";
                break;

        }
        return errorMessage;
    }

    //show progress layout
    public void showProgressLayout(AVLoadingIndicatorView view) {
        if (view != null)
            view.setVisibility(View.VISIBLE);
    }

    //hide progress layout
    public void hideProgressLayout(AVLoadingIndicatorView view) {
        if (view != null)
            view.setVisibility(View.GONE);
    }

    //show progress layout
    public void showProgressRelativeLayout(RelativeLayout view) {
        if (view != null)
            view.setVisibility(View.VISIBLE);
    }

    //hide progress layout
    public void hideProgressRelativeLayout(RelativeLayout view) {
        if (view != null)
            view.setVisibility(View.GONE);
    }

    public void setImageMyProfile(String imageUrl, ImageView profileImageView) {
        int placeHolder = 0;
        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
            placeHolder = R.drawable.female;
        } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
            placeHolder = R.drawable.male;
        }

        setImageUsingPicasso(imageUrl, null, profileImageView, placeHolder, 0);
    }

    public void setImage(String photoViewCount, String photoViewStatus, String imageApproval, String imageUrl, ImageView profileImageView, ImageView circleImageView, int totalHMargin) {
        //String[] segments = imageUrl.split("/");
        int placeHolder = 0, photoProtectPlaceHolder = 0;
        if (session.getLoginData(SessionManager.KEY_GENDER).equals("Female")) {
            photoProtectPlaceHolder = R.drawable.photo_protected;
            placeHolder = R.drawable.male;
        } else if (session.getLoginData(SessionManager.KEY_GENDER).equals("Male")) {
            photoProtectPlaceHolder = R.drawable.photo_protected;
            placeHolder = R.drawable.female;
        }

//        if(segments[segments.length-1]!=null || segments[segments.length-1].equalsIgnoreCase("null")) {
//
//        }

        // AppDebugLog.print("imageUrl : "+(segments[segments.length-1]));
        // AppDebugLog.print("imageUrl : "+(segments[segments.length-1]==null || segments[segments.length-1].equalsIgnoreCase("null")));
        if (imageApproval.equals("UNAPPROVED")) {
            setImageUsingPicasso(null, circleImageView, profileImageView, placeHolder, totalHMargin);
        } else if (photoViewStatus.equals("0") && (photoViewCount != null && photoViewCount.equals("0"))) {
            setImageUsingPicasso(null, circleImageView, profileImageView, photoProtectPlaceHolder, totalHMargin);
        } else if (photoViewStatus.equals("1")) {
            setImageUsingPicasso(imageUrl, circleImageView, profileImageView, placeHolder, totalHMargin);
        } else if (photoViewStatus.equals("2") && !MyApplication.getPlan()) {
            setImageUsingPicasso(null, circleImageView, profileImageView, photoProtectPlaceHolder, totalHMargin);
        } else {
            setImageUsingPicasso(null, circleImageView, profileImageView, placeHolder, totalHMargin);
        }
    }

    private void setImageUsingPicasso(String imageUrl, ImageView circleImageView, ImageView profileImageView, int placeHolder, int totalHMargin) {
        //Transformation transformation = getImageRatio(profileImageView, totalHMargin);
        if (imageUrl != null) {
            if (circleImageView != null)
                Picasso.get().load(imageUrl).placeholder(placeHolder).error(placeHolder).into(circleImageView);
            Picasso.get().load(imageUrl)
                    .placeholder(placeHolder)
                    .error(placeHolder)
                    //.transform(transformation)
                    .fit()
                    .centerCrop(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                    .into(profileImageView);
        } else {
            if (circleImageView != null) circleImageView.setImageResource(placeHolder);
            // profileImageView.setImageResource(placeHolder);
            Picasso.get().load(placeHolder).placeholder(placeHolder)
                    .error(placeHolder)
                    //.transform(transformation)
                    .fit()
                    .centerCrop(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                    .into(profileImageView);
            setTintPlaceHolder(circleImageView, profileImageView);
        }
    }

    private void setTintPlaceHolder(ImageView circleImageView, ImageView profileImageView) {
//        if (circleImageView != null)
//            circleImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.OVERLAY);
//        profileImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.OVERLAY);
    }

    private Transformation getImageRatio(ImageView profileImageView, int totalHMargin) {
        int screenWidth = ApplicationData.getSharedInstance().getDisplayWidth() - convertDpToPixels(totalHMargin, MyApplication.getContext());
        // int width = convertDpToPixels(screenWidth, MyApplication.getContext());
        int height = (int) (screenWidth * 1.210f); //convertDpToPixels((screenWidth * 1.210f), MyApplication.getContext());
//        AppDebugLog.print("screenWidth : "+screenWidth);
//        AppDebugLog.print("height : "+height);
        Transformation transformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                try {
                    double aspectRatio = height / screenWidth;
                    int targetHeight = (int) (screenWidth * aspectRatio);
                    Bitmap result = Bitmap.createScaledBitmap(source, screenWidth, targetHeight, false);
                    if (result != source) {
                        source.recycle();
                    }
                    return result;
                } catch (Exception e) {
                    Bitmap result = Bitmap.createScaledBitmap(source, screenWidth, height, false);
                    return result;
                }
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };
        profileImageView.getLayoutParams().height = height;

        return transformation;
    }

    public long getFIleSizeInMB(File file) {

        long fileSizeInBytes = file.length();
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        long fileSizeInMB = fileSizeInKB / 1024;

        return fileSizeInMB;
    }

    public static String getExtensionFromPath(String filePath) {
        String extension = "";
        if (filePath != null)
            extension = filePath.substring(filePath.lastIndexOf("."));
        return extension;
    }

    /**
     * Save and get Bean in SharedPreference
     */
    public static String getStringFromModel(VendorParentModel beanObject) {
        Gson gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();
        String json = gson.toJson(beanObject);
        return json;
    }

    /**
     * Save and get Bean in SharedPreference
     */
    public static VendorParentModel getModelFromString(String json) {
        Gson gson = new GsonBuilder().setDateFormat(AppConstants.GSONDateTimeFormat).create();
        return gson.fromJson(json, VendorParentModel.class);
    }

    private static String checkNull(String text) {
        if (text != null && !text.equals("") && !text.equals("null")) {
            return text + ", ";
        }
        return "";
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {
        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }


    public static boolean isTextViewEmpty(Editable s) {
        if (s == null) {
            return true;
        } else {
            return TextUtils.isEmpty(s);
        }
    }

    //get formatted date like today , yesterday and so on...
    public static String getFormattedDate(long timeInMilis, boolean isGetFullDate) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEE, dd MMM, h:mm aa";
        final String dateFormatString = "dd/MM/yy";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == time.get(Calendar.DATE)) {
            if (isGetFullDate)
                return "Today " + DateFormat.format(timeFormatString, time);
            else
                return "" + DateFormat.format(timeFormatString, time);
        } else if (now.get(Calendar.DATE) - time.get(Calendar.DATE) == 1) {
            if (isGetFullDate)
                return "Yesterday " + DateFormat.format(timeFormatString, time);
            else
                return "Yesterday";
        } else if (now.get(Calendar.YEAR) == time.get(Calendar.YEAR)) {
            if (isGetFullDate)
                return DateFormat.format(dateTimeFormatString, time).toString();
            else
                return DateFormat.format(dateFormatString, time).toString();
        } else {
            if (isGetFullDate)
                return DateFormat.format("MMMM dd yyyy, h:mm aa", time).toString();
            else
                return DateFormat.format(dateFormatString, time).toString();
        }
    }
}
