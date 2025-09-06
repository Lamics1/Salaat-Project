package com.finalproject.tuwaiqfinal.Repository;

import com.finalproject.tuwaiqfinal.Model.ReviewSubHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewSubHallRepository extends JpaRepository<ReviewSubHall, Integer> {
    ReviewSubHall findReviewSubHallById(Integer id);
    boolean existsByCustomerIdAndSubHallId(Integer customerId, Integer subHallId);
}
