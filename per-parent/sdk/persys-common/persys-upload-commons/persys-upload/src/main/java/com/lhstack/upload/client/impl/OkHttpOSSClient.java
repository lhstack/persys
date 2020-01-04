package com.lhstack.upload.client.impl;

import com.alibaba.fastjson.JSONObject;
import com.lhstack.upload.client.OSSClient;
import com.lhstack.upload.client.UploadFileNameGeneratorType;
import com.lhstack.upload.client.config.GlobalConfig;
import com.lhstack.upload.exception.UploadFailException;
import com.lhstack.upload.resut.UploadResult;
import okhttp3.*;
import okhttp3.OkHttpClient.Builder;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *  	默认值
 * @author lhstack
 *
 */
public class OkHttpOSSClient implements OSSClient {

	private String token;

	private String uploadUrl;
	private OkHttpClient okHttpClient;

	public OkHttpOSSClient() {
		this.token = GlobalConfig.getTOKEN();
		this.uploadUrl = GlobalConfig.getFullBaseUrl();
		initOkhttpClient();
	}

	public OkHttpOSSClient(OkHttpClient okHttpClient) {
		this.token = GlobalConfig.getTOKEN();
		this.uploadUrl = GlobalConfig.getFullBaseUrl();
		this.okHttpClient = okHttpClient;
	}

