/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sd4.model.Beer;
import com.sd4.model.Brewery;
import com.sd4.model.Category;
import com.sd4.model.Style;
import com.sun.istack.NotNull;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import service.beerService;
import service.breweryService;
import service.categoryService;
import service.styleService;

/**
 *
 * @author ldebi
 */
@RestController
@RequestMapping("/beers")
public class BeerController {

    @Autowired
    private final beerService beerService;

    @Autowired
    private final breweryService breweryService;

    @Autowired
    private final categoryService categoryService;

    @Autowired
    private final styleService styleService;

    public BeerController(beerService beerService, breweryService breweryService, categoryService categoryService, styleService styleService) {
        this.beerService = beerService;
        this.breweryService = breweryService;
        this.categoryService = categoryService;
        this.styleService = styleService;
    }

    @GetMapping(value = "/getAllBeers", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Beer>> getbeers() {
        try {
            List<Beer> beerList = beerService.findAll();
            if(beerList.isEmpty())
            {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            else return ResponseEntity.ok(beerList);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No beer records found !", ex);
        }
    }

    @GetMapping(value = "getBeer/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Beer> getbeer(@PathVariable Long id) {
        try {
             Optional<Beer> b = beerService.findOne(id);
            if(!b.isPresent())
            {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            else return ResponseEntity.ok(b.get());
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer with ID of " + id + " could not be found !", ex);
        }
    }

    @GetMapping(value = "getStyle/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Style> getStyle(@PathVariable Long id) {
        try {
             Optional<Style> b = styleService.findOne(id);
            if(!b.isPresent())
            {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            else return ResponseEntity.ok(b.get());
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Style with ID of " + id + " could not be found !", ex);
        }
    }

    @GetMapping(value = "getBrewery/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Brewery> getBrewery(@PathVariable Long id) {
        try {
             Optional<Brewery> b = breweryService.findOne(id);
            if(!b.isPresent())
            {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            else return ResponseEntity.ok(b.get());
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Brewery with ID of " + id + " could not be found !", ex);
        }
    }

    @GetMapping(value = "getCategory/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        try {
             Optional<Category> b = categoryService.findOne(id);
            if(!b.isPresent())
            {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            else return ResponseEntity.ok(b.get());
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with ID of " + id + " could not be found !", ex);
        }
    }

    @PutMapping(value = "updateBeer/{id}", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateBeer(@PathVariable @NotNull Long id, @RequestBody @Valid Beer beer) {
        try {
            Beer currentBeer = beerService.findOne(id).orElseThrow(RuntimeException::new);
            
            currentBeer.setName(beer.getName()); 
            currentBeer.setBuy_price(beer.getBuy_price());
            currentBeer.setSell_price(beer.getSell_price());
            currentBeer.setAbv(beer.getAbv());
            currentBeer.setIbu(beer.getIbu());
            currentBeer.setSrm(beer.getSrm());
            currentBeer.setDescription(beer.getDescription());
            currentBeer.setLast_mod(Date.from(java.time.Clock.systemUTC().instant()));
            
            System.out.println(Date.from(java.time.Clock.systemUTC().instant()));

            System.out.println(currentBeer);
            
            beerService.saveBeer(currentBeer);

            System.out.println(currentBeer);
            
            return ResponseEntity.ok(currentBeer);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not update beer with ID of " + id + "!", ex);
        }
    }
}
