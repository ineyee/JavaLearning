package bean;

public class ProductBean extends BaseBean {
    public ProductBean() {
    }

    private String name; // 必传
    private Double price; // 必传
    private String desc; // 可选
    private Integer brand; // 可选
    private Double score; // 可选
    // t_product 表里是通过外键 user_id 引用 t_user 表的
    // 那么按照“Bean 的字段必须和数据库表里的字段一一对应”的说法，这里应该定义成 Integer userId; 才对
    // 但实际上，在 Java 代码里我们一般不定义成外键属性，而是直接定义成对象，这样可以使得 Java 代码更加面向对象
    private UserBean userBean; // 可选

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getBrand() {
        return brand;
    }

    public void setBrand(Integer brand) {
        this.brand = brand;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    @Override
    public String toString() {
        return "ProductBean{" +
                "id='" + getId() + '\'' +
                ", createTime='" + getCreateTime() + '\'' +
                ", updateTime='" + getUpdateTime() + '\'' +
                "name='" + name + '\'' +
                ", price=" + price +
                ", desc='" + desc + '\'' +
                ", brand=" + brand +
                ", score=" + score +
                ", userBean=" + userBean +
                '}';
    }
}
