document.addEventListener('DOMContentLoaded', function () {
    const openPopupBtn = document.getElementById('add-temp-position-btn');
    const closePopupBtn = document.getElementById('close-popup-btn');
    const popupOverlay = document.getElementById('temp-position-form-overlay');

    if (openPopupBtn && closePopupBtn && popupOverlay) {
        openPopupBtn.addEventListener('click', function () {
            popupOverlay.style.display = 'flex';
        });

        closePopupBtn.addEventListener('click', function () {
            popupOverlay.style.display = 'none';
        });

        popupOverlay.addEventListener('click', function (e) {
            if (e.target === popupOverlay) {
                popupOverlay.style.display = 'none';
            }
        });
    }
});
