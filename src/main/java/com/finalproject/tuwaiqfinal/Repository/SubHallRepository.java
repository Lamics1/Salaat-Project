package com.finalproject.tuwaiqfinal.Repository;

import com.finalproject.tuwaiqfinal.Model.Hall;
import com.finalproject.tuwaiqfinal.Model.SubHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubHallRepository extends JpaRepository<SubHall, Integer> {
    SubHall findSubHallsById(Integer id);
    List<SubHall> getSubHallByHall(Hall hall);
    List<SubHall> findByHall_IdAndPricePerHourLessThanEqualOrderByPricePerHourAscIdAsc(Integer hallId, Integer pricePerHour);
}
