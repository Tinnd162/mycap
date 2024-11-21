package com.mycompany.handlers;

import java.math.BigDecimal;
import java.util.Map;
import org.slf4j.Logger;

import org.springframework.stereotype.Component;
import org.slf4j.LoggerFactory;

import com.sap.cds.services.ServiceException;
import com.sap.cds.services.cds.CdsCreateEventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.ServiceName;
import cds.gen.catalogservice.CatalogService_; // This import will work after step 2
import cds.gen.com.bookshop.Books_;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CatalogServiceHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(CatalogServiceHandler.class);

    // Validate price and set status before CREATE
    @Before(event = "CREATE", entity = Books_.CDS_NAME)
    public void validateBeforeCreate(CdsCreateEventContext context) {
        logger.info("Validating new book data...");

        Map<String, Object> book = context.getCqn().entries().get(0);
        validateBookData(book);
        setBookStatus(book);
    }

    @After(event = "CREATE")
    public void afterCreate(CdsCreateEventContext context) {
        Map<String, Object> book = context.getCqn().entries().get(0);
        logger.info("Book created successfully - Title: {}", book.get("title"));
    }

    private void validateBookData(Map<String, Object> book) {
        try {
            if (book.containsKey("price")) {
                BigDecimal price = new BigDecimal(book.get("price").toString());
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    throw new ServiceException("Price cannot be negative");
                }
            }

            if (book.containsKey("stock")) {
                Integer stock = (Integer) book.get("stock");

                if (stock < 0) {
                    throw new ServiceException("Stock cannot be negative");
                }
            }
        } catch (Exception e) {
            throw new ServiceException("Error when validate book data!");
        }
    }

    private void setBookStatus(Map<String, Object> book) {
        if (book.containsKey("stock")) {
            Integer stock = (Integer) book.get("stock");
            if (stock == 0) {
                book.put("status", "OUT_OF_STOCK");
            } else if (stock > 0) {
                book.put("status", "AVAILABLE");
            }
        }
    }
}

// Task 1: Create a handler that:
// Validates price cannot be negative
// Automatically sets status based on stock
// Logs all operations