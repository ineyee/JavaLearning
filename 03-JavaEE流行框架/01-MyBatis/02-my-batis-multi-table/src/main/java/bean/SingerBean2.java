package bean;

import java.util.List;

public class SingerBean2 extends BaseBean {
    private String name;
    private String sex;
    // 一对多表结构时，这里是一方、主表，在 Java 代码里我们会定义一个数组引用对方的“多”
    // 这样方便 Java 层面读写数据，而且 Java 层面一看就是一对多关系
    //
    // 也就是说，数据库表是通过外键来表征一对多关系的，Java 代码是通过对象引用和数组引用来表征一对多关系的
    private List<SongBean2> songBean2List;

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

    public List<SongBean2> getSongBean2List() {
        return songBean2List;
    }

    public void setSongBean2List(List<SongBean2> songBean2List) {
        this.songBean2List = songBean2List;
    }

    @Override
    public String toString() {
        return "SingerBean1{" +
                "id=" + getId() +
                ", createTime='" + getCreateTime() + '\'' +
                ", updateTime='" + getUpdateTime() + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", songBean2List=" + songBean2List +
                '}';
    }
}
