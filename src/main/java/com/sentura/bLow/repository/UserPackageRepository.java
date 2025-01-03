package com.sentura.bLow.repository;

import com.sentura.bLow.entity.PackageDetail;
import com.sentura.bLow.entity.UserDetail;
import com.sentura.bLow.entity.UserPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface UserPackageRepository extends JpaRepository<UserPackage, Long> {

    List<UserPackage> findByUserDetailAndIsActiveTrue(UserDetail userDetail);
    Page<UserPackage> findByPackageDetailAndIsActiveTrue(Pageable paging, PackageDetail packageDetail);
    UserPackage findFirstByUserDetailAndIsActiveTrue(UserDetail userDetail);

    @Query(nativeQuery = true, value = "SELECT SUM(packagePrice) FROM user_package where YEAR(CURDATE())=YEAR(createDateTime) AND MONTH(CURDATE())=MONTH(createDateTime)")
    Double monthlyRevenue();

    @Query(nativeQuery = true, value = "select count(*) from user_package where isActive=true")
    Integer activeSubscription();

//    @Query(nativeQuery = true, value = "SELECT\n" +
//            "    DAYNAME(createDateTime) AS day_of_week,\n" +
//            "    SUM(packagePrice) AS record_count\n" +
//            "FROM\n" +
//            "    user_package\n" +
//            "WHERE\n" +
//            "    YEARWEEK(createDateTime) = YEARWEEK(CURDATE())\n" +
//            "GROUP BY\n" +
//            "    DAYOFWEEK(createDateTime)\n" +
//            "ORDER BY\n" +
//            "    DAYOFWEEK(createDateTime)")
//    Map<String, Double> weeklyRevenueChart();

    @Query(nativeQuery = true, value = "select * from user_package where userDetail_userId=:userId AND isActive=true")
    UserPackage getPackageByUserId(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = "select * from user_detail ud, user_package up where up.userDetail_userId=ud.userId AND ud.isAdminCreate=true")
    List<UserPackage> getAdminCreateUsers();
}
