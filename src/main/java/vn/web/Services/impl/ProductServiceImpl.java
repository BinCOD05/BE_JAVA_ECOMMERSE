package vn.web.Services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.web.Controller.Request.ProductCreationRequest;
import vn.web.Controller.Request.ProductFilterSearch;
import vn.web.Controller.Response.PageResponse;
import vn.web.Controller.Response.ProductDetailResponse;
import vn.web.Controller.Response.ProductSummaryResponse;
import vn.web.Converter.ProductMapper;
import vn.web.Model.*;
import vn.web.Repository.BrandRepository;
import vn.web.Repository.CategoryRepository;
import vn.web.Repository.ProductRepository;
import vn.web.Services.ProductService;
import vn.web.Util.ProductSpec;

import java.util.List;


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

    @Override
    public ProductDetailResponse getProductDetail(long id) {

        Product product = productRepository.findByIdFullInfo(id).orElseThrow(() -> new RuntimeException("hihi"));

        return productMapper.toDTOResponse(product);
    }

    @Override
    public PageResponse<ProductSummaryResponse> getProductList(ProductFilterSearch req, Pageable pageable) {

        Specification<Product> spec = ProductSpec.search(req);
        Page<Product> products = productRepository.findAll(spec , pageable);

        List<ProductSummaryResponse> list = products.stream().map(product -> {
            ProductSummaryResponse response = new ProductSummaryResponse();
            response.setId(product.getId());
            response.setName(product.getName());
            response.setPrice(product.getPrice());
            response.setBrandName(product.getBrand().getName());
            response.setCategoryName(product.getCategory().getName());
            return  response;
        }).toList();

        return PageResponse.<ProductSummaryResponse>builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElement(products.getTotalElements())
                .totalPage(products.getTotalPages())
                .content(list)
                .build();
    }
}
