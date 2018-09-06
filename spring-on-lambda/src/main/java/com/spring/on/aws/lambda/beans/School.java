package com.spring.on.aws.lambda.beans;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;

public class School {

	private Long	createdOn;

	@NotNull(message = "School Address Cannot Be Null")
	@NotEmpty(message = "School Address Cannot Be Empty")
	@Length(min = 10, max = 250, message = "School Address Must Be Between 10 To 250 Length")
	private String	schoolAddress;

	@NotNull(message = "School Id Cannot Be Empty")
	@PositiveOrZero(message = "School Id Must Not Be Zero")
	private Long	schoolId;

	@NotNull(message = "School Name Cannot Be Null")
	@NotEmpty(message = "School Name Cannot Be Empty")
	@Length(min = 10, max = 250, message = "School Name Must Be Between 10 To 250 Length")
	private String	schoolName;

	@NotNull(message = "School Registration Number Cannot Be Null")
	@NotEmpty(message = "School Registration Number Cannot Be Empty")
	@Length(min = 10, max = 250, message = "School Registration Number Must Be Between 10 To 250 Length")
	private String	schoolRegistrationNumber;
	private String	status;
	private Long	updatedOn;

	@NotNull(message = "User Id Cannot Be Empty")
	@Positive(message = "User Id Must Not Be Zero")
	private Long	userId;

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public String getSchoolAddress() {
		return schoolAddress;
	}

	public void setSchoolAddress(String schoolAddress) {
		this.schoolAddress = schoolAddress;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSchoolRegistrationNumber() {
		return schoolRegistrationNumber;
	}

	public void setSchoolRegistrationNumber(String schoolRegistrationNumber) {
		this.schoolRegistrationNumber = schoolRegistrationNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Long updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "School [createdOn=" + createdOn + ", schoolAddress=" + schoolAddress + ", schoolId=" + schoolId + ", schoolName=" + schoolName
				+ ", schoolRegistrationNumber=" + schoolRegistrationNumber + ", status=" + status + ", updatedOn=" + updatedOn + ", userId=" + userId
				+ "]";
	}

}
