package com.advance.sanatani_vivah.activities;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadVideoActivity extends YouTubeBaseActivity {//implements YouTubePlayer.OnInitializedListener
    private LinearLayout lay_link;
    private Button btn_embd, btn_upload;
    private ImageView btnBack;
    private YouTubePlayerView youTubePlayerView;
    private static final int RECOVERY_REQUEST = 1;
    private EditText et_url;
    private Common common;
    private SessionManager session;
    private RelativeLayout loader;
    private Toolbar toolbar;
    private String vid_url = "";
    private final String youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";
    private static String[] videoIdRegex = {"\\?vi?=([^&]*)", "watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9\\-]*)"};

    String api_key = "AIzaSyDfjF8SONSgLMO-5Jzi4kflJCi-MeW7VAA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        common = new Common(this);
        session = new SessionManager(this);

        btnBack = findViewById(R.id.btnBack);
        youTubePlayerView = findViewById(R.id.youTubePlayerView);
        loader = findViewById(R.id.loader);
        et_url = findViewById(R.id.et_url);
        btn_upload = findViewById(R.id.btn_upload);
        lay_link = findViewById(R.id.lay_link);
        btn_embd = findViewById(R.id.btn_embd);

        btn_embd.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        btn_upload.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        btn_embd.setOnClickListener(view -> lay_link.setVisibility(lay_link.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));

        btnBack.setOnClickListener(v->{
            finish();
        });

        btn_upload.setOnClickListener(view -> {
            String url = et_url.getText().toString().trim();
            if (TextUtils.isEmpty(url)) {
                et_url.setError("Please enter/paste youtube link here");
                return;
            }
            if (!isValidUrl(url)) {
                et_url.setError("Your youtube link is not valid");
                return;
            }

            uploadUrl(url);

        });
        getMyprofile();
    }

    private void initYTPlayer(String id) {
        youTubePlayerView.initialize(
                api_key,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(
                            YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b)
                    {
                        youTubePlayer.loadVideo(id);
                        youTubePlayer.play();
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult
                                                                youTubeInitializationResult)
                    {
                        Toast.makeText(getApplicationContext(), "Video player Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String extractYTId(String ytUrl) {
        String youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(ytUrl);

        for (String regex : videoIdRegex) {
            Pattern compiledPattern = Pattern.compile(regex);
            Matcher matcher = compiledPattern.matcher(youTubeLinkWithoutProtocolAndDomain);

            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null;
    }

    public static String getYoutubeVideoId(String youtubeUrl) {
        String video_id = "";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http")) {

            String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }

    private String youTubeLinkWithoutProtocolAndDomain(String url) {
        Pattern compiledPattern = Pattern.compile(youTubeUrlRegEx);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            return url.replace(matcher.group(), "");
        }
        return url;
    }

    private void getMyprofile() {
       common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        common.makePostRequest(AppConstants.get_my_profile, param, response -> {
            common.hideProgressRelativeLayout(loader);
            // Log.d("myprof",response);
            try {
                JSONObject object = new JSONObject(response);
                JSONObject data = object.getJSONObject("data");

                if (!data.getString("video_url").equals("") && !data.getString("video_url").equals("null")) {
                    //String html = "<iframe width=\"345\" height=\"200\" src=\""+data.getString("video_url")+"?width=450&height=260&results=60&dynamic=true\" ></iframe>";
                    // web_video.loadData(html, "text/html", null);
                    vid_url = getYoutubeVideoId(data.getString("video_url"));
                    initYTPlayer(vid_url);
                } else {
                    // web_video.setVisibility(View.GONE);
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

    private void uploadUrl(String url) {
       common.showProgressRelativeLayout(loader);

        HashMap<String, String> param = new HashMap<>();
        param.put("member_id", session.getLoginData(SessionManager.KEY_USER_ID));
        param.put("videoUrl", url);

        common.makePostRequest(AppConstants.add_video, param, response -> {
            common.hideProgressRelativeLayout(loader);
            try {
                JSONObject object = new JSONObject(response);
                common.showToast(object.getString("errmessage"));
                if (object.getString("status").equals("success")) {
                    et_url.setText("");
                    recreate();
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

    private boolean isValidUrl(String url) {

        if (url == null) {
            return false;
        }
        if (URLUtil.isValidUrl(url)) {
            // Check host of url if youtube exists
            Uri uri = Uri.parse(url);
            return "www.youtube.com".equals(uri.getHost()) || "youtu.be".equals(uri.getHost());
        }
        // In other any case
        return false;
    }

}
