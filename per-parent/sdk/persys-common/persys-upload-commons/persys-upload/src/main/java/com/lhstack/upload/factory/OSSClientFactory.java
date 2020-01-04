package com.lhstack.upload.factory;

import com.lhstack.upload.client.OSSClient;
import com.lhstack.upload.client.impl.HttpClientOSSClient;
import com.lhstack.upload.client.impl.OkHttpOSSClient;

public abstract class OSSClientFactory {
	
	public static OSSClient createDefaultOSSClient() {
		return new OkHttpOSSClient();
	}

	public static OSSClient getSelectTypeOSSClient(String type) {
		return type.toLowerCase().equals("okhttp") ? new OkHttpOSSClient() : type.toLowerCase().equals("httpclient") ? new HttpClientOSSClient() : new OkHttpOSSClient();
	}
}
