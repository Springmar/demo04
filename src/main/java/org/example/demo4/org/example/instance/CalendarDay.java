package org.example.demo4.org.example.instance;

// 文件: CalendarDay.java


import java.time.LocalDate;

/**
 * 用于日历表格展示的单元格数据
 */
public class CalendarDay {
    private LocalDate date; // 日期
    private boolean isHoliday; // 是否休息
    private boolean isCurrentMonth; // 是否当月（用于变灰）

    // 构造函数
    public CalendarDay(LocalDate date, boolean isHoliday, boolean isCurrentMonth) {
        this.date = date;
        this.isHoliday = isHoliday;
        this.isCurrentMonth = isCurrentMonth;
    }

    // Getters and Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public boolean isHoliday() { return isHoliday; }
    public void setHoliday(boolean holiday) { isHoliday = holiday; }

    public boolean isCurrentMonth() { return isCurrentMonth; }
    public void setCurrentMonth(boolean currentMonth) { isCurrentMonth = currentMonth; }
}
