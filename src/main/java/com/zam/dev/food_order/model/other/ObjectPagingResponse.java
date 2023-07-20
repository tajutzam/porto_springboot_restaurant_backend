package com.zam.dev.food_order.model.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectPagingResponse <T> {

    private T data;

    private String message;
    private int status;

    private ObjectPaging objectPaging;


}
