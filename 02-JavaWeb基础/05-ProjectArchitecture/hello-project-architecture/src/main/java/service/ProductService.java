package service;

import bean.ProductBean;
import exception.ServiceException;

import java.sql.SQLException;

public interface ProductService {
    Boolean save(ProductBean productBean) throws SQLException, ServiceException;

    ProductBean get(Integer id) throws SQLException;
}
