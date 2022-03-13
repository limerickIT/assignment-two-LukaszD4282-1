/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import model.Style;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BeerRepository;
import repository.StyleRepository;

/**
 *
 * @author ldebi
 */
@Service
public class styleService {
    
    @Autowired
    private StyleRepository StyleRepo;
    
        public Optional<Style> findOne(Long id) {
        return StyleRepo.findById(id);
    }
}
