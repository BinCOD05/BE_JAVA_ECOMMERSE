package vn.web.Services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.web.Controller.Request.ProductCreationRequest;
import vn.web.Controller.Request.ProductFilterRequest;
import vn.web.Controller.Response.PageResponse;
import vn.web.Controller.Response.ProductDetailResponse;
import vn.web.Controller.Response.ProductSummaryResponse;
import vn.web.Converter.ProductMapper;
import vn.web.Model.*;
import vn.web.Repository.BrandRepository;
import vn.web.Repository.CategoryRepository;
import vn.web.Repository.ProductRepository;
import vn.web.Services.ProductService;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private  final BrandRepository brandRepository;
    @Override
    @Transactional
    public ProductDetailResponse createProduct(ProductCreationRequest req) {
        Product product = productMapper.toEntity(req);
        Category realCategory = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category không tồn tại"));

        Brand realBrand = brandRepository.findById(req.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand không tồn tại"));
        product.setCategory(realCategory);
        product.setBrand(realBrand);
        Product productSaved = productRepository.save(product);
        return productMapper.toDTOResponse(productSaved);
    }
}
