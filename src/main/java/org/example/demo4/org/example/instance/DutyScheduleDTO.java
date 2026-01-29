package org.example.demo4.org.example.instance;

import java.time.LocalDate;
import java.util.List;

public class DutyScheduleDTO {
    // 今天的日期
    private LocalDate today;

    // 今天是否是节假日
    private boolean isTodayHoliday;

    // 今天的值日生名字
    private String dutyName;

    // 下一位值日日期
    private String nextDutyName;

    public LocalDate getNextDutyDate() {
        return nextDutyDate;
    }

    public void setNextDutyDate(LocalDate nextDutyDate) {
        this.nextDutyDate = nextDutyDate;
    }

    // 下一位值日生名字
    private LocalDate nextDutyDate;

    // 排班预览列表（比如未来10天）
    private List<DutyScheduleItem> schedule;

    // 必须要有 getter 和 setter 方法，否则 Thymeleaf 拿不到数据
    public LocalDate getToday() { return today; }
    public void setToday(LocalDate today) { this.today = today; }

    public boolean isTodayHoliday() { return isTodayHoliday; }
    public void setTodayHoliday(boolean todayHoliday) { isTodayHoliday = todayHoliday; }

    public String getDutyName() { return dutyName; }
    public void setDutyName(String dutyName) { this.dutyName = dutyName; }

    public String getNextDutyName() { return nextDutyName; }
    public void setNextDutyName(String nextDutyName) { this.nextDutyName = nextDutyName; }

    public List<DutyScheduleItem> getSchedule() { return schedule; }
    public void setSchedule(List<DutyScheduleItem> schedule) { this.schedule = schedule; }
}
