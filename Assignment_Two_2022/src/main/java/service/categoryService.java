/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import com.sd4.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BeerRepository;
import repository.CategoryRepository;

/**
 *
 * @author ldebi
 */
@Service
public class categoryService {
    
    @Autowired
    private CategoryRepository CategoryRepo;
    
        public Optional<Category> findOne(Long id) {
        return CategoryRepo.findById(id);
    }
}
