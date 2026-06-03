package com.example.DTO;


import com.example.Model.TxnModel;
import com.example.Repo.TxnStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class InitiateTxnDTO {
    @NotNull
    private Integer receiverId;

    @Min(1)
    @NotNull
    private Long amount;

    private String message;

    public TxnModel to(){
        return TxnModel.builder()
                .externalTxnId(UUID.randomUUID().toString())
                .msg(this.message)
                .amount(this.amount)
                .ReceiverId(this.receiverId)
                .txnStatus(TxnStatus.PENDING)
                //.SenderId(this.senderId)
                .build();
    }
}
