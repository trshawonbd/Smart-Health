package com.ppal007.smarthealth.retrofit;

import com.ppal007.smarthealth.model.ModelAcceptPatient;
import com.ppal007.smarthealth.model.ModelAlert;
import com.ppal007.smarthealth.model.ModelBookingSchedule;
import com.ppal007.smarthealth.model.ModelDoctorSchedule;
import com.ppal007.smarthealth.model.ModelForgotPass;
import com.ppal007.smarthealth.model.ModelLogin;
import com.ppal007.smarthealth.model.ModelMessage;
import com.ppal007.smarthealth.model.ModelPatientRequestList;
import com.ppal007.smarthealth.model.ModelPatientSymptomPrescription;
import com.ppal007.smarthealth.model.ModelPatientSymptoms;
import com.ppal007.smarthealth.model.ModelPrescriptionMsg;
import com.ppal007.smarthealth.model.ModelRegisterAsDoctor;
import com.ppal007.smarthealth.model.ModelRegisterAsPatient;
import com.ppal007.smarthealth.model.ModelRequestCount;
import com.ppal007.smarthealth.model.ModelRetrieveDoctorName;
import com.ppal007.smarthealth.model.ModelService;
import com.ppal007.smarthealth.model.ModelServiceDoctorDefaultValue;
import com.ppal007.smarthealth.model.PatientModel;
import com.ppal007.smarthealth.model.RegiDoctor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
//    register as doctor
    @FormUrlEncoded
    @POST("insert_doctor_registration_info.php")
    Call<RegiDoctor> register_doctor(
            @Field("name") String Name,
            @Field("email") String Email,
            @Field("phone") String Phone,
            @Field("pass") String Pass,
            @Field("profile_img_path") String ProfileImgPath,
            @Field("img_path") String ImagePath,
            @Field("location") String Location
    );

//    registration as patient
    @FormUrlEncoded
    @POST("insert_patient_registration_info.php")
    Call<ModelRegisterAsPatient> register_patient(
            @Field("name") String Name,
            @Field("email") String Email,
            @Field("phone") String Phone,
            @Field("id_number") String IdNumber,
            @Field("age") String Age,
            @Field("gender") String Gender,
            @Field("pass") String Pass,
            @Field("doctor_id") String DoctorId,
            @Field("profile_img_path") String ProfileImgPath
    );

//    login as doctor and patient
    @FormUrlEncoded
    @POST("user_Login.php")
    Call<ModelLogin> user_Login(
            @Field("mood") String Mood,
            @Field("email") String Email,
            @Field("pass") String Password
    );

//    check user email and phone
    @FormUrlEncoded
    @POST("check_email_phone.php")
    Call<ModelForgotPass> check_User_Email_And_Phone(
            @Field("mode") String Mode,
            @Field("email") String Email,
            @Field("phone") String PhoneNumber
    );

//    update password
    @FormUrlEncoded
    @POST("update_user_password.php")
    Call<ModelForgotPass> update_User_Pass(
            @Field("mode") String Mode,
            @Field("id") String Id,
            @Field("pass") String Password
    );

//    retrieve doctor name
    @FormUrlEncoded
    @POST("retrieve_doctor_name.php")
    Call<List<ModelRetrieveDoctorName>> retrieve_doctor_name(
            @Field("location") String Location
    );

//    retrieve request list
    @FormUrlEncoded
    @POST("retrieve_patient_request_list.php")
    Call<List<ModelPatientRequestList>> retrieve_patient_request_list(
            @Field("doctor_id") String DoctorId
    );

//    accept patient
    @FormUrlEncoded
    @POST("accept_patient.php")
    Call<ModelAcceptPatient> accept_patient(
            @Field("doctor_id") String DoctorId,
            @Field("id") String Id
    );

//    retrieve patient accept list
    @FormUrlEncoded
    @POST("retrieve_patient_accept_list.php")
    Call<List<ModelBookingSchedule>> retrieve_patient_accept_booking(
            @Field("doctor_id") String DoctorId
    );

