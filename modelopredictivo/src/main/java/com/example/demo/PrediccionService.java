package com.example.demo;


import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PrediccionService {
    private MultiLayerNetwork model;

    @Autowired
    private VentaDataService ventaDataService;


    public void inicializarModelo() {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(123456)
                .updater(new Adam(0.001))
                .list()
                .layer(new DenseLayer.Builder().nIn(1).nOut(10)
                        .activation(Activation.RELU)
                        .build())
                .layer(new DenseLayer.Builder().nIn(10).nOut(10)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(10).nOut(1)
                        .build())
                .build();

        model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(10));
    }


    public void entrenarModelo() {

        INDArray data = ventaDataService.cargarDatosVentas();


        INDArray features = data.getColumn(0);
        INDArray labels = data.getColumn(1);


        features = features.reshape(features.length(), 1);
        labels = labels.reshape(labels.length(), 1);


        DataSet dataset = new DataSet(features, labels);

        for (int i = 0; i < 1000; i++) {
            model.fit(dataset);
        }
    }


    public double predecirVenta(double precio) {
        INDArray input = Nd4j.create(new double[][]{{precio}});
        INDArray output = model.output(input);
        return output.getDouble(0);
    }
}
