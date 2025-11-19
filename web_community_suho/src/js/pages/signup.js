// ============== Import 문 =================

import { ERROR_MESSAGES, SIGNUP_HELPER_MESSAGE } from "../config/constants.js";
import { isValidEmail, isValidPassword, isValidNickname } from "../utils/validation.js";
import { showErrorHelperText, hideHelperText, showHelperText } from "../utils/helperText.js";
import { handleImageUpload } from "../utils/imageUpload.js";

// ============== DOM 요소 참조 및 상수 =================

const profileImageInput = document.getElementById("profileImageInput");
const emailField = document.getElementById("emailField");
const passwordField = document.getElementById("passwordField");
const passwordConfirmField = document.getElementById("passwordConfirmField");
const nicknameField = document.getElementById("nicknameField");

// ============== 이벤트 핸들러 함수 =================

function handleUploadClick() {
    profileImageInput.click();
}

function handleImageChange(event) {
    const file = event.target.files[0];
    const profilePreview = document.getElementById("profilePreview");
    const helperText = document.getElementById("profileImageHelperText");

    handleImageUpload(
        file,
        profilePreview,
        () => {
            // 성공 시
            hideHelperText(helperText);
            updateButtonState();
        },
        () => {
            // 에러 또는 취소 시
            showErrorHelperText(helperText, ERROR_MESSAGES.PROFILE_IMAGE_EMPTY);
            updateButtonState();
        }
    );
}

function handleEmailBlur() {
    const email = emailField.value;
    const helperText = document.getElementById("emailHelperText");

    if (!email) {
        showErrorHelperText(helperText, ERROR_MESSAGES.EMAIL_EMPTY);
        return;
    }

    if (!isValidEmail(email)) {
        showErrorHelperText(helperText, ERROR_MESSAGES.EMAIL_INVALID);
        return;
    }

    // TODO: 중복 체크 API
    const isDuplicated = false;
    if (isDuplicated) {
        showErrorHelperText(helperText, ERROR_MESSAGES.EMAIL_DUPLICATED);
        return;
    }

    hideHelperText(helperText);
}

function handlePasswordBlur() {
    const password = passwordField.value;
    const helperText = document.getElementById("passwordHelperText");

    if (!password) {
        showErrorHelperText(helperText, ERROR_MESSAGES.PASSWORD_EMPTY);
        return;
    }

    if (!isValidPassword(password)) {
        showErrorHelperText(helperText, ERROR_MESSAGES.PASSWORD_INVALID);
        return;
    }

    showHelperText(helperText, SIGNUP_HELPER_MESSAGE.PASSWORD);
}

function handlePasswordConfirmBlur() {
    const password = passwordField.value;
    const passwordConfirm = passwordConfirmField.value;
    const helperText = document.getElementById("passwordConfirmHelperText");

    if (!passwordConfirm) {
        showErrorHelperText(helperText, ERROR_MESSAGES.PASSWORD_CONFIRM_EMPTY);
        return;
    }

    if (password !== passwordConfirm) {
        showErrorHelperText(helperText, ERROR_MESSAGES.PASSWORD_MISMATCH);
        return;
    }

    hideHelperText(helperText);
}

function handleNicknameBlur() {
    const nickname = nicknameField.value;
    const helperText = document.getElementById("nicknameHelperText");

    if (!nickname) {
        showErrorHelperText(helperText, ERROR_MESSAGES.NICKNAME_EMPTY);
        return;
    }

    if (/\s/.test(nickname)) {
        showErrorHelperText(helperText, ERROR_MESSAGES.NICKNAME_WHITESPACE);
        return;
    }

    if (nickname.length > 10) {
        showErrorHelperText(helperText, ERROR_MESSAGES.NICKNAME_TOO_LONG);
        return;
    }

    // TODO: 중복 체크 API
    const isDuplicated = false;
    if (isDuplicated) {
        showErrorHelperText(helperText, ERROR_MESSAGES.NICKNAME_DUPLICATED);
        return;
    }

    showHelperText(helperText, SIGNUP_HELPER_MESSAGE.NICKNAME);
}

function handleInputChange() {
    updateButtonState();
}

async function handleSignupSubmit(e) {
    e.preventDefault();

    const email = emailField.value;
    const password = passwordField.value;
    const nickname = nicknameField.value;

    try {
        const response = await fetch('http://127.0.0.1:8080/api/v1/users', {
            method: 'POST',
            headers: {
                'Content-Type' : 'application/json',
            },
            credentials: 'include', // 쿠키 자동 저장/전송
            body: JSON.stringify({
                email: email,
                password: password,
                nickname: nickname
            })
        });

        const data = await response.json();

        if(response.ok) {
            alert("회원가입이 완료되었습니다!");
            window.location.href = "./login.html";
        } else {
            alert("회원가입 실패: " + (data.message || "알 수 없는 오류"));
        }


    } catch (error) {
        console.error("API 호출 에러:", error);
    }

}

// ============== 버튼 상태 관리 함수 =================

function updateButtonState() {
    const profilePreview = document.getElementById("profilePreview");
    const email = emailField.value;
    const password = passwordField.value;
    const passwordConfirm = passwordConfirmField.value;
    const nickname = nicknameField.value;

    const hasProfileImage = profilePreview.src && profilePreview.src !== "";
    const isEmailValid = email && isValidEmail(email);
    const isPasswordValid = password && isValidPassword(password);
    const isPasswordMatch = password && passwordConfirm && password === passwordConfirm;
    const isNicknameValid = nickname && isValidNickname(nickname);

    const isValid = hasProfileImage && isEmailValid && isPasswordValid && isPasswordMatch && isNicknameValid;

    const signupButton = document.getElementById("signupButton");
    signupButton.disabled = !isValid;
    signupButton.classList.toggle('active', isValid);
}

// ============== 이벤트 리스너 등록 =================

document.getElementById("uploadButton").addEventListener("click", handleUploadClick);
profileImageInput.addEventListener("change", handleImageChange);

emailField.addEventListener("blur", handleEmailBlur);
emailField.addEventListener("input", handleInputChange);

passwordField.addEventListener("blur", handlePasswordBlur);
passwordField.addEventListener("input", handleInputChange);

passwordConfirmField.addEventListener("blur", handlePasswordConfirmBlur);
passwordConfirmField.addEventListener("input", handleInputChange);

nicknameField.addEventListener("blur", handleNicknameBlur);
nicknameField.addEventListener("input", handleInputChange);

document.getElementById("signupForm").addEventListener("submit", handleSignupSubmit);
