package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

// Working Effectively With Legacy Code, Michael Feathers
// Préciser ce qu'est any user
public class TripServiceTest {
    private static final User GUEST = null;
    private static final User ANY_USER = new User();
    private static final User USER_WITH_NO_FRIENDS = new User();
    private User loggedInUser;

    @Test
    public void should_fail_if_user_is_not_logged_in() {
        // Given
        TripService tripService = new TestableTripService();
        loggedInUser = GUEST;
        // Then
        assertThatExceptionOfType(UserNotLoggedInException.class).isThrownBy(() -> {
            // When
            tripService.getTripsByUser(ANY_USER);
        });
    }

    @Test
    public void should_return_no_trip_if_user_has_no_friends() {
        // Given
        TripService tripService = new TestableTripService();
        loggedInUser = new User();
        // When
        List<Trip> tripList = tripService.getTripsByUser(USER_WITH_NO_FRIENDS);
        // Then
        assertThat(tripList).isEmpty();
    }

    private class TestableTripService extends TripService {
        @Override
        protected User getLoggedUser() {
            return loggedInUser;
        }
    }
}
