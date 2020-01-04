package com.lhstack.upload.client.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lhstack.upload.client.OSSClient;
import com.lhstack.upload.client.UploadFileNameGeneratorType;
import com.lhstack.upload.client.config.GlobalConfig;
import com.lhstack.upload.exception.UploadFailException;
import com.lhstack.upload.resut.UploadResult;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HttpClientOSSClient implements OSSClient {

	private HttpClient httpClient;

	public HttpClientOSSClient() {
		// TODO Auto-generated constructor stub
		initHttpClient();
	}

	public HttpClientOSSClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * 	初始化httpClient
	 */
	private void initHttpClient() {
		// TODO Auto-generated method stub
		this.httpClient = HttpClients.custom().setConnectionTimeToLive(30, TimeUnit.SECONDS).setMaxConnTotal(10)
				.build();
	}

	@Override
	public UploadResult upload(byte[] content, String fileName, String directory) throws IOException {
		ByteArrayBody body = new ByteArrayBody(content, fileName);
		HttpEntity entity = MultipartEntityBuilder.create().addPart("file", body)
				.setContentType(ContentType.MULTIPART_FORM_DATA).build();
		HttpPost httpPost = buildHttpPost("origin",directory);
		httpPost.setEntity(entity);
		HttpResponse execute = httpClient.execute(httpPost);
		return buildResult(execute);
	}

	@Override
	public UploadResult upload(byte[] content, String fileName) throws IOException {
		// TODO Auto-generated method stub

		ByteArrayBody body = new ByteArrayBody(content, fileName);
		HttpEntity entity = MultipartEntityBuilder.create().addPart("file", body)
				.setContentType(ContentType.MULTIPART_FORM_DATA).build();
		HttpPost httpPost = buildHttpPost("origin","");
		httpPost.setEntity(entity);
		HttpResponse execute = httpClient.execute(httpPost);
		return buildResult(execute);
	}

	/**
	 * 	构建返回值
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private UploadResult buildResult(HttpResponse response) throws IOException {
		// TODO Auto-generated method stub
		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() != 200) {
			throw new UploadFailException(statusLine.getReasonPhrase());
		}
		String content = EntityUtils.toString(response.getEntity());
		JSONObject jsonObject = (JSONObject) JSONObject.parse(content);
		if (jsonObject.getInteger("code") != 200) {
			throw new UploadFailException(jsonObject.getString("msg"));
		}
		JSONObject data = jsonObject.getJSONObject("data");
		UploadResult uploadResult = new UploadResult();
		uploadResult.setFileName(data.getString("fileName"));
		uploadResult.setPath(data.getString("path"));
		uploadResult.setSize(data.getLongValue("size"));
		return uploadResult;
	}

	/**
	 * 	构建上传文件请求
	 * @param type
	 * @return
	 */
	private HttpPost buildHttpPost(String type,String directory) {
		// TODO Auto-generated method stub
		HttpPost httpPost = new HttpPost();
		httpPost.addHeader("Authorization", "Client-ID " + UUID.randomUUID());
		System.out.println(GlobalConfig.getFullBaseUrl());
		httpPost.setURI(
				URI.create(GlobalConfig.getFullBaseUrl() + "/oss/upload?token=" + GlobalConfig.TOKEN + "&generator_type=" + type + "&directory=" + directory));
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
		httpPost.setConfig(requestConfig);
		return httpPost;
	}

	@Override
	public UploadResult upload(InputStream in, String fileName) throws IOException {
		// TODO Auto-generated method stub
		return upload(IOUtils.toByteArray(in), fileName);
	}

	@Override
	public UploadResult upload(byte[] content, String fileName, UploadFileNameGeneratorType type) throws IOException {
		String generatorType = type == UploadFileNameGeneratorType.ORIGIN_FILE_NAME ? "origin" : "uuid";
		HttpPost httpPost = buildHttpPost(generatorType,"");
		ByteArrayBody body = new ByteArrayBody(content, fileName);
		HttpEntity entity = MultipartEntityBuilder.create().addPart("file", body)
				.setContentType(ContentType.MULTIPART_FORM_DATA).build();
		httpPost.setEntity(entity);
		HttpResponse execute = httpClient.execute(httpPost);
		return buildResult(execute);
	}

	@Override
	public UploadResult upload(byte[] content, String fileName, UploadFileNameGeneratorType type, String directory) throws IOException {
		String generatorType = type == UploadFileNameGeneratorType.ORIGIN_FILE_NAME ? "origin" : "uuid";
		HttpPost httpPost = buildHttpPost(generatorType,directory);
		ByteArrayBody body = new ByteArrayBody(content, fileName);
		HttpEntity entity = MultipartEntityBuilder.create().addPart("file", body)
				.setContentType(ContentType.MULTIPART_FORM_DATA).build();
		httpPost.setEntity(entity);
		HttpResponse execute = httpClient.execute(httpPost);
		return buildResult(execute);
	}

	@Override
	public UploadResult upload(InputStream in, String fileName, UploadFileNameGeneratorType type) throws IOException {
		// TODO Auto-generated method stub
		return upload(IOUtils.toByteArray(in), fileName, type);
	}

	@Override
	public UploadResult upload(File file) throws IOException {
		// TODO Auto-generated method stub
		return upload(IOUtils.toByteArray(new FileInputStream(file)), file.getName());
	}

	@Override
	public UploadResult upload(File file, String directory) throws IOException {
		return upload(IOUtils.toByteArray(new FileInputStream(file)), file.getName(),directory);
	}

	@Override
	public UploadResult upload(File file, UploadFileNameGeneratorType type) throws IOException {
		// TODO Auto-generated method stub
		return upload(IOUtils.toByteArray(new FileInputStream(file)), file.getName(), type);
	}

	@Override
	public UploadResult upload(File file, UploadFileNameGeneratorType type, String directory) throws IOException {
		return upload(IOUtils.toByteArray(new FileInputStream(file)), file.getName(),type,directory);
	}


	@Override
	public List<UploadResult> uploadMultiFiles(Map<String, byte[]> multiFiles, UploadFileNameGeneratorType type) throws IOException {
		String fileNameGenertorType = type == UploadFileNameGeneratorType.ORIGIN_FILE_NAME ? "origin" : "uuid";
		HttpPost httpPost = new HttpPost();
		httpPost.addHeader("Authorization", "Client-ID " + UUID.randomUUID());
		httpPost.setURI(
				URI.create(GlobalConfig.getFullBaseUrl() + "/oss/multi/upload?token=" + GlobalConfig.TOKEN + "&generator_type=" + fileNameGenertorType));
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
		httpPost.setConfig(requestConfig);
		MultipartEntityBuilder create = MultipartEntityBuilder.create();
		multiFiles.entrySet().forEach(item ->{
			ByteArrayBody body = new ByteArrayBody(item.getValue(), item.getKey());
			create.addPart("files", body);
		});
		HttpEntity entity = create.build();
		httpPost.setEntity(entity);
		HttpResponse execute = httpClient.execute(httpPost);
		StatusLine statusLine = execute.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if(statusCode != 200) {
			throw new UploadFailException(statusLine.getReasonPhrase());
		}
		HttpEntity result = execute.getEntity();
		String jsonResult = EntityUtils.toString(result);
		JSONObject jsonObject = (JSONObject) JSONObject.parse(jsonResult);
		if(jsonObject.getInteger("code") != 200) {
			throw new UploadFailException(jsonObject.getString("msg"));
		}
		List<UploadResult> list = jsonObject.getJSONArray("data").toJavaObject(new TypeReference<List<UploadResult>>() {});
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	public List<UploadResult> uploadMultiFiles(Map<String, byte[]> multiFiles, UploadFileNameGeneratorType type, String directory) throws IOException {
		String fileNameGenertorType = type == UploadFileNameGeneratorType.ORIGIN_FILE_NAME ? "origin" : "uuid";
		HttpPost httpPost = new HttpPost();
		httpPost.addHeader("Authorization", "Client-ID " + UUID.randomUUID());
		httpPost.setURI(
				URI.create(GlobalConfig.getFullBaseUrl() + "/oss/multi/upload?token=" + GlobalConfig.TOKEN + "&generator_type=" + fileNameGenertorType + "&directory=" + directory));
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
		httpPost.setConfig(requestConfig);
		MultipartEntityBuilder create = MultipartEntityBuilder.create();
		multiFiles.entrySet().forEach(item ->{
			ByteArrayBody body = new ByteArrayBody(item.getValue(), item.getKey());
			create.addPart("files", body);
		});
		HttpEntity entity = create.build();
		httpPost.setEntity(entity);
		HttpResponse execute = httpClient.execute(httpPost);
		StatusLine statusLine = execute.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if(statusCode != 200) {
			throw new UploadFailException(statusLine.getReasonPhrase());
		}
		HttpEntity result = execute.getEntity();
		String jsonResult = EntityUtils.toString(result);
		JSONObject jsonObject = (JSONObject) JSONObject.parse(jsonResult);
		if(jsonObject.getInteger("code") != 200) {
			throw new UploadFailException(jsonObject.getString("msg"));
		}
		List<UploadResult> list = jsonObject.getJSONArray("data").toJavaObject(new TypeReference<List<UploadResult>>() {});
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	public boolean deleteObject(String key) throws IOException {
		HttpDelete httpDelete = new HttpDelete();
		httpDelete.setURI(URI.create(GlobalConfig.getFullBaseUrl() + "/oss/delete?token=" + GlobalConfig.getTOKEN() + "&key=" + key));
		httpDelete.addHeader("Authorization", "Client-ID " + UUID.randomUUID());
		HttpResponse execute = httpClient.execute(httpDelete);
		StatusLine statusLine = execute.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if(statusCode != 200) {
			return false;
		}
		HttpEntity result = execute.getEntity();
		String jsonResult = EntityUtils.toString(result);
		JSONObject jsonObject = (JSONObject) JSONObject.parse(jsonResult);
		if(jsonObject.getInteger("code") != 200) {
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteObjects(List<String> keys) throws IOException {
		HttpDelete httpDelete = new HttpDelete();
		httpDelete.setURI(URI.create(GlobalConfig.getFullBaseUrl() + "/oss/deletes?token=" + GlobalConfig.getTOKEN() + "&keys=" + keys.stream().collect(Collectors.joining(","))));
		httpDelete.addHeader("Authorization", "Client-ID " + UUID.randomUUID());
		HttpResponse execute = httpClient.execute(httpDelete);
		StatusLine statusLine = execute.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if(statusCode != 200) {
			return false;
		}
		HttpEntity result = execute.getEntity();
		String jsonResult = EntityUtils.toString(result);
		JSONObject jsonObject = (JSONObject) JSONObject.parse(jsonResult);
		if(jsonObject.getInteger("code") != 200) {
			return false;
		}
		return true;
	}
}
