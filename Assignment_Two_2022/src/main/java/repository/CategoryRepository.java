/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import com.sd4.model.Category;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author ldebi
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {
    
}
