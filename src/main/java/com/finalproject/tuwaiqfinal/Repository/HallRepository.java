package com.finalproject.tuwaiqfinal.Repository;

import com.finalproject.tuwaiqfinal.Model.Hall;
import com.finalproject.tuwaiqfinal.Model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HallRepository extends JpaRepository<Hall, Integer> {
    Hall findHallById(Integer id);

    List<Hall> findByOwner(Owner owner);

    @Query("select h from Hall h where h.isAvailable = true")
    List<Hall> findAvailableHall();

    @Query("select h from Hall h where h.isAvailable = false")
    List<Hall> findUnAvailableHall();


}
