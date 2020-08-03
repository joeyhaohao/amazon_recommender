package edu.rice.cs.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    private String keyword;
    private Integer page;

    private static final Integer DEFAULT_PAGE = 1;

    public Integer getPage() {
        if (page == null){
            return DEFAULT_PAGE;
        }
        return Math.max(DEFAULT_PAGE, page);
    }

}
