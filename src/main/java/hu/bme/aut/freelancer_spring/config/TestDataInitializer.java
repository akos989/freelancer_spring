package hu.bme.aut.freelancer_spring.config;

import hu.bme.aut.freelancer_spring.model.Transfer;
import hu.bme.aut.freelancer_spring.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class TestDataInitializer implements ApplicationRunner {

    private final TransferRepository transferRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        transferRepository.saveAll(
                Arrays.asList(
                        new Transfer(new Date(), 23.34, 34.546),
                        new Transfer(new Date(), 13.34, 344.546),
                        new Transfer(new Date(), 543.34, 344.546),
                        new Transfer(new Date(), 237.34, 364.546)
                )
        );
    }
}
