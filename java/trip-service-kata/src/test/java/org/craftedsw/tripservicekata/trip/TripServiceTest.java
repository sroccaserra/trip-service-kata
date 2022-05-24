package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TripServiceTest {
    private static final User GUEST = null;
    private static final User ANY_TRAVELER = new User();
    private static final User TRAVELER_WITH_NO_FRIENDS = new User();
    private final Trip TRIP_OF_MY_FRIEND = new Trip();
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

    // Est-ce qu'une liste vide était bien pour le test ci-dessous ?
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
    public void should_return_a_list_of_trips_if_my_friend_has_one_trip() {
        // Given
        TripService tripService = new TestableTripService();
        loggedInTraveler = new User();

        User myFriend = new User();
        myFriend.addTrip(TRIP_OF_MY_FRIEND);
        myFriend.addFriend(loggedInTraveler);

        // When
        List<Trip> tripList = tripService.getTripsByUser(myFriend);

        // Then
        assertThat(tripList).containsExactly(TRIP_OF_MY_FRIEND);
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

        @Override
        protected List<Trip> findTripsByUser(User user) {
            return List.of(TRIP_OF_MY_FRIEND);
        }
    }
}
