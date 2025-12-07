package vn.web.Controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.web.Controller.Request.ProductCreationRequest;
import vn.web.Controller.Request.ProductFilterSearch;
import vn.web.Controller.Request.ProductUpdateRequest;
import vn.web.Controller.Response.ApiResponse;
import vn.web.Controller.Response.PageResponse;
import vn.web.Controller.Response.ProductDetailResponse;
import vn.web.Controller.Response.ProductSummaryResponse;
import vn.web.Services.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/products")
public class ProductController {

    private final ProductService productService;


    @GetMapping
    @Operation(summary = "get list products" ,tags = "get products" , description = "Lấy danh sách sản phẩm có lọc")
    public ApiResponse<PageResponse<ProductSummaryResponse>> getProducts(@ModelAttribute ProductFilterSearch request , @PageableDefault(size = 10 , direction = Sort.Direction.ASC ) Pageable pageable){
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

    @PostMapping
    public ApiResponse<ProductDetailResponse> createProduct(@RequestBody ProductCreationRequest request){
        return ApiResponse.<ProductDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .result(productService.createProduct(request))
                .build();
    }

    @PutMapping(value = "/{id}")
    public ApiResponse<ProductDetailResponse> updateProduct(@RequestBody ProductUpdateRequest request ,
                                                      @PathVariable Long id ){
        return null ;
    }

    @DeleteMapping(value = "/{id}")
    public ApiResponse<Object> deleteProduct(){
        return null ;
    }


//    @PatchMapping(value = "/{id}")
//    public ApiResponse<ProductResponse> updatePrice(@RequestBody ProductUpdateR)

}
