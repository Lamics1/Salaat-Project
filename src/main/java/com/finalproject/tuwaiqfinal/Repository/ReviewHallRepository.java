package com.finalproject.tuwaiqfinal.Repository;

import com.finalproject.tuwaiqfinal.Model.ReviewHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewHallRepository extends JpaRepository<ReviewHall, Integer> {
    ReviewHall findReviewHallById(Integer id);
    boolean existsByCustomerIdAndHallId(Integer customerId, Integer hallId);

    List<ReviewHall> findAllByHallId(Integer hallId);
}
