package net.seehope.crowd.util;

import net.seehope.crowd.constant.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * 工具方法类
 * @author JoinYang
 * @date 2022/3/17 11:57
 *
 */
public class CrowdUtil {

    //md5明文加密
    public static String md5(String source){
        // 1、判断source是否有效
        if (source==null||source.length()==0){
            // 2、如果不是有效字符串，就抛异常
            throw new RuntimeException(CrowdConstant.MASSAGE_STRING_INVALIDATE);
        }

        // 3、获取MessageDigest对象
        String algorithm="md5";
        try {
            MessageDigest messageDigest =MessageDigest.getInstance(algorithm);

            // 4、获取明文字符对应的字节数组
            byte[] input=source.getBytes();

            // 5、执行加密
            byte[] output =messageDigest.digest(input);

            // 6、创建BigInteger对象
            int signum=1;
            BigInteger bigInteger = new BigInteger(signum, output);

            //7、按照16进制将bigInteger的值转换为字符串
            int radix=16;
            String encoding=bigInteger.toString(radix);

            return encoding;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }




    //判断当前是否为Ajax请求
    public static boolean isAjaxRequestType(HttpServletRequest request){
        //1、获取请求消息头
        String acceptHeader=request.getHeader("Accept");
        String xRequestHeader=request.getHeader("X-Requested-With");

        //2、判断
        return ((acceptHeader!=null&&acceptHeader.contains("application/json"))||(xRequestHeader!=null&&xRequestHeader.equals("XMLHttpRequest")));
    }

}
