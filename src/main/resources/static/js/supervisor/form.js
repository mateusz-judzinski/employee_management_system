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

     select.addEventListener("mousedown", function(event) {
         event.preventDefault();
         this.size = 10;
         this.focus();
     });

     select.addEventListener("change", function() {
         this.size = 1;
         setTimeout(() => this.blur(), 100);
     });

     select.addEventListener("blur", function() {
         this.size = 1;
     });

     document.querySelectorAll("#employee option").forEach(option => {
         option.addEventListener("click", function() {
             select.value = this.value;
             select.dispatchEvent(new Event("change"));
         });
     });
 });








