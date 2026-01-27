package org.example.demo4.org.example.controller;

import org.example.demo4.org.example.instance.Student;
import org.example.demo4.org.example.interfaces.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DutyController {

    @Autowired
    private StudentRepository studentRepository;

    // 首页：查询所有学生并返回到页面
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("newStudent", new Student()); // 用于表单提交
        return "index"; // 返回 templates 目录下的 index.html
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
