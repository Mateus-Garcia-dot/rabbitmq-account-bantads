package com.bantads.accountOrchestrator.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long id;
    private Long cliente;
    private Long gerente;
    private Double limite;
    private Double saldo;
}
