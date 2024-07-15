package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationType;
import roomescape.domain.Waiting;
import roomescape.dto.request.ReservationAdminRequest;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.request.ReservationWaitingRequest;
import roomescape.dto.response.ReservationMineResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.WaitingResponse;
import roomescape.dto.response.WaitingWithRank;
import roomescape.exception.custom.ExistingReservationException;
import roomescape.exception.custom.InvalidReservationThemeException;
import roomescape.exception.custom.InvalidReservationTimeException;
import roomescape.exception.custom.PastDateReservationException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ReservationWaitingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;
    private final MemberService memberService;
    private final ReservationWaitingRepository reservationWaitingRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ReservationThemeRepository reservationThemeRepository, MemberService memberService,
                              ReservationWaitingRepository reservationWaitingRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationThemeRepository = reservationThemeRepository;
        this.memberService = memberService;
        this.reservationWaitingRepository = reservationWaitingRepository;
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

    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);

        Waiting waiting = reservationWaitingRepository.findFirstByOrderByIdAsc();
        if (waiting != null) {
            reservationRepository.save(new Reservation(waiting.getDate(), waiting.getTime(), waiting.getTheme()
                    , ReservationType.RESERVED.getName()
                    , waiting.getMemberId()));

            reservationWaitingRepository.delete(waiting);
        }
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
                , ReservationType.RESERVED.getName()
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
        List<ReservationMineResponse> reservationMineResponses = reservationRepository.findAllByMemberId(memberId)
                .stream()
                .map(this::convertToReservationMineResponse)
                .collect(Collectors.toList());

        List<ReservationMineResponse> reservationMineWaitingResponses = reservationWaitingRepository.findWaitingsWithRankByMemberId(
                        memberId)
                .stream()
                .map(this::convertToReservationMineWaitingResponse)
                .collect(Collectors.toList());

        reservationMineResponses.addAll(reservationMineWaitingResponses);

        return reservationMineResponses;
    }

    private ReservationMineResponse convertToReservationMineWaitingResponse(WaitingWithRank waitingWithRank) {
        Waiting waiting = waitingWithRank.getWaiting();

        ReservationTheme theme = findReservationThemeById(waiting.getTheme().getId());
        ReservationTime time = findReservationTimeById(waiting.getTime().getId());

        return new ReservationMineResponse(
                waiting.getId()
                , theme.getName()
                , waiting.getDate()
                , time.getStartAt()
                , ReservationType.WAITING.getName()
                , waitingWithRank.getRank()
        );
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

    public WaitingResponse createReservationWaiting(ReservationWaitingRequest request, Long memberId) {
        Waiting waiting = reservationWaitingRepository.save(convertToWaiting(request, memberId));
        return convertToWaitingResponse(waiting);
    }

    public Waiting convertToWaiting(ReservationWaitingRequest request, Long memberId) {
        ReservationTheme theme = findReservationThemeById(request.getThemeId());
        ReservationTime time = findReservationTimeById(request.getTimeId());

        return new Waiting(request.getDate()
                , theme
                , time
                , memberId);
    }

    public WaitingResponse convertToWaitingResponse(Waiting waiting) {
        return new WaitingResponse(waiting.getId()
                , waiting.getDate()
                , waiting.getTheme().getId()
                , waiting.getTime().getId());
    }

    public void reservationMineDelete(Long waitingId) {
        reservationWaitingRepository.deleteById(waitingId);
    }
}
