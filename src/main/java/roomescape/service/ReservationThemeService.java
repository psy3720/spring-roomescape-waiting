package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTheme;
import roomescape.dto.request.ReservationThemeRequest;
import roomescape.dto.response.ReservationThemeResponse;
import roomescape.exception.custom.DuplicateThemeException;
import roomescape.exception.custom.ReservationThemeConflictException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;

@Service
public class ReservationThemeService {

    private final ReservationThemeRepository reservationThemeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationThemeService(ReservationThemeRepository reservationThemeRepository,
                                   ReservationRepository reservationRepository) {
        this.reservationThemeRepository = reservationThemeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationThemeResponse createReservationTheme(ReservationThemeRequest request) {
        Long count = reservationThemeRepository.countByName(request.getName());
        if (count > 0) {
            throw new DuplicateThemeException();
        }

        ReservationTheme reservationTheme = reservationThemeRepository.save(this.convertToEntity(request));
        return this.convertToResponse(reservationTheme);
    }

    public List<ReservationThemeResponse> findAllReservationThemes() {
        return reservationThemeRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public void deleteReservationTheme(Long id) {
        long count = reservationRepository.countByThemeId(id);
        if (count > 0) {
            throw new ReservationThemeConflictException();
        }

        reservationThemeRepository.deleteById(id);
    }

    private ReservationThemeResponse convertToResponse(ReservationTheme reservationTheme) {
        return new ReservationThemeResponse(
                reservationTheme.getId()
                , reservationTheme.getName()
                , reservationTheme.getDescription()
                , reservationTheme.getThumbnail());
    }

    private ReservationTheme convertToEntity(ReservationThemeRequest reservationThemeRequest) {
        return new ReservationTheme(
                reservationThemeRequest.getName()
                , reservationThemeRequest.getDescription()
                , reservationThemeRequest.getThumbnail());
    }
}
