package com.example.firstproject.chime;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.chime.AmazonChime;
import com.amazonaws.services.chime.AmazonChimeClient;

public class AwsMeetingClient {

	public static AmazonChime getMeetingClient() {
		return AmazonChimeClient.builder()
				.withCredentials(new CredentialProvider())
				.withRegion(Regions.AP_EAST_1)
				.build();
	}
}
