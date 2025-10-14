package bean;

import java.util.List;

public class SingerBean3 extends BaseBean {
    private String name;
    private String sex;
    // 多对多表结构时，在 Java 代码里我们会定义一个数组引用对方的“多”
    // 这样方便 Java 层面读写数据，而且 Java 层面一看就是多对多关系
    //
    // 也就是说，数据库表是通过外键来表征多对多关系的，Java 代码是通过数组引用和数组引用来表征多对多关系的
    private List<SongBean3> songBean3List;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<SongBean3> getSongBean3List() {
        return songBean3List;
    }

    public void setSongBean3List(List<SongBean3> songBean3List) {
        this.songBean3List = songBean3List;
    }

    @Override
    public String toString() {
        return "SingerBean1{" +
                "id=" + getId() +
                ", createTime='" + getCreateTime() + '\'' +
                ", updateTime='" + getUpdateTime() + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", songBean3List=" + songBean3List +
                '}';
    }
}
