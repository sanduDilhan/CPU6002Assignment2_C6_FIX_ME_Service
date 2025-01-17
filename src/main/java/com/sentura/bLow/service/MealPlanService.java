package com.sentura.bLow.service;

import com.sentura.bLow.dto.*;
import com.sentura.bLow.entity.*;
import com.sentura.bLow.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MealPlanService {

    @Autowired
    private UserMealPlanRepository userMealPlanRepository;
    @Autowired
    private MealPlanDetailRepository mealPlanDetailRepository;
    @Autowired
    private MealPlanRecipeIngriedientRepository mealPlanRecipeIngriedientRepository;
    @Autowired
    private RecipeDetailRepository recipeDetailRepository;
    @Autowired
    private IngredientDetailRepository ingredientDetailRepository;
    @Autowired
    private FavouriteUserRecipeRepository favouriteUserRecipeRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private CustomRecipeIngredientRepository customRecipeIngredientRepository;

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto save(MealPlanDTO mealPlanDTO) throws Exception {

        // remove all the meal plan information if exists before saving new data
        if (mealPlanDTO.getUserMealPlanDTO() != null && mealPlanDTO.getUserMealPlanDTO().getUserMealPlanId() != null) {
            Optional<UserMealPlan> userMealPlanOptional = userMealPlanRepository.findById(mealPlanDTO.getUserMealPlanDTO().getUserMealPlanId());
            if (userMealPlanOptional.isPresent()) {
                for (MealPlanDetail mealPlanDetail: mealPlanDetailRepository.findByUserMealPlan(userMealPlanOptional.get())) {
                    mealPlanRecipeIngriedientRepository.deleteAll(mealPlanRecipeIngriedientRepository.findByMealPlanDetail(mealPlanDetail));
                    mealPlanDetailRepository.delete(mealPlanDetail);
                }
                userMealPlanRepository.delete(userMealPlanOptional.get());
            }
        }

        // save the user meal plan. if it already exists don't create new one. otherwise use the existing one
        UserMealPlan userMealPlan = new UserMealPlan();

        UserDetail userDetail = authRepository.findUserByUserIdAndIsActiveTrue(mealPlanDTO.getUserMealPlanDTO().getUserDetailId());

        userMealPlan.setUserDetail(userDetail);
        userMealPlan.setMealPlanName(mealPlanDTO.getUserMealPlanDTO().getMealPlanName());
        userMealPlan.setIsActive(true);
        userMealPlan = userMealPlanRepository.saveAndFlush(userMealPlan);

        // save the meal plan details relevant to user meal plan. if it already exists don't create new one. otherwise use the existing one
        if (mealPlanDTO.getMealPlanDetailDTOList() != null && !mealPlanDTO.getMealPlanDetailDTOList().isEmpty()) {
            for (MealPlanDetailDTO mealPlanDetailDTO: mealPlanDTO.getMealPlanDetailDTOList()) {

                MealPlanDetail mealPlanDetail = new MealPlanDetail();
                mealPlanDetail.setUserMealPlan(userMealPlan);
                mealPlanDetail.setDayName(mealPlanDetailDTO.getDayName());
                mealPlanDetail = mealPlanDetailRepository.saveAndFlush(mealPlanDetail);

                // save the meal plan recipe or ingredients or favourites. if it already exists don't create new one. otherwise use the existing one
                if (mealPlanDetailDTO.getMealPlanRecipeIngriedientDTOList() != null && !mealPlanDetailDTO.getMealPlanRecipeIngriedientDTOList().isEmpty()) {
                    for (MealPlanRecipeIngriedientDTO mealPlanRecipeIngriedientDTO: mealPlanDetailDTO.getMealPlanRecipeIngriedientDTOList()) {

                        MealPlanRecipeIngriedient mealPlanRecipeIngriedient = new MealPlanRecipeIngriedient();
                        mealPlanRecipeIngriedient.setMealPlanDetail(mealPlanDetail);
                        mealPlanRecipeIngriedient.setMealSession(mealPlanRecipeIngriedientDTO.getMealSession());
                        mealPlanRecipeIngriedient.setServingCount(mealPlanRecipeIngriedientDTO.getServingCount());
                        mealPlanRecipeIngriedient.setRecipeDetail(null);
                        mealPlanRecipeIngriedient.setFavouriteUserRecipe(null);
                        mealPlanRecipeIngriedient.setIngredientDetail(null);

                        if (mealPlanRecipeIngriedientDTO.getRecipeDetailDTO() != null && mealPlanRecipeIngriedientDTO.getRecipeDetailDTO().getRecipeDetailId() != null) {
                            Optional<RecipeDetail> recipeDetailOptional = recipeDetailRepository.findById(mealPlanRecipeIngriedientDTO.getRecipeDetailDTO().getRecipeDetailId());
                            recipeDetailOptional.ifPresent(mealPlanRecipeIngriedient::setRecipeDetail);
                        }

                        if (mealPlanRecipeIngriedientDTO.getIngredientDetailDTO() != null && mealPlanRecipeIngriedientDTO.getIngredientDetailDTO().getIngredientDetailId() != null) {
                            Optional<IngredientDetail> ingredientDetailOptional = ingredientDetailRepository.findById(mealPlanRecipeIngriedientDTO.getIngredientDetailDTO().getIngredientDetailId());
                            ingredientDetailOptional.ifPresent(mealPlanRecipeIngriedient::setIngredientDetail);
                        }

                        if (mealPlanRecipeIngriedientDTO.getFavouriteUserRecipeDTO() != null && mealPlanRecipeIngriedientDTO.getFavouriteUserRecipeDTO().getFavouriteUserRecipeId() != null) {
                            Optional<FavouriteUserRecipe> favouriteUserRecipeOptional = favouriteUserRecipeRepository.findById(mealPlanRecipeIngriedientDTO.getFavouriteUserRecipeDTO().getFavouriteUserRecipeId());
                            favouriteUserRecipeOptional.ifPresent(mealPlanRecipeIngriedient::setFavouriteUserRecipe);
                        }

                        mealPlanRecipeIngriedientRepository.save(mealPlanRecipeIngriedient);
                    }
                }
            }
        }

        return new ResponseDto("success", "200", userMealPlan.getUserMealPlanId());
    }

    public ResponseDto get(long mealPlanId) throws Exception {

        Optional<UserMealPlan> userMealPlanOptional = userMealPlanRepository.findById(mealPlanId);
        if (userMealPlanOptional.isPresent()) {
            MealPlanDTO mealPlanDTO = new MealPlanDTO();

            UserMealPlanDTO userMealPlanDTO = new UserMealPlanDTO();
            userMealPlanDTO.setUserMealPlanId(userMealPlanOptional.get().getUserMealPlanId());
            userMealPlanDTO.setMealPlanName(userMealPlanOptional.get().getMealPlanName());
            userMealPlanDTO.setIsActive(userMealPlanOptional.get().getIsActive());
            userMealPlanDTO.setUserDetailId(userMealPlanOptional.get().getUserDetail().getUserId());

            List<MealPlanDetailDTO> mealPlanDetailDTOList = new ArrayList<>();
            List<MealPlanDetail> mealPlanDetailList = mealPlanDetailRepository.findByUserMealPlan(userMealPlanOptional.get());

            for (MealPlanDetail mealPlanDetail: mealPlanDetailList) {

                MealPlanDetailDTO mealPlanDetailDTO = new MealPlanDetailDTO();

                mealPlanDetailDTO.setMealPlanDetailId(mealPlanDetail.getMealPlanDetailId());
                mealPlanDetailDTO.setDayName(mealPlanDetail.getDayName());

                List<MealPlanRecipeIngriedientDTO> mealPlanRecipeIngriedientDTOList = new ArrayList<>();
                List<MealPlanRecipeIngriedient> mealPlanRecipeIngriedients = mealPlanRecipeIngriedientRepository.findByMealPlanDetail(mealPlanDetail);

                for (MealPlanRecipeIngriedient mealPlanRecipeIngriedient: mealPlanRecipeIngriedients) {

                    MealPlanRecipeIngriedientDTO mealPlanRecipeIngriedientDTO = new MealPlanRecipeIngriedientDTO();

                    mealPlanRecipeIngriedientDTO.setMealSession(mealPlanRecipeIngriedient.getMealSession());
                    mealPlanRecipeIngriedientDTO.setMealPlanRecipeIngriedientId(mealPlanRecipeIngriedient.getMealPlanRecipeIngriedientId());
                    mealPlanRecipeIngriedientDTO.setServingCount(mealPlanRecipeIngriedient.getServingCount());

                    if (mealPlanRecipeIngriedient.getRecipeDetail() != null) {
                        mealPlanRecipeIngriedientDTO.setRecipeDetailDTO(convertEntityToRecipeDetailDTO(mealPlanRecipeIngriedient.getRecipeDetail()));
                        if (mealPlanRecipeIngriedient.getRecipeDetail().getIsUserCustomRecipe()){
                            List<CustomRecipeIngredient> customRecipeIngredientList = customRecipeIngredientRepository.getAllByCustomRecipeId(mealPlanRecipeIngriedient.getRecipeDetail().getRecipeDetailId());
                            List<CustomRecipeIngredientDTO> customRecipeIngredientDTOList = new ArrayList<>();
                            for (CustomRecipeIngredient customRecipeIngredient: customRecipeIngredientList){
                                CustomRecipeIngredientDTO customRecipeIngredientDTO = new CustomRecipeIngredientDTO();
                                customRecipeIngredientDTO.setCustomRecipeIngredientId(customRecipeIngredient.getCustomRecipeIngredientId());
                                customRecipeIngredientDTO.setIngredient(customRecipeIngredient.getIngredient());
                                customRecipeIngredientDTO.setMeasurementType(customRecipeIngredient.getMeasurementType());
                                customRecipeIngredientDTO.setMeasurement(customRecipeIngredient.getMeasurement());
                                customRecipeIngredientDTO.setIsActive(customRecipeIngredient.getIsActive());
                                customRecipeIngredientDTO.setCreateDate(customRecipeIngredient.getCreateDate());

                                customRecipeIngredientDTOList.add(customRecipeIngredientDTO);
                            }
                            mealPlanRecipeIngriedientDTO.setCustomRecipeIngredientDTOList(customRecipeIngredientDTOList);
                        }
                    }

                    if (mealPlanRecipeIngriedient.getFavouriteUserRecipe() != null) {
                        FavouriteUserRecipeDTO favouriteUserRecipeDTO = new FavouriteUserRecipeDTO();
                        favouriteUserRecipeDTO.setServingCount(mealPlanRecipeIngriedient.getFavouriteUserRecipe().getServingCount());
                        favouriteUserRecipeDTO.setCarbsCount(mealPlanRecipeIngriedient.getFavouriteUserRecipe().getCarbsCount());
                        favouriteUserRecipeDTO.setFavouriteUserRecipeId(mealPlanRecipeIngriedient.getFavouriteUserRecipe().getFavouriteUserRecipeId());
                        favouriteUserRecipeDTO.setRecipeDetail(mealPlanRecipeIngriedient.getFavouriteUserRecipe().getRecipeDetail());
                        if (mealPlanRecipeIngriedient.getFavouriteUserRecipe().getEditFavouriteRecipeName() != null){
                            mealPlanRecipeIngriedient.getFavouriteUserRecipe().getRecipeDetail().setRecipeName(mealPlanRecipeIngriedient.getFavouriteUserRecipe().getEditFavouriteRecipeName());
                        }

                        favouriteUserRecipeDTO.setIngredientDetail(mealPlanRecipeIngriedient.getFavouriteUserRecipe().getIngredientDetail());

                        if (mealPlanRecipeIngriedient.getFavouriteUserRecipe().getRecipeDetail() != null){
                            if (mealPlanRecipeIngriedient.getFavouriteUserRecipe().getRecipeDetail().getIsUserCustomRecipe()){
                                favouriteUserRecipeDTO.getRecipeDetail().setUserDetail(null);
                                List<CustomRecipeIngredient> customRecipeIngredientList = customRecipeIngredientRepository.getAllByCustomRecipeId(mealPlanRecipeIngriedient.getFavouriteUserRecipe().getRecipeDetail().getRecipeDetailId());
                                List<CustomRecipeIngredientDTO> customRecipeIngredientDTOList = new ArrayList<>();
                                for (CustomRecipeIngredient customRecipeIngredient: customRecipeIngredientList){
                                    CustomRecipeIngredientDTO customRecipeIngredientDTO = new CustomRecipeIngredientDTO();
                                    customRecipeIngredientDTO.setCustomRecipeIngredientId(customRecipeIngredient.getCustomRecipeIngredientId());
                                    customRecipeIngredientDTO.setIngredient(customRecipeIngredient.getIngredient());
                                    customRecipeIngredientDTO.setMeasurementType(customRecipeIngredient.getMeasurementType());
                                    customRecipeIngredientDTO.setMeasurement(customRecipeIngredient.getMeasurement());
                                    customRecipeIngredientDTO.setIsActive(customRecipeIngredient.getIsActive());
                                    customRecipeIngredientDTO.setCreateDate(customRecipeIngredient.getCreateDate());

                                    customRecipeIngredientDTOList.add(customRecipeIngredientDTO);
                                }
                                mealPlanRecipeIngriedientDTO.setCustomRecipeIngredientDTOList(customRecipeIngredientDTOList);
                            }
                        }

                        mealPlanRecipeIngriedientDTO.setFavouriteUserRecipeDTO(favouriteUserRecipeDTO);
                    }

                    if (mealPlanRecipeIngriedient.getIngredientDetail() != null) {
                        mealPlanRecipeIngriedientDTO.setIngredientDetailDTO(convertEntityToIngredientDetailDTO(mealPlanRecipeIngriedient.getIngredientDetail()));
                    }

                    mealPlanRecipeIngriedientDTOList.add(mealPlanRecipeIngriedientDTO);
                }

                mealPlanDetailDTO.setMealPlanRecipeIngriedientDTOList(mealPlanRecipeIngriedientDTOList);
                mealPlanDetailDTOList.add(mealPlanDetailDTO);
            }

            mealPlanDTO.setUserMealPlanDTO(userMealPlanDTO);
            mealPlanDTO.setMealPlanDetailDTOList(mealPlanDetailDTOList);

            return new ResponseDto("success", "200", mealPlanDTO);
        } else {
            return new ResponseDto("error", "500", "Meal plan not found!");
        }
    }

    public ResponseDto getAll(long userDetailId) throws Exception {

        UserDetail userDetail = authRepository.findUserByUserIdAndIsActiveTrue(userDetailId);
        if (userDetail != null) {
            List<UserMealPlan> userMealPlanList = userMealPlanRepository.findByUserDetailAndIsActiveTrue(userDetail);
            List<UserMealPlanDTO> userMealPlanDTOList = new ArrayList<>();
            for (UserMealPlan userMealPlan: userMealPlanList) {

                UserMealPlanDTO userMealPlanDTO = new UserMealPlanDTO();
                userMealPlanDTO.setUserMealPlanId(userMealPlan.getUserMealPlanId());
                userMealPlanDTO.setMealPlanName(userMealPlan.getMealPlanName());

                userMealPlanDTOList.add(userMealPlanDTO);
            }
            return new ResponseDto("success", "200", userMealPlanDTOList);
        } else {
            return new ResponseDto("error", "500", "User not found!");
        }
    }

    public ResponseDto updateMealPlanName(long userMealPlanId, String mealPlanName) throws Exception {

       UserMealPlan userMealPlan = userMealPlanRepository.findById(userMealPlanId).get();
       if(userMealPlan != null){
           userMealPlan.setMealPlanName(mealPlanName);
           userMealPlanRepository.save(userMealPlan);
           return new ResponseDto("success", "200", null);
       }else{
           return new ResponseDto("error", "500", "Meal plan not found!");
       }
    }

    public ResponseDto deleteMealPlan(long userMealPlanId) throws Exception {

        UserMealPlan userMealPlan = userMealPlanRepository.findById(userMealPlanId).get();
        if(userMealPlan != null){
            userMealPlan.setIsActive(false);
            userMealPlanRepository.save(userMealPlan);
            return new ResponseDto("success", "200", null);
        }else{
            return new ResponseDto("error", "500", "Meal plan not found!");
        }
    }

    private RecipeDetailDTO convertEntityToRecipeDetailDTO(RecipeDetail recipeDetail) {

        RecipeDetailDTO recipeDetailDTO = new RecipeDetailDTO();
        recipeDetailDTO.setServingCount(recipeDetail.getServingCount());
        recipeDetailDTO.setCarbsPerServe(recipeDetail.getCarbsPerServe());
        recipeDetailDTO.setRecipeDetailId(recipeDetail.getRecipeDetailId());
        recipeDetailDTO.setIsUserCustomRecipe(recipeDetail.getIsUserCustomRecipe());
        recipeDetailDTO.setImageUrl(recipeDetail.getImageUrl());
        recipeDetailDTO.setRecipeName(recipeDetail.getRecipeName());
        recipeDetailDTO.setCustomRecipeDescription(recipeDetail.getCustomRecipeDescription());

        return recipeDetailDTO;
    }

    private IngredientDetailDTO convertEntityToIngredientDetailDTO(IngredientDetail ingredientDetail) {

        IngredientDetailDTO ingredientDetailDTO = new IngredientDetailDTO();
        ingredientDetailDTO.setIngredient(ingredientDetail.getIngredient());
        ingredientDetailDTO.setIngredientDetailId(ingredientDetail.getIngredientDetailId());
        ingredientDetailDTO.setBrand(ingredientDetail.getBrand());
        ingredientDetailDTO.setCarbs(ingredientDetail.getCarbs());
        ingredientDetailDTO.setMeasurement(ingredientDetail.getMeasurement());
        ingredientDetailDTO.setMeasurementType(ingredientDetail.getMeasurementType());
        ingredientDetailDTO.setTspOrQty(ingredientDetail.getTspOrQty());
        ingredientDetailDTO.setSubTitle(ingredientDetail.getSubTitle());
        ingredientDetailDTO.setExternalRecipe(ingredientDetail.getExternalRecipe());

        return ingredientDetailDTO;
    }
}
