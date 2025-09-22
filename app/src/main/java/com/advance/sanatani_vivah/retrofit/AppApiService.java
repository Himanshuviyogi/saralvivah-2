package com.advance.sanatani_vivah.retrofit;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

import com.advance.sanatani_vivah.utility.AppConstants;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Use for api call and get your response in your activity or fragment
 * Created by Nasirali on 02-02-2019.
 */

public interface AppApiService {

    @Multipart
    @POST
    Call<JsonObject> uploadPhotoNew(@Url String url, @Part MultipartBody.Part file1, @PartMap() Map<String, RequestBody> params);


    /**
     * @param files
     * @param params All params in multipart request body
     * @return JsonObject
     */
    @Multipart
    @POST
    Call<JsonObject> uploadMultipleFiles(@Url String url, @Part ArrayList<MultipartBody.Part> files, @PartMap() Map<String, RequestBody> params);


    @FormUrlEncoded
    @POST
    Call<JsonObject> postRequestWithParams(@Url String url, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST
    Call<JsonObject> postRequest(@Url String url);

    @GET
    Call<JsonObject> getRequest(@Url String url);

    @FormUrlEncoded
    @POST(AppConstants.get_token)
    Call<JsonObject> getToken(@FieldMap Map<String, String> params);

    @GET(AppConstants.common_list)
    Call<JsonObject> getCommonList();

    @FormUrlEncoded
    @POST(AppConstants.common_depedent_list)
    Call<JsonObject> getCommonDepedentList(@FieldMap Map<String, String> params);


    /**
     * @param file1  Crop image
     * @param file2  Original image
     * @param params All params in multipart request body
     * @return JsonObject
     */
    @Multipart
    @POST(AppConstants.register_upload_profile_image)
    Call<JsonObject> uploadPhoto(@Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @PartMap() Map<String, RequestBody> params);

    @Multipart
    @POST(AppConstants.upload_horoscope)
    Call<JsonObject> uploadHoroscopePhoto(@Part MultipartBody.Part file1, @PartMap() Map<String, RequestBody> params);

    @Multipart
    @POST(AppConstants.upload_id_proof_photo)
    Call<JsonObject> uploadIdProof(@Part MultipartBody.Part file1, @PartMap() Map<String, RequestBody> params);

    @Multipart
    @POST(AppConstants.upload_photo_new)
    Call<JsonObject> uploadMyPhotoWithCrop(@Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @PartMap() Map<String, RequestBody> params);

    @Multipart
    @POST(AppConstants.upload_photo_new)
    Call<JsonObject> uploadMyPhotoWithoutCrop(@Part MultipartBody.Part file1, @PartMap() Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST(AppConstants.success_story)
    Call<JsonObject> getSuccessStoryListRequest(@FieldMap() Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.GET_VENDOR_CATEGORY_LIST)
    Call<JsonObject> getVendorCategoryList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.GET_VENDOR_LIST)
    Call<JsonObject> getVendorList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.GET_VENDOR_DETAILS)
    Call<JsonObject> getVendorDetails(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.SEND_VENDOR_INQUIRY)
    Call<JsonObject> sendVendorInquiry(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.SEND_VENDOR_REVIEW)
    Call<JsonObject> sendVendorReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.get_notification_list)
    Call<JsonObject> getNotificationList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.GET_ONLINE_MEMBERS)
    Call<JsonObject> getOnlineMemberListRequest(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.GET_MESSAGE_LIST)
    Call<JsonObject> getMessageList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.GET_CONVERSATION_LIST)
    Call<JsonObject> getConversationList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.CREATE_CONVERSATION)
    Call<JsonObject> createConversation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.DELETE_MESSAGE_REQUEST)
    Call<JsonObject> deleteMessageRequest(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.DELETE_ALL_MESSAGES)
    Call<JsonObject> deleteAllMessageList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(AppConstants.SEND_MESSAGE)
    Call<JsonObject> sendMessageRequest(@FieldMap Map<String, String> params);
}
