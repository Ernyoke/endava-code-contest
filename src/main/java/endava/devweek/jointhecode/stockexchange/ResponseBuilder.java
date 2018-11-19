package endava.devweek.jointhecode.stockexchange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.util.Map;

public class ResponseBuilder {
    public static String build(Map<File, PriceGain> responseMap) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        responseMap.forEach((file, priceGain) -> {
            JsonNode node = mapper.convertValue(priceGain, JsonNode.class);
            rootNode.set(file.getName(), node);
        });
        return rootNode.toString();
    }
}
