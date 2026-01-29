package org.example.demo4.org.example.service;

import org.example.demo4.org.example.instance.Holiday;
import org.example.demo4.org.example.interfaces.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    /**
     * 切换日期状态（休息日/工作日）
     * @param date 日期
     * @return true表示设置为休息日，false表示设置为工作日
     */
    public boolean toggleHolidayStatus(LocalDate date,Boolean isHoliday) {
        // 检查日期是否已存在
        Optional<Holiday> optionalHoliday = holidayRepository.findByDate(date);

        if (isHoliday) {
            // 日期已存在，删除（设置为工作日）

            if(optionalHoliday.isEmpty()) {
                Holiday holiday = new Holiday();
                holiday.setDate(date);
                holiday.setType(0);
                holidayRepository.save(holiday);
            }else{
                holidayRepository.delete(optionalHoliday.get());
                Holiday holiday = new Holiday();
                holiday.setDate(date);
                holiday.setType(0);
                holidayRepository.save(holiday);
            }

            return false; // 表示设置为工作日
        } else {
            // 日期不存在，创建（设置为休息日）
            if(optionalHoliday.isEmpty()) {
                Holiday holiday = new Holiday();
                holiday.setDate(date);
                holiday.setType(1);
                holidayRepository.save(holiday);
            }else{
                holidayRepository.delete(optionalHoliday.get());
                Holiday holiday = new Holiday();
                holiday.setDate(date);
                holiday.setType(1);
                holidayRepository.save(holiday);
            }

            return true; // 表示设置为休息日
        }
    }
}