/**
 * 这个 Servlet 演示单文件上传
 * 我们一般使用 commons-fileupload 这个三方库来实现文件上传
 */

package com.ineyee.helloservlet._08_fileupload;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/upload08")
// 必须加这个注解，否则 Servlet 不会接收 multipart/form-data
@MultipartConfig
public class UploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // 磁盘文件工厂：用于创建 DiskFileItem 对象，管理内存存储，防止大文件撑爆内存
            DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
            // 上传解析器：负责解析 HttpServletRequest
            JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> upload =
                    new JakartaServletFileUpload<>(factory);
            // 解析 HttpServletRequest，得到请求里的普通参数和文件参数，一个 DiskFileItem 对象就是一个请求参数
            List<DiskFileItem> items = upload.parseRequest(req);

            String userId = null;
            DiskFileItem file = null;

            // 遍历请求参数
            for (DiskFileItem item : items) {
                // 普通参数并且参数名是 userId（字段名要跟客户端约定好）
                if (item.isFormField() && "userId".equals(item.getFieldName())) {
                    // 参数值
                    userId = item.getString(StandardCharsets.UTF_8);
                }

                // 文件参数并且参数名是 file（字段名要跟客户端约定好）
                if (!item.isFormField() && "file".equals(item.getFieldName())) {
                    // 文件对象
                    file = item;
                }
            }

            if (userId != null && file != null) {
                // 获取当前项目的 applicationContext
                ServletContext applicationContext = req.getServletContext();
                // 设置文件在服务器上的存储目录，这里存储在当前项目根目录下的 upload/image 文件夹里
                // 一般就是存储在 upload/text、upload/image、upload/audio、upload/video 这类文件夹里
                // 文件夹存在的话就是直接获取，不存在的话就会创建
                String dirPath = applicationContext.getRealPath("/upload/image");
                // 设置文件在服务器上的文件名
                // 不建议用时间戳，因为大量用户可能在同一时间上传文件，文件名会重复
                // 更不建议用客户端传上来的原始文件名，因为用户可能会上传同名文件，文件名会重复
                // 建议使用 UUID 生成唯一的随机字符串来作为文件名
                String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getName());
                // 文件路径
                String filePath = dirPath + "/" + fileName;

                // 把文件存储到服务器硬盘上
                FileUtils.copyInputStreamToFile(file.getInputStream(), new File(filePath));

                // 这里我们是把文件存储到我们自己的服务器上，所以只需要把文件的相对路径（/upload/image/xxx.png 这种）存储到数据库并返回给客户端就可以了
                // 因为前面的 ip 地址、端口号、applicationContext 我们都可能会修改，所以让客户端拿到相对路径后拼接上前面的部分就能访问到文件了
                //
                // 如果是把文件存储到三方 OSS，那就直接把文件的 URL（http://xxx/xxx/xxx.png 这种） 存储到数据库并返回给客户端就可以了
                String relativePath = "/upload/image/" + fileName;

                // 这里假设是在根据 userId 把文件的相对路径存储到数据库
                // ......
                // 然后给客户端返回响应
                Map<String, Object> data = new HashMap<>();
                data.put("userId", userId);
                data.put("relativePath", relativePath);
                Map<String, Object> result = new HashMap<>();
                result.put("code", 0);
                result.put("message", "请求成功");
                result.put("data", data);
                resp.getWriter().write(new Gson().toJson(result));
            } else {
                resp.getWriter().write("{\"code\":-1,\"message\":\"请求参数错误\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
