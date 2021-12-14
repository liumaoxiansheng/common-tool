package com.example.commontool.utils.file;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName: CommonFtpUtil
 * @Description: Ftp工具
 * @Author: th_legend
 **/
@Slf4j
public class CommonFtpUtil {


    private final static String host="127.0.0.1";
    private final static int port=21;
    private final static String username="user";
    private final static String password="pwd";

//    @Autowired
//    private ResourceLoader resourceLoader;
    private final static String PROTOCOL = "SSL";
    private final static String DIRSPLIT = "/";

    private final static String KEYSTORE_PASS = "";


    /**
     *   上传文件到ftps
     *
     * @param buffIn: 文件输入流
     * @param fileName: 文件名称  eg: a.doc
     * @param dirName: 上传的文件夹目录，从根目录"/"开始, eg:/20211209
     * @return: boolean
     * @Author: th_legend
     **/
    public  boolean uploadToFtps(InputStream buffIn, String fileName, String dirName)
            throws NoSuchAlgorithmException, IOException, UnrecoverableKeyException,
            KeyStoreException, CertificateException {
        boolean storeFile = false, binaryTransfer = false,result=false;
        binaryTransfer = true;
        FTPSClient ftpsClient = new FTPSClient(PROTOCOL);
        setSSLContext();
        storeFile = true;
        ftpsClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        try {
            int reply;
            // 连接服务
            ftpsClient.connect(host);
            ftpsClient.execPROT("P");
            log.info("Connected to " + host + ".");
            reply = ftpsClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpsClient.disconnect();
                log.error("FTP server refused connection.");
            }
        } catch (IOException e) {
            if (ftpsClient.isConnected()) {
                try {
                    ftpsClient.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            log.error("Could not connect to server.",e);
        }

        __login:
        try {
            log.info("Completed SSL Handshake");
            ftpsClient.setBufferSize(1024);
            // 登录服务
            if (!ftpsClient.login(username, password)) {
                ftpsClient.logout();
                break __login;
            }
            log.info("Remote system is " + ftpsClient.getSystemName());
            if (binaryTransfer) {
                ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
            }
            ftpsClient.enterLocalPassiveMode();
            // 文件夹处理
            String basePath = "/";
            String[] pathArray = dirName.split(DIRSPLIT);
            for (String path : pathArray) {
                basePath += path + DIRSPLIT;
                //3.指定目录 返回布尔类型 true表示该目录存在
                boolean dirExsists = ftpsClient.changeWorkingDirectory(basePath);
                //4.如果指定的目录不存在，则创建目录
                if (!dirExsists) {
                    //此方式，每次，只能创建一级目录
                    boolean flag = ftpsClient.makeDirectory(basePath);
                    if (flag) {
                        log.info("创建成功！");
                    }
                }
            }
            //进入文件目录
            ftpsClient.changeWorkingDirectory(dirName);
            if (storeFile) {
                InputStream input;
                input =  buffIn;
                // 上传文件
                result=ftpsClient.storeFile(fileName, input);
                log.info("Stored local File : " + fileName + " to Server :" + host + " at : " + dirName+DIRSPLIT+fileName);
                input.close();
            }
            // 退出
            ftpsClient.logout();
        } catch (FTPConnectionClosedException e) {
            log.error("Server closed connection.");
        } catch (IOException e) {
        } finally {
            if (ftpsClient.isConnected()) {
                try {
                    // 断开连接
                    ftpsClient.disconnect();
                } catch (IOException f) {
                }
            }
        }

        return result;
    }

    public String uploadToFtps(String url) {
        InputStream inputStream = getFileInputStream(url);
        if (inputStream == null) {
            return null;
        }
        String dir = getCurrentDateDir();
        String extention = getExtention(url);
        //使用uuid，保存文件名唯一性
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + extention;
        boolean uploadOk = false;
        try {
            uploadOk = uploadToFtps(inputStream, fileName, dir);
        } catch (Exception e) {
            log.error("uploadToFtp() error ", e);
        }
        if (uploadOk) {
            return dir + DIRSPLIT + fileName;
        }
        return null;
    }



    public static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    public static String getNoPointExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1);
    }


//    private KeyManager[] getKeyManagers()
//            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
//            IOException, UnrecoverableKeyException, CertificateException {
//        KeyStore ks = KeyStore.getInstance("JKS");
//        Resource resource = resourceLoader.getResource("classpath:" + "certificate.crt");
//        InputStream crt = resource.getInputStream();
//        ks.load(crt, KEYSTORE_PASS.toCharArray());
//        KeyManagerFactory tmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//        System.out.println("KeyManagerFactory.getDefaultAlgorithm() : " + KeyManagerFactory.getDefaultAlgorithm());
//        tmf.init(ks, KEYSTORE_PASS.toCharArray());
//        return tmf.getKeyManagers();
//    }

//    private TrustManager[] getTrustManagers()
//            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
//            IOException, UnrecoverableKeyException, CertificateException {
//        KeyStore ks = KeyStore.getInstance("JKS");
//        Resource resource = resourceLoader.getResource("classpath:" + "certificate.crt");
//        InputStream crt = resource.getInputStream();
//        ks.load(crt, KEYSTORE_PASS.toCharArray());
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//        tmf.init(ks);
//        return tmf.getTrustManagers();
//    }

    private void setSSLContext() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
