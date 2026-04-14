package com.projects.edustore.dto.product;

import java.math.BigDecimal;

public class ProductStudentResponseDto extends ProductCustomerResponseDto {

        private Long makerId;
        private Integer stockQuanity;
        private BigDecimal costPrice;

    public Integer getStockQuanity() {
        return stockQuanity;
    }

    public void setStockQuanity(Integer stockQuanity) {
        this.stockQuanity = stockQuanity;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Long getMakerId() {
        return makerId;
    }

    public void setMakerId(Long makerId) {
        this.makerId = makerId;
    }
}
