// Helper Text 표시/숨김 함수

export function hideHelperText(element) {
    element.classList.add("hidden");
    element.classList.remove("error");
}

export function showErrorHelperText(element, message) {
    element.textContent = `* ${message}`;
    element.classList.add("error");
    element.classList.remove("hidden");
}

export function showHelperText(element, message) {
    element.textContent = `* ${message}`;
    element.classList.remove("error");
    element.classList.remove("hidden");
}
