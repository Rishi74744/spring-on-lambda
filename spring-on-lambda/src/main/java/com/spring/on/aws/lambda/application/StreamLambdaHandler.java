package com.spring.on.aws.lambda.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spring.on.aws.lambda.config.DatasourceConfig;
import com.spring.on.aws.lambda.config.SchoolManagementApplicationConfig;

public class StreamLambdaHandler implements RequestStreamHandler {

	private static SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse>	handler;
	private static ObjectMapper														mapper			= new ObjectMapper();
	public static ThreadLocal<AwsProxyRequest>										PROXY_REQUEST	= new ThreadLocal<>();

	private static final Logger														_LOGGER			= LoggerFactory
			.getLogger(MethodHandles.lookup().lookupClass());

	static {
		try {
			handler = SpringLambdaContainerHandler.getAwsProxyHandler(SchoolManagementApplicationConfig.class, DatasourceConfig.class);
			handler.onStartup(servletContext -> {
				_LOGGER.info("Handler Initializing");
			});
		}
		catch (Exception e) {
			_LOGGER.error("Exception Occurred : " + e.getMessage(), e);
		}
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		AwsProxyRequest request = mapper.readValue(input, AwsProxyRequest.class);
		JsonObject body = new JsonParser().parse(request.getBody()).getAsJsonObject();
		JsonObject inputRequest = new JsonObject();
		inputRequest.addProperty("userDomain", request.getRequestContext().getAuthorizer().getContextValue("userDomain"));
		inputRequest.addProperty("stage", request.getStageVariables().get("env"));
		inputRequest.addProperty("userId", request.getRequestContext().getAuthorizer().getPrincipalId());
		inputRequest.add("data", body);
		request.setBody(new Gson().toJson(inputRequest));

		PROXY_REQUEST.set(request);
		if (request.getPath() != null && request.getStageVariables() != null && request.getStageVariables().get("env") != null) {
			request.setPath(request.getPath().replace("/" + request.getStageVariables().get("env"), ""));
		}

		String proxyReq = mapper.writeValueAsString(request);

		handler.proxyStream(IOUtils.toInputStream(proxyReq), output, context);

		_LOGGER.info("Proxy Request : ", proxyReq);
		output.close();
	}

}