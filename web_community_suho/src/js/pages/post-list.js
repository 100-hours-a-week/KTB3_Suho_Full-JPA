// ============== Import 문 =================

import { formatDate } from '../utils/formatter.js'

// ============== DOM 요소 참조 및 상수 =================

const postList = document.getElementById("boardList");

let currentCursor = null;
let observer = null;

// ============== 유틸리티 함수 =================

function createPostElement(postData) {
    const article = document.createElement("article");
    article.className = "post-item";

    article.innerHTML = `
        <h3 class="post-title">${postData.title}</h3>

        <div class="post-meta">
            <span>좋아요</span><span class="post-likes">${postData.likeCount}</span>
            <span>댓글</span><span class="post-comments">${postData.commentCount}</span>
            <span>조회수</span><span class="post-views">${postData.viewCount}</span>
            <span class="post-date">${postData.date}</span>
        </div>
        <hr class="divider">
        <div class="post-author">
            <img src="../../src/images/dummy/dog.jpg" class="profile-image profile-image-sm">
            <span class="author-name">${postData.author}</span>
        </div>
    `;
    article.setAttribute('data-post-id', postData.postId)

    return article;
}

function updateObserver() {
    if (observer) {
        observer.disconnect();
    }

    const posts = postList.querySelectorAll('.post-item');

    if (posts.length >= 3) {
        const thirdFromLast = posts[posts.length - 3];

        observer = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting && currentCursor) {
                renderPostList();
            }
        });

        observer.observe(thirdFromLast);
    }
}

// ============== API 호출 함수 =================

async function renderPostList() {

    try {

        let url = 'http://127.0.0.1:8080/api/v1/posts?size=20';
        if (currentCursor) {
            url += `&nextCursor=${currentCursor}`
        }

        const response = await fetch(url, {
            method: 'GET',
            headers: {
                "Content-Type": "application/json"
            },
            credentials: 'include'
        });

        const data = await response.json();

        if (response.ok) {

            currentCursor = data.data.pagination.nextCursor;

            const posts = data.data.posts;

            // 각 post를 화면에 렌더링
            posts.forEach(post => {

                const postData = {
                    postId: post.postId,
                    title: post.title,
                    author: post.writerInfo.nickname,
                    profileImage: post.writerInfo.profileImageUrl,
                    likeCount: post.likeCount,
                    commentCount: post.commentCount,
                    viewCount: post.viewCount,
                    date: formatDate(post.createdAt)
                };

                const postElement = createPostElement(postData);
                postList.appendChild(postElement);
            });

            updateObserver();
        }
        else {
            alert("게시글 목록 조회 실패: " + (data.message || "알 수 없는 오류"));
        }

    } catch (error) {
        alert("서버 연결 실패");
    }

}

// ============== 이벤트 리스너 등록 =================

postList.addEventListener("click", (e) => {
    const postItem = e.target.closest('.post-item');
    if(postItem) {
        const postId = postItem.dataset.postId;
        window.location.href = `./detail.html?postId=${postId}`;
    }
})

// ============== 초기 실행 코드 =================

renderPostList();
