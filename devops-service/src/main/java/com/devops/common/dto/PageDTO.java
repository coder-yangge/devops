package com.devops.common.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * @author yangge
 * @version 1.0.0
 * @title: PageDTO
 * @date 2020/7/5 12:29
 */
@Data
public class PageDTO<T> {

    private Integer pageNum;

    private Integer pageSize;

    private Long total;

    private Integer totalPage;

    private List<T> data;

    public static<T, R> PageDTO<T> of(Page<R> page, Supplier<T> supplier, BiConsumer<R, T> function) {
        List<T> data = page.get().map(r -> {
            T t = supplier.get();
            function.accept(r, t);
            return t;
        }).collect(Collectors.toList());
        return of(page, data);
    }

    public static<T, R> PageDTO<T> of(Page<R> page, List<T> data) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(page.getNumber());
        pageDTO.setPageSize(page.getSize());
        pageDTO.setTotal(page.getTotalElements());
        pageDTO.setTotalPage(page.getTotalPages());
        pageDTO.setData(data);
        return pageDTO;
    }
}
