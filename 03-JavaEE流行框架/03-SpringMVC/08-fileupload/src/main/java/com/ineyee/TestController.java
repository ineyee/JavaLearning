package com.ineyee;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class TestController {
    // 假设客户端发送的 post 请求为（字段名要跟客户端约定好）：
    // url = http://localhost:9999/{{applicationContext}}/uploadFile
    // body =
    //     --Boundary+1234567890
    //     Content-Disposition: form-data; name="email"
    //     zhangsan@qq.com
    //     --Boundary+1234567890
    //     Content-Disposition: form-data; name="username"
    //     zhangsan张三
    //     --Boundary+1234567890
    //     Content-Disposition: form-data; name="file"
    //     filedata
    @PostMapping("/uploadFile")
    @ResponseBody
    public String uploadFile(@RequestParam(value = "email", required = true) String email,
                             @RequestParam(value = "username", required = false) String username,
                             @RequestParam(value = "file", required = true) MultipartFile file,
                             HttpServletRequest req) throws IOException {
        // 获取当前项目的 applicationContext
        ServletContext applicationContext = req.getServletContext();
        // 设置文件在服务器上的存储目录，这里存储在当前项目根目录下的 upload/image 文件夹里
        // 一般就是存储在 upload/text、upload/image、upload/audio、upload/video 这类文件夹里
        // 文件夹存在的话就是直接获取
        String dirPath = applicationContext.getRealPath("/upload/image");
        // 不存在的话就创建，确保目录存在
        File dir = new File(dirPath);
        if (!dir.exists()) {
            // 创建目录及其父目录
            boolean dirCreated = dir.mkdirs();
            if (!dirCreated) {
                throw new IOException("Failed to create directory: " + dirPath);
            }
        }
        // 设置文件在服务器上的文件名
        // 不建议用时间戳，因为大量用户可能在同一时间上传文件，文件名会重复
        // 更不建议用客户端传上来的原始文件名，因为用户可能会上传同名文件，文件名会重复
        // 建议使用 UUID 生成唯一的随机字符串来作为文件名
        String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        // 文件路径
        String filePath = dirPath + "/" + fileName;

        // 把文件存储到服务器硬盘上
        file.transferTo(new File(filePath));

        // 这里我们是把文件存储到我们自己的服务器上，所以只需要把文件的相对路径（/upload/image/xxx.png 这种）存储到数据库并返回给客户端就可以了
        // 因为前面的 ip 地址、端口号、applicationContext 我们都可能会修改，所以让客户端拿到相对路径后拼接上前面的部分就能访问到文件了
        //
        // 如果是把文件存储到三方 OSS，那就直接把文件的 URL（http://xxx/xxx/xxx.png 这种） 存储到数据库并返回给客户端就可以了
        String relativePath = "/upload/image/" + fileName;

        return "uploadFile success = " + email + " " + username + " " + relativePath;
    }

    // 假设客户端发送的 post 请求为（字段名要跟客户端约定好）：
    // url = http://localhost:9999/{{applicationContext}}/uploadFiles
    // body =
    //     --Boundary+1234567890
    //     Content-Disposition: form-data; name="email"
    //     zhangsan@qq.com
    //     --Boundary+1234567890
    //     Content-Disposition: form-data; name="username"
    //     zhangsan张三
    //     --Boundary+1234567890
    //     Content-Disposition: form-data; name="files"
    //     filedata1、filedata2
    @PostMapping("/uploadFiles")
    @ResponseBody
    public String uploadFiles(@RequestParam(value = "email", required = true) String email,
                              @RequestParam(value = "username", required = false) String username,
                              @RequestParam(value = "files", required = true) List<MultipartFile> files,
                              HttpServletRequest req) throws IOException {
        // 获取当前项目的 applicationContext
        ServletContext applicationContext = req.getServletContext();
        // 设置文件在服务器上的存储目录，这里存储在当前项目根目录下的 upload/image 文件夹里
        // 一般就是存储在 upload/text、upload/image、upload/audio、upload/video 这类文件夹里
        // 文件夹存在的话就是直接获取
        String dirPath = applicationContext.getRealPath("/upload/image");
        // 不存在的话就创建，确保目录存在
        File dir = new File(dirPath);
        if (!dir.exists()) {
            // 创建目录及其父目录
            boolean dirCreated = dir.mkdirs();
            if (!dirCreated) {
                throw new IOException("Failed to create directory: " + dirPath);
            }
        }

        // 处理所有上传的文件
        List<String> relativePaths = new ArrayList<>();
        for (MultipartFile file : files) {
            // 设置文件在服务器上的文件名
            // 不建议用时间戳，因为大量用户可能在同一时间上传文件，文件名会重复
            // 更不建议用客户端传上来的原始文件名，因为用户可能会上传同名文件，文件名会重复
            // 建议使用 UUID 生成唯一的随机字符串来作为文件名
            String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            // 文件路径
            String filePath = dirPath + "/" + fileName;

            // 把文件存储到服务器硬盘上
            file.transferTo(new File(filePath));

            // 这里我们是把文件存储到我们自己的服务器上，所以只需要把文件的相对路径（/upload/image/xxx.png 这种）存储到数据库并返回给客户端就可以了
            // 因为前面的 ip 地址、端口号、applicationContext 我们都可能会修改，所以让客户端拿到相对路径后拼接上前面的部分就能访问到文件了
            //
            // 如果是把文件存储到三方 OSS，那就直接把文件的 URL（http://xxx/xxx/xxx.png 这种） 存储到数据库并返回给客户端就可以了
            String relativePath = "/upload/image/" + fileName;
            relativePaths.add(relativePath);
        }

        return "uploadFile success = " + email + " " + username + " " + relativePaths;
    }
}
