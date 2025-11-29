package kr.adapterz.jpa_suho.annotation.swagger;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 삭제 API (권한 검증 필요)
 * 참고: DELETE는 일반적으로 RequestBody가 없으므로 400 제외
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "인증 필요"),
        @ApiResponse(responseCode = "403", description = "권한 없음"),
        @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
})
public @interface DeleteApi {
}
