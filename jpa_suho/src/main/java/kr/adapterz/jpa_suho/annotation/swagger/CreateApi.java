package kr.adapterz.jpa_suho.annotation.swagger;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 생성 API (리소스 중복, 부모 리소스 없을 수 있음)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 필요"),
        @ApiResponse(responseCode = "404", description = "부모 리소스를 찾을 수 없음"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 리소스"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
})
public @interface CreateApi {
}
