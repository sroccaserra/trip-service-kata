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
    private static final User ANY_TRAVELER = new User();
    private static final User TRAVELER_WITH_NO_FRIENDS = new User();
    private User loggedInTraveler;

    @Test
    public void should_fail_if_traveler_is_not_logged_in() {
        // Given
        TripService tripService = new TestableTripService();
        loggedInTraveler = GUEST;
        // Then
        assertThatExceptionOfType(UserNotLoggedInException.class).isThrownBy(() -> {
            // When
            tripService.getTripsByUser(ANY_TRAVELER);
        });
    }

    @Test
    public void should_return_no_trip_if_traveler_has_no_friends() {
        // Given
        TripService tripService = new TestableTripService();
        loggedInTraveler = new User();
        // When
        List<Trip> tripList = tripService.getTripsByUser(TRAVELER_WITH_NO_FRIENDS);
        // Then
        assertThat(tripList).isEmpty();
    }

    @Test
    public void should_return_no_trip_if_traveler_has_friends_but_is_not_my_friend() {
        // Given
        TripService tripService = new TestableTripService();
        loggedInTraveler = new User();

        User selectedTraveler = new User();
        User friendOfSelectedTraveler = new User();
        selectedTraveler.addFriend(friendOfSelectedTraveler);

        // When
        List<Trip> tripList = tripService.getTripsByUser(selectedTraveler);

        // Then
        assertThat(tripList).isEmpty();
    }

    private class TestableTripService extends TripService {
        @Override
        protected User getLoggedUser() {
            return loggedInTraveler;
        }
    }
}
