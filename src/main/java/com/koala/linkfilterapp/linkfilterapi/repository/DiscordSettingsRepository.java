package com.koala.linkfilterapp.linkfilterapi.repository;

import com.koala.linkfilterapp.linkfilterapi.api.discord.entity.DiscordSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscordSettingsRepository extends JpaRepository<DiscordSettings, String>, JpaSpecificationExecutor<DiscordSettings> {

}
