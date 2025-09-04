package com.finalproject.tuwaiqfinal.Repository;

import com.finalproject.tuwaiqfinal.Model.SubHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubHallRepository extends JpaRepository<SubHall, Integer> {
    SubHall findSubHallsById(Integer id);
}
