package com.lhstack.utils;

import cn.hutool.core.io.IoUtil;
import com.lhstack.aspect.sys.SysLog;
import com.lhstack.entity.fastdfs.FastResult;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class FastDfsUtils {

    @Value("${fastdfs.http.prefix:http://localhost:8888}")
    private String prefix;
    private static TrackerClient trackerClient;

    static {
        try {
            ClientGlobal.init("fastdfs.conf");
            trackerClient = new TrackerClient();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }


    @SysLog
    public String uploadFile(byte[] bytes, String extName) throws Exception {
        String str = null;
        TrackerServer connection = trackerClient.getConnection();
        StorageClient1 storageClient1 = new StorageClient1(connection, null);
        str = storageClient1.upload_file1(bytes, extName, null);
        return prefix + str;
    }

    @SysLog
    public FastResult uploadFile(File file) throws Exception {
        FastResult fastResult = new FastResult();
        fastResult.setFileName(file.getName());
        fastResult.setSize(file.length());
        fastResult.setPath(uploadFile(new FileInputStream(file),file.getName().substring(file.getName().lastIndexOf(".") + 1)));
        return fastResult;
    }

    @SysLog
    public String uploadFile(InputStream in, String extName) throws Exception {
        return uploadFile(IoUtil.readBytes(in), extName);
    }

    @SysLog
    public FastResult uploadFile(FilePart filePart) throws Exception {
        String extName = filePart.filename().substring(filePart.filename().lastIndexOf(".") + 1);
        FastResult fastResult = new FastResult();
        byte[] bytes = FileUtils.filePartToBytes(filePart);
        fastResult.setSize(bytes.length * 1L);
        fastResult.setPath(uploadFile(bytes, extName));
        fastResult.setFileName(filePart.filename());
        return fastResult;
    }
}
