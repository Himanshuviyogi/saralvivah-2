package com.advance.sanatani_vivah.utility;


import java.text.SimpleDateFormat;
import java.util.Locale;

public class AppConstants {

    public static final int DRAWABLE_SIZE = 20;
    public static final int REQUEST_TIMEOUT = 60000;

    public static final String USER_AGENT = "NI-AAPP";

    // zegocloud a/v call
    public static final long appID = 1179695776;
    public static final String appSign = "618a2e8f9feb43394fa5465107d2ff46971530edad320ee1a97e2249e38a4b36";
    public static final String AV_CALL_FINISH_ACTIVITY = "com.advanced.user.zegocloud";

    //staging url
    public static final String BASE_URL = "https://www.sanatanimatrimony.com/";


    public static final String PAYMENT_LOGO_PREFIX_URL = BASE_URL + "assets/payment_logo/";//Working

    public static final String DIRECTORY_NAME = "shadii";

    public static final String get_token = BASE_URL + "common_request/get_tocken";
    //public static final String get_token = BASE_URL + "common_request/get_tocken";
    public static final String login = BASE_URL + "login/check_login_service";
    public static final String generate_otp = BASE_URL + "mobile_verification/generate_otp_home";
    public static final String verify_otp = BASE_URL + "mobile_verification/varify_mobile_check_otp_home";
    public static final String verify_otp_firebase = BASE_URL + "my_dashboard/mobile_verify";
    public static final String forgot = BASE_URL + "login/check_email_forgot";
    public static final String common_list = BASE_URL + "common_request/get_common_list_ddr";
    public static final String common_depedent_list = BASE_URL + "common_request/get_list_json";
    public static final String register_first = BASE_URL + "register/save_register";
    public static final String register_step = BASE_URL + "register/save_register_step";
    public static final String site_data = BASE_URL + "common_request/get_site_data";
    public static final String register_upload_profile_image = "register/save_register_step";
    public static final String contact_form = BASE_URL + "contact/submit_contact";
    public static final String get_my_profile = BASE_URL + "my-profile/get_my_profile";
    public static final String recent_join = BASE_URL + "my-dashboard/recent-profile";
    public static final String recent_login = BASE_URL + "my-dashboard/recently-login";
    public static final String photo_password_request = BASE_URL + "search/send_photo_password_request";
    public static final String check_plan = BASE_URL + "premium-member/current-plan";
    public static final String update_location = BASE_URL + "my-dashboard/add_lat_long/";
    public static final String like_profile = BASE_URL + "search/member-like";
    public static final String send_interest = BASE_URL + "search/express-interest-sent";
    public static final String shortlist_user = BASE_URL + "search/add_remove_shortlist_app";
    public static final String block_user = BASE_URL + "search/blocklist";
    public static final String change_password = BASE_URL + "privacy-setting/change_password";
    public static final String saved_search = BASE_URL + "search/saved/";
    public static final String delete_saved_search = BASE_URL + "search/delete-saved-search";
    public static final String delete_profile = BASE_URL + "my-profile/send-delete-reason-admin";
    public static final String contact_setting = BASE_URL + "privacy-setting/contact_privacy_setting";
    public static final String block_list = BASE_URL + "my-profile/block-list/";
    public static final String all_cms = BASE_URL + "cms/get_cms";
    public static final String shortlist_profile = BASE_URL + "my-profile/short_list_app/";
    public static final String like_profile_list = BASE_URL + "my-profile/like_unlike_profile_app/";
    public static final String i_viewed_list = BASE_URL + "my-profile/i_viewed_profile_app/";
    public static final String i_viewed_contact_list = BASE_URL + "my-profile/i_viewed_contact_app/";
    public static final String who_viewed_list = BASE_URL + "my-profile/who_viewed_profile_app/";
    public static final String who_viewed_contact_list = BASE_URL + "my-profile/who_viewed_contact_app/";
    public static final String photo_pass_request_received = BASE_URL + "my-profile/photo_pass_request_received_app/";
    public static final String photo_pass_request_send = BASE_URL + "my-profile/photo_pass_request_sent_app/";
    public static final String delete_request = BASE_URL + "my-profile/delete_request";
    public static final String add_video = BASE_URL + "upload/add_video";

