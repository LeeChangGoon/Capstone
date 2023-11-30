package com.school.demo.ErrorCode;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "Operation successful"),
    BAD_REQUEST(400, "Bad request"), //현재시간 이전의 시간 예약
    UNAUTHORIZED(401, "Unauthorized access"),
    FORBIDDEN(403, "Access to the resource is forbidden"),
    NOT_FOUND(404, "Resource not found"),
    CONFLICT(409, "가용한 자리가 없습니다."), //예약 자리 0일때,
    CONFLICT2(409, "중복 예약을 할 수 없습니다."), // 중복 예약
	INTERNAL_SERVER_ERROR (500,"Internal server error");


    private final int statusCode;
    private final String message;

    ResultCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getMessage() {
        return this.message;
   }
}

