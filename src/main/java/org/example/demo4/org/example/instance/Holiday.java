package org.example.demo4.org.example.instance;







import jakarta.persistence.*;


import java.time.LocalDate;

@Entity
@Table(name = "t_holiday")
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 备注 (例如: 春节调休)
    private String remark;

    @Column(nullable = true)
    private Integer type;

    // 日期 (唯一索引，避免重复)
    @Column(unique = true, nullable = false)
    private LocalDate date;

    public Integer getType() {
        return type;
    }


    // 类型: 0=工作日, 1=节假日
    public void setType(Integer type) {
        this.type = type;
    }





    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


}
