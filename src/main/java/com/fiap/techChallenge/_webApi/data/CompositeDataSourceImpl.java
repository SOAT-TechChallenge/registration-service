package com.fiap.techChallenge._webApi.data;

import com.fiap.techChallenge._webApi.data.persistence.repository.user.CustomerDataSourceImpl;
import com.fiap.techChallenge.core.application.dto.product.ProductDTO;
import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.interfaces.AttendantDataSource;
import com.fiap.techChallenge.core.interfaces.CompositeDataSource;
import com.fiap.techChallenge.core.interfaces.ProductDataSource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CompositeDataSourceImpl implements CompositeDataSource {

    private final AttendantDataSource attendantDataSource;
    private final CustomerDataSourceImpl customerDataSource;
    private final ProductDataSource productDataSource;

    @Override
    public AttendantDTO saveAttendant(AttendantDTO attendantDTO) {
        return attendantDataSource.save(attendantDTO);
    }

    @Override
    public AttendantDTO findFirstAttendantByCpf(String cpf) {
        return attendantDataSource.findFirstByCpf(cpf);
    }

    @Override
    public AttendantDTO findFirstAttendantById(UUID id) {
        return attendantDataSource.findFirstById(id);
    }

    @Override
    public List<AttendantDTO> findAllAttendants() {
        return attendantDataSource.findAll();
    }

    @Override
    public void deleteAttendant(UUID id) {
        attendantDataSource.delete(id);
    }

    @Override
    public CustomerFullDTO saveCustomer(CustomerFullDTO attendantDTO) {
        return customerDataSource.save(attendantDTO);
    }

    @Override
    public CustomerFullDTO findFirstCustomerByCpf(String cpf) {
        return customerDataSource.findFirstByCpf(cpf);
    }

    @Override
    public CustomerFullDTO findFirstCustomerById(UUID id) {
        return customerDataSource.findFirstById(id);
    }

    @Override
    public List<CustomerFullDTO> findAllCustomerNotAnonym() {
        return customerDataSource.findAllNotAnonym();
    }

    @Override
    public void deleteCustomer(UUID id) {
        customerDataSource.delete(id);
    }

    @Override
    public ProductDTO saveProduct(ProductDTO product) {
        return productDataSource.save(product);
    }

    @Override
    public ProductDTO findProductById(UUID id) {
        return productDataSource.findById(id);
    }

    @Override
    public ProductDTO findProductByName(String name) {
        return productDataSource.findByName(name);
    }

    @Override
    public List<Category> listAvailableProductCategories() {
        return productDataSource.listAvailableCategorys();
    }

    @Override
    public List<ProductDTO> listProducts() {
        return productDataSource.list();
    }

    @Override
    public List<ProductDTO> listProductsByCategory(Category category) {
        return productDataSource.listByCategory(category);
    }

    @Override
    public List<ProductDTO> listProductsByStatusAndCategory(ProductStatus status, Category category) {
        return productDataSource.listByStatusAndCategory(status, category);
    }

    @Override
    public List<ProductDTO> listProductsByStatus(ProductStatus status) {
        return productDataSource.listByStatus(status);
    }

    @Override
    public void deleteProduct(UUID id) {
        productDataSource.delete(id);
    }

    @Override
    public void deleteProductByCategory(Category category) {
        productDataSource.deleteByCategory(category);
    }
}
