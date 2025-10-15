import bean.SingerBean1;
import bean.SongBean1;
import util.MyBatisUtil;
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

    // 保存歌曲成功的话，只需要给客户端返回保存成功的歌曲的完整信息即可，客户端本身就知道这首歌曲应该挂在哪个歌手身上，因为客户端新增歌曲的时候必然得选择一个歌手 id 传过来，所以客户端拿到这个结果后就知道该怎么刷新数据和 UI 了
    // 服务端的这个方法里不需要维护歌手和歌曲的新内存关系，只需要保证数据库存进去就行了，用户下次访问 get 或 list 时肯定能获取到歌手和歌曲的新内存关系
    @Test
    void save() {
        try (SqlSession session = MyBatisUtil.openSession()) {
            // 客户端可能只传上来一个歌手 id，但如果歌手有一些其它必填字段，那这里就用歌手 id 去歌手表里获取一下歌手的完整信息
            SingerBean1 singerBean1 = new SingerBean1();
            singerBean1.setId(4);

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
        }
    }
}
