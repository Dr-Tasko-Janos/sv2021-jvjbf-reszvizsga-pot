package booking;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/accommodations")
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<AccommodationDTO> getAccommodations(@RequestParam Optional<String> city) {
        return bookingService.getAccommodations(city);
    }

    @GetMapping("/{id}")
    public AccommodationDTO getAccommodationById(@Valid @PathVariable("id") long id) {
        return bookingService.getAccommodationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccommodationDTO createAccommodation(@Valid @RequestBody CreateAccommodationCommand command) {
        return bookingService.createAccommodation(command);
    }

    @PutMapping("{id}")
    public AccommodationDTO updateAccommodationPrice(@PathVariable("id") long id, @RequestBody UpdatePriceCommand command) {
        return bookingService.updateAccommodationPrice(id, command);
    }

    @PostMapping("/{id}/book")
    public AccommodationDTO reserveAccommodation(@PathVariable("id") long id, @RequestBody CreateReservationCommand command) {
        return bookingService.reserveAccommodation(id, command);
    }

    @DeleteMapping
    public void deleteAll() {
        bookingService.deleteAll();
    }



    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Problem> handleNotFound(IllegalArgumentException ex) {
        Problem problem =
                Problem.builder()
                        .withType(URI.create("accommodation/not-found"))
                        .withTitle("Not Found")
                        .withStatus(Status.NOT_FOUND)
                        .withDetail(ex.getMessage())
                        .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);


    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Problem> handleNotFound(IllegalStateException ex) {
        Problem problem =
                Problem.builder()
                        .withType(URI.create("accommodation/bad-reservation"))
                        .withTitle("Bad reservation")
                        .withStatus(Status.BAD_REQUEST)
                        .withDetail(ex.getMessage())
                        .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

}
