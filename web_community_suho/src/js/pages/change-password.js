// ============== Import 문 =================

import { ERROR_MESSAGES, SIGNUP_HELPER_MESSAGE } from "../config/constants.js";
import { isValidPassword }  from "../utils/validation.js";
import { showErrorHelperText, hideHelperText, showHelperText } from "../utils/helperText.js";
import { getCsrfToken, initializeCsrfToken } from "../utils/csrf.js";

// ============== DOM 요소 참조 및 상수 =================

const passwordField = document.getElementById("passwordField");
const passwordConfirmField = document.getElementById("passwordConfirmField");

// ============== 이벤트 핸들러 함수 =================

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


function handleInputChange() {
    updateButtonState();
}

async function handleChangePassword(e) {
    e.preventDefault();

    const password = passwordField.value;
    const userId = localStorage.getItem("userId");

    try {
        const response = await fetch(`http://127.0.0.1:8080/api/v1/users/${userId}/password`, {
            method: 'PATCH',
            headers: {
                'Content-Type' : 'application/json',
                'X-XSRF-TOKEN' : getCsrfToken()
            },
            credentials: 'include', // 쿠키 자동 저장/전송
            body: JSON.stringify({
                password: password
            })
        });

        if (response.ok) {
            alert("비밀번호가 변경되었습니다!");
            window.location.href = "../posts/list.html";
        } else {
            alert("비밀번호 변경 실패");
        }
    } catch (error) {
        console.error("API 호출 에러:", error);
        alert("서버 연결 실패");
    }
}

// ============== 버튼 상태 관리 함수 =================

function updateButtonState() {
    const password = passwordField.value;
    const passwordConfirm = passwordConfirmField.value;

    const isPasswordValid = password && isValidPassword(password);
    const isPasswordMatch = password && passwordConfirm && password === passwordConfirm;

    const isValid =isPasswordValid && isPasswordMatch;

    const changeButton = document.getElementById("changeButton");
    changeButton.disabled = !isValid;
    changeButton.classList.toggle('active', isValid);
}

// ============== 이벤트 리스너 등록 =================

passwordField.addEventListener("blur", handlePasswordBlur);
passwordField.addEventListener("input", handleInputChange);

passwordConfirmField.addEventListener("blur", handlePasswordConfirmBlur);
passwordConfirmField.addEventListener("input", handleInputChange);


document.getElementById("changePasswordForm").addEventListener("submit", handleChangePassword);

// ============ 초기화 함수 =====================

initializeCsrfToken();