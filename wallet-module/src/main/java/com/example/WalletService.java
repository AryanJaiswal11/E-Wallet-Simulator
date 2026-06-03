package com.example;

import io.lettuce.core.json.JsonObject;
import org.apache.kafka.common.internals.Topic;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class WalletService {

    @Value("${wallet.initial.bonus}")
    private Long initialBonus;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    JSONParser jsonParser;

    private final String TOPIC = "WALLET_UPDATED";

    public Wallet findBywalletID(String walletID){
        return walletRepo.findBywalletId(walletID);
    }

    //Defining Kafka Consumer
    @KafkaListener(topics = "user-created" , groupId = "wallet")
    public void listen(String message) throws ParseException {
        try {
            JSONObject data = (JSONObject) this.jsonParser.parse(message);
            Integer userID = ((Number) data.get("id")).intValue();
            Wallet wallet = Wallet.builder()
                    .userID(userID)
                    .balance(initialBonus)
                    .build();
            System.out.println("WalletId: " + wallet.getWalletId());
            this.walletRepo.save(wallet);

            //todo: publish a kafka event upon succsessful wallet creation, so that notification
            //      service can notify user


        }
        catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @KafkaListener(topics = "Txn-Initiated", groupId = "wallet-2")
    public void listen2(String message) throws ParseException {
        try {
            System.out.println("Received message: " + message);
            JSONObject data = (JSONObject) this.jsonParser.parse(message);
            Integer senderID = ((Number) data.get("SenderId")).intValue();
            Integer receiverID = ((Number) data.get("ReceiverId")).intValue();
            Long amount = (Long) data.get("amount");
            String ExtID =  (String) data.get("externalTxnId");
            System.out.println("ExtID: " + ExtID);
            Wallet Sender = this.walletRepo.findByuserID(senderID);
            Wallet Receiver = this.walletRepo.findByuserID(receiverID);

            //todo: publish kafka event
            JSONObject WalletUpdateData = new JSONObject();
            WalletUpdateData.put("senderID", senderID);
            WalletUpdateData.put("receiverID", receiverID);
            WalletUpdateData.put("amount", amount);
            WalletUpdateData.put("ExternalTxnID", ExtID);
            WalletUpdateData.put("SenderWalletID", Sender);
            WalletUpdateData.put("ReceiverWalletID", Receiver);

            if(Sender==null||Sender.getBalance() < amount){
                //todo: Publish Transaction Failed
                WalletUpdateData.put("Status", "FAILED");//not txnStatus
                WalletUpdateData.put("msg", "NOT ENOUGH BALANCE");
                this.kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(WalletUpdateData));
                return;
            }
            if(Receiver==null){
                //todo: Publish Receiver Account Does Not Exists
                WalletUpdateData.put("Status", "FAILED");//not txnStatus
                WalletUpdateData.put("msg", "Account Not Found");
                this.kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(WalletUpdateData));
                return;
            }

            Sender.setBalance(Sender.getBalance()-amount);
            Receiver.setBalance(Receiver.getBalance()+amount);
            this.walletRepo.saveAll(List.of(Sender, Receiver));

            //TODO: Publish kafka event for succsess
            WalletUpdateData.put("Status", "SUCCESS");//not txnStatus
            this.kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(WalletUpdateData));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
