package com.ineyee.hello_mysql._03_jdbc.demo.dao;

import com.ineyee.hello_mysql._03_jdbc.demo.bean.PhoneBean;
import com.ineyee.hello_mysql._03_jdbc.demo.util.DatabaseUtil;

import java.sql.*;
import java.util.List;

// 数据层？返回的不是原始数据，上层能直接用了
public class PhoneDao {
    public List<PhoneBean> getPhoneList() throws SQLException, ClassNotFoundException {
        String selectSql = """
                SELECT
                    id,
                    name,
                    price,
                    `desc`,
                    brand,
                    score
                FROM t_product
                WHERE price <= ?;
                """;
        List<PhoneBean> phoneBeanlist = DatabaseUtil.executeQuery(selectSql, resultSet -> {
            PhoneBean phoneBean = new PhoneBean();
            phoneBean.setId(resultSet.getInt("id"));
            phoneBean.setName(resultSet.getString("name"));
            phoneBean.setDesc(resultSet.getString("desc"));
            phoneBean.setPrice(resultSet.getDouble("price"));
            phoneBean.setBrand(resultSet.getString("brand"));
            phoneBean.setScore(resultSet.getDouble("score"));
            return phoneBean;
        }, 6666);
        System.out.println(phoneBeanlist);
        return phoneBeanlist;
    }
}
