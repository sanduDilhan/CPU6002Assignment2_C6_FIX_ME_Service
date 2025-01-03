package com.sentura.bLow.service;

import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import com.sentura.bLow.dto.*;
import com.sentura.bLow.entity.*;
import com.sentura.bLow.repository.*;
import com.sentura.bLow.util.FileUtilizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.util.*;

@Service
@Slf4j
public class RecipeDetailService {

    @Autowired
    private RecipeDetailRepository recipeDetailRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    private RecipeInstructionRepository recipeInstructionRepository;

    @Autowired
    private RecipeNoteRepository recipeNoteRepository;

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Autowired
    private IngredientDetailRepository ingredientDetailRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private MealPlanService mealPlanService;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private AdditionalIngredientRepository additionalIngredientRepository;

    @Autowired
    private CustomRecipeIngredientRepository customRecipeIngredientRepository;

    @Value("${server.file.prefix}")
    private String filePrefix;

    @Value("${archive.path}")
    private String archivePath;

    @Value("${server.url}")
    private String serverUrl;

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto createRecipe(String recipe, MultipartFile recipeImage) throws Exception {

        Gson gson = new Gson();
        RecipeDetailDTO recipeDetailDTO = gson.fromJson(recipe, RecipeDetailDTO.class);

//        RecipeCategory recipeCategory = recipeCategoryRepository.findById(recipeDetailDTO.getRecipeCategoryId()).get();

        RecipeDetail recipeDetail = new RecipeDetail();
        recipeDetail.setRecipeDetailId(0L);
        recipeDetail.setRecipeCategoryId(recipeDetailDTO.getRecipeCategoryId());
        recipeDetail.setRecipeName(recipeDetailDTO.getRecipeName());
        recipeDetail.setPreparationTime(recipeDetailDTO.getPreparationTime());
        recipeDetail.setCookTime(recipeDetailDTO.getCookTime());
        recipeDetail.setServingCount(recipeDetailDTO.getServingCount());
        recipeDetail.setCarbsPerServe(recipeDetailDTO.getCarbsPerServe());
        recipeDetail.setYoutubeVideoUrl(recipeDetailDTO.getYoutubeVideoUrl());
        recipeDetail.setCreateDate(new Date());
        recipeDetail.setIsActive(true);
        recipeDetail.setIsUserCustomRecipe(false);
        recipeDetail.setAdditionalTopic(recipeDetailDTO.getAdditionalTopic());

        if (recipeDetailDTO.getMainIngredientTopic().isEmpty()) {
            recipeDetail.setMainIngredientTopic(null);
        }else{
            recipeDetail.setMainIngredientTopic(recipeDetailDTO.getMainIngredientTopic());
        }

        if (recipeImage == null) {
            recipeDetail.setImageUrl(null);
        } else {
            String[] results = imageUploadService.getResultsOfFileWrite(recipeImage);
            recipeDetail.setImageUrl(results[1]);
        }

        RecipeDetail recipeDetail1 = recipeDetailRepository.save(recipeDetail);

        for (RecipeIngredientDTO recipeIngredientDTO : recipeDetailDTO.getRecipeIngredientDTOList()) {
            IngredientDetail ingredientDetail = ingredientDetailRepository.findById(recipeIngredientDTO.getIngredientDetailId()).get();

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipeIngredientId(0L);
            recipeIngredient.setRecipeDetail(recipeDetail1);
            recipeIngredient.setIngredientDetail(ingredientDetail);
            recipeIngredient.setMeasurement(recipeIngredientDTO.getMeasurement());
            recipeIngredient.setIsActive(true);
            recipeIngredient.setCreateDate(new Date());

            recipeIngredientRepository.save(recipeIngredient);
        }

        for (RecipeInstructionDTO recipeInstructionDTO : recipeDetailDTO.getRecipeInstructionDTOList()) {
            RecipeInstruction recipeInstruction = new RecipeInstruction();
            recipeInstruction.setRecipeInstructionId(0L);
            recipeInstruction.setRecipeDetail(recipeDetail1);
            recipeInstruction.setDescription(recipeInstructionDTO.getDescription());
            recipeInstruction.setCreate_date(new Date());

            recipeInstructionRepository.save(recipeInstruction);
        }

        for (RecipeNoteDTO recipeNoteDTO : recipeDetailDTO.getRecipeNoteDTOList()) {
            RecipeNote recipeNote = new RecipeNote();
            recipeNote.setRecipeNoteId(0L);
            recipeNote.setRecipeDetail(recipeDetail1);
            recipeNote.setDescription(recipeNoteDTO.getDescription());
            recipeNote.setCreate_date(new Date());

            recipeNoteRepository.save(recipeNote);
        }

        for (AdditionalIngredientDTO additionalIngredientDTO : recipeDetailDTO.getAdditionalIngredientDTOList()) {
            IngredientDetail ingredientDetail = ingredientDetailRepository.findById(additionalIngredientDTO.getIngredientDetailId()).get();
            AdditionalIngredient additionalIngredient = new AdditionalIngredient();
            additionalIngredient.setAdditionalIngredientId(0L);
            additionalIngredient.setRecipeDetail(recipeDetail1);
            additionalIngredient.setIngredientDetail(ingredientDetail);
            additionalIngredient.setMeasurement(additionalIngredientDTO.getMeasurement());
            additionalIngredient.setIsActive(true);

            additionalIngredientRepository.save(additionalIngredient);
        }

        return new ResponseDto("success", "200", null);
    }

