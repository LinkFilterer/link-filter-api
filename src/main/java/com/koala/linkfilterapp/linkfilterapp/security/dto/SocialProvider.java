package com.koala.linkfilterapp.linkfilterapp.security.dto;

public enum SocialProvider {
    FACEBOOK("facebook"), TWITTER("twitter"), LINKEDIN("linkedin"), GOOGLE("google"), GITHUB("github"), LOCAL("local");

    private final String providerType;

    SocialProvider(final String providerType) {
        this.providerType = providerType;
    }

    public String getProviderType() {
        return providerType;
    }
}
