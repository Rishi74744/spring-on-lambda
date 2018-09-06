package com.spring.on.aws.lambda.service.implementation;

import java.lang.invoke.MethodHandles;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;

import com.spring.on.aws.lambda.beans.School;
import com.spring.on.aws.lambda.dao.SchoolDao;
import com.spring.on.aws.lambda.service.SchoolService;

@Service
public class SchoolServiceImplementation implements SchoolService {

	@Autowired
	SchoolDao					schoolDao;

	@Autowired
	SingleConnectionDataSource	singleConnectionDataSource;

	private static final String	CLASS_LOG_TAG	= "<<<--- SchoolServiceImplementation --->>>";

	private static final Logger	_LOGGER			= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public long addSchool(School school) throws SQLException {
		_LOGGER.info(CLASS_LOG_TAG, " Add School Service");
		return schoolDao.addSchool(school, singleConnectionDataSource.getConnection());
	}

	@Override
	public boolean updateSchool(School school) throws SQLException {
		_LOGGER.info(CLASS_LOG_TAG, " Update School Service");
		return schoolDao.updateSchool(school, singleConnectionDataSource.getConnection());
	}

	@Override
	public boolean checkIfSchoolExistsBySchooId(School school) throws SQLException {
		_LOGGER.info(CLASS_LOG_TAG, " Check If School Exists By School Id Service");
		return schoolDao.checkIfSchoolExistsBySchooId(school, singleConnectionDataSource.getConnection());
	}

	@Override
	public School fetchSchoolBySchoolId(long schoolId, long userId) throws SQLException {
		_LOGGER.info(CLASS_LOG_TAG, " Fetch School By School Id Service");
		return schoolDao.fetchSchoolBySchoolId(schoolId, userId, singleConnectionDataSource.getConnection());
	}

	@Override
	public List<School> fetchSchoolsByUserId(long userId) throws SQLException {
		_LOGGER.info(CLASS_LOG_TAG, " Fetch Schools By User Id Service");
		return schoolDao.fetchSchoolsByUserId(userId, singleConnectionDataSource.getConnection());
	}

	@Override
	public boolean checkIfSchoolExistsByRegistrationNumber(School school) throws SQLException {
		_LOGGER.info(CLASS_LOG_TAG, " Check If School Exists By School Registration Numnber Service");
		return schoolDao.checkIfSchoolExistsByRegistrationNumber(school, singleConnectionDataSource.getConnection());
	}

}
