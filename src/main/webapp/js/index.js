document.getElementById("btnLogin").addEventListener("click", function (event) {
    event.preventDefault();

    // Obtener valores de los campos
    const usuario = document.getElementById("logiClie").value.trim();
    const clave = document.getElementById("passClie").value.trim();

    if (usuario === "" || clave === "") {
        alert("Por favor, complete todos los campos.");
        return;
    }

    // Codifica la clave como WordArray con exactamente 8 bytes (igual que Java espera)
    const claveSecreta = "12345678"; // Debe ser exactamente 8 caracteres

    const key = CryptoJS.enc.Latin1.parse(claveSecreta); // CAMBIO IMPORTANTE

    const claveCifrada = CryptoJS.DES.encrypt(
            clave,
            key,
            {
                mode: CryptoJS.mode.ECB,
                padding: CryptoJS.pad.Pkcs7
            }
    ).toString();


    console.log("Clave cifrada DES (Base64):", claveCifrada);

    // Enviar datos al servlet con el contexto correcto (/Fact/login)
    fetch("/Fact/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `logiClie=${encodeURIComponent(usuario)}&passClie=${encodeURIComponent(claveCifrada)}`
    })
            .then(response => response.text())
            .then(data => {
                console.log("Respuesta del servidor:", data);
                if (data === "OK") {
                    window.location.href = "principal.html";
                } else {
                    alert("Credenciales incorrectas.");
                }
            })
            .catch(error => {
                console.error("Error en el login:", error);
            });
});