package com.sentura.bLow.repository;

import com.sentura.bLow.entity.DailyCarbLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DailyCarbLimitRepository extends JpaRepository<DailyCarbLimit, Long> {

    @Query(nativeQuery = true, value = "select * from daily_carb_limit where userDetail_userId=:userId And isActive=true")
    DailyCarbLimit getByUserId(@Param("userId") Long userId);
}
