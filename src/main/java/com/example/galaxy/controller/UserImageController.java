package com.example.galaxy.controller;

import com.example.galaxy.VO.ResultVO;
import com.example.galaxy.common.utils.ResultVOUtils;
import com.example.galaxy.entity.SysUser;
import com.example.galaxy.entity.SysUserDTO;
import com.example.galaxy.service.inter.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;


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
    public <T>ResultVO<T> uploadUserImage(@RequestBody SysUserDTO sysUserDTO, @RequestParam("file") MultipartFile file) throws IOException {
        SysUser sysUser = new SysUser(sysUserDTO);
        // 验证文件是否为空
        if (file.isEmpty()) {
            return ResultVOUtils.failed("文件不能为空");
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
        // 检测同名文件并添加时间戳
        File destination = new File(IMAGE_PATH, originalFilename);
        if (destination.exists()) {
            String fileNameWithoutExt = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String timestamp = String.valueOf(System.currentTimeMillis());
            destination = new File(IMAGE_PATH, fileNameWithoutExt + "_" + timestamp + fileExtension);
        }
        // 保存文件
        file.transferTo(destination);
        if (!destination.exists()) {
            return ResultVOUtils.failed("文件保存失败");
        }
        // 更新用户头像路径
        sysUser.setUserAvatarUrl(destination.getAbsoluteFile().getName());
        iSysUserService.updateUserByAccount(sysUser);
        return ResultVOUtils.success();
    }

    /**
     * 查看用户头像
     * @param filename 前端需要的文件名
     *
     */
    @GetMapping("/view/{filename}")
    public void viewUserImage(@PathVariable String filename, HttpServletResponse response) throws IOException {
        // 验证文件名，防止目录遍历攻击
        if (filename.contains("..")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "非法的文件名");
            return;
        }
        File file = new File(IMAGE_PATH, filename);
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "文件不存在");
            return;
        }
        try {
            // 设置响应头
            response.setContentType(Files.probeContentType(file.toPath()));
            // 将文件内容写入响应输出流
            Files.copy(file.toPath(), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "文件读取失败");
        }
    }

    @GetMapping("/download/{filename}")
    public void downloadUserImage(@PathVariable String filename, HttpServletResponse response) {
        // 验证文件名，防止目录遍历攻击
        if (filename.contains("..")) {
            throw new IllegalArgumentException("非法的文件名");
        }
        File file = new File(IMAGE_PATH, filename);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        try {
            // 设置响应头
            response.setContentType(Files.probeContentType(file.toPath()));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            // 将文件写入响应输出流
            Files.copy(file.toPath(), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }

}