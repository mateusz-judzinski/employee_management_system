document.addEventListener("DOMContentLoaded", () => {
    const tabs = document.querySelectorAll(".tab-button");
    const contents = document.querySelectorAll(".tab-content");
    const datePickerContainer = document.querySelector(".date-picker-container");
    const searchForm = document.querySelector(".search-form");
    const addForm = document.getElementById("add-form"); // Formularz dodawania nowego elementu

    // Mapowanie data-tab do odpowiedniego endpointa
    const tabRoutes = {
        shifts: "/supervisor-panel/management/add-shift",
        employees: "/supervisor-panel/management/add-employee",
        positions: "/supervisor-panel/management/add-position",
        users: "/supervisor-panel/management/add-user",
        qualifications: "/supervisor-panel/management/add-qualification",
        skills: "/supervisor-panel/management/add-skill",
    };

    const searchPerformed = document.querySelector(".tab-button[data-tab='employees']").classList.contains("active");

    if (searchPerformed) {
        setActiveTab("employees");
    } else {
        setActiveTab("shifts");
    }

    tabs.forEach((tab) => {
        tab.addEventListener("click", () => {
            setActiveTab(tab.dataset.tab);
        });
    });

    function setActiveTab(activeTab) {
        tabs.forEach((t) => t.classList.remove("active"));
        contents.forEach((content) => content.classList.remove("active"));

        const activeTabButton = document.querySelector(`.tab-button[data-tab='${activeTab}']`);
        const activeTabContent = document.getElementById(activeTab);

        if (activeTabButton) activeTabButton.classList.add("active");
        if (activeTabContent) activeTabContent.classList.add("active");

        datePickerContainer.style.display = activeTab === "shifts" ? "flex" : "none";
        searchForm.style.display = activeTab === "employees" ? "flex" : "none";

        // Aktualizacja action formularza dodawania nowego elementu
        if (addForm && tabRoutes[activeTab]) {
            addForm.action = tabRoutes[activeTab];
        }
    }
});
