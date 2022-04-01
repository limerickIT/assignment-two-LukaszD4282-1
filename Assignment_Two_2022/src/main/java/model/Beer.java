/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Beer extends RepresentationModel<Beer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotEmpty(message = "ID cannot be empty !")
    private long id;
    @Min(value = 0, message = "Brewery ID cannot be less than 0 !")
    @NotNull
    @NotEmpty
    private long brewery_id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private Integer cat_id;
    @NotNull
    @NotEmpty
    private Integer style_id;
    private Double abv;
    @Max(Long.MAX_VALUE)
    private Double ibu;
    @Max(Long.MAX_VALUE)
    private Double srm;

    @Lob
    private String description;
    private Integer add_user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date last_mod;

    private String image;
    @Min(value = 0, message = "Sell_price cannot be below 0 !")
    @NotNull
    @NotEmpty
    private Double buy_price;
    @Min(value = 0, message = "Sell_price cannot be below 0 !")
    @NotNull
    @NotEmpty
    private Double sell_price;

}
