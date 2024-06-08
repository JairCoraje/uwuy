package com.acme.center.platform.learning.outboundservices.acl;


import com.acme.center.platform.learning.domain.model.valueobjects.ProfileId;
import com.acme.center.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class ExternalProfileService {
    private final ProfilesContextFacade profilesContextFacade;
    public ExternalProfileService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }

    /**
     * Fetch profileId by email
     * @param email the email to search for
     * @return profileId if found, empty otherwise
     */
    public Optional<ProfileId> fetchProfileIdByEmail(String email){
        var profileId = profilesContextFacade.fetchProfileIdByEmail(email);
        if (profileId == 0L) return Optional.empty();
        return Optional.of(new ProfileId(profileId));
    }
    /**
     * Create profile
     * @param firstName
     * @param lastName
     * @param email
     * @param street
     * @param number
     * @param city
     * @param postalCode
     * @param country
     * @return profileId if created, empty otherwise
     */
    public Optional<ProfileId> createProfile(String firstName, String lastName,
                                             String email, String street,
                                             String number, String city,
                                             String postalCode, String country) {
        var profileId = profilesContextFacade.createProfile(firstName, lastName,
                email, street, number, city, postalCode, country);
        if (profileId == 0L) return Optional.empty();
        return Optional.of(new ProfileId(profileId));
    }
}
