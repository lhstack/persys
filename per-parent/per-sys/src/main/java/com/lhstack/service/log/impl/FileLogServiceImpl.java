package com.lhstack.service.log.impl;

import cn.hutool.core.io.FileUtil;
import com.lhstack.entity.log.FileLogInfo;
import com.lhstack.entity.log.FileLogType;
import com.lhstack.service.log.IFileLogService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("fileLogService")
public class FileLogServiceImpl implements IFileLogService {


    @Value("${spring.log.path}")
    private String logPath = "E:/logback/v1";

    /**
     * 删除目录
     * @param path
     * @return
     */
    @Override
    public Boolean deleteFile(String path){
        File file = new File(path);
        if(!file.exists() || file.isDirectory()){
            throw new IllegalArgumentException("删除的文件不是一个文件或者不存在");
        }
        return FileUtil.del(path);
    }

    /**
     * 删除过期文件
     * @param fileLogInfos
     */
    private void clearFile(List<FileLogInfo> fileLogInfos,Integer expireDay){
        fileLogInfos.forEach(item ->{
            try {
                com.lhstack.utils.FileUtils.clearExpireLogFile(item.getFullPath(),expireDay);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 清除过期目录,任务调度
     * @throws Exception
     */
    @Override
    public void clearFileLogCache(Integer expireDay) throws Exception{
        /**
         * 获取根目录，也就是 E:/logback/v1 的上一级 如E:/logback
         */
        List<FileLogInfo> root = getRoot();

        /**
         * 遍历根目录下面的目录，如E:/logback 获得 E:/logback/v1 E:/logback/v2
         */
        root.forEach(item ->{
            /**
             * 获取E:/logback/v[n]下面的all目录下面的日志目录，并清理过期的all日志
             * 例如: E:/logback/v[n]/all --> E:/logback/v[n]/all/[yyyy-MM-dd]
             */
            List<FileLogInfo> allLogDirector = getTypeDirectory(FileLogType.ALL, item.getFullPath());
            clearFile(allLogDirector,expireDay);
            List<FileLogInfo> debugDirector = getTypeDirectory(FileLogType.DEBUG, item.getFullPath());
            clearFile(debugDirector,expireDay);
            List<FileLogInfo> infoDirector = getTypeDirectory(FileLogType.INFO, item.getFullPath());
            clearFile(infoDirector,expireDay);
            List<FileLogInfo> warnDirector = getTypeDirectory(FileLogType.WARNING, item.getFullPath());
            clearFile(warnDirector,expireDay);
            List<FileLogInfo> errorDirector = getTypeDirectory(FileLogType.ERROR, item.getFullPath());
            clearFile(errorDirector,expireDay);
        });
    }

    @Override
    public List<FileLogInfo> getRoot(){
        char c = logPath.charAt(logPath.length() - 1);
        if(c == '/' || c == '\\'){
            throw new IllegalArgumentException("日志路径输入有误，请去掉路径最后的/或者\\");
        }
        String[] str1 = logPath.split("/");
        String[] str2 = logPath.split("\\\\");
        if(str1.length <= 1 && str2.length <= 1){
            throw new IllegalArgumentException("日志路径必须为二级或二级以上目录级别");
        }
        String str11 = "";
        if(logPath.lastIndexOf("/") >= 0){
            str11 = logPath.substring(0,logPath.lastIndexOf("/"));
        }
        if(logPath.lastIndexOf("\\") >= 0){
            str11 = logPath.substring(0,logPath.lastIndexOf("\\"));
        }
        File[] ls = FileUtil.ls(str11);
        return fileToFileLogInfo(ls);
    }

    /**
     * 删除目录
     * @param path
     * @return
     */
    @Override
    public Boolean deleteDirectory(String path){
        File file = new File(path);
        if(!file.exists() || file.isFile()){
            throw new IllegalArgumentException("删除的文件不是一个目录或者不存在");
        }
        return FileUtil.del(path);
    }

    /**
     * 删除指定类型超时的日志文件
     * @param logPath
     * @param type
     */
    @Override
    public void deleteExpireLog(String logPath, FileLogType type){
        com.lhstack.utils.FileUtils.clearExpireFile(new File(getPath(type,logPath)));
    }



    /**
     * 数组File 转list FileLogInfo
     * @param files
     * @return
     */
    @Override
    public List<FileLogInfo> fileToFileLogInfo(File[] files){
        return Arrays.asList(files).stream().map(item -> {
            FileLogInfo fileLogInfo = new FileLogInfo();
            fileLogInfo.setFileName(item.getName())
                    .setFullPath(item.getPath())
                    .setSize(item.length())
                    .setFileType(item.isFile() ? "FILE" : "DIRECTOR")
                    .setSizeName(FileUtils.byteCountToDisplaySize(item.length()))
                    .setType(item.isFile() ? FileUtil.extName(item) : "DIRECTOR");
            return fileLogInfo;
        }).collect(Collectors.toList());
    }

    /**
     *
     * @param path 获取path下面的子目录
     * @return
     */
    @Override
    public List<FileLogInfo> getDirectoryChild(String path){
        if(StringUtils.isEmpty(path) || !FileUtil.exist(path)){
            throw new NullPointerException("文件不存在");
        }
        return fileToFileLogInfo(FileUtil.ls(path));
    }


    /**
     * 查询logPath下面的typeName的日志目录下面的所有目录
     * @param type all, err , info ,warning , debug
     * @param logPath 获取logPath下面指定的类型下面的目录，如logPath/all logPath/err  return -- >logPath/all/[yyyy-MM-dd] logPath/err/[yyyy-MM-dd]
     * @return
     */
    @Override
    public List<FileLogInfo> getTypeDirectory(FileLogType type, String logPath){
        return fileToFileLogInfo(FileUtil.ls(getPath(type,logPath)));
    }


    /**
     * 根据日志类型和路径返回日志日期路径
     * @param type
     * @param logPath
     * @return
     */
    private String getPath(FileLogType type, String logPath){
        StringBuffer stringBuffer = new StringBuffer(logPath);
        if(logPath.charAt(logPath.length() - 1) == '\\' || logPath.charAt(logPath.length() - 1) == '/'){
            stringBuffer.append(type.getTypeValue());
        }else{
            stringBuffer.append("/" + type.getTypeValue());
        }
        return stringBuffer.toString();
    }
}
