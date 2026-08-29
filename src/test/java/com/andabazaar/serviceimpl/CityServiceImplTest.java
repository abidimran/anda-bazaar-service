package com.andabazaar.serviceimpl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.andabazaar.dto.location.CityRequestDto;
import com.andabazaar.dto.location.CityResponseDto;
import com.andabazaar.repository.entity.City;
import com.andabazaar.repository.entity.State;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.CityRepository;
import com.andabazaar.repository.StateRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CityServiceImpl Tests")
class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private StateRepository stateRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    private State state;
    private City city;
    private CityRequestDto requestDto;

    @BeforeEach
    void setUp() {
        state = State.builder().id(1L).name("Karnataka").active(true).build();
        city = City.builder().id(1L).name("Bangalore").state(state).active(true).build();

        requestDto = CityRequestDto.builder()
                .name("Bangalore")
                .stateId(1L)
                .build();
    }

    @Nested
    @DisplayName("createCity")
    class CreateCity {

        @Test
        @DisplayName("should create city successfully")
        void shouldCreateCitySuccessfully() {
            when(stateRepository.findById(1L)).thenReturn(Optional.of(state));
            when(cityRepository.existsByNameIgnoreCaseAndStateId("Bangalore", 1L)).thenReturn(false);
            when(cityRepository.save(any(City.class))).thenReturn(city);

            CityResponseDto result = cityService.createCity(requestDto);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Bangalore");
            assertThat(result.getStateId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when city already exists in state")
        void shouldThrowWhenCityExists() {
            when(stateRepository.findById(1L)).thenReturn(Optional.of(state));
            when(cityRepository.existsByNameIgnoreCaseAndStateId("Bangalore", 1L)).thenReturn(true);

            assertThatThrownBy(() -> cityService.createCity(requestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("City already exists in this state");
        }

        @Test
        @DisplayName("should throw when state not found")
        void shouldThrowWhenStateNotFound() {
            when(stateRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> cityService.createCity(requestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("State not found");
        }
    }

    @Nested
    @DisplayName("getCityById")
    class GetCityById {

        @Test
        @DisplayName("should return city by id")
        void shouldReturnCityById() {
            when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

            CityResponseDto result = cityService.getCityById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when city not found")
        void shouldThrowWhenNotFound() {
            when(cityRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> cityService.getCityById(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getAllCities")
    class GetAllCities {

        @Test
        @DisplayName("should return all cities")
        void shouldReturnAllCities() {
            when(cityRepository.findAll()).thenReturn(List.of(city));

            List<CityResponseDto> result = cityService.getAllCities();

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getCitiesByState")
    class GetCitiesByState {

        @Test
        @DisplayName("should return cities by state")
        void shouldReturnCitiesByState() {
            when(stateRepository.findById(1L)).thenReturn(Optional.of(state));
            when(cityRepository.findByStateIdAndActiveTrueOrderByNameAsc(1L)).thenReturn(List.of(city));

            List<CityResponseDto> result = cityService.getCitiesByState(1L);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("should throw when state not found")
        void shouldThrowWhenStateNotFound() {
            when(stateRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> cityService.getCitiesByState(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("updateCity")
    class UpdateCity {

        @Test
        @DisplayName("should update city successfully when name unchanged")
        void shouldUpdateCityWhenNameUnchanged() {
            when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
            when(stateRepository.findById(1L)).thenReturn(Optional.of(state));
            when(cityRepository.save(any(City.class))).thenReturn(city);

            CityResponseDto result = cityService.updateCity(1L, requestDto);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should update city with changed name")
        void shouldUpdateCityWithChangedName() {
            requestDto.setName("Mysore");
            when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
            when(stateRepository.findById(1L)).thenReturn(Optional.of(state));
            when(cityRepository.existsByNameIgnoreCaseAndStateId("Mysore", 1L)).thenReturn(false);
            when(cityRepository.save(any(City.class))).thenReturn(city);

            CityResponseDto result = cityService.updateCity(1L, requestDto);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when duplicate on update with changed name")
        void shouldThrowWhenDuplicate() {
            requestDto.setName("Mysore");
            when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
            when(stateRepository.findById(1L)).thenReturn(Optional.of(state));
            when(cityRepository.existsByNameIgnoreCaseAndStateId("Mysore", 1L)).thenReturn(true);

            assertThatThrownBy(() -> cityService.updateCity(1L, requestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("City already exists in this state");
        }
    }

    @Nested
    @DisplayName("deleteCity")
    class DeleteCity {

        @Test
        @DisplayName("should soft delete city")
        void shouldSoftDeleteCity() {
            when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
            when(cityRepository.save(any(City.class))).thenReturn(city);

            cityService.deleteCity(1L);

            assertThat(city.getActive()).isFalse();
            verify(cityRepository).save(city);
        }
    }
}
