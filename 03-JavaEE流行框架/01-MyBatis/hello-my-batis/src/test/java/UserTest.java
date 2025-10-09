import bean.UserBean;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.util.List;

public class UserTest {
    @Test
    void select() throws Exception {
        // 加载 MyBatis 的配置文件（Resources 来自 org.apache.ibatis.io.Resources）
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            // 创建一个工厂构建器
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            // 创建一个工厂
            SqlSessionFactory factory = builder.build(reader);
            // 创建一个会话
            try (SqlSession session = factory.openSession()) {
                // 执行 SQL 语句
                // session.selectOne(SQL 语句的命名空间.SQL 语句的唯一标识): 用来查询单条数据
                // session.selectList(SQL 语句的命名空间.SQL 语句的唯一标识): 用来查询多条数据
                List<UserBean> userBeanList = session.selectList("com.ineyee.dao.UserDao.list");
                for (UserBean userBean : userBeanList) {
                    System.out.println(userBean);
                }
            }
        }
    }
}
