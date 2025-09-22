package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    // 当前 Servlet 支持的 GET 方法，即接口的第二层路径名
    @Override
    protected List<String> supportedGetMethods() {
        return List.of("query");
    }

    // 当前 Servlet 支持的 POST 方法，即接口的第二层路径名
    @Override
    protected List<String> supportedPostMethods() {
        return List.of("insert", "delete", "update");
    }

    // 注意：
    // 1、这几个方法名必须跟接口的第二层路径同名，因为我们正是要通过第二层路径来反射出接口对应的方法
    // 2、这几个方法必须定义成 public 才能被上面的 getClass().getMethod(xxx) 方法查找到
    public void insert(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("新增一个用户成功");
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("删除一个用户成功");
    }

    public void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("更新一个用户成功");
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("查询一个用户成功");
    }
}
