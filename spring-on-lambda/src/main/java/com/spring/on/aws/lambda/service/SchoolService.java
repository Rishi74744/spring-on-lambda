package com.spring.on.aws.lambda.service;

import java.sql.SQLException;
import java.util.List;

import com.spring.on.aws.lambda.beans.School;

public interface SchoolService {

	public long addSchool(School school) throws SQLException;

	public boolean updateSchool(School school) throws SQLException;

	public boolean checkIfSchoolExistsBySchooId(School school) throws SQLException;

	public School fetchSchoolBySchoolId(long schoolId, long userId) throws SQLException;

	public List<School> fetchSchoolsByUserId(long userId) throws SQLException;

	public boolean checkIfSchoolExistsByRegistrationNumber(School school) throws SQLException;
}
