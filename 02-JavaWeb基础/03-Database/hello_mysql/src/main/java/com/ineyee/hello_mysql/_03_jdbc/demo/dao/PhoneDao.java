package com.ineyee.hello_mysql._03_jdbc.demo.dao;

import com.ineyee.hello_mysql._03_jdbc.demo.bean.PhoneBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhoneDao {
    public List<PhoneBean> getPhoneList() {
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
