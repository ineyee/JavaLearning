package com.ineyee.hellomysql._03_jdbc.demo.servlet;

import com.google.gson.Gson;
import com.ineyee.hellomysql._03_jdbc.demo.bean.PhoneBean;
import com.ineyee.hellomysql._03_jdbc.demo.dao.PhoneDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/getPhoneList")
public class PhoneListServLet extends HttpServlet {
    private final PhoneDao phoneDao = new PhoneDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
