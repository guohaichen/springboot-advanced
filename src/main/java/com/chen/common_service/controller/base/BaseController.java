package com.chen.common_service.controller.base;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.chen.common_service.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cgh
 * @create 2023-03-31
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class BaseController {

    private final static String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
    private final static String accessKeyId = "LTAI5tC77a34yF6KYtBQ9xjJ";
    private final static String accessKeySecret = "WB5izJZEnQNdWwegRl4etBLOR8QOxO";
    private final static String bucketName = "guohai-test";
    private final static String key = "<downloadKey>";
    private final static String uploadFile = "<uploadFile>";

    @Value(value = "${img.baseSrc}")
    private String uploadPath;

    @PostMapping("/uploadFile")
    //文件上传至服务器
    public Result<?> uploadImg(@RequestParam(value = "file") MultipartFile multipartFile, HttpServletRequest request) {
        //获取文件名
        String path = request.getParameter("path");
        File filePrePath = new File(uploadPath + File.separator + path + File.separator);
        //文件夹不存在，创建文件夹
        if (!filePrePath.exists()) {
            if (!filePrePath.mkdirs()) {
                return Result.error("文件路径出错");
            }
        }
        String filename = multipartFile.getOriginalFilename();
        if (filename != null && filename.contains(".")) {
            //保证文件唯一
            filename = filename.substring(0, filename.lastIndexOf(".")) + "-" + System.currentTimeMillis() + filename.substring(filename.lastIndexOf("."));
        } else {
            filename = filename + "-" + System.currentTimeMillis();
        }
        //保存文件
        File saveFile = new File(filePrePath + File.separator + filename);
        log.info("文件最终路径:{}", saveFile);
        try {
            multipartFile.transferTo(saveFile);
            return Result.OK("上传成功!", path + File.separator + filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 阿里云oss 通过文件流上传 上传方法
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("/oss/upload")
    public Result<?> ossUpload(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        log.info("文件名称: {}", multipartFile.getOriginalFilename());
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //获取文件流
        InputStream inputStream = multipartFile.getInputStream();
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, setOSSFilePath(multipartFile.getOriginalFilename()), inputStream);
            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传文件。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            log.info("文件上传成功{}", result.getRequestId());
            return Result.OK("上传成功");
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return Result.error("上传失败");
    }

    //拼接路径+文件名
    private String setOSSFilePath(String fileName) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = format.format(date);
        // 2023-07-06 ----> 2023/07/06
        String fileDirectory = formatDate.replaceAll("-", "/");

        log.info("文件目录:{},文件名称:{} ", fileDirectory,fileName);
        return fileDirectory + "/" + fileName;
    }
}
