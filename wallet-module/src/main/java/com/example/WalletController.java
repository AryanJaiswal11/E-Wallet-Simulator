package com.example;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/wallet/{walletID}")
    public Wallet getWallet(@PathVariable("walletID") String walletID){
        return this.walletService.findBywalletID(walletID);

    }

}
