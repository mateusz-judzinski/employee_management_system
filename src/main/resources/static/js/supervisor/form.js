document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("nextStepBtn").addEventListener("click", nextStep);
    document.getElementById("prevStepBtn").addEventListener("click", prevStep);
});

function nextStep() {
    document.getElementById("step1").style.display = "none";
    document.getElementById("step2").style.display = "block";

    document.getElementById("nav-step1").style.display = "none";
    document.getElementById("nav-step2").style.display = "block";
}

function prevStep() {
    document.getElementById("step1").style.display = "block";
    document.getElementById("step2").style.display = "none";

    document.getElementById("nav-step1").style.display = "block";
    document.getElementById("nav-step2").style.display = "none";
}


 document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll('.form-check-input').forEach(input => {
            input.checked = false;
        });
    });



 document.addEventListener("DOMContentLoaded", function() {
     const select = document.getElementById("employee");

     select.addEventListener("click", function() {
         this.size = 5;
     });

     select.addEventListener("blur", function() {
         this.size = 1;
     });
 });




