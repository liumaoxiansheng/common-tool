package com.example.commontool.utils.http;

import cn.hutool.http.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: HttpUtil
 * @Description: http请求工具
 * @Author: th_legend
 **/
@Slf4j
public class CommonHttpUtil {
    private static final String APPLICATION_JSON_HEADER = "application/json;charset=UTF-8";

    private CommonHttpUtil() {
    }

    /**
     * post请求
     *
     * @param url: 请求地址
     * @param reportData: 请求数据
     * @return: java.lang.String 返回数据
     * @Author: th_legend
     **/
    public static String post(String url, String reportData) {
        HttpRequest request = HttpUtil.createRequest(Method.POST, url)
                .header(Header.CONTENT_TYPE, APPLICATION_JSON_HEADER)
                .body(reportData);
        HttpResponse response = doHttpRequset(request);
        return response.body();
    }

    private static HttpResponse doHttpRequset(HttpRequest request) {
        log.info("{}", request);
        HttpResponse response = request.execute();
        log.info("{}", response);
        return response;
    }

    /**
     * post请求
     *
     * @param url: 请求地址
     * @param reportData: 请求数据
     * @param userName: 账号
     * @param password: 密码
     * @return: java.lang.String 返回数据
     * @Author: th_legend
     **/
    public static String postBasicAuth(String url, String reportData,String userName,String password) {
        HttpRequest request = HttpUtil.createRequest(Method.POST, url)
                .header(Header.CONTENT_TYPE, APPLICATION_JSON_HEADER)
                .basicAuth(userName,password)
                .body(reportData);
        HttpResponse response = doHttpRequset(request);
        return response.body();
    }
    /**
     * post请求
     *
     * @param url: 请求地址
     * @param reportData: 请求数据
     * @param bearerToken: token eg:  Bearer ca7b9faf-a200-4019-a681-d46f34e2f630
     * @return: java.lang.String 返回数据
     * @Author: th_legend
     **/
    public static String postBearerAuth(String url, String reportData,String bearerToken) {
        HttpRequest request = HttpUtil.createRequest(Method.POST, url)
                .header(Header.CONTENT_TYPE, APPLICATION_JSON_HEADER)
                .auth(bearerToken)
                .body(reportData);
        HttpResponse response = doHttpRequset(request);
        return response.body();
    }

    /**
     * 获取当前网络ip
     *
     * @param request
     * @return
     */
//    public static String getIpAddr(HttpServletRequest request) {
//        String ipAddress = request.getHeader("x-forwarded-for");
//        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
//            ipAddress = request.getHeader("Proxy-Client-IP");
//        }
//        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
//            ipAddress = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
//            ipAddress = request.getRemoteAddr();
//            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
//                //根据网卡取本机配置的IP
//                InetAddress inet = null;
//                try {
//                    inet = InetAddress.getLocalHost();
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                }
//                ipAddress = inet.getHostAddress();
//            }
//        }
//        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
//        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
//            if (ipAddress.indexOf(",") > 0) {
//                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
//            }
//        }
//        return ipAddress;
//    }
//
//    public static HttpServletRequest getRequest() {
//        try {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            return request;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
