package servlet;

import bean.UserBean;
import dao.UserDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private final UserDao userDao = new UserDao();

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
            int result = userDao.save(List.of(userBean1, userBean2));
//            int result = userDao.remove(List.of(5));
//            int result = userDao.update(List.of(9), Map.of("age", "19", "height", "1.99"));
            System.out.println(result);

            UserBean readUserBean = userDao.get(10);
            System.out.println(readUserBean);
            List<UserBean> userBeanList = userDao.list(2, 1);
            System.out.println(userBeanList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
