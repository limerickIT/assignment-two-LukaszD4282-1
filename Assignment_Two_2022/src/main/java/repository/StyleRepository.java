/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import model.Style;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author ldebi
 */
public interface StyleRepository extends CrudRepository<Style, Long> {
    
}
