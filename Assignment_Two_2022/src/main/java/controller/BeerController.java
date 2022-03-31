/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import application.BeerPDFExporter;
import model.Beer;
import model.Brewery;
import com.sun.istack.NotNull;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.DocumentException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletResponse;
import model.Breweries_Geocode;
import model.Category;
import model.Style;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import service.breweriesGeocodeService;
import service.zipService;

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

    @Autowired
    private final breweriesGeocodeService breweriesGeocodeService;

    private final zipService zipService = new zipService();

    public BeerController(beerService beerService, breweryService breweryService, categoryService categoryService, styleService styleService, breweriesGeocodeService breweriesGeocodeService) {
        this.beerService = beerService;
        this.breweryService = breweryService;
        this.categoryService = categoryService;
        this.styleService = styleService;
        this.breweriesGeocodeService = breweriesGeocodeService;
    }

    @GetMapping(value = "getAllBeers/{page}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<Beer> getbeers(@PathVariable int page) {
        try {
            Pageable paging = PageRequest.of(page, 10);
            Slice<Beer> slicedResult = beerService.findAll(paging);
            List<Beer> beerList = slicedResult.getContent();

            beerList.forEach(b -> {
                long beerID = b.getId();
                Link selfLink = WebMvcLinkBuilder.linkTo(BeerController.class).slash("getBeer/"+ beerID).withSelfRel();
                b.add(selfLink);

                Link simpleDataLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BeerController.class).getbeerSimple(b.getId())).withRel("DisplaySimpleBeerData");
                b.add(simpleDataLink);
            });

            Link link = WebMvcLinkBuilder.linkTo(BeerController.class).slash("getAllBeers/" + page).withSelfRel();
            CollectionModel<Beer> result = CollectionModel.of(beerList, link);
            return result;

        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No beer records found !", ex);
        }
    }

    public Brewery getBrewery(@PathVariable Long id) {
        try {
            return breweryService.findOne(id).orElseThrow(NullPointerException::new);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Brewery with ID of " + id + " could not be found !", ex);
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

            if (beerInp == null) {
                return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
            } else {
                beerInp.setLast_mod(Date.from(java.time.Clock.systemUTC().instant()));
                beerService.saveBeer(beerInp);
                System.out.println("Beer after: " + beerInp);
                return new ResponseEntity(HttpStatus.CREATED);
            }
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not create beer !", ex);
        }
    }

    @DeleteMapping("deleteBeer/{id}")
    public ResponseEntity<Beer> deleteBeer(@PathVariable Long id) {
        try {
            beerService.deleteByID(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer with ID of " + id + " could not be found !", ex);
        }
    }

    @GetMapping(value = "/getZip")
    ResponseEntity<StreamingResponseBody> getZip() throws ZipException {

        ZipFile zipFile = zipService.downloadZipFileWithoutPassword();
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/zip"))
                .header("Content-Disposition", "attachment; filename=\"BeerImages.zip\"")
                .body(outputStream -> {

                    try ( OutputStream os = outputStream;  InputStream inputStream = new FileInputStream(zipFile.getFile())) {
                        IOUtils.copy(inputStream, os);
                    }
                });

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
                Link self = WebMvcLinkBuilder.linkTo(BeerController.class).slash("getBeer/" + id).withSelfRel();
                b.get().add(self);

                Link AllBeersLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BeerController.class).getbeers(0)).withRel("getAllBeers");
                b.get().add(AllBeersLink);

                return ResponseEntity.ok(b.get());
            }
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer with ID of " + id + " could not be found !", ex);
        }
    }

    @GetMapping(value = "showBeerImage/{imgType}/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> showBeerImage(@PathVariable int imgType, @PathVariable Long id) throws WriterException, IOException {
        try {
            String path = null;

            if (imgType == 1) {
                path = (System.getProperty("user.dir") + "\\src\\main\\resources\\static\\assets\\images\\large\\");
            } else {
                path = (System.getProperty("user.dir") + "\\src\\main\\resources\\static\\assets\\images\\thumbs\\");
            }

            File img = new File(path + id + ".jpg");
            byte[] image = Files.readAllBytes(img.toPath());
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer with ID of " + id + " could not be found !", ex);
        }
    }

    @GetMapping("/generatePdf/{id}")
    public void generatePdf(@PathVariable Long id, HttpServletResponse response) throws DocumentException, IOException {

        Beer b = beerService.findOne(id).get();
        Brewery br = breweryService.findOne(id).get();
        Category c = categoryService.findOne(id).get();
        Style s = styleService.findOne(id).get();

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        BeerPDFExporter exporter = new BeerPDFExporter(b, br, c, s);
        exporter.export(response);

    }

}
