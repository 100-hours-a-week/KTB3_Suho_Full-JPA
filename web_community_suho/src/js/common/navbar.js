// 네비게이션바 프로필 드롭다운 토글
document.addEventListener('DOMContentLoaded', () => {
    const profileButton = document.getElementById('profileButton');
    const profileDropdown = document.getElementById('profileDropdown');
    const logoutButton = document.getElementById('logoutButton');

    if (profileButton && profileDropdown) {
        // 프로필 클릭 시 드롭다운 토글
        profileButton.addEventListener('click', (e) => {
            e.stopPropagation();
            profileDropdown.classList.toggle('show');
        });

        // 문서 클릭 시 드롭다운 닫기
        document.addEventListener('click', () => {
            profileDropdown.classList.remove('show');
        });

        // 드롭다운 내부 클릭 시 이벤트 전파 방지
        profileDropdown.addEventListener('click', (e) => {
            e.stopPropagation();
        });
    }

    // 로그아웃 버튼
    if (logoutButton) {
        logoutButton.addEventListener('click', async (e) => {
            e.preventDefault();

            try {
                const response = await fetch('http://127.0.0.1:8080/api/v1/logout', {
                    method: 'POST',
                    credentials: 'include'
                });

                if (response.ok) {
                    localStorage.clear();
                    window.location.href = '../auth/login.html';
                } else {
                    alert('로그아웃 실패');
                }
            } catch (error) {
                console.error('로그아웃 에러:', error);
                alert('로그아웃 실패');
            }
        });
    }
});
