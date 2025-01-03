package com.sentura.bLow.service;

import com.sentura.bLow.dto.DailyCarbLimitDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.entity.DailyCarbLimit;
import com.sentura.bLow.entity.UserDetail;
import com.sentura.bLow.repository.DailyCarbLimitRepository;
import com.sentura.bLow.repository.UserDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
@Slf4j
public class DailyCarbLimitService {

    @Autowired
    private DailyCarbLimitRepository dailyCarbLimitRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto createDailyCarbLimit(DailyCarbLimitDTO dailyCarbLimitDTO) throws Exception {
        if(dailyCarbLimitDTO != null){
            UserDetail userDetail = userDetailRepository.findById(dailyCarbLimitDTO.getUserDetailId()).get();
            DailyCarbLimit dailyCarbLimit = new DailyCarbLimit();
            dailyCarbLimit.setUserDetail(userDetail);
            dailyCarbLimit.setCarbLimit(dailyCarbLimitDTO.getCarbLimit());
            dailyCarbLimit.setCreateDate(new Date());
            dailyCarbLimit.setIsActive(true);

            DailyCarbLimit checkActiveRecord = dailyCarbLimitRepository.getByUserId(dailyCarbLimitDTO.getUserDetailId());
            if(checkActiveRecord != null){
                dailyCarbLimit.setDailyCarbLimitId(checkActiveRecord.getDailyCarbLimitId());
            }else{
                dailyCarbLimit.setDailyCarbLimitId(0L);
            }
            dailyCarbLimitRepository.save(dailyCarbLimit);
            return new ResponseDto("success", "200", null);
        }else{
            return new ResponseDto("failed", "500", null);
        }
    }

    public ResponseDto getCarbLimitByUserId(Long userId) throws Exception {
        DailyCarbLimit dailyCarbLimit = dailyCarbLimitRepository.getByUserId(userId);
        if(dailyCarbLimit != null){
            DailyCarbLimitDTO dailyCarbLimitDTO = new DailyCarbLimitDTO();
            dailyCarbLimitDTO.setDailyCarbLimitId(dailyCarbLimit.getDailyCarbLimitId());
            dailyCarbLimitDTO.setUserDetailId(null);
            dailyCarbLimitDTO.setCarbLimit(dailyCarbLimit.getCarbLimit());
            dailyCarbLimitDTO.setIsActive(dailyCarbLimit.getIsActive());
            dailyCarbLimitDTO.setCreateDate(dailyCarbLimit.getCreateDate());

            return new ResponseDto("success", "200", dailyCarbLimitDTO);
        }else{
            return new ResponseDto("failed", "500", null);
        }
    }
}
