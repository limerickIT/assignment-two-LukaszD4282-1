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
@RequestMapping("/brewery")
public class BreweryController {

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

    public BreweryController(beerService beerService, breweryService breweryService, categoryService categoryService, styleService styleService, breweriesGeocodeService breweriesGeocodeService) {
        this.beerService = beerService;
        this.breweryService = breweryService;
        this.categoryService = categoryService;
        this.styleService = styleService;
        this.breweriesGeocodeService = breweriesGeocodeService;
    }

        @GetMapping("getBrewery/{id}")
    public Brewery getBrewery(@PathVariable Long id) {
        try {
            return breweryService.findOne(id).orElseThrow(NullPointerException::new);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Brewery with ID of " + id + " could not be found !", ex);
        }
    }

    @GetMapping("getBreweryGeocode/{id}")
    public Breweries_Geocode getBreweryGeocode(@PathVariable Long id) {
        try {
            System.out.println("In getBreweryGeocode");
            return breweriesGeocodeService.findOne(id).orElseThrow(NullPointerException::new);
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Brewery with ID of " + id + " could not be found !", ex);
        }
    }

    @GetMapping(value = "breweryQR/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public BufferedImage showBreweryQR(@PathVariable Long id) throws WriterException, IOException {
        try {
            Brewery br = this.getBrewery(id);
            String data = "MECARD:N:" + br.getName() + ";ADR:" + br.getAddress1() + " " + br.getAddress2() + ";TEL:" + br.getPhone() + ";EMAIL:" + br.getEmail() + ";URL:" + br.getWebsite() + ";;";
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 250, 250);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);

        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Brewery with ID of " + id + " could not be found !", ex);
        }
    }

    @GetMapping("breweryMap/{id}")
    @ResponseBody
    public String showBrewery(@PathVariable Long id) {
        try {

            Breweries_Geocode bc = this.getBreweryGeocode(id);
            Brewery br = this.getBrewery(id);

            return "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "  <head>\n"
                    + "    <title>Breweries map</title>\n"
                    + "  </head>\n"
                    + "  <body>\n"
                    + "    <h3>Name of Brewery: " + br.getName() + "</h3>\n"
                    + "    <h3>Address: " + br.getAddress1() + " " + br.getAddress2() + "</h3>\n"
                    + "    <!--The div element for the map -->\n"
                    + "    <div id=\"map\"></div>\n"
                    + "\n"
                    + "    <!-- Async script executes immediately and must be after any DOM elements used in callback. -->\n"
                    + "    <script\n"
                    + "      src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyBn4nK-n0-yNcbHYYQlecEGaD5KIzP0sPM&callback=initMap&v=weekly\"\n"
                    + "      async\n"
                    + "    ></script>\n"
                    + "  </body>\n"
                    + "</html>"
                    + "<style>/* Set the size of the div element that contains the map */\n"
                    + "#map {\n"
                    + "  height: 700px;\n"
                    + "  /* The height is 400 pixels */\n"
                    + "  width: 100%;\n"
                    + "  /* The width is the width of the web page */\n"
                    + "}</style><script>// Initialize and add the map\n"
                    + "function initMap() {\n"
                    + "  // The location of Uluru\n"
                    + "  const uluru = { lat: " + bc.getLatitude() + ", lng: " + bc.getLongitude() + " };\n"
                    + "  // The map, centered at Uluru\n"
                    + "  const map = new google.maps.Map(document.getElementById(\"map\"), {\n"
                    + "    zoom: 15,\n"
                    + "    center: uluru,\n"
                    + "  });\n"
                    + "  // The marker, positioned at Uluru\n"
                    + "  const marker = new google.maps.Marker({\n"
                    + "    position: uluru,\n"
                    + "    map: map,\n"
                    + "  });\n"
                    + "}</script>";
        } catch (NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found", ex);
        }
    }
}