//    retrieve patient details match by booking doctor id
    @FormUrlEncoded
    @POST("retrieve_patient_details_match_by_booking_doctor_id.php")
    Call<List<ModelPatientRequestList>> retrieve_patient_detail_match_by_booking_doctor_id(
            @Field("doctor_id") String DoctorId
    );

//    get my patient list
    @FormUrlEncoded
    @POST("retrieve_my_patient.php")
    Call<List<ModelPatientRequestList>> retrieve_my_patient(
            @Field("doctor_id") String DoctorId
    );
//    retrieve symptom list
    @FormUrlEncoded
    @POST("retrieve_symptom_list.php")
    Call<List<ModelPatientSymptoms>> retrieve_symptom_list(
            @Field("patient_tbl_id") String PatientTblList
    );

//    create doctor schedule
    @FormUrlEncoded
    @POST("create_doctor_schedule.php")
    Call<ModelDoctorSchedule> create_doctor_schedule(
            @Field("doctor_id") String DoctorId,
            @Field("schedule_date") String Date,
            @Field("schedule_time_start") String ScheduleTimeStart,
            @Field("schedule_time_end") String ScheduleTimeEnd
    );

//    retrieve doctor schedules
    @FormUrlEncoded
    @POST("retrieve_doctor_schedules.php")
    Call<ModelDoctorSchedule> retrieve_doctor_schedule(
            @Field("doctor_id") String DoctorId
    );
//    retrieve doctor schedule from patient
    @FormUrlEncoded
    @POST("retrieve_doctor_schedule_from_patient.php")
    Call<ModelDoctorSchedule> retrieve_doctor_schedule_from_patient(
            @Field("id") String ID
    );

//    delete doctor schedule
    @FormUrlEncoded
    @POST("delete_doctor_schedule.php")
    Call<ModelDoctorSchedule> delete_doctor_schedule(
            @Field("doctor_id") String DoctorId
    );

//    send patient symptoms
    @FormUrlEncoded
    @POST("send_patient_symptoms_to_doctor.php")
    Call<ModelPatientSymptoms> send_patient_symptoms_to_doctor(
            @Field("patient_tbl_id") String PatientTblId,
            @Field("bp_up") String BpUp,
            @Field("bp_down") String BpDown,
            @Field("spo2") String Spo2,
            @Field("ecg") String Ecg,
            @Field("gl_befr_eat") String GlBfrEat,
            @Field("gl_aftr_eat") String GlAfrEat,
            @Field("temperature") String Temperature,
            @Field("submit_time") String SubmitTime
    );

//    set patient symptom from doctor
    @FormUrlEncoded
    @POST("set_patient_symptoms_from_doctor.php")
    Call<ModelPatientSymptoms> set_patient_symptoms_from_doctor(
            @Field("patient_tbl_id") String PatientTblId,
            @Field("bp_up") String BpUp,
            @Field("bp_down") String BpDown,
            @Field("temperature") String Temperature,
            @Field("spo2") String Spo2,
            @Field("ecg") String Ecg,
            @Field("gl_befr_eat") String GlBfrEat,
            @Field("gl_aftr_eat") String GlAfrEat

    );

//    retrieve patient default value
    @FormUrlEncoded
    @POST("retrieve_patient_default_value.php")
    Call<ModelPatientSymptoms> retrieve_patient_default_value(
            @Field("patient_tbl_id") String PatientTblId
    );

//    send message
    @FormUrlEncoded
    @POST("send_message.php")
    Call<ModelMessage> send_message(
            @Field("patient_id") String PatientId,
            @Field("msg") String Msg,
            @Field("msg_time") String MsgTime
    );

//    retrieve patient list who messages
    @GET("retrieve_location.php")
    Call<List<ModelRegisterAsDoctor>> retrieve_doctor_location();

//    retrieve message details
    @FormUrlEncoded
    @POST("retrieve_message_details.php")
    Call<List<ModelMessage>> retrieve_message_details(
            @Field("patient_id") String PatientId
    );

