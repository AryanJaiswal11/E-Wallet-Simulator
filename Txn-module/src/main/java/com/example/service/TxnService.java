package com.example.service;

import com.example.DTO.InitiateTxnDTO;
import com.example.Model.TxnModel;
import com.example.Repo.TxnRepo;
import com.example.Repo.TxnStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class TxnService {

    public final String Txn_Initiate_Topic="Txn-Initiated";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TxnRepo txnRepo;

    @Autowired
    JSONObject jsonObject;

    public String initiate(InitiateTxnDTO request, Integer id){
        TxnModel txnModel = request.to();
        txnModel.setSenderId(id);
        this.txnRepo.save(txnModel);
        String data = objectMapper.writeValueAsString(txnModel);
        kafkaTemplate.send(Txn_Initiate_Topic, data);
        return txnModel.getExternalTxnId();
    }

    @KafkaListener(topics = "WALLET_UPDATED", groupId = "txn")
    public void WalletUpdate(String msg) throws ParseException {
        System.out.println("Received message: " + msg);
        JSONObject data = (JSONObject) new JSONParser().parse(msg);
        String status = data.get("Status").toString();
        String externaltxnId = data.get("ExternalTxnID").toString();
        TxnModel txnModel = this.txnRepo.findByExternalTxnId(externaltxnId);

        if(status.equals("FAILED")){
            String reason = data.get("msg").toString();
            txnModel.setTxnStatus(TxnStatus.FAILED);
            System.out.println(reason);
        }
        else{
            txnModel.setTxnStatus(TxnStatus.SUCCESS);
        }
        this.txnRepo.save(txnModel);

    }
}
