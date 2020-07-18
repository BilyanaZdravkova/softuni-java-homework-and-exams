package com.softuni.springexercise.services.impl;

import com.softuni.springexercise.constants.GlobalConstants;
import com.softuni.springexercise.entities.Category;
import com.softuni.springexercise.repositories.CategoryRepository;
import com.softuni.springexercise.services.CategoryService;
import com.softuni.springexercise.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

import static com.softuni.springexercise.constants.GlobalConstants.*;

@Service
public class CategoryServiceImpl implements CategoryService {
   private final CategoryRepository categoryRepository;
   private final FileUtil fileUtil;

   @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, FileUtil fileUtil) {
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedCategories() throws IOException {
        if (this.categoryRepository.count() != 0) {
            return;
        }

        String[] fileContent = this.fileUtil
                .readFileContent(CATEGORIES_FILE_PATH);

        Arrays.stream(fileContent).forEach(r -> {
            Category category = new Category(r);

            this.categoryRepository.saveAndFlush(category);
        });
    }

    @Override
    public Category getCategoryById(Long id) {
        return this.categoryRepository.getOne(id);
    }
}
