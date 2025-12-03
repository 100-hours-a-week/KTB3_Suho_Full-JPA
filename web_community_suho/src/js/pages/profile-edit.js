// ============== Import 문 =================

import { ERROR_MESSAGES, SIGNUP_HELPER_MESSAGE } from "../config/constants.js";
import { isValidNickname } from "../utils/validation.js";
import { showErrorHelperText, showHelperText } from "../utils/helperText.js";
import { initializeCsrfToken, getCsrfToken } from "../utils/csrf.js";

// ============== DOM 요소 참조 및 상수 =================

const uploadButton = document.getElementById("uploadButton");
const profileImageInput = document.getElementById("profileImageInput");
const profilePreview = document.getElementById("profilePreview");
const nicknameField = document.getElementById("nicknameField");
const nicknameHelperText = document.getElementById("nicknameHelperText");
const signupButton = document.getElementById("signupButton");
const form = document.getElementById("signupForm");
const emailDisplay = document.querySelector(".email-display");

let originalNickname = "";
let selectedImage = null;

// 모달 요소
const deleteModal = document.getElementById("deleteModal");
const modalCancelButton = document.getElementById("modalCancelButton");
const modalConfirmButton = document.getElementById("modalConfirmButton");
const deleteAccountButton = document.getElementById("deleteAccountButton");

// 토스트 요소
const toast = document.getElementById("toast");

// ============== 유틸리티 함수 =================

function showToast(message) {
    toast.textContent = message;
    toast.classList.add('show');

    setTimeout(() => {
        toast.classList.remove('show');
    }, 2000);
}

function showDeleteModal() {
    deleteModal.classList.add('show');
}

function hideDeleteModal() {
    deleteModal.classList.remove('show');
}

// ============== 이벤트 핸들러 함수 =================

function handleNicknameBlur() {
    const nickname = nicknameField.value;

    if (!nickname) {
        showErrorHelperText(nicknameHelperText, ERROR_MESSAGES.NICKNAME_EMPTY);
        return;
    }

    if (nickname.includes(" ")) {
        showErrorHelperText(nicknameHelperText, ERROR_MESSAGES.NICKNAME_WHITESPACE);
        return;
    }

    if (!isValidNickname(nickname)) {
        showErrorHelperText(nicknameHelperText, ERROR_MESSAGES.NICKNAME_TOO_LONG);
        return;
    }

    showHelperText(nicknameHelperText, SIGNUP_HELPER_MESSAGE.NICKNAME);
}

async function handleSubmit(e) {
    e.preventDefault();

    const userId = localStorage.getItem("userId");
    const nickname = nicknameField.value;

    try {
        const response = await fetch(`http://127.0.0.1:8080/api/v1/users/${userId}/nickname`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'X-XSRF-TOKEN': getCsrfToken()
            },
            credentials: 'include',
            body: JSON.stringify({
                nickname: nickname
            })
        });

        if (response.ok) {
            showToast("수정완료");
            originalNickname = nickname;
            signupButton.disabled = true;
            signupButton.classList.remove('active');
        } else {
            const data = await response.json();

            if (data.message && data.message.includes("닉네임")) {
                showErrorHelperText(nicknameHelperText, ERROR_MESSAGES.NICKNAME_DUPLICATED);
            } else {
                alert("회원정보 수정 실패: " + (data.message || "알 수 없는 오류"));
            }
        }
    } catch (error) {
        console.error("API 호출 에러:", error);
        alert("서버 연결 실패");
    }
}

// ============== API 호출 함수 =================

async function loadUserProfile() {
    const userId = localStorage.getItem("userId");

    try {
        const response = await fetch(`http://127.0.0.1:8080/api/v1/users/${userId}`, {
            method: 'GET',
            credentials: 'include'
        });

        if (response.ok) {
            const result = await response.json();
            const data = result.data;

            // 이메일 표시
            emailDisplay.textContent = data.email;

            // 닉네임 설정
            nicknameField.value = data.nickname;
            originalNickname = data.nickname;

            // 프로필 이미지 설정
            if (data.profileImageUrl) {
                profilePreview.src = data.profileImageUrl;
            }
        } else {
            console.error("프로필 정보 로드 실패");
        }
    } catch (error) {
        console.error("API 호출 에러:", error);
    }
}

async function deleteAccount() {
    const userId = localStorage.getItem("userId");

    try {
        const response = await fetch(`http://127.0.0.1:8080/api/v1/users/${userId}`, {
            method: 'DELETE',
            headers: {
                "Content-Type": "application/json",
                'X-XSRF-TOKEN': getCsrfToken()
            },
            credentials: 'include'
        });

        if (response.status === 204) {
            alert("회원탈퇴가 완료되었습니다.");
            localStorage.clear();
            window.location.href = "../auth/login.html";
        } else {
            alert("회원탈퇴 실패");
        }
    } catch (error) {
        console.error("API 호출 에러:", error);
        alert("서버 연결 실패");
    }
}

// ============== 버튼 상태 관리 함수 =================

function updateButtonState() {
    const nickname = nicknameField.value;

    // 닉네임이 유효하고, (닉네임이 변경되었거나 이미지가 선택됨)
    const isNicknameValid = nickname && isValidNickname(nickname) && !nickname.includes(" ");
    const isChanged = nickname !== originalNickname || selectedImage !== null;

    const isValid = isNicknameValid && isChanged;

    signupButton.disabled = !isValid;
    signupButton.classList.toggle('active', isValid);
}

// ============== 이벤트 리스너 등록 =================

uploadButton.addEventListener("click", () => {
    profileImageInput.click();
});

profileImageInput.addEventListener("change", (e) => {
    const file = e.target.files[0];
    if (file) {
        selectedImage = file;
        const reader = new FileReader();
        reader.onload = (e) => {
            profilePreview.src = e.target.result;
        };
        reader.readAsDataURL(file);
        updateButtonState();
    }
});

nicknameField.addEventListener("blur", handleNicknameBlur);
nicknameField.addEventListener("input", updateButtonState);
form.addEventListener("submit", handleSubmit);

deleteAccountButton.addEventListener("click", (e) => {
    e.preventDefault();
    showDeleteModal();
});

modalCancelButton.addEventListener("click", hideDeleteModal);
modalConfirmButton.addEventListener("click", () => {
    hideDeleteModal();
    deleteAccount();
});

// 모달 외부 클릭 시 닫기
deleteModal.addEventListener("click", (e) => {
    if (e.target === deleteModal) {
        hideDeleteModal();
    }
});

// ============== 초기 실행 코드 =================

loadUserProfile();

initializeCsrfToken();
