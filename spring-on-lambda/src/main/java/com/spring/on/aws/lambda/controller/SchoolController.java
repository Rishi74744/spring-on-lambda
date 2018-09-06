package com.spring.on.aws.lambda.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spring.on.aws.lambda.beans.Response;
import com.spring.on.aws.lambda.beans.School;
import com.spring.on.aws.lambda.service.SchoolService;

@RestController
public class SchoolController {

	@Autowired
	SchoolService				schoolService;

	private static final String	CLASS_LOG_TAG	= "<<<--- SchoolController --->>>";
	private static final Logger	_LOGGER			= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@RequestMapping(path = "/school/fetch", method = RequestMethod.GET)
	public Response fetchSchool(@RequestParam long schoolId, @RequestParam long userId) {
		School school = null;
		try {
			school = schoolService.fetchSchoolBySchoolId(schoolId, userId);
			if (school == null) {
				return createResponse(401, "No Data Found", null);
			}
			return createResponse(200, "Success", school);
		}
		catch (Exception e) {
			_LOGGER.error(CLASS_LOG_TAG + " Exception Occurred : " + e.getMessage(), e);
			return createResponse(500, "Internal Server Error", null);
		}
	}

	@RequestMapping(path = "/schools", method = RequestMethod.GET)
	public Response fetchSchoolByUserId(@RequestParam long userId) {
		List<School> schools = null;
		try {
			schools = schoolService.fetchSchoolsByUserId(userId);
			if (schools == null || schools.isEmpty()) {
				return createResponse(401, "No Data Found", null);
			}
			return createResponse(200, "Success", schools);
		}
		catch (Exception e) {
			_LOGGER.error(CLASS_LOG_TAG + " Exception Occurred : " + e.getMessage(), e);
			return createResponse(500, "Internal Server Error", null);
		}
	}

	@RequestMapping(path = "/school/register", method = RequestMethod.POST)
	public Response registerSchool(@RequestBody String request) {
		long schoolId;
		try {
			JsonObject inputRequest = new JsonParser().parse(request).getAsJsonObject();
			School school = new Gson().fromJson(inputRequest, School.class);
			if (schoolService.checkIfSchoolExistsByRegistrationNumber(school)) {
				return createResponse(401, "School Already Exists", null);
			}
			schoolId = schoolService.addSchool(school);
			if (schoolId == 0) {
				return createResponse(401, "Save Failed", null);
			}
			return createResponse(200, "Success", schoolId);
		}
		catch (Exception e) {
			_LOGGER.error(CLASS_LOG_TAG + " Exception Occurred : " + e.getMessage(), e);
			return createResponse(500, "Internal Server Error", null);
		}
	}

	@RequestMapping(path = "/school/update", method = RequestMethod.PUT)
	public Response updateSchool(@RequestBody String request) {
		try {
			JsonObject inputRequest = new JsonParser().parse(request).getAsJsonObject();
			School school = new Gson().fromJson(inputRequest, School.class);
			if (schoolService.checkIfSchoolExistsBySchooId(school)) {
				return createResponse(401, "School Does Not Exists", null);
			}
			if (schoolService.updateSchool(school)) {
				return createResponse(401, "Update Failed", null);
			}
			return createResponse(200, "Success", null);
		}
		catch (Exception e) {
			_LOGGER.error(CLASS_LOG_TAG + " Exception Occurred : " + e.getMessage(), e);
			return createResponse(500, "Internal Server Error", null);
		}
	}

	private Response createResponse(int code, String message, Object data) {
		Response response = new Response();
		response.setCode(code);
		response.setMessage(message);
		response.setData(data);
		return response;
	}

}