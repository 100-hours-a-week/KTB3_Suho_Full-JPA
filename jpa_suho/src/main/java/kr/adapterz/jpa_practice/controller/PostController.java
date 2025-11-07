package kr.adapterz.jpa_practice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kr.adapterz.jpa_practice.dto.common.CommonResponse;
import kr.adapterz.jpa_practice.dto.post.*;
import kr.adapterz.jpa_practice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "게시물 등록", description = "새로운 게시물을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<CommonResponse<CreatePostResponse>> create(
            @RequestBody CreatePostRequest request,
            @Parameter(hidden = true) @SessionAttribute("userId") Long userId) {

        Long postId = postService.create(userId, request.getTitle(), request.getContent(), request.getImageUrl());

        CommonResponse<CreatePostResponse> commonApiResponse = new CommonResponse<>(
                "201",
                "게시물이 정상적으로 등록됐습니다.",
                new CreatePostResponse(postId)
        );

        return ResponseEntity.status(201).body(commonApiResponse);

    }

    @GetMapping
    @Operation(summary = "게시물 목록 조회", description = "게시물 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<CommonResponse<GetPostsResponse>> getPosts(
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required= false) Long nextCursor
    ) {

        nextCursor = nextCursor == null ? Long.MAX_VALUE : nextCursor;

        GetPostsResponse postsResponse = postService.findWithSize(size, nextCursor);

        CommonResponse<GetPostsResponse> commonApiResponse = new CommonResponse<>("200", "게시물 목록을 정상적으로 불러왔습니다.", postsResponse);

        return ResponseEntity.status(200).body(commonApiResponse);

   }

    @PutMapping("/{postId}")
    @Operation(summary = "게시물 수정", description = "게시물의 제목, 내용, 이미지를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<CommonResponse<UpdatePostResponse>> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request, @Parameter(hidden = true) @SessionAttribute("userId") Long userId) {

        Long id = postService.update(postId, userId, request.getTitle(), request.getContent(), request.getPostImageUrl());

        CommonResponse<UpdatePostResponse> response = new CommonResponse<>("post_update_success", "게시물이 수정됐습니다.", new UpdatePostResponse(id));

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시물 삭제", description = "게시물ID를 통해 게시물을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "No Content")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @Parameter(hidden = true) @SessionAttribute("userId") Long userId) {

        postService.delete(postId, userId);

        return ResponseEntity.status(204).build();

    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시물 상세 조회", description = "게시물ID를 통해 게시물 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<CommonResponse<DetailPostResponse>> getPostById(
            @PathVariable Long postId,
            @Parameter(hidden = true) @SessionAttribute("userId") Long userId) {

        DetailPostResponse detailPostResponse = postService.findById(postId, userId);

        CommonResponse<DetailPostResponse> response = new CommonResponse<>("post_detail_success", "게시물(상세)를 정상적으로 불러왔습니다.", detailPostResponse);

        return ResponseEntity.status(200).body(response);
    }

    // TODO: 게시글 상세 댓글 추가

    // TODO: 게시글 목록 댓글수, 목록수, 조회수 추가



}
