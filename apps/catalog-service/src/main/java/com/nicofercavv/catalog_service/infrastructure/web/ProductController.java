package com.nicofercavv.catalog_service.infrastructure.web;

import com.nicofercavv.catalog_service.domain.model.Sneaker;
import com.nicofercavv.catalog_service.domain.model.SneakerVariation;
import com.nicofercavv.catalog_service.domain.usecase.CreateProductUseCase;
import com.nicofercavv.catalog_service.domain.usecase.SearchProductsByImageUseCase;
import com.nicofercavv.catalog_service.infrastructure.repository.MongoProductRepository;
import com.nicofercavv.catalog_service.infrastructure.web.dto.CreateProductRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalog")
public class ProductController {

    private final MongoProductRepository repository;
    private final SearchProductsByImageUseCase searchProductsByImageUseCase;
    private final CreateProductUseCase createProductUseCase;

    public ProductController(MongoProductRepository repository,  SearchProductsByImageUseCase searchProductsByImageUseCase, CreateProductUseCase createProductUseCase) {
        this.repository = repository;
        this.searchProductsByImageUseCase = searchProductsByImageUseCase;
        this.createProductUseCase = createProductUseCase;
    }

    // 1. Listar todos os tênis
    @GetMapping
    public ResponseEntity<List<Sneaker>> getAllProducts() {
        return ResponseEntity.ok(repository.findAll());
    }

    // 2. Buscar detalhes de um tênis específico
    @GetMapping("/{id}")
    public ResponseEntity<Sneaker> getProductById(@PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Cadastrar um novo tênis (Alimentação via Admin/BFF)
    @PostMapping
    public ResponseEntity<Sneaker> createProduct(@Valid @RequestBody CreateProductRequest request) {
        List<SneakerVariation> variations = request.variations().stream().map(v -> {
            SneakerVariation variation = new SneakerVariation();
            variation.setSku(v.sku());
            variation.setSize(v.size());
            variation.setInStock(false);
            return variation;
        }).collect(Collectors.toList());

        Sneaker newSneaker = Sneaker.builder()
                .name(request.name())
                .brand(request.brand())
                .description(request.description())
                .priceInCents(request.priceInCents())
                .imageUrls(request.imageUrls())
                .isAvailable(false)
                .variations(variations)
                .build();

        Sneaker saved = createProductUseCase.execute(newSneaker);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/search/image")
    public ResponseEntity<List<Sneaker>> searchByImage(@RequestParam("image") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Executa o caso de uso convertendo para byte[] puro (independente de framework)
        List<Sneaker> similarSneakers = searchProductsByImageUseCase.execute(file.getBytes());

        return ResponseEntity.ok(similarSneakers);
    }
}
