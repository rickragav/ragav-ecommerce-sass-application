package com.ragav.ecommerce.product_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.stereotype.Component;

import com.ragav.ecommerce.product_service.persistence.ProductEntity;

/**
 * Component responsible for creating MongoDB indexes after application startup.
 * This ensures that @Indexed annotations on entities are properly processed.
 */
@Component
public class MongoIndexInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(MongoIndexInitializer.class);

    @Autowired
    private MongoOperations mongoTemplate;

    @EventListener(ContextRefreshedEvent.class)
    public void initIndicesAfterStartup() {
        LOG.info("Initializing MongoDB indexes...");

        try {
            MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext = mongoTemplate
                    .getConverter().getMappingContext();

            IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);
            IndexOperations indexOps = mongoTemplate.indexOps(ProductEntity.class);

            resolver.resolveIndexFor(ProductEntity.class).forEach(indexDefinition -> {
                LOG.info("Creating index: {}", indexDefinition);
                indexOps.ensureIndex(indexDefinition);
            });

            LOG.info("MongoDB indexes initialized successfully!");

        } catch (Exception e) {
            LOG.error("Failed to initialize MongoDB indexes", e);
        }
    }
}