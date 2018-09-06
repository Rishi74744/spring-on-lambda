package com.spring.on.aws.lambda.application;

import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.spring.on.aws.lambda.utils.ProxyUtils;

public class Main {

	public static void main(String[] args) {
		ProxyUtils utils = new ProxyUtils();
		ProxyUtils.handler = new StreamLambdaHandler();
		AwsProxyResponse response = utils.get("/dev/pets", null, null);
		System.out.println(response.getBody());
	}

}
