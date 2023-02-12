package com.bantads.accountOrchestrator.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String uuid;
    private String customer;
    private String manager;
    private Double limitAmount;
    private Double balance;
}
