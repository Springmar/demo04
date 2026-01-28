package org.example.demo4.org.example.instance;

import java.time.LocalDate;

// DutyScheduleItem.java
public class DutyScheduleItem {
    private LocalDate date;
    private String studentName;
    private boolean today = false;

    private boolean workDay;

    // Getter 和 Setter
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    // 👇 新增：workDay 的 Getter 和 Setter
    public boolean isWorkDay() {
        return workDay;
    }

    public void setWorkDay(boolean workDay) {
        this.workDay = workDay;
    }
    public boolean isToday() { return today; }
    public void setToday(boolean today) { this.today = today; }
}
