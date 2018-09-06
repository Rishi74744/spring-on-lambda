package com.spring.on.aws.lambda.dao.implementation;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.spring.on.aws.lambda.beans.School;
import com.spring.on.aws.lambda.dao.SchoolDao;

@Repository
public class SchoolDaoImplementation implements SchoolDao {

	public static final String	STATUS															= "status";
	public static final String	UPDATED_ON														= "updatedOn";
	public static final String	CREATED_ON														= "createdOn";
	public static final String	USER_ID															= "userId";
	public static final String	SCHOOL_REGISTRATION_NUMBER										= "schoolRegistrationNumber";
	public static final String	SCHOOL_ADDRESS													= "schoolAddress";
	public static final String	SCHOOL_NAME														= "schoolName";
	public static final String	SCHOOL_ID														= "schoolId";
	public static final String	STATUS_ACTIVE													= "A";
	public static final String	FETCH_SCHOOLS_BY_USER_ID_QUERY									= "Select schoolId, schoolName, schoolAddress, schoolRegistrationNumber, userId, studentAddress, createdOn, updatedOn, status From school Where status = ? and userId = ?;";
	public static final String	ENVIRONMENT_VARIABLE_FETCH_SCHOOLS_BY_USER_ID_QUERY				= "FETCH_SCHOOL_BY_SCHOOL_ID_QUERY";
	public static final String	FETCH_SCHOOL_BY_SCHOOL_ID_QUERY									= "Select schoolId, schoolName, schoolAddress, schoolRegistrationNumber, userId, studentAddress, createdOn, updatedOn, status From school Where schoolId = ? and status = ? and userId = ?;";
	public static final String	ENVIRONMENT_VARIABLE_FETCH_SCHOOL_BY_SCHOOL_ID_QUERY			= "FETCH_SCHOOL_BY_SCHOOL_ID_QUERY";
	public static final String	UPDATE_SCHOOL_QUERY												= "Update school Set schoolName = ?, schoolAddress = ?, schoolRegistrationNumber = ?, updatedOn = ?, status = ? Where schoolId = ?";
	public static final String	ENVIRONMENT_VARIBALE_UPDATE_SCHOOL_QUERY						= "UPDATE_SCHOOL_QUERY";
	public static final String	FETCH_SCHOOL_QUERY												= "Select school From school Where schoolId = ? and userId = ? and status = ?;";
	public static final String	ENVIRONMENT_VARIBALE_FETCH_SCHOOL_QUERY							= "FETCH_SCHOOL_QUERY";
	public static final String	ADD_SCHOOL_QUERY												= "Insert Into school(schoolName, schoolAddress, schoolRegistrationNumber, userId, createdOn, updatedOn, status) Values(?,?,?,?,?,?,?);";
	public static final String	ENVIRONMENT_VARIABLE_ADD_SCHOOL_QUERY							= "ADD_SCHOOL_QUERY";
	public static final String	FETCH_SCHOOL_BY_REGISTRATION_NUMBER_QUERY						= "Select school From school Where schoolRegistrationNumber = ?;";
	public static final String	ENVIRONMENT_VARIBALE_FETCH_SCHOOL_BY_REGISTRATION_NUMBER_QUERY	= "FETCH_SCHOOL_BY_REGISTRATION_NUMBER_QUERY";
	private static final String	CLASS_LOG_TAG													= "<<<--- SchoolDaoImplementation --->>>";
	private static final Logger	_LOGGER															= LoggerFactory
			.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public long addSchool(School school, Connection connection) throws SQLException {
		long studentId = 0;
		String addStudentQuery = ADD_SCHOOL_QUERY;
		if (System.getenv(ENVIRONMENT_VARIABLE_ADD_SCHOOL_QUERY) != null) {
			addStudentQuery = System.getenv(ENVIRONMENT_VARIABLE_ADD_SCHOOL_QUERY);
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(addStudentQuery, Statement.RETURN_GENERATED_KEYS);) {
			preparedStatement.setString(1, school.getSchoolName());
			preparedStatement.setString(2, school.getSchoolAddress());
			preparedStatement.setString(3, school.getSchoolRegistrationNumber());
			preparedStatement.setLong(4, school.getUserId());
			preparedStatement.setLong(5, school.getCreatedOn());
			preparedStatement.setLong(6, school.getUpdatedOn());
			preparedStatement.setString(7, school.getStatus());
			_LOGGER.info(CLASS_LOG_TAG, " Query To Add Student : ", preparedStatement);
			preparedStatement.executeUpdate();
			try (ResultSet resultSet = preparedStatement.getGeneratedKeys();) {
				if (resultSet.next()) {
					studentId = resultSet.getLong(1);
				}
			}
		}
		return studentId;
	}

	@Override
	public boolean updateSchool(School school, Connection connection) throws SQLException {
		String updateStudentQuery = UPDATE_SCHOOL_QUERY;
		if (System.getenv(ENVIRONMENT_VARIBALE_UPDATE_SCHOOL_QUERY) != null) {
			updateStudentQuery = System.getenv(ENVIRONMENT_VARIBALE_UPDATE_SCHOOL_QUERY);
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(updateStudentQuery);) {
			preparedStatement.setString(1, school.getSchoolName());
			preparedStatement.setString(2, school.getSchoolAddress());
			preparedStatement.setString(3, school.getSchoolRegistrationNumber());
			preparedStatement.setLong(4, school.getUpdatedOn());
			preparedStatement.setString(5, school.getStatus());
			preparedStatement.setLong(6, school.getSchoolId());
			_LOGGER.info(CLASS_LOG_TAG, " Query To Add Student : ", preparedStatement);
			return preparedStatement.executeUpdate() == 0;
		}
	}

