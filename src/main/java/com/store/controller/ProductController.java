package com.store.controller;

import com.store.dto.ProductDto;
import com.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String findAll(
            Model model,
            @RequestParam("page") Optional<Integer> rawPage,
            @RequestParam("size") Optional<Integer> rawSize,
            @RequestParam("filter") Optional<String> rawFilter,
            @RequestParam("sortBy") Optional<String> rawSortBy
    ) {
        int currentPage = rawPage.orElse(1);
        int pageSize = rawSize.orElse(10);
        String filter = rawFilter.orElse("");
        String sortBy = rawSortBy.orElse("name");

        Page<ProductDto> products = productService.findAll(currentPage - 1, pageSize, filter, sortBy);

        model.addAttribute("products", products);
        model.addAttribute("filter", filter);
        model.addAttribute("sortBy", sortBy);

        int totalPages = products.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "products-list";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable("id") Long id) {
        ProductDto product = productService.findById(id);

        model.addAttribute("product", product);
        return "product-description";
    }
}
