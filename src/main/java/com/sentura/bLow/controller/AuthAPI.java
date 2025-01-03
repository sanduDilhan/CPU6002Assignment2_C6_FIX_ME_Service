package com.sentura.bLow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.sentura.bLow.configuration.JwtAuthenticationController;
import com.sentura.bLow.configuration.JwtRequest;
import com.sentura.bLow.configuration.JwtResponse;
import com.sentura.bLow.dto.AuthDto;
import com.sentura.bLow.dto.CustomEmailDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.dto.UserDetailDTO;
import com.sentura.bLow.entity.UserDetail;
import com.sentura.bLow.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthAPI {

    @Autowired
    private JwtAuthenticationController jwtAuthenticationController;

    @Autowired
    private AuthService authService;

    @PostMapping("/signIn")
    public ResponseEntity<ResponseDto> signIn(@RequestBody @Valid AuthDto authDto) throws DisabledException,BadCredentialsException {

        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername(authDto.getEmail());
        jwtRequest.setPassword(authDto.getPassword());

        JwtResponse jwtResponse = jwtAuthenticationController.generateAuthenticationToken(jwtRequest);

        UserDetail user = authService.findUserByEmail(authDto.getEmail());
        Map<String,Object> map = new HashMap<>();
        map.put("userId", user.getUserId());
        map.put("firstName", user.getFirstName());
        map.put("lastName", user.getLastName());
        map.put("role", user.getUserRole());
        map.put("email", user.getEmail());
        map.put("token", jwtResponse.getToken());

        return new ResponseEntity<>(new ResponseDto("success","200", map),HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto> signUp(@Valid @RequestBody UserDetailDTO userDto) throws Exception{
        return new ResponseEntity<>(authService.signUp(userDto), HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseDto> refreshToken(HttpServletRequest request) throws Exception{

        String refreshedToken = jwtAuthenticationController.getRefreshToken(request);
        return new ResponseEntity<>(new ResponseDto("success","200",refreshedToken), HttpStatus.OK);
    }

    @PostMapping("/forgetPassword/{email:.+}")
    public ResponseEntity<ResponseDto> forgetPassword(@PathVariable("email") String email) throws Exception{

        return new ResponseEntity<>(authService.forgetPassword(email),HttpStatus.ACCEPTED);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ResponseDto> forgetPassword(@RequestBody JsonNode jsonNode){

        String email = null;
        String resetCode = null;
        String password = null;

        if (jsonNode.get("email") != null) {
            email = jsonNode.get("email").asText();
        }
        if (jsonNode.get("resetCode") != null) {
            resetCode = jsonNode.get("resetCode").asText();
        }

        if (jsonNode.get("password") != null) {
            password = jsonNode.get("password").asText();
        }

        if (email != null && resetCode != null && password != null && !password.isEmpty() && !email.isEmpty()) {
            return new ResponseEntity<>(authService.resetPassword(email,resetCode, password),HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new ResponseDto("500","Email, Password and Reset Code Required!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<ResponseDto> verifyEmail(@RequestBody JsonNode jsonNode) throws Exception{

        String email = null;
        String verifyCode = null;
        if (jsonNode.get("email") != null) {
            email = jsonNode.get("email").asText();
        }
        if (jsonNode.get("verifyCode") != null) {
            verifyCode = jsonNode.get("verifyCode").asText();
        }

        if (email != null && verifyCode != null) {
            return new ResponseEntity<>(authService.verifyEmail(email,verifyCode),HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new ResponseDto("500","Email and Verify Code Required!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/resendVerifyEmail/{email:.+}")
    public ResponseEntity<ResponseDto> resendVerifyEmail(@PathVariable("email") String email) throws Exception{

        return new ResponseEntity<>(authService.resendVerifyEmail(email),HttpStatus.ACCEPTED);
    }

    @PostMapping("/updateUserDetail")
    public ResponseEntity<ResponseDto> updateUserDetail(@RequestBody @Valid UserDetailDTO userDetailDTO) throws Exception {
        return new ResponseEntity<>(new ResponseDto("success","200", authService.updateUserDetail(userDetailDTO)),HttpStatus.OK);
    }

    @GetMapping("/getUserDetailById/{userId}")
    public ResponseEntity<ResponseDto> getUserDetailByUserId(@PathVariable("userId") Long userId) throws Exception {
        return new ResponseEntity<>(new ResponseDto("success","200", authService.getPackageAndUserDetailbyUserId(userId)),HttpStatus.OK);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<ResponseDto> getAllUserDetail() throws Exception {
        return new ResponseEntity<>(new ResponseDto("success","200", authService.getAllUser()),HttpStatus.OK);
    }

    @GetMapping("/getAllAdmin")
    public ResponseEntity<ResponseDto> getAllAdminDetail() throws Exception {
        return new ResponseEntity<>(new ResponseDto("success","200", authService.getAllAdmin()),HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody JsonNode jsonNode) throws Exception{
        String email = null;
        String oldPassword = null;
        String newPassword = null;

        if (jsonNode.get("email") != null) {
            email = jsonNode.get("email").asText();
        }
        if (jsonNode.get("oldPassword") != null) {
            oldPassword = jsonNode.get("oldPassword").asText();
        }

        if (jsonNode.get("newPassword") != null) {
            newPassword = jsonNode.get("newPassword").asText();
        }

        if (email != null && oldPassword != null && newPassword != null && !newPassword.isEmpty() && !email.isEmpty() && !oldPassword.isEmpty()) {
            return new ResponseEntity<>(authService.changePassword(email,oldPassword,newPassword),HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new ResponseDto("500","Email, old password and new password Required!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sendCustomEmail")
    public ResponseEntity<ResponseDto> sendCustomEmail(@RequestBody CustomEmailDTO customEmailDTO) throws Exception{
        return new ResponseEntity<>(authService.sendCustomEmail(customEmailDTO),HttpStatus.ACCEPTED);
    }

    @PostMapping("/createUserByAdmin")
    public ResponseEntity<ResponseDto> CreateUserByAdmin(
            @RequestParam(value = "firstName") String firstName,
            @RequestParam(value = "lastName") String lastName,
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "userRole") String userRole,
            @RequestParam(value = "validDayCount") Integer validDayCount) throws Exception {
        return new ResponseEntity<>(new ResponseDto("success","200", authService.CreateUserByAdmin(firstName,lastName,userName,email,password,userRole,validDayCount)),HttpStatus.OK);
    }

    @GetMapping("/getAllAdminCreateUser")
    public ResponseEntity<ResponseDto> getAllAdminCreateUser() throws Exception {
        return new ResponseEntity<>(new ResponseDto("success","200", authService.getAllAdminCreateUser()),HttpStatus.OK);
    }

    @PostMapping("/UpdateExpireDateAdminCreateUser")
    public ResponseEntity<ResponseDto> updateExpireDateAdminCreateUser(
            @RequestParam(value = "userPackageId") Long userPackageId,
            @RequestParam(value = "validDayCount") Integer validDayCount) throws Exception {
        return new ResponseEntity<>(new ResponseDto("success","200", authService.updateExpireDateAdminCreateUser(userPackageId,validDayCount)),HttpStatus.OK);
    }
}
