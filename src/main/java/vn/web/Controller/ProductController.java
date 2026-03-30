package vn.web.Controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.web.Controller.Request.ProductCreationRequest;
import vn.web.Controller.Request.ProductFilterSearch;
import vn.web.Controller.Request.ProductUpdateRequest;
import vn.web.Controller.Response.ApiResponse;
import vn.web.Controller.Response.PageResponse;
import vn.web.Controller.Response.ProductDetailResponse;
import vn.web.Controller.Response.ProductSummaryResponse;
import vn.web.Services.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/products")
public class ProductController {

    private final ProductService productService;


    @GetMapping
    @Operation(summary = "get list products" ,tags = "get products" , description = "Lấy danh sách sản phẩm có lọc")
    public ApiResponse<PageResponse<ProductSummaryResponse>> getProducts(@ModelAttribute ProductFilterSearch request , @PageableDefault(size = 30 , direction = Sort.Direction.ASC ) Pageable pageable){
        return ApiResponse.<PageResponse<ProductSummaryResponse>>builder()
                .result(productService.getProductList(request , pageable))
                .build();
    }

    @GetMapping(value = "/{id}")
    public ApiResponse<ProductDetailResponse> getProductDetail(@PathVariable Long id ){
        return  ApiResponse.<ProductDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("get product detail successful")
                .result(productService.getProductDetail(id))
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductDetailResponse> createProduct(@RequestPart("product") ProductCreationRequest request ,
                                                            @RequestPart(value = "files" , required = false)List<MultipartFile> files){
        if (files != null && request.getImages() != null && files.size() != request.getImages().size()) {
            throw new RuntimeException("Số lượng file ảnh và thông tin ảnh không khớp nhau!");
        }
        return ApiResponse.<ProductDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .result(productService.createProduct(request , files))
                .build();
    }

    @PutMapping(value = "/{id}")
    public ApiResponse<ProductDetailResponse> updateProduct(@RequestBody ProductUpdateRequest request ,
                                                      @PathVariable Long id ){

        return ApiResponse.<ProductDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .result(productService.updateProduct(request , id))
                .build();
    }

    @DeleteMapping(value = "/{id}")
    public ApiResponse<Object> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return null ;
    }


//    @PatchMapping(value = "/{id}")
//    public ApiResponse<ProductResponse> updatePrice(@RequestBody ProductUpdateR)

}
