package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.constants.GlobalConstants;
import softuni.exam.models.dtos.OfferSeedRootDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Offer;
import softuni.exam.models.entities.Picture;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.PictureService;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final OfferRepository offerRepository;
    private final XmlParser xmlParser;
    private final SellerService sellerService;
    private final PictureService pictureService;
    private final CarService carService;

    public OfferServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, OfferRepository offerRepository, XmlParser xmlParser, SellerService sellerService, PictureService pictureService, CarService carService) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.offerRepository = offerRepository;
        this.xmlParser = xmlParser;
        this.sellerService = sellerService;
        this.pictureService = pictureService;
        this.carService = carService;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(GlobalConstants.OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder result = new StringBuilder();

        OfferSeedRootDto offerSeedRootDto = this.xmlParser.unmarshalFromFile(GlobalConstants.OFFERS_FILE_PATH, OfferSeedRootDto.class);

        offerSeedRootDto.getOffers().forEach(seedDto -> {
            if (this.validationUtil.isValid(seedDto)) {

                LocalDateTime localDate = LocalDateTime.parse(seedDto.getAddedOn(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                if (this.offerRepository.findByDescriptionAndAddedOn(seedDto.getDescription(), localDate) == null) {
                    Offer offer = this.modelMapper.map(seedDto, Offer.class);
                    offer.setAddedOn(localDate);
                    Car car = this.carService.findById(seedDto.getCar().getId());
                    Seller seller = this.sellerService.findById(seedDto.getSeller().getId());
                    Set<Picture> pictures = this.pictureService.getAllByCarId(car.getId());
                    offer.setPictures(pictures);
                    offer.setCar(car);
                    offer.setSeller(seller);
                    this.offerRepository.saveAndFlush(offer);
                    result.append(String.format("Successfully imported offer %s - %s", offer.getAddedOn().toString(), offer.isHasGoldStatus()));
                } else {
                    result.append("Invalid offer");
                }

            } else {
                result.append("Invalid offer");
            }
            result.append(System.lineSeparator());
        });

        return result.toString();
    }
}
