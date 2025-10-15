import bean.SingerBean1;
import util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SingerTest1 {
    @Test
    void list1() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            List<SingerBean1> singerBean1List = session.selectList("dao.singerDao1.list1");
            if (!singerBean1List.isEmpty()) {
                System.out.println("查询成功：" + singerBean1List);
            } else {
                System.out.println("查询失败");
            }
        }
    }

    @Test
    void list2() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            List<SingerBean1> singerBean1List = session.selectList("dao.singerDao1.list2");
            if (!singerBean1List.isEmpty()) {
                System.out.println("查询成功：" + singerBean1List);
            } else {
                System.out.println("查询失败");
            }
        }
    }

    // 保存歌手成功的话，只需要给客户端返回保存成功的歌手的完整信息即可，客户端拿到这个结果后就知道该怎么刷新数据和 UI 了
    @Test
    void save() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            SingerBean1 singerBean1 = new SingerBean1();
            singerBean1.setName("老狼");
            singerBean1.setSex("男");

            int ret = session.insert("dao.singerDao1.save", singerBean1);
            if (ret > 0) {
                System.out.println("保存成功：" + singerBean1);
            } else {
                System.out.println("保存失败");
            }

            session.commit();
        }
    }
}
