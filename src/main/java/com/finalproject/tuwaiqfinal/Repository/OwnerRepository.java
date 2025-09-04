package com.finalproject.tuwaiqfinal.Repository;

import com.finalproject.tuwaiqfinal.Model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {
}
