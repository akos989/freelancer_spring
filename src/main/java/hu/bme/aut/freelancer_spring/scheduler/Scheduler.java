package hu.bme.aut.freelancer_spring.scheduler;

import hu.bme.aut.freelancer_spring.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;

@Component
@AllArgsConstructor
public class Scheduler {
    private final TransferService transferService;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void RouteCalculateJob() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        var transfers = transferService.getTransfersOnDate(tomorrow);

        transfers.forEach(transferService::calculateRoute);
    }
}
