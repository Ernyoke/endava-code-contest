package endava.devweek.jointhecode.mainpage;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainPageController {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String handleRequest() {
        return "Usage: send a POST request to '/stockExchange' end-point with the form data of 'file:<input>.zip'!";
    }
}
