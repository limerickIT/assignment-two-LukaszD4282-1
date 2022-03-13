/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import model.Beer;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BeerRepository;

/**
 *
 * @Beer ldebi
 */
@Service
public class beerService {

    @Autowired
    private BeerRepository BeerRepo;

    public Optional<Beer> findOne(Long id) {
        return BeerRepo.findById(id);
    }

    public List<Beer> findAll() {
        return (List<Beer>) BeerRepo.findAll();
    }

    public long count() {
        return BeerRepo.count();
    }

    public void deleteByID(long BeerID) {
        BeerRepo.deleteById(BeerID);
    }

    public Beer saveBeer(Beer a) {
        return BeerRepo.save(a);
    }  
    
}//end class
