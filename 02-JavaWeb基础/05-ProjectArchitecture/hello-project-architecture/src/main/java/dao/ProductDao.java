package dao;

import bean.ProductBean;

import java.sql.SQLException;

public interface ProductDao {
    int save(ProductBean productBean) throws SQLException;

    ProductBean get(Integer id) throws SQLException;
}