    public ResponseDto getAllRecipeDetails() throws Exception {
        List<RecipeDetail> recipeDetailList = recipeDetailRepository.findAllByIsUserCustomRecipeFalseAndIsActiveTrue();
        List<RecipeDetailDTO> recipeDetailDTOList = new ArrayList<>();

        for (RecipeDetail recipeDetail : recipeDetailList) {
            List<AdditionalIngredientDTO> additionalIngredientDTOList = new ArrayList<>();
            List<RecipeIngredientDTO> recipeIngredientDTOList = new ArrayList<>();
            List<RecipeInstructionDTO> recipeInstructionDTOList = new ArrayList<>();
            List<RecipeNoteDTO> recipeNoteDTOList = new ArrayList<>();

            RecipeDetailDTO recipeDetailDTO = new RecipeDetailDTO();
            recipeDetailDTO.setRecipeDetailId(recipeDetail.getRecipeDetailId());
//            recipeDetailDTO.setRecipeCategory(recipeDetail.getRecipeCategory());
            recipeDetailDTO.setRecipeName(recipeDetail.getRecipeName());
            recipeDetailDTO.setPreparationTime(recipeDetail.getPreparationTime());
            recipeDetailDTO.setCookTime(recipeDetail.getCookTime());
            recipeDetailDTO.setServingCount(recipeDetail.getServingCount());
            recipeDetailDTO.setCarbsPerServe(recipeDetail.getCarbsPerServe());
            recipeDetailDTO.setImageUrl(recipeDetail.getImageUrl());
            recipeDetailDTO.setYoutubeVideoUrl(recipeDetail.getYoutubeVideoUrl());
            recipeDetailDTO.setCreateDate(recipeDetail.getCreateDate());
            recipeDetailDTO.setIsActive(recipeDetail.getIsActive());
            recipeDetailDTO.setAdditionalTopic(recipeDetail.getAdditionalTopic());
            recipeDetailDTO.setMainIngredientTopic(recipeDetail.getMainIngredientTopic());

            List<AdditionalIngredient> additionalIngredientList = additionalIngredientRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (AdditionalIngredient additionalIngredient : additionalIngredientList) {
                AdditionalIngredientDTO additionalIngredientDTO = new AdditionalIngredientDTO();
                additionalIngredientDTO.setAdditionalIngredientId(additionalIngredient.getAdditionalIngredientId());
                additionalIngredientDTO.setMeasurement(additionalIngredient.getMeasurement());
                additionalIngredientDTO.setIsActive(additionalIngredient.getIsActive());
                additionalIngredientDTO.setCreateDate(additionalIngredient.getCreateDate());
                additionalIngredientDTO.setIngredientDetail(additionalIngredient.getIngredientDetail());

                additionalIngredientDTOList.add(additionalIngredientDTO);
            }

            List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeIngredient recipeIngredient : recipeIngredientList) {
                RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();
                recipeIngredientDTO.setIngredientDetailId(recipeIngredient.getRecipeIngredientId());
                recipeIngredientDTO.setRecipeDetailId(null);
                recipeIngredientDTO.setIngredientDetailId(null);
                recipeIngredientDTO.setMeasurement(recipeIngredient.getMeasurement());
                recipeIngredientDTO.setIsActive(recipeIngredient.getIsActive());
                recipeIngredientDTO.setCreateDate(recipeIngredient.getCreateDate());
                recipeIngredientDTO.setIngredientDetail(recipeIngredient.getIngredientDetail());

                recipeIngredientDTOList.add(recipeIngredientDTO);
            }

            List<RecipeInstruction> recipeInstructionList = recipeInstructionRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeInstruction recipeInstruction : recipeInstructionList) {
                RecipeInstructionDTO recipeInstructionDTO = new RecipeInstructionDTO();
                recipeInstructionDTO.setRecipeInstructionId(recipeInstruction.getRecipeInstructionId());
                recipeInstructionDTO.setRecipeDetailId(null);
                recipeInstructionDTO.setDescription(recipeInstruction.getDescription());
                recipeInstructionDTO.setCreate_date(recipeInstruction.getCreate_date());

                recipeInstructionDTOList.add(recipeInstructionDTO);
            }

