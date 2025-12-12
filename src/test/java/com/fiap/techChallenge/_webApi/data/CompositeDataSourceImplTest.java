package com.fiap.techChallenge._webApi.data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.techChallenge._webApi.data.persistence.repository.user.CustomerDataSourceImpl;
import com.fiap.techChallenge.core.application.dto.product.ProductDTO;
import com.fiap.techChallenge.core.application.dto.user.AttendantDTO;
import com.fiap.techChallenge.core.application.dto.user.CustomerFullDTO;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;
import com.fiap.techChallenge.core.interfaces.AttendantDataSource;
import com.fiap.techChallenge.core.interfaces.ProductDataSource;

@ExtendWith(MockitoExtension.class)
class CompositeDataSourceImplTest {

    @Mock
    private AttendantDataSource attendantDataSource;

    @Mock
    private CustomerDataSourceImpl customerDataSource;

    @Mock
    private ProductDataSource productDataSource;

    @InjectMocks
    private CompositeDataSourceImpl compositeDataSource;

    @Test
    @DisplayName("Should handle all Attendant operations correctly")
    void shouldHandleAttendantOperations() {
        // Arrange
        UUID id = UUID.randomUUID();
        String cpf = "12345678900";
        
        AttendantDTO dto = AttendantDTO.builder()
                .id(id)
                .name("Attendant Name")
                .cpf(cpf)
                .email("attendant@test.com")
                .build();
        
        List<AttendantDTO> list = Collections.singletonList(dto);

        when(attendantDataSource.save(dto)).thenReturn(dto);
        when(attendantDataSource.findFirstByCpf(cpf)).thenReturn(dto);
        when(attendantDataSource.findFirstById(id)).thenReturn(dto);
        when(attendantDataSource.findAll()).thenReturn(list);

        // Act & Assert
        assertEquals(dto, compositeDataSource.saveAttendant(dto));
        assertEquals(dto, compositeDataSource.findFirstAttendantByCpf(cpf));
        assertEquals(dto, compositeDataSource.findFirstAttendantById(id));
        assertEquals(list, compositeDataSource.findAllAttendants());
        
        compositeDataSource.deleteAttendant(id);
        verify(attendantDataSource).delete(id);
    }

    @Test
    @DisplayName("Should handle all Customer operations correctly")
    void shouldHandleCustomerOperations() {
        // Arrange
        UUID id = UUID.randomUUID();
        String cpf = "12345678900";
        
        CustomerFullDTO dto = CustomerFullDTO.builder()
                .id(id)
                .name("Customer Name")
                .cpf(cpf)
                .email("customer@test.com")
                .anonymous(false)
                .build();
        
        List<CustomerFullDTO> list = Collections.singletonList(dto);

        when(customerDataSource.save(dto)).thenReturn(dto);
        when(customerDataSource.findFirstByCpf(cpf)).thenReturn(dto);
        when(customerDataSource.findFirstById(id)).thenReturn(dto);
        when(customerDataSource.findAllNotAnonym()).thenReturn(list);

        // Act & Assert
        assertEquals(dto, compositeDataSource.saveCustomer(dto));
        assertEquals(dto, compositeDataSource.findFirstCustomerByCpf(cpf));
        assertEquals(dto, compositeDataSource.findFirstCustomerById(id));
        assertEquals(list, compositeDataSource.findAllCustomerNotAnonym());

        compositeDataSource.deleteCustomer(id);
        verify(customerDataSource).delete(id);
    }

    @Test
    @DisplayName("Should handle all Product operations correctly")
    void shouldHandleProductOperations() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "X-Burger";
        Category category = Category.LANCHE;
        // Atualizado para usar o Enum correto fornecido (DISPONIVEL/INDISPONIVEL)
        ProductStatus status = ProductStatus.DISPONIVEL; 
        
        ProductDTO dto = ProductDTO.builder()
                .id(id)
                .name(name)
                .description("Delicious burger")
                .price(BigDecimal.TEN)
                .category(category)
                .status(status)
                .image("image.jpg")
                .build();

        List<ProductDTO> list = Collections.singletonList(dto);
        List<Category> categories = Collections.singletonList(category);

        when(productDataSource.save(dto)).thenReturn(dto);
        when(productDataSource.findById(id)).thenReturn(dto);
        when(productDataSource.findByName(name)).thenReturn(dto);
        when(productDataSource.listAvailableCategorys()).thenReturn(categories);
        when(productDataSource.list()).thenReturn(list);
        when(productDataSource.listByCategory(category)).thenReturn(list);
        when(productDataSource.listByStatusAndCategory(status, category)).thenReturn(list);
        when(productDataSource.listByStatus(status)).thenReturn(list);

        // Act & Assert
        assertEquals(dto, compositeDataSource.saveProduct(dto));
        assertEquals(dto, compositeDataSource.findProductById(id));
        assertEquals(dto, compositeDataSource.findProductByName(name));
        assertEquals(categories, compositeDataSource.listAvailableProductCategories());
        assertEquals(list, compositeDataSource.listProducts());
        assertEquals(list, compositeDataSource.listProductsByCategory(category));
        assertEquals(list, compositeDataSource.listProductsByStatusAndCategory(status, category));
        assertEquals(list, compositeDataSource.listProductsByStatus(status));

        compositeDataSource.deleteProduct(id);
        verify(productDataSource).delete(id);

        compositeDataSource.deleteProductByCategory(category);
        verify(productDataSource).deleteByCategory(category);
    }
}