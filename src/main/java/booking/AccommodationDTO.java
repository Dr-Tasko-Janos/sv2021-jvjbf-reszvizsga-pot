package booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationDTO {

    private String name;
    private String country;
    private String city;
    private int availableCapacity;
    private int price;

}
