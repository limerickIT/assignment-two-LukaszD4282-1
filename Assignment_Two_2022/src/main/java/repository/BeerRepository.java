/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import model.Beer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author ldebi
 */
public interface BeerRepository extends CrudRepository<Beer, Long> {
    public Slice<Beer> findAll(Pageable pageable);
}
