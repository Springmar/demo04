package org.example.demo4.org.example.interfaces;

import org.example.demo4.org.example.instance.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    // 按日期查找
    Optional<Holiday> findByDate(LocalDate date);

    // 查询某个月的数据 (用于日历展示)
    List<Holiday> findByDateBetween(LocalDate start, LocalDate end);
}
