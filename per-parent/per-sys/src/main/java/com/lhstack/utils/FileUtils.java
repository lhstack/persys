package com.lhstack.utils;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.FileCopyUtils;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class FileUtils {

    /**
     * 定制方法，只能用于此系统，logPath目录下目录文件命名规则应该为yyyy-MM-dd
     * @param logPath 如 E:\logback\v1\all\2019-11-15
     * @param expireDay 超时时间，详情请看checkIsExpireAndClear
     */
    public static void clearExpireLogFile(String logPath, Integer expireDay){
        try {
            //directory.getName() == 2019-11-15
            File directory = new File(logPath);
            checkIsExpireAndClear(directory.getName(),directory,expireDay,"yyyy-MM-dd");
        } catch (Exception e) {
            return ;
        }
    }

    /**
     *  满足超过expireDate的dateName，就删除file
     * @param dateName 时间 格式yyyy-MM-dd
     * @param file 要删除的文件
     * @param expireDate 超过时间
     * @param format 日期格式
     * @throws Exception
     */
    public static void checkIsExpireAndClear(String dateName,File file,Integer expireDate,String format) throws Exception {
        Date date = new SimpleDateFormat(format).parse(dateName);
        Date matchDate = DateUtils.addDays(new Date(), -expireDate);
        if(date.getTime() <= matchDate.getTime()){
            clearExpireFile(file);
        }
    }

    /**
     * 删除文件或者目录
     * @param file
     * @return
     */
    public static Boolean clearExpireFile(File file) {
        return FileUtil.del(file);
    }

    /**
     * 将filePart转换成byte[]
     * @param filePart
     * @return
     */
    public static byte[] filePartToBytes(FilePart filePart){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        filePart.content().subscribe(dataBuffer -> {
            try {
                byteArrayOutputStream.write(dataBuffer.asByteBuffer().array());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 将filePart复制到file
     * @param filePart
     * @param file
     * @throws IOException
     */
    public static void filePartToFile(FilePart filePart, File file) throws IOException {
        FileCopyUtils.copy(filePartToBytes(filePart),file);
    }

    /**
     * 将filePart输出到outputStream
     * @param filePart
     * @param outputStream
     * @throws IOException
     */
    public static void filePartToOutputStream(FilePart filePart, OutputStream outputStream) throws IOException {
        outputStream.write(filePartToBytes(filePart));
    }

    public static String generatorFileName(String generatorType, String fileName) {
        switch (generatorType.toLowerCase()){
            case "uuid":{
                return UUID.randomUUID().toString() + "." + FileUtil.extName(fileName);
            }
            case "origin":{
                return fileName;
            }
        }
        return fileName;
    }

    public static Map<String,byte[]> toMapFileMappingContents(Flux<FilePart> fileParts) {
        List<Map<String, byte[]>> mapList = fileParts.map(item -> {
            Map<String, byte[]> map = new HashMap<>();
            map.put(item.filename(), FileUtils.filePartToBytes(item));
            return map;
        }).collect(Collectors.toList()).block();
        return mapList.stream().flatMap(item -> item.entrySet().stream())
                .collect(Collectors.toMap(item -> item.getKey(),item -> item.getValue()));
    }
}
