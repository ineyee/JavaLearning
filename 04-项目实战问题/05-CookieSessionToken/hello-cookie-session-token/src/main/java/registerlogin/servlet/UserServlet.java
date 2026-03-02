package registerlogin.servlet;

import _04_登录凭证_token.KeyLoadUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import registerlogin.bean.UserBean;
import registerlogin.constant.response.CommonResponse;
import registerlogin.constant.response.UserResponse;
import registerlogin.exception.ServiceException;
import registerlogin.service.UserService;
import registerlogin.service.UserServiceImpl;

import java.io.BufferedReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
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
    private static final PrivateKey privateKey;
    private static final PublicKey publicKey;

    static {
        try {
            privateKey = KeyLoadUtil.loadPrivateKey("keys/private_key.pem");
            publicKey = KeyLoadUtil.loadPublicKey("keys/public_key.pem");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 这里用的业务层方案是方案一
    private final UserService userService = new UserServiceImpl();

    // 当前 Servlet 支持的 GET 方法，即接口的第二层路径名
    @Override
    protected List<String> supportedGetMethods() {
        return List.of("info");
    }

    // 当前 Servlet 支持的 POST 方法，即接口的第二层路径名
    @Override
    protected List<String> supportedPostMethods() {
        return List.of("register", "login");
    }

    // 注意：
    // 1、这几个方法名必须跟接口的第二层路径同名，因为我们正是要通过第二层路径来反射出接口对应的方法
    // 2、这几个方法必须定义成 public 才能被上面的 getClass().getMethod(xxx) 方法查找到

    /// POST 请求的参数放在请求体里，所以如果不是开发者和黑客的话一般不知道咋拦截请求体，相对来说更安全一点
    /// 但是开发者和黑客很容易能拦截到 POST 请求的请求体，比如浏览器的检查里就拦截了 POST 请求的请求体，因此 POST 请求 + 明文传输也不是绝对地安全
    ///
    /// 所以注册、登录接口我们一般这么设计：
    ///   * 用户在客户端上输入原始明文密码
    ///   * 客户端对原始明文密码进行 MD5 加密
    ///   * 客户端走注册、登录接口时传递的就是 MD5 加密后的密码，这样传输过程就是安全的
    ///   * 服务端收到 MD5 加密后的密码直接存储到数据库里，而不是明文存储，这样数据库存储也是安全的

    public void register(HttpServletRequest req, HttpServletResponse resp) {
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

        // 对请求参数进行基础有效性校验，这三个字段必传，其它字段可选
        if (!jsonObject.has("name") || jsonObject.get("name").isJsonNull()
                || !jsonObject.has("email") || jsonObject.get("email").isJsonNull()
                || !jsonObject.has("password") || jsonObject.get("password").isJsonNull()) {
            responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
            return;
        }

        String name = jsonObject.get("name").getAsString();
        String email = jsonObject.get("email").getAsString();
        String password = jsonObject.get("password").getAsString();

        UserBean tempUserBean = new UserBean();
        tempUserBean.setName(name);
        tempUserBean.setEmail(email);
        tempUserBean.setPassword(password);

        try {
            // 调用业务层 API
            UserBean fullUserBean = userService.save(tempUserBean);
            if (fullUserBean != null) {
                // 注册成功，一般有两种做法：
                //   * 只返回注册成功的标识，客户端再跳转登录界面让用户去登录
                //   * 直接完成自动登录，返回 token 和用户信息，减少用户登录步骤
                // 这里我们选择第二种做法

                // 服务端生成 token
                //   claims：携带的业务信息，将来其它接口验证 token 时可以通过业务信息决定是哪个用户
                //   expiration：过期时间（单位秒）
                //   signWith：私钥
                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", fullUserBean.getId());
                claims.put("email", fullUserBean.getEmail());
                String token = Jwts.builder()
                        .claims(claims)
                        .expiration(Date.from(Instant.now().plusSeconds(30 * 24 * 60 * 60)))
                        .signWith(privateKey)
                        .compact();

                responseData(resp, new HashMap<>(Map.of(
                        "token", token,
                        "userBean", fullUserBean
                )));
            } else {
                responseError(resp, new ServiceException(CommonResponse.REQUEST_ERROR.getCode(), CommonResponse.REQUEST_ERROR.getMessage()));
            }
        } catch (Exception e) {
            responseError(resp, e);
        }
    }

    public void login(HttpServletRequest req, HttpServletResponse resp) {
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
        if (!jsonObject.has("email") || jsonObject.get("email").isJsonNull()
                || !jsonObject.has("password") || jsonObject.get("password").isJsonNull()) {
            responseError(resp, new ServiceException(CommonResponse.PARAM_ERROR.getCode(), CommonResponse.PARAM_ERROR.getMessage()));
            return;
        }

        String email = jsonObject.get("email").getAsString();
        String password = jsonObject.get("password").getAsString();

        try {
            // 调用业务层 API
            UserBean userBean = userService.get(email);
            if (userBean != null) {
                if (userBean.getPassword().equals(password)) {
                    // 登录成功，返回 token 和用户信息

                    // 服务端生成 token
                    //   claims：携带的业务信息，将来其它接口验证 token 时可以通过业务信息决定是哪个用户
                    //   expiration：过期时间（单位秒）
                    //   signWith：私钥
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("userId", userBean.getId());
                    claims.put("email", userBean.getEmail());
                    String token = Jwts.builder()
                            .claims(claims)
                            .expiration(Date.from(Instant.now().plusSeconds(30 * 24 * 60 * 60)))
                            .signWith(privateKey)
                            .compact();

                    responseData(resp, new HashMap<>(Map.of(
                            "token", token,
                            "userBean", userBean
                    )));
                } else {
                    responseError(resp, new ServiceException(UserResponse.EMAIL_PASSWORD_NOT_CORRECT.getCode(), UserResponse.EMAIL_PASSWORD_NOT_CORRECT.getMessage()));
                }
            } else {
                responseError(resp, new ServiceException(UserResponse.EMAIL_PASSWORD_NOT_CORRECT.getCode(), UserResponse.EMAIL_PASSWORD_NOT_CORRECT.getMessage()));
            }
        } catch (Exception e) {
            responseError(resp, e);
        }
    }

    public void info(HttpServletRequest req, HttpServletResponse resp) {
        // 获取 token
        String token = req.getHeader("token");
        System.out.println("请求参数：" + token);

        try {
            // 对请求参数进行基础有效性校验
            // 验证 token
            //   verifyWith：公钥
            //   parse：token
            Claims claims = Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            if (claims.get("userId") == null || claims.get("email") == null) {
                responseError(resp, new ServiceException(UserResponse.INVALID_TOKEN.getCode(), UserResponse.INVALID_TOKEN.getMessage()));
                return;
            }

            Integer userId = Integer.valueOf(claims.get("userId").toString());
            String email = claims.get("email").toString();

            try {
                // 调用业务层 API
                UserBean userBean = userService.get(email);
                if (userBean != null) {
                    responseData(resp, userBean);
                } else {
                    responseError(resp, new ServiceException(UserResponse.INVALID_TOKEN.getCode(), UserResponse.INVALID_TOKEN.getMessage()));
                }
            } catch (Exception e) {
                responseError(resp, e);
            }
        } catch (Exception e) {
            // 验证失败，代表客户端从来没登录过或登录过但 token 过期了
            responseError(resp, new ServiceException(UserResponse.INVALID_TOKEN.getCode(), UserResponse.INVALID_TOKEN.getMessage()));
        }
    }
}
