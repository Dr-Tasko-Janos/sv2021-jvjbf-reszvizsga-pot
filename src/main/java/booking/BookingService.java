package booking;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private ModelMapper modelMapper;

    private List<Accommodation> accommodationsList = Collections.synchronizedList(new ArrayList<>());

    private AtomicLong idGenerator = new AtomicLong();

    public BookingService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    private Accommodation findAccommodationById(long id) {

        Accommodation foundAccommodation = accommodationsList.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find accommodation with the given id"));

        return foundAccommodation;
    }

    public List<AccommodationDTO> getAccommodations(Optional<String> city) {

        Type returnListType = new TypeToken<List<AccommodationDTO>>() {
        }.getType();

        List<Accommodation> filteredAccommodationsList = accommodationsList.stream()
                .filter(e -> city.isEmpty() || e.getCity().equalsIgnoreCase(city.get()))
                .collect(Collectors.toList());
        return modelMapper.map(filteredAccommodationsList, returnListType);
    }

    public void deleteAll() {
        accommodationsList.clear();
        idGenerator = new AtomicLong();
    }

    public AccommodationDTO getAccommodationById(long id) {
        return modelMapper.map(findAccommodationById(id), AccommodationDTO.class);
    }

    public AccommodationDTO createAccommodation(CreateAccommodationCommand command) {
        Accommodation newAccommodation = new Accommodation(idGenerator.incrementAndGet(), command.getName(), command.getCity(), command.getMaxCapacity(), command.getPrice());
        accommodationsList.add(newAccommodation);
        return modelMapper.map(newAccommodation, AccommodationDTO.class);
    }

    public AccommodationDTO updateAccommodationPrice(long id, UpdatePriceCommand command) {
        Accommodation accommodationForPriceUpdate = findAccommodationById(id);
        if (accommodationForPriceUpdate.getPrice() != command.getNewPrice()) {
            accommodationForPriceUpdate.setPrice(command.getNewPrice());
        }
        return modelMapper.map(accommodationForPriceUpdate, AccommodationDTO.class);
    }

    public AccommodationDTO reserveAccommodation(long id, CreateReservationCommand command) {
        Accommodation accommodationForReservation = findAccommodationById(id);
        if (command.getRequestCapacity() <= accommodationForReservation.getAvailableCapacity()) {
            accommodationForReservation.getNumbersOfGuests().add(command.getRequestCapacity());
            accommodationForReservation.setAvailableCapacity(accommodationForReservation.getAvailableCapacity() - command.getRequestCapacity());
        } else {
            throw new IllegalStateException("There isn't enough capacity available at the moment");
        }
        return modelMapper.map(accommodationForReservation, AccommodationDTO.class);

    }
}
