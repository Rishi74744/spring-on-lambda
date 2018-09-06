package com.spring.on.aws.lambda.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.spring.on.aws.lambda.application.StreamLambdaHandler;

public class ProxyUtils {

	private static final String			AMAZON_PROXY_JAVA_TEST_NG_TEST	= "AmazonProxy/Java(TestNG:Test)";
	public static StreamLambdaHandler	handler;
	public static Context				lambdaContext					= newContext();
	private static final Logger			_LOGGER							= LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public AwsProxyResponse get(String handler, Map<String, String> headers, Map<String, String> queryParams) {

		AwsProxyRequestBuilder request = new AwsProxyRequestBuilder(handler, HttpMethod.GET).header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
				.userAgent(AMAZON_PROXY_JAVA_TEST_NG_TEST);

		if (!CollectionUtils.isEmpty(headers)) {
			for (Entry<String, String> header : headers.entrySet()) {
				request.header(header.getKey(), header.getValue());
			}
		}

		if (!CollectionUtils.isEmpty(queryParams)) {
			for (Entry<String, String> param : queryParams.entrySet()) {
				request.queryString(param.getKey(), param.getValue());
			}
		}

		InputStream requestStream = request.buildStream();

		ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
		handle(requestStream, responseStream);
		return readResponse(responseStream);
	}

	public AwsProxyResponse post(String handler, Object paylod, Map<String, String> headers) {

		AwsProxyRequestBuilder request = new AwsProxyRequestBuilder(handler, HttpMethod.POST).header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).body(paylod).userAgent(AMAZON_PROXY_JAVA_TEST_NG_TEST);

		if (!CollectionUtils.isEmpty(headers)) {
			for (Entry<String, String> header : headers.entrySet()) {
				request.header(header.getKey(), header.getValue());
			}
		}

		InputStream requestStream = request.buildStream();

		ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
		handle(requestStream, responseStream);
		return readResponse(responseStream);
	}

	public Map<String, String> getAuthHeaders() {
		return new HashMap<>();
	}

	private void handle(InputStream is, ByteArrayOutputStream os) {
		try {
			handler.handleRequest(is, os, lambdaContext);
		}
		catch (IOException e) {
			_LOGGER.error("Exception Occurred : " + e.getMessage(), e);
		}
	}

	private AwsProxyResponse readResponse(ByteArrayOutputStream responseStream) {
		try {
			return LambdaContainerHandler.getObjectMapper().readValue(responseStream.toByteArray(), AwsProxyResponse.class);
		}
		catch (IOException e) {
			_LOGGER.error("Exception Occurred : " + e.getMessage(), e);
		}
		return null;
	}

	public static InputStream newPostInputStream(String handler, Object paylod, Map<String, String> headers) {
		AwsProxyRequestBuilder request = new AwsProxyRequestBuilder(handler, HttpMethod.POST).header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).body(paylod).userAgent(AMAZON_PROXY_JAVA_TEST_NG_TEST);

		if (!CollectionUtils.isEmpty(headers)) {
			for (Entry<String, String> header : headers.entrySet()) {
				request.header(header.getKey(), header.getValue());
			}
		}
		return request.buildStream();
	}

	public static InputStream newGetInputStream(String handler, Map<String, String> headers, Map<String, String> queryParams) {
		AwsProxyRequestBuilder request = new AwsProxyRequestBuilder(handler, HttpMethod.GET).header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
				.userAgent(AMAZON_PROXY_JAVA_TEST_NG_TEST);

		if (!CollectionUtils.isEmpty(headers)) {
			for (Entry<String, String> header : headers.entrySet()) {
				request.header(header.getKey(), header.getValue());
			}
		}

		if (!CollectionUtils.isEmpty(queryParams)) {
			for (Entry<String, String> param : queryParams.entrySet()) {
				request.queryString(param.getKey(), param.getValue());
			}
		}
		return request.buildStream();
	}

	public static OutputStream newOutputStream() {
		return new ByteArrayOutputStream();
	}

	public static Context newContext() {
		return new MockLambdaContext();
	}

}