	private void initOkhttpClient() {
		// TODO Auto-generated method stub
		Builder builder = new OkHttpClient().newBuilder();
		this.okHttpClient = builder.callTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS).build();
	}

	@Override
	public UploadResult upload(byte[] content, String fileName, String directory) throws IOException {
		Call call = okHttpClient.newCall(buildRequest(content, fileName, "origin",directory));
		Response response = call.execute();
		return buildResult(response);
	}

	@Override
	public UploadResult upload(byte[] content, String fileName) throws IOException {
		// TODO Auto-generated method stub
		Call call = okHttpClient.newCall(buildRequest(content, fileName, "origin",""));
		Response response = call.execute();
		return buildResult(response);
	}

	/**
	 *  	构建返回对象
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private UploadResult buildResult(Response response) throws IOException {
		// TODO Auto-generated method stub

		if (response.code() != 200) {
			throw new UploadFailException(response.message());
		}
		String jsonResult = new String(response.body().bytes());
		JSONObject jsonObject= (JSONObject) JSONObject.parse(jsonResult);
		if(jsonObject.getInteger("code") != 200) {
			throw new UploadFailException(jsonObject.getString("msg"));
		}
		UploadResult uploadResult = new UploadResult();
		JSONObject data = jsonObject.getJSONObject("data");
		uploadResult.setFileName(data.getString("fileName"));
		uploadResult.setPath(data.getString("path"));
		uploadResult.setSize(data.getLongValue("size"));
		return uploadResult;
	}

	/**
	 * 构建请求
	 * @param content  文件内容
	 * @param fileName 文件名称
	 * @param type 文件名称生成类型
	 * @return
	 */
	private Request buildRequest(byte[] content, String fileName, String type,String directory) {
		// TODO Auto-generated method stub
		Request.Builder builder = new Request.Builder();
		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("file", fileName, RequestBody.create(content))
				.build();
		return builder.url(this.uploadUrl + "/oss/upload?token=" + this.token + "&generator_type=" + type + "&directory=" + directory).post(requestBody)
				.header("Authorization", "Client-ID " + UUID.randomUUID())
				.build();
	}

	@Override
	public UploadResult upload(InputStream in, String fileName) throws IOException {
		byte[] byteArray = IOUtils.toByteArray(in);
		return upload(byteArray, fileName);
	}

	@Override
	public UploadResult upload(File file, UploadFileNameGeneratorType type) throws IOException {
		// TODO Auto-generated method stub
		return upload(IOUtils.toByteArray(new FileInputStream(file)), file.getName(), type);
	}

	@Override
	public UploadResult upload(File file, UploadFileNameGeneratorType type, String directory) throws IOException {
		return upload(IOUtils.toByteArray(new FileInputStream(file)),file.getName(),type, directory);
	}

	@Override
	public UploadResult upload(byte[] content, String fileName, UploadFileNameGeneratorType type) throws IOException {
		// TODO Auto-generated method stub
		String uploadType = type == UploadFileNameGeneratorType.ORIGIN_FILE_NAME ? "origin" : "uuid";
		Call call = okHttpClient.newCall(buildRequest(content, fileName, uploadType,""));
		Response response = call.execute();
		return buildResult(response);
	}

	@Override
	public UploadResult upload(byte[] content, String fileName, UploadFileNameGeneratorType type, String directory) throws IOException {
		String uploadType = type == UploadFileNameGeneratorType.ORIGIN_FILE_NAME ? "origin" : "uuid";
		Call call = okHttpClient.newCall(buildRequest(content, fileName, uploadType,directory));
		Response response = call.execute();
		return buildResult(response);
	}

	@Override
	public UploadResult upload(InputStream in, String fileName, UploadFileNameGeneratorType type) throws IOException {
		// TODO Auto-generated method stub
		return upload(IOUtils.toByteArray(in), fileName, type);
	}

	@Override
	public UploadResult upload(File file) throws IOException {
		// TODO Auto-generated method stub
		return upload(file, UploadFileNameGeneratorType.ORIGIN_FILE_NAME);
	}

	@Override
	public UploadResult upload(File file, String directory) throws IOException {
		return upload(file, UploadFileNameGeneratorType.ORIGIN_FILE_NAME,directory);
	}

	@Override
	public List<UploadResult> uploadMultiFiles(Map<String, byte[]> multiFiles, UploadFileNameGeneratorType type)
			throws IOException {
		String generatorType = type == UploadFileNameGeneratorType.ORIGIN_FILE_NAME ? "origin" : "uuid";
		Request.Builder builder = new Request.Builder();
		MultipartBody.Builder multiPartBuild = new MultipartBody.Builder()
		.setType(MultipartBody.FORM);
		multiFiles.entrySet().forEach(item ->{
			multiPartBuild.addFormDataPart("files", item.getKey(), RequestBody.create(item.getValue()));
		});
		MultipartBody requestBody = multiPartBuild.build();
		Request request = builder.url(GlobalConfig.getFullBaseUrl() + "/oss/multi/upload?token=" + this.token + "&generator_type=" + generatorType).post(requestBody)
				.header("Authorization", "Client-ID " + UUID.randomUUID())
				.build();
		Call call = okHttpClient.newCall(request);
		Response execute = call.execute();

		if(execute.code() != 200) {
			throw new UploadFailException(execute.message());
		}
		ResponseBody responseBody = execute.body();
		JSONObject jsonObject = (JSONObject) JSONObject.parse(responseBody.bytes());
		if(jsonObject.getInteger("code") != 200) {
			throw new UploadFailException(jsonObject.getString("msg"));
		}
		List<UploadResult> resultList = jsonObject.getJSONArray("data").toJavaList(UploadResult.class);
		return resultList;
	}

	@Override
	public List<UploadResult> uploadMultiFiles(Map<String, byte[]> multiFiles, UploadFileNameGeneratorType type, String directory) throws IOException {
		String generatorType = type == UploadFileNameGeneratorType.ORIGIN_FILE_NAME ? "origin" : "uuid";
		Request.Builder builder = new Request.Builder();
		MultipartBody.Builder multiPartBuild = new MultipartBody.Builder()
				.setType(MultipartBody.FORM);
		multiFiles.entrySet().forEach(item ->{
			multiPartBuild.addFormDataPart("files", item.getKey(), RequestBody.create(item.getValue()));
		});
		MultipartBody requestBody = multiPartBuild.build();
		Request request = builder.url(GlobalConfig.getFullBaseUrl() + "/oss/multi/upload?token=" + this.token + "&generator_type=" + generatorType + "&directory=" + directory).post(requestBody)
				.header("Authorization", "Client-ID " + UUID.randomUUID())
				.build();
		Call call = okHttpClient.newCall(request);
		Response execute = call.execute();

		if(execute.code() != 200) {
			throw new UploadFailException(execute.message());
		}
		ResponseBody responseBody = execute.body();
		JSONObject jsonObject = (JSONObject) JSONObject.parse(responseBody.bytes());
		if(jsonObject.getInteger("code") != 200) {
			throw new UploadFailException(jsonObject.getString("msg"));
		}
		List<UploadResult> resultList = jsonObject.getJSONArray("data").toJavaList(UploadResult.class);
		return resultList;
	}

	@Override
	public boolean deleteObject(String key) throws IOException {
		Request.Builder builder = new Request.Builder();
		Request request = builder.url(GlobalConfig.getFullBaseUrl() + "/oss/delete?token=" + GlobalConfig.getTOKEN() + "&key=" + key).delete()
				.header("Authorization", "Client-ID " + UUID.randomUUID())
				.build();
		Response response = okHttpClient.newCall(request).execute();
		if (response.code() != 200) {
			return false;
		}
		String jsonResult = new String(response.body().bytes());
		JSONObject jsonObject= (JSONObject) JSONObject.parse(jsonResult);
		if(jsonObject.getInteger("code") != 200) {
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteObjects(List<String> keys) throws IOException {
		Request.Builder builder = new Request.Builder();
		Request request = builder.url(GlobalConfig.getFullBaseUrl() + "/oss/deletes?token=" + GlobalConfig.getTOKEN() + "&keys=" + keys.stream().collect(Collectors.joining(","))).delete()
				.header("Authorization", "Client-ID " + UUID.randomUUID())
				.build();
		Response response = okHttpClient.newCall(request).execute();
		if (response.code() != 200) {
			return false;
		}
		String jsonResult = new String(response.body().bytes());
		JSONObject jsonObject= (JSONObject) JSONObject.parse(jsonResult);
		if(jsonObject.getInteger("code") != 200) {
			return false;
		}
		return true;
	}
}
