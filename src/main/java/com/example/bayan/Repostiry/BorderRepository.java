package com.example.bayan.Repostiry;

import com.example.bayan.Model.Border;
import com.example.bayan.Model.CustomsBroker;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BorderRepository extends JpaRepository<Border, Integer> {

    @Query("SELECT c FROM CustomsBroker c JOIN c.borderNames b WHERE b = :borderName")
    List<CustomsBroker> findByBorderName(String borderName);


    Border findAllByName(String name);

    Border findBorderByName( String name);
}
