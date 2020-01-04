package com.lhstack.common;

import com.sun.management.OperatingSystemMXBean;

import java.io.File;
import java.lang.management.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SystemUtils {

    public static Map<String, Object> getSystemInfo() {
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Map<String, Object> map = new HashMap<>();
        map.put("operationSystem", operatingSystemMXBean.getName());
        map.put("arch", operatingSystemMXBean.getArch());
        map.put("processors", operatingSystemMXBean.getAvailableProcessors());
        map.put("committedVirtualMemorySize",operatingSystemMXBean.getCommittedVirtualMemorySize());
        map.put("freePhysicalMemorySize",operatingSystemMXBean.getFreePhysicalMemorySize());
        map.put("freeSwapSpaceSize",operatingSystemMXBean.getFreeSwapSpaceSize());
        map.put("processCpuTime",operatingSystemMXBean.getProcessCpuTime());
        map.put("systemCpuLoad",operatingSystemMXBean.getSystemCpuLoad());
        map.put("totalPhysicalMemorySize",operatingSystemMXBean.getTotalPhysicalMemorySize());
        map.put("totalSwapSpaceSize",operatingSystemMXBean.getTotalSwapSpaceSize());
        return map;
    }

    public static Map<String, Object> getMemoryInfo() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        Map<String, Object> map = new HashMap<>();
        map.put("heapMemoryUsage.committed", memoryMXBean.getHeapMemoryUsage().getCommitted());
        map.put("heapMemoryUsage.init", memoryMXBean.getHeapMemoryUsage().getInit());
        map.put("heapMemoryUsage.max", memoryMXBean.getHeapMemoryUsage().getMax());
        map.put("heapMemoryUsage.used", memoryMXBean.getHeapMemoryUsage().getUsed());
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        map.put("nonHeapMemoryUsage.used", nonHeapMemoryUsage.getUsed());
        map.put("nonHeapMemoryUsage.init", nonHeapMemoryUsage.getInit());
        map.put("nonHeapMemoryUsage.max", nonHeapMemoryUsage.getMax());
        map.put("nonHeapMemoryUsage.committed", nonHeapMemoryUsage.getCommitted());
        map.put("objectPendingFinalizationCount", memoryMXBean.getObjectPendingFinalizationCount());
        return map;

    }

    public static Map<String,Map<String, Object>> getDeskInfo(){
        Map<String, Map<String, Object>> collect = Arrays.asList(File.listRoots()).stream().map(item -> {
            Map<String, Object> fileInfo = new HashMap<>();
            fileInfo.put("freeSpace", item.getFreeSpace());
            fileInfo.put("usableSpace", item.getUsableSpace());
            fileInfo.put("totalSpace", item.getTotalSpace());
            fileInfo.put("name", item.getAbsolutePath().replaceAll("(:|\\\\)",""));
            return fileInfo;
        }).collect(Collectors.toMap(item -> item.remove("name").toString(), item -> item));
        return collect;
    }

    public static Map<String, Object> getThreadInfo() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        Map<String, Object> map = new HashMap<>();
        map.put("currentThreadCpuTime", threadMXBean.getCurrentThreadCpuTime());
        map.put("currentThreadUserTime",threadMXBean.getCurrentThreadUserTime());
        map.put("daemonThreadCount", threadMXBean.getDaemonThreadCount());
        map.put("peakThreadCount", threadMXBean.getPeakThreadCount());
        map.put("totalStartedThreadCount", threadMXBean.getTotalStartedThreadCount());
        map.put("deadlockedThreads", threadMXBean.findDeadlockedThreads());
        map.put("monitorDeadlockedThreads", threadMXBean.findMonitorDeadlockedThreads());
        return map;
    }

    public static Map<String, Object> getRunTimeInfo() {
        Map<String,Object> map = new HashMap<>();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        map.put("bootClassPath",runtimeMXBean.getBootClassPath());
        map.put("classpath",runtimeMXBean.getClassPath());
        map.put("inputArguments",runtimeMXBean.getInputArguments());
        map.put("libraryPath",runtimeMXBean.getLibraryPath());
        map.put("managementSpecVersion",runtimeMXBean.getManagementSpecVersion());
        map.put("name",runtimeMXBean.getName());
        map.put("specName",runtimeMXBean.getSpecName());
        map.put("specVendor",runtimeMXBean.getSpecVendor());
        map.put("specVersion",runtimeMXBean.getSpecVendor());
        map.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(runtimeMXBean.getStartTime())));
        map.put("uptime",runtimeMXBean.getUptime());
        map.put("vmName",runtimeMXBean.getVmName());
        map.put("vmVendor",runtimeMXBean.getVmVendor());
        map.put("vmVersion",runtimeMXBean.getVmVersion());
        return map;
    }

    public static Map<String, Object> getClassLoaderInfo() {
        Map<String, Object> map = new HashMap<>();
        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        map.put("totalLoadedClassCount", classLoadingMXBean.getTotalLoadedClassCount());
        map.put("loadedClassCount", classLoadingMXBean.getLoadedClassCount());
        map.put("unloadedClassCount", classLoadingMXBean.getUnloadedClassCount());
        return map;
    }

}
