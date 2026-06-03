package com.example.Model;

import com.example.Repo.TxnStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TxnModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String externalTxnId;

    @Enumerated(EnumType.STRING)
    public TxnStatus txnStatus;

    @NotNull
    private Integer SenderId;

    @NotNull
    private Integer ReceiverId;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @Min(1)
    private Long amount;

    private String msg;
}
