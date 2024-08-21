package com.zz.cb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long supplierId;  // 添加一个唯一标识符

    private String name;

    @Enumerated(EnumType.STRING)
    private SupplierStatus status;

    // 不需要显式添加 getters 和 setters，因为使用了 @Data 注解
}