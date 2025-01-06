document.addEventListener('DOMContentLoaded', function () {
    const openPopupBtns = document.querySelectorAll('[id^="deactivate-link-"]');
    const closePopupBtn = document.getElementById('close-popup-btn');
    const popupOverlay = document.getElementById('deactivation-popup');
    const continueBtn = document.getElementById('continue-btn');

    let selectedPositionId = null;

    if (openPopupBtns.length && closePopupBtn && popupOverlay && continueBtn) {
        openPopupBtns.forEach(function (button) {
            button.addEventListener('click', function (e) {
                e.preventDefault();

                selectedPositionId = button.getAttribute('data-position-id');
                popupOverlay.style.display = 'flex';
            });
        });

        closePopupBtn.addEventListener('click', function () {
            popupOverlay.style.display = 'none';
            selectedPositionId = null;
        });

        popupOverlay.addEventListener('click', function (e) {
            if (e.target === popupOverlay) {
                popupOverlay.style.display = 'none';
                selectedPositionId = null;
            }
        });

        continueBtn.addEventListener('click', function () {
            if (selectedPositionId) {
                window.location.href = `/leader-panel/position/switch-active/${selectedPositionId}`;
            }
        });
    }
});