    public static final String UPDATE_VIDEO_AUIDO_CALL_DURATION = BASE_URL + "my-profile/update_call_duration_app";

    public static final String send_mail_member = BASE_URL + "register/send_mail_member";

    public static final String delete_id_proof_photo = BASE_URL + "upload/delete_id_proof_photo";
    public static final String upload_id_proof_photo = BASE_URL + "upload/upload_id_proof_photo";
    public static final String upload_horoscope = BASE_URL + "upload/upload_horoscope_photo";
    public static final String save_search = BASE_URL + "search/add_saved_search";
    public static final String received_match_from_admin = BASE_URL + "matches/received-match-from-admin";
    public static final String message_list = BASE_URL + "message/get_message_list";
    public static final String newmessage_list = BASE_URL + "message/massages_list_api";
    public static final String conversation = BASE_URL + "message/conversation_list_api";
    public static final String update_status = BASE_URL + "message/update_status";
    public static final String express_interest = BASE_URL + "express_interest/index/";
    public static final String action_update_status = BASE_URL + "express-interest/action_update_status";
    public static final String upload_photo_new = BASE_URL + "modify_photo/upload_photo_new";
    public static final String delete_photo = BASE_URL + "modify_photo/delete_photo";
    public static final String set_profile_pic = BASE_URL + "modify_photo/set_profile_pic";
    public static final String photo_visibility_status = BASE_URL + "privacy-setting/photo_visibility_for_mobile_app";
    public static final String search_result = BASE_URL + "search/result/";
    public static final String plan_list = BASE_URL + "premium_member/get_plan_data";
    public static final String check_coupan = BASE_URL + "premium_member/check-coupan";
    public static final String save_matches = BASE_URL + "matches/save_matches";
    public static final String search_now = BASE_URL + "matches/search_now/";
    public static final String other_user_profile = BASE_URL + "search/view_profile_app";
    public static final String edit_profile = BASE_URL + "my-profile/save-profile";
    public static final String view_contact = BASE_URL + "search/view-contact-details";
    public static final String contact_admin = BASE_URL + "Contact/send_msg_admin";
    public static final String send_message = BASE_URL + "message/send_message";
    public static final String get_payment_method = BASE_URL + "premium_member/get_payment_method";
    public static final String payment_url = BASE_URL + "premium-member/payment-process-mobile-app/";
    public static final String payment_fail = BASE_URL + "premium-member/payment_fail_mobile_app_redirect";
    public static final String payment_success = BASE_URL + "premium-member/payment_success_mobile_app_redirect";
    public static final String delete_user_message = BASE_URL + "message/delete_user_message_list_api";
    public static final String reject_request = BASE_URL + "my-profile/reject_request";
    public static final String delete_cover_photo = BASE_URL + "upload/delete_cover_photo";
    public static final String delete_msg = BASE_URL + "message/update_status";
    public static final String success_story = BASE_URL + "success-story";
    public static final String save_story = BASE_URL + "success-story/save_story";

    public static final String set_mobile_verify = BASE_URL + "My_dashboard/varifyMobileFirebase";

    public static final String get_notification_list = BASE_URL + "common_request/get_notification_list";

    //chat apis
    public static final String chat_list = BASE_URL + "chat/massages_list_api";
    public static final String online_chat_list = BASE_URL + "custom_chat_api/get_online_users";
    public static final String delete_user_chat = BASE_URL + "chat/delete_user_message_list_api";
    public static final String chat_conversation = BASE_URL + "chat/conversation_list_api";
    public static final String send_chat = BASE_URL + "chat/send_message_new";
    public static final String update_chat_status = BASE_URL + "chat/update_status";
    public static final String update_online_offline_status = BASE_URL + "login/online_offline_app";

