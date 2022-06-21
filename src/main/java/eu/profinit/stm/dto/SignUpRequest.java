package eu.profinit.stm.dto;

import eu.profinit.stm.model.user.SocialProviderEnum;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Chinna
 * @since 26/3/18
 */
@Data
public class SignUpRequest {

    private Long userID;

    private String providerUserId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @NotEmpty
    private String email;

    private SocialProviderEnum socialProvider;

    private String password;

    public SignUpRequest(String providerUserId, String name, String surname, String email, String password, SocialProviderEnum socialProvider) {
        this.providerUserId = providerUserId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.socialProvider = socialProvider;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String providerUserID;
        private String name;
        private String surname;
        private String email;
        private String password;
        private SocialProviderEnum socialProvider;

        public Builder addProviderUserID(final String userID) {
            this.providerUserID = userID;
            return this;
        }

        public Builder addName(final String name) {
            this.name = name;
            return this;
        }

        public Builder addSurname(final String surname) {
            this.surname = surname;
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

        public Builder addSocialProvider(final SocialProviderEnum socialProvider) {
            this.socialProvider = socialProvider;
            return this;
        }

        public SignUpRequest build() {
            return new SignUpRequest(providerUserID, name, surname, email, password, socialProvider);
        }
    }
}
