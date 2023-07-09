package com.benitez.best_travel.infraestructure.Service;

import com.benitez.best_travel.api.models.responses.HotelResponse;
import com.benitez.best_travel.domain.entities.Hotel;
import com.benitez.best_travel.domain.repository.HotelRepository;
import com.benitez.best_travel.infraestructure.abstract_services.IHotelService;
import com.benitez.best_travel.util.exceptions.enums.SortType;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@AllArgsConstructor
@Service
public class HotelService implements IHotelService {
    private final HotelRepository hotelRepository;

    @Override
    public Page<HotelResponse> readAll(Integer page, Integer size, SortType sortType) {
        PageRequest pageRequest = null;
        switch (sortType) {
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }
        return hotelRepository.findAll(pageRequest).map(this::EntityToResponse);

    }

    @Override
    public Set<HotelResponse> readLessPrice(BigDecimal price) {
        return hotelRepository.findByPriceLessThan(price)
                .stream().map(this::EntityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<HotelResponse> readBetweenPrices(BigDecimal min, BigDecimal max) {
        return hotelRepository.findByPriceBetween(min, max)
                .stream().map(this::EntityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<HotelResponse> readGreaterThan(Integer rating) {
        return hotelRepository.findByRatingGreaterThan(rating)
                .stream().map(this::EntityToResponse)
                .collect(Collectors.toSet());
    }

    private HotelResponse EntityToResponse(Hotel entity) {
        HotelResponse response = new HotelResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
