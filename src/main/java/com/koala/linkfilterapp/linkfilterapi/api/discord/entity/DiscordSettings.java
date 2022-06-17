package com.koala.linkfilterapp.linkfilterapi.api.discord.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "discordSettings")
public class DiscordSettings {
    @Id
    @Column(name = "serverId", length = 32)
    String serverId;

    @Column
    Boolean isAutoDeleteEnabled;

    @Column
    Short deleteThresh;

    @Temporal(TemporalType.TIMESTAMP)
    Date createdTime;
}
