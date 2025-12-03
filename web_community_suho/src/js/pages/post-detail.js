// ============== Import 문 =================

import { formatDate } from "../utils/formatter.js";
import { requireLogin } from "../utils/auth-modal.js";
import { getCsrfToken, initializeCsrfToken } from "../utils/csrf.js";

// ============== DOM 요소 참조 및 상수 =================

const urlParams = new URLSearchParams(window.location.search);
const postId = urlParams.get('postId');

const contentField = document.getElementById('contentField');
const commentSubmitButton = document.getElementById('commentSubmitButton');

// 모달 요소
const deleteModal = document.getElementById('deleteModal');
const modalTitle = document.getElementById('modalTitle');
const modalCancelButton = document.getElementById('modalCancelButton');
const modalConfirmButton = document.getElementById('modalConfirmButton');

let postData = null;
let editingCommentId = null; // 수정 중인 댓글 ID
let deleteCallback = null; // 삭제 확인 시 실행할 콜백

// ============== 유틸리티 함수 =================

function showDeleteModal(title, callback) {
    modalTitle.textContent = title;
    deleteCallback = callback;
    deleteModal.classList.add('show');
}

function hideDeleteModal() {
    deleteModal.classList.remove('show');
    deleteCallback = null;
}

function renderPostDetail(post) {
    document.getElementById('postTitle').textContent = post.title;
    document.getElementById('authorName').textContent = post.writerInfo.nickname;
    document.getElementById('postDate').textContent = formatDate(post.createdAt);
    document.getElementById('postContent').textContent = post.content;
    document.getElementById('postLikes').textContent = post.detailStatistics.likeCount;
    document.getElementById('postViews').textContent = post.detailStatistics.viewCount;
    document.getElementById('postComments').textContent = post.detailStatistics.commentCount;

    // 좋아요 상태 반영
    const likeButton = document.getElementById('likeButton');
    if (post.detailStatistics.liked) {
        likeButton.classList.add('liked');
    } else {
        likeButton.classList.remove('liked');
    }

    // 수정/삭제 버튼 권한 체크
    const currentUserId = localStorage.getItem('userId');
    const isOwner = currentUserId && parseInt(currentUserId) === post.writerInfo.user_id;

    const editButton = document.getElementById('editButton');
    const deleteButton = document.getElementById('deleteButton');

    if (!isOwner) {
        editButton.style.display = 'none';
        deleteButton.style.display = 'none';
    } else {
        editButton.style.display = 'block';
        deleteButton.style.display = 'block';
    }

    renderComments(post.comments);
}

function renderComments(comments) {
    const commentsList = document.getElementById('commentsList');
    commentsList.innerHTML = '';

    if (!comments || comments.length === 0) {
        commentsList.innerHTML = '<p>댓글이 없습니다.</p>';
        return;
    }

    comments.forEach(comment => {
        const commentDiv = document.createElement('div');
        commentDiv.className = 'comment-item';

        commentDiv.innerHTML = `
            <img src="../../src/images/dummy/dog.jpg" class="profile-image profile-image-sm">
            <div class="comment-body">
                <div class="comment-header">
                    <div class="comment-info">
                        <span class="comment-author">${comment.writerInfo.nickname}</span>
                        <span class="comment-date">${formatDate(comment.createdAt)}</span>
                    </div>
                    <div class="comment-actions">
                        <button class="commentEditButton action-button">수정</button>
                        <button class="commentDeleteButton action-button">삭제</button>
                    </div>
                </div>
                <p class="comment-content">${comment.content}</p>
            </div>
        `;

        // 수정/삭제 버튼 권한 체크
        const currentUserId = localStorage.getItem('userId');
        const isOwner = currentUserId && parseInt(currentUserId) === comment.writerInfo.user_id;

        const editButton = commentDiv.querySelector('.commentEditButton');
        const deleteButton = commentDiv.querySelector('.commentDeleteButton');

        if (!isOwner) {
            editButton.style.display = 'none';
            deleteButton.style.display = 'none';
        } else {
            editButton.style.display = 'block';
            deleteButton.style.display = 'block';

            // 댓글 수정 버튼 클릭 이벤트
            editButton.addEventListener('click', () => {
                editingCommentId = comment.commentId;
                contentField.value = comment.content;
                commentSubmitButton.textContent = '댓글 수정';
                contentField.focus();
                updateCommentButtonState();
            });

            deleteButton.addEventListener('click', () => {
                deleteComment(comment.commentId);
            });
        }

        commentsList.appendChild(commentDiv);
    });
}

// ============== 이벤트 핸들러 함수 =================

function editPost() {
    if (postData) {
        const { title, content } = postData;
        window.location.href = `../posts/edit.html?postId=${postId}&title=${encodeURIComponent(title)}&content=${encodeURIComponent(content)}`;
    }
}

