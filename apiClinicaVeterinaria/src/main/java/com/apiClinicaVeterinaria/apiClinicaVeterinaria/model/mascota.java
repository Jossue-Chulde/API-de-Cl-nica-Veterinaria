package com.apiClinicaVeterinaria.apiClinicaVeterinaria.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class mascota {
    private String id;
    private String nombre;
    private String especie;
    private LocalDate fechaNacimiento;
    private String propietario;
}