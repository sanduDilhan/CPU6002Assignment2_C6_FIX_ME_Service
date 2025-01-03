package com.sentura.bLow.service;

import com.opencsv.CSVWriter;
import com.sentura.bLow.dto.IngredientDetailDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.entity.IngredientDetail;
import com.sentura.bLow.entity.RecipeDetail;
import com.sentura.bLow.repository.IngredientDetailRepository;
import com.sentura.bLow.util.Utilities;
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

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class IngredientDetailService {

    @Autowired
    private IngredientDetailRepository ingredientDetailRepository;

    @Value("${server.file.prefix}")
    private String filePrefix;

    @Value("${archive.path}")
    private String archivePath;

    @Value("${server.url}")
    private String serverUrl;

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto createIngredient(IngredientDetailDTO ingredientDetailDTO) throws Exception{
        IngredientDetail ingredientDetail = new IngredientDetail();
        ingredientDetail.setIngredientDetailId(0L);
        ingredientDetail.setIngredient(ingredientDetailDTO.getIngredient());
        ingredientDetail.setBrand(ingredientDetailDTO.getBrand());

        if (ingredientDetailDTO.getSubTitle() == "" || ingredientDetailDTO.getSubTitle() == null){
            ingredientDetail.setSubTitle(null);
        }else {
            ingredientDetail.setSubTitle(ingredientDetailDTO.getSubTitle());
        }

        if (ingredientDetailDTO.getExternalRecipe() == "" || ingredientDetailDTO.getExternalRecipe() == null){
            ingredientDetail.setExternalRecipe(null);
        }else {
            ingredientDetail.setExternalRecipe(ingredientDetailDTO.getExternalRecipe());
        }
        ingredientDetail.setTspOrQty(null);
        ingredientDetail.setMeasurementType(ingredientDetailDTO.getMeasurementType());
        ingredientDetail.setMeasurement(ingredientDetailDTO.getMeasurement());

//        if(ingredientDetailDTO.getMeasurementType().equals("100g")){
//            ingredientDetailDTO.setMeasurement(100.0);
//        }else if(ingredientDetailDTO.getMeasurementType().equals("100ml")){
//            ingredientDetailDTO.setMeasurement(100.0);
//        }else{
//            ingredientDetailDTO.setMeasurement(1.0);
//        }

//        ingredientDetail.setMeasurement(ingredientDetailDTO.getMeasurement());

        if(ingredientDetailDTO.getCarbs() == null || ingredientDetailDTO.getCarbs() == 0){
            ingredientDetail.setCarbs(0.0);
        }else {
            ingredientDetail.setCarbs(ingredientDetailDTO.getCarbs());
        }
        ingredientDetail.setCreateDate(new Date());

        ingredientDetail.setIsHide(false);

        ingredientDetailRepository.save(ingredientDetail);
        return new ResponseDto("success", "200", null);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto updateIngredient(IngredientDetailDTO ingredientDetailDTO) throws Exception{
        IngredientDetail ingredientDetail = new IngredientDetail();
        ingredientDetail.setIngredientDetailId(ingredientDetailDTO.getIngredientDetailId());
        ingredientDetail.setIngredient(ingredientDetailDTO.getIngredient());
        ingredientDetail.setBrand(ingredientDetailDTO.getBrand());

        if (ingredientDetailDTO.getSubTitle() == "" || ingredientDetailDTO.getSubTitle() == null){
            ingredientDetail.setSubTitle(null);
        }else {
            ingredientDetail.setSubTitle(ingredientDetailDTO.getSubTitle());
        }

        if (ingredientDetailDTO.getExternalRecipe() == "" || ingredientDetailDTO.getExternalRecipe() == null){
            ingredientDetail.setExternalRecipe(null);
        }else {
            ingredientDetail.setExternalRecipe(ingredientDetailDTO.getExternalRecipe());
        }

        ingredientDetail.setTspOrQty(null);
        ingredientDetail.setMeasurementType(ingredientDetailDTO.getMeasurementType());
        ingredientDetail.setMeasurement(ingredientDetailDTO.getMeasurement());
//        if(ingredientDetailDTO.getMeasurementType().equals("100g")){
//            ingredientDetailDTO.setMeasurement(100.0);
//        }else if(ingredientDetailDTO.getMeasurementType().equals("100ml")){
//            ingredientDetailDTO.setMeasurement(100.0);
//        }else{
//            ingredientDetailDTO.setMeasurement(1.0);
//        }
//        ingredientDetail.setMeasurement(ingredientDetailDTO.getMeasurement());

        if(ingredientDetailDTO.getCarbs() == null || ingredientDetailDTO.getCarbs() == 0){
            ingredientDetail.setCarbs(0.0);
        }else {
            ingredientDetail.setCarbs(ingredientDetailDTO.getCarbs());
        }

        ingredientDetail.setCreateDate(new Date());

        ingredientDetail.setIsHide(ingredientDetailDTO.getIsHide());

        ingredientDetailRepository.save(ingredientDetail);
        return new ResponseDto("success", "200", null);
    }

    public ResponseDto getAllIngredient() throws Exception{
        List<IngredientDetail> ingredientDetailList = ingredientDetailRepository.getAllOrderByIngredientName();
        return new ResponseDto("success", "200", ingredientDetailList);
    }

    public ResponseDto findByIngredientId(Long ingredientId) throws Exception{
        IngredientDetail ingredientDetail = ingredientDetailRepository.findById(ingredientId).get();
        return new ResponseDto("success", "200", ingredientDetail);
    }

    public ResponseDto getAllActiveIngredient() throws Exception{
        List<IngredientDetail> ingredientDetailList = ingredientDetailRepository.findByIsHideFalseOrderByIngredientAsc();
        return new ResponseDto("success", "200", ingredientDetailList);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto hideAndShowIngredient(Long ingredientId) throws Exception{
        IngredientDetail ingredientDetail = ingredientDetailRepository.findById(ingredientId).get();
        if(ingredientDetail.getIsHide()){
            ingredientDetail.setIsHide(false);
        }else{
            ingredientDetail.setIsHide(true);
        }

        ingredientDetailRepository.save(ingredientDetail);
        return new ResponseDto("success", "200", null);
    }

    public ResponseDto getAllActiveIngredientByAdminSite() throws Exception{
        List<IngredientDetail> ingredientDetailList = ingredientDetailRepository.findAll();
        List<IngredientDetailDTO> ingredientDetailDTOList = new ArrayList<>();
        for(IngredientDetail ingredientDetail: ingredientDetailList){
            IngredientDetailDTO ingredientDetailDTO = new IngredientDetailDTO();
            ingredientDetailDTO.setIngredientDetailId(ingredientDetail.getIngredientDetailId());
            if(ingredientDetail.getSubTitle() != null){
                ingredientDetailDTO.setIngredient(ingredientDetail.getSubTitle());
            }else {
                ingredientDetailDTO.setIngredient(ingredientDetail.getIngredient());
            }
            ingredientDetailDTO.setBrand(ingredientDetail.getBrand());
            ingredientDetailDTO.setTspOrQty(ingredientDetail.getTspOrQty());
            ingredientDetailDTO.setMeasurementType(ingredientDetail.getMeasurementType());
            ingredientDetailDTO.setMeasurement(ingredientDetail.getMeasurement());
            ingredientDetailDTO.setCarbs(ingredientDetail.getCarbs());
            ingredientDetailDTO.setCreateDate(ingredientDetail.getCreateDate());

            ingredientDetailDTOList.add(ingredientDetailDTO);
        }
        return new ResponseDto("success", "200", ingredientDetailDTOList);
    }

    public ResponseDto getAllActiveIngredientByPublicSite() throws Exception{
        List<IngredientDetail> ingredientDetailList = ingredientDetailRepository.findByIsHideFalse();
        List<IngredientDetailDTO> ingredientDetailDTOList = new ArrayList<>();
        for(IngredientDetail ingredientDetail: ingredientDetailList){
            IngredientDetailDTO ingredientDetailDTO = new IngredientDetailDTO();
            ingredientDetailDTO.setIngredientDetailId(ingredientDetail.getIngredientDetailId());
            if(ingredientDetail.getExternalRecipe() != null){
                ingredientDetailDTO.setIngredient(ingredientDetail.getExternalRecipe());
            }else {
                if(ingredientDetail.getSubTitle() != null){
                    ingredientDetailDTO.setIngredient(ingredientDetail.getSubTitle());
                }else {
                    ingredientDetailDTO.setIngredient(ingredientDetail.getIngredient());
                }
            }
            ingredientDetailDTO.setBrand(ingredientDetail.getBrand());
            ingredientDetailDTO.setTspOrQty(ingredientDetail.getTspOrQty());
            ingredientDetailDTO.setMeasurementType(ingredientDetail.getMeasurementType());
            ingredientDetailDTO.setMeasurement(ingredientDetail.getMeasurement());
            ingredientDetailDTO.setCarbs(ingredientDetail.getCarbs());
            ingredientDetailDTO.setCreateDate(ingredientDetail.getCreateDate());

            ingredientDetailDTOList.add(ingredientDetailDTO);
        }
        return new ResponseDto("success", "200", ingredientDetailDTOList);
    }

    public ResponseDto ingredientDetailWriteCSV() throws Exception {
        // Define the data you want to write to the CSV file
        String[] headers = {"ID", "INGREDIENT NAME", "BRAND", "SUBTITLE", "MEASUREMENT TYPE", "MEASUREMENT", "CARBS", "IS HIDE", "EXTERNAL RECIPE"};

        long currentTimeMillis = System.currentTimeMillis();

        // Specify the file path
        String filePath = archivePath+"/ingredient_"+currentTimeMillis+".csv";
        String urlPrefix = serverUrl + "/" + filePrefix + "/ingredient_"+currentTimeMillis+".csv";

        // Create a FileWriter object to write to the CSV file
        FileWriter writer = new FileWriter(filePath);

        // Create a CSVWriter object with the FileWriter
        CSVWriter csvWriter = new CSVWriter(writer);

        // Write the headers to the CSV file
        csvWriter.writeNext(headers);

        List<IngredientDetail> ingredientDetailList = ingredientDetailRepository.findAll();
        for (IngredientDetail ingredientDetail: ingredientDetailList){
            String[] ingredientArray = new String[11];
            ingredientArray[0] = ingredientDetail.getIngredientDetailId()+"";
            ingredientArray[1] = ingredientDetail.getIngredient();
            ingredientArray[2] = ingredientDetail.getBrand();
            ingredientArray[3] = ingredientDetail.getSubTitle();
            ingredientArray[4] = ingredientDetail.getMeasurementType();
            ingredientArray[5] = ingredientDetail.getMeasurement()+"";
            ingredientArray[6] = ingredientDetail.getCarbs()+"";
            ingredientArray[7] = ingredientDetail.getIsHide()+"";
            ingredientArray[8] = ingredientDetail.getExternalRecipe();

            csvWriter.writeNext(ingredientArray);
        }

        // Close the CSVWriter
        csvWriter.close();

        return new ResponseDto("success", "200", urlPrefix);
    }

    public ResponseDto getAllIngredientWithPagination(int pageNo, int pageCount, String searchRec) throws Exception{
        Pageable paging = PageRequest.of(pageNo, pageCount);
        Page<IngredientDetail> ingredientDetailList;
        if (searchRec.equals("null")) {
            ingredientDetailList = ingredientDetailRepository.getAllOrderByIngredientNameWithPagination(paging);
        }else{
            ingredientDetailList = ingredientDetailRepository.getAllByIngredientNameWithPagination(paging, searchRec);
        }
        return new ResponseDto("success", "200",ingredientDetailList.getTotalElements(), ingredientDetailList.getTotalPages(), ingredientDetailList);
    }
}
