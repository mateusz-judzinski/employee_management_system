document.addEventListener("DOMContentLoaded", function () {
    let deleteForm;
    const deleteModal = document.getElementById("deleteConfirmModal");
    const errorModal = document.getElementById("errorModal");
    const deletePasswordInput = document.getElementById("deletePassword");
    const confirmButton = document.getElementById("confirmDelete");
    const closeDeleteButton = document.getElementById("closeModal");
    const closeErrorButton = document.getElementById("closeErrorModal");
    const deleteWarningMessage = document.getElementById("deleteWarningMessage");
    const errorMessageElement = document.getElementById("errorMessageText");

    const warningMessages = {
        "employee": "⚠️ Uwaga! Usunięcie spowoduje trwałe zmiany na pracowniku i jego powiązaniach.",
        "user": "⚠️ Uwaga! Usunięcie spowoduje trwałe zmiany na liderze i jego powiązaniach.",
        "shift": "⚠️ Uwaga! Usunięcie spowoduje trwałe zmiany na grafiku i jego powiązaniach.",
        "qualification": "⚠️ Uwaga! Usunięcie spowoduje trwałe zmiany na kwalifikacji i jej powiązaniach.",
        "skill": "⚠️ Uwaga! Usunięcie spowoduje trwałe zmiany na umiejętności i jej powiązaniach.",
        "position": "⚠️ Uwaga! Usunięcie spowoduje trwałe zmiany na stanowisku i jego powiązaniach."
    };

    const errorMessageContainer = document.getElementById("errorMessageContainer");
    if (errorMessageContainer && errorMessageContainer.textContent.trim() !== "") {
        errorMessageElement.textContent = errorMessageContainer.textContent;
        errorModal.style.display = "block";
    }

    document.querySelectorAll(".delete-button").forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();
            deleteForm = this.closest("form");
            let entityType = deleteForm.getAttribute("data-entity-type");

            deleteWarningMessage.textContent = entityType && warningMessages[entityType]
                ? warningMessages[entityType]
                : "⚠️ Czy na pewno chcesz usunąć ten element?";

            deleteModal.style.display = "block";
        });
    });

    confirmButton.addEventListener("click", function () {
        if (deletePasswordInput.value.trim() === "") {
            alert("Wpisz hasło, aby potwierdzić usunięcie.");
            return;
        }

        const passwordInput = document.createElement("input");
        passwordInput.type = "hidden";
        passwordInput.name = "password";
        passwordInput.value = deletePasswordInput.value;
        deleteForm.appendChild(passwordInput);

        deleteForm.submit();
        deleteModal.style.display = "none";
        deletePasswordInput.value = "";
    });

    closeDeleteButton.addEventListener("click", function () {
        deleteModal.style.display = "none";
        deletePasswordInput.value = "";
    });

    closeErrorButton.addEventListener("click", function () {
        errorModal.style.display = "none";
    });

    window.onclick = function (event) {
        if (event.target === deleteModal) {
            deleteModal.style.display = "none";
            deletePasswordInput.value = "";
        }
        if (event.target === errorModal) {
            errorModal.style.display = "none";
        }
    };
});
