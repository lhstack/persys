package com.lhstack;

import cn.hutool.core.io.FileUtil;
import com.lhstack.service.log.impl.FileLogServiceImpl;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

class DemoApplicationTests {

    private static String logUrl = "E:/logback";
    public static void main(String[] args) throws Exception {
       /* File directory = new File(logUrl + "/all");
        File[] files = directory.listFiles();
        for(File file : files){
            checkIsExpire(file.getName(),file);

        }*/
        FileLogServiceImpl fileLogService = new FileLogServiceImpl();
        fileLogService.clearFileLogCache(5);
    }

    private static void checkIsExpire(String dateName,File file) throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateName);
        Date matchDate = DateUtils.addDays(new Date(), -5);
        if(date.getTime() <= matchDate.getTime()){
            clearExpireFile(file);
        }
    }

    private static Boolean clearExpireFile(File file) {
        return FileUtil.del(file);
    }

}
