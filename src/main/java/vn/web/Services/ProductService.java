package vn.web.Services;

import org.springframework.data.domain.Page;
import vn.web.Controller.Request.ProductCreationRequest;
import vn.web.Controller.Request.ProductFilterRequest;
import vn.web.Controller.Response.PageResponse;
import vn.web.Controller.Response.ProductDetailResponse;

import java.util.List;

public interface ProductService {
   ProductDetailResponse createProduct(ProductCreationRequest req);
}
