package org.example.demo4.org.example.util;

import org.example.demo4.org.example.instance.Holiday;
import org.example.demo4.org.example.interfaces.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public  class HolidayManager {


    private final HolidayRepository holidayRepository;


    // 构造器注入
    public HolidayManager(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    /**
     * 判断某天是否为节假日 (休息日)
     * true = 休息, false = 工作
     */
    public  boolean isHoliday(LocalDate date) {
        // 1. 先查数据库有没有自定义设置
        Optional<Holiday> dbOpt = holidayRepository.findByDate(date);
        if (dbOpt.isPresent()) {
            // 如果数据库里有记录，以数据库为准
            // type=1 是节假日(休息), type=0 是工作日
            return dbOpt.get().getType() == 1;
        }

        // 2. 如果数据库里没有记录，按默认规则：周六周日休息
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}