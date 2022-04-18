package com.koala.linkfilterapp.linkfilterapp.security.dto;

import com.koala.linkfilterapp.linkfilterapp.security.utils.PasswordMatches;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Chinna
 * @since 26/3/18
 */
@Data
@PasswordMatches
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    Long userID;

    String providerUserId;

    @NotEmpty
    String displayName;

    @NotEmpty
    String email;

    SocialProvider socialProvider;

    @Size(min = 6, message = "{Size.userDto.password}")
    String password;

    @NotEmpty
    String matchingPassword;

    public SignUpRequest(String providerUserId, String displayName, String email, String password, SocialProvider socialProvider) {
        this.providerUserId = providerUserId;
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.socialProvider = socialProvider;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        String providerUserID;
        String displayName;
        String email;
        String password;
        SocialProvider socialProvider;

        public Builder addProviderUserID(final String userID) {
            this.providerUserID = userID;
            return this;
        }

        public Builder addDisplayName(final String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder addEmail(final String email) {
            this.email = email;
            return this;
        }

        public Builder addPassword(final String password) {
            this.password = password;
            return this;
        }

        public Builder addSocialProvider(final SocialProvider socialProvider) {
            this.socialProvider = socialProvider;
            return this;
        }

        public SignUpRequest build() {
            return new SignUpRequest(providerUserID, displayName, email, password, socialProvider);
        }
    }
}