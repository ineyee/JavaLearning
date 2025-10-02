package service;

import bean.UserBean;
import exception.ServiceException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

// 用户模块接口的业务层（方案二）
public class UserServiceImpl2 implements UserService {
    @Override
    public Boolean save(List<UserBean> userBeanList) throws SQLException, ServiceException {
        return null;
    }

    @Override
    public Boolean remove(List<Integer> idList) throws SQLException, ServiceException {
        return null;
    }

    @Override
    public Boolean update(List<Integer> idList, Map<String, Object> fieldsToUpdate) throws SQLException, ServiceException {
        return null;
    }

    @Override
    public UserBean get(Integer id) throws SQLException {
        return null;
    }

    @Override
    public List<UserBean> list(Integer pageSize, Integer currentPage) throws SQLException {
        return List.of();
    }
}
