package com.booking.app.model;

import com.booking.app.model.enums.Transactionstatus;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.Transaction;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.transaction.TransactionStatus;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    private UUID paymentId;
//had to change to char type as it wasnt able to do getbyid


    @JsonDeserialize(using = ToLowerCaseDeserializer.class)
    @Column(nullable = false)
    private String paymentMode;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Transactionstatus transactionStatus;

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp paymentCreateDate;


    //datestamp
    //enum mode of payment
    //payment should be done then booking id is generated


}
