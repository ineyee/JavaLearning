package registerlogin.service;

import registerlogin.bean.UserBean;
import registerlogin.constant.response.UserResponse;
import registerlogin.dao.UserDao;
import registerlogin.dao.UserDaoImpl;
import registerlogin.exception.ServiceException;

import java.sql.SQLException;

// 用户模块接口的业务层（方案一）
//
// 业务层（service）的职责就是处理我们实际的业务逻辑，如业务规则校验、数据库事务管理等
// 业务层内部在调用数据层的 API，等待数据层给它返回“影响的数据条数”或“原始模型 bean”，然后把它们处理成表现层更方便使用的业务型数据交付给表现层，比如“影响的数据条数”转换成“成功与否”这样更易使用的业务型数据
//
// 换句话说：
//   * 对下，业务层要对数据层负责，负责的体现就是要做好业务规则校验，确保传递给数据层的参数是人家直接能用的，不用人家再做什么乱七八糟的校验
//   * 对上
//     * 业务层要绝对相信表现层，相信的体现就是直接拿着参数去做业务，而不再纠结于参数会不会出问题，要绝对相信经过表现层校验后的参数是肯定没问题的
//     * 业务层要对表现层负责，负责的体现就是要做好数据转换，把数据处理成表现层更方便使用的业务型数据交付给表现层
//   * 其它的业务层就不用关心了，至于执行结果失败了怎样、成功了怎样，那是表现层拿到结果后该干的事
//
// 实践经验：
//  * 执行成功时，总是把执行成功的结果返回，表现层用不用执行成功的结果由它自己决定
//  * 执行失败时，这里不需要 try-catch 数据层 API 执行失败的异常，继续往上层抛、抛到表现层
//  * 业务规则校验出错或做业务出错时，需要我们主动抛出异常、抛到表现层
//
// 注意：业务层绝对不是数据层的马甲层，它有存在的意义，比如同样都是 save 方法，数据层里的 save 就是直接往数据库里写数据
// 而业务层里的 save 则需要首先判断数据库里存不存在这个用户邮箱，存在的话就跳过，不存在的话再往数据库里写，因为用户邮箱是不允许重复的
// 也就是说，业务层的一个方法里可能会调用数据层里的多个方法来共同完成一个业务
public class UserServiceImpl implements UserService {
    // 这里用的数据层方案是方案一
    private final UserDao userDao = new UserDaoImpl();

    public UserBean save(UserBean userBean) throws SQLException, ServiceException {
        // 业务规则校验：数据库里不存在才添加，已存在则不添加
        UserBean tempUserBean = get(userBean.getEmail());
        if (tempUserBean != null) {
            throw new ServiceException(UserResponse.USER_ALREADY_EXIST.getCode(), UserResponse.USER_ALREADY_EXIST.getMessage());
        }

        if (userDao.save(userBean) > 0) {
            return userDao.get(userBean.getEmail());
        } else {
            return null;
        }
    }

    public UserBean get(String email) throws SQLException {
        return userDao.get(email);
    }
}
