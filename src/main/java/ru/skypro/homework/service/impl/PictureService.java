package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.PictureDto;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.PictureMapper;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Picture;
import ru.skypro.homework.repo.AdsRepository;
import ru.skypro.homework.repo.PictureRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
public class PictureService {

    private final Logger logger = LoggerFactory.getLogger(PictureService.class);
    private final PictureRepository pictureRepository;
    private final AdsRepository adsRepository;
    private final PictureMapper pictureMapper;

    public PictureService(PictureRepository pictureRepository, AdsRepository adsRepository, PictureMapper pictureMapper) {
        this.pictureRepository = pictureRepository;
        this.adsRepository = adsRepository;
        this.pictureMapper = pictureMapper;
    }

    public PictureDto uploadAdsPicture(Long idAds, MultipartFile pictureFile) throws IOException, NotFoundException {
        logger.info("Method was called - uploadPetPhoto");
        Optional<Ads> ads = Optional.ofNullable(adsRepository.findById(idAds).orElseThrow(NotFoundException::new));

        Picture picture = new Picture();
        picture.setAds(ads.get());
        picture.setFileSize((int) pictureFile.getSize());
        picture.setMediaType(pictureFile.getContentType());
        picture.setData(pictureFile.getBytes());
        pictureRepository.save(picture);
        return pictureMapper.pictureToPictureDto(picture, ads.get());
    }

    public Picture findPictureById(Long idPicture) throws NotFoundException {
        return pictureRepository.findById(idPicture).orElseThrow(NotFoundException::new);
    }

    public void deletePicture(Long idPicture) throws NotFoundException {
        Optional<Picture> picture = Optional.ofNullable(pictureRepository.findById(idPicture)
                .orElseThrow(NotFoundException::new));
        pictureRepository.deleteById(idPicture);
    }

    public void deleteAll() {
        pictureRepository.deleteAll();
    }
}
