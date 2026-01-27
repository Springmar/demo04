package org.example.demo4.org.example.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class HolidayManager {

    // 使用静态集合存储节假日 (实际项目中可以从数据库查询)
    private static Set<LocalDate> holidaySet = new HashSet<>();
    private static Set<LocalDate> weekendSet = new HashSet<>(); // 可配置的周末（如果调休）

    static {
        // 初始化节假日数据 (示例：2024年春节)
        holidaySet.add(LocalDate.of(2026, 2, 10));
        holidaySet.add(LocalDate.of(2026, 2, 11));
        holidaySet.add(LocalDate.of(2026, 2, 12));
        holidaySet.add(LocalDate.of(2026, 2, 13));
        holidaySet.add(LocalDate.of(2026, 2, 14));
        // 可以继续添加其他日期...

        // 初始化周末 (默认周六周日为休息日)
        // 注意：如果遇到国庆/春节调休，这里需要手动添加工作日
        // weekendSet.add(LocalDate.of(2024, 2, 4)); // 假设初七上班
    }

    /**
     * 判断指定日期是否为节假日（包含周末）
     */
    public static boolean isHoliday(LocalDate date) {
        // 如果在黑名单里，或者是周六周日（且未被标记为补班），则视为休息日
        if (holidaySet.contains(date)) {
            return true;
        }

        // 检查是否为周六或周日
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if ((dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)) {
            // 如果是周末，但被标记为补班（在weekendSet里），则不算休息日
            return !weekendSet.contains(date);
        }

        return false;
    }
}