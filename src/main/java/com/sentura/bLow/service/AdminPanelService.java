package com.sentura.bLow.service;

import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.repository.RecipeDetailRepository;
import com.sentura.bLow.repository.UserDetailRepository;
import com.sentura.bLow.repository.UserPackageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AdminPanelService {

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private RecipeDetailRepository recipeDetailRepository;

    @Autowired
    private UserPackageRepository userPackageRepository;

    public ResponseDto dashboardCount() throws Exception {

        Integer userCount = userDetailRepository.activeUserCount();
        Integer recipeCount = recipeDetailRepository.activeRecipeCount();
        Double monthlyRevenue = userPackageRepository.monthlyRevenue();
        Integer activeSubscription = userPackageRepository.activeSubscription();

        Map<String, Object> countMap = new HashMap<>();

        countMap.put("userCount", userCount);
        countMap.put("recipeCount", recipeCount);
        countMap.put("monthlyRevenue", monthlyRevenue);
        countMap.put("activeSubscription", activeSubscription);

        return new ResponseDto("success", "200", countMap);
    }

    public ResponseDto weeklyRevenueDayWise() throws Exception {
//        Map<String, Double> revenueChart = userPackageRepository.weeklyRevenueChart();
//        for (Map.Entry<String, Double> entry : revenueChart.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//        }
//        return new ResponseDto("success", "200", userPackageRepository.weeklyRevenueChart());
        return new ResponseDto();
    }
}
