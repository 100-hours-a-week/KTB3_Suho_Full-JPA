package kr.adapterz.jpa_suho.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import kr.adapterz.jpa_suho.dto.comment.CommentRequest;
import kr.adapterz.jpa_suho.dto.comment.CommentResponse;
import kr.adapterz.jpa_suho.dto.common.CommonResponse;
import kr.adapterz.jpa_suho.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 등록", description = "새로운 댓글을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<CommonResponse<CommentResponse>> create(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request,
            @Parameter(hidden = true) @SessionAttribute("userId") Long userId) {

        Long commentId = commentService.create(postId, userId, request.getContent());

        CommonResponse<CommentResponse> response = new CommonResponse<>(
                "COMMENT_CREATE_SUCCESS",
                "댓글이 정상적으로 등록되었습니다.",
                new CommentResponse(postId, commentId)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<CommonResponse<CommentResponse>> update(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            @Parameter(hidden = true) @SessionAttribute("userId") Long userId) {

        Long id = commentService.update(userId, postId, commentId, request.getContent());

        CommonResponse<CommentResponse> response = new CommonResponse<>(
                "COMMENT_UPDATE_SUCCESS",
                "댓글이 수정됐습니다.",
                new CommentResponse(postId, id)
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "No Content")
    public ResponseEntity<Void> delete(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Parameter(hidden = true) @SessionAttribute("userId") Long userId) {

        commentService.delete(userId, postId, commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
