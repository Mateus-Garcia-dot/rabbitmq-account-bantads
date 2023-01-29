package com.bantads.accountOrchestrator.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatus {
    private Account account;
    private String status;
    private String message;
}
