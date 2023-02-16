package com.bantads.accountOrchestrator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manager {
    private String uuid;
    private String name;
    private String cpf;
    private String telephone;
}
