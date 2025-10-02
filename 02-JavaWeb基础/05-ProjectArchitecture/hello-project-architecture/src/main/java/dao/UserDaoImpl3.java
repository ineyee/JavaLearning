package dao;

import bean.UserBean;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

// 用户模块接口的数据层（MyBatis 版）
public class UserDaoImpl3 implements UserDao {
    @Override
    public int save(List<UserBean> userBeanList) throws SQLException {
        return 0;
    }

    @Override
    public int remove(List<Integer> idList) throws SQLException {
        return 0;
    }

    @Override
    public int update(List<Integer> idList, Map<String, Object> fieldsToUpdate) throws SQLException {
        return 0;
    }

    @Override
    public UserBean get(Integer id) throws SQLException {
        return null;
    }

    @Override
    public List<UserBean> list(Integer pageSize, Integer currentPage) throws SQLException {
        return List.of();
    }

    @Override
    public int countUserByEmail(String email) throws SQLException {
        return 0;
    }
}
