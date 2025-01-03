package com.sentura.bLow.repository;

import com.sentura.bLow.entity.UserDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthRepository extends JpaRepository<UserDetail,Long> {

    UserDetail findFirstByEmailAndIsActiveTrue(String email);

    UserDetail findByEmail(String email);

    List<UserDetail> findUserByFirstName(String name);

    UserDetail findUserByUserIdAndIsActiveTrue(Long id);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM USERS WHERE ROLE IS NULL")
    int findUserCount();

    @Query(nativeQuery = true, value = "SELECT role, COUNT(*) FROM USERS GROUP BY role")
    List<Object[]> countUsersByRole();

    @Query(nativeQuery = true, value = "SELECT * FROM USERS WHERE USER_STATUS = 'PENDING' ")
    Page<UserDetail> findAllPendingMerchantList(Pageable pageable);
}
