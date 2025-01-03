package com.sentura.bLow.repository;

import com.sentura.bLow.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    @Query(nativeQuery = true, value = "select count(*) from user_detail where userRole='USER' AND isActive=true")
    Integer activeUserCount();

    @Query(nativeQuery = true, value = "select * from user_detail where userRole='USER'")
    List<UserDetail> getAllUsers();

    @Query(nativeQuery = true, value = "select * from user_detail where userRole='ADMIN'")
    List<UserDetail> getAllAdmin();

}
