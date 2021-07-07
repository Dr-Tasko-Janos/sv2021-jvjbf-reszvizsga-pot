package booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Accommodation {

    private long id;
    private String name;
    private String country = "Hungary";
    private String city;
    private List<Integer> numbersOfGuests = new ArrayList<>();
    private int maxCapacity;
    private int availableCapacity;
    private int price;

    public Accommodation(long id, String name, String city, int maxCapacity, int price) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.maxCapacity = maxCapacity;
        this.price = price;
        this.availableCapacity = maxCapacity;
    }

}
