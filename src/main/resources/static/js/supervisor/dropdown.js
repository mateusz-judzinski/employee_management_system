document.addEventListener("DOMContentLoaded", function () {
    $('.select2').select2({
        width: '100%',
        placeholder: "Wybierz opcje",
        allowClear: true
    });

    function setupSelectAll(selectElement) {
        if (selectElement.find('option[value="select-all"]').length === 0) {
            selectElement.prepend('<option value="select-all">Zaznacz wszystko</option>');
        }

        selectElement.on("select2:select", function (e) {
            if (e.params.data.id === "select-all") {
                selectElement.val(selectElement.find('option:not(:first)').map(function () {
                    return $(this).val();
                }).get()).trigger("change");
            }
        });

        selectElement.on("select2:unselect", function (e) {
            if (e.params.data.id === "select-all") {
                selectElement.val(null).trigger("change");
            }
        });
    }

    setupSelectAll($("#employees"));
    setupSelectAll($("#positions"));
});
