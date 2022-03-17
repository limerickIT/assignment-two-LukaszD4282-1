/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Beer;
import model.Brewery;
import model.Category;
import model.Style;
import com.sun.istack.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import service.beerService;
import service.breweryService;
import service.categoryService;
import service.styleService;
import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;

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

    @GetMapping(value = "/getAllBeers", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<Beer> getbeers() {
        try {
            List<Beer> beerList = beerService.findAll();

            beerList.forEach(b -> {
                long beerID = b.getId();
                Link selfLink = WebMvcLinkBuilder.linkTo(BeerController.class).slash(beerID).withSelfRel();
                b.add(selfLink);

                Link simpleDataLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BeerController.class).getbeerSimple(b.getId())).withRel("DisplaySimpleBeerData");
                b.add(simpleDataLink);
            });

            Link link = WebMvcLinkBuilder.linkTo(BeerController.class).withSelfRel();
            CollectionModel<Beer> result = CollectionModel.of(beerList, link);
            return result;

        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No beer records found !", ex);
        }
    }

    @GetMapping(value = "getBeer/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Beer> getbeer(@PathVariable Long id) {
        System.out.println("In getBeer");
        try {
            Optional<Beer> b = beerService.findOne(id);
            if (!b.isPresent()) {
                System.out.println("Didnt find beer");
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                Link self = WebMvcLinkBuilder.linkTo(BeerController.class).slash(id).withSelfRel();
                b.get().add(self);

                Link AllBeersLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BeerController.class).getbeers()).withRel("getAllBeers");
                b.get().add(AllBeersLink);

                return ResponseEntity.ok(b.get());
            }
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer with ID of " + id + " could not be found !", ex);
        }
    }

    @GetMapping(value = "getBeerSimple/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<String>> getbeerSimple(@PathVariable Long id) {
        System.out.println("In getBeerSimple");

        Optional<Beer> beer = beerService.findOne(id);

        try {
            List<String> simpleData = new ArrayList<>();
            simpleData.add(beer.get().getName());
            simpleData.add(beer.get().getDescription());
            simpleData.add(this.getBrewery(beer.get().getBrewery_id()).getName());

            return ResponseEntity.ok(simpleData);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer with ID of " + beer.get().getId() + " could not be found !", ex);
        }
    }

    @GetMapping("getBrewery/{id}")
    public Brewery getBrewery(@PathVariable Long id) {
        try {
            return breweryService.findOne(id).orElseThrow(NullPointerException::new);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Brewery with ID of " + id + " could not be found !", ex);
        }
    }

    @PutMapping(value = "updateBeer/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "createBeer/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beerInp) {
        try {
            System.out.println(beerInp);
            System.out.println("Beer before: " + beerInp);
            
            if(beerInp == null)
            {
                return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {     
                beerInp.setLast_mod(Date.from(java.time.Clock.systemUTC().instant()));
                beerService.saveBeer(beerInp);
                System.out.println("Beer after: " + beerInp);
                return new ResponseEntity(HttpStatus.CREATED);
            }
        } catch (RuntimeException ex) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not create beer !", ex);
        }
    } 
}
