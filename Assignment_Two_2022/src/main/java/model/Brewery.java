/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Alan.Ryan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Brewery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String address1;
    @NotNull
    @NotEmpty
    private String address2;
    @NotNull
    @NotEmpty
    private String city;
    @NotNull
    @NotEmpty
    private String state;
    @NotNull
    @NotEmpty
    private String code;
    @NotNull
    @NotEmpty
    private String country;
    @NotNull
    @NotEmpty
    private String phone;
    @NotNull
    @NotEmpty
    private String website;
    @NotNull
    @NotEmpty
    private String image;

    @Lob
    private String description;

    private Integer add_user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date last_mod;

    private Double credit_limit;
    private String email;
}