//    schedule booking
    @FormUrlEncoded
    @POST("booking_schedule.php")
    Call<ModelBookingSchedule> booking_schedule(
            @Field("patient_id") String PatientId,
            @Field("schedule_boking_time") String ScheduleBookingTime,
            @Field("position") String SchedulePosition
    );

//    retrieve schedule booking list
    @FormUrlEncoded
    @POST("retrieve_schedule_booking_list.php")
    Call<List<ModelBookingSchedule>> retrieve_booking_schedule_list(
            @Field("doctor_id") String DoctorId
    );

//    send prescription message
    @FormUrlEncoded
    @POST("send_prescription_in_message.php")
    Call<ModelPrescriptionMsg> send_prescription_msg(
            @Field("msg_tbl_id") String MsgTblId,
            @Field("patient_id") String PatientId,
            @Field("doctor_id") String DoctorId,
            @Field("prescription") String Prescription,
            @Field("pres_time") String PrescriptionTime
    );

//    retrieve prescription message
    @FormUrlEncoded
    @POST("retrieve_msg_from_patient.php")
    Call<List<ModelMessage>> retrieve_message_from_patient(
            @Field("patient_id") String PatientId
    );

    //get doctor msg prescription
    @FormUrlEncoded
    @POST("retrieve_message_prescription.php")
    Call<List<ModelPrescriptionMsg>> retrieve_doctor_msg_prescription(
            @Field("msg_tbl_id") String MsgTblId
    );

//    retrieve doctor info
    @FormUrlEncoded
    @POST("retrieve_doctor_info_for_message_prescription.php")
    Call<ModelRegisterAsDoctor> retrieve_doctor_info_for_msg_prescription(
            @Field("id") String ID
    );

//    retrieve doctor name
    @FormUrlEncoded
    @POST("retrieve_doctor_info.php")
    Call<ModelRegisterAsDoctor> retrieve_doctor_info(
            @Field("id") String PatientId
    );

//    send prescription for patient symptom
    @FormUrlEncoded
    @POST("send_prescription_to_patient_symptom.php")
    Call<ModelPatientSymptomPrescription> send_prescription_to_patient_symptom(
            @Field("symptom_tbl_id") String SymptomTblId,
            @Field("prescription") String Prescription,
            @Field("pres_time") String PresTime,
            @Field("pres_year_month") String PresYearMonth
    );

    //retrieve prescription for symptom
    @FormUrlEncoded
    @POST("retrieve_symptom_prescription.php")
    Call<List<ModelPatientSymptomPrescription>> retrieve_symptom_prescription(
            @Field("id") String PatientId
    );

    //retrieve prescription history
    @FormUrlEncoded
    @POST("retrieve_pres_history.php")
    Call<List<ModelPatientSymptomPrescription>> retrieve_pres_history(
            @Field("id") String PatientId,
            @Field("pres_year_month") String PresYearMonth
    );

    //retrieve message
    @FormUrlEncoded
    @POST("retrieve_msg_list_by_msg_tbl_id.php")
    Call<ModelMessage> retrieve_message_by_msg_tbl_id(
            @Field("id") String MsgTblID
    );

    //retrieve symptom list
    @FormUrlEncoded
    @POST("retrieve_symptom_by_symptom_tbl_id.php")
    Call<ModelPatientSymptoms> retrieve_patient_symptom_by_symptom_tbl_id(
            @Field("id") String SymptomTblId
    );

    //count patient request number
    @FormUrlEncoded
    @POST("count_patient_request.php")
    Call<ModelRequestCount> count_patient_request(
            @Field("doctor_id") String DoctorId
    );

    //get alert
    @FormUrlEncoded
    @POST("get_alert.php")
    Call<ModelAlert> get_alert(
            @Field("doctor_id") String DoctorId
    );

    //update db for stop notification double time
    @FormUrlEncoded
    @POST("update_db_stop_notification.php")
    Call<ModelAlert> update_db_stop_notification_double_time(
            @Field("patient_tbl_id") String PatientTblId
    );

    //get patient information
    @FormUrlEncoded
    @POST("get_patient_information.php")
    Call<PatientModel> get_patient_name(
            @Field("id") String Id
    );

}
