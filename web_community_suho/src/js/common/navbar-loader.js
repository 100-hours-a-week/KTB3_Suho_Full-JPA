const response = await fetch('/src/components/navbar.html');
const html = await response.text();

document.body.insertAdjacentHTML('afterbegin', html);

const hideBack = document.body.getAttribute('data-navbar-back') === 'false';
const hideProfile = document.body.getAttribute('data-navbar-profile') === 'false';

// 뒤로가기 버튼 숨기기 옵션
if (hideBack) {
    document.querySelector('.navbar-back').style.visibility = 'hidden';
}
// 프로필 메뉴 숨기기 옵션
if (hideProfile) {
    document.querySelector('.navbar-profile').style.display = 'none';
    document.querySelector('.navbar-login').style.display = 'none';
} else {
    if (localStorage.getItem("userId")) {
        await import('./navbar.js');
        document.querySelector('.navbar-login').style.display = 'none';
    } else {    
        document.querySelector('.navbar-profile').style.display = 'none';
    }
}
