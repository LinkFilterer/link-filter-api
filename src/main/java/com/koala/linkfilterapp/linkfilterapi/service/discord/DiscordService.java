package com.koala.linkfilterapp.linkfilterapi.service.discord;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.discord.dto.DiscordSettingsRequest;
import com.koala.linkfilterapp.linkfilterapi.api.discord.entity.DiscordSettings;

public interface DiscordService {
    DiscordSettings getSettings(String serverId) throws CommonException;
    DiscordSettings updateSettings(DiscordSettingsRequest settings) throws CommonException;
}
