package servlet;

import bean.UserBean;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import constant.response.CommonResponse;
import constant.response.UserResponse;
import exception.ServiceException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserService;
import service.UserServiceImpl;

import java.io.BufferedReader;
import java.util.ArrayList;
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
    private final UserService userService = new UserServiceImpl();

    // 当前 Servlet 支持的 GET 方法，即接口的第二层路径名
    @Override
    protected List<String> supportedGetMethods() {
        return List.of("get", "list", "listPageHelper");
    }

    // 当前 Servlet 支持的 POST 方法，即接口的第二层路径名
    @Override
    protected List<String> supportedPostMethods() {
        return List.of("save", "saveBatch", "remove", "removeBatch", "update", "updateBatch");
    }

    // 注意：
    // 1、这几个方法名必须跟接口的第二层路径同名，因为我们正是要通过第二层路径来反射出接口对应的方法
    // 2、这几个方法必须定义成 public 才能被上面的 getClass().getMethod(xxx) 方法查找到

    /*
     url = http://localhost:9999/helloMyBatis/user/get?id=21
     */
    public void get(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Integer id = Integer.valueOf(req.getParameter("id"));
            System.out.println("请求参数：" + id);

            // 调用业务层 API
            UserBean userBean = userService.get(id);
            if (userBean != null) {
                responseData(resp, userBean);
            } else {
                responseError(resp, new ServiceException(UserResponse.USER_NOT_EXIST.getCode(), UserResponse.USER_NOT_EXIST.getMessage()));
            }
        } catch (Exception e) {
            responseError(resp, e);
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/list?pageSize=3&pageNum=2
     */
    public void list(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Integer pageSize = Integer.valueOf(req.getParameter("pageSize"));
            Integer pageNum = Integer.valueOf(req.getParameter("pageNum"));
            System.out.println("请求参数：" + pageSize + " " + pageNum);

            // 调用业务层 API
            List<UserBean> userBeanList = userService.list(pageSize, pageNum);
            responseData(resp, userBeanList);
        } catch (Exception e) {
            responseError(resp, e);
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/list?pageSize=3&pageNum=2
     */
    public void listPageHelper(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Integer pageSize = Integer.valueOf(req.getParameter("pageSize"));
            Integer pageNum = Integer.valueOf(req.getParameter("pageNum"));
            System.out.println("请求参数：" + pageSize + " " + pageNum);

            // 调用业务层 API
            List<UserBean> userBeanList = userService.listPageHelper(pageSize, pageNum);
            responseData(resp, userBeanList);
        } catch (Exception e) {
            responseError(resp, e);
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/save
     body = {
        "user": {
            "name": "库里",
            "age": 37,
            "height": 1.88,
            "email": "curry@qq.com"
        }
     }
     */
    public void save(HttpServletRequest req, HttpServletResponse resp) {
        // 从请求体里读取 JSON 字符串
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            responseError(resp, e);
            return;
        }
        String body = sb.toString();

        try {
            // 用 JSON 库解析，这里暂时用的是 gson 这个库
            Gson gson = new Gson();

            JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
            System.out.println("请求参数：" + jsonObject);
            // 对请求参数进行基础有效性校验
            if (!jsonObject.has("user") || jsonObject.get("user").isJsonNull()) {
                responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
                return;
            }

            JsonObject userJsonObject = jsonObject.get("user").getAsJsonObject();
            // 对请求参数进行基础有效性校验
            if (!userJsonObject.has("name") || userJsonObject.get("name").isJsonNull()
                    || !userJsonObject.has("age") || userJsonObject.get("age").isJsonNull()
                    || !userJsonObject.has("height") || userJsonObject.get("height").isJsonNull()
                    || !userJsonObject.has("email") || userJsonObject.get("email").isJsonNull()) {
                responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
                return;
            }

            // 创建 UserBean 对象
            UserBean userBean = new UserBean();
            userBean.setName(userJsonObject.get("name").getAsString());
            userBean.setAge(userJsonObject.get("age").getAsInt());
            userBean.setHeight(userJsonObject.get("height").getAsDouble());
            userBean.setEmail(userJsonObject.get("email").getAsString());

            // 调用业务层 API
            UserBean fullUserBean = userService.save(userBean);
            if (fullUserBean != null) {
                responseData(resp, fullUserBean);
            } else {
                responseError(resp, new ServiceException(CommonResponse.REQUEST_ERROR.getCode(), CommonResponse.REQUEST_ERROR.getMessage()));
            }
        } catch (Exception e) {
            responseError(resp, e);
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/saveBatch
     body = {
         "userList": [
             {
                 "name": "三三",
                 "age": 18,
                 "height": 1.88,
                 "email": "sansan@qq.com"
             },
             {
                 "name": "四四",
                 "age": 19,
                 "height": 1.99,
                 "email": "sisi@qq.com"
             },
             {
                 "name": "五五",
                 "age": 20,
                 "height": 2.00,
                 "email": "wuwu@qq.com"
             }
         ]
     }
     */
    public void saveBatch(HttpServletRequest req, HttpServletResponse resp) {
        // 从请求体里读取 JSON 字符串
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            responseError(resp, e);
            return;
        }
        String body = sb.toString();

        try {
            // 用 JSON 库解析，这里暂时用的是 gson 这个库
            Gson gson = new Gson();

            JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
            System.out.println("请求参数：" + jsonObject);
            // 对请求参数进行基础有效性校验
            if (!jsonObject.has("userList") || jsonObject.get("userList").isJsonNull()) {
                responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
                return;
            }

            JsonArray userJsonArray = jsonObject.get("userList").getAsJsonArray();
            // 对请求参数进行基础有效性校验
            if (userJsonArray.isJsonNull()) {
                responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
                return;
            }

            List<UserBean> userBeanList = new ArrayList<>();
            for (JsonElement userJsonElement : userJsonArray) {
                JsonObject userJsonObject = userJsonElement.getAsJsonObject();

                // 对请求参数进行基础有效性校验
                if (!userJsonObject.has("name") || userJsonObject.get("name").isJsonNull()
                        || !userJsonObject.has("age") || userJsonObject.get("age").isJsonNull()
                        || !userJsonObject.has("height") || userJsonObject.get("height").isJsonNull()
                        || !userJsonObject.has("email") || userJsonObject.get("email").isJsonNull()) {
                    responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
                    return;
                }

                // 创建 UserBean 对象
                UserBean userBean = new UserBean();
                userBean.setName(userJsonObject.get("name").getAsString());
                userBean.setAge(userJsonObject.get("age").getAsInt());
                userBean.setHeight(userJsonObject.get("height").getAsDouble());
                userBean.setEmail(userJsonObject.get("email").getAsString());
                userBeanList.add(userBean);
            }

            // 调用业务层 API
            List<Integer> ret = userService.saveBatch(userBeanList);
            if (!ret.isEmpty()) {
                responseData(resp, ret);
            } else {
                responseError(resp, new ServiceException(CommonResponse.REQUEST_ERROR.getCode(), CommonResponse.REQUEST_ERROR.getMessage()));
            }
        } catch (Exception e) {
            responseError(resp, e);
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/remove
     body = {
        "id": 1
     }
     */
    public void remove(HttpServletRequest req, HttpServletResponse resp) {
        // 从请求体里读取 JSON 字符串
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            responseError(resp, e);
            return;
        }
        String body = sb.toString();

        try {
            // 用 JSON 库解析，这里暂时用的是 gson 这个库
            Gson gson = new Gson();

            JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
            System.out.println("请求参数：" + jsonObject);
            // 对请求参数进行基础有效性校验
            if (!jsonObject.has("id") || jsonObject.get("id").isJsonNull()) {
                responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
                return;
            }

            Integer id = jsonObject.get("id").getAsInt();

            // 调用业务层 API
            Boolean ret = userService.remove(id);
            if (ret) {
                responseData(resp, null);
            } else {
                responseError(resp, new ServiceException(UserResponse.USER_NOT_EXIST.getCode(), UserResponse.USER_NOT_EXIST.getMessage()));
            }
        } catch (Exception e) {
            responseError(resp, e);
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/removeBatch
     body = {
        "idList": [1, 2, 54, 55]
     }
     */
    public void removeBatch(HttpServletRequest req, HttpServletResponse resp) {
        // 从请求体里读取 JSON 字符串
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            responseError(resp, e);
            return;
        }
        String body = sb.toString();

        try {
            // 用 JSON 库解析，这里暂时用的是 gson 这个库
            Gson gson = new Gson();

            JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
            System.out.println("请求参数：" + jsonObject);
            // 对请求参数进行基础有效性校验
            if (!jsonObject.has("idList") || jsonObject.get("idList").isJsonNull()) {
                responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
                return;
            }

            JsonArray jsonArray = jsonObject.get("idList").getAsJsonArray();
            // 对请求参数进行基础有效性校验
            if (jsonArray.isJsonNull()) {
                responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
                return;
            }

            List<Integer> idList = new ArrayList<>();
            for (JsonElement jsonElement : jsonArray) {
                Integer id = jsonElement.getAsInt();
                idList.add(id);
            }

            // 调用业务层 API
            Boolean ret = userService.removeBatch(idList);
            if (ret) {
                responseData(resp, null);
            } else {
                responseError(resp, new ServiceException(CommonResponse.REQUEST_ERROR.getCode(), CommonResponse.REQUEST_ERROR.getMessage()));
            }
        } catch (Exception e) {
            responseError(resp, e);
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/update
     body = {
        "id": 1,
        "fieldsToUpdate": {
            "age": 31
        }
     }
     */
    public void update(HttpServletRequest req, HttpServletResponse resp) {
        // 从请求体里读取 JSON 字符串
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            responseError(resp, e);
            return;
        }
        String body = sb.toString();

        try {
            // 用 JSON 库解析，这里暂时用的是 gson 这个库
            Gson gson = new Gson();

            JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
            System.out.println("请求参数：" + jsonObject);
            // 对请求参数进行基础有效性校验
            if (jsonObject.isJsonNull()) {
                responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
                return;
            }

            Integer id = jsonObject.get("id").getAsInt();
            JsonObject fieldsToUpdateJsonObject = jsonObject.get("fieldsToUpdate").getAsJsonObject();
            // 对请求参数进行基础有效性校验
            if (fieldsToUpdateJsonObject.isJsonNull()) {
                responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
                return;
            }

            Map<String, Object> fieldsToUpdate = gson.fromJson(fieldsToUpdateJsonObject, new TypeToken<Map<String, Object>>() {
            }.getType());

            // 调用业务层 API
            Boolean ret = userService.update(id, fieldsToUpdate);
            if (ret) {
                responseData(resp, null);
            } else {
                responseError(resp, new ServiceException(CommonResponse.REQUEST_ERROR.getCode(), CommonResponse.REQUEST_ERROR.getMessage()));
            }
        } catch (Exception e) {
            responseError(resp, e);
        }
    }

    /*
     url = http://localhost:9999/helloMyBatis/user/updateBatch
     body = {
        "idList": [1, 2, 54, 55],
        "fieldsToUpdate": {
            "age": 31
        }
     }
     */
    public void updateBatch(HttpServletRequest req, HttpServletResponse resp) {
        // 从请求体里读取 JSON 字符串
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            responseError(resp, e);
            return;
        }
        String body = sb.toString();

        try {
            // 用 JSON 库解析，这里暂时用的是 gson 这个库
            Gson gson = new Gson();

            JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
            System.out.println("请求参数：" + jsonObject);
            // 对请求参数进行基础有效性校验
            if (jsonObject.isJsonNull()) {
                responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
                return;
            }

            JsonArray idJsonArray = jsonObject.get("idList").getAsJsonArray();
            JsonObject fieldsToUpdateJsonObject = jsonObject.get("fieldsToUpdate").getAsJsonObject();
            // 对请求参数进行基础有效性校验
            if (idJsonArray.isJsonNull() || fieldsToUpdateJsonObject.isJsonNull()) {
                responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
                return;
            }

            List<Integer> idList = new ArrayList<>();
            for (JsonElement jsonElement : idJsonArray) {
                Integer id = jsonElement.getAsInt();
                idList.add(id);
            }
            Map<String, Object> fieldsToUpdate = gson.fromJson(fieldsToUpdateJsonObject, new TypeToken<Map<String, Object>>() {
            }.getType());

            // 调用业务层 API
            Boolean ret = userService.updateBatch(idList, fieldsToUpdate);
            if (ret) {
                responseData(resp, null);
            } else {
                responseError(resp, new ServiceException(CommonResponse.REQUEST_ERROR.getCode(), CommonResponse.REQUEST_ERROR.getMessage()));
            }
        } catch (Exception e) {
            responseError(resp, e);
        }
    }
}
