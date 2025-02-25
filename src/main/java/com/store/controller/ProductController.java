package com.store.controller;

import com.store.dto.ProductDto;
import com.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String findAll(Model model) {
        List<ProductDto> all = productService.findAll();

        model.addAttribute("products", all);
        return "products-list";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable("id") Long id) {
        ProductDto product = productService.findById(id);

        model.addAttribute("product", product);
        return "product-description";
    }
}
