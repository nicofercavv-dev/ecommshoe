package com.nicofercavv.catalog_service.infrastructure.ai;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OllamaEmbeddingAdapter {

    private final EmbeddingModel embeddingModel;
    private final RestTemplate restTemplate; // Usado para baixar a imagem da URL

    public OllamaEmbeddingAdapter(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
        this.restTemplate = new RestTemplate();
    }

    public List<Double> generateEmbeddingFromUrl(String imageUrl) {
        byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
        return generateEmbeddingFromBytes(imageBytes);
    }

    public List<Double> generateEmbeddingFromBytes(byte[] imageBytes) {
        if (imageBytes == null) return List.of();

        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        EmbeddingResponse response = embeddingModel.embedForResponse(List.of(base64Image));

        float[] floatList = response.getResults().get(0).getOutput();

        List<Double> doubleList = new ArrayList<>(floatList.length);
        for (float f : floatList) {
            doubleList.add((double) f);
        }

        return doubleList;
    }
}
