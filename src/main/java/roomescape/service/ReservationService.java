package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationAdminRequest;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationMineResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.custom.ExistingReservationException;
import roomescape.exception.custom.InvalidReservationThemeException;
import roomescape.exception.custom.InvalidReservationTimeException;
import roomescape.exception.custom.PastDateReservationException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;
    private final MemberService memberService;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ReservationThemeRepository reservationThemeRepository, MemberService memberService) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationThemeRepository = reservationThemeRepository;
        this.memberService = memberService;
    }

    public ReservationResponse createReservation(ReservationRequest reservationRequest, LoginMember loginMember) {
        validateReservationCreation(reservationRequest);

        Member findMember = memberService.findById(loginMember.getId());

        Reservation reservation = reservationRepository.save(
                this.convertToEntity(reservationRequest, findMember));
        return this.convertToResponse(reservation);
    }

    public ReservationResponse createAdminReservation(ReservationAdminRequest request, Member member) {
        ReservationRequest reservationRequest = new ReservationRequest(request.getDate()
                , request.getTimeId()
                , request.getThemeId());
        validateReservationCreation(reservationRequest);

        Reservation reservation = reservationRepository.save(
                this.convertToEntity(reservationRequest, member));
        return this.convertToResponse(reservation);
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    private void validateReservationCreation(ReservationRequest reservationRequest) {
        findReservationThemeById(reservationRequest.getThemeId());
        ReservationTime reservationTime = findReservationTimeById(reservationRequest.getTimeId());

        long count = reservationRepository.countByReservationDateAndTimeIdAndThemeId(
                reservationRequest.getDate()
                , reservationRequest.getTimeId()
                , reservationRequest.getThemeId());
        if (count > 0) {
            throw new ExistingReservationException();
        }

        if (isDateExpired(reservationRequest.getDate(), reservationTime.getStartAt())) {
            throw new PastDateReservationException();
        }
    }

    private ReservationResponse convertToResponse(Reservation reservation) {
        Member findMember = memberService.findById(reservation.getMemberId());

        return new ReservationResponse(reservation.getId(), findMember.getName(), reservation.getReservationDate(),
                reservation.getTime().getStartAt(), reservation.getTheme().getName());
    }

    private Reservation convertToEntity(ReservationRequest reservationRequest, Member member) {
        ReservationTime reservationTime = findReservationTimeById(reservationRequest.getTimeId());
        ReservationTheme reservationTheme = findReservationThemeById(reservationRequest.getThemeId());

        return new Reservation(reservationRequest.getDate()
                , reservationTime
                , reservationTheme
                , "예약"
                , member.getId());
    }

    private ReservationTime findReservationTimeById(Long timeId) {
        return reservationTimeRepository
                .findById(timeId)
                .orElseThrow(InvalidReservationTimeException::new);
    }

    private ReservationTheme findReservationThemeById(Long themeId) {
        return reservationThemeRepository
                .findById(themeId)
                .orElseThrow(InvalidReservationThemeException::new);
    }

    private boolean isDateExpired(String date, String startAt) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(startAt));

        return reservationDateTime.isBefore(now);
    }

    public List<ReservationMineResponse> reservationMine(Long memberId) {
        return reservationRepository.findAllByMemberId(memberId)
                .stream()
                .map(this::convertToReservationMineResponse)
                .toList();
    }

    private ReservationMineResponse convertToReservationMineResponse(Reservation reservation) {
        return new ReservationMineResponse(
                reservation.getId()
                , reservation.getTheme().getName()
                , reservation.getReservationDate()
                , reservation.getTime().getStartAt()
                , reservation.getStatus()
        );
    }
}
