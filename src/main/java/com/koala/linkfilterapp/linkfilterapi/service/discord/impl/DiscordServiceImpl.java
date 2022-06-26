package com.koala.linkfilterapp.linkfilterapi.service.discord.impl;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.discord.dto.DiscordSettingsRequest;
import com.koala.linkfilterapp.linkfilterapi.api.discord.entity.DiscordSettings;
import com.koala.linkfilterapp.linkfilterapi.repository.DiscordSettingsRepository;
import com.koala.linkfilterapp.linkfilterapi.service.discord.DiscordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class DiscordServiceImpl implements DiscordService {

    @Autowired
    DiscordSettingsRepository repository;

    @Cacheable("discordSettings")
    @Override
    public DiscordSettings getSettings(String serverId) throws CommonException {
        Optional<DiscordSettings> entity = repository.findById(serverId);
        if (!entity.isPresent()) {
            throw new CommonException(HttpStatus.NOT_FOUND, "Discord settings not found for this server");
        }
        return entity.get();
    }

    @CacheEvict(value = "discordSettings", allEntries = true)
    @Override
    public DiscordSettings updateSettings(DiscordSettingsRequest settings) throws CommonException {
        Optional<DiscordSettings> entity = repository.findById(settings.getServerId());
        DiscordSettings settingsEntity;
        if (!entity.isPresent()) {
            DiscordSettings newSettings = new DiscordSettings();
            newSettings.setServerId(settings.getServerId());
            newSettings.setDeleteThresh(nonNull(settings.getDeleteThreshold()) ? settings.getDeleteThreshold() : -8);
            newSettings.setIsAutoDeleteEnabled(nonNull(settings.getIsAutoDeleteEnabled()) ? settings.getIsAutoDeleteEnabled() : false);
            newSettings.setCreatedTime(new Date());
            settingsEntity = repository.save(newSettings);

        } else {
            DiscordSettings currentSettings = entity.get();
            if (nonNull(settings.getIsAutoDeleteEnabled())) {
                currentSettings.setIsAutoDeleteEnabled(settings.getIsAutoDeleteEnabled());
            }
            Short deleteThreshold = settings.getDeleteThreshold();
            if (nonNull(deleteThreshold)) {
                currentSettings.setDeleteThresh(deleteThreshold);
            }
            settingsEntity = repository.save(currentSettings);
        }
        return settingsEntity;
    }
}
