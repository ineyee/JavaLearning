package servlet;

import bean.UserBean;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserService;

import java.util.List;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private final UserService userService = new UserService();

    // 当前 Servlet 支持的 GET 方法，即接口的第二层路径名
    @Override
    protected List<String> supportedGetMethods() {
        return List.of("query");
    }

    // 当前 Servlet 支持的 POST 方法，即接口的第二层路径名
    @Override
    protected List<String> supportedPostMethods() {
        return List.of("saveUser");
    }

    // 注意：
    // 1、这几个方法名必须跟接口的第二层路径同名，因为我们正是要通过第二层路径来反射出接口对应的方法
    // 2、这几个方法必须定义成 public 才能被上面的 getClass().getMethod(xxx) 方法查找到
    public void saveUser(HttpServletRequest req, HttpServletResponse resp) {
        UserBean userBean = new UserBean();
        userBean.setName("张三");
        userBean.setAge(18);
        userBean.setHeight(1.88);
        userBean.setEmail("zhangsan@qq.com");

        UserBean userBean1 = new UserBean();
        userBean1.setName("李四");
        userBean1.setAge(19);
        userBean1.setHeight(1.99);
        userBean1.setEmail("lisi@qq.com");

        UserBean userBean2 = new UserBean();
        userBean2.setName("王五");
        userBean2.setAge(20);
        userBean2.setHeight(2.00);
        userBean2.setEmail("wangwu@qq.com");

        try {
//            Boolean result = userService.save(List.of(userBean, userBean1, userBean2));
//            Boolean result = userService.remove(List.of(19, 20));
//            Boolean result = userService.update(List.of(22), Map.of("email", "lisi1@qq.com"));
//            System.out.println(result);

            UserBean readUserBean = userService.get(21);
            System.out.println(readUserBean);
            List<UserBean> userBeanList = userService.list(2, 2);
            System.out.println(userBeanList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
