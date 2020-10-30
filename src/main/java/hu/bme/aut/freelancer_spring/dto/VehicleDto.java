package hu.bme.aut.freelancer_spring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VehicleDto {

    private Long id;

    private String name;

    private int x;

    private int y;

    private int z;

    private double weightLimit;

    private Long ownerId;
}
