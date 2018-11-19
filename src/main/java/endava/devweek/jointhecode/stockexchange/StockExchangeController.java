package endava.devweek.jointhecode.stockexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/stockExchange")
public class StockExchangeController {
    private Logger logger = LoggerFactory.getLogger(StockExchangeService.class);
    private StockExchangeService stockExchangeService;

    public StockExchangeController(StockExchangeService stockExchangeService) {
        this.stockExchangeService = stockExchangeService;
    }

    @PostMapping(consumes = { "multipart/form-data" }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String handleZip(@RequestParam(name = "file", required = false) MultipartFile multipartFile) {
        if (multipartFile != null) {
            logger.info("Uploaded: " + multipartFile.getOriginalFilename());
        }
        return stockExchangeService.getResult(multipartFile);
    }
}
