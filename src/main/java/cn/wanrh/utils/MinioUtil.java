package cn.wanrh.utils;

import io.minio.MinioClient;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wanrh@jurassic.com.cn
 * @date 2019/4/15 16:58
 */
public class MinioUtil {

    @Value("${minio.uri}")
    private static String minio_url;

    @Value("${minio.accesskey}")
    private static String minio_name;

    @Value("${minio.serectkey}")
    private static String minio_pass;

    @Value("${minio.bucket}")
    private static String minio_bucketName;


    /**
     *
     * @Title: uploadImage
     * @Description:上传图片
     * @param inputStream
     * @param suffix
     * @return
     * @throws Exception
     */
    public static JSONObject uploadImage(InputStream inputStream, String suffix) throws Exception {
        return upload(inputStream, suffix, "image/jpeg");
    }


    /**
     * @Title: uploadVideo
     * @Description:上传视频
     * @param inputStream
     * @param suffix
     * @return
     * @throws Exception
     */
    public static JSONObject uploadVideo(InputStream inputStream, String suffix) throws Exception {
        return upload(inputStream, suffix, "video/mp4");
    }


    /**
     * @Title: uploadVideo
     * @Description:上传文件
     * @param inputStream
     * @param suffix
     * @return
     * @throws Exception
     */
    public static JSONObject uploadFile(InputStream inputStream, String suffix) throws Exception {
        return upload(inputStream, suffix, "application/octet-stream");
    }


    /**
     * 上传字符串大文本内容
     *
     * @Title: uploadString
     * @Description:描述方法
     * @param str
     * @return
     * @throws Exception
     */
    public static JSONObject uploadString(String str) throws Exception {
        if (!StringUtils.isBlank(str)) {
            return new JSONObject();
        }
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        return upload(inputStream, null, "text/html");
    }


    /**
     * @Title: upload
     * @Description:上传主功能
     * @return
     * @throws Exception
     */
    private static JSONObject upload(InputStream inputStream, String suffix, String contentType) throws Exception {
        JSONObject map = new JSONObject();
        MinioClient minioClient = new MinioClient(minio_url, minio_name, minio_pass);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String ymd = sdf.format(new Date());
        String objectName = ymd + "/" + UUID.UU64() + (suffix != null ? suffix : "");
        minioClient.putObject(minio_bucketName, objectName, inputStream, contentType);
        String url = minioClient.getObjectUrl(minio_bucketName, objectName);
        map.put("flag", "0");
        map.put("mess", "上传成功");
        map.put("url", url);
        map.put("urlval", url);
        map.put("path", minio_bucketName + "/" + objectName);
        return map;
    }

}
