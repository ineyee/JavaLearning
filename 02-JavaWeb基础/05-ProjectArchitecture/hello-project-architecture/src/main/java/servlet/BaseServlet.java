package servlet;

import com.google.gson.Gson;
import constant.response.CommonResponse;
import exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

// 顶级类只能用 public 或 package-private 修饰
//     * public：在项目里的任何地方都能访问
//     * package-private（默认）：在当前包里 + 仅当前包里的子类能访问、子类如果在当前包外是不能访问的
// 这里的 BaseServlet 肯定不限于仅当前包里的子类能访问，所以用 public 修饰
//
// BaseServlet 的使命只是为了抽取所有子类 Servlet 的公共方法，没有实例化的必要，所以定义为抽象类，也无需提供构造方法
//
// BaseServlet 里就不用写 @WebServlet(xxx) 注解了，因为它不负责匹配任何具体的请求，只是负责抽取公共的“接口和方法映射”逻辑
public abstract class BaseServlet extends HttpServlet {
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
        System.out.println("requestURI = " + requestURI); // /${Application Context}/${第一层路径}/${第二层路径}

        List<String> paths = Arrays.asList(requestURI.split("/")); // 用 / 分割一下
        System.out.println("paths = " + paths); // [, ${Application Context}, ${第一层路径}, ${第二层路径}]


        String methodName = paths.get(paths.size() - 1); // 按照我们公司内部的规范，第二层路径肯定是 paths 的最后一个元素
        System.out.println("methodName = " + methodName); // ${第二层路径}]

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
            // 如果第二层路径乱写了，没匹配上，报找不到资源
            if (!supportedAllMethods().contains(methodName)) {
                System.out.println(methodName + " not found");
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, methodName + " not found");
                return false;
            }

            // 如果该用 GET 请求，却用了 POST 请求，报方法不允许
            if (method.equals("GET") || method.equals("get")) {
                if (!supportedGetMethods().contains(methodName)) {
                    System.out.println(methodName + " only supports GET");
                    resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, methodName + " only supports GET");
                    return false;
                }
            }

            // 如果该用 POST 请求，却用了 GET 请求，报方法不允许
            if (method.equals("POST") || method.equals("post")) {
                if (!supportedPostMethods().contains(methodName)) {
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
            Method method = getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            // 调用这个接口对应方法
            // 第一个参数：谁调用这个方法（即下面那 4 个方法），那肯定是当前对象 this
            // 第二个参数：可变参数列表，这个方法（即下面那 4 个方法）要接收什么参数
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 某个子 Servlet 支持的 GET 方法，让子 Servlet 自己去维护
     *
     * @return GET 方法列表
     */
    protected abstract List<String> supportedGetMethods();

    /**
     * 某个子 Servlet 支持的 POST 方法，让子 Servlet 自己去维护
     *
     * @return POST 方法列表
     */
    protected abstract List<String> supportedPostMethods();

    /**
     * 当前项目支持的所有方法
     *
     * @return 所有方法列表
     */
    private List<String> supportedAllMethods() {
        List<String> allMethods = new ArrayList<>();
        allMethods.addAll(supportedGetMethods());
        allMethods.addAll(supportedPostMethods());
        return allMethods;
    }

    /**
     * 给客户端响应数据
     *
     * @param resp 响应对象
     * @param data 数据
     */
    protected void responseData(HttpServletResponse resp, Object data) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("code", CommonResponse.SUCCESS.getCode());
        responseMap.put("message", CommonResponse.SUCCESS.getMessage());
        responseMap.put("data", data);
        try {
            resp.getWriter().write(new Gson().toJson(responseMap));
        } catch (Exception e) {
            // 这里是捕获 getWriter() 方法抛出的 Exception，都没法写了所以此时就不应该再给客户端继续返回响应了、避免循环 try-catch，但是我们可以记录下日志
            e.printStackTrace();
        }
    }

    /**
     * 给客户端响应错误
     *
     * @param resp      响应对象
     * @param exception 异常对象
     */
    protected void responseError(HttpServletResponse resp, Exception exception) {
        Map<String, Object> responseMap = new HashMap<>();
        if (exception instanceof ServiceException) {
            // 这里是业务异常 ServiceException，业务异常各有各的错误码和错误信息，用户在客户端看到错误信息是有提示意义的
            ServiceException serviceException = (ServiceException) exception;
            responseMap.put("code", serviceException.getCode());
            responseMap.put("message", serviceException.getMessage());
        } else {
            // 这里是系统异常 Exception，系统异常是没有 code 的，而且系统异常五花八门、我们也不知道它们什么时候会抛出、抛出的错误信息对用户来说也没有提示意义
            // 所以我们把系统异常的错误码统一为 -100000，错误信息统一为请求失败
            responseMap.put("code", CommonResponse.REQUEST_ERROR.getCode());
            responseMap.put("message", CommonResponse.REQUEST_ERROR.getMessage());
        }

        try {
            resp.getWriter().write(new Gson().toJson(responseMap));
        } catch (Exception e) {
            // 这里是捕获 getWriter() 方法抛出的 Exception，都没法写了所以此时就不应该再给客户端继续返回响应了、避免循环 try-catch，但是我们可以记录下日志
            e.printStackTrace();
        }
    }
}
