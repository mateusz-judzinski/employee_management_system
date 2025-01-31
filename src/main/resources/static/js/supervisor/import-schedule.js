    const dropZone = document.getElementById('drop-zone');
    const fileInput = document.getElementById('file-input');
    const hiddenFileInput = document.getElementById('hidden-file-input');
    const fileForm = document.getElementById('file-form');
    const fileInfo = document.getElementById('file-info');

    dropZone.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropZone.classList.add('dragover');
    });

    dropZone.addEventListener('dragleave', () => {
        dropZone.classList.remove('dragover');
    });

    dropZone.addEventListener('drop', (e) => {
        e.preventDefault();
        dropZone.classList.remove('dragover');
        const files = e.dataTransfer.files;

        if (files.length > 0) {
            handleFile(files[0]);
        }
    });

    dropZone.addEventListener('click', () => {
        fileInput.click();
    });

    fileInput.addEventListener('change', () => {
        if (fileInput.files.length > 0) {
            handleFile(fileInput.files[0]);
        }
    });

    function handleFile(file) {
        const dataTransfer = new DataTransfer();
        dataTransfer.items.add(file);
        hiddenFileInput.files = dataTransfer.files;

        fileForm.submit();
    }