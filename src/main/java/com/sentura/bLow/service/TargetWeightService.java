package com.sentura.bLow.service;

import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.dto.TargetWeightDTO;
import com.sentura.bLow.entity.TargetWeight;
import com.sentura.bLow.entity.UserDetail;
import com.sentura.bLow.repository.TargetWeightRepository;
import com.sentura.bLow.repository.UserDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TargetWeightService {

    @Autowired
    private TargetWeightRepository targetWeightRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto setTargetOrActualWeight(TargetWeightDTO targetWeightDTO) throws Exception {
        UserDetail userDetail = userDetailRepository.findById(targetWeightDTO.getUserDetailId()).get();
        if(userDetail != null){
            TargetWeight targetWeight = new TargetWeight();
            targetWeight.setTargetWeightId(0L);
            targetWeight.setUserDetail(userDetail);
            targetWeight.setWeight(targetWeightDTO.getWeight());
            targetWeight.setSpecialNote(targetWeightDTO.getSpecialNote());
            targetWeight.setIsTargetWeight(targetWeightDTO.getIsTargetWeight());
            targetWeight.setIsActive(true);
            targetWeight.setCreateDate(new Date());

            if(targetWeightDTO.getActualWeightDate() != null){
                Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(targetWeightDTO.getActualWeightDate());
                targetWeight.setActualWeightDate(date1);
            }else{
                targetWeight.setActualWeightDate(null);
            }

            if(targetWeightDTO.getIsTargetWeight()){
                TargetWeight checkPastWeight = targetWeightRepository.getActiveTargetWeightByUserId(userDetail.getUserId());
                if(checkPastWeight != null){
                    checkPastWeight.setIsActive(false);
                    targetWeightRepository.save(checkPastWeight);
                }
            }

            targetWeightRepository.save(targetWeight);
            return new ResponseDto("success", "200", null);
        }else{
            return new ResponseDto("User not found", "500", null);
        }
    }

    public ResponseDto getActiveTargetWeight(Long userId) throws Exception {
        TargetWeight targetWeight = targetWeightRepository.getActiveTargetWeightByUserId(userId);
        TargetWeightDTO targetWeightDTO = new TargetWeightDTO();
        targetWeightDTO.setTargetWeightId(targetWeight.getTargetWeightId());
        targetWeightDTO.setUserDetailId(null);
        targetWeightDTO.setWeight(targetWeight.getWeight());
        targetWeightDTO.setSpecialNote(targetWeight.getSpecialNote());
        targetWeightDTO.setIsTargetWeight(targetWeight.getIsTargetWeight());
        targetWeightDTO.setIsActive(targetWeight.getIsActive());
        targetWeightDTO.setCreateDate(targetWeight.getCreateDate());

        return new ResponseDto("success", "200", targetWeightDTO);
    }

    public ResponseDto getWeightTrackerHistory(Long userId) throws Exception {
        List<TargetWeight> actualTargetWeight = targetWeightRepository.getActualWeightByUserId(userId);
        List<TargetWeightDTO> targetWeightDTOList = new ArrayList<>();
        if(actualTargetWeight != null){
            for (TargetWeight targetWeight: actualTargetWeight){
                TargetWeightDTO targetWeightDTO = new TargetWeightDTO();
                targetWeightDTO.setTargetWeightId(targetWeight.getTargetWeightId());
                targetWeightDTO.setUserDetailId(null);
                targetWeightDTO.setWeight(targetWeight.getWeight());
                targetWeightDTO.setSpecialNote(targetWeight.getSpecialNote());
                targetWeightDTO.setIsTargetWeight(targetWeight.getIsTargetWeight());
                targetWeightDTO.setActualWeightDate(targetWeight.getActualWeightDate()+"");
                targetWeightDTO.setIsActive(targetWeight.getIsActive());
                targetWeightDTO.setCreateDate(targetWeight.getCreateDate());

                targetWeightDTOList.add(targetWeightDTO);
            }
        }
        return new ResponseDto("success", "200", targetWeightDTOList);
    }

    public ResponseDto getWeightTracker(Long userId) throws Exception {
        List<TargetWeight> actualTargetWeight = targetWeightRepository.getActiveWeightByUserId(userId);
        List<TargetWeightDTO> targetWeightDTOList = new ArrayList<>();
        if(actualTargetWeight != null){
            for (TargetWeight targetWeight: actualTargetWeight){
                TargetWeightDTO targetWeightDTO = new TargetWeightDTO();
                targetWeightDTO.setTargetWeightId(targetWeight.getTargetWeightId());
                targetWeightDTO.setUserDetailId(null);
                targetWeightDTO.setWeight(targetWeight.getWeight());
                targetWeightDTO.setSpecialNote(targetWeight.getSpecialNote());
                targetWeightDTO.setIsTargetWeight(targetWeight.getIsTargetWeight());
                targetWeightDTO.setActualWeightDate(targetWeight.getActualWeightDate()+"");
                targetWeightDTO.setIsActive(targetWeight.getIsActive());
                targetWeightDTO.setCreateDate(targetWeight.getCreateDate());

                targetWeightDTOList.add(targetWeightDTO);
            }
        }
        return new ResponseDto("success", "200", targetWeightDTOList);
    }
}
