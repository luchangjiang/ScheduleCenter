package com.giveu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giveu.job.common.util.MD5Util;
import jodd.http.HttpMultiMap;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import java.util.Date;

/**
 * @program: ScheduleCenter
 * @description: 发送消息
 * @author: qin
 * @create: 2018-08-22 10:01
 **/

public class SendMessage {
//    private static final String sendMessageUrl = "http://localhost:8080/";
    private static final String sendMessageUrl = "http://10.11.13.26/";
//       private static final String sendMessageUrl = "http://10.14.21.38:9000/";
//       private static final String sendMessageUrl = "http://10.14.21.38:8080/";
//	private static final String dataUrl = "http://10.12.11.196:8765/";
//	private static final String dataUrl = "http://10.10.11.52:8765/";
    private static final String appSecret = "FASF@_23412@!ADFAXYY_23TT";

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            sendDataMessage();
            Thread.sleep(100);
        }
    }

    public static void sendDataMessage() {
        HttpRequest httpRequest = HttpRequest.post(sendMessageUrl + "sendMessage/send");
        String xGiveuAppKey = "sendmsg";
        String xGiveuTimestamp = String.valueOf(new Date().getTime());
        String type = "1";
        String id = "ADFASDFADSFAD";
        // String msg = "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看< a href='http://work.weixin.qq.com'>邮件中心视频实况</ a>，聪明避开排队。";
        String msg = "数据预警通知\n8月6日\n类别：接口预警\n服务器地址：10.0.10.0\n系统名称：fdsafdasf\n预警信息：接口错误\n< a href='noticecenter.jyfq.com/sendMessage/send'>详情</ a>";
        //String msg="fdsafdasfdsa";
        httpRequest.header("xGiveuAppKey", xGiveuAppKey);
        httpRequest.header("xGiveuTimestamp", xGiveuTimestamp);
        HttpMultiMap multiMap = httpRequest.query();
        multiMap.add("id", id);
        multiMap.add("type", type);
        multiMap.add("msg", msg);

        StringBuilder sb = new StringBuilder();
        sb.append("xGiveuAppKey").append(xGiveuAppKey);
        sb.append("xGiveuTimestamp").append(xGiveuTimestamp);
        sb.append("id").append(id);
        sb.append("type").append(type);
        sb.append("msg").append(msg);
        sb.append(appSecret);

        String original = sb.toString();
        String encrypted = MD5Util.sign(original);
        httpRequest.header("xGiveuSign", encrypted);
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse response = httpRequest.send();
        System.out.println(response.bodyText());
    }

