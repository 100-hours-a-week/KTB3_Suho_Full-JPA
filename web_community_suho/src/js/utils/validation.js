// 이메일 유효성 검사 성공: true / 실패: false
export function isValidEmail(email) {
    // 정규표현식
    // - [a-zA-Z0-9._-] : id(영문, 숫자, 점, 언더스코어, 하이픈)
    // - @ : @기호
    // - [a-zA-Z0-9.-] : 도메인 이름(영문, 숫자, 점, 언더스코어, 하이픈)
    // - \. : 점
    // - [a-zA-Z]{2,} : 최상위 도메인 (com, kr 등, 최소 2글자)
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
}

// 비밀번호 유효성 검사 성공: true / 실패: false
export function isValidPassword(password) {
    // 정규표현식
    // - (?=.*[a-z]) : 소문자 최소 1개  (lookahead)
    // - (?=.*[A-Z]) : 대문자 최소 1개 (lookahead)
    // - (?=.*\d) : 숫자 최소 1개 (lookahead)
    // - (?=.*[@$!%*?&]) : 특수문자 최소 1개 (lookahead)
    // - [A-Za-z\d@$!%*?&]{8,20} : 허용된 문자로 8~20자
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;
    return passwordRegex.test(password);
}

// 닉네임 유효성 검사 성공: true / 실패: false
export function isValidNickname(nickname) {
    // 정규표현식
    // - \s : 공백 체크 (있으면 안 됨)
    if (/\s/.test(nickname)) {
        return false;
    }
    // 길이 체크: 1~10자
    if (nickname.length < 1 || nickname.length > 10) {
        return false;
    }
    return true;
}
