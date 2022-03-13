/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;
import model.Brewery;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BeerRepository;
import repository.BreweryRepository;

/**
 *
 * @author ldebi
 */
@Service
public class breweryService {
    
    @Autowired
    private BreweryRepository BreweryRepo;
    
    public Optional<Brewery> findOne(Long id) {
        return BreweryRepo.findById(id);
    }
}
