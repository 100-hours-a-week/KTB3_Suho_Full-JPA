// ============== Import 문 =================

import { ERROR_MESSAGES } from "../config/constants.js";
import { getCsrfToken, initializeCsrfToken } from "../utils/csrf.js";
import { hideHelperText, showHelperText, showErrorHelperText } from "../utils/helperText.js";
import { handleImageUpload } from "../utils/imageUpload.js";

// ============== DOM 요소 참조 및 상수 =================

const titleField = document.getElementById("titleField");
const contentField = document.getElementById("contentField");
const imageInput = document.getElementById("imageInput");
const imageUploadButton = document.getElementById("imageUploadButton");
const imagePreview = document.getElementById("imagePreview");
const imagePreviewContainer = document.getElementById("imagePreviewContainer");
const submitButton = document.getElementById("submitButton");
const imageHelperText = document.getElementById("imageHelperText");

// ============== 이벤트 핸들러 함수 =================

function handleImageUploadClick() {
    imageInput.click();
}

function handleImageChange(event) {
    const file = event.target.files[0];

    handleImageUpload(
        file,
        imagePreview,
        () => {
            // 성공 시
            imagePreviewContainer.classList.remove('hidden');
            hideHelperText(imageHelperText);
        },
        () => {
            // 에러 또는 취소 시
            imagePreviewContainer.classList.add('hidden');
            showHelperText(imageHelperText, ERROR_MESSAGES.IMAGE_FILE_SELECT);
        }
    );
}

function handleInputChange() {
    updateButtonState();
}

async function handleSubmit(e) {
    e.preventDefault();

    const title = titleField.value.trim();
    const content = contentField.value.trim();
    const contentHelperText = document.getElementById("contentHelperText");

    // 제목이나 내용이 비어있으면 에러 표시
    if (!title || !content) {
        showErrorHelperText(contentHelperText, ERROR_MESSAGES.POST_FORM_INCOMPLETE);
        return;
    }

    // 에러 메시지 숨김
    hideHelperText(contentHelperText);

    try {
        const response = await fetch('http://127.0.0.1:8080/api/v1/posts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-XSRF-TOKEN': getCsrfToken()
            },
            credentials: 'include',
            body: JSON.stringify({
                title: title,
                content: content
            })
        });

        const data = await response.json();

        if (response.ok) {
            alert("게시글이 등록되었습니다!");
            window.location.href = "./list.html";
        } else {
            alert("게시글 등록 실패: " + (data.message || "알 수 없는 오류"));
            initializeCsrfToken();
        }
    } catch (error) {
        console.error("API 호출 에러:", error);
        alert("서버 연결 실패");
    }
}

// ============== 버튼 상태 관리 함수 =================

function updateButtonState() {
    const title = titleField.value.trim();
    const content = contentField.value.trim();

    const isValid = title && content;

    if (isValid) {
        submitButton.style.backgroundColor = '#E56922';
    } else {
        submitButton.style.backgroundColor = '#D1D5DB';
    }
}

// ============== 이벤트 리스너 등록 =================

imageUploadButton.addEventListener("click", handleImageUploadClick);
imageInput.addEventListener("change", handleImageChange);

titleField.addEventListener("input", handleInputChange);
contentField.addEventListener("input", handleInputChange);

document.getElementById("postCreateForm").addEventListener("submit", handleSubmit);

// ============== 초기 실행 코드 =================

// 페이지 로드 시 이미지 헬퍼 텍스트 표시
showHelperText(imageHelperText, ERROR_MESSAGES.IMAGE_FILE_SELECT);

// 초기 버튼 색상 설정
submitButton.style.backgroundColor = '#D1D5DB';

//CSRF 토큰 초기화
 (async () => {
      await initializeCsrfToken();
  })();
