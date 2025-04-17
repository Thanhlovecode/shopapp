package shop.example.dto.response.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PageResponse <T>{
    private int pageNo;
    private int pageSize;
    private int totalPage;
    private long totalElement;
    private T items;
}
