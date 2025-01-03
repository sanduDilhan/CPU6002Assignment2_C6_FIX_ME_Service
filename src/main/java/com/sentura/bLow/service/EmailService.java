package com.sentura.bLow.service;

import com.sentura.bLow.constants.EmailTypes;
import com.sentura.bLow.dto.CustomEmailDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.entity.EmailTemplate;
import com.sentura.bLow.repository.EmailTemplateRepository;
import com.sentura.bLow.util.SendMail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private SendMail sendMail;

    public void sendUserVerificationEmail(String email, String verifyCode, String customerName) throws Exception{

        Map<String,String> map = new HashMap<>();
        map.put("xVerifyCode",verifyCode);
        map.put("xCustomerName",customerName);

        formatEmailContentAndSend(map,null,true, EmailTypes.USER_VERIFY_EMAIL,email,null);
    }

    public void sendForgetPasswordRequestEmail(String email, String verifyCode, String customerName) throws Exception{

        Map<String,String> map = new HashMap<>();
        map.put("xResetCode",verifyCode);
        map.put("xEmail",email);
        map.put("xCustomerName",customerName);

        formatEmailContentAndSend(map,null,true, EmailTypes.FORGET_PASSWORD_REQUEST_EMAIL,email,null);
    }

    public void sendUserVerifiedEmail(String email, String customerName) throws Exception{

        Map<String,String> map = new HashMap<>();
        map.put("xEmail",email);
        map.put("xCustomerName",customerName);

        formatEmailContentAndSend(map,null,true, EmailTypes.USER_VERIFIED_EMAIL,email,null);
    }

    public void sendPaymentCompletedEmail(String email, String amount, String coins) throws Exception{

        Map<String,String> map = new HashMap<>();
        map.put("xEmail",email);
        map.put("xAmount","₹ " + amount);
        map.put("xCoins","Rs. " + coins);

        formatEmailContentAndSend(map,null,true, EmailTypes.PAYMENT_COMPLETED,email,null);
    }

    public void sendPaymentReceivedEmail(String xFromEmail, String xToEmail, String xCoins) throws Exception{

        Map<String,String> map = new HashMap<>();
        map.put("XFromEmail",xFromEmail);
        map.put("xToEmail",xToEmail);
        map.put("xCoins","Rs. " + xCoins);

        formatEmailContentAndSend(map,null,true, EmailTypes.PAYMENT_RECEIVED,xToEmail,null);
    }

    public void customEmail(CustomEmailDTO customEmailDTO) throws Exception {

        Map<String,String> map = new HashMap<>();
        map.put("xCustomerEmail",customEmailDTO.getCustomerEmail());
        map.put("xCustomerName",customEmailDTO.getCustomerName());
        map.put("xEmailSubject",customEmailDTO.getEmailSubject());
        map.put("xEmailBody",customEmailDTO.getEmailBody());

//        String xToEmail = "sandundilhan123@gmail.com";
        String xToEmail = "info@b-low.com.au";

        formatEmailContentAndSend(map,null,true, EmailTypes.CUSTOM_EMAIL,xToEmail,null);
    }

    private void formatEmailContentAndSend(Map<String,String> keywordMap,Map<String,String> attachmentData, boolean isHtmlBody, String emailType, String to, String cc) throws Exception {

        EmailTemplate emailTemplate = emailTemplateRepository.findByEmailTypeAndActiveTrue(emailType);

        if (emailType != null) {
            String template = emailTemplate.getTemplate();
            for(String key: keywordMap.keySet()) {
                template = template.replace(key,keywordMap.get(key));
            }

            sendMail.sendEmail(to, cc, emailTemplate.getSubject(), template,isHtmlBody,attachmentData);

        } else {
            throw new Exception("Email Type Not Found");
        }
    }

}
