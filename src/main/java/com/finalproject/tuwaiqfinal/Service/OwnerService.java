package com.finalproject.tuwaiqfinal.Service;


import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.DTOin.OwnerDTO;
import com.finalproject.tuwaiqfinal.Model.Hall;
import com.finalproject.tuwaiqfinal.Model.Owner;
import com.finalproject.tuwaiqfinal.Model.ReviewHall;
import com.finalproject.tuwaiqfinal.Model.User;
import com.finalproject.tuwaiqfinal.Repository.HallRepository;
import com.finalproject.tuwaiqfinal.Repository.OwnerRepository;
import com.finalproject.tuwaiqfinal.Repository.ReviewHallRepository;
import com.finalproject.tuwaiqfinal.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final ReviewHallRepository reviewHallRepository;
    private final HallRepository hallRepository;
    private final AiService aiService;

    public List<Owner> getAllOwners(){
        return ownerRepository.findAll();
    }

    public Owner getSingleOwner(Integer ownerId){
        return ownerRepository.findById(ownerId)
                .orElseThrow(()-> new ApiException("owner not found"));
    }

    public void registerOwner(OwnerDTO ownerDTO){
        User user = new User();

        user.setUsername(ownerDTO.getUsername());
        user.setPassword(ownerDTO.getPassword());   //todo: add encryption
        user.setEmail(ownerDTO.getEmail());
        user.setRole("OWNER");
        userRepository.save(user);


        Owner owner = new Owner();
        owner.setAccount_serial_num(ownerDTO.getAccount_serial_num());
        owner.setUser(user);
        ownerRepository.save(owner);
    }

    public void updateOwner(Integer ownerId, OwnerDTO ownerDTO){
        Owner oldOwner= ownerRepository.findById(ownerId)
                .orElseThrow(()->new ApiException("owner not found"));

        User user = oldOwner.getUser();
        user.setUsername(ownerDTO.getUsername());
        user.setPassword(ownerDTO.getPassword());   //todo: add encryption
        user.setEmail(ownerDTO.getEmail());
        user.setRole("OWNER");
        userRepository.save(user);

        oldOwner.setAccount_serial_num(ownerDTO.getAccount_serial_num());
        oldOwner.setUser(user);
        ownerRepository.save(oldOwner);
    }

    public void deleteOwner(Integer ownerId){
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()-> new ApiException("owner not found"));

        ownerRepository.delete(owner);
    }

    public String reviewFeedback(Integer ownerId, Integer hallId) {

        // check if owner exists
        Owner owner = ownerRepository.findOwnerById(ownerId);
        if(owner == null) {
            throw new ApiException("owner not found");
        }

        // check if hall exists
        Hall hall = hallRepository.findHallById(hallId);
        if(hall == null) {
            throw new ApiException("hall not found");
        }

        // check if hall and owner belong to each other
        if(hall.getOwner() == null || !hall.getOwner().getId().equals(owner.getId())) {
            throw new ApiException("hall and owner not belong to each other");
        }

        // find all hall reviews from owner
        List<ReviewHall> reviewHalls = reviewHallRepository.findAllByHallId(hall.getId());

        if(reviewHalls.isEmpty()) {
            throw new ApiException("reviews are empty nothing to review");
        }
        return aiService.hallFeedback(reviewHalls);
    }

}
