package com.example.galaxy.controller;

import com.example.galaxy.VO.ResultVO;
import com.example.galaxy.common.utils.ResultVOUtils;
import com.example.galaxy.entity.SysUser;
import com.example.galaxy.entity.SysUserDTO;
import com.example.galaxy.service.inter.ISysUserService;
import jdk.internal.dynalink.beans.StaticClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/user/image")
public class UserImageController {
    private static final String IMAGE_PATH = "src/main/resources/userImages";
    private final ISysUserService iSysUserService;
    @Autowired
    public UserImageController(ISysUserService iSysUserService) {
        // 初始化目录
        this.iSysUserService = iSysUserService;
        File dir = new File(IMAGE_PATH);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("无法创建上传目录: " + IMAGE_PATH);
        }
    }

    @PostMapping("/upload")
    public <T>ResultVO<T> uploadUserImage(@RequestBody SysUserDTO sysUserDTO,@RequestParam("file") MultipartFile file) throws IOException {
        // 验证文件是否为空
        if (file.isEmpty()) {
            return  ResultVOUtils.failed("文件不能为空");
        }
        // 验证文件名，防止目录遍历攻击
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..")) {
            return ResultVOUtils.failed("非法的文件名");
        }
        // 验证文件类型（示例：仅允许图片类型）
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResultVOUtils.failed("仅支持图片文件");
        }
        // 保存文件
        File destination = new File(IMAGE_PATH, originalFilename);
        file.transferTo(destination);
        //iSysUserService.updateUserByAccount()
        return ResultVOUtils.success();
    }

}
