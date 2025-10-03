package servlet;

import bean.ProductBean;
import bean.UserBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import constant.response.CommonResponse;
import constant.response.ProductResponse;
import exception.ServiceException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ProductService;
import service.ProductServiceImpl;
import service.UserService;
import service.UserServiceImpl1;

import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/product/*")
public class ProductServlet extends BaseServlet {
    private final UserService userService = new UserServiceImpl1();
    private final ProductService productService = new ProductServiceImpl();

    @Override
    protected List<String> supportedGetMethods() {
        return List.of("get");
    }

    @Override
    protected List<String> supportedPostMethods() {
        return List.of("save");
    }

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

        // 用 JSON 库解析，这里暂时用的是 gson 这个库
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
        System.out.println("请求参数：" + jsonObject);

        // 对请求参数进行基础有效性校验
        // 表里的 name、price、user_id 字段我们设计成了必传字段，desc 字段我们设计成了可选字段
        if (!jsonObject.has("name") || jsonObject.get("name").isJsonNull()
                || !jsonObject.has("price") || jsonObject.get("price").isJsonNull()
                || !jsonObject.has("userId") || jsonObject.get("userId").isJsonNull()) {
            responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
            return;
        }

        String desc = "";
        if (jsonObject.has("desc") && jsonObject.get("desc").isJsonNull()) {
            desc = jsonObject.getAsString();
        }

        // 根据 userId 查询用户
        Integer userId = jsonObject.get("userId").getAsInt();
        UserBean userBean = null;
        try {
            userBean = userService.get(userId);
        } catch (SQLException e) {
            responseError(resp, new ServiceException(CommonResponse.REQUEST_ERROR.getCode(), CommonResponse.REQUEST_ERROR.getMessage()));
            return;
        }

        // 创建 ProductBean 对象
        ProductBean productBean = new ProductBean();
        productBean.setName(jsonObject.get("name").getAsString());
        productBean.setPrice(jsonObject.get("price").getAsDouble());
        productBean.setDesc(desc);
        productBean.setUserBean(userBean);

        try {
            // 调用业务层 API
            Boolean ret = productService.save(productBean);
            if (ret) {
                responseData(resp, null);
            } else {
                responseError(resp, new ServiceException(CommonResponse.REQUEST_ERROR.getCode(), CommonResponse.REQUEST_ERROR.getMessage()));
            }
        } catch (Exception e) {
            responseError(resp, e);
        }
    }

    public void get(HttpServletRequest req, HttpServletResponse resp) {
        Integer id = Integer.valueOf(req.getParameter("id"));
        System.out.println("请求参数：" + id);

        try {
            // 调用业务层 API
            ProductBean productBean = productService.get(id);
            if (productBean != null) {
                responseData(resp, productBean);
            } else {
                responseError(resp, new ServiceException(ProductResponse.PRODUCT_NOT_EXIST.getCode(), ProductResponse.PRODUCT_NOT_EXIST.getMessage()));
            }
        } catch (Exception e) {
            responseError(resp, e);
        }
    }
}
