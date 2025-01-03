package com.sentura.bLow.repository;

import com.sentura.bLow.entity.IngredientDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngredientDetailRepository extends JpaRepository<IngredientDetail, Long> {

    List<IngredientDetail> findByIsHideFalse();
    List<IngredientDetail> findByIsHideFalseOrderByIngredientAsc();

    @Query(nativeQuery = true, value = "select * from ingredient_detail order by ingredient, brand ASC")
    List<IngredientDetail> getAllOrderByIngredientName();

    @Query(nativeQuery = true, value = "select * from ingredient_detail order by ingredient, brand ASC")
    Page<IngredientDetail> getAllOrderByIngredientNameWithPagination(Pageable paging);

    @Query(nativeQuery = true, value = "select * from ingredient_detail where ingredient like %:searchRec% order by ingredient, brand ASC")
    Page<IngredientDetail> getAllByIngredientNameWithPagination(Pageable paging, @Param("searchRec") String searchRec);
}
