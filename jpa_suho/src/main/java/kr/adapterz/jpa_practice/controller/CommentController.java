package kr.adapterz.jpa_practice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.adapterz.jpa_practice.dto.comment.CommentRequest;
import kr.adapterz.jpa_practice.dto.comment.CommentResponse;
import kr.adapterz.jpa_practice.dto.common.CommonResponse;
import kr.adapterz.jpa_practice.service.CommentService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<CommonResponse<CommentResponse>> create(@PathVariable Long postId, @RequestBody CommentRequest request, @Parameter(hidden = true) @SessionAttribute("userId") Long userId) {

        Long commentId = commentService.create(postId, userId, request.getContent());

        CommonResponse<CommentResponse> response = new CommonResponse<>("comment_register_success", "댓글이 정상적으로 등록되었습니다.", new CommentResponse(postId, commentId));

        return ResponseEntity.status(201).body(response);

    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<CommonResponse<CommentResponse>> update(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentRequest request,
            @Parameter(hidden = true) @SessionAttribute("userId") Long userId) {
        Long id = commentService.update(userId, postId, commentId, request.getContent());
        CommentResponse response = new CommentResponse(postId, id);
        CommonResponse<CommentResponse> commonResponse = new CommonResponse<>("comment_update_success", "댓글이 수정됐습니다.", response);
        return ResponseEntity.status(200).body(commonResponse);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "No Content")
    public ResponseEntity<Void> delete (
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Parameter(hidden=true) @SessionAttribute("userId") Long userId) {

        commentService.delete(userId, postId, commentId);

        return ResponseEntity.status(204).build();

    }

}
