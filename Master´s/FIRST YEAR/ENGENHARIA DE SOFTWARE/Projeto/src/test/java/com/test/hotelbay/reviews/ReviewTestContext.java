package com.test.hotelbay.reviews;

import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;

@Component
public class ReviewTestContext {

    private ResultActions action;
    private Long reservationId;

    public ResultActions getAction() {
        return action;
    }

    public void setAction(ResultActions action) {
        this.action = action;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}