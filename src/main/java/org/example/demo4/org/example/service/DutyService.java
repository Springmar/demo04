package org.example.demo4.org.example.service;


import org.example.demo4.org.example.instance.DutyScheduleDTO;
import org.example.demo4.org.example.instance.DutyScheduleItem;
import org.example.demo4.org.example.instance.Student;

import org.example.demo4.org.example.interfaces.StudentRepository;
import org.example.demo4.org.example.interfaces.stdRepository;
import org.example.demo4.org.example.util.HolidayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DutyService {

    @Autowired
    private StudentRepository stdRepository;

    @Autowired
    private HolidayManager holidayManager; // 假设你已经有了这个管理类

    public DutyScheduleDTO generateDutySchedule() {
        // 1. 获取所有学生 (已经按 lastDutyDate 升序排好)
        List<Student> students = stdRepository.findAllByOrderByLastDutyDateAsc();

        if (students.isEmpty()) {
            throw new RuntimeException("暂无学生信息");

        }

        LocalDate today = LocalDate.now();
        DutyScheduleDTO result = new DutyScheduleDTO();
        result.setToday(today);
        result.setTodayHoliday(holidayManager.isHoliday(today));

        // =================================================
        // 👇 核心算法：找第一个“可用”的学生
        // =================================================
        Student currentDutyStudent = null;
        LocalDate dutyDate = today;

        // 如果今天是休息日，我们需要找下一个工作日
        if (holidayManager.isHoliday(today)) {
            LocalDate nextWorkDay = findNextWorkDay(today);
            // 获取那天的值日生
            currentDutyStudent = assignStudentForDate(nextWorkDay, students);
            dutyDate = nextWorkDay;
        } else {
            // 今天是工作日，直接取队列第一个
            currentDutyStudent = students.get(0);
            dutyDate = today;
        }

        // =================================================
        // 👇 设置返回结果
        // =================================================
        result.setDutyName(currentDutyStudent.getName());
        result.setToday(dutyDate);

        // 获取下一位（取排序后的第二个，注意判空）
        if (students.size() > 1) {
            Student nextStudent = students.get(1); // 第二个就是下一位
            result.setNextDutyName(nextStudent.getName());
        } else {
            result.setNextDutyName("无");
        }

        // =================================================
        // 👇 生成未来10天预览 (逻辑同上)
        // =================================================
        List<DutyScheduleItem> schedule = new ArrayList<>();
        LocalDate cursor = today;
        int count = 0;

        while (count < 10) {
            if (!holidayManager.isHoliday(cursor)) {
                Student student = assignStudentForDate(cursor, students);
                DutyScheduleItem item = new DutyScheduleItem();
                item.setDate(cursor);
                item.setStudentName(student.getName());
                result.setTodayHoliday(true); // 标记为工作日
                schedule.add(item);

                //下一个值日日期
                LocalDate nextWorkDay = today.plusDays(1);
                while (holidayManager.isHoliday(nextWorkDay)) {
                    nextWorkDay = nextWorkDay.plusDays(1);
                }
                result.setNextDutyDate(nextWorkDay);
                count++;
            } else {
                DutyScheduleItem item = new DutyScheduleItem();
                item.setDate(cursor);
                item.setStudentName("休息");
                result.setTodayHoliday(false);//标记为休息日

                //下一个值日日期
                LocalDate nextWorkDay = today.plusDays(1);
                while (holidayManager.isHoliday(nextWorkDay)) {
                    nextWorkDay = nextWorkDay.plusDays(1);
                }
                result.setNextDutyDate(nextWorkDay);
                schedule.add(item);
            }
            cursor = cursor.plusDays(1);
        }
        result.setSchedule(schedule);

        // =================================================
        // 👇 关键步骤：更新数据库 (标记该学生已值班)
        // 这一步必须放在最后，确保页面展示完再更新
        // =================================================
        if (currentDutyStudent != null) {
            currentDutyStudent.setLastDutyDate(dutyDate);
            // 注意：由于 @Transactional，这里不需要显式 save，但显式调用更清晰
            stdRepository.save(currentDutyStudent);
        }

        return result;
    }

    // 辅助方法：找下一个工作日
    private LocalDate findNextWorkDay(LocalDate date) {
        LocalDate next = date.plusDays(1);
        while (holidayManager.isHoliday(next)) {
            next = next.plusDays(1);
        }
        return next;
    }

    // 辅助方法：为指定日期分配学生
    // 传入日期和已排序的学生列表
    // 逻辑：返回列表中第一个 lastDutyDate <= cursor 的学生
    // 这里简化处理：直接取列表第一个（因为我们查出来就是按这个排的）
    private Student assignStudentForDate(LocalDate date, List<Student> sortedStudents) {
        // 因为我们传入的 sortedStudents 已经是按 lastDutyDate 升序排好的
        // 所以直接取第一个即可
        return sortedStudents.get(0);
    }
}
