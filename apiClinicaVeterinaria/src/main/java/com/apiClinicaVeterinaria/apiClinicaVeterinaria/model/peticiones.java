package com.apiClinicaVeterinaria.apiClinicaVeterinaria.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class peticiones {

    private List<mascota> listaMascotas = new ArrayList<>();
    public peticiones() {
        listaMascotas.add(new mascota("M001", "Rocky", "Perro", LocalDate.of(2020, 5, 15), "Maria Sevallos"));
        listaMascotas.add(new mascota("M002", "Jack", "Gato", LocalDate.of(2021, 8, 22), "Ismael Ordoñez"));
        listaMascotas.add(new mascota("M003", "Luna", "Perro", LocalDate.of(2019, 3, 17), "Alan López"));
        listaMascotas.add(new mascota("M004", "Simba", "Gato", LocalDate.of(2021, 1, 25), "Anita Rodríguez"));
        listaMascotas.add(new mascota("M005", "Malu", "Hámster", LocalDate.of(2025, 11, 30), "Alex Sánchez"));
    }

    // 1.	GET /mascotas
    @GetMapping("/mascotas")
    public ResponseEntity<List<mascota>> obtenerTodasLasMascotas() {
        return ResponseEntity
                .status(HttpStatus.OK)   // 200 OK
                .header("Content-Type", "application/json")
                .header("Cache-Control", "no-cache")
                .header("X-App-Version", "1.0")
                .header("X-Response-Time", LocalDateTime.now().toString())
                .body(listaMascotas);
    }

    // 2. GET /mascotas/{id}
    @GetMapping("/mascotas/{id}")
    public ResponseEntity<mascota> obtenerMascotaPorId(@PathVariable String id) {
        mascota mascotaEncontrada = null;
        for (mascota m : listaMascotas) {
            if (m.getId().equals(id)) {
                mascotaEncontrada = m;
                break;
            }
        }

        // 404 NOT FOUND
        if (mascotaEncontrada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // 200 OK
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .header("Cache-Control", "no-cache")
                .header("X-App-Version", "1.0")
                .header("X-Response-Time", LocalDateTime.now().toString())
                .body(mascotaEncontrada);
    }

    // 3.	GET /mascotas/menores?edad=...
    @GetMapping("/mascotas/menores")
    public ResponseEntity<List<mascota>> obtenerMascotasMenores(@RequestParam int edad) {

        List<mascota> mascotasMenores = new ArrayList<>();
        LocalDate fechaActual = LocalDate.now();

        for (mascota menor : listaMascotas) {
            Period periodo = Period.between(menor.getFechaNacimiento(), fechaActual);
            int edadMascota = periodo.getYears();

            if (edadMascota < edad) {
                mascotasMenores.add(menor);
            }
        }

        // 404 NOT FOUND
        if (mascotasMenores.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("X-Message", "No se encontraron mascotas menores de " + edad + " años")
                    .build();
        }

        // 200 OK
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .header("Cache-Control", "no-cache")
                .header("X-App-Version", "1.0")
                .header("X-Response-Time", LocalDateTime.now().toString())
                .header("X-Edad-Filtro", String.valueOf(edad))
                .header("X-Resultados", String.valueOf(mascotasMenores.size()))
                .body(mascotasMenores);
    }

    // 4.	POST /mascotas
    @PostMapping("/mascotas")
    public ResponseEntity<mascota> registrarMascota(@RequestBody mascota nuevaMascota) {
        if (nuevaMascota.getId() == null || nuevaMascota.getId().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        for (mascota m : listaMascotas) {
            if (m.getId().equals(nuevaMascota.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        if (nuevaMascota.getNombre() == null || nuevaMascota.getNombre().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (nuevaMascota.getEspecie() == null || nuevaMascota.getEspecie().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (nuevaMascota.getPropietario() == null || nuevaMascota.getPropietario().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (nuevaMascota.getFechaNacimiento() != null &&
                nuevaMascota.getFechaNacimiento().isAfter(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        listaMascotas.add(nuevaMascota);
        System.out.println("Mascota creada");
        System.out.println("ID: " + nuevaMascota.getId());
        System.out.println("Nombre: " + nuevaMascota.getNombre());
        System.out.println("Especie: " + nuevaMascota.getEspecie());
        System.out.println("Fecha Nacimiento: " + nuevaMascota.getFechaNacimiento());
        System.out.println("Propietario: " + nuevaMascota.getPropietario());

        // 201 CREATED
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Content-Type", "application/json")
                .header("Cache-Control", "no-cache")
                .header("X-App-Version", "1.0")
                .header("X-Response-Time", LocalDateTime.now().toString())
                .header("Location", "/api/mascotas/" + nuevaMascota.getId())
                .body(nuevaMascota);
    }
}
