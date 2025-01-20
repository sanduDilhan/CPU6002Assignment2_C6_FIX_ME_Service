package com.sentura.bLow.service;

import com.sentura.bLow.dto.MailChimpRequestDTO;
import com.sentura.bLow.dto.NewsLetterSubscriptionRequestDTO;
import com.sentura.bLow.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class MailchimpService {

//    @Value("${mailchimp.api-key}")
    private String API_KEY="88a6848529c968a67267cbaa056278d1-us3";

//    @Value("${mailchimp.list-id}")
    private String LIST_ID="1275bb0541";

    private String STATUS = "subscribed";

    private final RestTemplate restTemplate;

    public MailchimpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseDto subscribeService(NewsLetterSubscriptionRequestDTO subscriptionRequestDTO) {
        MailChimpRequestDTO requestDTO = new MailChimpRequestDTO(subscriptionRequestDTO.getEmail(),STATUS);

        String hashedEmail = this.md5Encode(subscriptionRequestDTO.getEmail());

        String auth = Base64.encodeBase64String(("user:" + API_KEY).getBytes());

        String datacenter = API_KEY.substring(API_KEY.lastIndexOf('-') + 1);
        String mailchimpUrl = "https://" + datacenter + ".api.mailchimp.com/3.0/lists/" + LIST_ID + "/members/" + hashedEmail;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        headers.set("Authorization","Basic "+auth);

        HttpEntity<Object> request = new HttpEntity<>(requestDTO, headers);

        System.out.println(MailChimpRequestDTO.class);
        ResponseEntity<MailChimpRequestDTO> exchange = restTemplate.exchange(mailchimpUrl, HttpMethod.PUT, request,
                MailChimpRequestDTO.class);

        if(exchange.getBody().getStatus().equals(STATUS)){
            return new ResponseDto("success", "200", "Thank You for Subscribing Our Newsletter");
        }else{
            return new ResponseDto("failed", "500", "Something Went Wrong");
        }
    }

    private String md5Encode(String data){
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes());
            byte[] messageDigest = md.digest();
            return DatatypeConverter.printHexBinary(messageDigest).toLowerCase();
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
