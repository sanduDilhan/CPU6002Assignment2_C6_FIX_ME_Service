package com.sentura.bLow.service;

import com.sentura.bLow.configuration.JwtAuthenticationController;
import com.sentura.bLow.constants.AppConstants;
import com.sentura.bLow.dto.CustomEmailDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.dto.UserDetailDTO;
import com.sentura.bLow.entity.PackageDetail;
import com.sentura.bLow.entity.UserDetail;
import com.sentura.bLow.entity.UserPackage;
import com.sentura.bLow.exception.types.EmailNotFoundException;
import com.sentura.bLow.exception.types.EmailNotValidException;
import com.sentura.bLow.repository.AuthRepository;
import com.sentura.bLow.repository.PackageDetailRepository;
import com.sentura.bLow.repository.UserDetailRepository;
import com.sentura.bLow.repository.UserPackageRepository;
import com.sentura.bLow.util.Utilities;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationController authenticationController;

    @Autowired
    private PackageDetailRepository packageDetailRepository;

    @Autowired
    private UserPackageRepository userPackageRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public UserDetail findValidUserByEmail(String email) {
        return authRepository.findFirstByEmailAndIsActiveTrue(email);
    }

    public UserDetail findUserByEmail(String email) {
        return authRepository.findByEmail(email);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto signUp(UserDetailDTO userDto) throws Exception {
        UserDetail userDetail = findUserByEmail(userDto.getEmail());
        long userId = 0;
        if(userDetail != null){
            log.info("{}. tried to sign up again",userDetail.getEmail());
            return new ResponseDto("500","That email is taken. Try another one!");
        } else {
            String verifyCode = new Utilities().generateRandomNumber();
            Stripe.apiKey = stripeSecretKey;

            UserDetail detail = new UserDetail();
            detail.setUserId(0L);
            detail.setFirstName(userDto.getFirstName());
            detail.setLastName(userDto.getLastName());
            detail.setUserName(userDto.getUserName());
            detail.setEmail(userDto.getEmail());
            detail.setPassword(passwordEncoder.encode(userDto.getPassword()));
            detail.setUserRole(userDto.getUserRole());
            detail.setVerifyCode(verifyCode);
            detail.setIsActive(false);
            detail.setVerified(false);
            detail.setIsAdminCreate(false);
            detail.setCreateDate(new Date());

            CustomerCreateParams params =
                    CustomerCreateParams.builder()
                            .setName(userDto.getFirstName() + " " + userDto.getLastName())
                            .setEmail(userDto.getEmail())
                            .build();
            Customer customer = Customer.create(params);
            detail.setStripeCustomerId(customer.getId());

            verifyCode = String.join(" ", verifyCode.split(""));
            detail = authRepository.saveAndFlush(detail);
            userId = detail.getUserId();

            emailService.sendUserVerificationEmail(userDto.getEmail(), verifyCode, userDto.getFirstName());
        }
        return new ResponseDto("success", "200", userId);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto verifyEmail(String email, String verificationCode) throws Exception {
        UserDetail user;
        if (email.matches(AppConstants.emailValidationRegex)) {
            user = findUserByEmail(email);
            if (user == null) {
                throw new EmailNotFoundException();

            } else if (user.getVerified()) {
                log.info("{}. user already verified", email);
                return new ResponseDto("500","User Already Verified!");

            } else if (verificationCode.equals(user.getVerifyCode())) {
                user.setVerified(true);
                user.setVerifyCode(null);
                user.setIsActive(true);
                authRepository.save(user);
                emailService.sendUserVerifiedEmail(email, user.getFirstName());

                return new ResponseDto("success","200","User Verified Successfully!");

            } else {
                log.info("{}. wrong verification code", verificationCode);
                return new ResponseDto("500","Wrong Verify Code!");
            }
        } else {
            throw new EmailNotValidException();
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto resendVerifyEmail(String email) throws Exception {
        UserDetail user;
        if (email.matches(AppConstants.emailValidationRegex)) {
            user = findUserByEmail(email);
            if (user == null) {
                throw new EmailNotFoundException();

            } else if (user.getVerified()) {
                log.info("{}. user already verified", email);
                return new ResponseDto("500","User Already Verified!");

            } else {
                String verifyCode = new Utilities().generateRandomNumber();
                user.setVerifyCode(verifyCode);
                authRepository.save(user);

                verifyCode = String.join(" ", verifyCode.split(""));
                emailService.sendUserVerificationEmail(email,verifyCode, user.getFirstName());

                return new ResponseDto("200","Verify Code Sent Again!");
            }
        } else {
            throw new EmailNotValidException();
        }
    }

    public ResponseDto resetPassword(String email, String resetCode, String password) {
        UserDetail user;
        if (email.matches(AppConstants.emailValidationRegex)) {
            user = findUserByEmail(email);
            if (user == null) {
                throw new EmailNotFoundException();

            } else if (user.getVerifyCode().equals(resetCode)) {
                password = passwordEncoder.encode(password);
                user.setPassword(password);
                user.setVerifyCode(null);
                authRepository.save(user);

                return new ResponseDto("success","200","Password Reset Done!");

            } else {
                return new ResponseDto("500","Invalid Reset Code!");
            }
        } else {
            throw new EmailNotValidException();
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto forgetPassword(String email) throws Exception {
        UserDetail user;
        if (email.matches(AppConstants.emailValidationRegex)) {
            user = findUserByEmail(email);
            if (user == null) {
                throw new EmailNotFoundException();

            } else {
                String resetCode = new Utilities().generateRandomNumber();
                user.setVerifyCode(resetCode);
                authRepository.save(user);

                resetCode = String.join(" ", resetCode.split(""));
                emailService.sendForgetPasswordRequestEmail(email,resetCode,user.getFirstName());

                return new ResponseDto("200","Password Reset Code Sent to the given email address!");
            }
        } else {
            throw new EmailNotValidException();
        }
    }

    public ResponseDto changePassword(String email, String oldPassword, String newPassword) {
        UserDetail user;

        if (email.matches(AppConstants.emailValidationRegex)) {
            user = findUserByEmail(email);
            if (user != null) {

                authenticationController.authenticate(user.getEmail(), oldPassword);

                user.setPassword(passwordEncoder.encode(newPassword));
                authRepository.save(user);

                return new ResponseDto("success", "200", "Password Changed Successfully!");

            } else {
                throw new EmailNotFoundException();
            }
        } else {
            throw new EmailNotValidException();
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto updateUserDetail(UserDetailDTO userDto) throws Exception {
        if(userDto != null){
            UserDetail checkUser = userDetailRepository.findById(userDto.getUserId()).get();
            UserDetail userDetail = new UserDetail();
            userDetail.setUserId(checkUser.getUserId());
            userDetail.setFirstName(userDto.getFirstName());
            userDetail.setLastName(userDto.getLastName());
            userDetail.setUserName(userDto.getUserName());
            userDetail.setEmail(checkUser.getEmail());
            userDetail.setPassword(checkUser.getPassword());
            userDetail.setUserRole(checkUser.getUserRole());
            userDetail.setVerifyCode(checkUser.getVerifyCode());
            userDetail.setVerified(checkUser.getVerified());
            if(userDto.getIsActive() != null){
                userDetail.setIsActive(userDto.getIsActive());
            }else{

                userDetail.setIsActive(checkUser.getIsActive());
            }
            userDetail.setCreateDate(checkUser.getCreateDate());

            userDetailRepository.save(userDetail);
            return new ResponseDto("success", "200", null);
        }else{
            return new ResponseDto("failed", "500", null);
        }
    }

    public ResponseDto getPackageAndUserDetailbyUserId(Long userId) throws Exception {
        UserPackage userPackage = userPackageRepository.getPackageByUserId(userId);
        if(userPackage != null){
            userPackage.getUserDetail().setPassword(null);
            userPackage.getUserDetail().setUserRole(null);
            userPackage.getUserDetail().setVerifyCode(null);

            if (userPackage.getUserDetail().getIsAdminCreate()){
                userPackage.getPackageDetail().setPackageName("This profile create by admin");
            }
        }
        return new ResponseDto("success", "200", userPackage);
    }

    public ResponseDto getAllUser() throws Exception {
        List<UserDetail> userDetailList = userDetailRepository.getAllUsers();
        List<UserDetailDTO> userDetailDTOList = new ArrayList<>();
        if(userDetailList != null){
            for(UserDetail userDetail: userDetailList){
                UserDetailDTO userDetailDTO = new UserDetailDTO();
                userDetailDTO.setUserId(userDetail.getUserId());
                userDetailDTO.setFirstName(userDetail.getFirstName());
                userDetailDTO.setLastName(userDetail.getLastName());
                userDetailDTO.setUserName(userDetail.getUserName());
                userDetailDTO.setEmail(userDetail.getEmail());
                userDetailDTO.setIsActive(userDetail.getIsActive());
                userDetailDTO.setCreateDate(userDetail.getCreateDate());

                userDetailDTOList.add(userDetailDTO);
            }
        }
        return new ResponseDto("success", "200", userDetailDTOList);
    }

    public ResponseDto getAllAdmin() throws Exception {
        List<UserDetail> userDetailList = userDetailRepository.getAllAdmin();
        List<UserDetailDTO> userDetailDTOList = new ArrayList<>();
        if(userDetailList != null){
            for(UserDetail userDetail: userDetailList){
                UserDetailDTO userDetailDTO = new UserDetailDTO();
                userDetailDTO.setUserId(userDetail.getUserId());
                userDetailDTO.setFirstName(userDetail.getFirstName());
                userDetailDTO.setLastName(userDetail.getLastName());
                userDetailDTO.setUserName(userDetail.getUserName());
                userDetailDTO.setEmail(userDetail.getEmail());
                userDetailDTO.setIsActive(userDetail.getIsActive());
                userDetailDTO.setCreateDate(userDetail.getCreateDate());

                userDetailDTOList.add(userDetailDTO);
            }
        }
        return new ResponseDto("success", "200", userDetailDTOList);
    }

    public ResponseDto sendCustomEmail(CustomEmailDTO customEmailDTO) throws Exception {
        emailService.customEmail(customEmailDTO);
        return new ResponseDto("success", "200", "Email Send Successfully!");
    }

    public ResponseDto CreateUserByAdmin(String firstName,String lastName,String userName,String email,String password,String userRole,Integer validDayCount) throws Exception {
        UserDetail userDetail = findUserByEmail(email);
        long userId = 0;
        if(userDetail != null){
            log.info("{}. tried to sign up again",userDetail.getEmail());
            return new ResponseDto("500","That email is taken. Try another one!");
        } else {
//            String verifyCode = new Utilities().generateRandomNumber();

            Stripe.apiKey = stripeSecretKey;

            UserDetail detail = new UserDetail();
            detail.setUserId(0L);
            detail.setFirstName(firstName);
            detail.setLastName(lastName);
            detail.setUserName(userName);
            detail.setEmail(email);
            detail.setPassword(passwordEncoder.encode(password));
            detail.setUserRole(userRole);
            detail.setVerifyCode(null);
            detail.setIsActive(true);
            detail.setVerified(true);
            detail.setIsAdminCreate(true);
            detail.setCreateDate(new Date());

            CustomerCreateParams params =
                    CustomerCreateParams.builder()
                            .setName(firstName+ " " + lastName)
                            .setEmail(email)
                            .build();
            Customer customer = Customer.create(params);
            detail.setStripeCustomerId(customer.getId());

//            verifyCode = String.join(" ", verifyCode.split(""));
            detail = authRepository.saveAndFlush(detail);
            userId = detail.getUserId();

//            emailService.sendUserVerificationEmail(userDto.getEmail(), verifyCode, userDto.getFirstName());

            PackageDetail packageDetail = packageDetailRepository.queryTrialPackage();
            UserPackage userPackage = new UserPackage();
            userPackage.setUserPackageId(0L);
            userPackage.setUserDetail(detail);
            userPackage.setPackageDetail(packageDetail);
            userPackage.setSubscriptionId(null);
            userPackage.setPackagePrice(null);
            userPackage.setValidDayCount(validDayCount);
            userPackage.setTrialDayCount(null);
            userPackage.setStartDate(new Date());

            Calendar packageCalendar = Calendar.getInstance();
            packageCalendar.setTime(new Date());
            packageCalendar.add(Calendar.DATE, validDayCount + 1);
//            packageCalendar.add(Calendar.DATE, validDayCount);

            userPackage.setExpireDate(packageCalendar.getTime());
            userPackage.setIsActive(true);

            userPackageRepository.saveAndFlush(userPackage);
        }
        return new ResponseDto("success", "200", userId);
    }

    public ResponseDto getAllAdminCreateUser() throws Exception {
        List<UserPackage> userPackageList = userPackageRepository.getAdminCreateUsers();
        List<UserDetailDTO> userDetailDTOList = new ArrayList<>();
        if(userPackageList != null){
            for(UserPackage userPackage: userPackageList){
                UserDetailDTO userDetailDTO = new UserDetailDTO();
                userDetailDTO.setUserId(userPackage.getUserDetail().getUserId());
                userDetailDTO.setUserPackageId(userPackage.getUserPackageId());
                userDetailDTO.setFirstName(userPackage.getUserDetail().getFirstName());
                userDetailDTO.setLastName(userPackage.getUserDetail().getLastName());
                userDetailDTO.setUserName(userPackage.getUserDetail().getUserName());
                userDetailDTO.setEmail(userPackage.getUserDetail().getEmail());
                userDetailDTO.setIsActive(userPackage.getUserDetail().getIsActive());
                userDetailDTO.setCreateDate(userPackage.getUserDetail().getCreateDate());
                userDetailDTO.setAdminSetExpireDate(userPackage.getExpireDate());

                userDetailDTOList.add(userDetailDTO);
            }
        }
        return new ResponseDto("success", "200", userDetailDTOList);
    }

    public ResponseDto updateExpireDateAdminCreateUser(Long userPackageId,Integer validDayCount) throws Exception {
        UserPackage userPackage = userPackageRepository.findById(userPackageId).get();

        Calendar packageCalendar = Calendar.getInstance();
        packageCalendar.setTime(new Date());
        packageCalendar.add(Calendar.DATE, validDayCount + 1);
//        packageCalendar.add(Calendar.DATE, validDayCount);

        userPackage.setExpireDate(packageCalendar.getTime());
        userPackage.setValidDayCount(validDayCount);

        userPackageRepository.saveAndFlush(userPackage);
        return new ResponseDto("success", "200", null);
    }
}
