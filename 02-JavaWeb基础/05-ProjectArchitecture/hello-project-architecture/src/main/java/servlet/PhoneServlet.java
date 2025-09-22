package servlet;

import bean.PhoneBean;
import com.google.gson.Gson;
import dao.PhoneDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/phone/*")
public class PhoneServlet extends BaseServlet {
    private final PhoneDao phoneDao = new PhoneDao();

    // 当前 Servlet 支持的 GET 方法，即接口的第二层路径名
    @Override
    protected List<String> supportedGetMethods() {
        return List.of("query", "list");
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
        resp.getWriter().write("新增一个手机成功");
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("删除一个手机成功");
    }

    public void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("更新一个手机成功");
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("查询一个手机成功");
    }

    public void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("查询手机列表成功");

        // 获取数据
        List<PhoneBean> phoneBeanlist = null;
        try {
            phoneBeanlist = phoneDao.getPhoneList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 处理数据
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("code", 0);
        responseMap.put("message", "success");
        responseMap.put("data", phoneBeanlist);

        Gson gson = new Gson();
        String responseJson = gson.toJson(responseMap);

        // 给客户端返回响应
        // 告诉客户端响应体的格式为 json，编码方式为 utf-8
        resp.setContentType("application/json;charset=utf-8");
        // 写数据
        resp.getWriter().write(responseJson);
    }
}
