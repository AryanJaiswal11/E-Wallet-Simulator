package com.example.Repo;

import com.example.Model.TxnModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TxnRepo extends JpaRepository<TxnModel, Integer> {
    public TxnModel findByExternalTxnId(String id);
}
