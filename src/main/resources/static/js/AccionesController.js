function buscarAcciones(nombreAccion) {
    axios.get('/acciones/search/' + nombreAccion).then(function (response) {
        document.getElementById("Options").innerHTML = "";
        var data = response.data["bestMatches"];
        for (var i in data) {
            var options = document.getElementById("Options");
            var a = document.createElement("A");
            a.setAttribute("id", data[i][Object.keys(data[i])[0]]);
            a.setAttribute("onclick", "setAccion(this.id)");
            a.setAttribute("href", "#");
            a.innerHTML = data[i][Object.keys(data[i])[1]];
            options.appendChild(a);
            options.appendChild(document.createElement("BR"));
        }
    }).catch(function (error) {
        console.log(error);
        alert("Caracteres no validos");
    });
}

function getAcciones(tipo, nombreAccion) {
    axios.get('/acciones/time_series_' + tipo + '/' + nombreAccion).then(function (response) {
        document.getElementById("Tabla").innerHTML = "";
        document.getElementById("Data").innerHTML = "";

        var data = response.data;
        var metaDataKeys = Object.keys(data)[0];
        var timeSeries = Object.keys(data)[1];

        //Crear Meta datos
        var metaData = "{ ";
        for (i in data[metaDataKeys]) {
            metaData += i + ": " + data[metaDataKeys][i] + ",  -  ";
        }
        metaData += "}";

        //Agregar meta datos
        var dataMark = document.getElementById("Data");
        dataMark.setAttribute("class", "d-flex justify-content-center row alert alert-info")
        dataMark.innerHTML = metaData;

        //Crear tabla <table> https://getbootstrap.com/docs/4.0/content/tables/
        var table = document.createElement("TABLE");
        table.setAttribute("class", "table");

        //Crear encabezado
        var thead = document.createElement("THEAD");
        thead.setAttribute("class", "thead-light");

        //Crear fila
        var tr = document.createElement("TR");

        //Crear columnas encabezados
        var encabezados = ["Fecha", "Open", "Hight", "Low", "Close", "Volume"];
        for (e in encabezados) {
            var th = document.createElement("TH");
            th.setAttribute("scope", "col");
            th.innerHTML = encabezados[e];
            tr.appendChild(th);
        }

        //Agregar las columnas de los encabezados al encabezado. Y agregar este ultimo a la tabla.
        thead.appendChild(tr);
        table.appendChild(thead);

        //Crear cuerpo
        var tbody = document.createElement("TBODY");

        //Agregar filas
        for (i in data[timeSeries]) {
            //Crear fila
            tr = document.createElement("TR");

            th = document.createElement("TH");
            th.innerHTML = i;
            tr.appendChild(th);

            //Agregar columnas a la fila antes creada
            for (j in data[timeSeries][i]) {
                td = document.createElement("TD");
                td.innerHTML = data[timeSeries][i][j];
                tr.appendChild(td);
            }
            tbody.appendChild(tr);
        }
        table.appendChild(tbody);
        document.getElementById("Tabla").appendChild(table);
    }).catch(function (error) {
        console.log(error);
        alert("Tipo y accion no encontradas");
    });
}

function setAccion(nombreAccion) {
    document.getElementById("PalabraClave").value = nombreAccion;
}