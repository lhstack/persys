package com.lhstack.service.log;

import com.lhstack.entity.log.FileLogInfo;
import com.lhstack.entity.log.FileLogType;

import java.io.File;
import java.util.List;

public interface IFileLogService {

    Boolean deleteFile(String path);

    void clearFileLogCache(Integer expireDay) throws Exception;

    List<FileLogInfo> getRoot();

    Boolean deleteDirectory(String path);

    void deleteExpireLog(String logPath, FileLogType type);

    List<FileLogInfo> fileToFileLogInfo(File[] files);

    List<FileLogInfo> getDirectoryChild(String path);

    List<FileLogInfo> getTypeDirectory(FileLogType type, String logPath);
}
