document.addEventListener('DOMContentLoaded', function () {
    const openPopupBtns = document.querySelectorAll('[id^="deactivate-link-"]');
    const closePopupBtn = document.getElementById('close-popup-btn');
    const popupOverlay = document.getElementById('deactivation-popup');

    if (openPopupBtns.length && closePopupBtn && popupOverlay) {
        openPopupBtns.forEach(function (button) {
            button.addEventListener('click', function (e) {
                e.preventDefault();

                const positionId = button.getAttribute('data-position-id');
                document.getElementById('position-id').value = positionId;

                popupOverlay.style.display = 'flex';
            });
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
