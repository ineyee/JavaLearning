package com.ineyee.hello_mysql._03_jdbc.demo.servlet;

import com.google.gson.Gson;
import com.ineyee.hello_mysql._03_jdbc.demo.bean.PhoneBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/getPhoneList")
public class PhoneListServLet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<PhoneBean> phoneBeanlist = getPhoneList();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("code", 0);
        responseMap.put("message", "success");
        responseMap.put("data", phoneBeanlist);

        Gson gson = new Gson();
        String responseJson = gson.toJson(responseMap);

        // 告诉客户端响应体的格式为 json，编码方式为 utf-8
        resp.setContentType("application/json;charset=utf-8");
        // 写数据
        resp.getWriter().write(responseJson);
    }

    private List<PhoneBean> getPhoneList() {
        List<PhoneBean> phoneBeanlist = new ArrayList<PhoneBean>();

        final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
        final String URL = "jdbc:mysql://localhost:3306/db_hello_mysql?serverTimezone=UTC";
        final String USER = "root";
        final String PASSWORD = "mysqlroot";

        try {
            Class.forName(DRIVER_CLASS_NAME);

            String selectSql = """
                    SELECT
                        id,
                        name,
                        price,
                        `desc`,
                        brand,
                        score
                    FROM t_product;
                    """;
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
                ResultSet selectRet = preparedStatement.executeQuery();
                while (selectRet.next()) {
                    PhoneBean phoneBean = new PhoneBean();
                    phoneBean.setId(selectRet.getInt("id"));
                    phoneBean.setName(selectRet.getString("name"));
                    phoneBean.setDesc(selectRet.getString("desc"));
                    phoneBean.setPrice(selectRet.getDouble("price"));
                    phoneBean.setBrand(selectRet.getString("brand"));
                    phoneBean.setScore(selectRet.getDouble("score"));
                    phoneBeanlist.add(phoneBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(phoneBeanlist);
        return phoneBeanlist;
    }
}
