package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.DTOin.OwnerDTO;
import com.finalproject.tuwaiqfinal.Model.Hall;
import com.finalproject.tuwaiqfinal.Model.Owner;
import com.finalproject.tuwaiqfinal.Model.SubHall;
import com.finalproject.tuwaiqfinal.Model.User;
import com.finalproject.tuwaiqfinal.Repository.HallRepository;
import com.finalproject.tuwaiqfinal.Repository.OwnerRepository;
import com.finalproject.tuwaiqfinal.Repository.SubHallRepository;
import com.finalproject.tuwaiqfinal.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HallService {

    private final HallRepository hallRepository;
    private final OwnerRepository ownerRepository;
    private final SubHallRepository subHallRepository;

    public List<Hall> getAllHalls(){
        return hallRepository.findAll();
    }

    public Hall getSingleHall(Integer hallId){
        return hallRepository.findById(hallId)
                .orElseThrow(()-> new ApiException("hall not found"));
    }

    public void addHall(Integer ownerId,Hall hall){

        //check owner
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()->new ApiException("owner not found"));

        hall.setOwner(owner);
        hallRepository.save(hall);
    }

    public void updateHall(Integer hallId,Integer ownerId, Hall hall){

        Hall oldHall = hallRepository.findById(hallId)
                .orElseThrow(()-> new ApiException("hall not found"));

        oldHall.setName(hall.getName());
        oldHall.setDescription(hall.getDescription());
        oldHall.setStatus(hall.getStatus());
        oldHall.setLocation(hall.getLocation());

        //check owner
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()-> new ApiException("owner not found"));

        oldHall.setOwner(owner);
        hallRepository.save(oldHall);
    }

    public void deleteHall(Integer hallId){
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(()-> new ApiException("hall not found"));
        hall.setOwner(null);
        hallRepository.delete(hall);
    }

    public List<SubHall> getAllSubHalls(Integer hallId){
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(()-> new ApiException("hall not found"));
        return subHallRepository.getSubHallByHall(hall);
    }

    @Service
    @AllArgsConstructor
    public static class OwnerService {
        private final OwnerRepository ownerRepository;
        private final UserRepository userRepository;
        private final WhatsAppService whatsAppService;

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

    }
}
