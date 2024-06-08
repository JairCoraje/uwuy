package com.acme.center.platform.profiles.interfaces.acl;


import com.acme.center.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.acme.center.platform.profiles.domain.model.queries.GetProfileByEmailQuery;
import com.acme.center.platform.profiles.domain.model.valueobjects.EmailAddress;
import com.acme.center.platform.profiles.domain.services.ProfileCommandService;
import com.acme.center.platform.profiles.domain.services.ProfileQueryService;
import org.springframework.stereotype.Service;

/**

 * ProfilesContextFacade

 * <p>

 *     It is a facade to the profiles context.

 *     It is implement as part of an anti-corruption layer (ACL) to be consumed by other contexts.

 * </p>

 */

@Service
public class ProfilesContextFacade {
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;
    public ProfilesContextFacade(ProfileCommandService profileCommandService, ProfileQueryService profileQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    /**
     * Creates a new Profile
     * @param firstName
     * @param lastName
     * @param email
     * @param street
     * @param number
     * @param city
     * @param state
     * @param zipCode
     * @return the profile id
     */
    public Long createProfile(String firstName, String lastName, String email, String street,
                              String number, String city, String state, String zipCode) {
        var createProfileCommand = new CreateProfileCommand(firstName, lastName, email,
                street, number, city, state, zipCode);
        var profile = profileCommandService.handle(createProfileCommand);
        if(profile.isEmpty()) return 0L;
        return profile.get().getId();
    }

    /**+
     * Fetches the profile id by email
     * @param email
     * @return the profile id
     */

    public Long fetchProfileIdByEmail(String email){
        var getProfileByEmailQuery = new GetProfileByEmailQuery(new EmailAddress(email));
        var profile = profileQueryService.handle(getProfileByEmailQuery);
        if(profile.isEmpty()) return 0L;
        return profile.get().getId();
    }

}
