package com.laporeon.expensetracker.service;

import com.laporeon.expensetracker.dto.request.CreateCategoryDTO;
import com.laporeon.expensetracker.dto.response.CategoryResponseDTO;
import com.laporeon.expensetracker.entity.Category;
import com.laporeon.expensetracker.exception.AlreadyRegisteredException;
import com.laporeon.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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

    public List<CategoryResponseDTO> listCategories() {

        return categoryRepository.findAll()
                                 .stream()
                                 .map(category -> new CategoryResponseDTO(category.getName()))
                                 .sorted(Comparator.comparing(CategoryResponseDTO::name))
                                 .toList();
    }

}
