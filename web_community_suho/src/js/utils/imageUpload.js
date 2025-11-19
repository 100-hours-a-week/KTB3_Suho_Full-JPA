// 이미지 파일 업로드 처리

export function handleImageUpload(file, previewElement, onSuccess, onError) {
    if (file) {
        const reader = new FileReader();

        reader.onload = function (e) {
            previewElement.src = e.target.result;
            if (onSuccess) onSuccess(file, e.target.result);
        };

        reader.onerror = function () {
            if (onError) onError();
        };

        reader.readAsDataURL(file);
    } else {
        previewElement.src = "";
        if (onError) onError();
    }
}
