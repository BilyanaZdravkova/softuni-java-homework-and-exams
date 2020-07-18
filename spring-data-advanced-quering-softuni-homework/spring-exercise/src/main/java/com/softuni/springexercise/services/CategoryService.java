package com.softuni.springexercise.services;

import com.softuni.springexercise.entities.Category;

import java.io.IOException;

public interface CategoryService {
    void seedCategories() throws IOException;

    Category getCategoryById(Long id);

}