    //Vendor related
    public static final String GET_VENDOR_CATEGORY_LIST = "wedding-vendor/app-vendor-categories";
    public static final String GET_VENDOR_LIST = "wedding-vendor/app-vendor-list";
    public static final String GET_VENDOR_DETAILS = "wedding-vendor/app-details";
    public static final String SEND_VENDOR_REVIEW = "wedding-vendor/app_send_review";
    public static final String SEND_VENDOR_INQUIRY = "wedding-vendor/app_send_enquiry";

    //Online members
    public static final String GET_ONLINE_MEMBERS = "user/online_memeber";
    //Messages
    public static final String GET_MESSAGE_LIST = "message/massages_list_api";
    public static final String GET_CONVERSATION_LIST = BASE_URL +  "custom_chat_api/get_more_message/";
    public static final String CREATE_CONVERSATION = "custom_chat_api/create_conversation";
    public static final String DELETE_MESSAGE_REQUEST = "common_request/send_common_request";
    public static final String DELETE_PHOTO_REQUEST = "common_request/send_common_request";
    public static final String DELETE_ALL_MESSAGES = "message/conversation_trash_api";
    public static final String SEND_MESSAGE = "custom_chat_api/send_message";
    public static final String STATUS_CHANGE_REQUEST = BASE_URL + "custom_chat_api/online_offline_user";

    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    public static final String CHAT_LIST_TAG = "com.chat_list";
    public static final String CHAT_CONVERSATION_TAG = "com.chat_conversation";

    public static final String OUICK_TAG = "quickmessage";
    public static final String CHAT_TAG = "chatting";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final long MAX_IMAGE_SIZE_IN_MB_TO_UPLOAD = 5;

    public static final String TYPE_SEARCH_QUICK = "Quick Search";
    public static final String TYPE_SEARCH_ADVANCE = "Advance Search";
    public static final String TYPE_SEARCH_ID = "Id Search";
    public static final String TYPE_SEARCH_KEYWORD = "Keyword Search";

    public static final String GSONDateTimeFormat = "MMM dd, yyyy hh:mm:ss a";
    public static final String BIRTH_DATE_FORMAT = "dd-MM-yyyy";
    public static final String BIRTH_DATE_UPLOAD_FORMAT = "yyyy-M-dd";
    public static final SimpleDateFormat filterDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(BIRTH_DATE_FORMAT, Locale.ENGLISH);
    public static final SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMM dd hh:mm a");
    public static final SimpleDateFormat DateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat serverBirthDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat fullDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    //api common keys
    public static final String KEY_DATA = "data";
    public static final String KEY_MESSAGE = "errmessage";
    public static final String KEY_STATUS_CODE = "statuscode";
    public static final String KEY_STATUS = "status";
    public static final String KEY_TOTAL_COUNT = "total_count";
    public static final String KEY_CONTINUE_REQUEST = "continue_request";
    public static final String KEY_TOKEN = "tocken";
    public static final String KEY_DISCOUNT_AMOUNT = "discount_amount";
    public static final String KEY_SUBCATEGORY_LIST = "sub_category_list";
    public static final String KEY_TAG_LIST = "tag_list";
    public static final String KEY_INTENT = "key_intent";

    //Intent
    public static final String CONVERSATION_LIST_UPDATE = "com.advanced.user.update.conversationlist";
    public static final String STATUS_UPDATE = "com.advanced.user.update.status";
    public static final String PERSONALIZED_CONVERSATION_LIST_UPDATE = "com.advanced.user.update.personalized.conversationlist";
    public static final String MESSAGE_LIST_UPDATE = "com.advanced.user.update.messagelist";


