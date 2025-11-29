/**
 * 로그인 필요 모달 유틸리티
 */

let modalLoaded = false;
let modal = null;
let cancelButton = null;
let confirmButton = null;

/**
 * 모달 HTML을 로드하고 초기화
 */
async function loadModal() {
    if (modalLoaded) return;

    try {
        const response = await fetch('/src/components/login-required-modal.html');
        if (!response.ok) {
            throw new Error('Failed to load modal');
        }

        const html = await response.text();
        document.body.insertAdjacentHTML('beforeend', html);

        modal = document.getElementById('loginRequiredModal');
        cancelButton = document.getElementById('loginModalCancelButton');
        confirmButton = document.getElementById('loginModalConfirmButton');

        // 취소 버튼 - 모달 닫기
        cancelButton.addEventListener('click', hideLoginRequiredModal);

        // 확인 버튼 - 로그인 페이지로 이동
        confirmButton.addEventListener('click', () => {
            // 현재 페이지 경로를 redirect 파라미터로 전달
            const currentPath = encodeURIComponent(window.location.pathname + window.location.search);
            window.location.href = `/pages/auth/login.html?redirect=${currentPath}`;
        });

        // 모달 외부 클릭 시 닫기
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                hideLoginRequiredModal();
            }
        });

        modalLoaded = true;
    } catch (error) {
        console.error('Error loading login required modal:', error);
    }
}

/**
 * 로그인 필요 모달 표시
 */
export async function showLoginRequiredModal() {
    await loadModal();
    if (modal) {
        modal.classList.add('show');
    }
}

/**
 * 로그인 필요 모달 숨기기
 */
export function hideLoginRequiredModal() {
    if (modal) {
        modal.classList.remove('show');
    }
}

/**
 * 로그인 상태 확인
 * @returns {boolean} 로그인 여부
 */
export function isLoggedIn() {
    // localStorage에 userId 있는지 확인
    return localStorage.getItem('userId') !== null;
}

/**
 * 로그인이 필요한 작업 실행
 * @param {Function} callback - 로그인 되어있을 때 실행할 콜백
 */
export async function requireLogin(callback) {
    if (isLoggedIn()) {
        callback();
    } else {
        await showLoginRequiredModal();
    }
}
