package roomescape.dto.response;

import roomescape.domain.Waiting;

public class WaitingWithRank {

    private Waiting waiting;

    private Long Rank;

    public WaitingWithRank(Waiting waiting, Long rank) {
        this.waiting = waiting;
        Rank = rank;
    }

    public Waiting getWaiting() {
        return waiting;
    }

    public Long getRank() {
        return Rank;
    }
}
