package com.zz.cb.repository;

import com.zz.cb.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findById(Long id);
    Optional<Supplier> findBySupplierId(Long supplierId);  // 添加通过 supplierId 查找的方法
}