            List<RecipeNote> recipeNoteList = recipeNoteRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeNote recipeNote : recipeNoteList) {
                RecipeNoteDTO recipeNoteDTO = new RecipeNoteDTO();
                recipeNoteDTO.setRecipeNoteId(recipeNote.getRecipeNoteId());
                recipeNoteDTO.setRecipeDetailId(null);
                recipeNoteDTO.setDescription(recipeNote.getDescription());
                recipeNoteDTO.setCreate_date(recipeNote.getCreate_date());

                recipeNoteDTOList.add(recipeNoteDTO);
            }
            recipeDetailDTO.setAdditionalIngredientDTOList(additionalIngredientDTOList);
            recipeDetailDTO.setRecipeIngredientDTOList(recipeIngredientDTOList);
            recipeDetailDTO.setRecipeInstructionDTOList(recipeInstructionDTOList);
            recipeDetailDTO.setRecipeNoteDTOList(recipeNoteDTOList);

            recipeDetailDTOList.add(recipeDetailDTO);
        }

        return new ResponseDto("success", "200", recipeDetailDTOList);
    }

    public ResponseDto getRecipeDetailsFindById(Long recipeId) throws Exception {
        RecipeDetail recipeDetail = recipeDetailRepository.findById(recipeId).get();

        List<AdditionalIngredientDTO> additionalIngredientDTOList = new ArrayList<>();
        List<RecipeIngredientDTO> recipeIngredientDTOList = new ArrayList<>();
        List<RecipeInstructionDTO> recipeInstructionDTOList = new ArrayList<>();
        List<RecipeNoteDTO> recipeNoteDTOList = new ArrayList<>();
        List<RecipeCategory> recipeCategoryList = new ArrayList<>();

        List<String> categoryIdList = Arrays.asList(recipeDetail.getRecipeCategoryId().split(","));
        for (String categoryId : categoryIdList) {
            RecipeCategory recipeCategory = recipeCategoryRepository.findById(Long.parseLong(categoryId)).get();
            recipeCategoryList.add(recipeCategory);
        }

        RecipeDetailDTO recipeDetailDTO = new RecipeDetailDTO();
        recipeDetailDTO.setRecipeDetailId(recipeDetail.getRecipeDetailId());
        recipeDetailDTO.setRecipeCategoryList(recipeCategoryList);
        recipeDetailDTO.setRecipeName(recipeDetail.getRecipeName());
        recipeDetailDTO.setPreparationTime(recipeDetail.getPreparationTime());
        recipeDetailDTO.setCookTime(recipeDetail.getCookTime());
        recipeDetailDTO.setServingCount(recipeDetail.getServingCount());
        recipeDetailDTO.setCarbsPerServe(recipeDetail.getCarbsPerServe());
        recipeDetailDTO.setImageUrl(recipeDetail.getImageUrl());
        recipeDetailDTO.setYoutubeVideoUrl(recipeDetail.getYoutubeVideoUrl());
        recipeDetailDTO.setCreateDate(recipeDetail.getCreateDate());
        recipeDetailDTO.setIsActive(recipeDetail.getIsActive());
        recipeDetailDTO.setAdditionalTopic(recipeDetail.getAdditionalTopic());
        recipeDetailDTO.setMainIngredientTopic(recipeDetail.getMainIngredientTopic());

        List<AdditionalIngredient> additionalIngredientList = additionalIngredientRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
        for (AdditionalIngredient additionalIngredient : additionalIngredientList) {
            AdditionalIngredientDTO additionalIngredientDTO = new AdditionalIngredientDTO();
            additionalIngredientDTO.setAdditionalIngredientId(additionalIngredient.getAdditionalIngredientId());
            additionalIngredientDTO.setMeasurement(additionalIngredient.getMeasurement());
            additionalIngredientDTO.setIsActive(additionalIngredient.getIsActive());
            additionalIngredientDTO.setCreateDate(additionalIngredient.getCreateDate());
            additionalIngredientDTO.setIngredientDetail(additionalIngredient.getIngredientDetail());

            additionalIngredientDTOList.add(additionalIngredientDTO);
        }

        List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
        for (RecipeIngredient recipeIngredient : recipeIngredientList) {
            RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();
            recipeIngredientDTO.setIngredientDetailId(recipeIngredient.getRecipeIngredientId());
            recipeIngredientDTO.setRecipeDetailId(null);
            recipeIngredientDTO.setIngredientDetailId(null);
            recipeIngredientDTO.setMeasurement(recipeIngredient.getMeasurement());
            recipeIngredientDTO.setIsActive(recipeIngredient.getIsActive());
            recipeIngredientDTO.setCreateDate(recipeIngredient.getCreateDate());
            recipeIngredientDTO.setIngredientDetail(recipeIngredient.getIngredientDetail());

            recipeIngredientDTOList.add(recipeIngredientDTO);
        }

        List<RecipeInstruction> recipeInstructionList = recipeInstructionRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
        for (RecipeInstruction recipeInstruction : recipeInstructionList) {
            RecipeInstructionDTO recipeInstructionDTO = new RecipeInstructionDTO();
            recipeInstructionDTO.setRecipeInstructionId(recipeInstruction.getRecipeInstructionId());
            recipeInstructionDTO.setRecipeDetailId(null);
            recipeInstructionDTO.setDescription(recipeInstruction.getDescription());
            recipeInstructionDTO.setCreate_date(recipeInstruction.getCreate_date());

            recipeInstructionDTOList.add(recipeInstructionDTO);
        }

        List<RecipeNote> recipeNoteList = recipeNoteRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
        for (RecipeNote recipeNote : recipeNoteList) {
            RecipeNoteDTO recipeNoteDTO = new RecipeNoteDTO();
            recipeNoteDTO.setRecipeNoteId(recipeNote.getRecipeNoteId());
            recipeNoteDTO.setRecipeDetailId(null);
            recipeNoteDTO.setDescription(recipeNote.getDescription());
            recipeNoteDTO.setCreate_date(recipeNote.getCreate_date());

            recipeNoteDTOList.add(recipeNoteDTO);
        }
        recipeDetailDTO.setAdditionalIngredientDTOList(additionalIngredientDTOList);
        recipeDetailDTO.setRecipeIngredientDTOList(recipeIngredientDTOList);
        recipeDetailDTO.setRecipeInstructionDTOList(recipeInstructionDTOList);
        recipeDetailDTO.setRecipeNoteDTOList(recipeNoteDTOList);

        return new ResponseDto("success", "200", recipeDetailDTO);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto updateRecipe(String recipe, MultipartFile recipeImage) throws Exception {

        Gson gson = new Gson();
        RecipeDetailDTO recipeDetailDTO = gson.fromJson(recipe, RecipeDetailDTO.class);

        recipeIngredientRepository.deleteByRecipeId(recipeDetailDTO.getRecipeDetailId());
        additionalIngredientRepository.deleteByRecipeId(recipeDetailDTO.getRecipeDetailId());
        recipeInstructionRepository.deleteByRecipeId(recipeDetailDTO.getRecipeDetailId());
        recipeNoteRepository.deleteByRecipeId(recipeDetailDTO.getRecipeDetailId());

        RecipeDetail recDetail = recipeDetailRepository.findById(recipeDetailDTO.getRecipeDetailId()).get();
//        RecipeCategory recipeCategory = recipeCategoryRepository.findById(recipeDetailDTO.getRecipeCategoryId()).get();

        RecipeDetail recipeDetail = new RecipeDetail();
        recipeDetail.setRecipeDetailId(recipeDetailDTO.getRecipeDetailId());
        recipeDetail.setRecipeCategoryId(recipeDetailDTO.getRecipeCategoryId());
        recipeDetail.setRecipeName(recipeDetailDTO.getRecipeName());
        recipeDetail.setPreparationTime(recipeDetailDTO.getPreparationTime());
        recipeDetail.setCookTime(recipeDetailDTO.getCookTime());
        recipeDetail.setServingCount(recipeDetailDTO.getServingCount());
        recipeDetail.setCarbsPerServe(recipeDetailDTO.getCarbsPerServe());
        recipeDetail.setYoutubeVideoUrl(recipeDetailDTO.getYoutubeVideoUrl());
        recipeDetail.setCreateDate(new Date());
        recipeDetail.setIsActive(true);
        recipeDetail.setIsUserCustomRecipe(false);
        recipeDetail.setAdditionalTopic(recipeDetailDTO.getAdditionalTopic());

        if (recipeDetailDTO.getMainIngredientTopic().isEmpty()) {
            recipeDetail.setMainIngredientTopic(null);
        }else{
            recipeDetail.setMainIngredientTopic(recipeDetailDTO.getMainIngredientTopic());
        }

        if (recipeImage == null) {
            recipeDetail.setImageUrl(recDetail.getImageUrl());
        } else {
            String[] results = imageUploadService.getResultsOfFileWrite(recipeImage);
            recipeDetail.setImageUrl(results[1]);
        }

        RecipeDetail recipeDetail1 = recipeDetailRepository.save(recipeDetail);

        for (RecipeIngredientDTO recipeIngredientDTO : recipeDetailDTO.getRecipeIngredientDTOList()) {
            IngredientDetail ingredientDetail = ingredientDetailRepository.findById(recipeIngredientDTO.getIngredientDetailId()).get();

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipeIngredientId(0L);
            recipeIngredient.setRecipeDetail(recipeDetail1);
            recipeIngredient.setIngredientDetail(ingredientDetail);
            recipeIngredient.setMeasurement(recipeIngredientDTO.getMeasurement());
            recipeIngredient.setIsActive(true);
            recipeIngredient.setCreateDate(new Date());

            recipeIngredientRepository.save(recipeIngredient);
        }

        for (RecipeInstructionDTO recipeInstructionDTO : recipeDetailDTO.getRecipeInstructionDTOList()) {
            RecipeInstruction recipeInstruction = new RecipeInstruction();
            recipeInstruction.setRecipeInstructionId(0L);
            recipeInstruction.setRecipeDetail(recipeDetail1);
            recipeInstruction.setDescription(recipeInstructionDTO.getDescription());
            recipeInstruction.setCreate_date(new Date());

            recipeInstructionRepository.save(recipeInstruction);
        }

        for (RecipeNoteDTO recipeNoteDTO : recipeDetailDTO.getRecipeNoteDTOList()) {
            RecipeNote recipeNote = new RecipeNote();
            recipeNote.setRecipeNoteId(0L);
            recipeNote.setRecipeDetail(recipeDetail1);
            recipeNote.setDescription(recipeNoteDTO.getDescription());
            recipeNote.setCreate_date(new Date());

            recipeNoteRepository.save(recipeNote);
        }

        for (AdditionalIngredientDTO additionalIngredientDTO : recipeDetailDTO.getAdditionalIngredientDTOList()) {
            IngredientDetail ingredientDetail = ingredientDetailRepository.findById(additionalIngredientDTO.getIngredientDetailId()).get();
            AdditionalIngredient additionalIngredient = new AdditionalIngredient();
            additionalIngredient.setAdditionalIngredientId(0L);
            additionalIngredient.setRecipeDetail(recipeDetail1);
            additionalIngredient.setIngredientDetail(ingredientDetail);
            additionalIngredient.setMeasurement(additionalIngredientDTO.getMeasurement());
            additionalIngredient.setIsActive(true);

            additionalIngredientRepository.save(additionalIngredient);
        }

        return new ResponseDto("success", "200", null);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto createCustomRecipe(String recipe, MultipartFile recipeImage, String mealPlan, String mealPlanDayName, String mealPlanMealSession) throws Exception {

        Gson gson = new Gson();
        RecipeDetailDTO recipeDetailDTO = gson.fromJson(recipe, RecipeDetailDTO.class);
        MealPlanDTO mealPlanDTO = gson.fromJson(mealPlan, MealPlanDTO.class);

//        RecipeCategory recipeCategory = recipeCategoryRepository.findById(recipeDetailDTO.getRecipeCategoryId()).get();
        UserDetail userDetail = userDetailRepository.findById(recipeDetailDTO.getUserDetailId()).get();

        RecipeDetail recipeDetail = new RecipeDetail();
        recipeDetail.setRecipeDetailId(0L);
        recipeDetail.setUserDetail(userDetail);
        recipeDetail.setRecipeName(recipeDetailDTO.getRecipeName());
        recipeDetail.setServingCount(1);
        recipeDetail.setCarbsPerServe(recipeDetailDTO.getCarbsPerServe());
        recipeDetail.setCreateDate(new Date());
        recipeDetail.setIsActive(true);
        recipeDetail.setIsUserCustomRecipe(true);
        recipeDetail.setCustomRecipeDescription(recipeDetailDTO.getCustomRecipeDescription());

        if (recipeImage == null) {
            recipeDetail.setImageUrl(null);
        } else {
            String[] results = imageUploadService.getResultsOfFileWrite(recipeImage);
            recipeDetail.setImageUrl(results[1]);
        }

        RecipeDetail recipeDetail1 = recipeDetailRepository.save(recipeDetail);

        for (CustomRecipeIngredientDTO customRecipeIngredientDTO : recipeDetailDTO.getCustomRecipeIngredientDTOList()) {
            CustomRecipeIngredient customRecipeIngredient = new CustomRecipeIngredient();
            customRecipeIngredient.setCustomRecipeIngredientId(0L);
            customRecipeIngredient.setRecipeDetail(recipeDetail1);
            customRecipeIngredient.setIngredient(customRecipeIngredientDTO.getIngredient());
            customRecipeIngredient.setMeasurementType(customRecipeIngredientDTO.getMeasurementType());
            customRecipeIngredient.setMeasurement(customRecipeIngredientDTO.getMeasurement());
            customRecipeIngredient.setIsActive(true);

            customRecipeIngredientRepository.save(customRecipeIngredient);
        }

        for(MealPlanDetailDTO mealPlanDetailDTO: mealPlanDTO.getMealPlanDetailDTOList()){
            if (mealPlanDetailDTO.getDayName().equals(mealPlanDayName)){
                for (MealPlanRecipeIngriedientDTO mealPlanRecipeIngriedientDTO: mealPlanDetailDTO.getMealPlanRecipeIngriedientDTOList()){
                    if (mealPlanRecipeIngriedientDTO.getMealSession().equals(mealPlanMealSession)){
                        if(mealPlanRecipeIngriedientDTO.getRecipeDetailDTO().getRecipeDetailId() == 0){
                            mealPlanRecipeIngriedientDTO.getRecipeDetailDTO().setRecipeDetailId(recipeDetail1.getRecipeDetailId());
                        }
                    }
                }
            }
        }

        ResponseDto responseDto = mealPlanService.save(mealPlanDTO);
        if (responseDto.getCode().equals("200")) {
            return new ResponseDto("success", "200", responseDto.getResult());
        } else {
            return new ResponseDto("failed", "500", null);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto updateCustomRecipe(String recipe, MultipartFile recipeImage) throws Exception {

        Gson gson = new Gson();
        RecipeDetailDTO recipeDetailDTO = gson.fromJson(recipe, RecipeDetailDTO.class);

        RecipeDetail recipeDetail = recipeDetailRepository.findById(recipeDetailDTO.getRecipeDetailId()).get();
        recipeDetail.setRecipeName(recipeDetailDTO.getRecipeName());
        recipeDetail.setCarbsPerServe(recipeDetailDTO.getCarbsPerServe());
        recipeDetail.setCreateDate(new Date());
        recipeDetail.setCustomRecipeDescription(recipeDetailDTO.getCustomRecipeDescription());

        if (recipeImage != null) {
            String[] results = imageUploadService.getResultsOfFileWrite(recipeImage);
            recipeDetail.setImageUrl(results[1]);
        }

        recipeDetailRepository.save(recipeDetail);

        customRecipeIngredientRepository.deleteByCustomRecipeId(recipeDetail.getRecipeDetailId());

        for (CustomRecipeIngredientDTO customRecipeIngredientDTO : recipeDetailDTO.getCustomRecipeIngredientDTOList()) {
            CustomRecipeIngredient customRecipeIngredient = new CustomRecipeIngredient();
            customRecipeIngredient.setCustomRecipeIngredientId(0L);
            customRecipeIngredient.setRecipeDetail(recipeDetail);
            customRecipeIngredient.setIngredient(customRecipeIngredientDTO.getIngredient());
            customRecipeIngredient.setMeasurementType(customRecipeIngredientDTO.getMeasurementType());
            customRecipeIngredient.setMeasurement(customRecipeIngredientDTO.getMeasurement());
            customRecipeIngredient.setIsActive(true);

            customRecipeIngredientRepository.save(customRecipeIngredient);
        }

        return new ResponseDto("success", "200", null);

    }

    public ResponseDto getCustomRecipeById(Long customRecipeId) throws Exception {
        RecipeDetail recipeDetail = recipeDetailRepository.findById(customRecipeId).get();
        RecipeDetailDTO recipeDetailDTO = new RecipeDetailDTO();
        recipeDetailDTO.setRecipeDetailId(recipeDetail.getRecipeDetailId());
        recipeDetailDTO.setRecipeName(recipeDetail.getRecipeName());
        recipeDetailDTO.setServingCount(recipeDetail.getServingCount());
        recipeDetailDTO.setCarbsPerServe(recipeDetail.getCarbsPerServe());
        recipeDetailDTO.setImageUrl(recipeDetail.getImageUrl());
        recipeDetailDTO.setCreateDate(recipeDetail.getCreateDate());
        recipeDetailDTO.setIsActive(recipeDetail.getIsActive());
        recipeDetailDTO.setIsUserCustomRecipe(recipeDetail.getIsUserCustomRecipe());
        recipeDetailDTO.setCustomRecipeDescription(recipeDetail.getCustomRecipeDescription());

        List<CustomRecipeIngredient> customRecipeIngredientList = customRecipeIngredientRepository.getAllByCustomRecipeId(customRecipeId);
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

        recipeDetailDTO.setCustomRecipeIngredientDTOList(customRecipeIngredientDTOList);

        return new ResponseDto("success", "200", recipeDetailDTO);
    }

    public ResponseDto getAllCustomRecipeByUserId(Long userId) throws Exception {
        List<RecipeDetail> recipeDetailList = recipeDetailRepository.getAllCustomRecipeByUserId(userId);
        List<RecipeDetailDTO> recipeDetailDTOList = new ArrayList<>();

        for (RecipeDetail recipeDetail : recipeDetailList) {
            List<RecipeIngredientDTO> recipeIngredientDTOList = new ArrayList<>();
            List<RecipeInstructionDTO> recipeInstructionDTOList = new ArrayList<>();
            List<RecipeNoteDTO> recipeNoteDTOList = new ArrayList<>();

            RecipeDetailDTO recipeDetailDTO = new RecipeDetailDTO();
            recipeDetailDTO.setRecipeDetailId(recipeDetail.getRecipeDetailId());
//            recipeDetailDTO.setRecipeCategory(recipeDetail.getRecipeCategory());
            recipeDetailDTO.setRecipeName(recipeDetail.getRecipeName());
            recipeDetailDTO.setPreparationTime(recipeDetail.getPreparationTime());
            recipeDetailDTO.setCookTime(recipeDetail.getCookTime());
            recipeDetailDTO.setServingCount(recipeDetail.getServingCount());
            recipeDetailDTO.setCarbsPerServe(recipeDetail.getCarbsPerServe());
            recipeDetailDTO.setImageUrl(recipeDetail.getImageUrl());
            recipeDetailDTO.setYoutubeVideoUrl(recipeDetail.getYoutubeVideoUrl());
            recipeDetailDTO.setCreateDate(recipeDetail.getCreateDate());
            recipeDetailDTO.setIsActive(recipeDetail.getIsActive());
            recipeDetailDTO.setIsUserCustomRecipe(recipeDetail.getIsUserCustomRecipe());

            List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeIngredient recipeIngredient : recipeIngredientList) {
                RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();
                recipeIngredientDTO.setIngredientDetailId(recipeIngredient.getRecipeIngredientId());
                recipeIngredientDTO.setRecipeDetailId(null);
                recipeIngredientDTO.setIngredientDetailId(null);
                recipeIngredientDTO.setMeasurement(recipeIngredient.getMeasurement());
                recipeIngredientDTO.setIsActive(recipeIngredient.getIsActive());
                recipeIngredientDTO.setCreateDate(recipeIngredient.getCreateDate());
                recipeIngredientDTO.setIngredientDetail(recipeIngredient.getIngredientDetail());

                recipeIngredientDTOList.add(recipeIngredientDTO);
            }

            List<RecipeInstruction> recipeInstructionList = recipeInstructionRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeInstruction recipeInstruction : recipeInstructionList) {
                RecipeInstructionDTO recipeInstructionDTO = new RecipeInstructionDTO();
                recipeInstructionDTO.setRecipeInstructionId(recipeInstruction.getRecipeInstructionId());
                recipeInstructionDTO.setRecipeDetailId(null);
                recipeInstructionDTO.setDescription(recipeInstruction.getDescription());
                recipeInstructionDTO.setCreate_date(recipeInstruction.getCreate_date());

                recipeInstructionDTOList.add(recipeInstructionDTO);
            }

            List<RecipeNote> recipeNoteList = recipeNoteRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeNote recipeNote : recipeNoteList) {
                RecipeNoteDTO recipeNoteDTO = new RecipeNoteDTO();
                recipeNoteDTO.setRecipeNoteId(recipeNote.getRecipeNoteId());
                recipeNoteDTO.setRecipeDetailId(null);
                recipeNoteDTO.setDescription(recipeNote.getDescription());
                recipeNoteDTO.setCreate_date(recipeNote.getCreate_date());

                recipeNoteDTOList.add(recipeNoteDTO);
            }
            recipeDetailDTO.setRecipeIngredientDTOList(recipeIngredientDTOList);
            recipeDetailDTO.setRecipeInstructionDTOList(recipeInstructionDTOList);
            recipeDetailDTO.setRecipeNoteDTOList(recipeNoteDTOList);

            recipeDetailDTOList.add(recipeDetailDTO);
        }

        return new ResponseDto("success", "200", recipeDetailDTOList);
    }

    public ResponseDto getAllRecipebyCategoryId(Long categoryId) throws Exception {
        List<RecipeDetail> recipeDetailList = recipeDetailRepository.getAllRecipeBycategoryId(categoryId);
        List<RecipeDetailDTO> recipeDetailDTOList = new ArrayList<>();

        for (RecipeDetail recipeDetail : recipeDetailList) {
            List<AdditionalIngredientDTO> additionalIngredientDTOList = new ArrayList<>();
            List<RecipeIngredientDTO> recipeIngredientDTOList = new ArrayList<>();
            List<RecipeInstructionDTO> recipeInstructionDTOList = new ArrayList<>();
            List<RecipeNoteDTO> recipeNoteDTOList = new ArrayList<>();

            RecipeDetailDTO recipeDetailDTO = new RecipeDetailDTO();
            recipeDetailDTO.setRecipeDetailId(recipeDetail.getRecipeDetailId());
//            recipeDetailDTO.setRecipeCategory(recipeDetail.getRecipeCategory());
            recipeDetailDTO.setRecipeName(recipeDetail.getRecipeName());
            recipeDetailDTO.setPreparationTime(recipeDetail.getPreparationTime());
            recipeDetailDTO.setCookTime(recipeDetail.getCookTime());
            recipeDetailDTO.setServingCount(recipeDetail.getServingCount());
            recipeDetailDTO.setCarbsPerServe(recipeDetail.getCarbsPerServe());
            recipeDetailDTO.setImageUrl(recipeDetail.getImageUrl());
            recipeDetailDTO.setYoutubeVideoUrl(recipeDetail.getYoutubeVideoUrl());
            recipeDetailDTO.setCreateDate(recipeDetail.getCreateDate());
            recipeDetailDTO.setIsActive(recipeDetail.getIsActive());
            recipeDetailDTO.setAdditionalTopic(recipeDetail.getAdditionalTopic());
            recipeDetailDTO.setMainIngredientTopic(recipeDetail.getMainIngredientTopic());

            List<AdditionalIngredient> additionalIngredientList = additionalIngredientRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (AdditionalIngredient additionalIngredient : additionalIngredientList) {
                AdditionalIngredientDTO additionalIngredientDTO = new AdditionalIngredientDTO();
                additionalIngredientDTO.setAdditionalIngredientId(additionalIngredient.getAdditionalIngredientId());
                additionalIngredientDTO.setMeasurement(additionalIngredient.getMeasurement());
                additionalIngredientDTO.setIsActive(additionalIngredient.getIsActive());
                additionalIngredientDTO.setCreateDate(additionalIngredient.getCreateDate());
                additionalIngredientDTO.setIngredientDetail(additionalIngredient.getIngredientDetail());

                additionalIngredientDTOList.add(additionalIngredientDTO);
            }

            List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeIngredient recipeIngredient : recipeIngredientList) {
                RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();
                recipeIngredientDTO.setIngredientDetailId(recipeIngredient.getRecipeIngredientId());
                recipeIngredientDTO.setRecipeDetailId(null);
                recipeIngredientDTO.setIngredientDetailId(null);
                recipeIngredientDTO.setMeasurement(recipeIngredient.getMeasurement());
                recipeIngredientDTO.setIsActive(recipeIngredient.getIsActive());
                recipeIngredientDTO.setCreateDate(recipeIngredient.getCreateDate());
                recipeIngredientDTO.setIngredientDetail(recipeIngredient.getIngredientDetail());

                recipeIngredientDTOList.add(recipeIngredientDTO);
            }

            List<RecipeInstruction> recipeInstructionList = recipeInstructionRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeInstruction recipeInstruction : recipeInstructionList) {
                RecipeInstructionDTO recipeInstructionDTO = new RecipeInstructionDTO();
                recipeInstructionDTO.setRecipeInstructionId(recipeInstruction.getRecipeInstructionId());
                recipeInstructionDTO.setRecipeDetailId(null);
                recipeInstructionDTO.setDescription(recipeInstruction.getDescription());
                recipeInstructionDTO.setCreate_date(recipeInstruction.getCreate_date());

                recipeInstructionDTOList.add(recipeInstructionDTO);
            }

            List<RecipeNote> recipeNoteList = recipeNoteRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeNote recipeNote : recipeNoteList) {
                RecipeNoteDTO recipeNoteDTO = new RecipeNoteDTO();
                recipeNoteDTO.setRecipeNoteId(recipeNote.getRecipeNoteId());
                recipeNoteDTO.setRecipeDetailId(null);
                recipeNoteDTO.setDescription(recipeNote.getDescription());
                recipeNoteDTO.setCreate_date(recipeNote.getCreate_date());

                recipeNoteDTOList.add(recipeNoteDTO);
            }
            recipeDetailDTO.setAdditionalIngredientDTOList(additionalIngredientDTOList);
            recipeDetailDTO.setRecipeIngredientDTOList(recipeIngredientDTOList);
            recipeDetailDTO.setRecipeInstructionDTOList(recipeInstructionDTOList);
            recipeDetailDTO.setRecipeNoteDTOList(recipeNoteDTOList);

            recipeDetailDTOList.add(recipeDetailDTO);
        }

        return new ResponseDto("success", "200", recipeDetailDTOList);
    }

    public ResponseDto deleteRecipesById(Long recipeId) throws Exception {
        RecipeDetail recipeDetail = recipeDetailRepository.findById(recipeId).get();
        if (recipeDetail != null) {
            recipeDetail.setIsActive(false);
            recipeDetailRepository.save(recipeDetail);

            return new ResponseDto("success", "200", null);
        } else {
            return new ResponseDto("recipe not found", "500", null);
        }
    }

    public ResponseDto getAllRecipeDetailsWithPagination(int pageNo, int pageCount, @PathVariable("categoryId") Long categoryId, @PathVariable("searchRec") String searchRec) throws Exception {
        Pageable paging = PageRequest.of(pageNo, pageCount, Sort.by("recipeName").ascending());
        Page<RecipeDetail> recipeDetailList;
        if (searchRec.equals("null")) {
            if (categoryId == 0) {
                recipeDetailList = recipeDetailRepository.findAllByIsUserCustomRecipeFalseAndIsActiveTrue(paging);
            } else {
                recipeDetailList = recipeDetailRepository.getAllRecipeBycategoryIdWithPagination(categoryId, paging);
            }
        } else {
            recipeDetailList = recipeDetailRepository.searchRecipe(searchRec, paging);
        }

        List<RecipeDetailDTO> recipeDetailDTOList = new ArrayList<>();

        for (RecipeDetail recipeDetail : recipeDetailList) {
            List<AdditionalIngredientDTO> additionalIngredientDTOList = new ArrayList<>();
            List<RecipeIngredientDTO> recipeIngredientDTOList = new ArrayList<>();
            List<RecipeInstructionDTO> recipeInstructionDTOList = new ArrayList<>();
            List<RecipeNoteDTO> recipeNoteDTOList = new ArrayList<>();

            RecipeDetailDTO recipeDetailDTO = new RecipeDetailDTO();
            recipeDetailDTO.setRecipeDetailId(recipeDetail.getRecipeDetailId());
//            recipeDetailDTO.setRecipeCategory(recipeDetail.getRecipeCategory());
            recipeDetailDTO.setRecipeName(recipeDetail.getRecipeName());
            recipeDetailDTO.setPreparationTime(recipeDetail.getPreparationTime());
            recipeDetailDTO.setCookTime(recipeDetail.getCookTime());
            recipeDetailDTO.setServingCount(recipeDetail.getServingCount());
            recipeDetailDTO.setCarbsPerServe(recipeDetail.getCarbsPerServe());
            recipeDetailDTO.setImageUrl(recipeDetail.getImageUrl());
            recipeDetailDTO.setYoutubeVideoUrl(recipeDetail.getYoutubeVideoUrl());
            recipeDetailDTO.setCreateDate(recipeDetail.getCreateDate());
            recipeDetailDTO.setIsActive(recipeDetail.getIsActive());
            recipeDetailDTO.setAdditionalTopic(recipeDetail.getAdditionalTopic());
            recipeDetailDTO.setMainIngredientTopic(recipeDetail.getMainIngredientTopic());

            List<AdditionalIngredient> additionalIngredientList = additionalIngredientRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (AdditionalIngredient additionalIngredient : additionalIngredientList) {
                AdditionalIngredientDTO additionalIngredientDTO = new AdditionalIngredientDTO();
                additionalIngredientDTO.setAdditionalIngredientId(additionalIngredient.getAdditionalIngredientId());
                additionalIngredientDTO.setMeasurement(additionalIngredient.getMeasurement());
                additionalIngredientDTO.setIsActive(additionalIngredient.getIsActive());
                additionalIngredientDTO.setCreateDate(additionalIngredient.getCreateDate());
                additionalIngredientDTO.setIngredientDetail(additionalIngredient.getIngredientDetail());

                additionalIngredientDTOList.add(additionalIngredientDTO);
            }

            List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeIngredient recipeIngredient : recipeIngredientList) {
                RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();
                recipeIngredientDTO.setIngredientDetailId(recipeIngredient.getRecipeIngredientId());
                recipeIngredientDTO.setRecipeDetailId(null);
                recipeIngredientDTO.setIngredientDetailId(null);
                recipeIngredientDTO.setMeasurement(recipeIngredient.getMeasurement());
                recipeIngredientDTO.setIsActive(recipeIngredient.getIsActive());
                recipeIngredientDTO.setCreateDate(recipeIngredient.getCreateDate());
                recipeIngredientDTO.setIngredientDetail(recipeIngredient.getIngredientDetail());

                recipeIngredientDTOList.add(recipeIngredientDTO);
            }

            List<RecipeInstruction> recipeInstructionList = recipeInstructionRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeInstruction recipeInstruction : recipeInstructionList) {
                RecipeInstructionDTO recipeInstructionDTO = new RecipeInstructionDTO();
                recipeInstructionDTO.setRecipeInstructionId(recipeInstruction.getRecipeInstructionId());
                recipeInstructionDTO.setRecipeDetailId(null);
                recipeInstructionDTO.setDescription(recipeInstruction.getDescription());
                recipeInstructionDTO.setCreate_date(recipeInstruction.getCreate_date());

                recipeInstructionDTOList.add(recipeInstructionDTO);
            }

            List<RecipeNote> recipeNoteList = recipeNoteRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeNote recipeNote : recipeNoteList) {
                RecipeNoteDTO recipeNoteDTO = new RecipeNoteDTO();
                recipeNoteDTO.setRecipeNoteId(recipeNote.getRecipeNoteId());
                recipeNoteDTO.setRecipeDetailId(null);
                recipeNoteDTO.setDescription(recipeNote.getDescription());
                recipeNoteDTO.setCreate_date(recipeNote.getCreate_date());

                recipeNoteDTOList.add(recipeNoteDTO);
            }
            recipeDetailDTO.setAdditionalIngredientDTOList(additionalIngredientDTOList);
            recipeDetailDTO.setRecipeIngredientDTOList(recipeIngredientDTOList);
            recipeDetailDTO.setRecipeInstructionDTOList(recipeInstructionDTOList);
            recipeDetailDTO.setRecipeNoteDTOList(recipeNoteDTOList);

            recipeDetailDTOList.add(recipeDetailDTO);
        }

        return new ResponseDto("success", "200", recipeDetailList.getTotalElements(), recipeDetailList.getTotalPages(), recipeDetailDTOList);
    }

    public ResponseDto getAllRecipebyCategoryIdWithPagination(Long categoryId, int pageNo, int pageCount) throws Exception {
        Pageable paging = PageRequest.of(pageNo, pageCount);
        Page<RecipeDetail> recipeDetailList = recipeDetailRepository.getAllRecipeBycategoryIdWithPagination(categoryId, paging);
        List<RecipeDetailDTO> recipeDetailDTOList = new ArrayList<>();

        for (RecipeDetail recipeDetail : recipeDetailList) {
            List<AdditionalIngredientDTO> additionalIngredientDTOList = new ArrayList<>();
            List<RecipeIngredientDTO> recipeIngredientDTOList = new ArrayList<>();
            List<RecipeInstructionDTO> recipeInstructionDTOList = new ArrayList<>();
            List<RecipeNoteDTO> recipeNoteDTOList = new ArrayList<>();

            RecipeDetailDTO recipeDetailDTO = new RecipeDetailDTO();
            recipeDetailDTO.setRecipeDetailId(recipeDetail.getRecipeDetailId());
//            recipeDetailDTO.setRecipeCategory(recipeDetail.getRecipeCategory());
            recipeDetailDTO.setRecipeName(recipeDetail.getRecipeName());
            recipeDetailDTO.setPreparationTime(recipeDetail.getPreparationTime());
            recipeDetailDTO.setCookTime(recipeDetail.getCookTime());
            recipeDetailDTO.setServingCount(recipeDetail.getServingCount());
            recipeDetailDTO.setCarbsPerServe(recipeDetail.getCarbsPerServe());
            recipeDetailDTO.setImageUrl(recipeDetail.getImageUrl());
            recipeDetailDTO.setYoutubeVideoUrl(recipeDetail.getYoutubeVideoUrl());
            recipeDetailDTO.setCreateDate(recipeDetail.getCreateDate());
            recipeDetailDTO.setIsActive(recipeDetail.getIsActive());
            recipeDetailDTO.setAdditionalTopic(recipeDetail.getAdditionalTopic());
            recipeDetailDTO.setMainIngredientTopic(recipeDetail.getMainIngredientTopic());

            List<AdditionalIngredient> additionalIngredientList = additionalIngredientRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (AdditionalIngredient additionalIngredient : additionalIngredientList) {
                AdditionalIngredientDTO additionalIngredientDTO = new AdditionalIngredientDTO();
                additionalIngredientDTO.setAdditionalIngredientId(additionalIngredient.getAdditionalIngredientId());
                additionalIngredientDTO.setMeasurement(additionalIngredient.getMeasurement());
                additionalIngredientDTO.setIsActive(additionalIngredient.getIsActive());
                additionalIngredientDTO.setCreateDate(additionalIngredient.getCreateDate());
                additionalIngredientDTO.setIngredientDetail(additionalIngredient.getIngredientDetail());

                additionalIngredientDTOList.add(additionalIngredientDTO);
            }

            List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeIngredient recipeIngredient : recipeIngredientList) {
                RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();
                recipeIngredientDTO.setIngredientDetailId(recipeIngredient.getRecipeIngredientId());
                recipeIngredientDTO.setRecipeDetailId(null);
                recipeIngredientDTO.setIngredientDetailId(null);
                recipeIngredientDTO.setMeasurement(recipeIngredient.getMeasurement());
                recipeIngredientDTO.setIsActive(recipeIngredient.getIsActive());
                recipeIngredientDTO.setCreateDate(recipeIngredient.getCreateDate());
                recipeIngredientDTO.setIngredientDetail(recipeIngredient.getIngredientDetail());

                recipeIngredientDTOList.add(recipeIngredientDTO);
            }

            List<RecipeInstruction> recipeInstructionList = recipeInstructionRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeInstruction recipeInstruction : recipeInstructionList) {
                RecipeInstructionDTO recipeInstructionDTO = new RecipeInstructionDTO();
                recipeInstructionDTO.setRecipeInstructionId(recipeInstruction.getRecipeInstructionId());
                recipeInstructionDTO.setRecipeDetailId(null);
                recipeInstructionDTO.setDescription(recipeInstruction.getDescription());
                recipeInstructionDTO.setCreate_date(recipeInstruction.getCreate_date());

                recipeInstructionDTOList.add(recipeInstructionDTO);
            }

            List<RecipeNote> recipeNoteList = recipeNoteRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            for (RecipeNote recipeNote : recipeNoteList) {
                RecipeNoteDTO recipeNoteDTO = new RecipeNoteDTO();
                recipeNoteDTO.setRecipeNoteId(recipeNote.getRecipeNoteId());
                recipeNoteDTO.setRecipeDetailId(null);
                recipeNoteDTO.setDescription(recipeNote.getDescription());
                recipeNoteDTO.setCreate_date(recipeNote.getCreate_date());

                recipeNoteDTOList.add(recipeNoteDTO);
            }
            recipeDetailDTO.setAdditionalIngredientDTOList(additionalIngredientDTOList);
            recipeDetailDTO.setRecipeIngredientDTOList(recipeIngredientDTOList);
            recipeDetailDTO.setRecipeInstructionDTOList(recipeInstructionDTOList);
            recipeDetailDTO.setRecipeNoteDTOList(recipeNoteDTOList);

            recipeDetailDTOList.add(recipeDetailDTO);
        }

        return new ResponseDto("success", "200", recipeDetailDTOList);
    }

    public ResponseDto getAllRecipeDetailsDropDown() throws Exception {
        List<RecipeDetail> recipeDetailList = recipeDetailRepository.findAllByIsUserCustomRecipeFalseAndIsActiveTrueOrderByRecipeNameAsc();
        List<RecipeDetailByDropDownDTO> recipeDetailByDropDownDTOList = new ArrayList<>();

        for (RecipeDetail recipeDetail: recipeDetailList){
            RecipeDetailByDropDownDTO recipeDetailByDropDownDTO = new RecipeDetailByDropDownDTO();
            recipeDetailByDropDownDTO.setRecipeDetailId(recipeDetail.getRecipeDetailId());
            recipeDetailByDropDownDTO.setRecipeName(recipeDetail.getRecipeName());
            recipeDetailByDropDownDTO.setImageUrl(recipeDetail.getImageUrl());

            recipeDetailByDropDownDTOList.add(recipeDetailByDropDownDTO);
        }
        return new ResponseDto("success", "200", recipeDetailByDropDownDTOList);
    }

    public ResponseDto recipeDetailWriteCSV() throws Exception {
        // Define the data you want to write to the CSV file
        String[] headers = {"ID", "RECIPE NAME", "PREPARATION TIME", "COOK TIME", "SERVING COUNT", "CARBS PER SERVE", "IMAGE", "VIDEO", "INGREDIENT", "INSTRUCTION", "SPECIAL NOTE"};

        long currentTimeMillis = System.currentTimeMillis();

        // Specify the file path
        String filePath = archivePath + "/recipe_"+currentTimeMillis+".csv";
        String urlPrefix = serverUrl + "/" + filePrefix + "/recipe_"+currentTimeMillis+".csv";

        // Create a FileWriter object to write to the CSV file
        FileWriter writer = new FileWriter(filePath);

        // Create a CSVWriter object with the FileWriter
        CSVWriter csvWriter = new CSVWriter(writer);

        // Write the headers to the CSV file
        csvWriter.writeNext(headers);

        List<RecipeDetail> recipeDetailList = recipeDetailRepository.findAllByIsUserCustomRecipeFalseAndIsActiveTrue();
        List<RecipeDetailDTO> recipeDetailDTOList = new ArrayList<>();

        for (RecipeDetail recipeDetail : recipeDetailList) {
            String[] recipeArray = new String[11];
            recipeArray[0] = recipeDetail.getRecipeDetailId() + "";
            recipeArray[1] = recipeDetail.getRecipeName();
            recipeArray[2] = recipeDetail.getPreparationTime() + "";
            recipeArray[3] = recipeDetail.getCookTime() + "";
            recipeArray[4] = recipeDetail.getServingCount() + "";
            recipeArray[5] = recipeDetail.getCarbsPerServe() + "";
            recipeArray[6] = recipeDetail.getImageUrl();
            recipeArray[7] = recipeDetail.getYoutubeVideoUrl();

            List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            StringBuilder ingredientConcatenatedValues = new StringBuilder();
            for (RecipeIngredient recipeIngredient : recipeIngredientList) {
                ingredientConcatenatedValues.append("NAME:" + recipeIngredient.getIngredientDetail().getIngredient()
                        + " BRAND:" + recipeIngredient.getIngredientDetail().getBrand()
                        + " SUBTITLE:" + recipeIngredient.getIngredientDetail().getBrand()
                        + " MEASUREMENT TYPE:" + recipeIngredient.getIngredientDetail().getMeasurementType()
                        + " MEASUREMENT:" + recipeIngredient.getMeasurement()).append(" / ");
            }

            recipeArray[8] = ingredientConcatenatedValues.toString();

            List<RecipeInstruction> recipeInstructionList = recipeInstructionRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            StringBuilder instructionConcatenatedValues = new StringBuilder();
            for (RecipeInstruction recipeInstruction : recipeInstructionList) {
                instructionConcatenatedValues.append(recipeInstruction.getDescription()).append(" / ");
            }
            recipeArray[9] = instructionConcatenatedValues.toString();

            List<RecipeNote> recipeNoteList = recipeNoteRepository.findByRecipeId(recipeDetail.getRecipeDetailId());
            StringBuilder specialNoteConcatenatedValues = new StringBuilder();
            for (RecipeNote recipeNote : recipeNoteList) {
                specialNoteConcatenatedValues.append(recipeNote.getDescription()).append(" / ");
            }
            recipeArray[10] = specialNoteConcatenatedValues.toString();

            csvWriter.writeNext(recipeArray);
        }


        // Close the CSVWriter
        csvWriter.close();

        return new ResponseDto("success", "200", urlPrefix);
    }
}
