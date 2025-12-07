package vn.web.Services;

import org.springframework.data.domain.Pageable;
import vn.web.Controller.Request.ProductCreationRequest;
import vn.web.Controller.Request.ProductFilterSearch;
import vn.web.Controller.Response.PageResponse;
import vn.web.Controller.Response.ProductDetailResponse;
import vn.web.Controller.Response.ProductSummaryResponse;

public interface ProductService {
   ProductDetailResponse createProduct(ProductCreationRequest req);

   ProductDetailResponse getProductDetail(long id) ;

   PageResponse<ProductSummaryResponse> getProductList(ProductFilterSearch req , Pageable pageable);
}
