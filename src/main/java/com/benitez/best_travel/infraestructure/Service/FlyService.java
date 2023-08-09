package com.benitez.best_travel.infraestructure.Service;

import com.benitez.best_travel.api.models.responses.FlyResponse;
import com.benitez.best_travel.domain.entities.Fly;
import com.benitez.best_travel.domain.repository.FlyRepository;
import com.benitez.best_travel.infraestructure.abstract_services.IFlyService;
import com.benitez.best_travel.util.constants.CacheConstants;
import com.benitez.best_travel.util.exceptions.enums.SortType;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@AllArgsConstructor
@Service
public class FlyService implements IFlyService {
    private final FlyRepository flyRepository;
    private final WebClient webClient;

    @Override
    public Page<FlyResponse> readAll(Integer page, Integer size, SortType sortType) {
        PageRequest pageRequest = null;
        switch (sortType) {
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }
        return flyRepository.findAll(pageRequest).map(this::EntityToResponse);
    }

    @Override
    @Cacheable(value = CacheConstants.FLY_CACHE_NAME)
    public Set<FlyResponse> readLessPrice(BigDecimal price) {
        return flyRepository.selectLessPrice(price).stream()
                .map(this::EntityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    @Cacheable(value = CacheConstants.FLY_CACHE_NAME)
    public Set<FlyResponse> readBetweenPrices(BigDecimal min, BigDecimal max) {
        return flyRepository.selectBetweenPrice(min, max)
                .stream().map(this::EntityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    @Cacheable(value = CacheConstants.FLY_CACHE_NAME)
    public Set<FlyResponse> readByOriginDestiny(String origen, String destiny) {
        return flyRepository.selectOriginDestiny(origen, destiny)
                .stream().map(this::EntityToResponse)
                .collect(Collectors.toSet());
    }

    private FlyResponse EntityToResponse(Fly entity) {
        FlyResponse response = new FlyResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
