/**
 * 前面 LoginServlet 和 SongListServlet 的做法，是一个接口对应一个 Servlet
 * 但是这样会导致项目里有无穷多个 Servlet 文件，不利于维护
 * <p>
 * 实际开发中一般都是一张表对应一个 Servlet，也就是说关于这张表的增删改查众多接口都会搞到一个 Servlet 里
 * <p>
 * 这个 Servlet 演示怎么把众多接口都搞到一个 Servlet 里
 */

package com.ineyee.helloservlet._02_requestpathmethod;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 比如这里我们要写 4 个接口：
// http://localhost:9999/helloServlet/student/insert（新增一个学生）
// http://localhost:9999/helloServlet/student/delete（删除一个学生）
// http://localhost:9999/helloServlet/student/update（修改一个学生）
// http://localhost:9999/helloServlet/student/query（查询一个学生）
//
// 创建一个 StudentServlet 类，student 表就对应这个 servlet，也就是说关于 student 表的增删改查众多接口都会搞到这个 servlet 里
// 如果是前面的做法，我们就需要创建 StudentInsertServlet、StudentDeleteServlet、StudentUpdateServlet、StudentQueryServlet，每个 servlet 里只写一个接口
//
// 前面我们指定路径的时候，都只指定了一层路径
// 现在我们指定两层路径，并且第二层路径用通配符 *，这就意味着只要接口的第一层路径是 student，第二层不管是啥，都能匹配到这个 servlet 里来
// 并且第二层路径也不能乱写，而只能是我们接口里固定的那几个路径，因为我们正是要通过第二层路径来反射出接口对应的方法
// 你可能会说那别人乱传怎么办？没关系，乱传的话就报找不到资源就完事了，毕竟接口是我们后台提供给客户端调用的，这都是公司内部约定好的，所以客户端一般是不会乱传的
@WebServlet("/student/*")
public class StudentServlet extends HttpServlet {
    // 定义只允许 GET 请求的方法
    private static final List<String> GET_METHODS = Arrays.asList("query");
    // 定义只允许 POST 请求的方法
    private static final List<String> POST_METHODS = Arrays.asList("insert", "delete", "update");
    // 全部方法
    private static final List<String> ALL_METHODS = new ArrayList<>();

    static {
        ALL_METHODS.addAll(GET_METHODS);
        ALL_METHODS.addAll(POST_METHODS);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle("GET", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle("POST", req, resp);
    }

    /**
     * 处理 doGet 或 doPost
     *
     * @param method 请求方法 GET/POST
     * @param req    请求对象
     * @param resp   响应对象
     */
    private void handle(String method, HttpServletRequest req, HttpServletResponse resp) {
        // 从请求URI中提取方法名
        String methodName = getMethodName(req.getRequestURI());
        // 检查方法是否合法并进行权限验证
        Boolean ret = checkMethod(methodName, method, req, resp);
        if (ret) {
            // 调用对应的方法处理请求
            invokeMethod(methodName, req, resp);
        }
    }


    /**
     * 根据第二层路径反射出方法名
     *
     * @param requestURI 请求路径
     * @return 方法名
     */
    private String getMethodName(String requestURI) {
        /*
         helloServlet：Application Context
         student：第一层路径
         xxx：第二层路径
        */
        System.out.println("requestURI = " + requestURI); // /helloServlet/student/xxx

        // 用 / 分割一下
        List<String> paths = Arrays.asList(requestURI.split("/"));
        System.out.println("paths = " + paths); // [, helloServlet, student, insert]

        // 按照我们公司内部的规范，第二层路径肯定是 paths 的最后一个元素
        String methodName = paths.getLast();
        System.out.println("methodName = " + methodName); // insert

        return methodName;
    }

    /**
     * 检查方法是否有效
     *
     * @param methodName 方法名
     * @param method     请求方法 GET/POST
     * @param req        请求对象
     * @param resp       响应对象
     * @return 是否有效
     */
    private Boolean checkMethod(String methodName, String method, HttpServletRequest req, HttpServletResponse resp) {
        try {
            // 第二层路径乱写了，没匹配上，报找不到资源
            if (!ALL_METHODS.contains(methodName)) {
                System.out.println(methodName + " not found");
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, methodName + " not found");
                return false;
            }

            // 该用 GET 请求，却用了 POST 请求，报方法不允许
            if (method.equals("GET") || method.equals("get")) {
                if (!GET_METHODS.contains(methodName)) {
                    System.out.println(methodName + " only supports GET");
                    resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, methodName + " only supports GET");
                    return false;
                }
            }

            // 该用 POST 请求，却用了 GET 请求，报方法不允许
            if (method.equals("POST") || method.equals("post")) {
                if (!POST_METHODS.contains(methodName)) {
                    System.out.println(methodName + " only supports POST");
                    resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, methodName + " only supports POST");
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 调用方法
     *
     * @param methodName 方法名
     * @param req        请求对象
     * @param resp       响应对象
     */
    private void invokeMethod(String methodName, HttpServletRequest req, HttpServletResponse resp) {
        try {
            // 用方法名获取到对应的方法
            // 第一个参数：方法名
            // 第二个参数：可变参数列表的类型，这里把 req 和 resp 都传递下去，以便具体的接口方法使用
            Method method = StudentServlet.class.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            // 调用这个接口对应方法
            // 第一个参数：谁调用这个方法（即下面那 4 个方法），那肯定是当前对象 this
            // 第二个参数：可变参数列表，这个方法（即下面那 4 个方法）要接收什么参数
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 注意：
    // 1、这 4 个方法名必须跟接口的第二层路径同名，因为我们正是要通过第二层路径来反射出接口对应的方法
    // 2、这 4 个方法必须定义成 public 才能被上面的 getMethod 方法查找到
    public void insert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("新增一个学生成功");
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("删除一个学生成功");
    }

    public void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("更新一个学生成功");
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("查询一个学生成功");
    }
}
