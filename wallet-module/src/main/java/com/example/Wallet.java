package com.example;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String walletId;

    @NotNull
    @Column(unique = true)
    private Integer userID;

    public Long balance;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    public Date updatedAt;
}
