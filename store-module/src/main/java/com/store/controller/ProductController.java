package com.store.controller;

import com.store.dto.ProductDto;
import com.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Mono<String> findAll(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "filter", defaultValue = "") String filter,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy
    ) {
        return productService.searchProducts(page - 1, size, filter, sortBy)
                .doOnNext(response -> {
                    long totalPages = response.getTotalElements();
                    if (totalPages > 0) {
                        List<Long> pageNumbers = LongStream.rangeClosed(1L, totalPages)
                                .boxed()
                                .collect(Collectors.toList());
                        model.addAttribute("pageNumbers", pageNumbers);
                    }

                    model.addAttribute("products", response.getData());
                    model.addAttribute("page", page);
                    model.addAttribute("size", size);
                    model.addAttribute("totalPages", (int) Math.ceil((double) response.getTotalElements() / size));

                    model.addAttribute("filter", filter);
                    model.addAttribute("sortBy", sortBy);
                })
                .thenReturn("products-list");
    }

    @GetMapping("/{id}")
    public Mono<String> findById(Model model, @PathVariable("id") Long id) {
        return productService.findById(id)
                .doOnNext(product -> model.addAttribute("product", product))
                .thenReturn("product-description");
    }

    @PostMapping
    public Mono<String> save(@ModelAttribute ProductDto product) {
        return productService.save(product)
                .thenReturn("redirect:/products");
    }
}
