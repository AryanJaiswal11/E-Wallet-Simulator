package com.example;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class WalletDTO {

    @NotNull
    @Column(unique = true)
    private Integer userID;
    private Long balance;

}