//    public static void sendDataMessage() {
//
//        String xGiveuAppKey = "sendmsg";
//        String xGiveuTimestamp = String.valueOf(new Date().getTime());
//        String type = "1";
//        String id = "ADFASDFADSFAD";
//        // String msg = "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href='http://work.weixin.qq.com'>邮件中心视频实况</a>，聪明避开排队。";
//        String msg = "数据预警通知\n8月6日\n类别：接口预警\n服务器地址：10.0.10.0\n系统名称：fdsafdasf\n预警信息：接口错误\n<a href='http://work.weixin.qq.com'>详情</a>";
////        try {
////            msg = java.net.URLEncoder.encode(msg, "utf-8");
////        } catch (Exception e) {
////            return;
////        }
//
////        HttpRequest httpRequest = HttpRequest.post(sendMessageUrl + "sendMessage/send").form(
////                "id",id,
////                "type",type,
////                "msg",msg
////        );
//        HttpRequest httpRequest = HttpRequest.post(sendMessageUrl + "sendMessage/send");
//        httpRequest.header("xGiveuAppKey", xGiveuAppKey);
//        httpRequest.header("xGiveuTimestamp", xGiveuTimestamp);
//
////        StringBuilder sb = new StringBuilder();
////        sb.append("xGiveuAppKey").append(xGiveuAppKey);
////        sb.append("xGiveuTimestamp").append(xGiveuTimestamp);
////        sb.append("id").append(id);
////        sb.append("type").append(type);
////        sb.append("msg").append(msg);
////        sb.append(appSecret);
////
////        String original = sb.toString();
////        String encrypted = MD5Util.sign(original);
////        httpRequest.header("xGiveuSign", encrypted);
////
////        HttpResponse response = httpRequest.send();
////        String rest = response.bodyText();
////        System.out.println(rest);
//
//        HttpMultiMap multiMap = httpRequest.query();
//        multiMap.add("id", id);
//        multiMap.add("type", type);
//        multiMap.add("msg", msg);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("xGiveuAppKey").append(xGiveuAppKey);
//        sb.append("xGiveuTimestamp").append(xGiveuTimestamp);
//        sb.append("id").append(id);
//        sb.append("type").append(type);
//        sb.append("msg").append(msg);
//        sb.append(appSecret);
//
//        String original = sb.toString();
//        String encrypted = MD5Util.sign(original);
//        httpRequest.header("xGiveuSign", encrypted);
//        ObjectMapper mapper = new ObjectMapper();
//        HttpResponse response = httpRequest.send();
//        response.contentType("Content-type", "text/html;charset=UTF-8");
//        //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
//        response.charset("UTF-8");
////        System.out.println(response);
//        System.out.println(response.bodyText());
//    }

    public static void sendTaskMessage() {
        HttpRequest httpRequest = HttpRequest.post(sendMessageUrl + "sendMessage/send");
        String xGiveuAppKey = "sendmsg";
        String xGiveuTimestamp = String.valueOf(new Date().getTime());
        String type = "2";
        String id = "253C221D7EE04BB5ADF9BC2375B7FD43";
        // String msg = "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href='http://work.weixin.qq.com'>邮件中心视频实况</a>，聪明避开排队。";
        String msg = "任务预警通知\n8月6日\n类别：接口预警\n服务器地址：10.0.10.0\n系统名称：fdsafdasf\n预警信息：接口错误\n<a href='http://work.weixin.qq.com'>详情</a>";
        try {
            msg = java.net.URLEncoder.encode(msg, "utf-8");
        } catch (Exception e) {
            return;
        }
        httpRequest.header("xGiveuAppKey", xGiveuAppKey);
        httpRequest.header("xGiveuTimestamp", xGiveuTimestamp);
        httpRequest.header("Content-type", "application/json; charset=utf-8");
        httpRequest.header("Accept", "application/json");


        HttpMultiMap multiMap = httpRequest.query();
        multiMap.add("id", id);
        multiMap.add("type", type);
        multiMap.add("msg", msg);

        StringBuilder sb = new StringBuilder();
        sb.append("xGiveuAppKey").append(xGiveuAppKey);
        sb.append("xGiveuTimestamp").append(xGiveuTimestamp);
        sb.append("id").append(id);
        sb.append("type").append(type);
        sb.append("msg").append(msg);
        sb.append(appSecret);

        String original = sb.toString();
        String encrypted = MD5Util.sign(original);
        httpRequest.header("xGiveuSign", encrypted);
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse response = httpRequest.send();
        //这句话的意思，是让浏览器用utf8来解析返回的数据
        response.contentType("Content-type", "text/html;charset=UTF-8");
        //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
        response.charset("UTF-8");
        System.out.println(response);
    }

    public static void sendInterfaceMessage() {
        HttpRequest httpRequest = HttpRequest.post(sendMessageUrl + "sendMessage/send");
        String xGiveuAppKey = "sendmsg";
        String xGiveuTimestamp = String.valueOf(new Date().getTime());
        String type = "3";
        String id = "10.10.10.10:80";
        // String msg = "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href='http://work.weixin.qq.com'>邮件中心视频实况</a>，聪明避开排队。";
        String msg = "接口预警通知\n8月6日\n类别：接口预警\n服务器地址：10.0.10.0\n系统名称：fdsafdasf\n预警信息：接口错误\n<a href='http://work.weixin.qq.com'>详情</a>";
        try {
            msg = java.net.URLEncoder.encode(msg, "utf-8");
        } catch (Exception e) {
            return;
        }
        httpRequest.header("xGiveuAppKey", xGiveuAppKey);
        httpRequest.header("xGiveuTimestamp", xGiveuTimestamp);
        httpRequest.header("Content-type", "application/json; charset=utf-8");
        httpRequest.header("Accept", "application/json");


        HttpMultiMap multiMap = httpRequest.query();
        multiMap.add("id", id);
        multiMap.add("type", type);
        multiMap.add("msg", msg);

        StringBuilder sb = new StringBuilder();
        sb.append("xGiveuAppKey").append(xGiveuAppKey);
        sb.append("xGiveuTimestamp").append(xGiveuTimestamp);
        sb.append("id").append(id);
        sb.append("type").append(type);
        sb.append("msg").append(msg);
        sb.append(appSecret);

        String original = sb.toString();
        String encrypted = MD5Util.sign(original);
        httpRequest.header("xGiveuSign", encrypted);
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse response = httpRequest.send();
        //这句话的意思，是让浏览器用utf8来解析返回的数据
        response.contentType("Content-type", "text/html;charset=UTF-8");
        //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
        response.charset("UTF-8");
        System.out.println(response);
    }
}
