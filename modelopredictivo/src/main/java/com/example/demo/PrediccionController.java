package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prediccion")
public class PrediccionController {

    @Autowired
    PrediccionService prediccionService;

    @PostMapping("/entrenar")
    public ResponseEntity<String> entrenarModelo() {
        prediccionService.inicializarModelo();
        prediccionService.entrenarModelo();
        return ResponseEntity.ok("Modelo entrenado");
    }

    @GetMapping("/venta")
    public ResponseEntity<Double> predecirVenta(@RequestParam double precio) {
        double prediccion = prediccionService.predecirVenta(precio);
        return ResponseEntity.ok(prediccion);
    }
}
