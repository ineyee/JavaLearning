/**
 * 前面 LoginServlet 和 SongListServlet 的做法，是一个接口对应一个 Servlet
 * 但是这样会导致项目里有无穷多个 Servlet 文件，不利于维护
 * <p>
 * 实际开发中一般都是一张表对应一个 Servlet，也就是说关于这张表的增删改查众多接口都会搞到一个 Servlet 里
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
import java.util.Arrays;
import java.util.List;

// 前面我们指定路径的时候，都只指定了一层路径
// 现在我们指定两层路径，并且第二层路径用通配符 *，这就意味着只要接口的第一层路径是 student，第二层不管是啥，都能匹配到这个 Servlet 里来
//
// 并且第二层路径也不能乱写，而只能是我们固定的几个路径，因为我们正是要通过第二层路径来反射出具体调用的是哪个接口对应的方法
// 你可能会说那别人乱传怎么办？没关系，乱传的话就报找不到资源就完事了，毕竟接口是我们后台提供给客户端调用的，这都是公司内部约定好的，所以客户端一般是不会乱传的
//
// 比如这里我们就约定好了 4 个接口：
// /student/insert：新增一个学生
// /student/delete：删除一个学生
// /student/update：修改一个学生
// /student/query：查询一个学生
@WebServlet("/student/*")
public class StudentServlet extends HttpServlet {
    // 定义只允许 GET 请求和 POST 请求的方法
    private static final List<String> GET_METHODS = Arrays.asList("query");
    private static final List<String> POST_METHODS = Arrays.asList("insert", "delete", "update");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodName = getMethodName(req.getRequestURI());

        if (!GET_METHODS.contains(methodName)) {
            System.out.println(methodName + " only supports GET");
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, methodName + " only supports GET");
            return;
        }

        invokeMethod(methodName, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodName = getMethodName(req.getRequestURI());

        if (!POST_METHODS.contains(methodName)) {
            System.out.println(methodName + " only supports POST");
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, methodName + " only supports POST");
            return;
        }

        invokeMethod(methodName, req, resp);
    }

    private String getMethodName(String requestURI) {
        /*
         helloServlet：项目名
         student：第一层路径
         xxx：第二层路径
        */
        System.out.println("requestURI = " + requestURI); // /helloServlet/student/xxx

        // 用 / 分割一下
        List<String> paths = Arrays.asList(requestURI.split("/"));
        System.out.println("paths = " + paths); // [, helloServlet, student, insert]

        // 按我们公司内部的规范来说，第二层路径肯定是 paths 的最后一个元素
        String methodName = paths.getLast();
        System.out.println("methodName = " + methodName); // [, helloServlet, student, insert]

        return methodName;
    }

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
    // 1、这 4 个方法名必须跟第二层路径同名，因为我们正是要通过第二层路径来反射出具体调用的是哪个接口对应的方法
    // 2、这 4 个方法必须定义成 public 才能被上面的 getMethod 方法查找到
    public void insert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("insert 接口：新增一个学生");
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("delete 接口：删除一个学生");
    }

    public void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("update 接口：更新一个学生");
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("query 接口：查询一个学生");
    }
}
