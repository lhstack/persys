package com.lhstack.upload.client.config;

public class GlobalConfig {

	public static String TOKEN = "";

	public static String BASE_URL = "https://adming.lhstack.xyz";

	public static String getTOKEN() {
		return TOKEN;
	}

	public static void setTOKEN(String TOKEN) {
		GlobalConfig.TOKEN = TOKEN;
	}

	public static String getBaseUrl() {
		return BASE_URL;
	}

	public static void setBaseUrl(String baseUrl) {
		BASE_URL = baseUrl;
	}

	public static String getFullBaseUrl(){
		return GlobalConfig.getBaseUrl().charAt(GlobalConfig.getBaseUrl().length() - 1) == '/' || GlobalConfig.getBaseUrl().charAt(GlobalConfig.getBaseUrl().length() - 1) == '\\' ? GlobalConfig.getBaseUrl().substring(1) : GlobalConfig.getBaseUrl();
	}
}
