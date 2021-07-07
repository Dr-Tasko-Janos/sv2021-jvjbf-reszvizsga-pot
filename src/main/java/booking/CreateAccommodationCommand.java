package booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccommodationCommand {

    @NotEmpty
    private String name;
    @NotEmpty
    private String city;
    @Min(10)
    private int maxCapacity;
    private int price;

}
