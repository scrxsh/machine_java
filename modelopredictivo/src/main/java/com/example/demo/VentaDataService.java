package com.example.demo;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaDataService {


    @Autowired
    private VentaRepository ventaRepository;

    public INDArray cargarDatosVentas() {
        List<VentaModel> ventas = ventaRepository.findAll();

        if (ventas.isEmpty()) {
            throw new IllegalStateException("No hay datos de ventas disponibles.");
        }

        int numFilas = ventas.size();
        int numColumnas = 2;

        double[][] data = new double[numFilas][numColumnas];

        for (int i = 0; i < numFilas; i++) {
            VentaModel venta = ventas.get(i);
            data[i][0] = venta.getPrecio();
            data[i][1] = venta.getCantidad();
        }

        return Nd4j.create(data);
    }
}
