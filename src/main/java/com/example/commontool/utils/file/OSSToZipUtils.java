package com.example.commontool.utils.file;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: OSSToZipUtils
 * @Description: oss文件转zip工具方法
 * @Author: th_legend
 **/
@Slf4j
public class OSSToZipUtils {

    /**
     * 获取当前系统的临时目录
     */
     private static final String FILE_PATH = System.getProperty("java.io.tmpdir");



    /**
     * oss文件转zip
     *
     * @param ossZipName: zip名称
     * @param paths:      oss文件集合,key 源文件名 value 别名
     * @return: void
     * @Author: th_legend
     **/
    public static void getZipFromOSSByPathMap(String ossZipName,
                                              Map<String, String> paths) {
        String tmpFileName = FILE_PATH + DateUtil.format(new Date(), "yyyyMMddHHmmss") + (int) (Math.random() * 100000) + ".zip";
        String zipDirName = FILE_PATH + "ziptemp" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + (int) (Math.random() * 100000);
        if (paths.size() == 0) {
            log.error("NO::fILE::::>>{}", tmpFileName);
            return;
        }
        File dirFile = new File(zipDirName);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        // 循环根据路径从OSS获得对象，存入临时文件zip文件夹中
        Set<Map.Entry<String, String>> entries = paths.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String sourceFilePath = entry.getKey();
            String targetFileName = entry.getValue();
            FileOutputStream subFos = null;
            InputStream is = null;
            try {
                subFos = new FileOutputStream(zipDirName + File.separator + targetFileName);
                is = getFileInputStream(sourceFilePath);
                IoUtil.copy(is, subFos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error("Error:::>>>", e);
                    }
                }

                if (subFos != null) {
                    try {
                        subFos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error("Error:::>>>", e);
                    }
                }
            }
        }
        //压缩
        try {
            ZipUtil.zip(zipDirName, tmpFileName);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error:::>>>", e);
        }
        // 上传文件
        File tempFile = new File(tmpFileName);
        // AliOssUtil.upload(tempFile, ossZipName);

        // 删除临时文件
       FileUtil.del(dirFile);
       tempFile.deleteOnExit();
    }


    /**
     * oss文件转zip
     *
     * @param ossZipName: zip名称
     * @param paths:      oss文件集合
     * @return: void
     * @Author: th_legend
     **/
    public static void getZipFromOSSByPaths(String ossZipName,
                                            List<String> paths) {
        String tmpFileName = FILE_PATH + DateUtil.format(new Date(), "yyyyMMddHHmmss") + (int) (Math.random() * 100000) + ".zip";
        String zipDirName = FILE_PATH + "ziptemp" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + (int) (Math.random() * 100000);
       // log.error("tmpFileName::::>>{}", tmpFileName);
        if (paths.size() == 0) {
            log.error("NO::fILE::::>>{}", tmpFileName);
            return;
        }
        File dirFile = new File(zipDirName);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        // 循环根据路径从OSS获得对象，存入临时文件zip文件夹中
        for (String path : paths) {
            // 将文件放入zip中，并命名不能重复
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            FileOutputStream subFos = null;
            InputStream is = null;
            try {
                subFos = new FileOutputStream(zipDirName + File.separator + fileName);
                is = getFileInputStream(path);
                IoUtil.copy(is, subFos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error("Error:::>>>", e);
                    }
                }

                if (subFos != null) {
                    try {
                        subFos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error("Error:::>>>", e);
                    }
                }
            }
        }
        //压缩
        try {
            ZipUtil.zip(zipDirName, tmpFileName);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error:::>>>", e);
        }
        // 上传文件
        File tempFile = new File(tmpFileName);
      //  AliOssUtil.upload(tempFile, ossZipName);

        // 删除临时文件
        FileUtil.del(dirFile);
        tempFile.deleteOnExit();

    }


    /**
     * 获取文件输入流
     *
     * @param url: 可正常访问的有效文件URL地址
     * @return: java.io.InputStream
     * @Author: th_legend
     **/
    public static InputStream getFileInputStream(String url) {
        try {
            URL realUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
