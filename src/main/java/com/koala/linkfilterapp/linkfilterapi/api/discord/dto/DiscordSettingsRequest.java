package com.koala.linkfilterapp.linkfilterapi.api.discord.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class DiscordSettingsRequest {
    @Column(length = 32)
    String serverId;
    Boolean isAutoDeleteEnabled;
    Short deleteThreshold;
}
