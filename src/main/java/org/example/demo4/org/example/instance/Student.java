package org.example.demo4.org.example.instance;

import jakarta.persistence.*;

@Entity // 这个注解表示这是一个数据库表
@Table(name = "t_student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String className; // 班级

    @Column(nullable = false)
    private String studentID; // 学号


//    @Column(nullable = false)
//    private String dutyTime; // 值日时间


    // 无参构造函数 (JPA要求)
    public Student() {}

    // 全参构造函数
    public Student(String name, String className) {
        this.name = name;
        this.className = className;
    }

    // Getter 和 Setter 方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }

//    public String getDutyTime() { return dutyTime; }
//    public void setDutyTime(String dutyTime) { this.dutyTime = dutyTime; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}
