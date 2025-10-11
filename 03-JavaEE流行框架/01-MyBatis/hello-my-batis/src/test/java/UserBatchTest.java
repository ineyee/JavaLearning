import bean.UserBean;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import util.MyBatisUtil;

import java.util.List;

public class UserBatchTest {
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
