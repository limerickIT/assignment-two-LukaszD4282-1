/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import model.Breweries_Geocode;
import model.Brewery;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author ldebi
 */
public interface BreweriesGeocodeRepository extends CrudRepository<Breweries_Geocode, Long> {
    
}
