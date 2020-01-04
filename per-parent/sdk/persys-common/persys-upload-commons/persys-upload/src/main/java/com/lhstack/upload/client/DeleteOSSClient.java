package com.lhstack.upload.client;

import java.io.IOException;
import java.util.List;

public interface DeleteOSSClient {

    boolean deleteObject(String key) throws IOException;

    boolean deleteObjects(List<String> keys) throws IOException;
}
