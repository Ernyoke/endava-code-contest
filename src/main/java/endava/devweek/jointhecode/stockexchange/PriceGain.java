package endava.devweek.jointhecode.stockexchange;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PriceGain {
    @JsonSerialize(using = ToStringSerializer.class)
    private float buyPoint;

    @JsonSerialize(using = ToStringSerializer.class)
    private float sellPoint;
}