    public static final String DRAWER_MENU_DATA = "{\n" +
            "tocken: \"8f9dcb074f8e8a9ed53679d46678cddf\",\n" +
            "status: \"success\",\n" +
            "android_version: \"1.0\",\n" +
            "app_version: \"1.0\",\n" +
            "is_force_update: false,\n" +
            "menu_arr: [\n" +
            "{\n" +
            "is_expandable: 0,\n" +
            "menu_action: \"Dashboard\",\n" +
            "menu_id: 0,\n" +
            "menu_img: \"ic_dashboard_new\",\n" +
            "menu_title: \"Dashboard\",\n" +
            "sub_menu: [ ]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 1,\n" +
            "menu_action: \"\",\n" +
            "menu_id: 2,\n" +
            "menu_img: \"ic_profile_new\",\n" +
            "menu_title: \"My Profile\",\n" +
            "sub_menu: [\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"ViewMyProfileActivity\",\n" +
            "sub_menu_id: 0,\n" +
            "sub_menu_title: \"View Profile\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"ChangePasswordActivity\",\n" +
            "sub_menu_id: 1,\n" +
            "sub_menu_title: \"Change Password\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"ManagePhotosActivity\",\n" +
            "sub_menu_id: 2,\n" +
            "sub_menu_title: \"Manage Photos\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"ManageAccountActivity\",\n" +
            "sub_menu_id: 3,\n" +
            "sub_menu_title: \"Manage Account\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"SavedSearchActivity\",\n" +
            "sub_menu_id: 4,\n" +
            "sub_menu_title: \"My Saved Search\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"UploadVideoActivity\",\n" +
            "sub_menu_id: 5,\n" +
            "sub_menu_title: \"Upload Video\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"UploadIdAndHoroscopeActivity\",\n" +
            "sub_menu_id: 6,\n" +
            "submenu_tag: \"id\",\n" +
            "sub_menu_title: \"Upload Id Proof\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"UploadIdAndHoroscopeActivity\",\n" +
            "sub_menu_id: 7,\n" +
            "submenu_tag: \"horoscope\",\n" +
            "sub_menu_title: \"Upload Horoscope\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"DeleteProfileActivity\",\n" +
            "sub_menu_id: 8,\n" +
            "sub_menu_title: \"Delete Account\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 0,\n" +
            "menu_action: \"SearchActivity\",\n" +
            "menu_id: 1,\n" +
            "menu_img: \"ic_search_new\",\n" +
            "menu_title: \"Search\",\n" +
            "sub_menu: [ ]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 1,\n" +
            "menu_action: \"\",\n" +
            "menu_id: 2,\n" +
            "menu_img: \"ic_custom_match_new\",\n" +
            "menu_title: \"My Matches\",\n" +
            "sub_menu: [\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"MatchFromAdminActivity\",\n" +
            "sub_menu_id: 0,\n" +
            "sub_menu_title: \"Match Of The Day\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"CustomMatchActivity\",\n" +
            "sub_menu_id: 1,\n" +
            "sub_menu_title: \"Recommended Match\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"CustomMatchActivity\",\n" +
            "sub_menu_id: 2,\n" +
            "sub_menu_title: \"Premium Match\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"CustomMatchActivity\",\n" +
            "sub_menu_id: 2,\n" +
            "sub_menu_title: \"NearBy Match\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 0,\n" +
            "menu_action: \"RecentlyJoinActivity\",\n" +
            "menu_id: 0,\n" +
            "menu_img: \"ic_recently_join_new\",\n" +
            "menu_title: \"Recently Join\",\n" +
            "sub_menu: [ ]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 0,\n" +
            "menu_action: \"RecentlyLoginActivity\",\n" +
            "menu_id: 0,\n" +
            "menu_img: \"ic_recently_login_new\",\n" +
            "menu_title: \"Recently Login\",\n" +
            "sub_menu: [ ]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 0,\n" +
            "menu_action: \"ExpressInterestActivity\",\n" +
            "menu_id: 9,\n" +
            "menu_img: \"ic_send_interest_new\",\n" +
            "menu_title: \"Express Interest\",\n" +
            "sub_menu: [ ]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 0,\n" +
            "menu_action: \"PhotoPasswordActivity\",\n" +
            "menu_id: 7,\n" +
            "menu_img: \"ic_photo_request\",\n" +
            "menu_title: \"Photo Request\",\n" +
            "sub_menu: [ ]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 0,\n" +
            "menu_action: \"QuickMessageActivity\",\n" +
            "menu_id: 4,\n" +
            "menu_img: \"ic_message_new\",\n" +
            "menu_title: \"Message\",\n" +
            "sub_menu: [ ]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 0,\n" +
            "menu_action: \"ChatActivity\",\n" +
            "menu_id: 4,\n" +
            "menu_img: \"ic_chat_new\",\n" +
            "menu_title: \"Chat\",\n" +
            "sub_menu: [ ]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 1,\n" +
            "menu_action: \"\",\n" +
            "menu_id: 8,\n" +
            "menu_img: \"ic_action_new\",\n" +
            "menu_title: \"Activity\",\n" +
            "sub_menu: [\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"ViewedProfileActivity\",\n" +
            "sub_menu_id: 0,\n" +
            "submenu_tag: \"i_viewed\",\n" +
            "sub_menu_title: \"Profiles I Viewed\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"ViewedProfileActivity\",\n" +
            "sub_menu_id: 1,\n" +
            "submenu_tag: \"my_profile\",\n" +
            "sub_menu_title: \"Viewed My Profiles\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"ViewedContactActivity\",\n" +
            "sub_menu_id: 0,\n" +
            "submenu_tag: \"i_viewed\",\n" +
            "sub_menu_title: \"Contact I Viewed\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"ViewedContactActivity\",\n" +
            "sub_menu_id: 1,\n" +
            "submenu_tag: \"my_profile\",\n" +
            "sub_menu_title: \"Viewed My Contact\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"LikeProfileActivity\",\n" +
            "sub_menu_id: 2,\n" +
            "sub_menu_title: \"Liked Profiles\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"ShortlistedProfileActivity\",\n" +
            "sub_menu_id: 2,\n" +
            "sub_menu_title: \"Shortlisted Profiles\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"BlockListActivity\",\n" +
            "sub_menu_id: 2,\n" +
            "sub_menu_title: \"Blocked Profiles\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 0,\n" +
            "menu_action: \"SuccessStoryActivity\",\n" +
            "menu_id: 9,\n" +
            "menu_img: \"ic_success_story_new\",\n" +
            "menu_title: \"Success Story\",\n" +
            "sub_menu: [ ]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 0,\n" +
            "menu_action: \"FirstVendorCategoryListActivity\",\n" +
            "menu_id: 9,\n" +
            "menu_img: \"ic_vendor_new\",\n" +
            "menu_title: \"Wedding Vendor\",\n" +
            "sub_menu: [ ]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 1,\n" +
            "menu_action: \"\",\n" +
            "menu_id: 3,\n" +
            "menu_img: \"ic_settings_new\",\n" +
            "menu_title: \"Additional Setting\",\n" +
            "sub_menu: [\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"ContactUsActivity\",\n" +
            "sub_menu_id: 0,\n" +
            "sub_menu_title: \"Contact Us\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"AllCmsActivity\",\n" +
            "sub_menu_id: 1,\n" +
            "submenu_tag: \"privacy\",\n" +
            "sub_menu_title: \"Privacy Policy\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"AllCmsActivity\",\n" +
            "sub_menu_id: 2,\n" +
            "submenu_tag: \"refund\",\n" +
            "sub_menu_title: \"Refund Policy\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"AllCmsActivity\",\n" +
            "sub_menu_id: 3,\n" +
            "submenu_tag: \"term\",\n" +
            "sub_menu_title: \"Terms & Condition\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"AllCmsActivity\",\n" +
            "sub_menu_id: 4,\n" +
            "submenu_tag: \"about\",\n" +
            "sub_menu_title: \"About Us\"\n" +
            "},\n" +
            "{\n" +
            "img_sub_menu: \"\",\n" +
            "sub_menu_action: \"ReportMissuseActivity\",\n" +
            "sub_menu_id: 5,\n" +
            "sub_menu_title: \"Report Misuse\"\n" +
            "}\n" +
            "]\n" +
            "},\n" +
            "{\n" +
            "is_expandable: 0,\n" +
            "menu_action: \"Logout\",\n" +
            "menu_id: 10,\n" +
            "menu_img: \"ic_logout_new\",\n" +
            "menu_title: \"Logout\",\n" +
            "sub_menu: [ ]\n" +
            "}\n" +
            "]\n" +
            "}";
}
