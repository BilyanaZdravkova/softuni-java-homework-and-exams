package softuni.exam.models.dtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;


public class PictureSeedGsonDto {
    @Expose
    private String name;
    @Expose
    private String dateAndTime;
    @Expose
    private Integer car;

    public PictureSeedGsonDto() {
    }

    @Length(min = 3, max = 19)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @NotNull
    public Integer getCar() {
        return car;
    }

    public void setCar(Integer car) {
        this.car = car;
    }
}
