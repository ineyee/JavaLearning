package bean;

public class SingerBean1 extends BaseBean {
    private String name;
    private String sex;
    // 一对一表结构时，在 Java 代码里我们会定义一个对象引用对方的“一”
    // 这样方便 Java 层面读写数据，而且 Java 层面一看就是一对一关系
    //
    // 也就是说，数据库表是通过外键来表征一对一关系的，Java 代码是通过对象引用和对象引用来表征一对一关系的
    private SongBean1 songBean1;

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

    public SongBean1 getSongBean1() {
        return songBean1;
    }

    public void setSongBean1(SongBean1 songBean1) {
        this.songBean1 = songBean1;
    }

    @Override
    public String toString() {
        return "SingerBean1{" +
                "id=" + getId() +
                ", createTime='" + getCreateTime() + '\'' +
                ", updateTime='" + getUpdateTime() + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", songBean1=" + songBean1 +
                '}';
    }
}
