package com.sentura.bLow.repository;

import com.sentura.bLow.entity.TargetWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TargetWeightRepository extends JpaRepository<TargetWeight, Long> {

    @Query(nativeQuery = true, value = "select * from target_weight where isActive=true and isTargetWeight=true and userDetail_userId=:userId")
    TargetWeight getActiveTargetWeightByUserId(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = "select * from target_weight where isActive=true and isTargetWeight=false and userDetail_userId=:userId")
    List<TargetWeight> getActualWeightByUserId(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = "select * from target_weight where isActive=true and userDetail_userId=:userId")
    List<TargetWeight> getActiveWeightByUserId(@Param("userId") Long userId);
}
