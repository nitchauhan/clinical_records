package com.example.clinicalrecord;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	private static String base_url = "http://192.168.0.106:80/clinical_records/endpoints";

	public static String get_BaseUrl() {
		return base_url;
	}

	public static boolean isEmailValid(String email) {
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/*
	 * public static Boolean Validate_Form_Registration(String email,String
	 * name,String shopname) { return name!=null && shopname != null &&
	 * isEmailValid(email); }
	 */

	public static Boolean Validate_Form_Login(String email, String password) {
		return password != null && isEmailValid(email);
	}

	// (DoctorName, EmailID, DoctorContactNo,ProfilePic,specilization))
	public static Boolean Validate_Form_DoctorsAdd(String DoctorName, String EmailID, String DoctorContactNo,
			String ProfilePic, String specilization) {
		return DoctorName != null && DoctorContactNo != null && specilization != null && isEmailValid(EmailID);
	}

	// (PatientCode, PatientName, mobile, Addresss, DOB, BloodGroup,
	// Gender,DoctorID);

	public static Boolean Validate_Form_PatientsAdd(String PatientCode, String PatientName, String mobile,
			String Addresss, String DOB, String BloodGroup, String Gender)// ,String DoctorID)
	{
		return PatientName != null && PatientCode != null && mobile != null && DOB != null && Gender != null
				&& BloodGroup != null; // && DoctorID !=null;
	}

	public static Boolean Validate_Form_PatientsDtl(String diesesName, String symptoms, String temparature,
			String dateTime, String needToAdmit, String situations) {
		return diesesName != null && symptoms != null && temparature != null && dateTime != null && needToAdmit != null
				&& situations != null; // && DoctorID !=null;
	}

	// patientName1, patientAdmitDate, patientDischargeDate, Ward
	public static Boolean Validate_Form_AdmitAdd(String patientName1, String patientAdmitDate,
			String patientDischargeDate, String Ward) {
		return patientName1 != null && patientAdmitDate != null && patientDischargeDate != null && Ward != null; // &&
																													// DoctorID
																													// !=null;
	}

}
