import bean.SingerBean1;
import bean.SongBean1;
import common.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SongTest1 {
    @Test
    void list1() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            List<SongBean1> songBean1List = session.selectList("dao.songDao1.list1");
            if (!songBean1List.isEmpty()) {
                System.out.println("查询成功：" + songBean1List);
            } else {
                System.out.println("查询失败");
            }
        }
    }

    @Test
    void list2() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            List<SongBean1> songBean1List = session.selectList("dao.songDao1.list2");
            if (!songBean1List.isEmpty()) {
                System.out.println("查询成功：" + songBean1List);
            } else {
                System.out.println("查询失败");
            }
        }
    }

    @Test
    void save() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            SingerBean1 singerBean1 = session.selectOne("dao.singerDao1.get", 4);
            if (singerBean1 != null) {
                SongBean1 songBean1 = new SongBean1();
                songBean1.setName("同桌的你");
                songBean1.setSingerBean1(singerBean1);

                int ret = session.insert("dao.songDao1.save", songBean1);
                if (ret > 0) {
                    System.out.println("保存成功：" + songBean1);
                } else {
                    System.out.println("保存失败");
                }

                session.commit();
            } else {
                System.out.println("查询用户失败，无法保存产品");
            }
        }
    }
}
