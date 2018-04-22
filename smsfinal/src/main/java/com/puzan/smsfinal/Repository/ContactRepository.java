/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzan.smsfinal.Repository;

import com.puzan.smsfinal.Entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author puzansakya
 */
public interface ContactRepository extends JpaRepository<Contact, Integer>  {

}
