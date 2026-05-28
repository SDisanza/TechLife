document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector(".register-card");
    const btnSubmit = form.querySelector(".btn-register");
    
    if (!form) return;

    const regexFormati = {
        email: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
        cf: /^[A-Z]{6}[0-9]{2}[A-Z]{1}[0-9]{2}[A-Z]{1}[0-9]{3}[A-Z]{1}$/i,
        p_iva: /^[0-9]{11}$/
    };


    function convalidaAsincrona(inputElement, tipoControllo, messaggioDuplicato, messaggioFormatoErrato) {
        inputElement.addEventListener("blur", function () {
            const valore = inputElement.value.trim();
            
            const contenitore = inputElement.closest(".form-group");
            if (contenitore) {
                const vecchioErrore = contenitore.querySelector(".error-message-js");
                if (vecchioErrore) vecchioErrore.remove();
            }

            if (valore === "") {
                aggiornaStatoBottone();
                return;
            }

            const regex = regexFormati[tipoControllo];
            if (regex && !regex.test(valore)) {
                mostraErrore(inputElement, messaggioFormatoErrato);
                aggiornaStatoBottone();
                return; 
            }


            fetch("ValidaAjaxServlet", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: "tipo=" + tipoControllo + "&valore=" + encodeURIComponent(valore)
            })
            .then(response => response.json())
            .then(data => {
                if (data.esiste) {
                    mostraErrore(inputElement, messaggioDuplicato);
                }

                aggiornaStatoBottone();
            })
            .catch(error => {
                console.error("Errore AJAX:", error);
                aggiornaStatoBottone();
            });
        });
    }


    if (document.getElementById("email")) {
        convalidaAsincrona(document.getElementById("email"), "email", "Questa email è già registrata.", "Formato email non valido (es. nome@dominio.it).");
    }
    if (document.getElementById("cf")) {
        convalidaAsincrona(document.getElementById("cf"), "cf", "Questo Codice Fiscale è già registrato.", "Formato Codice Fiscale non valido. Inserire 16 caratteri nel formato corretto.");
    }
    if (document.getElementById("p_iva")) {
        convalidaAsincrona(document.getElementById("p_iva"), "p_iva", "Questa Partita IVA è già registrata.", "La Partita IVA deve essere composta da esattamente 11 cifre numeriche.");
    }

    function mostraErrore(inputElement, messaggio) {
        const contenitore = inputElement.closest(".form-group");
        if (contenitore && !contenitore.querySelector(".error-message-js")) {
            const erroreDiv = document.createElement("p");
            erroreDiv.className = "error-message-js";
            erroreDiv.textContent = messaggio;
            contenitore.appendChild(erroreDiv);
        }
    }


    function aggiornaStatoBottone() {
        const numeroErrori = document.querySelectorAll(".error-message-js").length;
        if (numeroErrori > 0) {
            btnSubmit.disabled = true;
            btnSubmit.style.opacity = "0.5";
            btnSubmit.style.cursor = "not-allowed";
        } else {
            btnSubmit.disabled = false;
            btnSubmit.style.opacity = "1";
            btnSubmit.style.cursor = "pointer";
        }
    }


    form.addEventListener("submit", function (event) {
        const numeroErrori = document.querySelectorAll(".error-message-js").length;
        if (numeroErrori > 0) {
            event.preventDefault();
            alert("Risolvi gli errori nel modulo prima di procedere con la registrazione.");
        }
    });
});