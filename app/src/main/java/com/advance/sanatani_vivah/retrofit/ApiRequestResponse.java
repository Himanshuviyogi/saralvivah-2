package com.advance.sanatani_vivah.retrofit;

import android.content.Context;

import androidx.annotation.Nullable;

import com.advance.sanatani_vivah.R;
import com.advance.sanatani_vivah.network.ConnectionDetector;
import com.advance.sanatani_vivah.utility.AppConstants;
import com.advance.sanatani_vivah.utility.AppDebugLog;
import com.advance.sanatani_vivah.utility.Common;
import com.advance.sanatani_vivah.utility.SessionManager;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ApiRequestResponse {

    public void postRequest(Context context, String url, Map<String, String> params, AppApiRequestCallbacks listener, @Nullable String requestTag) {
        if (!ConnectionDetector.isConnectingToInternet(context)) {
            Common.showToast(context.getString(R.string.err_msg_no_intenet_connection));
            if (listener != null) listener.onError(true, requestTag);
            return;
        }

        AppDebugLog.print("Request Url : " + url);

        Retrofit retrofit = RetrofitClient.getClientWithHeader();
        AppApiService appApiService = retrofit.create(AppApiService.class);

        Call<JsonObject> call;
        if (params != null) {
            params.put("user_agent", AppConstants.USER_AGENT);
            params.put("csrf_new_matrimonial","");
            AppDebugLog.print("params : " + params.toString());
            call = appApiService.postRequestWithParams(url, params);
        } else call = appApiService.getRequest(url);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                AppDebugLog.print(requestTag + " response in postRequest : " + response.body());
                JsonObject data = response.body();
                if (data != null) {
                    if (response.isSuccessful()) {
                        AppDebugLog.print("data : " + data.toString());
                        if (data.has(AppConstants.KEY_STATUS) && data.get(AppConstants.KEY_STATUS).getAsString().equals("success")) {
                            if (listener != null) listener.onSuccess(data, requestTag);
                        } else if (data.has("status") && data.get("status").getAsString().equalsIgnoreCase("warning")) {
                            if(requestTag.equalsIgnoreCase("other_user_profile")){
                                listener.onSuccess(data, requestTag);
                            }else{
                                Common.showToast(data.get(AppConstants.KEY_MESSAGE).getAsString());
                                listener.onError(false, requestTag);
                            }

                        } else {
                            if (data.has(AppConstants.KEY_STATUS) && data.get(AppConstants.KEY_STATUS).getAsString().equals("error")) {
                                Common.showToast(data.get(AppConstants.KEY_MESSAGE).getAsString());
                                listener.onError(false, requestTag);
                                return;
                            }
                            if (listener != null && data.has(AppConstants.KEY_STATUS)){
                                listener.onError(true, requestTag);
                                Common.showToast(data.get(AppConstants.KEY_MESSAGE).getAsString());
                            }

                            if (data.has(AppConstants.KEY_MESSAGE) && data.has(AppConstants.KEY_STATUS))
                                //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,Integer.valueOf(data.get(AppConstants.KEY_STATUS).getAsString()), data.get(AppConstants.KEY_MESSAGE).getAsString()), Toast.LENGTH_LONG, true).show();
                                Common.showToast(getErrorMessageFromErrorCode(context,Integer.parseInt(data.get(AppConstants.KEY_STATUS).getAsString()), data.get(AppConstants.KEY_MESSAGE).getAsString()));
                            else
                                //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,response.code(), ""), Toast.LENGTH_LONG, true).show();
                                Common.showToast(getErrorMessageFromErrorCode(context,response.code(), ""));
                        }
                    } else {
                        if (listener != null) listener.onError(true, requestTag);
                        if (data.has(AppConstants.KEY_MESSAGE) && data.has(AppConstants.KEY_STATUS))
                            //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,Integer.valueOf(data.get(AppConstants.KEY_STATUS).getAsString()), data.get(AppConstants.KEY_MESSAGE).getAsString()), Toast.LENGTH_SHORT, true).show();
                            Common.showToast(getErrorMessageFromErrorCode(context,Integer.valueOf(data.get(AppConstants.KEY_STATUS).getAsString()), data.get(AppConstants.KEY_MESSAGE).getAsString()));
                        else
                            //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,response.code(), ""), Toast.LENGTH_LONG, true).show();
                            Common.showToast(getErrorMessageFromErrorCode(context,response.code(), ""));
                    }
                } else {
                    if (listener != null) listener.onError(true, requestTag);
                    Common.showToast(getErrorMessageFromErrorCode(context,response.code(), ""));
                    //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,response.code(), ""), Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppDebugLog.print("error in  : " + requestTag + " " + t.getMessage());
                Common.showToast("Something went wrong, Please try again later!!");
                if (listener != null) listener.onError(true, requestTag);
                //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,0, ""), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    public void postFileRequest(Context context, String url, Map<String, RequestBody> params, MultipartBody.Part file, AppApiRequestCallbacks listener, @Nullable String requestTag) {
        if (!ConnectionDetector.isConnectingToInternet(context)) {
            Common.showToast(context.getString(R.string.err_msg_no_intenet_connection));
            if (listener != null) listener.onError(true, requestTag);
            return;
        }

        AppDebugLog.print("Request Url : " + url);

        Retrofit retrofit = RetrofitClient.getClientWithHeader();
        AppApiService appApiService = retrofit.create(AppApiService.class);

        Call<JsonObject> call = appApiService.uploadPhotoNew(url, file, params);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                AppDebugLog.print(requestTag + " response in postRequest : " + response.body());
                JsonObject data = response.body();
                if (data != null) {
                    if (response.isSuccessful()) {
                        if (data.has(AppConstants.KEY_STATUS) && data.get(AppConstants.KEY_STATUS).getAsString() == "success") {
                            if (listener != null) listener.onSuccess(data, requestTag);
                        } else {
                            if (listener != null) listener.onError(true, requestTag);
                            if (data.has(AppConstants.KEY_MESSAGE) && data.has(AppConstants.KEY_STATUS))
                                //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,Integer.valueOf(data.get(AppConstants.KEY_STATUS).getAsString()), data.get(AppConstants.KEY_MESSAGE).getAsString()), Toast.LENGTH_LONG, true).show();
                                Common.showToast(data.get(AppConstants.KEY_MESSAGE).getAsString());
                            else
                                //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,response.code(), ""), Toast.LENGTH_LONG, true).show();
                                Common.showToast(getErrorMessageFromErrorCode(context,response.code(), ""));
                        }
                    } else {
                        if (listener != null) listener.onError(true, requestTag);
                        if (data.has(AppConstants.KEY_MESSAGE) && data.has(AppConstants.KEY_STATUS))
                            //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,Integer.valueOf(data.get(AppConstants.KEY_STATUS).getAsString()), data.get(AppConstants.KEY_MESSAGE).getAsString()), Toast.LENGTH_SHORT, true).show();
                            Common.showToast(data.get(AppConstants.KEY_MESSAGE).getAsString());
                        else
                            //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,response.code(), ""), Toast.LENGTH_LONG, true).show();
                            Common.showToast(getErrorMessageFromErrorCode(context,response.code(), ""));

                    }
                } else {
                    if (listener != null) listener.onError(true, requestTag);
                    Common.showToast(getErrorMessageFromErrorCode(context,response.code(), ""));
                    //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,response.code(), ""), Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppDebugLog.print("error in  : " + requestTag + t.getMessage());
                Common.showToast("Something went wrong, Please try again later!!");
                if (listener != null) listener.onError(true, requestTag);
                //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,0, ""), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    public void postMultipleFilesRequest(Context context, String url, Map<String, RequestBody> params, ArrayList<MultipartBody.Part> files, AppApiRequestCallbacks listener, @Nullable String requestTag) {
        if (!ConnectionDetector.isConnectingToInternet(context)) {
            Common.showToast(context.getString(R.string.err_msg_no_intenet_connection));
            if (listener != null) listener.onError(true, requestTag);
            return;
        }

        AppDebugLog.print("Request Url : " + url);
        if (params != null)
            AppDebugLog.print("params : " + params.toString());

        Retrofit retrofit = RetrofitClient.getClientWithHeader();
        AppApiService appApiService = retrofit.create(AppApiService.class);

        Call<JsonObject> call = appApiService.uploadMultipleFiles(url, files, params);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                AppDebugLog.print(requestTag + " response in postRequest : " + response.body());
                JsonObject data = response.body();
                if (data != null) {
                    if (response.isSuccessful()) {
                        if (data.has(AppConstants.KEY_STATUS)) {
                            if (listener != null) listener.onSuccess(data, requestTag);
                        } else {
                            if (listener != null) listener.onError(true, requestTag);
                            if (data.has(AppConstants.KEY_MESSAGE) && data.has(AppConstants.KEY_STATUS))
                                //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,Integer.valueOf(data.get(AppConstants.KEY_STATUS).getAsString()), data.get(AppConstants.KEY_MESSAGE).getAsString()), Toast.LENGTH_LONG, true).show();
                                Common.showToast(getErrorMessageFromErrorCode(context,Integer.valueOf(data.get(AppConstants.KEY_STATUS).getAsString()), data.get(AppConstants.KEY_MESSAGE).getAsString()));
                            else
                                //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,response.code(), ""), Toast.LENGTH_LONG, true).show();
                                Common.showToast(getErrorMessageFromErrorCode(context,response.code(), ""));
                        }
                    } else {
                        if (listener != null) listener.onError(true, requestTag);
                        if (data.has(AppConstants.KEY_MESSAGE) && data.has(AppConstants.KEY_STATUS))
                            //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,Integer.valueOf(data.get(AppConstants.KEY_STATUS).getAsString()), data.get(AppConstants.KEY_MESSAGE).getAsString()), Toast.LENGTH_SHORT, true).show();
                            Common.showToast(getErrorMessageFromErrorCode(context,Integer.valueOf(data.get(AppConstants.KEY_STATUS).getAsString()), data.get(AppConstants.KEY_MESSAGE).getAsString()));
                        else
                            //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,response.code(), ""), Toast.LENGTH_LONG, true).show();
                            Common.showToast(getErrorMessageFromErrorCode(context,response.code(), ""));

                    }
                } else {
                    if (listener != null) listener.onError(true, requestTag);
                    Common.showToast(getErrorMessageFromErrorCode(context,response.code(), ""));
                    //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,response.code(), ""), Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppDebugLog.print("error in  : " + requestTag + t.getMessage());
                Common.showToast("Something went wrong, Please try again later!!");
                if (listener != null) listener.onError(true, requestTag);
                //Toasty.error(Application.getAppContext(), getErrorMessageFromErrorCode(context,0, ""), Toast.LENGTH_LONG, true).show();
            }
        });
    }


    public interface AppApiRequestCallbacks {
        void onSuccess(JsonObject data, @Nullable String requestTag);
        void onError(boolean isError, @Nullable String requestTag);
    }

    private static String getErrorMessageFromErrorCode(Context context,int errCode, String errMsg) {
        String errorMessage = "Something went wrong, Please try again later!!";

        switch (errCode) {
            case 300:
                errorMessage = errMsg;
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
            case 1001:
                logoutFromApp(context);
                errorMessage = errMsg;
                break;
            case 1002:
                logoutFromApp(context);
                errorMessage = errMsg;
                break;
            case 1003:
                errorMessage = errMsg;
                break;
            case 1004:
                errorMessage = errMsg;
                break;
        }
        return errorMessage;
    }

    private static void logoutFromApp(Context context) {
        SessionManager session = new SessionManager(context);
        session.logoutUser();
    }


}
