package com.koala.linkfilterapp.linkfilterapi.api.common.dto.request;

import com.koala.linkfilterapp.linkfilterapi.api.common.enums.BanStatus;
import lombok.Data;

@Data
public class BanAction {
    String ipAddress;
    BanStatus isBanned;
}
