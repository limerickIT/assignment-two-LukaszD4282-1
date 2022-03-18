/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;
import model.Brewery;
import java.util.List;
import java.util.Optional;
import model.Breweries_Geocode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BeerRepository;
import repository.BreweriesGeocodeRepository;
import repository.BreweryRepository;

/**
 *
 * @author ldebi
 */
@Service
public class breweriesGeocodeService {
    
    @Autowired
    private BreweriesGeocodeRepository BreweriesGeocodeRepo;
    
    public Optional<Breweries_Geocode> findOne(Long id) {
        return BreweriesGeocodeRepo.findById(id);
    }
}
