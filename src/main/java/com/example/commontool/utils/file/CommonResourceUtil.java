package com.example.commontool.utils.file;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * @ClassName: CommonResourceUtil
 * @Description: 资源文件工具类
 * @Author: th_legend
 **/
public class CommonResourceUtil {

    private static String BASE64_HEADER="data:image/jpeg;base64,";

    /**
     * 将BASE64编码字符串转换为字节数组
     *
     * @param base64Str BASE64编码字符串
     * @return 转换后的字节数组
     */
    public static byte[] base64ToBytes(String base64Str) {
        if (StrUtil.isNotBlank(base64Str)) {
            return Base64.decode(base64Str);
        }
        return null;
    }

    // data:image/jpeg;base64,  data:image/png;base64,

    /**
     * 将网络地址内容转换为BASE64编码字符串
     *
     * @param url 网络地址
     * @return 转换后的BASE64编码字符串
     */
    public static String urlToBase64(String url) throws ValidateException {
        if (StrUtil.isBlank(url)) {
            return null;
        }
        Validator.validateUrl(url, "不是有效URL");
        try {
            HttpRequest request = HttpUtil.createGet(url);
            return Base64.encode(request.execute().bodyBytes());
        } catch (HttpException e) {
            return null;
        }
    }
    public static String urlToBase64IncludeHeader(String url,Long scaleSize) throws ValidateException {
        if (StrUtil.isBlank(url)) {
            return null;
        }
        Validator.validateUrl(url, "不是有效URL");

        try {
            HttpRequest request = HttpUtil.createGet(url);
            String base64 = Base64.encode(request.execute().bodyBytes());
            if (scaleSize!=null){
                base64=  compressUnderSize(base64,scaleSize);
            }
            return BASE64_HEADER+ base64;
        } catch (HttpException e) {
            return null;
        }
    }

    /**
     * 将字节数组转换为BASE64编码字符串
     *
     * @param bytes 字节数组
     * @return 转换后的BASE64编码字符串
     */
    public static String byteToBase64(byte[] bytes) {
        return Base64.encode(bytes);
    }

    /**
     * 将图片压缩到指定大小以内
     *
     * @param base64  源图片数据
     * @param maxSize 目的图片大小
     * @return 压缩后的图片数据
     */
    public static String compressUnderSize(String base64, long maxSize) {
        float scale = 0.9f;
        if (base64.length() > maxSize) {
            do {
                try {
                    System.out.println(base64.length());
                    base64 = scale(base64, scale);
                } catch (Exception e) {
                    throw new IllegalStateException("压缩图片过程中出错，请及时联系管理员！", e);
                }
            } while (base64.length() > maxSize);
        }
        return base64;
    }

    /**
     * 压缩图片
     *
     * @param base64 图片字符串
     * @param scale  压缩比例
     * @return
     */
    public static String scale(String base64, float scale) {
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        String result = null;
        try {
            Image image = ImgUtil.toImage(base64);
            ImgUtil.scale(image, ops, scale);
            result = byteToBase64(ops.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ops.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 压缩图片
     *
     * @param bytes 图片二进制
     * @param scale  压缩比例
     * @return
     */
    public byte[]  scale(byte[] bytes, float scale) {
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        String result = null;
        try {
            Image image = ImgUtil.toImage(bytes);
            ImgUtil.scale(image, ops, scale);
            return ops.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ops.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将网络地址内容转换为BASE64编码字符串
     *
     * @param url 网络地址
     * @return 转换后的BASE64编码字符串
     */
    public static BufferedImage urlToBufferedImage(String url) throws ValidateException {
        Validator.validateUrl(url, "不是有效URL");
        try {
            HttpRequest request = HttpUtil.createGet(url);
            return ImgUtil.toImage(request.execute().bodyBytes());
        } catch (HttpException e) {
            e.printStackTrace();
            return null;
        }
    }
}
