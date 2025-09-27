package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api (tags = "通用接口")
@Slf4j
public class CommonController {

    private static String FILE_UPLOAD_PATH = "E:\\Cache Files\\sky-take-out\\";
    /**
     * 文件上传
     * @param file
     * @return Result<String> 返回文件上传路径</String>
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("文件上传：{}", file);


        // 创建保存文件的文件夹
        File dir = new File(FILE_UPLOAD_PATH);
        if (!dir.exists() || !dir.isDirectory()) {
            boolean created = dir.mkdirs();
            if (created) {
                log.info("创建文件夹成功: {}", FILE_UPLOAD_PATH);
            } else {
                log.warn("创建文件夹失败或已经存在: {}", FILE_UPLOAD_PATH);
            }
        }
        // 将上传的文件本地存储
        String originalFilename = file.getOriginalFilename();
        //获取文件名后缀
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //构造新文件名称
        String objectName = UUID.randomUUID().toString()+extension;
        // 文件直接保存到本地
        try{
            file.transferTo(new File(FILE_UPLOAD_PATH+objectName));
            log.info("文件上传成功");
        }catch (Exception e){
            log.error("文件上传失败: {}", originalFilename);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }

        String fileURL = "http://localhost:8080/static/"+objectName;

        return Result.success(fileURL);
    }
}
