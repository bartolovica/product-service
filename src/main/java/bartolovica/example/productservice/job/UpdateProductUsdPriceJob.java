package bartolovica.example.productservice.job;

import bartolovica.example.productservice.repository.ProductRepository;
import bartolovica.example.productservice.service.ConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateProductUsdPriceJob {

    private final ProductRepository productRepository;
    private final ConversionService conversionService;

    @Scheduled(cron = "${job.update-conversion-rate.cron}")
    public void updateProductUsdPrice() {
        log.info("Starting product USD price update job.");
        conversionService.getUsdToEurConversion()
                .doOnNext(conversionRate -> log.info("Conversion rate: {}", conversionRate))
                .flatMap(productRepository::updateProductUsdPrice)
                .doOnNext(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        log.info("Number of rows updated: {}", rowsUpdated);
                    } else {
                        log.info("No rows were updated.");
                    }
                })
                .subscribe();
    }
}
