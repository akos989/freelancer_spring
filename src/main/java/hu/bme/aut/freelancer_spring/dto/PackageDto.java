package hu.bme.aut.freelancer_spring.dto;

import hu.bme.aut.freelancer_spring.model.enums.Size;
import hu.bme.aut.freelancer_spring.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PackageDto {

    private Long id;

    private String name;

    private Size size;

    private double weight;

    private double fromLat;

    private double fromLong;

    private double toLat;

    private double toLong;

    private Status status;

    private Long senderId;

    private Long transferId;
}
