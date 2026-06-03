package com.example.controller;

import com.example.DTO.InitiateTxnDTO;
import com.example.Model.UserResponseModel;
import com.example.service.TxnService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TxnController {

    @Autowired
    private TxnService txnService;

    @PostMapping("/transaction")
    public String initiate(@Valid @RequestBody InitiateTxnDTO dto) {
        System.out.println("initiate Transaction: " + dto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserResponseModel user = (UserResponseModel) authentication.getPrincipal();
        Integer id = user.getUserId();
        System.out.println("initiate Transaction Sender Id: " + id);
        return txnService.initiate(dto, id);
    }

}
