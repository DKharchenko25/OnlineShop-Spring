package com.dkharchenko_hillel.homework7.facades;

import com.dkharchenko_hillel.homework7.converters.PriceConverter;
import com.dkharchenko_hillel.homework7.converters.ProductConverter;
import com.dkharchenko_hillel.homework7.dtos.ProductDto;
import com.dkharchenko_hillel.homework7.services.ProductService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.dkharchenko_hillel.homework7.validators.InputValidator.checkName;

@Slf4j
@Component
public class ProductFacadeImpl implements ProductFacade {

    private final ProductService productService;

    private final PriceConverter priceConverter;


    public ProductFacadeImpl(ProductService productService, PriceConverter priceConverter) {
        this.productService = productService;
        this.priceConverter = priceConverter;
    }

    @Override
    public void addProduct(@NonNull ProductDto dto) {
        productService.addProduct(checkName(dto.getName()), dto.getPriceInUah(), dto.getShopId());
    }

    @Override
    public void removeProduct(@NonNull ProductDto dto) {
        productService.removeProductById(dto.getId());
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<ProductDto> allProducts = productService.getAllProducts().stream()
                .map(ProductConverter::convertProductToProductDto)
                .collect(Collectors.toList());
        allProducts.forEach(productDto -> productDto.setPriceInUsd(
                priceConverter.getConvertedPrice(productDto.getPriceInUah())));
        return allProducts;
    }

    @Override
    public void updateProductName(@NonNull ProductDto dto) {
        productService.updateProductNameById(dto.getId(), checkName(dto.getName()));
    }

    @Override
    public void updateProductPrice(@NonNull ProductDto dto) {
        productService.updateProductPriceById(dto.getId(), dto.getPriceInUah());
    }
}
