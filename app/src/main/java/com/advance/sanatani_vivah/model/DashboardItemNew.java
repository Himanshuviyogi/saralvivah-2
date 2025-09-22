package com.advance.sanatani_vivah.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashboardItemNew {
    @SerializedName("id")
    String id;
    @SerializedName("user_id")
    String userId;
    @SerializedName("fb_id")
    String fbId;
    @SerializedName("matri_id")
    String matriId;
    @SerializedName("prefix")
    String prefix;
    @SerializedName("title")
    String title;
    @SerializedName("description")
    String description;
    @SerializedName("keyword")
    String keyword;
    @SerializedName("terms")
    String terms;
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;
    @SerializedName("cpassword")
    String cpassword;
    @SerializedName("cpass_status")
    String cpassStatus;
    @SerializedName("marital_status")
    String maritalStatus;
    @SerializedName("profileby")
    String profileby;
    @SerializedName("time_to_call")
    String timeToCall;
    @SerializedName("reference")
    String reference;
    @SerializedName("username")
    String username;
    @SerializedName("firstname")
    String firstname;
    @SerializedName("lastname")
    String lastname;
    @SerializedName("gender")
    String gender;
    @SerializedName("birthdate")
    String birthdate;
    @SerializedName("birthtime")
    String birthtime;
    @SerializedName("birthplace")
    String birthplace;
    @SerializedName("total_children")
    String totalChildren;
    @SerializedName("status_children")
    String statusChildren;
    @SerializedName("education_detail")
    String educationDetail;
    @SerializedName("income")
    String income;
    @SerializedName("occupation")
    String occupation;
    @SerializedName("employee_in")
    String employeeIn;
    @SerializedName("designation")
    String designation;
    @SerializedName("latitude")
    String latitude;
    @SerializedName("longitude")
    String longitude;
    @SerializedName("religion")
    String religion;
    @SerializedName("caste")
    String caste;
    @SerializedName("subcaste")
    String subcaste;
    @SerializedName("gothra")
    String gothra;
    @SerializedName("star")
    String star;
    @SerializedName("moonsign")
    String moonsign;
    @SerializedName("horoscope")
    String horoscope;
    @SerializedName("manglik")
    String manglik;
    @SerializedName("mother_tongue")
    String motherTongue;
    @SerializedName("height")
    String height;
    @SerializedName("weight")
    String weight;
    @SerializedName("blood_group")
    String bloodGroup;
    @SerializedName("complexion")
    String complexion;
    @SerializedName("bodytype")
    String bodytype;
    @SerializedName("diet")
    String diet;
    @SerializedName("smoke")
    String smoke;
    @SerializedName("drink")
    String drink;
    @SerializedName("languages_known")
    String languagesKnown;
    @SerializedName("address")
    String address;
    @SerializedName("country_id")
    String countryId;
    @SerializedName("state_id")
    String stateId;
    @SerializedName("city")
    String city;
    @SerializedName("phone")
    String phone;
    @SerializedName("mobile")
    String mobile;
    @SerializedName("contact_view_security")
    String contactViewSecurity;
    @SerializedName("residence")
    String residence;
    @SerializedName("father_name")
    String fatherName;
    @SerializedName("mother_name")
    String motherName;
    @SerializedName("father_living_status")
    String fatherLivingStatus;
    @SerializedName("mother_living_status")
    String motherLivingStatus;
    @SerializedName("father_occupation")
    String fatherOccupation;
    @SerializedName("mother_occupation")
    String motherOccupation;
    @SerializedName("profile_text")
    String profileText;
    @SerializedName("looking_for")
    String lookingFor;
    @SerializedName("family_details")
    String familyDetails;
    @SerializedName("family_type")
    String familyType;
    @SerializedName("family_status")
    String familyStatus;
    @SerializedName("no_of_brothers")
    String noOfBrothers;
    @SerializedName("no_of_sisters")
    String noOfSisters;
    @SerializedName("no_of_married_brother")
    String noOfMarriedBrother;
    @SerializedName("no_of_married_sister")
    String noOfMarriedSister;
    @SerializedName("part_frm_age")
    String partFrmAge;
    @SerializedName("part_to_age")
    String partToAge;
    @SerializedName("part_bodytype")
    String partBodytype;
    @SerializedName("part_diet")
    String partDiet;
    @SerializedName("part_smoke")
    String partSmoke;
    @SerializedName("part_drink")
    String partDrink;
    @SerializedName("part_income")
    String partIncome;
    @SerializedName("part_employee_in")
    String partEmployeeIn;
    @SerializedName("part_occupation")
    String partOccupation;
    @SerializedName("part_designation")
    String partDesignation;
    @SerializedName("part_expect")
    String partExpect;
    @SerializedName("part_height")
    String partHeight;
    @SerializedName("part_height_to")
    String partHeightTo;
    @SerializedName("part_complexion")
    String partComplexion;
    @SerializedName("part_mother_tongue")
    String partMotherTongue;
    @SerializedName("android_device_id")
    String androidDeviceId;
    @SerializedName("ios_device_id")
    String iosDeviceId;
    @SerializedName("web_device_id")
    String webDeviceId;
    @SerializedName("part_religion")
    String partReligion;
    @SerializedName("part_caste")
    String partCaste;
    @SerializedName("part_manglik")
    String partManglik;
    @SerializedName("part_star")
    String partStar;
    @SerializedName("part_education")
    String partEducation;
    @SerializedName("part_country_living")
    String partCountryLiving;
    @SerializedName("part_state")
    String partState;
    @SerializedName("part_city")
    String partCity;
    @SerializedName("part_resi_status")
    String partResiStatus;
    @SerializedName("hobby")
    String hobby;
    @SerializedName("horoscope_photo_approve")
    String horoscopePhotoApprove;
    @SerializedName("horoscope_photo")
    String horoscopePhoto;
    @SerializedName("photo_protect")
    String photoProtect;
    @SerializedName("photo_password")
    String photoPassword;
    @SerializedName("video")
    String video;
    @SerializedName("video_approval")
    String videoApproval;
    @SerializedName("video_url")
    String videoUrl;
    @SerializedName("video_view_status")
    String videoViewStatus;
    @SerializedName("photo_view_status")
    String photoViewStatus;
    @SerializedName("photo1")
    String photo1;
    @SerializedName("photo1_approve")
    String photo1Approve;
    @SerializedName("photo2")
    String photo2;
    @SerializedName("photo2_approve")
    String photo2Approve;
    @SerializedName("photo3")
    String photo3;
    @SerializedName("photo3_approve")
    String photo3Approve;
    @SerializedName("photo4")
    String photo4;
    @SerializedName("photo4_approve")
    String photo4Approve;
    @SerializedName("photo5")
    String photo5;
    @SerializedName("photo5_approve")
    String photo5Approve;
    @SerializedName("photo6")
    String photo6;
    @SerializedName("photo6_approve")
    String photo6Approve;
    @SerializedName("photo7")
    String photo7;
    @SerializedName("photo7_approve")
    String photo7Approve;
    @SerializedName("photo8")
    String photo8;
    @SerializedName("photo8_approve")
    String photo8Approve;
    @SerializedName("photo1_uploaded_on")
    String photo1UploadedOn;
    @SerializedName("photo2_uploaded_on")
    String photo2UploadedOn;
    @SerializedName("photo3_uploaded_on")
    String photo3UploadedOn;
    @SerializedName("photo4_uploaded_on")
    String photo4UploadedOn;
    @SerializedName("photo5_uploaded_on")
    String photo5UploadedOn;
    @SerializedName("photo6_uploaded_on")
    String photo6UploadedOn;
    @SerializedName("photo7_uploaded_on")
    String photo7UploadedOn;
    @SerializedName("photo8_uploaded_on")
    String photo8UploadedOn;
    @SerializedName("registered_on")
    String registeredOn;
    @SerializedName("ip")
    String ip;
    @SerializedName("agent")
    String agent;
    @SerializedName("agent_approve")
    String agentApprove;
    @SerializedName("last_login")
    String lastLogin;
    @SerializedName("status")
    String status;
    @SerializedName("fstatus")
    String fstatus;
    @SerializedName("logged_in")
    String loggedIn;
    @SerializedName("adminrole_id")
    String adminroleId;
    @SerializedName("franchised_by")
    String franchisedBy;
    @SerializedName("staff_assign_id")
    String staffAssignId;
    @SerializedName("franchise_assign_id")
    String franchiseAssignId;
    @SerializedName("staff_assign_date")
    String staffAssignDate;
    @SerializedName("franchise_assign_date")
    String franchiseAssignDate;
    @SerializedName("commented")
    String commented;
    @SerializedName("adminrole_view_status")
    String adminroleViewStatus;
    @SerializedName("mobile_verify_status")
    String mobileVerifyStatus;
    @SerializedName("plan_id")
    String planId;
    @SerializedName("plan_name")
    String planName;
    @SerializedName("plan_status")
    String planStatus;
    @SerializedName("plan_expired_on")
    String planExpiredOn;
    @SerializedName("is_deleted")
    String isDeleted;
    @SerializedName("id_proof")
    String idProof;
    @SerializedName("id_proof_approve")
    String idProofApprove;
    @SerializedName("id_proof_uploaded_on")
    String idProofUploadedOn;
    @SerializedName("horoscope_photo_uploaded_on")
    String horoscopePhotoUploadedOn;
    @SerializedName("registered_from")
    String registeredFrom;
    @SerializedName("cover_photo")
    String coverPhoto;
    @SerializedName("cover_photo_approve")
    String coverPhotoApprove;
    @SerializedName("cover_photo_uploaded_on")
    String coverPhotoUploadedOn;
    @SerializedName("country_name")
    String countryName;
    @SerializedName("state_name")
    String stateName;
    @SerializedName("city_name")
    String cityName;
    @SerializedName("religion_name")
    String religionName;
    @SerializedName("caste_name")
    String casteName;
    @SerializedName("education_name")
    String educationName;
    @SerializedName("occupation_name")
    String occupationName;
    @SerializedName("mtongue_name")
    String mtongueName;
    @SerializedName("designation_name")
    String designationName;
    @SerializedName("assign_to_staff")
    String assignToStaff;
    @SerializedName("assign_to_franchise")
    String assignToFranchise;
    @SerializedName("age")
    String age;
    @SerializedName("profile_description")
    String profileDescription;
    @SerializedName("photo_view_count")
    int photoViewCount;
    @SerializedName("photoUrl")
    String photoUrl;
    @SerializedName("badgeUrl")
    String badgeUrl;
    @SerializedName("badge")
    String badge;
    @SerializedName("color")
    String color;
    @SerializedName("action")
    List<MemberAction> action = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getMatriId() {
        return matriId;
    }

    public void setMatriId(String matriId) {
        this.matriId = matriId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpassword() {
        return cpassword;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public String getCpassStatus() {
        return cpassStatus;
    }

    public void setCpassStatus(String cpassStatus) {
        this.cpassStatus = cpassStatus;
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

    public String getTimeToCall() {
        return timeToCall;
    }

    public void setTimeToCall(String timeToCall) {
        this.timeToCall = timeToCall;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
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

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getTotalChildren() {
        return totalChildren;
    }

    public void setTotalChildren(String totalChildren) {
        this.totalChildren = totalChildren;
    }

    public String getStatusChildren() {
        return statusChildren;
    }

    public void setStatusChildren(String statusChildren) {
        this.statusChildren = statusChildren;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getSubcaste() {
        return subcaste;
    }

    public void setSubcaste(String subcaste) {
        this.subcaste = subcaste;
    }

    public String getGothra() {
        return gothra;
    }

    public void setGothra(String gothra) {
        this.gothra = gothra;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getMoonsign() {
        return moonsign;
    }

    public void setMoonsign(String moonsign) {
        this.moonsign = moonsign;
    }

    public String getHoroscope() {
        return horoscope;
    }

    public void setHoroscope(String horoscope) {
        this.horoscope = horoscope;
    }

    public String getManglik() {
        return manglik;
    }

    public void setManglik(String manglik) {
        this.manglik = manglik;
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

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getComplexion() {
        return complexion;
    }

    public void setComplexion(String complexion) {
        this.complexion = complexion;
    }

    public String getBodytype() {
        return bodytype;
    }

    public void setBodytype(String bodytype) {
        this.bodytype = bodytype;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getLanguagesKnown() {
        return languagesKnown;
    }

    public void setLanguagesKnown(String languagesKnown) {
        this.languagesKnown = languagesKnown;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
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

    public String getContactViewSecurity() {
        return contactViewSecurity;
    }

    public void setContactViewSecurity(String contactViewSecurity) {
        this.contactViewSecurity = contactViewSecurity;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherLivingStatus() {
        return fatherLivingStatus;
    }

    public void setFatherLivingStatus(String fatherLivingStatus) {
        this.fatherLivingStatus = fatherLivingStatus;
    }

    public String getMotherLivingStatus() {
        return motherLivingStatus;
    }

    public void setMotherLivingStatus(String motherLivingStatus) {
        this.motherLivingStatus = motherLivingStatus;
    }

    public String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public String getMotherOccupation() {
        return motherOccupation;
    }

    public void setMotherOccupation(String motherOccupation) {
        this.motherOccupation = motherOccupation;
    }

    public String getProfileText() {
        return profileText;
    }

    public void setProfileText(String profileText) {
        this.profileText = profileText;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getFamilyDetails() {
        return familyDetails;
    }

    public void setFamilyDetails(String familyDetails) {
        this.familyDetails = familyDetails;
    }

    public String getFamilyType() {
        return familyType;
    }

    public void setFamilyType(String familyType) {
        this.familyType = familyType;
    }

    public String getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(String familyStatus) {
        this.familyStatus = familyStatus;
    }

    public String getNoOfBrothers() {
        return noOfBrothers;
    }

    public void setNoOfBrothers(String noOfBrothers) {
        this.noOfBrothers = noOfBrothers;
    }

    public String getNoOfSisters() {
        return noOfSisters;
    }

    public void setNoOfSisters(String noOfSisters) {
        this.noOfSisters = noOfSisters;
    }

    public String getNoOfMarriedBrother() {
        return noOfMarriedBrother;
    }

    public void setNoOfMarriedBrother(String noOfMarriedBrother) {
        this.noOfMarriedBrother = noOfMarriedBrother;
    }

    public String getNoOfMarriedSister() {
        return noOfMarriedSister;
    }

    public void setNoOfMarriedSister(String noOfMarriedSister) {
        this.noOfMarriedSister = noOfMarriedSister;
    }

    public String getPartFrmAge() {
        return partFrmAge;
    }

    public void setPartFrmAge(String partFrmAge) {
        this.partFrmAge = partFrmAge;
    }

    public String getPartToAge() {
        return partToAge;
    }

    public void setPartToAge(String partToAge) {
        this.partToAge = partToAge;
    }

    public String getPartBodytype() {
        return partBodytype;
    }

    public void setPartBodytype(String partBodytype) {
        this.partBodytype = partBodytype;
    }

    public String getPartDiet() {
        return partDiet;
    }

    public void setPartDiet(String partDiet) {
        this.partDiet = partDiet;
    }

    public String getPartSmoke() {
        return partSmoke;
    }

    public void setPartSmoke(String partSmoke) {
        this.partSmoke = partSmoke;
    }

    public String getPartDrink() {
        return partDrink;
    }

    public void setPartDrink(String partDrink) {
        this.partDrink = partDrink;
    }

    public String getPartIncome() {
        return partIncome;
    }

    public void setPartIncome(String partIncome) {
        this.partIncome = partIncome;
    }

    public String getPartEmployeeIn() {
        return partEmployeeIn;
    }

    public void setPartEmployeeIn(String partEmployeeIn) {
        this.partEmployeeIn = partEmployeeIn;
    }

    public String getPartOccupation() {
        return partOccupation;
    }

    public void setPartOccupation(String partOccupation) {
        this.partOccupation = partOccupation;
    }

    public String getPartDesignation() {
        return partDesignation;
    }

    public void setPartDesignation(String partDesignation) {
        this.partDesignation = partDesignation;
    }

    public String getPartExpect() {
        return partExpect;
    }

    public void setPartExpect(String partExpect) {
        this.partExpect = partExpect;
    }

    public String getPartHeight() {
        return partHeight;
    }

    public void setPartHeight(String partHeight) {
        this.partHeight = partHeight;
    }

    public String getPartHeightTo() {
        return partHeightTo;
    }

    public void setPartHeightTo(String partHeightTo) {
        this.partHeightTo = partHeightTo;
    }

    public String getPartComplexion() {
        return partComplexion;
    }

    public void setPartComplexion(String partComplexion) {
        this.partComplexion = partComplexion;
    }

    public String getPartMotherTongue() {
        return partMotherTongue;
    }

    public void setPartMotherTongue(String partMotherTongue) {
        this.partMotherTongue = partMotherTongue;
    }

    public String getAndroidDeviceId() {
        return androidDeviceId;
    }

    public void setAndroidDeviceId(String androidDeviceId) {
        this.androidDeviceId = androidDeviceId;
    }

    public String getIosDeviceId() {
        return iosDeviceId;
    }

    public void setIosDeviceId(String iosDeviceId) {
        this.iosDeviceId = iosDeviceId;
    }

    public String getWebDeviceId() {
        return webDeviceId;
    }

    public void setWebDeviceId(String webDeviceId) {
        this.webDeviceId = webDeviceId;
    }

    public String getPartReligion() {
        return partReligion;
    }

    public void setPartReligion(String partReligion) {
        this.partReligion = partReligion;
    }

    public String getPartCaste() {
        return partCaste;
    }

    public void setPartCaste(String partCaste) {
        this.partCaste = partCaste;
    }

    public String getPartManglik() {
        return partManglik;
    }

    public void setPartManglik(String partManglik) {
        this.partManglik = partManglik;
    }

    public String getPartStar() {
        return partStar;
    }

    public void setPartStar(String partStar) {
        this.partStar = partStar;
    }

    public String getPartEducation() {
        return partEducation;
    }

    public void setPartEducation(String partEducation) {
        this.partEducation = partEducation;
    }

    public String getPartCountryLiving() {
        return partCountryLiving;
    }

    public void setPartCountryLiving(String partCountryLiving) {
        this.partCountryLiving = partCountryLiving;
    }

    public String getPartState() {
        return partState;
    }

    public void setPartState(String partState) {
        this.partState = partState;
    }

    public String getPartCity() {
        return partCity;
    }

    public void setPartCity(String partCity) {
        this.partCity = partCity;
    }

    public String getPartResiStatus() {
        return partResiStatus;
    }

    public void setPartResiStatus(String partResiStatus) {
        this.partResiStatus = partResiStatus;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getHoroscopePhotoApprove() {
        return horoscopePhotoApprove;
    }

    public void setHoroscopePhotoApprove(String horoscopePhotoApprove) {
        this.horoscopePhotoApprove = horoscopePhotoApprove;
    }

    public String getHoroscopePhoto() {
        return horoscopePhoto;
    }

    public void setHoroscopePhoto(String horoscopePhoto) {
        this.horoscopePhoto = horoscopePhoto;
    }

    public String getPhotoProtect() {
        return photoProtect;
    }

    public void setPhotoProtect(String photoProtect) {
        this.photoProtect = photoProtect;
    }

    public String getPhotoPassword() {
        return photoPassword;
    }

    public void setPhotoPassword(String photoPassword) {
        this.photoPassword = photoPassword;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoApproval() {
        return videoApproval;
    }

    public void setVideoApproval(String videoApproval) {
        this.videoApproval = videoApproval;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoViewStatus() {
        return videoViewStatus;
    }

    public void setVideoViewStatus(String videoViewStatus) {
        this.videoViewStatus = videoViewStatus;
    }

    public String getPhotoViewStatus() {
        return photoViewStatus;
    }

    public void setPhotoViewStatus(String photoViewStatus) {
        this.photoViewStatus = photoViewStatus;
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

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto2Approve() {
        return photo2Approve;
    }

    public void setPhoto2Approve(String photo2Approve) {
        this.photo2Approve = photo2Approve;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto3Approve() {
        return photo3Approve;
    }

    public void setPhoto3Approve(String photo3Approve) {
        this.photo3Approve = photo3Approve;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getPhoto4Approve() {
        return photo4Approve;
    }

    public void setPhoto4Approve(String photo4Approve) {
        this.photo4Approve = photo4Approve;
    }

    public String getPhoto5() {
        return photo5;
    }

    public void setPhoto5(String photo5) {
        this.photo5 = photo5;
    }

    public String getPhoto5Approve() {
        return photo5Approve;
    }

    public void setPhoto5Approve(String photo5Approve) {
        this.photo5Approve = photo5Approve;
    }

    public String getPhoto6() {
        return photo6;
    }

    public void setPhoto6(String photo6) {
        this.photo6 = photo6;
    }

    public String getPhoto6Approve() {
        return photo6Approve;
    }

    public void setPhoto6Approve(String photo6Approve) {
        this.photo6Approve = photo6Approve;
    }

    public String getPhoto7() {
        return photo7;
    }

    public void setPhoto7(String photo7) {
        this.photo7 = photo7;
    }

    public String getPhoto7Approve() {
        return photo7Approve;
    }

    public void setPhoto7Approve(String photo7Approve) {
        this.photo7Approve = photo7Approve;
    }

    public String getPhoto8() {
        return photo8;
    }

    public void setPhoto8(String photo8) {
        this.photo8 = photo8;
    }

    public String getPhoto8Approve() {
        return photo8Approve;
    }

    public void setPhoto8Approve(String photo8Approve) {
        this.photo8Approve = photo8Approve;
    }

    public String getPhoto1UploadedOn() {
        return photo1UploadedOn;
    }

    public void setPhoto1UploadedOn(String photo1UploadedOn) {
        this.photo1UploadedOn = photo1UploadedOn;
    }

    public String getPhoto2UploadedOn() {
        return photo2UploadedOn;
    }

    public void setPhoto2UploadedOn(String photo2UploadedOn) {
        this.photo2UploadedOn = photo2UploadedOn;
    }

    public String getPhoto3UploadedOn() {
        return photo3UploadedOn;
    }

    public void setPhoto3UploadedOn(String photo3UploadedOn) {
        this.photo3UploadedOn = photo3UploadedOn;
    }

    public String getPhoto4UploadedOn() {
        return photo4UploadedOn;
    }

    public void setPhoto4UploadedOn(String photo4UploadedOn) {
        this.photo4UploadedOn = photo4UploadedOn;
    }

    public String getPhoto5UploadedOn() {
        return photo5UploadedOn;
    }

    public void setPhoto5UploadedOn(String photo5UploadedOn) {
        this.photo5UploadedOn = photo5UploadedOn;
    }

    public String getPhoto6UploadedOn() {
        return photo6UploadedOn;
    }

    public void setPhoto6UploadedOn(String photo6UploadedOn) {
        this.photo6UploadedOn = photo6UploadedOn;
    }

    public String getPhoto7UploadedOn() {
        return photo7UploadedOn;
    }

    public void setPhoto7UploadedOn(String photo7UploadedOn) {
        this.photo7UploadedOn = photo7UploadedOn;
    }

    public String getPhoto8UploadedOn() {
        return photo8UploadedOn;
    }

    public void setPhoto8UploadedOn(String photo8UploadedOn) {
        this.photo8UploadedOn = photo8UploadedOn;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAgentApprove() {
        return agentApprove;
    }

    public void setAgentApprove(String agentApprove) {
        this.agentApprove = agentApprove;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFstatus() {
        return fstatus;
    }

    public void setFstatus(String fstatus) {
        this.fstatus = fstatus;
    }

    public String getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(String loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getAdminroleId() {
        return adminroleId;
    }

    public void setAdminroleId(String adminroleId) {
        this.adminroleId = adminroleId;
    }

    public String getFranchisedBy() {
        return franchisedBy;
    }

    public void setFranchisedBy(String franchisedBy) {
        this.franchisedBy = franchisedBy;
    }

    public String getStaffAssignId() {
        return staffAssignId;
    }

    public void setStaffAssignId(String staffAssignId) {
        this.staffAssignId = staffAssignId;
    }

    public String getFranchiseAssignId() {
        return franchiseAssignId;
    }

    public void setFranchiseAssignId(String franchiseAssignId) {
        this.franchiseAssignId = franchiseAssignId;
    }

    public String getStaffAssignDate() {
        return staffAssignDate;
    }

    public void setStaffAssignDate(String staffAssignDate) {
        this.staffAssignDate = staffAssignDate;
    }

    public String getFranchiseAssignDate() {
        return franchiseAssignDate;
    }

    public void setFranchiseAssignDate(String franchiseAssignDate) {
        this.franchiseAssignDate = franchiseAssignDate;
    }

    public String getCommented() {
        return commented;
    }

    public void setCommented(String commented) {
        this.commented = commented;
    }

    public String getAdminroleViewStatus() {
        return adminroleViewStatus;
    }

    public void setAdminroleViewStatus(String adminroleViewStatus) {
        this.adminroleViewStatus = adminroleViewStatus;
    }

    public String getMobileVerifyStatus() {
        return mobileVerifyStatus;
    }

    public void setMobileVerifyStatus(String mobileVerifyStatus) {
        this.mobileVerifyStatus = mobileVerifyStatus;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getPlanExpiredOn() {
        return planExpiredOn;
    }

    public void setPlanExpiredOn(String planExpiredOn) {
        this.planExpiredOn = planExpiredOn;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public String getIdProofApprove() {
        return idProofApprove;
    }

    public void setIdProofApprove(String idProofApprove) {
        this.idProofApprove = idProofApprove;
    }

    public String getIdProofUploadedOn() {
        return idProofUploadedOn;
    }

    public void setIdProofUploadedOn(String idProofUploadedOn) {
        this.idProofUploadedOn = idProofUploadedOn;
    }

    public String getHoroscopePhotoUploadedOn() {
        return horoscopePhotoUploadedOn;
    }

    public void setHoroscopePhotoUploadedOn(String horoscopePhotoUploadedOn) {
        this.horoscopePhotoUploadedOn = horoscopePhotoUploadedOn;
    }

    public String getRegisteredFrom() {
        return registeredFrom;
    }

    public void setRegisteredFrom(String registeredFrom) {
        this.registeredFrom = registeredFrom;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getCoverPhotoApprove() {
        return coverPhotoApprove;
    }

    public void setCoverPhotoApprove(String coverPhotoApprove) {
        this.coverPhotoApprove = coverPhotoApprove;
    }

    public String getCoverPhotoUploadedOn() {
        return coverPhotoUploadedOn;
    }

    public void setCoverPhotoUploadedOn(String coverPhotoUploadedOn) {
        this.coverPhotoUploadedOn = coverPhotoUploadedOn;
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

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
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

    public String getAssignToStaff() {
        return assignToStaff;
    }

    public void setAssignToStaff(String assignToStaff) {
        this.assignToStaff = assignToStaff;
    }

    public String getAssignToFranchise() {
        return assignToFranchise;
    }

    public void setAssignToFranchise(String assignToFranchise) {
        this.assignToFranchise = assignToFranchise;
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

    public int getPhotoViewCount() {
        return photoViewCount;
    }

    public void setPhotoViewCount(int photoViewCount) {
        this.photoViewCount = photoViewCount;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<MemberAction> getAction() {
        return action;
    }

    public void setAction(List<MemberAction> action) {
        this.action = action;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
