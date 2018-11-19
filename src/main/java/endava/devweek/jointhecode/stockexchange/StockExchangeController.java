package endava.devweek.jointhecode.stockexchange;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/stockExchange")
public class StockExchangeController {
    private StockExchangeService stockExchangeService;

    public StockExchangeController(StockExchangeService stockExchangeService) {
        this.stockExchangeService = stockExchangeService;
    }

    @PostMapping(consumes = { "multipart/form-data" }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String handleZip(@RequestParam(name = "file", required = false) MultipartFile multipartFile) {
        return stockExchangeService.getResult(multipartFile);
    }
}
