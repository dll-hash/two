//package com.aaa.group_three.config;
//
//import com.aliyun.oss.ClientException;
//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClientBuilder;
//import com.aliyun.oss.OSSException;
//
//import java.io.ByteArrayInputStream;
//public class Oss {
//        public static void main(String[] args) throws Exception {
//            // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
//            String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
//            // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//            String accessKeyId = "LTAI5t6fNga6X46jqmz3F86t";
//            String accessKeySecret = "iI8eWZ3FNOn1ExtL4WZautPm8OY8Jo";
//            // 填写Bucket名称，例如examplebucket。
//            String bucketName = "two_project";
//            // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
//            String objectName = "two_project/nav_bar/1.jpg";
//
//            // 创建OSSClient实例。
//            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//            try {
//                String content = "Hello OSS";
//                ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));
//            } catch (OSSException oe) {
//                System.out.println("Caught an OSSException, which means your request made it to OSS, "
//                        + "but was rejected with an error response for some reason.");
//                System.out.println("Error Message:" + oe.getErrorMessage());
//                System.out.println("Error Code:" + oe.getErrorCode());
//                System.out.println("Request ID:" + oe.getRequestId());
//                System.out.println("Host ID:" + oe.getHostId());
//            } catch (ClientException ce) {
//                System.out.println("Caught an ClientException, which means the client encountered "
//                        + "a serious internal problem while trying to communicate with OSS, "
//                        + "such as not being able to access the network.");
//                System.out.println("Error Message:" + ce.getMessage());
//            } finally {
//                if (ossClient != null) {
//                    ossClient.shutdown();
//                }
//            }
//        }
//
//}
