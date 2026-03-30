package com.projects.edustore.dto.order;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsResponseDto extends OrderBasicResponseDto {

    private List<OrderItemDto> items = new ArrayList<>();


    public List<OrderItemDto> getItems() {
        return items;
    }

    public void addItemDtos(List<OrderItemDto> items) {
        this.items = items;
    }
}
