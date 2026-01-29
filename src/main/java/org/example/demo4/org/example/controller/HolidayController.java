package org.example.demo4.org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.demo4.org.example.instance.CalendarDay;
import org.example.demo4.org.example.instance.Holiday;
import org.example.demo4.org.example.interfaces.HolidayRepository;
import org.example.demo4.org.example.service.HolidayService;
import org.example.demo4.org.example.util.HolidayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// 文件: HolidayController.java
@Controller
public class HolidayController {

    @Autowired
    private HolidayRepository holidayRepository;

    private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayManager holidayManager;

    @GetMapping("/holiday")
    public String holidayView(Model model) {
        LocalDate now = LocalDate.now();
        // 生成日历数据
        List<List<CalendarDay>> calendar = buildCalendar(now.getYear(), now.getMonthValue());

        model.addAttribute("calendar", calendar);
        model.addAttribute("currentYear", now.getYear());
        model.addAttribute("currentMonth", now.getMonthValue());
        return "holiday/index"; // 对应 templates/holiday/index.html
    }

    /**
     * 构建日历矩阵的核心逻辑
     */
    private List<List<CalendarDay>> buildCalendar(int year, int month) {
        // 1. 获取年月
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        // 2. 计算偏移量 (第一天是星期几: 周日=0, 周一=1, ..., 周六=6)
        // Java Time API 中 Sunday = 1, Monday = 2... 我们需要把 Sunday 变成 0
        int firstDayOfWeek = (firstDay.getDayOfWeek().getValue() % 7);

        // 3. 准备结果
        List<List<CalendarDay>> weeks = new ArrayList<>();
        List<CalendarDay> currentWeek = new ArrayList<>();

        // --- 1. 填充第一行前面的空白（上个月） ---
        LocalDate prevMonthDay = firstDay.minusDays(firstDayOfWeek);
        for (int i = 0; i < firstDayOfWeek; i++) {
            LocalDate date = prevMonthDay.plusDays(i);
            // 判断是否为节假日（如果是周末或者在数据库中标记为休息）
            boolean isHoliday = holidayManager.isHoliday(date);
            currentWeek.add(new CalendarDay(date, isHoliday, false));

            // 如果填满了一周，加入结果集
            if (currentWeek.size() == 7) {
                weeks.add(currentWeek);
                currentWeek = new ArrayList<>();
            }
        }

        // --- 2. 填充当月日期 ---
        for (int day = 1; day <= lastDay.getDayOfMonth(); day++) {
            LocalDate date = LocalDate.of(year, month, day);

            if(date != null) {

            }
            boolean isHoliday = holidayManager.isHoliday(date);

            // 如果当前周满了，开启新的一周
            if (currentWeek.size() == 7) {
                weeks.add(currentWeek);
                currentWeek = new ArrayList<>();
            }
            currentWeek.add(new CalendarDay(date, isHoliday, true));
        }

        // --- 3. 填充最后一行后面的空白（下个月） ---
        while (currentWeek.size() < 7) {
            // 获取当前最后一个日期，推算下一天
            LocalDate lastDate = currentWeek.get(currentWeek.size() - 1).getDate();
            LocalDate nextDate = lastDate.plusDays(1);
            boolean isHoliday = holidayManager.isHoliday(nextDate);

            currentWeek.add(new CalendarDay(nextDate, isHoliday, false));
        }
        weeks.add(currentWeek);

        // --- 4. 确保至少有6行 (防止前端表格跳动，可选) ---
        // --- 4. 确保有6行，但每一行都必须有7个元素 ---
        while (weeks.size() < 6) {
            // 创建一个包含7个空占位符的周，或者填充下个月的日期
            List<CalendarDay> emptyWeek = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                // 可以创建一个空的 CalendarDay，或者设置 date 为 null，但前端要处理
                emptyWeek.add(new CalendarDay(null, false, false)); // 假设你允许 date 为 null
            }
            weeks.add(emptyWeek);
        }

        return weeks;
    }

    //设置休息日或工作日

    /**
     * 切换日期状态（休息日/工作日）
     * @param dateStr 日期字符串，格式：yyyy-MM-dd
     * @param redirectAttrs 重定向属性
     * @return 重定向到日历页面
     */
    @GetMapping("/holiday/toggle")
    public String toggleHolidayStatus(@RequestParam("date") String dateStr,
                                      RedirectAttributes redirectAttrs) {
        try {


            // 解析日期
            LocalDate date = LocalDate.parse(dateStr);
            logger.info("接收到日期状态切换请求: {}", date);

            //判断是否为节假日
            boolean isHoliday = holidayManager.isHoliday(date);

            // 调用服务层方法切换日期状态
            boolean isHoliday1 = holidayService.toggleHolidayStatus(date,isHoliday);

            // 添加成功消息
            String message = isHoliday ? "✅ 已成功设置为休息日" : "✅ 已成功设置为工作日";
            logger.info("日期状态切换结果: {}", message);
            redirectAttrs.addFlashAttribute("message", message);

        } catch (DateTimeParseException e) {
            logger.error("日期格式错误: {}", dateStr, e);
            redirectAttrs.addFlashAttribute("error", "❌ 日期格式错误，请使用yyyy-MM-dd格式");
        } catch (Exception e) {
            logger.error("日期状态切换失败: {}", e.getMessage(), e);
            redirectAttrs.addFlashAttribute("error", "❌ 操作失败: " + e.getMessage());
        }

        // 重定向回日历页面
        return "redirect:/holiday";
    }


}