//        KeyManager keyManager = getKeyManagers()[0];
//        TrustManager trustManager = getTrustManagers()[0];
//        ftpsClient.setKeyManager(keyManager);
//        ftpsClient.setTrustManager(trustManager);
        log.info("Done Setting keyManager and trustManager");
    }

    public static String getCurrentDateDir() {
        return DIRSPLIT + DateUtil.format(new Date(), "yyyyMMdd");
    }

    /**
     * 获取文件输入流
     *
     * @param url: 可正常访问的有效文件URL地址
     * @return: java.io.InputStream
     **/
    public static InputStream getFileInputStream(String url) {
        try {
            URL realUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return inputStream;
        } catch (IOException e) {
            log.error("url get inputStream exception ", e);
        }
        return null;

    }


    //上传
    public boolean uploadToFtp(InputStream buffIn, String fileName, boolean needDelete,String dirName) throws  Exception {
        boolean returnValue = false;
        FTPClient ftpClient =null;
        try {
            // 建立连接
             ftpClient = connectToServer();
            // 设置传输二进制文件
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new IOException("failed to connect to the FTP Server:" + host);
            }
            /*ftpClient.enterLocalPassiveMode();*/
            ftpClient.setControlEncoding("UTF-8");
            // 根目录，判断文件是否存在
            String basePath="/";
            String[] pathArray = dirName.split(DIRSPLIT);
            for(String path:pathArray){
                basePath+=path+DIRSPLIT;
                //3.指定目录 返回布尔类型 true表示该目录存在
                boolean dirExsists = ftpClient.changeWorkingDirectory(basePath);
                //4.如果指定的目录不存在，则创建目录
                if(!dirExsists){
                    //此方式，每次，只能创建一级目录
                    boolean flag=ftpClient.makeDirectory(basePath);
                    if (flag){
                        System.out.println("创建成功！");
                    }
                }
            }
            //进入文件目录
            ftpClient.changeWorkingDirectory(dirName);

            // 上传文件到ftp
            returnValue = ftpClient.storeFile(fileName, buffIn);
            if (needDelete) {
                ftpClient.deleteFile(fileName);
            }
            // 输出操作结果信息
            if (returnValue) {
                log.info("uploadToFtp INFO: upload file  to ftp : succeed!");
            } else {
                log.error("uploadToFtp INFO: upload file  to ftp : failed!");
            }
            buffIn.close();
            // 关闭连接
            closeConnect(ftpClient);
        } catch (FTPConnectionClosedException e) {
            log.error("ftp连接被关闭！");
            throw e;
        } catch (Exception e) {
            returnValue = false;
            log.error("ERR : upload file  to ftp : failed! ");
            throw e;
        } finally {
            try {
                if (buffIn != null) {
                    buffIn.close();
                }
            } catch (Exception e) {
                log.error("ftp关闭输入流时失败！");
            }
            if (ftpClient!=null&&ftpClient.isConnected()) {
                closeConnect(ftpClient);
            }
        }
        return returnValue;
    }

    public String uploadToFtp(String url) {
        InputStream inputStream = getFileInputStream(url);
        if (inputStream==null) {
            return null;
        }
        String dir = getCurrentDateDir();
        String extention = getExtention(url);
        //使用uuid，保存文件名唯一性
        String uuid= UUID.randomUUID().toString();
        String fileName=uuid+extention;
        boolean uploadOk = false;
        try {
            uploadOk = uploadToFtp(inputStream, fileName, Boolean.FALSE, dir);
        } catch (Exception e) {
            log.error("uploadToFtp() error ",e);
        }
        if (uploadOk){
            return dir+fileName;
        }
        return null;
    }




    //下载
    public InputStream downloadFileFromFtp(String filename,String dirName)
            throws IOException {
        InputStream in = null;
        FTPClient ftpClient=null;
        try {

            // 建立连接
            ftpClient = connectToServer();
            ftpClient.enterLocalPassiveMode();
            // 设置传输二进制文件
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new IOException("failed to connect to the FTP Server:" + host);
            }
            ftpClient.changeWorkingDirectory(dirName);

            // ftp文件获取文件
            in = ftpClient.retrieveFileStream(filename);

        } catch (FTPConnectionClosedException e) {
            log.error("ftp连接被关闭！");
            throw e;
        } catch (Exception e) {
            log.error("ERR : upload file " + filename + " from ftp : failed!");

        }
        return in;
    }


    public void closeConnect(FTPClient ftpClient) {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            log.error("ftp连接关闭失败！",e);

        }
    }

    private FTPClient connectToServer() throws Exception {

            FTPClient  ftpClient=null;
            int reply;
            try {
                ftpClient = new FTPClient();
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.enterLocalPassiveMode();
                ftpClient.connect(host, port);
                ftpClient.login(username, password);
                reply = ftpClient.getReplyCode();

                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    log.error("connectToServer FTP server refused connection. connection info host is {} port is {}",host,port);
                }

            } catch (FTPConnectionClosedException ex) {
                log.error("没有连接数！there are too many connected users,please try later");
                throw ex;
            } catch (Exception e) {
                log.error("登录ftp服务器失败");
                throw e;
            }
            return ftpClient;

    }



}
