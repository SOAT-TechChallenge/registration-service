package com.fiap.techChallenge.core.interfaces;

import com.fiap.techChallenge.core.application.dto.product.ProductDTO;
import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;

import java.util.List;
import java.util.UUID;

public interface CompositeDataSource {

    // Attendant
    AttendantDTO saveAttendant(AttendantDTO attendantDTO);
    AttendantDTO findFirstAttendantByCpf(String cpf);
    AttendantDTO findFirstAttendantById(UUID id);
    List<AttendantDTO> findAllAttendants();
    void deleteAttendant(UUID id);

    // Customer
    CustomerFullDTO saveCustomer(CustomerFullDTO attendantDTO);
    CustomerFullDTO findFirstCustomerByCpf(String cpf);
    CustomerFullDTO findFirstCustomerById(UUID id);
    List<CustomerFullDTO> findAllCustomerNotAnonym();
    void deleteCustomer(UUID id);

    // Product
    ProductDTO saveProduct(ProductDTO product);
    ProductDTO findProductById(UUID id);
    ProductDTO findProductByName(String name);
    List<Category> listAvailableProductCategories();
    List<ProductDTO> listProducts();
    List<ProductDTO> listProductsByCategory(Category category);
    List<ProductDTO> listProductsByStatusAndCategory(ProductStatus status, Category category);
    List<ProductDTO> listProductsByStatus(ProductStatus status);
    void deleteProduct(UUID id);
    void deleteProductByCategory(Category category);
}
