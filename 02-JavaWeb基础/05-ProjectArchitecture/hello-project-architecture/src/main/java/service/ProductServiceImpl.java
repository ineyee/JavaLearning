package service;

import bean.ProductBean;
import dao.ProductDao;
import dao.ProductDaoImpl;
import exception.ServiceException;

import java.sql.SQLException;

public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    public Boolean save(ProductBean productBean) throws SQLException, ServiceException {
        return productDao.save(productBean) > 0;
    }

    @Override
    public ProductBean get(Integer id) throws SQLException {
        return productDao.get(id);
    }
}
