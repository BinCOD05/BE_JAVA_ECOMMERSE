package vn.web.Controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.web.Controller.Request.ProductCreationRequest;
import vn.web.Controller.Request.ProductUpdateRequest;
import vn.web.Controller.Response.ApiResponse;
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
    public ApiResponse<ProductSummaryResponse> getProducts(){
        return null;
    }

    @GetMapping(value = "/{id}")
    public ApiResponse<ProductDetailResponse> getProductDetail(@PathVariable Long id ){
        return  null ;
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
