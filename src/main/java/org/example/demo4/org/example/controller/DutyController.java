package org.example.demo4.org.example.controller;

import org.example.demo4.org.example.instance.DutyScheduleDTO;
import org.example.demo4.org.example.instance.DutyScheduleItem;
import org.example.demo4.org.example.instance.Student;
import org.example.demo4.org.example.interfaces.StudentRepository;
import org.example.demo4.org.example.service.DutyService;
import org.example.demo4.org.example.util.HolidayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class DutyController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private HolidayManager holidayManager;

    @Autowired
    private DutyService dutyService;

    // 首页：查询所有学生并返回到页面
//    @GetMapping("/")
//    public String index(Model model) {
//        model.addAttribute("students", studentRepository.findAll());
//        model.addAttribute("newStudent", new Student()); // 用于表单提交
//        return "index"; // 返回 templates 目录下的 index.html
//    }

    // 在 Controller 类中，修改查询方法
    @GetMapping("/")
    public String index(Model model,
                        // 👇 接收前端传来的页码参数，默认第0页（第一页）
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        // 👇 接收每页数量，默认5条
                        @RequestParam(value = "size", defaultValue = "10") int size, Locale locale) {

        // 1. 创建分页对象 (Spring Data JPA 专用)
        Pageable pageable = PageRequest.of(page, size);

        // 2. 查询分页数据
        // 👇 findAll 需要你修改 Repository 继承 JpaRepository 的 findAll 方法，或者直接使用默认的
        Page<Student> studentPage = studentRepository.findAll(pageable);

        // 3. 把分页数据和当前页码传给前端
        model.addAttribute("students", studentPage.getContent()); // 当前页的数据
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages()); // 总页数
        model.addAttribute("totalElements", studentPage.getTotalElements()); // 总记录数
        model.addAttribute("newStudent", new Student());
        model.addAttribute("size", size);


//
//        // 1. 获取所有学生
        List<Student> allStudents = studentRepository.findAll(Sort.by("id"));
        if (allStudents.isEmpty()) {
            return "index";
        }
//        // 2. 定义基准日 (假设从这个日期开始排班)
//        LocalDate baseDate = LocalDate.of(2024, 1, 1);
//        LocalDate today = LocalDate.now();
//        int workDayIndex = 0;
//        //值班表
//        if(holidayManager.isHoliday(today)){
//             workDayIndex = 1;
//        }else {
//             workDayIndex = 0;
//        }
//        // 工作日计数器
//        LocalDate cursor = baseDate;
//        // =================================================
//        // 👇 新增：生成未来10天的排班预览表 (跳过节假日)
//        // =================================================
//        List<DutyScheduleItem> schedule = new ArrayList<>();
//        cursor = today; // 从今天开始
//        int count = 0;
//        // 循环直到找到今天
//        while (!cursor.isAfter(today)) {
//            if (!holidayManager.isHoliday(cursor)) {
//                // 只有是工作日才计数
//                workDayIndex++;
//            }
//            cursor = cursor.plusDays(1);
//        }
//
//        // 3. 根据工作日索引计算今天该谁 (取余)
//        int studentIndex = (workDayIndex - 1) % allStudents.size(); // -1 是为了修正取余的索引
//        Student todayDuty = allStudents.get(studentIndex);
//
//        while (count < 10) { // 生成10个排班记录
//            if (!holidayManager.isHoliday(cursor)) {
//                // 如果是工作日，分配值日生
//                int idx = (workDayIndex + count) % allStudents.size();
//                Student student = allStudents.get(idx);
//
//                DutyScheduleItem item = new DutyScheduleItem();
//                item.setDate(cursor);
//                item.setStudentName(student.getName());
//                item.setWorkDay(true); // 标记为工作日
//                schedule.add(item);
//                count++;
//            } else {
//                // 如果是节假日，显示休息
//                DutyScheduleItem item = new DutyScheduleItem();
//                item.setDate(cursor);
//                item.setStudentName("休息");
//                item.setWorkDay(false);
//                schedule.add(item);
//                // 注意：节假日不增加 count，继续找下一天
//            }
//            cursor = cursor.plusDays(1);
//        }
//
//
//
//
//
//
//
//
//
//        //当日值日生
//        String dutyName = "暂无人员";
//        String nextDutyName = "无";
//        boolean isTodayHoliday = holidayManager.isHoliday(today);
//        LocalDate dutyDate = today;
//        if (!allStudents.isEmpty()) {
//            // 从今天开始倒推，找到最近的一个非节假日
//
//            // 循环查找，直到找到一个不是节假日的日期
//            // 这样做保证了：节假日显示的值日生，是节后第一个工作日的值日生
//
//            if (isTodayHoliday) {
//                // 情况A：今天是休息日
//                // 策略：找下一个工作日的值日生（或者你也可以显示“今天休息”）
//
//                // 找到下一个工作日
//                LocalDate nextWorkDay = today.plusDays(1);
//                while (holidayManager.isHoliday(nextWorkDay)) {
//                    nextWorkDay = nextWorkDay.plusDays(1);
//                }
//
//                // 计算下一个工作日该谁值日
//                long days = nextWorkDay.toEpochDay();
//                int index = (int) (days % allStudents.size());
//                dutyName = allStudents.get(index).getName();
//
//                // 告诉前端：实际上这是“未来”某天的值日生
//                dutyDate = nextWorkDay;
//
//            } else {
//                // 情况B：今天是工作日
//                // 直接计算今天的值日生
//                LocalDate nextWorkDay = today.plusDays(1);
//                while (holidayManager.isHoliday(nextWorkDay)) {
//                    nextWorkDay = nextWorkDay.plusDays(1);
//                }
//                long days = today.toEpochDay();
//                int index = (int) (days % allStudents.size());
//                dutyName = allStudents.get(index).getName();
//                if (allStudents.size() > 1) {
//                    // 使用取模运算实现循环：最后一个人的下一位是第一个人
//                    int nextIndex = (index + 1) % allStudents.size();
//                    nextDutyName = allStudents.get(nextIndex).getName();
//                }
//                dutyDate = nextWorkDay;
//                // dutyDate 就是 today
//            }
//
//
//
//        }


        DutyScheduleDTO items = dutyService.generateDutySchedule();

        String dutyName = items.getDutyName();
        List<DutyScheduleItem> schedule = items.getSchedule();
        String nextDutyName = items.getNextDutyName();
        LocalDate dutyDate = items.getNextDutyDate();
        boolean isTodayHoliday = items.isTodayHoliday();







//        model.addAttribute("todayDuty", todayDuty);
        model.addAttribute("schedule", schedule);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("dutyName", dutyName);
        model.addAttribute("nextDutyName", nextDutyName);
        model.addAttribute("dutyDate", dutyDate);
        model.addAttribute("isTodayHoliday", isTodayHoliday);

        return "index";
    }

    // 添加学生
    @PostMapping("/add")
    public String addStudent(@ModelAttribute Student student) {
        studentRepository.save(student);
        return "redirect:/"; // 重定向回首页
    }

    // 删除学生
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return "redirect:/";
    }

    // 这里可以添加“随机抽取值日生”的逻辑
    @GetMapping("/draw")
    public String drawDuty(Model model) {
        var allStudents = studentRepository.findAll();
        if (!allStudents.isEmpty()) {
            // 简单随机
            var randomStudent = allStudents.get((int) (Math.random() * allStudents.size()));
            model.addAttribute("luckyStudent", randomStudent.getName());
        }
        model.addAttribute("students", allStudents);
        model.addAttribute("newStudent", new Student());
        return "index";
    }
}
