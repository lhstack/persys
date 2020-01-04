package com.lhstack.upload.client;

import com.lhstack.upload.resut.UploadResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MultiPartOSSUploadClient {
    List<UploadResult> uploadMultiFiles(Map<String, byte[]> multiFiles, UploadFileNameGeneratorType type) throws IOException;

    List<UploadResult> uploadMultiFiles(Map<String, byte[]> multiFiles, UploadFileNameGeneratorType type, String directory) throws IOException;
}
