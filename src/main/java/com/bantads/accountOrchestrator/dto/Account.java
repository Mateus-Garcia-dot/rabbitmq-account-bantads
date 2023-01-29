package com.bantads.accountOrchestrator.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {
    private Long id;
    private Long cliente;
    private Long gerente;
    private Double limite;
    private Double saldo;
}
