package com.bantads.accountOrchestrator.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {
    private Long id;
    private Long customer;
    private Long manager;
    private Double limitAmount;
    private Double balance;
}
