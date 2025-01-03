package com.sentura.bLow.service;

import com.sentura.bLow.dto.*;
import com.sentura.bLow.entity.PackageDetail;
import com.sentura.bLow.entity.UserDetail;
import com.sentura.bLow.entity.UserPackage;
import com.sentura.bLow.exception.types.UserNotFoundException;
import com.sentura.bLow.repository.AuthRepository;
import com.sentura.bLow.repository.PackageDetailRepository;
import com.sentura.bLow.repository.UserPackageRepository;
import com.stripe.Stripe;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.InvoiceUpcomingParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.SubscriptionCancelParams;
import com.stripe.param.SubscriptionListParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PackageService {

    @Autowired
    private PackageDetailRepository packageDetailRepository;

    @Autowired
    private UserPackageRepository userPackageRepository;

    @Autowired
    private AuthRepository authRepository;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.success.url}")
    private String stripeSuccessUrl;

    @Value("${stripe.error.url}")
    private String stripeErrorUrl;

    public ResponseDto getAll(String keyword, int pageNumber, int pageSize) {
        PaginationDto paginationDto = new PaginationDto();
        List<PackageDetailDTO> dtoList = new ArrayList<>();
        Page<PackageDetail> packageDetails;

        if (keyword != null && keyword.equals("null")) {
            packageDetails = packageDetailRepository.queryAllActivePackages(PageRequest.of(pageNumber, pageSize));
        } else if (keyword != null && !keyword.isEmpty()) {
            packageDetails = packageDetailRepository.queryAllActivePackagesByName(PageRequest.of(pageNumber, pageSize), keyword);
        } else {
            packageDetails = packageDetailRepository.queryAllActivePackages(PageRequest.of(pageNumber, pageSize));
        }

        if (!packageDetails.isEmpty()) {
            for (PackageDetail packageDetail : packageDetails) {

                PackageDetailDTO packageDetailDTO = new PackageDetailDTO();
                packageDetailDTO.setPackageId(packageDetail.getPackageId());
                packageDetailDTO.setPackageName(packageDetail.getPackageName());
                packageDetailDTO.setPackagePrice(packageDetail.getPackagePrice());
                packageDetailDTO.setDescription(packageDetail.getDescription());
                packageDetailDTO.setValidDayCount(packageDetail.getValidDayCount());
                packageDetailDTO.setIsActive(packageDetail.getIsActive());
                packageDetailDTO.setCreateDate(packageDetail.getCreateDateTime());

                dtoList.add(packageDetailDTO);
            }
            paginationDto.setTotalPages(packageDetails.getTotalPages());
            paginationDto.setListData(dtoList);
        }

        return new ResponseDto("success", "200", paginationDto);
    }

    public ResponseDto getAllDeleted(String keyword, int pageNumber, int pageSize) {
        PaginationDto paginationDto = new PaginationDto();
        List<PackageDetailDTO> dtoList = new ArrayList<>();
        Page<PackageDetail> packageDetails;

        if (keyword != null && !keyword.isEmpty()) {
            packageDetails = packageDetailRepository.queryAllDeActivePackagesByName(PageRequest.of(pageNumber, pageSize), keyword);
        } else {
            packageDetails = packageDetailRepository.queryAllDeActivePackages(PageRequest.of(pageNumber, pageSize));
        }

        if (!packageDetails.isEmpty()) {
            for (PackageDetail packageDetail : packageDetails) {

                PackageDetailDTO packageDetailDTO = new PackageDetailDTO();
                packageDetailDTO.setPackageId(packageDetail.getPackageId());
                packageDetailDTO.setPackageName(packageDetail.getPackageName());
                packageDetailDTO.setPackagePrice(packageDetail.getPackagePrice());
                packageDetailDTO.setDescription(packageDetail.getDescription());
                packageDetailDTO.setValidDayCount(packageDetail.getValidDayCount());

                dtoList.add(packageDetailDTO);
            }
            paginationDto.setTotalPages(packageDetails.getTotalPages());
            paginationDto.setListData(dtoList);
        }

        return new ResponseDto("success", "200", paginationDto);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto save(PackageDetailDTO packageDetailDTO) throws Exception {

        if (packageDetailDTO != null) {

            PackageDetail packageDetail;

            if (packageDetailDTO.getPackageId() != null) {
                packageDetail = packageDetailRepository.findByPackageIdAndIsActiveTrue(packageDetailDTO.getPackageId());
                if (packageDetail == null) {
                    return new ResponseDto("error", "500", "Package not found!");
                }
            } else {
                packageDetail = new PackageDetail();
            }

            Stripe.apiKey = stripeSecretKey;

            packageDetail.setPackagePrice(packageDetailDTO.getPackagePrice());
            packageDetail.setPackageName(packageDetailDTO.getPackageName());
            packageDetail.setDescription(packageDetailDTO.getDescription());
            packageDetail.setValidDayCount(packageDetailDTO.getValidDayCount());
            packageDetail.setIsActive(true);
            packageDetail.setIsTrial(false);

            PriceCreateParams params =
                    PriceCreateParams.builder()
                            .setCurrency("AUD")
                            .setUnitAmount(packageDetailDTO.getPackagePrice().longValue() * 100)
                            .setRecurring(
                                    PriceCreateParams.Recurring.builder()
                                            .setInterval(
                                                    packageDetailDTO.getValidDayCount() == 30 ? PriceCreateParams.Recurring.Interval.MONTH :
                                                    packageDetailDTO.getValidDayCount() == 365 ? PriceCreateParams.Recurring.Interval.YEAR : PriceCreateParams.Recurring.Interval.WEEK)
                                            .build()
                            )
                            .setProductData(
                                    PriceCreateParams.ProductData.builder().setName(packageDetail.getPackageName()).build()
                            )
                            .build();
            Price price = Price.create(params);
            packageDetail.setPriceId(price.getId());

            packageDetailRepository.save(packageDetail);

            return new ResponseDto("success", "200", "Package saved successfully!");
        } else {
            log.info("{}. package dto is null", "");
            throw new NullPointerException();
        }
    }

    public ResponseDto updateTrial(PackageDetailDTO packageDetailDTO) throws Exception {

        if (packageDetailDTO != null) {

            PackageDetail packageDetail = packageDetailRepository.queryTrialPackage();
            if (packageDetail == null) {
                packageDetail = new PackageDetail();
            }
            packageDetail.setPackagePrice(packageDetailDTO.getPackagePrice());
            packageDetail.setPackageName(packageDetailDTO.getPackageName());
            packageDetail.setDescription(packageDetailDTO.getDescription());
            packageDetail.setValidDayCount(packageDetailDTO.getValidDayCount());
            packageDetail.setIsActive(true);
            packageDetail.setIsTrial(true);

            packageDetailRepository.save(packageDetail);

            return new ResponseDto("success", "200", "Package saved successfully!");
        } else {
            log.info("{}. package dto is null", "");
            throw new NullPointerException();
        }
    }

    public ResponseDto getTrial() throws Exception {

        PackageDetail packageDetail = packageDetailRepository.queryTrialPackage();
        if (packageDetail == null) {
            return new ResponseDto("success", "200", null);
        } else {
            PackageDetailDTO packageDetailDTO = new PackageDetailDTO();
            packageDetailDTO.setPackagePrice(packageDetail.getPackagePrice());
            packageDetailDTO.setCreateDate(packageDetail.getCreateDateTime());
            packageDetailDTO.setPackageName(packageDetail.getPackageName());
            packageDetailDTO.setDescription(packageDetail.getDescription());
            packageDetailDTO.setValidDayCount(packageDetail.getValidDayCount());
            packageDetailDTO.setPackageId(packageDetail.getPackageId());

            return new ResponseDto("success", "200", packageDetailDTO);
        }
    }

    public ResponseDto get(String email) throws Exception{
        UserDetail userDetail = authRepository.findFirstByEmailAndIsActiveTrue(email);
        if (userDetail != null) {

            Stripe.apiKey = stripeSecretKey;

            List<UserPackage> userPackages = userPackageRepository.findByUserDetailAndIsActiveTrue(userDetail);
            List<UserPackageDTO> userPackageDTOS = new ArrayList<>();
            if (!userPackages.isEmpty()) {
                for(UserPackage userPackage: userPackages) {
                    UserPackageDTO userPackageDTO = new UserPackageDTO();
                    userPackageDTO.setPackagePrice(userPackage.getPackagePrice());
                    userPackageDTO.setUserPackageId(userPackage.getUserPackageId());
                    userPackageDTO.setPackageDetail(userPackage.getPackageDetail());
                    userPackageDTO.setValidDayCount(userPackage.getValidDayCount());
                    userPackageDTO.setExpireDate(userPackage.getExpireDate());
                    userPackageDTO.setStartDate(userPackage.getStartDate());
                    userPackageDTO.setInitiateDate(userPackage.getCreateDateTime());
                    userPackageDTO.setIsExpired(userPackage.getExpireDate().compareTo(new Date()) < 0);

                    if (userPackage.getUserDetail().getIsAdminCreate() && userPackage.getExpireDate().compareTo(new Date()) > 0){

                    }else{
                        InvoiceUpcomingParams params =
                                InvoiceUpcomingParams.builder().setCustomer(userDetail.getStripeCustomerId()).build();
                        Invoice invoice = Invoice.upcoming(params);

                        userPackageDTO.setCycleDate(invoice.getNextPaymentAttempt());
                    }

//                    if (userPackage.getExpireDate().compareTo(new Date()) < 0){
//                        userPackage.setIsActive(false);
//                        userPackageRepository.save(userPackage);
//                    }

                    userPackageDTO.setIsTrialActive(userPackage.getStartDate().compareTo(userPackage.getCreateDateTime()) > 0);

                    userPackageDTOS.add(userPackageDTO);
                }
            }

            return new ResponseDto("success", "200", userPackageDTOS);
        } else {
            throw new UserNotFoundException();
        }
    }

    public ResponseDto getUsers(long packageId, int pageNumber, int pageSize) {

        PaginationDto paginationDto = new PaginationDto();
        List<UserDetailDTO> users = new ArrayList<>();

        Optional<PackageDetail> packageDetailOptional = packageDetailRepository.findById(packageId);
        if (packageDetailOptional.isPresent()) {

            Page<UserPackage> userPackages = userPackageRepository.findByPackageDetailAndIsActiveTrue(PageRequest.of(pageNumber, pageSize), packageDetailOptional.get());

            if (!userPackages.isEmpty()) {
                for(UserPackage userPackage: userPackages) {

                    UserDetailDTO userDetailDTO = new UserDetailDTO();
                    userDetailDTO.setUserName(userPackage.getUserDetail().getUserName());
                    userDetailDTO.setUserId(userPackage.getUserDetail().getUserId());
                    userDetailDTO.setIsActive(userPackage.getUserDetail().getIsActive());
                    userDetailDTO.setUserRole(userPackage.getUserDetail().getUserRole());
                    userDetailDTO.setEmail(userPackage.getUserDetail().getEmail());
                    userDetailDTO.setVerified(userPackage.getUserDetail().getVerified());
                    userDetailDTO.setFirstName(userPackage.getUserDetail().getFirstName());
                    userDetailDTO.setLastName(userPackage.getUserDetail().getLastName());
                    users.add(userDetailDTO);
                }
            }

            paginationDto.setTotalPages(userPackages.getTotalPages());
            paginationDto.setListData(users);

            return new ResponseDto("success", "200", paginationDto);
        } else {
            return new ResponseDto("error", "500", "Package not found!");
        }
    }

    public ResponseDto delete(long packageId) throws Exception {

        PackageDetail packageDetail = packageDetailRepository.findByPackageIdAndIsActiveTrue(packageId);
        if (packageDetail == null) {
            return new ResponseDto("error", "500", "Package not found!");
        } else {
            packageDetail.setIsActive(false);
            packageDetailRepository.save(packageDetail);
        }
        return new ResponseDto("success", "200", "Package deactivated successfully!");
    }

    public ResponseDto active(long packageId) throws Exception {

        PackageDetail packageDetail = packageDetailRepository.findByPackageIdAndIsActiveFalse(packageId);
        if (packageDetail == null) {
            return new ResponseDto("error", "500", "Package not found!");
        } else {
            packageDetail.setIsActive(true);
            packageDetailRepository.save(packageDetail);
        }
        return new ResponseDto("success", "200", "Package activated successfully!");
    }

    public ResponseDto createCheckoutSession(long packageId, long userId) throws Exception {

        PackageDetail packageDetail = packageDetailRepository.findByPackageIdAndIsActiveTrue(packageId);
        PackageDetail trialPackage = packageDetailRepository.queryTrialPackage();
        UserDetail userDetail = authRepository.findUserByUserIdAndIsActiveTrue(userId);

        if (packageDetail == null || userDetail == null) {
            return new ResponseDto("error", "500", "User or package is not available!");
        } else {

            Stripe.apiKey = stripeSecretKey;

            String priceId = packageDetail.getPriceId();

            SessionCreateParams params = new SessionCreateParams.Builder()
                    .setSuccessUrl(stripeSuccessUrl)
                    .setCancelUrl(stripeErrorUrl)
                    .setCurrency("AUD")
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setCustomer(userDetail.getStripeCustomerId())
                    .setSubscriptionData(
                            SessionCreateParams.SubscriptionData.
                                    builder()
                                    .setTrialPeriodDays(trialPackage.getValidDayCount().longValue())
                                    .build())
                    .addLineItem(new SessionCreateParams.LineItem.Builder()
                            .setQuantity(1L)
                            .setPrice(priceId)
                            .build()
                    )
                    .build();

            Session session = Session.create(params);

            return new ResponseDto("success", "200", session.getUrl());
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto successPayment(long packageId, long userId) throws Exception {

        PackageDetail packageDetail = packageDetailRepository.findByPackageIdAndIsActiveTrue(packageId);
        PackageDetail trialPackage = packageDetailRepository.queryTrialPackage();
        UserDetail userDetail = authRepository.findUserByUserIdAndIsActiveTrue(userId);

        if (packageDetail == null || userDetail == null) {
            return new ResponseDto("error", "500", "User or package is not available!");
        } else {

            UserPackage existingUserPackage = userPackageRepository.findFirstByUserDetailAndIsActiveTrue(userDetail);
            if (existingUserPackage != null) {
                existingUserPackage.setIsActive(false);
                userPackageRepository.save(existingUserPackage);
            }

            Stripe.apiKey = stripeSecretKey;

            UserPackage userPackage = new UserPackage();
            userPackage.setPackageDetail(packageDetail);
            userPackage.setPackagePrice(packageDetail.getPackagePrice());
            userPackage.setUserDetail(userDetail);
            userPackage.setValidDayCount(packageDetail.getValidDayCount());
            userPackage.setTrialDayCount(trialPackage.getValidDayCount());
            userPackage.setIsActive(true);

            SubscriptionListParams params =
                    SubscriptionListParams.builder().setCustomer(userDetail.getStripeCustomerId()).build();
            SubscriptionCollection subscriptions = Subscription.list(params);

            String subscriptionId = null;
            for (Subscription data: subscriptions.getData()) {
                if (data.getStatus().equals("active") || data.getStatus().equals("trialing")) {
                    subscriptionId = data.getId();
                }
            }
            userPackage.setSubscriptionId(subscriptionId);

            Calendar packageCalendar = Calendar.getInstance();
            packageCalendar.setTime(new Date());
            packageCalendar.add(Calendar.DATE, trialPackage.getValidDayCount() + 1);

            userPackage.setStartDate(packageCalendar.getTime());

            packageCalendar.add(Calendar.DATE, packageDetail.getValidDayCount());

            userPackage.setExpireDate(packageCalendar.getTime());
            userPackageRepository.save(userPackage);
        }

        return new ResponseDto("success", "200", null);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseDto cancelSubscription(String email) throws Exception {

        UserDetail userDetail = authRepository.findFirstByEmailAndIsActiveTrue(email);
        UserPackage userPackage = userPackageRepository.findFirstByUserDetailAndIsActiveTrue(userDetail);

        if (userPackage == null || userDetail == null) {
            return new ResponseDto("error", "500", "User or package is not available!");
        } else {

            Stripe.apiKey = stripeSecretKey;

            Subscription subscription = Subscription.retrieve(userPackage.getSubscriptionId());
            SubscriptionCancelParams params = SubscriptionCancelParams.builder().build();
            subscription.cancel(params);

            userPackage.setIsActive(false);
            userPackageRepository.save(userPackage);

        }

        return new ResponseDto("success", "200", null);
    }
}
