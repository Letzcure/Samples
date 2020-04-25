package com.example.firstproject.chime;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

public class CredentialProvider implements AWSCredentialsProvider {

	@Override
	public AWSCredentials getCredentials() {
		// Use your own access-id and security-access-id
		return new BasicAWSCredentials("<ACCESS-ID>", "<SECURITY-ACCESS-ID>");
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
	}

}
