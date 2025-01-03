package com.sentura.bLow.repository;

import com.sentura.bLow.entity.PackageDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PackageDetailRepository extends JpaRepository<PackageDetail, Long> {

    @Query(nativeQuery = true, value = "select * from package_detail where isActive=true and isTrial=false and packageName like %:packageName% ")
    Page<PackageDetail> queryAllActivePackagesByName(Pageable paging, @Param("packageName") String packageName);

    @Query(nativeQuery = true, value = "select * from package_detail where isActive=true and isTrial=false")
    Page<PackageDetail> queryAllActivePackages(Pageable paging);

    @Query(nativeQuery = true, value = "select * from package_detail where isActive=false and isTrial=false and packageName like %:packageName% ")
    Page<PackageDetail> queryAllDeActivePackagesByName(Pageable paging, @Param("packageName") String packageName);

    @Query(nativeQuery = true, value = "select * from package_detail where isActive=false and isTrial=false")
    Page<PackageDetail> queryAllDeActivePackages(Pageable paging);

    @Query(nativeQuery = true, value = "select * from package_detail where isTrial=true")
    PackageDetail queryTrialPackage();

    PackageDetail findByPackageIdAndIsActiveTrue(long packageId);

    PackageDetail findByPackageIdAndIsActiveFalse(long packageId);
}
