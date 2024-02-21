package com.example.learningesapi.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Fang Jinxu
 * @Description:
 * @Date: 2024-02-21 10:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String sku;

    private String name;

    private Float price;
}