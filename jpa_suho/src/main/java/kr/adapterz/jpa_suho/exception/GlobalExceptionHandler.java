package kr.adapterz.jpa_suho.exception;

import jakarta.persistence.EntityNotFoundException;
import kr.adapterz.jpa_suho.dto.common.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ===== 기존 커스텀 예외 핸들러 =====

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Void>> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("IllegalArgumentException: {}", e.getMessage());

        CommonResponse<Void> response = new CommonResponse<>(
                "400",
                e.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CommonResponse<Void>> handleForbidden(ForbiddenException e) {
        log.warn("ForbiddenException: {}", e.getMessage());

        CommonResponse<Void> response = new CommonResponse<>(
                "403",
                e.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // ===== Spring Web 예외 핸들러 =====

    /**
     * Bean Validation 실패 처리 (@Valid, @Validated)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Map<String,String>>> handleValidationErrors(MethodArgumentNotValidException e) {
        log.warn("Validation failed: {}", e.getMessage());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
           errors.put(error.getField(), error.getDefaultMessage())
        );

        CommonResponse<Map<String,String>> response = new CommonResponse<>(
                "400",
                "입력값 검증에 실패했습니다.",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * JSON 파싱 오류 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("Failed to read HTTP Message: {}", e.getMessage());

        CommonResponse<Void> response = new CommonResponse<>(
                "400",
                "요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요.",
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 경로 변수 타입 불일치 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CommonResponse<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException e) {
        log.warn("Type mismatch for parameter '{}': {}", e.getName(), e.getValue());

        String message = String.format(
                "파라미터 '%s'의 값 '%s'이(가) 올바르지 않습니다.",
                e.getName(),
                e.getValue()
        );

        CommonResponse<Void> response = new CommonResponse<>(
                "400",
                message,
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 지원하지 않는 HTTP 메서드 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonResponse<Void>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException e) {
        log.warn("HTTP method not supported: {}", e.getMethod());

        CommonResponse<Void> response = new CommonResponse<>(
                "405",
                String.format("지원하지 않는 HTTP 메서드입니다: %s", e.getMethod()),
                null
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    // ===== JPA/Database 예외 핸들러 =====

    /**
     * 엔티티를 찾지 못한 경우 (404)
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> handleEntityNotFound(
            EntityNotFoundException e) {
        log.warn("Entity not found: {}", e.getMessage());

        CommonResponse<Void> response = new CommonResponse<>(
                "404",
                "요청한 리소스를 찾을 수 없습니다.",
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 데이터베이스 제약 조건 위반 (UNIQUE, FK 등)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonResponse<Void>> handleDataIntegrityViolation(
            DataIntegrityViolationException e) {
        log.warn("Data integrity violation: {}", e.getMessage());

        CommonResponse<Void> response = new CommonResponse<>(
                "409",
                "데이터 무결성 제약 조건을 위반했습니다. 중복된 데이터이거나 참조 무결성을 위반했습니다.",
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * 낙관적 락 충돌
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<CommonResponse<Void>> handleOptimisticLock(
            ObjectOptimisticLockingFailureException e) {
        log.warn("Optimistic locking failure: {}", e.getMessage());

        CommonResponse<Void> response = new CommonResponse<>(
                "409",
                "다른 사용자가 동시에 수정했습니다. 다시 시도해주세요.",
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // ===== 기본 예외 핸들러 (최종 방어선) =====

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleException(Exception e) {
        log.error("Unexpected error occurred", e);

        CommonResponse<Void> response = new CommonResponse<>(
                "500",
                "서버 내부 오류가 발생했습니다.",
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }



}