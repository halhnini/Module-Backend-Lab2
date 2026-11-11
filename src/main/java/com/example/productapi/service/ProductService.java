package com.example.productapi.service;

import com.example.productapi.dto.ProductRequest;
import com.example.productapi.dto.ProductResponse;
import com.example.productapi.exception.InvalidQuantityException;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.model.Product;
import com.example.productapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    static final int MAX_QUANTITY = 1000;

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductResponse> getAllProducts() {
        return repository.findAll().stream().map(this::mapToResponse).toList();
    }

    public ProductResponse getProductById(Long id) {
        return mapToResponse(findProductOrThrow(id));
    }

    public List<ProductResponse> searchByName(String name) {
        String needle = name.toLowerCase();
        return repository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(needle))
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse createProduct(ProductRequest req) {
        checkQuantity(req.quantity());
        Product p = new Product(null, req.name(), req.description(), req.price(), req.quantity());
        return mapToResponse(repository.save(p));
    }

    public ProductResponse updateProduct(Long id, ProductRequest req) {
        Product p = findProductOrThrow(id);
        checkQuantity(req.quantity());
        p.setName(req.name());
        p.setDescription(req.description());
        p.setPrice(req.price());
        p.setQuantity(req.quantity());
        return mapToResponse(repository.save(p));
    }

    public void deleteProduct(Long id) {
        findProductOrThrow(id);
        repository.deleteById(id);
    }

    private Product findProductOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable avec l'id: " + id));
    }

    private void checkQuantity(Integer quantity) {
        if (quantity > MAX_QUANTITY) {
            throw new InvalidQuantityException("La quantité ne peut pas dépasser " + MAX_QUANTITY + " (reçu : " + quantity + ")");
        }
    }

    private ProductResponse mapToResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getQuantity());
    }
}
