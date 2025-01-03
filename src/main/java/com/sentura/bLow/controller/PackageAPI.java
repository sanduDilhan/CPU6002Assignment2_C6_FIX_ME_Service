package com.sentura.bLow.controller;

import com.sentura.bLow.dto.PackageDetailDTO;
import com.sentura.bLow.dto.ResponseDto;
import com.sentura.bLow.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/package")
public class PackageAPI {

    @Autowired
    private PackageService packageService;

    @GetMapping("/get-all/{keyword:.+}/{pageNumber}/{pageSize}")
    public ResponseEntity<ResponseDto> getAll(@PathVariable("keyword") String keyword,@PathVariable Integer pageNumber,@PathVariable Integer pageSize) throws Exception{
        return new ResponseEntity<>(packageService.getAll(keyword,pageNumber,pageSize), HttpStatus.OK);
    }

    @GetMapping("/get-all-deleted/{keyword:.+}/{pageNumber}/{pageSize}")
    public ResponseEntity<ResponseDto> getAllDeleted(@PathVariable("keyword") String keyword,@PathVariable Integer pageNumber,@PathVariable Integer pageSize) throws Exception{
        return new ResponseEntity<>(packageService.getAllDeleted(keyword,pageNumber,pageSize), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> create(@RequestBody PackageDetailDTO packageDetailDTO) throws Exception{
        return new ResponseEntity<>(packageService.save(packageDetailDTO), HttpStatus.OK);
    }

    @PostMapping("/update-trial")
    public ResponseEntity<ResponseDto> updateTrial(@RequestBody PackageDetailDTO packageDetailDTO) throws Exception{
        return new ResponseEntity<>(packageService.updateTrial(packageDetailDTO), HttpStatus.OK);
    }

    @GetMapping("/get-trial")
    public ResponseEntity<ResponseDto> getTrial() throws Exception{
        return new ResponseEntity<>(packageService.getTrial(), HttpStatus.OK);
    }

    @GetMapping("/get/{email:.+}")
    public ResponseEntity<ResponseDto> get(@PathVariable("email") String email) throws Exception{
        return new ResponseEntity<>(packageService.get(email), HttpStatus.OK);
    }

    @GetMapping("/get-users/{packageId}/{pageNumber}/{pageSize}")
    public ResponseEntity<ResponseDto> getUsers(@PathVariable("packageId") long packageId,@PathVariable Integer pageNumber,@PathVariable Integer pageSize) throws Exception{
        return new ResponseEntity<>(packageService.getUsers(packageId, pageNumber, pageSize), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{packageId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable("packageId") long packageId) throws Exception{
        return new ResponseEntity<>(packageService.delete(packageId), HttpStatus.OK);
    }

    @PutMapping("/active/{packageId}")
    public ResponseEntity<ResponseDto> active(@PathVariable("packageId") long packageId) throws Exception{
        return new ResponseEntity<>(packageService.active(packageId), HttpStatus.OK);
    }

    @GetMapping("/create-checkout-session/{packageId}/{userId}")
    public ResponseEntity<ResponseDto> createCheckoutSession(@PathVariable("packageId") long packageId, @PathVariable("userId") long userId) throws Exception{
        return new ResponseEntity<>(packageService.createCheckoutSession(packageId, userId), HttpStatus.OK);
    }

    @PostMapping("/success-payment/{packageId}/{userId}")
    public ResponseEntity<ResponseDto> successPayment(@PathVariable("packageId") long packageId, @PathVariable("userId") long userId) throws Exception{
        return new ResponseEntity<>(packageService.successPayment(packageId, userId), HttpStatus.OK);
    }

    @PostMapping("/cancel-subscription/{email:.+}")
    public ResponseEntity<ResponseDto> cancelSubscription(@PathVariable("email") String email) throws Exception{
        return new ResponseEntity<>(packageService.cancelSubscription(email), HttpStatus.OK);
    }
}
