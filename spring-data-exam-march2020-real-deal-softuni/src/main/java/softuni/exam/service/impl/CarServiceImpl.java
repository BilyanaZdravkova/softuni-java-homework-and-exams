package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.constants.GlobalConstants;
import softuni.exam.models.dtos.CarSeedDto;
import softuni.exam.models.entities.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CarRepository carRepository;
    private final Gson gson;

    public CarServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, CarRepository carRepository, Gson gson) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.carRepository = carRepository;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(Path.of(GlobalConstants.CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder result = new StringBuilder();

        CarSeedDto[] dtos = this.gson.fromJson(new FileReader(GlobalConstants.CARS_FILE_PATH), CarSeedDto[].class);

        Arrays.stream(dtos).forEach(carSeedDto -> {
            if (this.validationUtil.isValid(carSeedDto)) {

                if (this.carRepository.findByMakeAndModelAndKilometers(carSeedDto.getMake(), carSeedDto.getModel(), carSeedDto.getKilometers()) == null) {
                    Car car = this.modelMapper.map(carSeedDto, Car.class);
                    LocalDate localDate = LocalDate.parse(carSeedDto.getRegisteredOn(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    car.setRegisteredOn(localDate);
                    this.carRepository.saveAndFlush(car);
                    result.append(String.format("Successfully imported car - %s - %s", car.getMake(), car.getModel()));
                } else {
                    result.append("Invalid car");
                }
            } else {
                result.append("Invalid car");
            }
            result.append(System.lineSeparator());
        });

        return result.toString();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder result = new StringBuilder();
        this.carRepository.getAllCarsOrderByPicturesAndMake().forEach(car -> {
            result.append(String.format("Car make - %s, model - %s\n" +
                    "\tKilometers - %d\n" +
                    "\tRegistered on - %s\n" +
                    "\tNumber of pictures - %d\n", car.getMake(),
                    car.getModel(), car.getKilometers(),
                    car.getRegisteredOn(), car.getPictures().size()));
        });
        return result.toString();
    }

    @Override
    public Car findById(Integer id) {
        return this.carRepository.findById(id).orElse(null);
    }
}
