package com.koala.linkfilterapp.linkfilterapp.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchBean {
    String userId;
    String email;
    String sortType;
    String sortDir;
    Integer pageSize;
    Integer pageNo;
}
