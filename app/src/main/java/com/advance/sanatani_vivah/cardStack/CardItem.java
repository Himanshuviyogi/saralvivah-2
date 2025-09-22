package com.advance.sanatani_vivah.cardStack;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

public class CardItem {
    @SerializedName("id")
    private String id;
    @SerializedName("matri_id")
    private String matriId;
    @SerializedName("email")
    private String email;
    @SerializedName("marital_status")
    private String maritalStatus;
    @SerializedName("profileby")
    private String profileby;
    @SerializedName("username")
    private String username;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private Object lastname;
    @SerializedName("gender")
    private String gender;
    @SerializedName("birthdate")
    private String birthdate;
    @SerializedName("birthtime")
    private String birthtime;
    @SerializedName("education_detail")
    private String educationDetail;
    @SerializedName("education_name")
    private String educationName;
    @SerializedName("income")
    private String income;
    @SerializedName("occupation")
    private String occupation;
    @SerializedName("employee_in")
    private String employeeIn;
    @SerializedName("designation")
    private String designation;
    @SerializedName("religion")
    private String religion;
    @SerializedName("caste")
    private String caste;
    @SerializedName("mother_tongue")
    private String motherTongue;
    @SerializedName("height")
    private String height;
    @SerializedName("weight")
    private String weight;
    @SerializedName("country_id")
    private String countryId;
    @SerializedName("state_id")
    private Object stateId;
    @SerializedName("city")
    private Object city;
    @SerializedName("phone")
    private String phone;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("photo1")
    private String photo1;
    @SerializedName("photo1_approve")
    private String photo1Approve;
    @SerializedName("photo_protect")
    private String photoProtect;
    @SerializedName("photo_view_status")
    private String photoViewStatus;
    @SerializedName("photo_password")
    private String photoPassword;
    @SerializedName("status")
    private String status;
    @SerializedName("registered_on")
    private String registeredOn;
    @SerializedName("is_deleted")
    private String isDeleted;
    @SerializedName("registered_from")
    private String registeredFrom;
    @SerializedName("last_login")
    private String lastLogin;
    @SerializedName("fstatus")
    private String fstatus;
    @SerializedName("profile_text")
    private String profileText;
    @SerializedName("country_name")
    private String countryName;
    @SerializedName("state_name")
    private String stateName;
    @SerializedName("city_name")
    private String cityName;
    @SerializedName("religion_name")
    private String religionName;
    @SerializedName("caste_name")
    private String casteName;
    @SerializedName("occupation_name")
    private String occupationName;
    @SerializedName("mtongue_name")
    private String mtongueName;
    @SerializedName("designation_name")
    private String designationName;
    @SerializedName("age")
    private String age;
    @SerializedName("profile_description")
    private String profileDescription;
    @SerializedName("photoUrl")
    private String photoUrl;
    @SerializedName("badge")
    private String badge;
    @SerializedName("badgeUrl")
    private String badgeUrl;
    @SerializedName("color")
    private String color;
    @SerializedName("photo_view_count")
    private Integer photoViewCount;
    @SerializedName("action")
    private JsonArray action = null;
    @SerializedName("member_photo")
    private JsonArray memberPhoto = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatriId() {
        return matriId;
    }

    public void setMatriId(String matriId) {
        this.matriId = matriId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getProfileby() {
        return profileby;
    }

    public void setProfileby(String profileby) {
        this.profileby = profileby;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Object getLastname() {
        return lastname;
    }

    public void setLastname(Object lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getBirthtime() {
        return birthtime;
    }

    public void setBirthtime(String birthtime) {
        this.birthtime = birthtime;
    }

    public String getEducationDetail() {
        return educationDetail;
    }

    public void setEducationDetail(String educationDetail) {
        this.educationDetail = educationDetail;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEmployeeIn() {
        return employeeIn;
    }

    public void setEmployeeIn(String employeeIn) {
        this.employeeIn = employeeIn;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getMotherTongue() {
        return motherTongue;
    }

    public void setMotherTongue(String motherTongue) {
        this.motherTongue = motherTongue;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public Object getStateId() {
        return stateId;
    }

    public void setStateId(Object stateId) {
        this.stateId = stateId;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto1Approve() {
        return photo1Approve;
    }

    public void setPhoto1Approve(String photo1Approve) {
        this.photo1Approve = photo1Approve;
    }

    public String getPhotoProtect() {
        return photoProtect;
    }

    public void setPhotoProtect(String photoProtect) {
        this.photoProtect = photoProtect;
    }

    public String getPhotoViewStatus() {
        return photoViewStatus;
    }

    public void setPhotoViewStatus(String photoViewStatus) {
        this.photoViewStatus = photoViewStatus;
    }

    public String getPhotoPassword() {
        return photoPassword;
    }

    public void setPhotoPassword(String photoPassword) {
        this.photoPassword = photoPassword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getRegisteredFrom() {
        return registeredFrom;
    }

    public void setRegisteredFrom(String registeredFrom) {
        this.registeredFrom = registeredFrom;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getFstatus() {
        return fstatus;
    }

    public void setFstatus(String fstatus) {
        this.fstatus = fstatus;
    }

    public String getProfileText() {
        return profileText;
    }

    public void setProfileText(String profileText) {
        this.profileText = profileText;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getReligionName() {
        return religionName;
    }

    public void setReligionName(String religionName) {
        this.religionName = religionName;
    }

    public String getCasteName() {
        return casteName;
    }

    public void setCasteName(String casteName) {
        this.casteName = casteName;
    }

    public String getOccupationName() {
        return occupationName;
    }

    public void setOccupationName(String occupationName) {
        this.occupationName = occupationName;
    }

    public String getMtongueName() {
        return mtongueName;
    }

    public void setMtongueName(String mtongueName) {
        this.mtongueName = mtongueName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public Integer getPhotoViewCount() {
        return photoViewCount;
    }

    public void setPhotoViewCount(Integer photoViewCount) {
        this.photoViewCount = photoViewCount;
    }

    public JsonArray getAction() {
        return action;
    }

    public void setAction(JsonArray action) {
        this.action = action;
    }

    public JsonArray getMemberPhoto() {
        return memberPhoto;
    }

    public void setMemberPhoto(JsonArray memberPhoto) {
        this.memberPhoto = memberPhoto;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }
}