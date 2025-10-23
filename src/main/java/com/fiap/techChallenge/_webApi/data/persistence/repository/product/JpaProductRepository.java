package com.fiap.techChallenge._webApi.data.persistence.repository.product;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.techChallenge._webApi.data.persistence.entity.product.ProductEntity;
import com.fiap.techChallenge.core.domain.enums.Category;
import com.fiap.techChallenge.core.domain.enums.ProductStatus;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {

   ProductEntity findFirstById(UUID id);

   ProductEntity findFirstByName(String name);

    List<ProductEntity> findAllByOrderByCategoryAscNameAsc();

    List<ProductEntity> findByStatusOrderByCategoryAscNameAsc(ProductStatus status);

    List<ProductEntity> findByCategoryOrderByNameAsc(Category category);

    List<ProductEntity> findByStatusAndCategoryOrderByCategoryAscNameAsc(ProductStatus status, Category category);

    @Query("SELECT DISTINCT p.category FROM ProductEntity p WHERE p.status = :status")
    List<Category> listCategorysByProductStatus(@Param("status") ProductStatus status);

    @Transactional
    @Modifying
    void deleteByCategory(Category category);

}