async function deletePost() {
    showDeleteModal('게시글을 삭제하시겠습니까?', async () => {
        try {
            const response = await fetch(`http://127.0.0.1:8080/api/v1/posts/${postId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    'X-XSRF-TOKEN': getCsrfToken()
                },
                credentials: 'include'
            });

            if (response.ok) {
                alert("정상적으로 삭제됐습니다!");
                window.location.replace("../posts/list.html");
            } else {
                const data = await response.json();
                alert('게시글 삭제 실패: ' + (data.message || '알 수 없는 오류'));
            }
        } catch (error) {
            console.error('API 호출 에러:', error);
            alert('서버 연결 실패');
        }
    });
}

async function toggleLike() {
    const likeButton = document.getElementById('likeButton');
    const isLiked = likeButton.classList.contains('liked');

    try {
        const method = isLiked ? 'DELETE' : 'POST';
        const response = await fetch(`http://127.0.0.1:8080/api/v1/posts/${postId}/likes`, {
            method: method,
            headers: {
                "Content-Type": "application/json",
                'X-XSRF-TOKEN': getCsrfToken()
            },
            credentials: 'include'
        });

        if (response.ok) {
            // 좋아요 상태 토글
            if (isLiked) {
                likeButton.classList.remove('liked');
            } else {
                likeButton.classList.add('liked');
            }

            // 좋아요 수 업데이트
            const currentCount = parseInt(document.getElementById('postLikes').textContent);
            document.getElementById('postLikes').textContent = isLiked ? currentCount - 1 : currentCount + 1;
        } else {
            const data = await response.json();
            alert('좋아요 처리 실패: ' + (data.message || '알 수 없는 오류'));
        }
    } catch (error) {
        console.error('API 호출 에러:', error);
        alert('서버 연결 실패');
    }
}

async function deleteComment(commentId) {
    showDeleteModal('댓글을 삭제하시겠습니까?', async () => {
        try {
            const response = await fetch(`http://127.0.0.1:8080/api/v1/posts/${postId}/comments/${commentId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    'X-XSRF-TOKEN': getCsrfToken()
                },
                credentials: 'include',
            });

            if (response.ok) {
                alert('댓글이 삭제되었습니다!');
                window.location.reload();
            } else {
                const data = await response.json();
                alert('댓글 삭제 실패: ' + (data.message || '알 수 없는 오류'));
            }
        } catch (error) {
            console.error('API 호출 에러:', error);
            alert('서버 연결 실패');
        }
    });
}

async function createOrUpdateComment() {
    const content = contentField.value.trim();

    if (!content) {
        alert("댓글 내용을 입력해주세요.");
        return;
    }

    try {
        let response;

        // 수정 모드인 경우
        if (editingCommentId) {
            response = await fetch(`http://127.0.0.1:8080/api/v1/posts/${postId}/comments/${editingCommentId}`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                    'X-XSRF-TOKEN': getCsrfToken()
                },
                credentials: 'include',
                body: JSON.stringify({
                    content: content
                })
            });
        }
        // 등록 모드인 경우
        else {
            response = await fetch(`http://127.0.0.1:8080/api/v1/posts/${postId}/comments`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'X-XSRF-TOKEN': getCsrfToken()
                },
                credentials: 'include',
                body: JSON.stringify({
                    content: content
                })
            });
        }

        const data = await response.json();

        if (response.ok) {
            alert(editingCommentId ? "댓글이 수정되었습니다!" : "댓글이 등록되었습니다!");
            contentField.value = '';
            editingCommentId = null;
            commentSubmitButton.textContent = '댓글 등록';
            // 페이지 새로고침으로 댓글 목록 갱신
            window.location.reload();
        } else {
            alert((editingCommentId ? "댓글 수정 실패: " : "댓글 등록 실패: ") + (data.message || "알 수 없는 오류"));
        }
    }
    catch (error) {
        console.error("API 호출 에러:", error);
        alert("서버 연결 실패");
    }
}

// ============== API 호출 함수 =================

async function fetchPostDetail() {
    try {
        const response = await fetch(`http://127.0.0.1:8080/api/v1/posts/${postId}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                'X-XSRF-TOKEN': getCsrfToken()
            },
            credentials: 'include'
        });

        const data = await response.json();

        if (response.ok) {
            postData = data.data;
            renderPostDetail(postData);
        }
    } catch (error) {
        console.error(error);
    }
}

// ============== 버튼 상태 관리 함수 =================

function updateCommentButtonState() {
    const content = contentField.value.trim();
    const isValid = content.length > 0;

    commentSubmitButton.disabled = !isValid;
}

// ============== 이벤트 리스너 등록 =================

modalCancelButton.addEventListener('click', hideDeleteModal);
modalConfirmButton.addEventListener('click', () => {
    if (deleteCallback) {
        deleteCallback();
    }
    hideDeleteModal();
});

commentSubmitButton.addEventListener('click', () => {
    requireLogin(createOrUpdateComment);
});
contentField.addEventListener('input', updateCommentButtonState);

document.getElementById('editButton').addEventListener('click', editPost);
document.getElementById('deleteButton').addEventListener('click', deletePost);
document.getElementById('likeButton').addEventListener('click', () => {
    requireLogin(toggleLike);
});

// ============== 초기 실행 코드 =================

fetchPostDetail();
updateCommentButtonState();
initializeCsrfToken();
