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
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.mapper.CityMapper;
import com.andabazaar.repository.CityRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CityServiceImpl Tests")
class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityServiceImpl cityService;

    private City city;
    private CityRequestDto requestDto;

    @BeforeEach
    void setUp() {
        city = City.builder().id(1L).name("Bangalore").build();

        requestDto = CityRequestDto.builder()
                .name("Bangalore")
                .build();

        lenient().when(cityMapper.toResponseDto(any(City.class))).thenAnswer(inv -> {
            City c = inv.getArgument(0);
            return CityResponseDto.builder().id(c.getId()).name(c.getName()).build();
        });
    }

    @Nested
    @DisplayName("createCity")
    class CreateCity {

        @Test
        @DisplayName("should create city successfully")
        void shouldCreateCitySuccessfully() {
            when(cityRepository.existsByNameIgnoreCase("Bangalore")).thenReturn(false);
            when(cityRepository.save(any(City.class))).thenReturn(city);

            CityResponseDto result = cityService.createCity(requestDto);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Bangalore");
        }

        @Test
        @DisplayName("should throw when city already exists")
        void shouldThrowWhenCityExists() {
            when(cityRepository.existsByNameIgnoreCase("Bangalore")).thenReturn(true);

            assertThatThrownBy(() -> cityService.createCity(requestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("City already exists");
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
            when(cityRepository.findAllByOrderByNameAsc()).thenReturn(List.of(city));

            List<CityResponseDto> result = cityService.getAllCities();

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("updateCity")
    class UpdateCity {

        @Test
        @DisplayName("should update city successfully when name unchanged")
        void shouldUpdateCityWhenNameUnchanged() {
            when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
            when(cityRepository.save(any(City.class))).thenReturn(city);

            CityResponseDto result = cityService.updateCity(1L, requestDto);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should update city with changed name")
        void shouldUpdateCityWithChangedName() {
            requestDto.setName("Mysore");
            when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
            when(cityRepository.existsByNameIgnoreCase("Mysore")).thenReturn(false);
            when(cityRepository.save(any(City.class))).thenReturn(city);

            CityResponseDto result = cityService.updateCity(1L, requestDto);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw when duplicate on update with changed name")
        void shouldThrowWhenDuplicate() {
            requestDto.setName("Mysore");
            when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
            when(cityRepository.existsByNameIgnoreCase("Mysore")).thenReturn(true);

            assertThatThrownBy(() -> cityService.updateCity(1L, requestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("City already exists");
        }
    }

    @Nested
    @DisplayName("deleteCity")
    class DeleteCity {

        @Test
        @DisplayName("should delete city")
        void shouldDeleteCity() {
            when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

            cityService.deleteCity(1L);

            verify(cityRepository).delete(city);
        }
    }
}
