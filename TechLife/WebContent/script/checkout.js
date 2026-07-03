function toggleNuovoIndirizzo(mostra) {
    const formNuovo = document.getElementById("form-nuovo-indirizzo");
    if (formNuovo) {
        formNuovo.style.display = mostra ? "block" : "none";
        
        // Rendiamo i campi obbligatori solo se il form è visibile
        const inputs = formNuovo.querySelectorAll("input");
        inputs.forEach(input => {
            if(input.name !== "nuoveNote") {
                input.required = mostra;
            }
        });
    }
}