package com.example.mobileapi.service.impl;

import com.example.mobileapi.dto.DiscountDTO;
import com.example.mobileapi.entity.Discount;
import com.example.mobileapi.exception.AppException;
import com.example.mobileapi.exception.ErrorCode;
import com.example.mobileapi.mapper.DiscountMapper;
import com.example.mobileapi.repository.DiscountRepository;
import com.example.mobileapi.service.DiscountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiscountServiceImpl implements DiscountService {

    DiscountRepository discountRepository;
    DiscountMapper discountMapper;

    @Override
    public void checkValidDiscount(String code) {
        checkValidDiscount(getDiscountEntity(code));
    }

    @Override
    public void checkValidDiscount(Discount disc) {
        LocalDate nowDate = LocalDate.now();

        if (disc.getEndDate().isBefore(nowDate)) {
            throw new AppException(ErrorCode.EXPIRED_DISCOUNT);
        }

        if (disc.getStartDate().isAfter(nowDate)) {
            throw new AppException(ErrorCode.DISCOUNT_NOT_STARTED);
        }

        if (Boolean.FALSE.equals(disc.getActive())) {
            throw new AppException(ErrorCode.DISCOUNT_INACTIVE);
        }

    }

    @Override
    public Integer getDiscountPercent(String code) {
        Discount discount = discountRepository.findByCode(code).orElse(null);
        if (discount != null) {
            checkValidDiscount(discount);

        }


        return discount != null ? discount.getPercent() : 0;
    }

    @Override
    public DiscountDTO getDiscount(String code) {
        return discountMapper.toDTO(getDiscountEntity(code));
    }

    @Override
    public List<DiscountDTO> getAllDiscount() {
        return discountMapper.toListDTO(discountRepository.getAllByActive(true));
    }

    @Override
    @Transactional
    public DiscountDTO create(DiscountDTO discount) {
        Discount entity = discountMapper.toDiscount(discount);
        return discountMapper.toDTO(discountRepository.save(entity));
    }

    @Override
    @Transactional
    public DiscountDTO update(DiscountDTO dto) {
        Discount disc = getDiscountEntity(dto.getCode());
        disc.setPercent(dto.getPercent());
        disc.setStartDate(dto.getStartDate());
        disc.setEndDate(dto.getEndDate());
        disc.setActive(dto.getActive());
        return discountMapper.toDTO(discountRepository.save(disc));
    }


    @Override
    public void delete(String code) {
        Discount discount = getDiscountEntity(code);
        discount.setActive(false);
        discountRepository.save(discount);
    }

    private Discount getDiscountEntity(String code) {
        return discountRepository.findByCode(code).orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));
    }
}
