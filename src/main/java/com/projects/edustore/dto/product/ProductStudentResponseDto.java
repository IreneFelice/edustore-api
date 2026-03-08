package com.projects.edustore.dto.product;

import java.math.BigDecimal;

public class ProductStudentResponseDto {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;

        //students only:
        private Long makerId;
        private Integer stockQuanity;
        private BigDecimal costPrice;



        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }


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
