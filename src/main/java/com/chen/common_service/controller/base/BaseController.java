package com.chen.common_service.controller.base;

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

/**
 * @author cgh
 * @create 2023-03-31
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class BaseController {

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
        log.info("filename:{}", filename);
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
            return Result.OK("上传成功!", saveFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
