package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.DTOout.AssetDTO;
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
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HallService {

    private final HallRepository hallRepository;
    private final OwnerRepository ownerRepository;
    private final SubHallRepository subHallRepository;
    private final S3Service s3Service;

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
                                            g.setIsAvailable(game.getIsAvailable());
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
                                            g.setIsAvailable(game.getIsAvailable());
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
                                g.setIsAvailable(game.getIsAvailable());
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

        hall.setImageURL(null);
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

    public void addAsset(Integer hallId, Integer ownerId, MultipartFile image) throws IOException {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(()-> new ApiException("hall not found"));

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()->new ApiException("owner not found"));

        if (!hall.getOwner().equals(owner))
            throw new ApiException("permission denied");

        if (image.isEmpty())
            throw new ApiException("Cannot upload an empty file");

        String extension = FilenameUtils.getExtension(image.getOriginalFilename());

        if (!"png".equals(extension) && !"jpg".equals(extension) && !"jpeg".equals(extension))
            throw new ApiException("Only PNG, JPG, and JPEG files are allowed");

        String s3Key = "halls/" + hall.getId() + "/" + UUID.randomUUID().toString() + "." + extension;
        hall.setImageURL(s3Service.uploadFile(s3Key,image));
        hallRepository.save(hall);
    }

    public AssetDTO getAsset(Integer hallId, Integer ownerId){
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(()->new ApiException("hall not found"));

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()-> new ApiException("owner not found"));

        if (!hall.getOwner().equals(owner))
            throw new ApiException("permission denied");

        if (hall.getImageURL() == null) {
            throw new ApiException("Hall has no image");
        }

        try {
            URL url = new URL(hall.getImageURL());
            String key = url.getPath().substring(1);
            byte[] data = s3Service.downloadBytes(key);

            String contentType = "application/octet-stream"; // Default
            if (key.toLowerCase().endsWith(".png")) {
                contentType = "image/png";
            } else if (key.toLowerCase().endsWith(".jpg") || key.toLowerCase().endsWith(".jpeg")) {
                contentType = "image/jpeg";
            }

            return new AssetDTO(data, contentType);
        } catch (MalformedURLException e) {
            throw new ApiException("Invalid image URL: " + e.getMessage());
        }
    }

}

