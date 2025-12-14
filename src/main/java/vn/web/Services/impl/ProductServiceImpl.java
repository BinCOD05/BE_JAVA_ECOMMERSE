package vn.web.Services.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.web.Controller.Request.ProductCreationRequest;
import vn.web.Controller.Request.ProductFilterSearch;
import vn.web.Controller.Request.ProductUpdateRequest;
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

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private  final BrandRepository brandRepository;
    private final CloudinaryService cloudinaryService;


    @Override
    @Transactional
    public ProductDetailResponse createProduct(ProductCreationRequest req , List<MultipartFile> files) {
        Product product = productMapper.toEntity(req);

        Category realCategory = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category không tồn tại"));

        Brand realBrand = brandRepository.findById(req.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand không tồn tại"));


        if(files != null && !files.isEmpty() && req.getImages() != null ){
            List<String> images = files.stream().map( multipartFile ->
                cloudinaryService.upload(multipartFile) ).toList();

            Set<ProductImage> productImageSet = new HashSet<>();

            for (int i = 0 ; i < images.size() ; i ++){
                ProductImage image = new ProductImage() ;
                image.setImageUrl(images.get(i));
                productImageSet.add(image);
            }
            product.setProductImages(productImageSet);
        }

        Inventory inventory = new Inventory() ;
        product.setCategory(realCategory);
        product.setBrand(realBrand);
        inventory.setQuantity(req.getStock());
        inventory.setProduct(product);
        product.setInventory(inventory);
        Product productSaved = productRepository.save(product);
        return productMapper.toDTOResponse(productSaved);
    }

    @Override
    public ProductDetailResponse getProductDetail(long id) {

        Product product = productRepository.findByIdFullInfo(id).orElseThrow(() -> new RuntimeException("hihi"));

        return productMapper.toDTOResponse(product);
    }

    @Override
    @Transactional
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
            response.setColor(product.getColor());
            if(product.getInventory() != null ){ response.setStock(product.getInventory().getQuantity());};
            response.setThumbnailUrl(product.getProductImages().stream()
                    .filter(image -> Boolean.TRUE.equals(image.getPrimary()))
                    .findFirst()
                    .map(ProductImage::getImageUrl)
                    .orElse(null)
                    );
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

    @Override
    @Transactional
    public ProductDetailResponse updateProduct(ProductUpdateRequest req, long id) {
        Product product = productRepository.findByIdFullInfo(id).orElseThrow( () -> new RuntimeException("product not found"));
        productMapper.updateProduct(product , req);
        return  productMapper.toDTOResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(long id) {
        Product product = productRepository.findByIdFullInfo(id).orElseThrow( () -> new RuntimeException("product not found"));
        productRepository.delete(product);
    }
}
