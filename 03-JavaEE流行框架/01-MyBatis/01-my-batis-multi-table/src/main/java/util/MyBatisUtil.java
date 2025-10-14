package util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;

public abstract class MyBatisUtil {
    private static SqlSessionFactory factory;

    static {
        // 加载 MyBatis 的配置文件
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            // 创建一个工厂构建器，项目运行期间只需要创建一个
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            // 创建一个工厂，项目运行期间只需要创建一个
            factory = builder.build(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个会话，外界使用完毕后需要主动关闭会话
     *
     * @return 会话
     */
    static public SqlSession openSession() {
        // 创建一个会话，项目运行期间可以创建多个
        return factory.openSession();
    }
}
