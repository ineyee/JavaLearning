import bean.UserBean;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import util.MyBatisUtil;

import java.util.List;

public class UserBatchTest {
    @Test
    void saveBatch() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            UserBean userBean1 = new UserBean();
            userBean1.setName("Kobe");
            userBean1.setAge(41);
            userBean1.setHeight(1.98);
            userBean1.setEmail("kobe@gmail.com");

            UserBean userBean2 = new UserBean();
            userBean2.setName("James");
            userBean2.setEmail("james@gmail.com");

            List<UserBean> userBeanList = List.of(userBean1, userBean2);

            int ret = session.insert("com.ineyee.dao.UserBatchDao.saveBatch", userBeanList);
            if (ret > 0) {
                System.out.println("批量保存成功");
            } else {
                System.out.println("批量保存失败");
            }

            session.commit();
        }
    }

    @Test
    void removeBatch() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            List<Integer> idList = List.of(33, 34);

            int ret = session.delete("com.ineyee.dao.UserBatchDao.removeBatch", idList);
            if (ret > 0) {
                System.out.println("批量删除成功");
            } else {
                System.out.println("批量删除失败");
            }

            session.commit();
        }
    }
}
