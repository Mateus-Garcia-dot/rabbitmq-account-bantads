package com.bantads.accountOrchestrator.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatus implements Serializable {
    private Account account;
    private String status;
    private String message;
}
