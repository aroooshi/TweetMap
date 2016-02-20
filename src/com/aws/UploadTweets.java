package com.aws;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClient;
import com.amazonaws.services.cloudsearchdomain.model.UploadDocumentsRequest;
import com.amazonaws.services.cloudsearchdomain.model.UploadDocumentsResult;

public class UploadTweets {

	private static AmazonCloudSearchDomainClient domain;

	private AmazonCloudSearchDomainClient getDomain(String END_POINT) throws Exception {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential
		 * profile by reading from the credentials file located at
		 * (/home/rohitb/.aws/credentials).
		 */
		if (domain == null) {
			AWSCredentials credentials = null;
			try {
				credentials = new ProfileCredentialsProvider("default").getCredentials();
			} catch (Exception e) {
				throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
						+ "Please make sure that your credentials file is at the correct "
						+ "location (/home/rohitb/.aws/credentials), and is in valid format.", e);
			}
			System.out.println(credentials.getAWSAccessKeyId());
			domain = new AmazonCloudSearchDomainClient(credentials);
			domain.setEndpoint(END_POINT);
		}
		return domain;
	}
	
	public boolean addDocumentString(String toWrite){
		try {
			InputStream stream = new ByteArrayInputStream(toWrite.getBytes("UTF_8"));
			UploadDocumentsRequest req = new UploadDocumentsRequest();
			req.setDocuments(stream);
			req.setContentLength((long) toWrite.getBytes("UTF-8").length);
			UploadDocumentsResult result = domain.uploadDocuments(req);
			System.out.println(result.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean addDocumentFile(String END_POINT, String fileWrite){
		try {
			File file = new File(fileWrite);
		//	InputStream stream = new ByteArrayInputStream(toWrite.getBytes("UTF_8"));
			InputStream stream = new FileInputStream(file);
			UploadDocumentsRequest req = new UploadDocumentsRequest();
			req.setDocuments(stream);
			req.setContentType("application/json");
			req.setContentLength(file.length());
			UploadDocumentsResult result = this.getDomain(END_POINT).uploadDocuments(req);
			System.out.println(result.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public static void main(String[] args) throws Exception {
		UploadTweets ut = new UploadTweets();
		String END_POINT = "http://doc-tweetmap-v1-psvtm7xwecd2c7ey4rxlxubvke.us-east-1.cloudsearch.amazonaws.com/";
//		AmazonCloudSearchDomainClient domainClient = ut.getDomain(END_POINT);
		String fileToWrite = "Upload.json";
		ut.addDocumentFile(END_POINT,fileToWrite);
	}
}
