package com.bantads.accountOrchestrator.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String uuid;
    private Long customer;
    private Long manager;
    private Double limitAmount;
    private Double balance;
}
