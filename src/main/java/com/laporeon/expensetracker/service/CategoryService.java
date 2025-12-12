package com.laporeon.expensetracker.service;

import com.laporeon.expensetracker.dto.request.CreateCategoryDTO;
import com.laporeon.expensetracker.dto.response.CategoryResponseDTO;
import com.laporeon.expensetracker.dto.response.PageResponseDTO;
import com.laporeon.expensetracker.entity.Category;
import com.laporeon.expensetracker.exception.AlreadyRegisteredException;
import com.laporeon.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDTO createCategory(CreateCategoryDTO dto) {

        if(categoryRepository.existsByName(dto.name().toLowerCase())) {
            throw new AlreadyRegisteredException("Category already registered");
        }

        Category category = Category.builder()
                                    .name(dto.name())
                                    .build();

        categoryRepository.save(category);

        return new CategoryResponseDTO(
                category.getName()
        );
    }

    public PageResponseDTO<CategoryResponseDTO> listCategories(Pageable pageable) {

        Page<CategoryResponseDTO> categories = categoryRepository.findAll(pageable)
                                                                 .map(category -> new CategoryResponseDTO(
                                                                         category.getName()));

        return new PageResponseDTO<>(
                categories.getContent(),
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalPages(),
                categories.getTotalElements(),
                categories.getNumberOfElements(),
                categories.isFirst(),
                categories.isLast(),
                categories.isEmpty(),
                categories.getSort().isSorted(),
                categories.getSort().isUnsorted()
        );
    }

}
