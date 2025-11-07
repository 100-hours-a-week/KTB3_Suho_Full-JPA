package kr.adapterz.jpa_practice.exception;

import kr.adapterz.jpa_practice.dto.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Void>> handleIllegalArgument(IllegalArgumentException e) {

        CommonResponse<Void> response = new CommonResponse<Void>(
                "400",
                e.getMessage(),
                null
        );

        return ResponseEntity.status(400).body(response);

    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CommonResponse<Void>> handleForbidden(ForbiddenException e) {

        CommonResponse<Void> response = new CommonResponse<>(
                "403",
                e.getMessage(),
                null
        );

        return ResponseEntity.status(403).body(response);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleException(Exception e) {

        CommonResponse<Void> response = new CommonResponse<>(
                "500",
                "서버 내부 오류가 발생했습니다.",
                null
        );

        return ResponseEntity.status(500).body(response);

    }

}