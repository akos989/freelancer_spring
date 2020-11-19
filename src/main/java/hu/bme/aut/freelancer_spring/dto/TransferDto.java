package hu.bme.aut.freelancer_spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Date;

@Getter @Setter
public class TransferDto {
    private Long id;

    private Date date;

    private Long townId;

    private Long vehicleId;

    private Long carrierId;

    private double fromLat;

    private double fromLong;

    private double toLat;

    private double toLong;

    private LocalTime startTime = LocalTime.of(10,20);
}