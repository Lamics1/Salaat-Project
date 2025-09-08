package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.DTOout.GameDTO;
import com.finalproject.tuwaiqfinal.DTOout.HallDTO;
import com.finalproject.tuwaiqfinal.DTOout.SubHallDTO;
import com.finalproject.tuwaiqfinal.Model.Hall;
import com.finalproject.tuwaiqfinal.Model.Owner;
import com.finalproject.tuwaiqfinal.Model.ReviewHall;
import com.finalproject.tuwaiqfinal.Model.SubHall;
import com.finalproject.tuwaiqfinal.Repository.HallRepository;
import com.finalproject.tuwaiqfinal.Repository.OwnerRepository;
import com.finalproject.tuwaiqfinal.Repository.SubHallRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HallService {

    private final HallRepository hallRepository;
    private final OwnerRepository ownerRepository;
    private final SubHallRepository subHallRepository;

    public List<HallDTO> getAllHalls() {
        return hallRepository.findAll()
                .stream()
                .map(hall -> {
                    HallDTO dto = new HallDTO();
                    dto.setId(hall.getId());
                    dto.setName(hall.getName());
                    dto.setDescription(hall.getDescription());
                    dto.setIsAvailable(hall.getIsAvailable());
                    dto.setLocation(hall.getLocation());

                    // SubHalls -> SubHallDTO
                    Set<SubHallDTO> subHallDTOs = hall.getSubHalls().stream()
                            .map(subHall -> {
                                SubHallDTO sh = new SubHallDTO();
                                sh.setId(subHall.getId());
                                sh.setName(subHall.getName());
                                sh.setPricePerHour(subHall.getPricePerHour());
                                sh.setDescription(subHall.getDescription());

                                // Games -> GameDTO
                                Set<GameDTO> gameDTOs = subHall.getGames().stream()
                                        .map(game -> {
                                            GameDTO g = new GameDTO();
                                            g.setId(game.getId());
                                            g.setColor(game.getColor());
                                            g.setNumberOfPlayer(game.getNumberOfPlayer());
                                            return g;
                                        })
                                        .collect(Collectors.toSet());

                                sh.setGames(gameDTOs);

                                return sh;
                            })
                            .collect(Collectors.toSet());

                    dto.setSubHalls(subHallDTOs);

                    // ReviewHall
                    Set<ReviewHall> reviewHallDTOs = hall.getReviewHalls().stream()
                            .map(review -> {
                                ReviewHall r = new ReviewHall();
                                r.setId(review.getId());
                                r.setRating(review.getRating());
                                r.setComment(review.getComment());
                                r.setCreated_at(review.getCreated_at());
                                return r;
                            })
                            .collect(Collectors.toSet());

                    dto.setReviewHalls(reviewHallDTOs);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<HallDTO> getMyHalls(Integer ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ApiException("owner not found"));

        return hallRepository.findByOwner(owner)
                .stream()
                .map(hall -> {
                    HallDTO dto = new HallDTO();
                    dto.setId(hall.getId());
                    dto.setName(hall.getName());
                    dto.setDescription(hall.getDescription());
                    dto.setIsAvailable(hall.getIsAvailable());
                    dto.setLocation(hall.getLocation());

                    // SubHalls -> SubHallDTO
                    Set<SubHallDTO> subHallDTOs = hall.getSubHalls().stream()
                            .map(subHall -> {
                                SubHallDTO sh = new SubHallDTO();
                                sh.setId(subHall.getId());
                                sh.setName(subHall.getName());
                                sh.setPricePerHour(subHall.getPricePerHour());
                                sh.setDescription(subHall.getDescription());

                                // Games -> GameDTO
                                Set<GameDTO> gameDTOs = subHall.getGames().stream()
                                        .map(game -> {
                                            GameDTO g = new GameDTO();
                                            g.setId(game.getId());
                                            g.setColor(game.getColor());
                                            g.setNumberOfPlayer(game.getNumberOfPlayer());
                                            return g;
                                        })
                                        .collect(Collectors.toSet());

                                sh.setGames(gameDTOs);

                                return sh;
                            })
                            .collect(Collectors.toSet());

                    dto.setSubHalls(subHallDTOs);

                    // ReviewHall -> ReviewHallDTO
                    Set<ReviewHall> reviewHallDTOs = hall.getReviewHalls().stream()
                            .map(review -> {
                                ReviewHall r = new ReviewHall();
                                r.setId(review.getId());
                                r.setRating(review.getRating());
                                r.setComment(review.getComment());
                                r.setCreated_at(review.getCreated_at());
                                return r;
                            })
                            .collect(Collectors.toSet());

                    dto.setReviewHalls(reviewHallDTOs);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public HallDTO getSingleHall(Integer hallId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new ApiException("hall not found"));

        HallDTO dto = new HallDTO();
        dto.setId(hall.getId());
        dto.setName(hall.getName());
        dto.setDescription(hall.getDescription());
        dto.setIsAvailable(hall.getIsAvailable()); // إذا عندك status بدل isAvailable
        dto.setLocation(hall.getLocation());

        // SubHalls -> SubHallDTO
        Set<SubHallDTO> subHallDTOs = hall.getSubHalls().stream()
                .map(subHall -> {
                    SubHallDTO sh = new SubHallDTO();
                    sh.setId(subHall.getId());
                    sh.setName(subHall.getName());
                    sh.setPricePerHour(subHall.getPricePerHour());
                    sh.setDescription(subHall.getDescription());

                    // Games -> GameDTO
                    Set<GameDTO> gameDTOs = subHall.getGames().stream()
                            .map(game -> {
                                GameDTO g = new GameDTO();
                                g.setId(game.getId());
                                g.setColor(game.getColor());
                                g.setNumberOfPlayer(game.getNumberOfPlayer());
                                return g;
                            })
                            .collect(Collectors.toSet());

                    sh.setGames(gameDTOs);

                    return sh;
                })
                .collect(Collectors.toSet());

        dto.setSubHalls(subHallDTOs);

        // ReviewHall -> ReviewHallDTO
        Set<ReviewHall> reviewHallDTOs = hall.getReviewHalls().stream()
                .map(review -> {
                    ReviewHall r = new ReviewHall();
                    r.setId(review.getId());
                    r.setRating(review.getRating());
                    r.setComment(review.getComment());
                    r.setCreated_at(review.getCreated_at());
                    return r;
                })
                .collect(Collectors.toSet());

        dto.setReviewHalls(reviewHallDTOs);

        return dto;
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
        oldHall.setIsAvailable(hall.getIsAvailable());
        oldHall.setLocation(hall.getLocation());

        //check owner
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()-> new ApiException("owner not found"));

        if (!hall.getOwner().equals(owner))
            throw new ApiException("permission denied");

        oldHall.setOwner(owner);
        hallRepository.save(oldHall);
    }

    public void deleteHall(Integer ownerId,Integer hallId){
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(()-> new ApiException("hall not found"));

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()-> new ApiException("owner not found"));

        if (!hall.getOwner().equals(owner))
            throw new ApiException("permission denied");

        hall.setOwner(null);
        hallRepository.delete(hall);
    }

    public List<SubHall> getAllSubHalls(Integer hallId){
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(()-> new ApiException("hall not found"));
        return subHallRepository.getSubHallByHall(hall);
    }

    public List<Hall> getAvailableHall(){
        return hallRepository.findAvailableHall();
    }

    public List<Hall> getUnAvailableHall(){
        return hallRepository.findUnAvailableHall();
    }

}

