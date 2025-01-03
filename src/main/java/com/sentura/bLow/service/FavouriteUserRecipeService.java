package com.sentura.bLow.service;

import com.google.gson.Gson;
import com.sentura.bLow.dto.*;
import com.sentura.bLow.entity.*;
import com.sentura.bLow.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FavouriteUserRecipeService {

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private RecipeDetailRepository recipeDetailRepository;

    @Autowired
    private IngredientDetailRepository ingredientDetailRepository;

    @Autowired
    private FavouriteRecipeIngredientRepository favouriteRecipeIngredientRepository;

    @Autowired
    private FavouriteUserRecipeRepository favouriteUserRecipeRepository;

    @Autowired
    private RecipeInstructionRepository recipeInstructionRepository;

    @Autowired
    private RecipeNoteRepository recipeNoteRepository;

    @Autowired
    private FavouriteAdditionalIngredientRepository favouriteAdditionalIngredientRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private CustomRecipeIngredientRepository customRecipeIngredientRepository;

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto createFavouriteRecipeAndIngredient(FavouriteUserRecipeDTO favouriteUserRecipeDTO) throws Exception {

        UserDetail userDetail = userDetailRepository.findById(favouriteUserRecipeDTO.getUserDetailId()).get();

        FavouriteUserRecipe favouriteUserRecipe = new FavouriteUserRecipe();
        favouriteUserRecipe.setFavouriteUserRecipeId(0L);
        favouriteUserRecipe.setUserDetail(userDetail);

        favouriteUserRecipe.setCarbsCount(favouriteUserRecipeDTO.getCarbsCount());
        favouriteUserRecipe.setServingCount(favouriteUserRecipeDTO.getServingCount());
        favouriteUserRecipe.setCreateDate(new Date());
        favouriteUserRecipe.setIsActive(true);
        favouriteUserRecipe.setEditFavouriteRecipeName(favouriteUserRecipeDTO.getEditFavouriteRecipeName());

        if (favouriteUserRecipeDTO.getIngredientDetailId() != null) {
            IngredientDetail ingredientDetail = ingredientDetailRepository.findById(favouriteUserRecipeDTO.getIngredientDetailId()).get();
            favouriteUserRecipe.setIngredientDetail(ingredientDetail);
        } else {
            favouriteUserRecipe.setIngredientDetail(null);
        }

        if (favouriteUserRecipeDTO.getRecipeDetailId() != null) {
            RecipeDetail recipeDetail = recipeDetailRepository.findById(favouriteUserRecipeDTO.getRecipeDetailId()).get();
            favouriteUserRecipe.setRecipeDetail(recipeDetail);
            FavouriteUserRecipe favouriteUserRecipe1 = favouriteUserRecipeRepository.save(favouriteUserRecipe);

            for (FavouriteRecipeIngredientDTO favouriteRecipeIngredientDTO : favouriteUserRecipeDTO.getFavouriteRecipeIngredientDTOList()) {
                IngredientDetail ingredientDetail1 = ingredientDetailRepository.findById(favouriteRecipeIngredientDTO.getIngredientDetailId()).get();

                FavouriteRecipeIngredient favouriteRecipeIngredient = new FavouriteRecipeIngredient();
                favouriteRecipeIngredient.setFavouriteRecipeIngredientId(0L);
                favouriteRecipeIngredient.setFavouriteUserRecipe(favouriteUserRecipe1);
                favouriteRecipeIngredient.setIngredientDetail(ingredientDetail1);
                favouriteRecipeIngredient.setMeasurement(favouriteRecipeIngredientDTO.getMeasurement());
                favouriteRecipeIngredient.setCreateDate(new Date());
                favouriteRecipeIngredient.setIsActive(true);

                favouriteRecipeIngredientRepository.save(favouriteRecipeIngredient);
            }

            for (FavouriteAdditionalIngredientDTO favouriteAdditionalIngredientDTO : favouriteUserRecipeDTO.getFavouriteAdditionalIngredientDTOList()) {
                IngredientDetail ingredientDetail1 = ingredientDetailRepository.findById(favouriteAdditionalIngredientDTO.getIngredientDetailId()).get();

                FavouriteAdditionalIngredient favouriteAdditionalIngredient = new FavouriteAdditionalIngredient();
                favouriteAdditionalIngredient.setAdditionalIngredientId(0L);
                favouriteAdditionalIngredient.setFavouriteUserRecipe(favouriteUserRecipe1);
                favouriteAdditionalIngredient.setIngredientDetail(ingredientDetail1);
                favouriteAdditionalIngredient.setMeasurement(favouriteAdditionalIngredientDTO.getMeasurement());
                favouriteAdditionalIngredient.setIsActive(true);

                favouriteAdditionalIngredientRepository.save(favouriteAdditionalIngredient);
            }
        } else {
            favouriteUserRecipe.setRecipeDetail(null);
            favouriteUserRecipeRepository.save(favouriteUserRecipe);
        }

        return new ResponseDto("success", "200", null);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto createCustomRecipeFavourite(String recipe, MultipartFile recipeImage) throws Exception {
        Gson gson = new Gson();
        RecipeDetailDTO recipeDetailDTO = gson.fromJson(recipe, RecipeDetailDTO.class);

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

        FavouriteUserRecipe favouriteUserRecipe = new FavouriteUserRecipe();
        favouriteUserRecipe.setFavouriteUserRecipeId(0L);
        favouriteUserRecipe.setUserDetail(userDetail);
        favouriteUserRecipe.setRecipeDetail(recipeDetail1);
        favouriteUserRecipe.setIngredientDetail(null);
        favouriteUserRecipe.setCarbsCount(recipeDetailDTO.getCarbsPerServe());
        favouriteUserRecipe.setServingCount(1);
        favouriteUserRecipe.setIsActive(true);
        favouriteUserRecipe.setCreateDate(new Date());

        favouriteUserRecipeRepository.save(favouriteUserRecipe);

        return new ResponseDto("success", "200", recipeDetail1.getRecipeDetailId());
    }


    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto deleteFavouriteRecipeAndIngredient(Long favouriteRecipeId) throws Exception {
        FavouriteUserRecipe favouriteUserRecipe = favouriteUserRecipeRepository.findById(favouriteRecipeId).get();
        favouriteUserRecipe.setIsActive(false);

        favouriteUserRecipeRepository.save(favouriteUserRecipe);
        return new ResponseDto("success", "200", null);
    }

    public ResponseDto getAllFavouriteRecipeAndIngredientByUserId(Long userId) throws Exception {
        List<FavouriteUserRecipe> favouriteUserRecipeList = favouriteUserRecipeRepository.getAllFavouriteByUserId(userId);
        if (favouriteUserRecipeList != null) {
            List<FavouriteUserRecipeDTO> favouriteUserRecipeDTOList = new ArrayList<>();
            for (FavouriteUserRecipe favouriteUserRecipe : favouriteUserRecipeList) {
                FavouriteUserRecipeDTO favouriteUserRecipeDTO = new FavouriteUserRecipeDTO();
                favouriteUserRecipeDTO.setFavouriteUserRecipeId(favouriteUserRecipe.getFavouriteUserRecipeId());
                favouriteUserRecipeDTO.setUserDetailId(null);
                favouriteUserRecipeDTO.setRecipeDetailId(null);
                favouriteUserRecipeDTO.setIngredientDetailId(null);
                favouriteUserRecipeDTO.setCarbsCount(favouriteUserRecipe.getCarbsCount());
                favouriteUserRecipeDTO.setServingCount(favouriteUserRecipe.getServingCount());
                favouriteUserRecipeDTO.setCreateDate(favouriteUserRecipe.getCreateDate());
                favouriteUserRecipeDTO.setIsActive(favouriteUserRecipe.getIsActive());
                favouriteUserRecipeDTO.setIngredientDetail(favouriteUserRecipe.getIngredientDetail());

                if (favouriteUserRecipe.getRecipeDetail() != null) {
                    favouriteUserRecipe.getRecipeDetail().setUserDetail(null);
                    favouriteUserRecipeDTO.setRecipeDetail(favouriteUserRecipe.getRecipeDetail());
                    if (favouriteUserRecipe.getEditFavouriteRecipeName() != null){
                        favouriteUserRecipe.getRecipeDetail().setRecipeName(favouriteUserRecipe.getEditFavouriteRecipeName());
                    }

                    List<FavouriteRecipeIngredient> favouriteRecipeIngredientList = favouriteRecipeIngredientRepository.getAllFavouriteByUserId(favouriteUserRecipe.getFavouriteUserRecipeId());
                    List<FavouriteRecipeIngredientDTO> favouriteRecipeIngredientDTOList = new ArrayList<>();
                    for (FavouriteRecipeIngredient favouriteRecipeIngredient : favouriteRecipeIngredientList) {
                        FavouriteRecipeIngredientDTO favouriteRecipeIngredientDTO = new FavouriteRecipeIngredientDTO();
                        favouriteRecipeIngredientDTO.setFavouriteRecipeIngredientId(favouriteRecipeIngredient.getFavouriteRecipeIngredientId());
                        favouriteRecipeIngredientDTO.setFavouriteUserRecipeId(null);
                        favouriteRecipeIngredientDTO.setIngredientDetailId(null);
                        favouriteRecipeIngredientDTO.setMeasurement(favouriteRecipeIngredient.getMeasurement());
                        favouriteRecipeIngredientDTO.setIsActive(favouriteRecipeIngredient.getIsActive());
                        favouriteRecipeIngredientDTO.setCreateDate(favouriteRecipeIngredient.getCreateDate());
                        favouriteRecipeIngredientDTO.setIngredientDetail(favouriteRecipeIngredient.getIngredientDetail());

                        favouriteRecipeIngredientDTOList.add(favouriteRecipeIngredientDTO);
                    }
                    favouriteUserRecipeDTO.setFavouriteRecipeIngredientDTOList(favouriteRecipeIngredientDTOList);

                    List<FavouriteAdditionalIngredient> favouriteAdditionalIngredientList = favouriteAdditionalIngredientRepository.getAllFavouriteByRecipeId(favouriteUserRecipe.getFavouriteUserRecipeId());
                    List<FavouriteAdditionalIngredientDTO> favouriteAdditionalIngredientDTOList = new ArrayList<>();
                    for (FavouriteAdditionalIngredient favouriteAdditionalIngredient : favouriteAdditionalIngredientList) {
                        FavouriteAdditionalIngredientDTO favouriteAdditionalIngredientDTO = new FavouriteAdditionalIngredientDTO();
                        favouriteAdditionalIngredientDTO.setAdditionalIngredientId(favouriteAdditionalIngredient.getAdditionalIngredientId());
                        favouriteAdditionalIngredientDTO.setMeasurement(favouriteAdditionalIngredient.getMeasurement());
                        favouriteAdditionalIngredientDTO.setIsActive(favouriteAdditionalIngredient.getIsActive());
                        favouriteAdditionalIngredientDTO.setCreateDate(favouriteAdditionalIngredient.getCreateDate());

                        favouriteAdditionalIngredientDTOList.add(favouriteAdditionalIngredientDTO);
                    }
                    favouriteUserRecipeDTO.setFavouriteAdditionalIngredientDTOList(favouriteAdditionalIngredientDTOList);

                    List<CustomRecipeIngredient> customRecipeIngredientList = customRecipeIngredientRepository.getAllByCustomRecipeId(favouriteUserRecipe.getRecipeDetail().getRecipeDetailId());
                    List<CustomRecipeIngredientDTO> customRecipeIngredientDTOList = new ArrayList<>();
                    for (CustomRecipeIngredient customRecipeIngredient: customRecipeIngredientList){
                        CustomRecipeIngredientDTO customRecipeIngredientDTO = new CustomRecipeIngredientDTO();
                        customRecipeIngredientDTO.setCustomRecipeIngredientId(customRecipeIngredient.getCustomRecipeIngredientId());
                        customRecipeIngredientDTO.setIngredient(customRecipeIngredient.getIngredient());
                        customRecipeIngredientDTO.setMeasurementType(customRecipeIngredient.getMeasurementType());
                        customRecipeIngredientDTO.setMeasurement(customRecipeIngredient.getMeasurement());

                        customRecipeIngredientDTOList.add(customRecipeIngredientDTO);
                    }
                    favouriteUserRecipeDTO.setCustomRecipeIngredientDTOList(customRecipeIngredientDTOList);

                    List<RecipeInstruction> recipeInstructionList = recipeInstructionRepository.findByRecipeId(favouriteUserRecipe.getRecipeDetail().getRecipeDetailId());
                    List<RecipeInstructionDTO> recipeInstructionDTOList = new ArrayList<>();
                    for (RecipeInstruction recipeInstruction : recipeInstructionList) {
                        RecipeInstructionDTO recipeInstructionDTO = new RecipeInstructionDTO();
                        recipeInstructionDTO.setRecipeInstructionId(recipeInstruction.getRecipeInstructionId());
                        recipeInstructionDTO.setRecipeDetailId(null);
                        recipeInstructionDTO.setDescription(recipeInstruction.getDescription());
                        recipeInstructionDTO.setCreate_date(recipeInstruction.getCreate_date());

                        recipeInstructionDTOList.add(recipeInstructionDTO);
                    }
                    favouriteUserRecipeDTO.setRecipeInstructionDTOList(recipeInstructionDTOList);

                    List<RecipeNote> recipeNoteList = recipeNoteRepository.findByRecipeId(favouriteUserRecipe.getRecipeDetail().getRecipeDetailId());
                    List<RecipeNoteDTO> recipeNoteDTOList = new ArrayList<>();
                    for (RecipeNote recipeNote : recipeNoteList) {
                        RecipeNoteDTO recipeNoteDTO = new RecipeNoteDTO();
                        recipeNoteDTO.setRecipeNoteId(recipeNote.getRecipeNoteId());
                        recipeNoteDTO.setRecipeDetailId(null);
                        recipeNoteDTO.setDescription(recipeNote.getDescription());
                        recipeNoteDTO.setCreate_date(recipeNote.getCreate_date());

                        recipeNoteDTOList.add(recipeNoteDTO);
                    }
                    favouriteUserRecipeDTO.setRecipeNoteDTOList(recipeNoteDTOList);
                }
                favouriteUserRecipeDTOList.add(favouriteUserRecipeDTO);
            }
            return new ResponseDto("success", "200", favouriteUserRecipeDTOList);
        } else {
            return new ResponseDto("success", "200", null);
        }

    }

    public ResponseDto getFavouriteRecipeAndIngredientById(Long favouriteId) throws Exception {
        FavouriteUserRecipe favouriteUserRecipe = favouriteUserRecipeRepository.findById(favouriteId).get();
        if (favouriteUserRecipe != null) {
            FavouriteUserRecipeDTO favouriteUserRecipeDTO = new FavouriteUserRecipeDTO();
            favouriteUserRecipeDTO.setFavouriteUserRecipeId(favouriteUserRecipe.getFavouriteUserRecipeId());
            favouriteUserRecipeDTO.setUserDetailId(null);
            favouriteUserRecipeDTO.setRecipeDetailId(null);
            favouriteUserRecipeDTO.setIngredientDetailId(null);
            favouriteUserRecipeDTO.setCarbsCount(favouriteUserRecipe.getCarbsCount());
            favouriteUserRecipeDTO.setServingCount(favouriteUserRecipe.getServingCount());
            favouriteUserRecipeDTO.setCreateDate(favouriteUserRecipe.getCreateDate());
            favouriteUserRecipeDTO.setIsActive(favouriteUserRecipe.getIsActive());
            favouriteUserRecipeDTO.setRecipeDetail(favouriteUserRecipe.getRecipeDetail());
            if (favouriteUserRecipe.getEditFavouriteRecipeName() != null){
                favouriteUserRecipe.getRecipeDetail().setRecipeName(favouriteUserRecipe.getEditFavouriteRecipeName());
            }
            favouriteUserRecipeDTO.setIngredientDetail(favouriteUserRecipe.getIngredientDetail());

            if (favouriteUserRecipe.getRecipeDetail() != null) {
                List<FavouriteRecipeIngredient> favouriteRecipeIngredientList = favouriteRecipeIngredientRepository.getAllFavouriteByUserId(favouriteUserRecipe.getFavouriteUserRecipeId());
                List<FavouriteRecipeIngredientDTO> favouriteRecipeIngredientDTOList = new ArrayList<>();
                for (FavouriteRecipeIngredient favouriteRecipeIngredient : favouriteRecipeIngredientList) {
                    FavouriteRecipeIngredientDTO favouriteRecipeIngredientDTO = new FavouriteRecipeIngredientDTO();
                    favouriteRecipeIngredientDTO.setFavouriteRecipeIngredientId(favouriteRecipeIngredient.getFavouriteRecipeIngredientId());
                    favouriteRecipeIngredientDTO.setFavouriteUserRecipeId(null);
                    favouriteRecipeIngredientDTO.setIngredientDetailId(null);
                    favouriteRecipeIngredientDTO.setMeasurement(favouriteRecipeIngredient.getMeasurement());
                    favouriteRecipeIngredientDTO.setIsActive(favouriteRecipeIngredient.getIsActive());
                    favouriteRecipeIngredientDTO.setCreateDate(favouriteRecipeIngredient.getCreateDate());
                    favouriteRecipeIngredientDTO.setIngredientDetail(favouriteRecipeIngredient.getIngredientDetail());

                    favouriteRecipeIngredientDTOList.add(favouriteRecipeIngredientDTO);
                }
                favouriteUserRecipeDTO.setFavouriteRecipeIngredientDTOList(favouriteRecipeIngredientDTOList);

                List<FavouriteAdditionalIngredient> favouriteAdditionalIngredientList = favouriteAdditionalIngredientRepository.getAllFavouriteByRecipeId(favouriteUserRecipe.getFavouriteUserRecipeId());
                List<FavouriteAdditionalIngredientDTO> favouriteAdditionalIngredientDTOList = new ArrayList<>();
                for (FavouriteAdditionalIngredient favouriteAdditionalIngredient : favouriteAdditionalIngredientList) {
                    FavouriteAdditionalIngredientDTO favouriteAdditionalIngredientDTO = new FavouriteAdditionalIngredientDTO();
                    favouriteAdditionalIngredientDTO.setAdditionalIngredientId(favouriteAdditionalIngredient.getAdditionalIngredientId());
                    favouriteAdditionalIngredientDTO.setMeasurement(favouriteAdditionalIngredient.getMeasurement());
                    favouriteAdditionalIngredientDTO.setIsActive(favouriteAdditionalIngredient.getIsActive());
                    favouriteAdditionalIngredientDTO.setCreateDate(favouriteAdditionalIngredient.getCreateDate());
                    favouriteAdditionalIngredientDTO.setIngredientDetail(favouriteAdditionalIngredient.getIngredientDetail());

                    favouriteAdditionalIngredientDTOList.add(favouriteAdditionalIngredientDTO);
                }
                favouriteUserRecipeDTO.setFavouriteAdditionalIngredientDTOList(favouriteAdditionalIngredientDTOList);

                List<RecipeInstruction> recipeInstructionList = recipeInstructionRepository.findByRecipeId(favouriteUserRecipe.getRecipeDetail().getRecipeDetailId());
                List<RecipeInstructionDTO> recipeInstructionDTOList = new ArrayList<>();
                for (RecipeInstruction recipeInstruction : recipeInstructionList) {
                    RecipeInstructionDTO recipeInstructionDTO = new RecipeInstructionDTO();
                    recipeInstructionDTO.setRecipeInstructionId(recipeInstruction.getRecipeInstructionId());
                    recipeInstructionDTO.setRecipeDetailId(null);
                    recipeInstructionDTO.setDescription(recipeInstruction.getDescription());
                    recipeInstructionDTO.setCreate_date(recipeInstruction.getCreate_date());

                    recipeInstructionDTOList.add(recipeInstructionDTO);
                }
                favouriteUserRecipeDTO.setRecipeInstructionDTOList(recipeInstructionDTOList);

                List<RecipeNote> recipeNoteList = recipeNoteRepository.findByRecipeId(favouriteUserRecipe.getRecipeDetail().getRecipeDetailId());
                List<RecipeNoteDTO> recipeNoteDTOList = new ArrayList<>();
                for (RecipeNote recipeNote : recipeNoteList) {
                    RecipeNoteDTO recipeNoteDTO = new RecipeNoteDTO();
                    recipeNoteDTO.setRecipeNoteId(recipeNote.getRecipeNoteId());
                    recipeNoteDTO.setRecipeDetailId(null);
                    recipeNoteDTO.setDescription(recipeNote.getDescription());
                    recipeNoteDTO.setCreate_date(recipeNote.getCreate_date());

                    recipeNoteDTOList.add(recipeNoteDTO);
                }
                favouriteUserRecipeDTO.setRecipeNoteDTOList(recipeNoteDTOList);
            }
            return new ResponseDto("success", "200", favouriteUserRecipeDTO);
        } else {
            return new ResponseDto("success", "200", null);
        }

    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto updateFavouriteRecipe(FavouriteUserRecipeDTO favouriteUserRecipeDTO) throws Exception {
        FavouriteUserRecipe favouriteUserRecipe = favouriteUserRecipeRepository.findById(favouriteUserRecipeDTO.getFavouriteUserRecipeId()).get();
        favouriteUserRecipe.setCarbsCount(favouriteUserRecipeDTO.getCarbsCount());
        favouriteUserRecipe.setServingCount(favouriteUserRecipeDTO.getServingCount());
        favouriteUserRecipe.setEditFavouriteRecipeName(favouriteUserRecipeDTO.getEditFavouriteRecipeName());

        favouriteUserRecipeRepository.save(favouriteUserRecipe);
        favouriteRecipeIngredientRepository.deleteByRecipeId(favouriteUserRecipeDTO.getFavouriteUserRecipeId());
        favouriteAdditionalIngredientRepository.deleteByRecipeId(favouriteUserRecipeDTO.getFavouriteUserRecipeId());

        for (FavouriteRecipeIngredientDTO favouriteRecipeIngredientDTO : favouriteUserRecipeDTO.getFavouriteRecipeIngredientDTOList()) {
            IngredientDetail ingredientDetail = ingredientDetailRepository.findById(favouriteRecipeIngredientDTO.getIngredientDetailId()).get();
            FavouriteRecipeIngredient favouriteRecipeIngredient = new FavouriteRecipeIngredient();
            favouriteRecipeIngredient.setFavouriteRecipeIngredientId(0L);
            favouriteRecipeIngredient.setFavouriteUserRecipe(favouriteUserRecipe);
            favouriteRecipeIngredient.setIngredientDetail(ingredientDetail);
            favouriteRecipeIngredient.setMeasurement(favouriteRecipeIngredientDTO.getMeasurement());
            favouriteRecipeIngredient.setIsActive(true);
            favouriteRecipeIngredient.setCreateDate(new Date());

            favouriteRecipeIngredientRepository.save(favouriteRecipeIngredient);
        }

        for (FavouriteAdditionalIngredientDTO favouriteAdditionalIngredientDTO : favouriteUserRecipeDTO.getFavouriteAdditionalIngredientDTOList()) {
            IngredientDetail ingredientDetail1 = ingredientDetailRepository.findById(favouriteAdditionalIngredientDTO.getIngredientDetailId()).get();

            FavouriteAdditionalIngredient favouriteAdditionalIngredient = new FavouriteAdditionalIngredient();
            favouriteAdditionalIngredient.setAdditionalIngredientId(0L);
            favouriteAdditionalIngredient.setFavouriteUserRecipe(favouriteUserRecipe);
            favouriteAdditionalIngredient.setIngredientDetail(ingredientDetail1);
            favouriteAdditionalIngredient.setMeasurement(favouriteAdditionalIngredientDTO.getMeasurement());
            favouriteAdditionalIngredient.setIsActive(true);

            favouriteAdditionalIngredientRepository.save(favouriteAdditionalIngredient);
        }
        return new ResponseDto("success", "200", null);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto updateFavouriteRecipeServeCount(FavouriteUserRecipeDTO favouriteUserRecipeDTO) throws Exception {
        FavouriteUserRecipe favouriteUserRecipe = favouriteUserRecipeRepository.findById(favouriteUserRecipeDTO.getFavouriteUserRecipeId()).get();
        favouriteUserRecipe.setServingCount(favouriteUserRecipeDTO.getServingCount());

        favouriteUserRecipeRepository.save(favouriteUserRecipe);

        return new ResponseDto("success", "200", null);
    }
}
