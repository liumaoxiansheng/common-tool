package com.example.commontool.utils.file;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @ClassName: FreeMarkerUtil
 * @Description: freeMarker模板工具
 * @Author: th_legend
 **/
@Slf4j
public class FreeMarkerUtil {
    /**
     * 写入数据
     *
     * @param dataMap      word中需要展示的动态数据，用map集合来保存
     * @param ftlDir      ftl模板相对目录
     * @param templateName word模板名称，例如：test.ftl
     */
    @SuppressWarnings("unchecked")
    public static byte[] writeData(Map dataMap, String ftlDir, String templateName) {
        Writer out = null;
        byte[] bytes=null;
        try {
            // 创建配置实例
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
            // 设置编码
            configuration.setDefaultEncoding("UTF-8");
            // 设置处理空值
            configuration.setClassicCompatible(true);
            // 设置ftl模板文件加载方式
            configuration.setClassForTemplateLoading(FreeMarkerUtil.class,ftlDir);
            out = new CharArrayWriter();
            // 获取模板
            Template template = configuration.getTemplate(templateName);
            // 生成文件
            template.process(dataMap, out);
            // 上传oss
            bytes=out.toString().getBytes(StandardCharsets.UTF_8);
            // 清空缓存
            out.flush();
            // 关闭流
            out.close();
        } catch (Exception e) {
            log.error("【生成文件出错】：==>",e);
            e.printStackTrace();
        }finally {
            if(null != out){
                try {
                    out.close();
                }catch (Exception e){
                    log.error("【关闭异常】：==>",e);
                }
            }
        }
        return bytes;
    }

    /**
     * 写入数据到文件
     *
     * @param dataMap: 数据
     * @param ftlDir: 模板文件夹
     * @param templateName: 模板名称
     * @param filePath: 文件地址
     * @return: java.io.File
     **/
    public static File writeToFile( Map dataMap,String ftlDir,String templateName, String filePath) throws Exception {
        FileWriter out = null;
        try {
            // 通过FreeMarker的Confuguration读取相应的模板文件
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
            // 设置模板路径
            configuration.setClassForTemplateLoading(FreeMarkerUtil.class, ftlDir);
            // 设置默认字体
            configuration.setDefaultEncoding("utf-8");
            // 获取模板
            Template template = configuration.getTemplate(templateName);
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if(!file.exists()) {
                file.createNewFile();
            }else {
                log.error("文件{}已存在",filePath);
               throw new Exception("文件"+filePath+"已存在");
            }
            //设置输出流
            out = new FileWriter(file);
            //模板输出静态文件
            template.process(dataMap, out);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
