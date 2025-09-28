package com.ragav.ecommerce.product_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ragav.ecommerce.api.core.product.Product;
import com.ragav.ecommerce.api.core.product.ProductService;
import com.ragav.ecommerce.api.exceptions.BadRequestException;
import com.ragav.ecommerce.api.exceptions.InvalidInputException;
import com.ragav.ecommerce.api.exceptions.NotFoundException;
import com.ragav.ecommerce.product_service.persistence.ProductRepository;
import com.ragav.ecommerce.utils.http.ServiceUtil;

@RestController
public class ProductServiceImpl implements ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final ServiceUtil serviceUtil;

    @Autowired
    ProductServiceImpl(ProductMapper mapper, ProductRepository repository, ServiceUtil serviceUtil) {
        this.mapper = mapper;
        this.repository = repository;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product createProduct(Product body) {
        try {

            LOG.debug("createProduct: {}", body);

            if (body.getProductId() < 1) {
                throw new InvalidInputException("Invalid productId: " + body.getProductId());

            }

            if (body.getName() == null || body.getName().trim().isEmpty()) {
                throw new InvalidInputException("Invalid product name: " + body.getName());

            }

            var entity = mapper.apiToEntity(body);
            var newEntity = repository.save(entity);

            LOG.debug("createProduct: created a product entity: {}", newEntity);

            return mapper.entityToApi(newEntity);
        } catch (org.springframework.dao.DuplicateKeyException dke) {
            throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId());
        } catch (Exception e) {
            LOG.error("createProduct: failed to create a product", e);
            throw new BadRequestException("Invalid request: " + e.getMessage());
        }
    }

    @Override
    public Product getProduct(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        var entity = repository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));

        Product response = mapper.entityToApi(entity);

        response.setServiceAddress(serviceUtil.getServiceAddress());

        LOG.debug("getProduct: found productId={}", response.getProductId());

        return response;
    }

    @Override
    public void deleteProduct(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        LOG.debug("deleteProduct: tries to delete an entity with productId: {}", productId);

        repository.findByProductId(productId)
                .ifPresentOrElse(entity -> {
                    repository.delete(entity);
                    LOG.debug("deleteProduct: deleted entity with productId: {}", productId);
                }, () -> {
                    LOG.warn("deleteProduct: no entity found with productId: {}", productId);
                });
    }
}
