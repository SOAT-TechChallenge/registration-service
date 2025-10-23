package com.fiap.techChallenge._webApi.data.persistence.repository.product;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fiap.techChallenge._webApi.mappers.ProductMapper;
import com.fiap.techChallenge._webApi.data.persistence.entity.product.ProductEntity;
import com.fiap.techChallenge.core.application.dto.product.ProductDTO;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.interfaces.ProductDataSource;

@Component
public class ProductDataSourceImpl implements ProductDataSource {

    private final JpaProductRepository repository;

    public ProductDataSourceImpl(JpaProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductDTO save(ProductDTO product) {
        ProductEntity entity = ProductMapper.toEntity(product);
        entity = repository.save(entity);

        return ProductMapper.toDTO(entity);
    }

    @Override
    public ProductDTO findById(UUID id) {
        return ProductMapper.toDTO(repository.findFirstById(id));
    }

    @Override
    public ProductDTO findByName(String name) {
        return ProductMapper.toDTO(repository.findFirstByName(name));
    }

    @Override
    public List<ProductDTO> list() {
        return repository.findAllByOrderByCategoryAscNameAsc().stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> listByStatus(ProductStatus status) {
        return repository.findByStatusOrderByCategoryAscNameAsc(status).stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> listByCategory(Category category) {
        return repository.findByCategoryOrderByNameAsc(category).stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> listByStatusAndCategory(ProductStatus status, Category category) {
        return repository.findByStatusAndCategoryOrderByCategoryAscNameAsc(status, category).stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByCategory(Category category) {
        repository.deleteByCategory(category);
    }

    @Override
    public List<Category> listAvailableCategorys() {
        return repository.listCategorysByProductStatus(ProductStatus.DISPONIVEL);
    }
}
