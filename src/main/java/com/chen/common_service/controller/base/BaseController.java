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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final static String accessKeyId = "LTAI5tJmphKDCS4gB1o1vAM9";
    private final static String accessKeySecret = "OckTkiQZPpVIuC0506fxOhqYUsApTQ";
    private final static String bucketName = "guohai-test";
    private final static String key = "<downloadKey>";
    private final static String uploadFile = "<uploadFile>";
    @Value(value = "${img.baseSrc}")
    private String uploadPath;

    //分片文件临时存储目录
    public final Path uploadFileTempPath = Paths.get("F:\\gitHub\\blog\\temp\\");

    public BaseController() {
    }

    @PostMapping("/uploadFile")
    //文件上传至服务器
    public Result<?> uploadImg(@RequestParam(value = "file") MultipartFile multipartFile, HttpServletRequest request) {
        //获取文件名
        if (request.getParameter("path") == null) {
            return Result.error("上传路径不能为空");
        }
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
            return Result.error("上传失败");
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            return Result.error("上传失败");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * @param file         文件
     * @param filename     文件名
     * @param contentRange 文件字符
     *                     分片保存多个文件,这里注意因为涉及到文件的合并，需要将不同的符合条件的文件合并起来，这里一个简单做法就是根据
     *                     唯一的文件名称来创建 一个文件夹，然后 将分片文件 上传至 这个唯一的文件夹，遍历文件夹，进行合并；
     */
    @PostMapping("/bigFile")
    public Result<?> bigFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("filename") String filename,
                                   @RequestHeader("Content-Range") String contentRange) {
        try {
            log.info("filename: {}", filename);
            //根据唯一文件，创建唯一文件夹，将文件都存放在次唯一文件夹中
            final Path uploadFireDirectory = getUniqueDirectory(filename);
            //创建文件夹
            if (!Files.exists(uploadFireDirectory)) {
                Files.createDirectories(uploadFireDirectory);
            }
            //解析content-range头部，获取分片的起始结束字节位置
            String[] rangeParts = contentRange.split("[ -/]");
            long startByte = Long.parseLong(rangeParts[1]);

            //分片文件上传临时目录，以fileName和起始字节位置作为文件名,先将文件前后缀分开
            String filenamePrefix = filename.substring(0, filename.lastIndexOf("."));
            String fileSuffix = filename.substring(filename.lastIndexOf(".") + 1);

            Path tempFilePath = uploadFireDirectory.resolve(filenamePrefix + "-" + startByte + "." + fileSuffix);
            Files.write(tempFilePath, file.getBytes());
            return Result.OK("分片上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("分片上传失败");
        }
    }

    //根据文件名，创建唯一目录 Date.now-文件名
    private Path getUniqueDirectory(String filename) {
        String uniqueDirectory = filename.substring(0, filename.lastIndexOf("."));
        return Paths.get(uploadPath + File.separator + uniqueDirectory);
    }

    /**
     * 合并分片文件
     *
     * @return
     */
    @PostMapping("/bigFile/merge")
    public Result<?> mergeFile(@RequestParam("filename") String filename) {
        log.info("merge:filename:{}", filename);

        Path uniqueDirectory = getUniqueDirectory(filename);
        System.out.println(uniqueDirectory);
        return Result.OK();
    }

    //拼接路径+文件名
    private String setOSSFilePath(String fileName) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = format.format(date);
        // 2023-07-06 ----> 2023/07/06
        String fileDirectory = formatDate.replaceAll("-", "/");

        log.info("文件目录:{},文件名称:{} ", fileDirectory, fileName);
        return fileDirectory + "/" + fileName;
    }

}
