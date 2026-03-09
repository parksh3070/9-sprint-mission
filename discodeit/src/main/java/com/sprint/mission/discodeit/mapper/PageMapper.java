package com.sprint.mission.discodeit.mapper;

import java.util.List;
import java.util.function.Function;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {

    public <T, R> PageResponse<R> toResponse(Slice<T> slice, Function<T, R> mapper) {
        List<R> content = slice.getContent().stream()
                .map(mapper)
                .toList();

        return new PageResponse<>(
                content,
                slice.getNumber(),
                slice.getSize(),
                null
        );
    }
}