package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.constants.GlobalConstants;
import softuni.exam.models.dtos.PictureSeedGsonDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PictureServiceImpl implements PictureService {

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final PictureRepository pictureRepository;
    private final Gson gson;
    private final CarService carService;

    public PictureServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, PictureRepository pictureRepository, Gson gson, CarService carService) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.pictureRepository = pictureRepository;
        this.gson = gson;
        this.carService = carService;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return Files.readString(Path.of(GlobalConstants.PICTURES_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder result = new StringBuilder();

        PictureSeedGsonDto[] dtos = this.gson.fromJson(new FileReader(GlobalConstants.PICTURES_FILE_PATH), PictureSeedGsonDto[].class);

        Arrays.stream(dtos).forEach(seedGsonDto -> {
            if (this.validationUtil.isValid(seedGsonDto)) {

                if (this.pictureRepository.findByName(seedGsonDto.getName()) == null) {
                    Picture picture = this.modelMapper.map(seedGsonDto, Picture.class);
                    LocalDateTime localDate = LocalDateTime.parse(seedGsonDto.getDateAndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    picture.setDateAndTime(localDate);
                    Car car = this.carService.findById(seedGsonDto.getCar());
                    picture.setCar(car);
                    this.pictureRepository.saveAndFlush(picture);
                    result.append(String.format("Successfully imported picture - %s", picture.getName()));
                } else {
                    result.append("Invalid picture");
                }
            } else {
                result.append("Invalid picture");
            }
            result.append(System.lineSeparator());
        });

        return result.toString();
    }

    @Override
    public Set<Picture> getAllByCarId(Integer id) {
        return this.pictureRepository.getAllByCar_Id(id);
    }
}
