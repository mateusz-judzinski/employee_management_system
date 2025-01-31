document.addEventListener("DOMContentLoaded", () => {
    const tabs = document.querySelectorAll(".tab-button");
    const contents = document.querySelectorAll(".tab-content");
    const datePickerContainer = document.querySelector(".date-picker-container");
    const searchForm = document.querySelector(".search-form");
    const addForm = document.getElementById("add-form");
    const addButton = document.getElementById("add-button");
    const importIcon = document.getElementById("import-icon");

    const tabRoutes = {
        shifts: "/supervisor-panel/management/add-shift",
        employees: "/supervisor-panel/management/add-employee",
        positions: "/supervisor-panel/management/add-position",
        users: "/supervisor-panel/management/add-user",
        qualifications: "/supervisor-panel/management/add-qualification",
        skills: "/supervisor-panel/management/add-skill",
    };

    const tabTexts = {
        shifts: "Dodaj nową zmianę",
        employees: "Dodaj nowego pracownika",
        positions: "Dodaj nowe stanowisko",
        users: "Dodaj nowego lidera",
        qualifications: "Dodaj nową kwalifikację",
        skills: "Dodaj nową umiejętność",
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

        if (addForm && addButton) {
            addForm.action = tabRoutes[activeTab] || "#";
            addButton.textContent = tabTexts[activeTab] || "Dodaj nowy element";
        }

        if (importIcon) {
            importIcon.style.display = activeTab === "shifts" ? "inline-block" : "none";
        }
    }
});