	@Override
	public boolean checkIfSchoolExistsBySchooId(School school, Connection connection) throws SQLException {
		String fetchStudentQuery = FETCH_SCHOOL_QUERY;
		if (System.getenv(ENVIRONMENT_VARIBALE_FETCH_SCHOOL_QUERY) != null) {
			fetchStudentQuery = System.getenv(ENVIRONMENT_VARIBALE_FETCH_SCHOOL_QUERY);
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(fetchStudentQuery);) {
			preparedStatement.setLong(1, school.getSchoolId());
			preparedStatement.setLong(2, school.getUserId());
			preparedStatement.setString(3, STATUS_ACTIVE);
			_LOGGER.info(CLASS_LOG_TAG, " Query To Fetch School : ", preparedStatement);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				return resultSet.next();
			}
		}
	}

	@Override
	public School fetchSchoolBySchoolId(long schoolId, long userId, Connection connection) throws SQLException {
		School school = new School();
		String fetchStudentByStudentIdQuery = FETCH_SCHOOL_BY_SCHOOL_ID_QUERY;
		if (System.getenv(ENVIRONMENT_VARIABLE_FETCH_SCHOOL_BY_SCHOOL_ID_QUERY) != null) {
			fetchStudentByStudentIdQuery = System.getenv(ENVIRONMENT_VARIABLE_FETCH_SCHOOL_BY_SCHOOL_ID_QUERY);
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(fetchStudentByStudentIdQuery);) {
			preparedStatement.setLong(1, schoolId);
			preparedStatement.setString(2, STATUS_ACTIVE);
			preparedStatement.setLong(3, userId);
			_LOGGER.info(CLASS_LOG_TAG, " Query To Fetch School By School Id : ", preparedStatement);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					school.setSchoolId(resultSet.getLong(SCHOOL_ID));
					school.setSchoolName(resultSet.getString(SCHOOL_NAME));
					school.setSchoolAddress(resultSet.getString(SCHOOL_ADDRESS));
					school.setSchoolRegistrationNumber(resultSet.getString(SCHOOL_REGISTRATION_NUMBER));
					school.setUserId(resultSet.getLong(USER_ID));
					school.setCreatedOn(resultSet.getLong(CREATED_ON));
					school.setUpdatedOn(resultSet.getLong(UPDATED_ON));
					school.setStatus(resultSet.getString(STATUS));
				}
			}
		}
		return school;
	}

	@Override
	public List<School> fetchSchoolsByUserId(long userId, Connection connection) throws SQLException {
		List<School> schools = new ArrayList<>();
		String fetchStudentByStudentIdQuery = FETCH_SCHOOLS_BY_USER_ID_QUERY;
		if (System.getenv(ENVIRONMENT_VARIABLE_FETCH_SCHOOLS_BY_USER_ID_QUERY) != null) {
			fetchStudentByStudentIdQuery = System.getenv(ENVIRONMENT_VARIABLE_FETCH_SCHOOLS_BY_USER_ID_QUERY);
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(fetchStudentByStudentIdQuery);) {
			preparedStatement.setString(1, STATUS_ACTIVE);
			preparedStatement.setLong(2, userId);
			_LOGGER.info(CLASS_LOG_TAG, " Query To Fetch Schools By User Id : ", preparedStatement);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					School school = new School();
					school.setSchoolId(resultSet.getLong(SCHOOL_ID));
					school.setSchoolName(resultSet.getString(SCHOOL_NAME));
					school.setSchoolAddress(resultSet.getString(SCHOOL_ADDRESS));
					school.setSchoolRegistrationNumber(resultSet.getString(SCHOOL_REGISTRATION_NUMBER));
					school.setUserId(resultSet.getLong(USER_ID));
					school.setCreatedOn(resultSet.getLong(CREATED_ON));
					school.setUpdatedOn(resultSet.getLong(UPDATED_ON));
					school.setStatus(resultSet.getString(STATUS));
					schools.add(school);
				}
			}
		}
		return schools;
	}

	@Override
	public boolean checkIfSchoolExistsByRegistrationNumber(School school, Connection connection) throws SQLException {
		String fetchStudentQuery = FETCH_SCHOOL_BY_REGISTRATION_NUMBER_QUERY;
		if (System.getenv(ENVIRONMENT_VARIBALE_FETCH_SCHOOL_BY_REGISTRATION_NUMBER_QUERY) != null) {
			fetchStudentQuery = System.getenv(ENVIRONMENT_VARIBALE_FETCH_SCHOOL_QUERY);
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(fetchStudentQuery);) {
			preparedStatement.setString(1, school.getSchoolRegistrationNumber());
			_LOGGER.info(CLASS_LOG_TAG, " Query To Fetch School : ", preparedStatement);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				return resultSet.next();
			}
		}
	}

}