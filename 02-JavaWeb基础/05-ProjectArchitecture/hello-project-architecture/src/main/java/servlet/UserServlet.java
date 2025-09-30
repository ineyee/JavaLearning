package servlet;

import bean.UserBean;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserService;

import java.util.List;
import java.util.Map;

// 用户模块接口的表现层之控制器层
//
// 控制器层（controller）的职责就是直接与客户端打交道，即：
//   * 接收客户端的请求参数，对客户端的请求参数进行基础有效性校验（在表现层进行请求参数的基础有效性校验是为了在请求进入业务逻辑前就拒绝非法格式，避免无效数据渗透到下层，主要包含：必填字段校验、字段长度校验、字段格式校验等，其它跟业务相关的校验就交给业务层去校验）
//   * 调用业务层的 API 拿到业务型数据并转换成客户端直接能用的 JSON、HTML 等数据，给客户端返回响应
//
// 换句话说：
//   * 对下，表现层要对业务层负责，负责的体现就是要做好请求参数的基础有效性校验，确保传递给业务层的参数是人家直接能用的，业务层只需要在此基础上附加一层业务规则校验就可以了
//   * 对上，表现层要对客户端负责，负责的体现就是要做好数据转换，确保返回给客户端的数据是人家直接能用的 JSON、HTML 等数据或错误信息
//   * 其它的表现层就不用关心了
//
// 实践经验：
//   * 接口到底是请求成功还是请求失败，统一在表现层处理，失败了就给客户端响应错误，成功了就给客户端响应数据
//   * 基础有效性校验出错时，需要直接给客户端响应错误
//   * 调用业务层的 API 时一定要用 try-catch，因为数据层和业务层的异常它们都没处理、都是继续上抛到表现层来统一处理的，catch 到异常时就给客户端响应错误，没有错误时就给客户端响应数据
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
