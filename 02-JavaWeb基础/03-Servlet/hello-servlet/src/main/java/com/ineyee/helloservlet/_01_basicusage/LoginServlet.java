/**
 * 这个 Servlet 演示基本使用
 */

package com.ineyee.helloservlet._01_basicusage;

// 第一步：导入 servlet 相关的包

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// 第二步：创建一个 servlet 类，继承自 HttpServlet
// 约定俗成地我们会把这个类实例命名为 XxxServlet
// 第三步：指定请求路径 path，注意 / 不能少
// 将来客户端发送请求 http://ip:port/applicationContext/path 就能找到这个 servlet
@WebServlet("/login01")
public class LoginServlet extends HttpServlet {
    /**
     * 第四步：监听来自客户端的请求并返回响应
     * 调用的方法：如果是 get 请求那就会触发 doGet 方法，如果是 post 请求那就会触发 doPost 方法
     *
     * @param req  请求对象，用来读取客户端发过来的请求数据。包含了客户端请求服务器时的所有信息，如请求方法、请求路径、请求头、请求体等
     * @param resp 响应对象，用来写入响应数据给客户端。包含了服务器要返回给客户端的所有信息，如响应状态码、响应头、响应体等
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 返回响应、结束本次请求
        resp.getWriter().write("登录成功");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 返回响应、结束本次请求
        resp.getWriter().write("登录成功");
    }


    /// 补充：Servlet 的生命周期
    ///
    /// Servlet 的生命周期是指 Servlet 从创建、初始化、处理请求、直到销毁的整个过程
    /// 它由 Servlet 容器（如 Tomcat） 管理，开发者只需要实现相关方法
    ///
    /// Servlet 容器在第一次请求 Servlet 时（即客户端第一次请求该 Servlet 里的接口时）才会调用 Servlet() 构造方法来创建 Servlet 对象，也就是说是懒加载的

    /// 调用 Servlet() 构造方法创建好 Servlet 对象后，会紧接着调用 init() 方法来初始化 Servlet
    /// init() 方法只会执行一次，一般用来初始化资源（如数据库连接、配置文件加载等）
    @Override
    public void init() throws ServletException {
        System.out.println("init() 被调用");
    }

    /// 每次有请求进来时，容器都会调用 service() 方法，由它分发到 doGet()、doPost() 等方法
    /// service() 方法会调用多次
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        System.out.println("service() 被调用");
    }

    /// 当容器关闭或卸载 Servlet 时，就会调用 destroy() 方法
    /// destroy() 方法只会执行一次，一般用来释放资源
    @Override
    public void destroy() {
        System.out.println("destroy() 被调用");
    }
}

// 第五步：点击 Run 或 Debug 启动 Tomcat，Tomcat 就会把我们的 JavaWeb 项目给自动部署好
// 我们已经设置了 Tomcat 监听 9999 端口，启动 Tomcat 时会启动一个本地服务器
//
// 打开 Postman，访问 http://localhost:9999/helloServlet/login01 就可以了
// 注意：每次修改了服务器的代码后，都需要重新部署甚至重启服务器，否则服务器不会自动更新
