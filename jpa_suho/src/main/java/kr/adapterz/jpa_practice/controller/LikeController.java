package kr.adapterz.jpa_practice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.adapterz.jpa_practice.dto.common.CommonResponse;
import kr.adapterz.jpa_practice.dto.like.CreateLikeResponse;
import kr.adapterz.jpa_practice.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    @Operation(summary = "좋아요 추가", description = "게시물에 좋아요를 추가합니다.")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<CommonResponse<CreateLikeResponse>> createLike(
            @PathVariable Long postId,
            @Parameter(hidden = true) @SessionAttribute("userId") Long userId) {

        int likeCount = likeService.createLike(postId, userId);

        CommonResponse<CreateLikeResponse> response = new CommonResponse<>(
                "like_created",
                "좋아요가 추가되었습니다.",
                new CreateLikeResponse(postId, likeCount)
        );

        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping
    @Operation(summary = "좋아요 취소", description = "게시물의 좋아요를 취소합니다.")
    @ApiResponse(responseCode = "204", description = "No Content")
    public ResponseEntity<CommonResponse<Void>> deleteLike(
            @PathVariable Long postId,
            @Parameter(hidden = true) @SessionAttribute("userId") Long userId) {

        likeService.deleteLike(postId, userId);

        return ResponseEntity.status(204).build();
    }

}
