package com.spring.on.aws.lambda.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.spring.on.aws.lambda.beans.School;

public interface SchoolDao {

	public long addSchool(School school, Connection connection) throws SQLException;

	public boolean updateSchool(School school, Connection connection) throws SQLException;

	public boolean checkIfSchoolExistsBySchooId(School school, Connection connection) throws SQLException;

	public School fetchSchoolBySchoolId(long schoolId, long userId, Connection connection) throws SQLException;

	public List<School> fetchSchoolsByUserId(long userId, Connection connection) throws SQLException;

	public boolean checkIfSchoolExistsByRegistrationNumber(School school, Connection connection) throws SQLException;

}