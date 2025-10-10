import bean.UserBean;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import util.MyBatisUtil;

import java.util.List;
import java.util.Map;

public class UserTest {
    @Test
    void get() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.selectOne(statement, parameter)：用来查询单条数据，查到则返回 bean，查不到则返回 null
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当参数个数为一个时，直接把参数值传进去就可以了
            UserBean userBean = session.selectOne("com.ineyee.dao.UserDao.get", 21);
            System.out.println(userBean);
        }
    }

    @Test
    void select() {
        // 创建一个会话，项目运行期间可以创建多个
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 执行 SQL 语句
            // session.selectList(statement, parameter)：用来查询多条数据，查到则返回 [bean]，查不到则返回 []
            //   statement（String）：SQL 语句的命名空间.SQL 语句的唯一标识
            //   parameter（Object）：SQL 语句的参数，当参数个数为多个时，可以搞成 Map 把参数传进去（value 值在外界算好再传进去，里面只能读取、不能进行运算）
            Integer pageSize = 2;
            Integer currentPage = 2;
            List<UserBean> userBeanList = session.selectList("com.ineyee.dao.UserDao.list", Map.of(
                    "limit", pageSize,
                    "offset", (currentPage - 1) * pageSize
            ));
            for (UserBean userBean : userBeanList) {
                System.out.println(userBean);
            }
        }
    }
}
