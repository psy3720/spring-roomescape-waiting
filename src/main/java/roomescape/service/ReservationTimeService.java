package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.custom.DuplicateTimeException;
import roomescape.exception.custom.ReservationTimeConflictException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeDao,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeDao;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse createReservationTime(ReservationTimeRequest reservationTimeRequest) {
        Long count = reservationTimeRepository.countByStartAt(reservationTimeRequest.getStartAt());
        if (count > 0) {
            throw new DuplicateTimeException();
        }

        ReservationTime reservationTime = reservationTimeRepository.save(convertToEntity(reservationTimeRequest));
        return this.convertToResponse(reservationTime);
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public void deleteReservationTime(Long id) {
        long count = reservationRepository.countByTimeId(id);
        if (count > 0) {
            throw new ReservationTimeConflictException();
        }
        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTimeResponse> findAllByAvailableTime(String date, Long themeId, Long memberId) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        List<ReservationTime> availableTimes = reservationTimeRepository.findAvailableTimes(date, themeId);

        List<ReservationTime> existReservationTimes = reservationTimeRepository.existReservationTimes(
                date, themeId, memberId);
        List<ReservationTime> existWaitingTimes = reservationTimeRepository.existWaitingTimes(
                date, themeId, memberId);

        reservationTimes = reservationTimes.stream()
                .filter(time -> !existReservationTimes.contains(time) && !existWaitingTimes.contains(time))
                .collect(Collectors.toList());

        return this.convertToList(availableTimes, reservationTimes);
    }

    private ReservationTimeResponse convertToResponse(ReservationTime reservationTime, boolean alreadyBooked) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt(), alreadyBooked);
    }

    private ReservationTimeResponse convertToResponse(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }

    private ReservationTime convertToEntity(ReservationTimeRequest reservationTimeRequest) {
        return new ReservationTime(reservationTimeRequest.getStartAt());
    }

    private List<ReservationTimeResponse> convertToList(List<ReservationTime> availableTimes
            , List<ReservationTime> reservationTimes) {

        return reservationTimes.stream().map(
                reservationTime -> {
                    boolean alreadyBooked = !availableTimes.contains(reservationTime);
                    return this.convertToResponse(reservationTime, alreadyBooked);
                }
        ).toList();
    }
}
