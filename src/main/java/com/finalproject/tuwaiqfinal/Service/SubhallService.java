package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.DTOout.*;
import com.finalproject.tuwaiqfinal.Model.Game;
import com.finalproject.tuwaiqfinal.Model.Hall;
import com.finalproject.tuwaiqfinal.Model.Owner;
import com.finalproject.tuwaiqfinal.Model.SubHall;
import com.finalproject.tuwaiqfinal.Repository.HallRepository;
import com.finalproject.tuwaiqfinal.Repository.OwnerRepository;
import com.finalproject.tuwaiqfinal.Repository.SubHallRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubhallService {

    private final HallRepository hallRepository;
    private final SubHallRepository subHallRepository;
    private final OwnerRepository ownerRepository;

    private final S3Service s3Service;



    public SubHallDTO getSingleSubhall(Integer subHallId) {
        SubHall subHall = subHallRepository.findById(subHallId)
                .orElseThrow(() -> new ApiException("sub hall not found"));

        SubHallDTO dto = new SubHallDTO();

        dto.setId(subHall.getId());
        dto.setName(subHall.getName());
        dto.setPricePerHour(subHall.getPricePerHour());
        dto.setDescription(subHall.getDescription());

        // تحويل Game -> GameDTO
        Set<GameDTO> gameDTOs = subHall.getGames().stream()
                .map(game -> {
                    GameDTO g = new GameDTO();
                    g.setId(game.getId());
                    g.setColor(game.getColor());
                    g.setNumberOfPlayer(game.getNumberOfPlayer());
                    return g;
                })
                .collect(Collectors.toSet());

        dto.setGames(gameDTOs);

        //  ReviewSubHall -> ReviewSubHallDTO
        List<ReviewSubHallDTOout> reviewDTOs = subHall.getReviewSubHalls().stream()
                .map(review -> {
                    ReviewSubHallDTOout r = new ReviewSubHallDTOout();
                    r.setId(review.getId());
                    r.setRating(review.getRating());
                    r.setComment(review.getComment());
                    r.setCreatedAt(review.getCreated_at());
                    return r;
                })
                .collect(Collectors.toList());

        dto.setReviewSubHalls(reviewDTOs);

        return dto;
    }

    public void addSubHall(Integer ownerId,Integer hallId ,SubHall subHall){
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()-> new ApiException("owner not found"));

        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(()-> new ApiException("hall not found"));

        if (!hall.getOwner().equals(owner))
            throw new ApiException("permission denied");

        subHall.setHall(hall);
        subHallRepository.save(subHall);
        log.info("subHall:{} added to hall:{} by owner:{}",subHall.getId(),hall.getId(),owner.getId());
    }

    public void updateSubHall(Integer ownerId,Integer subHallId,SubHall subHall){
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()->new ApiException("owner not found"));

        SubHall oldSubHall = subHallRepository.findById(subHallId)
                .orElseThrow(()-> new ApiException("sub hall not found"));

        if (!subHall.getHall().getOwner().equals(owner))
            throw new ApiException("permission denied");

        oldSubHall.setName(subHall.getName());
        oldSubHall.setDescription(subHall.getDescription());
        oldSubHall.setPricePerHour(subHall.getPricePerHour());

        if (subHall.getHall() == null || subHall.getHall().getId() == null)
            throw new ApiException("hall id is required");

        Hall hall = hallRepository.findById(subHall.getHall().getId())
                .orElseThrow(()-> new ApiException("hall not found"));
        oldSubHall.setHall(hall);
        subHallRepository.save(oldSubHall);
        log.info("subHall:{} updated",subHall.getId());
    }

    //cannot delete because cascade

    public void deleteSubhall(Integer ownerId, Integer subHallId){
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()-> new ApiException("owner not found"));

        SubHall subHall = subHallRepository.findById(subHallId)
                .orElseThrow(()-> new ApiException("sub hall not found"));

        Hall hall = hallRepository.findById(subHall.getHall().getId())
                .orElseThrow(()-> new ApiException("hall not found"));

        if (!subHall.getHall().getOwner().getId().equals(owner.getId()))
            throw new ApiException("forbidden access");

        if (!subHall.getHall().equals(hall))
            throw new ApiException("subHall is not assigned with this main hall");

        subHall.setHall(null);
        subHallRepository.delete(subHall);
        log.info("subHall:{} deleted",subHall.getId());
    }

    public List<ReviewSubHallDTO> getSubHallsWithinBudget(Integer hallId, Integer subHallId, Integer pricePerHour) {


        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new ApiException("hall not found"));


        SubHall subHall = subHallRepository.findSubHallsById(subHallId);
        if (subHall == null) {
            throw new ApiException("sub hall not found");
        }
        if (subHall.getHall() == null || !subHall.getHall().getId().equals(hall.getId()))
            throw new ApiException("sub hall does not belong to this hall");


        if (pricePerHour == null || pricePerHour < 0)
            throw new ApiException("invalid price");

        List<SubHall> rows = subHallRepository.findByHall_IdAndPricePerHourLessThanEqualOrderByPricePerHourAscIdAsc(hallId, pricePerHour);

        List<ReviewSubHallDTO> out = new java.util.ArrayList<>();
        for (SubHall s : rows) {
            out.add(new ReviewSubHallDTO(s.getId(), s.getName(), s.getPricePerHour(), s.getDescription()));
        }

        return out;
    }


    public void addAsset(Integer subHallId, Integer ownerId, MultipartFile image) throws IOException {
        SubHall subHall = subHallRepository.findById(subHallId)
                .orElseThrow(()-> new ApiException("subBall not found"));

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()->new ApiException("owner not found"));

        if (!subHall.getHall().getOwner().equals(owner))
            throw new ApiException("permission denied");

        if (image.isEmpty())
            throw new ApiException("Cannot upload an empty file");

        String extension = FilenameUtils.getExtension(image.getOriginalFilename());

        if (!"png".equals(extension) && !"jpg".equals(extension) && !"jpeg".equals(extension))
            throw new ApiException("Only PNG, JPG, and JPEG files are allowed");

        String s3Key = "halls/"+subHall.getHall().getId()+"/subhall" + subHall.getId() + "/" + UUID.randomUUID().toString() + "." + extension;
        subHall.setImageURL(s3Service.uploadFile(s3Key,image));
        subHallRepository.save(subHall);
    }

    public AssetDTO getAsset(Integer subHallId, Integer ownerId){
        SubHall subHall = subHallRepository.findById(subHallId)
                .orElseThrow(()->new ApiException("Sub hall not found"));

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(()-> new ApiException("owner not found"));

        if (!subHall.getHall().getOwner().equals(owner))
            throw new ApiException("permission denied");

        if (subHall.getImageURL() == null) {
            throw new ApiException("Sub Hall has no image");
        }

        try {
            URL url = new URL(subHall.getImageURL());
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
