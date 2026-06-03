package com.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepo extends JpaRepository<Wallet, String> {

    public Wallet findBywalletId(String walletID);
    public Wallet findByuserID(Integer userID);
}
