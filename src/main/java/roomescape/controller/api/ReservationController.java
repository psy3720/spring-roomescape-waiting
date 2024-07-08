package roomescape.controller.api;


import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.annotations.ValidationSequence;
import roomescape.domain.LoginMember;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.request.ReservationWaitingRequest;
import roomescape.dto.response.ReservationMineResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.WaitingResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAllReservations() {
        List<ReservationResponse> reservations = reservationService.findAllReservations();
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody @Validated(ValidationSequence.class) ReservationRequest request,
            LoginMember loginMember) {
        ReservationResponse newReservation = reservationService.createReservation(request, loginMember);

        return ResponseEntity
                .created(URI.create("/reservations/" + newReservation.getId()))
                .body(newReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable(value = "id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationMineResponse>> reservationMine(LoginMember loginMember) {
        List<ReservationMineResponse> reservationMineResponses = reservationService.reservationMine(
                loginMember.getId());

        return ResponseEntity.ok(reservationMineResponses);
    }

    @DeleteMapping("/mine/{id}")
    public ResponseEntity<Void> reservationMineDelete(LoginMember loginMember, @PathVariable("id") Long waitingId) {
        reservationService.reservationMineDelete(waitingId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/waiting")
    public ResponseEntity<WaitingResponse> reservationWaiting(@RequestBody @Valid ReservationWaitingRequest request,
                                                              LoginMember loginMember) {
        WaitingResponse waitingResponse = reservationService.createReservationWaiting(request, loginMember.getId());

        return ResponseEntity.ok(waitingResponse);
    }
}
