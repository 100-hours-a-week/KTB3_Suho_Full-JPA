// ============== Import 문 =================

import { ERROR_MESSAGES } from "../config/constants.js";
import { isValidEmail, isValidPassword } from "../utils/validation.js";
import { showErrorHelperText, hideHelperText } from "../utils/helperText.js";

// ============== DOM 요소 참조 및 상수 =================

const emailField = document.getElementById("emailField");
const passwordField = document.getElementById("passwordField");
const helperText = document.getElementById("helperText");

let emailErrorMessage = null;
let passwordErrorMessage = null;

// ============== 유틸리티 함수 =================

function updateHelperText(element) {

    if (emailErrorMessage) {
        showErrorHelperText(element, emailErrorMessage);
    }

    else if (passwordErrorMessage) {
        showErrorHelperText(element, passwordErrorMessage);
    }

    else {
        hideHelperText(element);
    }

}

// ============== 이벤트 핸들러 함수 =================

function handleEmailBlur() {
    const email = emailField.value;

    // 이메일 필드 비었을때, 유효성 검증 실패
    if (!email || !isValidEmail(email)) {
        emailErrorMessage = ERROR_MESSAGES.EMAIL_INVALID;
    }
    // 이메일 유효성 검증 성공
    else {
        emailErrorMessage = null;
    }

    updateHelperText(helperText);
    updateButtonState();
}

function handlePasswordBlur() {

    const password = passwordField.value;

    // 비밀번호 필드 비었을때
    if (!password) {
        passwordErrorMessage = ERROR_MESSAGES.PASSWORD_EMPTY;
    }
    // 비밀번호 유효성 검증 실패
    else if (!isValidPassword(password)) {
        passwordErrorMessage = ERROR_MESSAGES.PASSWORD_INVALID;
    }
    // 비밀번호 유효성 검증 성공
    else {
        passwordErrorMessage = null;
    }

    updateHelperText(helperText);
    updateButtonState();

}

function handleInputChange() {
    updateButtonState();
}

async function handleLoginSubmit(e) {

    e.preventDefault();

    const email = emailField.value;
    const password = passwordField.value;

    try {
        const response = await fetch('http://127.0.0.1:8080/api/v1/login',{
            method: "POST",
            headers : {
                "Content-Type": "application/json",
            },
            credentials: 'include', // 쿠키 자동 저장/전송
            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        const data = await response.json();

        if (response.ok) {
            localStorage.setItem('userId', data.data);
            window.location.href = "../posts/list.html";
        }
        else {
            alert("로그인 실패" + (data.message || "알 수 없는 오류"));
        }

    } catch (error) {
        console.error("API 호출 에러:", error);
        alert("서버 연결 실패");
    }
}

// ============== 버튼 상태 관리 함수 =================

function updateButtonState() {

    const email = emailField.value;
    const password = passwordField.value;

    const isEmailValid = email && isValidEmail(email);
    const isPasswordValid = password && isValidPassword(password);
    const isValid = isEmailValid && isPasswordValid;

    const loginButton = document.getElementById("loginButton");
    loginButton.disabled = !isValid;
    loginButton.classList.toggle('active', isValid);

}

// ============== 이벤트 리스너 등록 =================

emailField.addEventListener("blur", handleEmailBlur);
emailField.addEventListener("input", handleInputChange);
passwordField.addEventListener("blur", handlePasswordBlur);
passwordField.addEventListener("input", handleInputChange);
document.getElementById("loginForm").addEventListener("submit", handleLoginSubmit);

