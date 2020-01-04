package com.lhstack.upload.client;

import com.lhstack.upload.resut.UploadResult;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface OSSUploadClient {

    UploadResult upload(byte[] content, String fileName, String directory) throws IOException;

    UploadResult upload(byte[] content, String fileName) throws IOException ;

    UploadResult upload(InputStream in, String fileName) throws IOException ;

    UploadResult upload(byte[] content, String fileName, UploadFileNameGeneratorType type) throws IOException ;

    UploadResult upload(byte[] content, String fileName, UploadFileNameGeneratorType type, String directory) throws IOException ;

    UploadResult upload(InputStream in, String fileName, UploadFileNameGeneratorType type) throws IOException ;

    UploadResult upload(File file) throws IOException ;

    UploadResult upload(File file, String directory) throws IOException ;

    UploadResult upload(File file, UploadFileNameGeneratorType type) throws IOException ;

    UploadResult upload(File file, UploadFileNameGeneratorType type, String directory) throws IOException ;

}
