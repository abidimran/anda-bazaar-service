package com.andabazaar.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.country.CitiesApiResponse;
import com.andabazaar.dto.country.CountryRequest;
import com.andabazaar.dto.country.StateCityRequest;
import com.andabazaar.dto.country.StatesApiResponse;
import com.andabazaar.entity.City;
import com.andabazaar.entity.State;
import com.andabazaar.feign.CountryApiClient;
import com.andabazaar.repository.CityRepository;
import com.andabazaar.repository.StateRepository;
import com.andabazaar.service.LocationSyncService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationSyncServiceImpl implements LocationSyncService {

    private static final String COUNTRY = "India";

    private final CountryApiClient countryApiClient;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    @Override
    @Transactional
    public void syncStatesAndCities() {

        log.info("Starting states and cities sync for {}", COUNTRY);

        // 1. Fetch all states
        StatesApiResponse statesResponse = countryApiClient.getStates(
                CountryRequest.builder().country(COUNTRY).build());

        if (statesResponse.getError() != null && statesResponse.getError()) {
            log.error("States API returned error: {}", statesResponse.getMsg());
            return;
        }

        List<StatesApiResponse.StateInfo> stateInfos = statesResponse.getData().getStates();
        if (stateInfos == null || stateInfos.isEmpty()) {
            log.warn("No states returned from API");
            return;
        }

        int statesSaved = 0;
        int statesSkipped = 0;
        int totalCitiesSaved = 0;
        int totalCitiesSkipped = 0;

        for (StatesApiResponse.StateInfo stateInfo : stateInfos) {

            String stateName = stateInfo.getName().trim();

            // Save or find state
            State state = stateRepository.findByNameIgnoreCase(stateName).orElse(null);

            if (state == null) {
                state = State.builder()
                        .name(stateName)
                        .active(true)
                        .build();
                state = stateRepository.save(state);
                statesSaved++;
                log.info("Saved state: {}", stateName);
            } else {
                statesSkipped++;
            }

            // 2. Fetch cities for this state
            try {
                CitiesApiResponse citiesResponse = countryApiClient.getCities(
                        StateCityRequest.builder().country(COUNTRY).state(stateName).build());

                if (citiesResponse.getError() != null && citiesResponse.getError()) {
                    log.warn("Cities API error for state={}: {}", stateName, citiesResponse.getMsg());
                    continue;
                }

                List<String> cityNames = citiesResponse.getData();
                if (cityNames == null || cityNames.isEmpty()) {
                    log.warn("No cities returned for state={}", stateName);
                    continue;
                }

                for (String cityName : cityNames) {
                    String trimmedCity = cityName.trim();

                    if (trimmedCity.isEmpty()) continue;

                    if (cityRepository.existsByNameIgnoreCaseAndStateId(trimmedCity, state.getId())) {
                        totalCitiesSkipped++;
                        continue;
                    }

                    City city = City.builder()
                            .name(trimmedCity)
                            .state(state)
                            .active(true)
                            .build();
                    cityRepository.save(city);
                    totalCitiesSaved++;
                }

                log.info("Synced cities for state={}: count={}", stateName, cityNames.size());

            } catch (Exception e) {
                log.error("Failed to fetch cities for state={}: {}", stateName, e.getMessage());
            }
        }

        log.info("Sync completed — states saved={} skipped={}, cities saved={} skipped={}",
                statesSaved, statesSkipped, totalCitiesSaved, totalCitiesSkipped);
    }
}
