package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.constants.GlobalConstants;
import softuni.exam.models.dtos.SellerSeedRootDto;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final SellerRepository sellerRepository;
    private final XmlParser xmlParser;

    public SellerServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, SellerRepository sellerRepository, XmlParser xmlParser) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.sellerRepository = sellerRepository;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(GlobalConstants.SELLERS_FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder result = new StringBuilder();

        SellerSeedRootDto sellerSeedRootDto = this.xmlParser.unmarshalFromFile(GlobalConstants.SELLERS_FILE_PATH, SellerSeedRootDto.class);

        sellerSeedRootDto.getSellers().forEach(sellerSeedDto -> {
            if (this.validationUtil.isValid(sellerSeedDto)) {
                if (this.sellerRepository.findByEmail(sellerSeedDto.getEmail()) == null) {
                    Seller seller = this.modelMapper.map(sellerSeedDto, Seller.class);
                    this.sellerRepository.saveAndFlush(seller);
                    result.append(String.format("Successfully imported seller %s - %s", seller.getLastName(), seller.getEmail()));
                } else {
                    result.append("Invalid seller");
                }

            } else {
                result.append("Invalid seller");
            }
            result.append(System.lineSeparator());

        });

        return result.toString();
    }

    @Override
    public Seller findById(Integer id) {
        return this.sellerRepository.findById(id).orElse(null);
    }
}
