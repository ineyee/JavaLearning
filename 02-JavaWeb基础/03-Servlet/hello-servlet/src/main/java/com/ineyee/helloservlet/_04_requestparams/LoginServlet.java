/**
 * 这个 Servlet 演示获取 get 请求和 post 请求的参数
 */

package com.ineyee.helloservlet._04_requestparams;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/login04")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("请求格式" + req.getContentType());

        if (req.getContentType().contains("application/x-www-form-urlencoded")) {
            formUrlencoded(req, resp);
        } else if (req.getContentType().contains("application/json")) {
            json(req, resp);
        }
    }

    // 当使用表单提交时，获取 post 请求的参数也是通过 req.getParameter(xxx) 这个方法一个参数一个参数获取就可以了
    // 不过需要注意的是：无论客户端传递参数的数据类型是什么，这个方法永远都是返回 String，所以可能需要手动转换一下数据类型
    private void formUrlencoded(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        System.out.println("请求参数：" + username + " " + password);

        if (username.equals("ineyee") && password.equals("123456")) {
            resp.getWriter().write("登录成功");
        } else {
            resp.getWriter().write("登录失败，请检查用户名或密码是否正确");
        }
    }

    // 当使用 JSON 提交时，不会自动解析 JSON，我们需要自己从请求体里读取 JSON 字符串，然后再用 JSON 库解析
    private void json(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 从请求体里读取 JSON 字符串
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String body = sb.toString(); // {    "username": "ineyee",    "password": "123456"}

        // 用 JSON 库解析，这里暂时用的是 gson 这个库
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(body, JsonObject.class);

        String username = json.get("username").getAsString();
        String password = json.get("password").getAsString();

        System.out.println("请求参数：" + username + " " + password);

        if (username.equals("ineyee") && password.equals("123456")) {
            resp.getWriter().write("登录成功");
        } else {
            resp.getWriter().write("登录失败，请检查用户名或密码是否正确");
        }
    }
}
