package com.erokhin.tools.ims24.appartement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Dmitry Erokhin (dmitry.erokhin@gmail.com)
 * 10/04/16
 */
@Repository
public interface ApartmentsRepo extends JpaRepository<Apartment, Long> {

    List<Apartment> findByPetsProhibited(boolean petsProhibited);

}
