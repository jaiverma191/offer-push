/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.expedia.hackathon.tomcat.web;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.expedia.hackathon.tomcat.service.HelloWorldService;

import java.io.IOException;

@Controller
public class SampleController {

	@Autowired
	private HelloWorldService helloWorldService;

	@RequestMapping("/")
	@ResponseBody
	public String helloWorld() {
		return this.helloWorldService.getHelloMessage();
	}


	@RequestMapping("/send/notification")
	@ResponseBody
	public String sendNotification() throws IOException {
		String accessKey="AKIAIQCPKW3DHONSGD7Q";
		String secretKey="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";
		AmazonSNSClient client = new AmazonSNSClient(new BasicAWSCredentials(accessKey,secretKey));
//		String endpointArn="arn:aws:sns:us-east-1:571829491973:endpoint/GCM/offerpush/173f980a-0d02-3248-aea9-6fb05cdcbb61";
		String endpointArn="arn:aws:sns:us-east-1:571829491973:endpoint/GCM/offerpush/bbdaf383-7f44-3693-b6f6-4acb541b4d21";
		client.setEndpoint("https://sns.us-east-1.amazonaws.com");

		PublishRequest request = new PublishRequest();
		request.setTargetArn(endpointArn);
		request.setMessage("{\n" +
				"\"GCM\": \"{ \\\"data\\\": { \\\"message\\\": \\\"hello jai\\\" } }\"\n" +
				"}");
		request.setMessageStructure("json");
		final PublishResult publishResult = client.publish(request);
		return publishResult.getMessageId();
	}
}
